package com.ruizhi.tech.sentencecenter.service;

import java.io.File;
import java.io.IOException;
import java.io.StringReader;

import cn.hutool.core.io.FileUtil;
import com.huaban.analysis.jieba.JiebaSegmenter;
import com.huaban.analysis.jieba.SegToken;
import com.huaban.analysis.jieba.WordDictionary;
import com.ruizhi.tech.sentencecenter.dao.SentenceESDao;
import com.ruizhi.tech.sentencecenter.es.entity.SentenceEntity;
import com.ruizhi.tech.sentencecenter.pojo.vo.request.AddSentenceRequest;
import com.ruizhi.tech.sentencecenter.pojo.vo.request.QuerySentenceRequest;
import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.core.KeywordAnalyzer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
//import org.wltea.analyzer.cfg.Configuration;
//import org.wltea.analyzer.cfg.DefaultConfig;
//import org.wltea.analyzer.core.IKSegmenter;
//import org.wltea.analyzer.core.Lexeme;
//import org.wltea.analyzer.dic.Dictionary;
//import org.wltea.analyzer.lucene.IKAnalyzer;
import org.apache.lucene.analysis.TokenStream;
import org.apache.lucene.analysis.tokenattributes.CharTermAttribute;
import org.apache.lucene.analysis.tokenattributes.OffsetAttribute;
import org.apache.lucene.analysis.tokenattributes.TypeAttribute;
import org.springframework.validation.annotation.Validated;

import javax.annotation.Resource;
import java.nio.charset.Charset;
import java.nio.file.FileSystems;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

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

    @Value("${rz.jieba.dic:H:/dict.txt}")
    private String jieDicPath;

    @Value("${rz.jieba.stop:H:/stopwords.txt}")
    private String jieStopPath;

    public void add(AddSentenceRequest addSentenceRequest) {
        SentenceEntity sentenceEntity = new SentenceEntity();
        sentenceEntity.setProjectId(addSentenceRequest.getProjectId());
        sentenceEntity.setId(UUID.randomUUID().toString());
        sentenceEntity.setTags(getSentenceKeys(addSentenceRequest.getContent()));
        sentenceEntity.setContent(addSentenceRequest.getContent());
        sentenceESDao.insert(sentenceEntity);
    }

    public List<String> getSentenceKeys(String content) {
        content = content.toLowerCase();
        content = content.replaceAll("\\d+", "");
        content = content.replaceAll("[\\w\\s]", "");
        List<String> res = new ArrayList<>();
        Path path = FileSystems.getDefault().getPath(jieDicPath);
        WordDictionary.getInstance().loadUserDict(path);
        List<String> stop_words = FileUtil.readLines(new File(jieStopPath), "UTF-8"); //FileUtils.readLines(new File(jieStopPath));
        JiebaSegmenter segmenter = new JiebaSegmenter();
        res = segmenter.sentenceProcess(content);
        res = res.stream().map(o -> o.trim()).filter(o -> !stop_words.contains(o)).distinct().collect(Collectors.toList());
        return res;
    }
//    public List<String> getSentenceKeys2(String content) {
//        List<String> res = new ArrayList<>();
////        List<String> expandWords = Arrays.asList("华为", "天际通", "流量券");
////        Configuration cfg = DefaultConfig.getInstance();
////        cfg.setUseSmart(true);
////        //Dictionary.initial(cfg);
////        cfg.getExtDictionarys().addAll(expandWords);
////
////        StringReader reader = new StringReader(content);
////        IKSegmenter ikSegmenter = new IKSegmenter(reader, cfg);
////        try {
////            Lexeme lex;
////            while ((lex = ikSegmenter.next()) != null) {
////                String word = lex.getLexemeText();
////                if (word.length() >= 2) { //取出的词至少#{MIN_LEN}个字
////                    // countMap.put(word, countMap.getOrDefault(word, 0) + 1);
////                }
////            }
////        } catch (Exception e) {
////            e.printStackTrace();
////        }
//       // Analyzer analyzer = new IKAnalyzer(cfg);
//        Analyzer analyzer = new KeywordAnalyzer();
//        //获取Lucene的TokenStream对象
//        TokenStream ts = null;
//        try {
//            ts = analyzer.tokenStream("myfield",
//                    new StringReader(content));
//            //获取词元位置属性
//            OffsetAttribute offset = ts.addAttribute(OffsetAttribute.class);
//            //获取词元文本属性
//            CharTermAttribute term = ts.addAttribute(CharTermAttribute.class);
//            //获取词元文本属性
//            TypeAttribute type = ts.addAttribute(TypeAttribute.class);
//            ts.reset();
//            //迭代获取分词结果
//            while (ts.incrementToken()) {
//                if (type.type().equals("CN_WORD")) {
//                    res.add(term.toString());
//                }
//                System.out.println(
//                        offset.startOffset() + " - " + offset.endOffset() + " : " + term.toString() + " | " + type
//                                .type());
//            }
//            //关闭TokenStream（关闭StringReader）
//            ts.end();   // Perform end-of-stream operations, e.g. set the final offset.
//
//        } catch (IOException e) {
//            e.printStackTrace();
//        } finally {
//            //释放TokenStream的所有资源
//            if (ts != null) {
//                try {
//                    ts.close();
//                } catch (IOException e) {
//                    e.printStackTrace();
//                }
//            }
//        }
//        return res;
//    }

    public List<SentenceEntity> query(QuerySentenceRequest addSentenceRequest) {
        List<SentenceEntity> res = new ArrayList<>();
        res = sentenceESDao.query(addSentenceRequest);
        return res;
    }

    public List<String> getContentWords(AddSentenceRequest addSentenceRequest) {
        Path path = FileSystems.getDefault().getPath(jieDicPath);
        WordDictionary.getInstance().loadUserDict(path);
        JiebaSegmenter segmenter = new JiebaSegmenter();
        List<SegToken> process = segmenter.process(addSentenceRequest.getContent(), JiebaSegmenter.SegMode.SEARCH);
        List<String> words = process.stream().map(i -> i.word).distinct().collect(Collectors.toList());
        return words;
    }

}
