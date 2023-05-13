package com.ruizhi.tech.sentencecenter.pojo.vo;

import lombok.Data;

import java.util.List;

/**
 * TODO
 *
 * @author EDY
 * @date 2023/4/12 17:46
 */
@Data
public class SentenceVo {
    private String content;

    private List<String> tags;
}
