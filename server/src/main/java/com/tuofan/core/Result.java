package com.tuofan.core;

import lombok.Data;

@Data
public class Result<T> {
    private int code;
    private T message;

    public static Result ok() {
        Result res = new Result();
        res.setCode(1);
        return res;
    }

    public static Result ok(Object obj) {
        Result res = new Result();
        res.setCode(1);
        res.message = obj;
        return res;
    }

    public static Result error(Object obj) {
        Result res = new Result();
        res.setCode(0);
        res.message = obj;
        return res;
    }
}
