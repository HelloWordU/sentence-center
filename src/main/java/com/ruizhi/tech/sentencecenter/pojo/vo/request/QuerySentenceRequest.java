package com.ruizhi.tech.sentencecenter.pojo.vo.request;

import lombok.Data;

import java.util.List;

@Data
public class QuerySentenceRequest {
    private List<String> tags;
}
