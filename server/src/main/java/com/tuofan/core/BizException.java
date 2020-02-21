package com.tuofan.core;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
public class BizException extends RuntimeException {

    private String code;

    private Object msg;

    public BizException(String code, Object message) {
        this.code = code;
        this.msg = message;
    }

    public BizException(Exception e) {
        super(e);
    }

}
