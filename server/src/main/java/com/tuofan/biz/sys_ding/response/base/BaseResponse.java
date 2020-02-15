package com.tuofan.biz.sys_ding.response.base;

import lombok.Data;

import java.io.Serializable;

@Data
public class BaseResponse<T> implements Serializable {

    public static int OK = 0;

    private int errcode;

    private String errmsg;

    private T result;

    public String toErrString() {
        return String.format("ErrCode:%s,ErrMsg:%s", this.getErrcode(), this.getErrmsg());
    }

}
