package com.ruizhi.tech.sentencecenter.service;

import java.io.IOException;
import java.io.StringReader;

import com.ruizhi.tech.sentencecenter.dao.SentenceESDao;
import com.ruizhi.tech.sentencecenter.es.entity.SentenceEntity;
import com.ruizhi.tech.sentencecenter.pojo.vo.request.AddSentenceRequest;
import org.apache.lucene.analysis.Analyzer;
import org.springframework.stereotype.Service;
import org.wltea.analyzer.cfg.Configuration;
import org.wltea.analyzer.cfg.DefaultConfig;
import org.wltea.analyzer.core.IKSegmenter;
import org.wltea.analyzer.core.Lexeme;
import org.wltea.analyzer.dic.Dictionary;
import org.wltea.analyzer.lucene.IKAnalyzer;
import org.apache.lucene.analysis.TokenStream;
import org.apache.lucene.analysis.tokenattributes.CharTermAttribute;
import org.apache.lucene.analysis.tokenattributes.OffsetAttribute;
import org.apache.lucene.analysis.tokenattributes.TypeAttribute;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

/**
 * TODO
 *
 * @author EDY
 * @date 2023/4/12 18:40
 */
@Service
public class SentenceService {

    @Resource
    private SentenceESDao sentenceESDao;

    public void add(AddSentenceRequest addSentenceRequest) {
        SentenceEntity sentenceEntity = new SentenceEntity();

        sentenceEntity.setId(UUID.randomUUID().toString());

        sentenceEntity.setTags(getSentenceKeys(addSentenceRequest.getContent()));
        sentenceEntity.setContent(addSentenceRequest.getContent());
        sentenceESDao.insert(sentenceEntity);
    }

    public List<String> getSentenceKeys(String content) {
        List<String> res = new ArrayList<>();
        List<String> expandWords = Arrays.asList("华为", "天际通", "流量券");
        Configuration cfg = DefaultConfig.getInstance();
        cfg.setUseSmart(true);
        //Dictionary.initial(cfg);
        cfg.getExtDictionarys().addAll(expandWords);

        StringReader reader = new StringReader(content);
        IKSegmenter ikSegmenter = new IKSegmenter(reader, cfg);
        try {
            Lexeme lex;
            while ((lex = ikSegmenter.next()) != null) {
                String word = lex.getLexemeText();
                if (word.length() >= 2) { //取出的词至少#{MIN_LEN}个字
                   // countMap.put(word, countMap.getOrDefault(word, 0) + 1);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        Analyzer analyzer = new IKAnalyzer(cfg);
        //获取Lucene的TokenStream对象
        TokenStream ts = null;
        try {
            ts = analyzer.tokenStream("myfield",
                    new StringReader(content));
            //获取词元位置属性
            OffsetAttribute offset = ts.addAttribute(OffsetAttribute.class);
            //获取词元文本属性
            CharTermAttribute term = ts.addAttribute(CharTermAttribute.class);
            //获取词元文本属性
            TypeAttribute type = ts.addAttribute(TypeAttribute.class);
            ts.reset();
            //迭代获取分词结果
            while (ts.incrementToken()) {
                if (type.type().equals("CN_WORD")) {
                    res.add(term.toString());
                }
                System.out.println(
                        offset.startOffset() + " - " + offset.endOffset() + " : " + term.toString() + " | " + type
                                .type());
            }
            //关闭TokenStream（关闭StringReader）
            ts.end();   // Perform end-of-stream operations, e.g. set the final offset.

        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            //释放TokenStream的所有资源
            if (ts != null) {
                try {
                    ts.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return res;
    }
}
