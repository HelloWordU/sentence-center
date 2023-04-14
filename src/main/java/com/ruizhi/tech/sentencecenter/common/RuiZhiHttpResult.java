package com.ruizhi.tech.sentencecenter.common;

import lombok.Data;

/**
 * TODO
 *
 * @author EDY
 * @date 2023/4/12 18:04
 */
@Data
public class RuiZhiHttpResult<T> {
    private T data;

    private Integer code;

    private String message;

    public RuiZhiHttpResult() {
        this.code = 200;
        this.message = "";
    }
}
