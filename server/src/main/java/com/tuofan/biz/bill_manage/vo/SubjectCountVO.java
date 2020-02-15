package com.tuofan.biz.bill_manage.vo;

import lombok.Data;

import java.util.Map;

@Data
public class SubjectCountVO {

    private Integer inDateCnt;

    private Integer outDateCnt;

    public static SubjectCountVO of(Integer inDateCount,Integer outDateCount){
        SubjectCountVO ele = new SubjectCountVO();
        ele.setInDateCnt(inDateCount);
        ele.setOutDateCnt(outDateCount);
        return ele;
    }

}
