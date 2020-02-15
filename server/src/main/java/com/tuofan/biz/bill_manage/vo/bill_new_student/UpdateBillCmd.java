package com.tuofan.biz.bill_manage.vo.bill_new_student;

import com.tuofan.biz.bill_manage.vo.UpdateStudentCmd;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.validation.Valid;
import java.util.List;

@Data
@EqualsAndHashCode(callSuper = true)
@ApiModel(value = "CreateBillCmd", description = "新生来了，开票")
public class UpdateBillCmd extends BaseBillCmd {

    @ApiModelProperty("转班|转校 需要用到")
    public Integer modelId;

    @ApiModelProperty("转校需要用到")
    public Integer newDeptSchoolId;

    @Valid
    UpdateStudentCmd student;

    List<CreateBillCourseCmd> billCourseList;

}
