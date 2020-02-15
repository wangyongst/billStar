package com.tuofan.biz.bill_manage.vo.bill_new_student;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;

@Data
public class UpdateBillStudentCmd {

    @NotNull
    @ApiModelProperty("ID")
    private Integer id;

    @NotNull
    @ApiModelProperty("姓名")
    private String name;

    @NotNull
    @ApiModelProperty("手机号")
    private String mobile;

    @NotNull
    @ApiModelProperty("学校；注意，该学校为学生就读学校")
    private String schoolName;

    @NotNull
    @ApiModelProperty("班级")
    private String className;

    @NotNull
    @ApiModelProperty("住址")
    private String address;
}
