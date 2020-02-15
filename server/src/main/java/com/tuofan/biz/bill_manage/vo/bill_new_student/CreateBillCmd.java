package com.tuofan.biz.bill_manage.vo.bill_new_student;

import io.swagger.annotations.ApiModel;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.validation.Valid;
import java.util.List;

@Data
@EqualsAndHashCode(callSuper = true)
@ApiModel(value = "CreateBillCmd", description = "新生来了，开票")
public class CreateBillCmd extends BaseBillCmd {

    @Valid
    CreateBillStudentCmd student;

    List<CreateBillCourseCmd> billCourseList;

}
