package com.tuofan.core.dto;

import com.fasterxml.jackson.annotation.JsonInclude;

import java.io.Serializable;

/**
 * Description: 请求的返回类型封装JSON结果
 *
 * @author admin
 * @version 1.0
 * @date 2018/5/24 14:36
 * @since JDK 1.8
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
public class BaseResult<T> implements Serializable {

    private static final long serialVersionUID = -4185151304730685014L;

    private static final String SUCCESS = "0";

    private String code;

    protected T data;

    private String msg;


    public static <T> BaseResult<T> buildSuccessResult() {
        return new BaseResult<T>();
    }

    public static <T> BaseResult<T> buildSuccessResult(String msg) {
        return new BaseResult<T>(SUCCESS, msg);
    }

    /**
     * 直接返回成功的baseResult
     */
    public static <T> BaseResult<T> success(T data) {
        BaseResult<T> baseResult = new BaseResult<T>();
        baseResult.setData(data);
        return baseResult;
    }

    public BaseResult() {
        this(SUCCESS, "操作成功");
    }

    public BaseResult(String code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    public static BaseResult buildFailResult(String msg) {
        return new BaseResult("500", msg);
    }


    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public boolean isSuccess() {
        return SUCCESS.equals(code);
    }

    @Override
    public String toString() {
        return "BaseResult [code=" + code + ", msg=" + msg + ", data=" + data + "]";
    }
}
