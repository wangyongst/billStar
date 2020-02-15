package com.tuofan.biz.bill_manage.vo;

import lombok.Data;

import java.util.List;

@Data
public class SysDictQuery {

    private String type;

    private List<Integer> parentIds;


}
