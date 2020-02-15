package com.tuofan.biz.bill_manage.vo;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
public class UpdateCourseCmd extends CreateCourseCmd {

    private Integer id;

}
