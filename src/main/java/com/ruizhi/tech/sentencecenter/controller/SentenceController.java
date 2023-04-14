package com.ruizhi.tech.sentencecenter.controller;

import com.ruizhi.tech.sentencecenter.common.RuiZhiHttpResult;
import com.ruizhi.tech.sentencecenter.pojo.vo.request.AddSentenceRequest;
import com.ruizhi.tech.sentencecenter.service.SentenceService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

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
    public RuiZhiHttpResult<Boolean> add(@RequestBody AddSentenceRequest addSentenceRequest) {
        RuiZhiHttpResult<Boolean> res = new RuiZhiHttpResult<>();
        try {
            sentenceService.add(addSentenceRequest);
        } catch (Exception e) {

        }
        return res;
    }
}
