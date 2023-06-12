package com.ruizhi.tech.sentencecenter.pojo.vo.request;

import lombok.Data;

/**
 * TODO
 *
 * @author EDY
 * @date 2023/4/12 18:07
 */
@Data
public class AddSentenceRequest {
    private  String content;
    private Long projectId;
}
