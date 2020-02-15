package com.tuofan.biz.bill_manage.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class SubjectStudentDTO {

    @ApiModelProperty("校区ID")
    private Integer deptSchoolId;

    @ApiModelProperty("科目ID")
    private Integer subjectId;

    @ApiModelProperty("在校总数")
    private Integer inDateCnt;

    @ApiModelProperty("超期总数")
    private Integer outDateCnt;

    @ApiModelProperty("校区名")
    private String deptSchoolName;

}
