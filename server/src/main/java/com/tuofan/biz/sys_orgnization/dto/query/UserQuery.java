package com.tuofan.biz.sys_orgnization.dto.query;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

@Data
public class UserQuery {

//    private Integer greaterThanId;

    private List<Integer> ids;

    private List<String> userIds;

    @ApiModelProperty("部门ID")
    private List<Integer> deptIds;

    @ApiModelProperty("姓名")
    private String name;

    @ApiModelProperty("%姓名%")
    private String nameLike;

    @ApiModelProperty("手机号")
    private String mobile;

    private Integer pageSize = 50;

    private Integer isTeacher;

    private boolean stopQuery ;

    public UserQuery(){
        this.setStopQuery(false);
    }

}
