package com.ruizhi.tech.sentencecenter.controller;

import com.ruizhi.tech.sentencecenter.common.ResultEntity;
import com.ruizhi.tech.sentencecenter.common.ResultEntityList;
import com.ruizhi.tech.sentencecenter.es.entity.SentenceEntity;
import com.ruizhi.tech.sentencecenter.pojo.vo.SentenceVo;
import com.ruizhi.tech.sentencecenter.pojo.vo.request.AddSentenceRequest;
import com.ruizhi.tech.sentencecenter.pojo.vo.request.QuerySentenceRequest;
import com.ruizhi.tech.sentencecenter.service.SentenceService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

/**
 * TODO
 *
 * @author EDY
 * @date 2023/4/12 17:46
 */
@Slf4j
@RestController
@RequestMapping("/api/v1/sentence")
public class SentenceController {

    @Resource
    private SentenceService sentenceService;

    @PostMapping("/add")
    public ResultEntity<Boolean> add(@RequestBody AddSentenceRequest addSentenceRequest) {
        ResultEntity<Boolean> res = new ResultEntity<>();
        try {
            sentenceService.add(addSentenceRequest);
        } catch (Exception e) {

        }
        return res;
    }


    @PostMapping("/query")
    public ResultEntityList<SentenceVo> query(@RequestBody QuerySentenceRequest addSentenceRequest) {
        ResultEntityList<SentenceVo> res = new ResultEntityList<>();
        try {
            List<SentenceEntity> sentenceEntities = sentenceService.query(addSentenceRequest);
            List<SentenceVo> result = new ArrayList<>();
            for (SentenceEntity item : sentenceEntities) {
                SentenceVo newItem = new SentenceVo();
                BeanUtils.copyProperties(item, newItem);
                result.add(newItem);
            }
            res.setData(result);
        } catch (Exception e) {
            res.setCode(0);
            res.setMessage(e.getMessage());
            log.error("query error ", e);
        }
        return res;
    }

    @PostMapping("/getContentKeys")
    public ResultEntityList<String> getContentKeys(@RequestBody AddSentenceRequest addSentenceRequest) {
        ResultEntityList<String> res = new ResultEntityList<>();
        try {
            List<String> sentenceEntities = sentenceService.getContentWords(addSentenceRequest);
            res.setData(sentenceEntities);
        } catch (Exception e) {
            res.setCode(0);
            res.setMessage(e.getMessage());
            log.error("query error ", e);
        }
        return res;
    }
    @PostMapping("/getESSentenceKeys")
    public ResultEntityList<String> getESSentenceKeys(@RequestBody AddSentenceRequest addSentenceRequest) {
        ResultEntityList<String> res = new ResultEntityList<>();
        try {
            List<String> sentenceEntities = sentenceService.getSentenceKeys(addSentenceRequest.getContent());
            res.setData(sentenceEntities);
        } catch (Exception e) {
            res.setCode(0);
            res.setMessage(e.getMessage());
            log.error("query error ", e);
        }
        return res;
    }


}
