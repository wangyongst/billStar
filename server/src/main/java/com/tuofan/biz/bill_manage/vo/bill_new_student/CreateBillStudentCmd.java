package com.tuofan.biz.bill_manage.vo.bill_new_student;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;

@Data
public class CreateBillStudentCmd {

    @NotNull(message = "姓名不能为空")
    @ApiModelProperty("姓名")
    private String name;

    @NotNull(message = "手机号不能为空")
    @ApiModelProperty("手机号")
    private String mobile;

    @NotNull(message = "性别不能为空")
    @ApiModelProperty("1=男，2=女")
    private Integer gender;

    @NotNull(message = "学校不能为空")
    @ApiModelProperty("学校；注意，该学校为学生就读学校")
    private String schoolName;

    @NotNull(message = "班级不能为空")
    @ApiModelProperty("班级")
    private String className;

    @NotNull(message = "住址不能为空")
    @ApiModelProperty("住址")
    private String address;
}
