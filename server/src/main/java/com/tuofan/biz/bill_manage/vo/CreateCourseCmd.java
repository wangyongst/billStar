package com.tuofan.biz.bill_manage.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;

@Data
public class CreateCourseCmd {

    @NotNull(message = "请选择关联教师")
    @ApiModelProperty("关联教师ID")
    private Integer teacherId;

    @NotNull(message = "请选择班级")
    @ApiModelProperty("字典表-班级ID（启蒙）")
    private Integer dictCourseId;//

    @NotNull(message = "请选择班别")
    @ApiModelProperty("班别（1,2,3,4,5,6...10）")
    private Integer courseIndex;//

    @NotNull(message = "请输入上课时间")
    @ApiModelProperty("上课时间（每周三10点到12点）")
    private String regularTime;//

    @NotNull(message = "请输入教室号")
    @ApiModelProperty("//教室号")
    private String classroomNo;

    @NotNull(message = "请输入校区")
    @ApiModelProperty("校区ID")
    private Integer deptSchoolId;

    @NotNull(message = "请输入学生人数")
    @ApiModelProperty("学生人数")
    private Integer studentNumber;

    @NotNull(message = "请选择学期")
    @ApiModelProperty("字典-学期ID")
    private Integer semesterId;

}
