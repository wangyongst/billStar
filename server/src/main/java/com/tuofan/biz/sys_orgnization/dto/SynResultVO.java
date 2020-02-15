package com.tuofan.biz.sys_orgnization.dto;

import lombok.Data;

@Data
public class SynResultVO {

    private String modelName;

    private int insertCnt;

    private int updateCnt;

    private String msg;

    public SynResultVO() {

    }

    public SynResultVO(String modelName, int insertCnt, int updateCnt, String msg) {
        this.modelName = modelName;
        this.insertCnt = insertCnt;
        this.updateCnt = updateCnt;
        this.msg = msg;
    }


    public SynResultVO(String msg) {
        this(null, 0, 0, msg);
    }
}
