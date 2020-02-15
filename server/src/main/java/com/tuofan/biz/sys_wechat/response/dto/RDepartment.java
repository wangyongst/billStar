package com.tuofan.biz.sys_wechat.response.dto;

import lombok.Data;

@Data
public class RDepartment {

    private Integer id;
    private String name;
    private Integer parentid;
    private Integer order;
}
