package com.tuofan.biz.bill_manage.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class YearSaleDto {

    @ApiModelProperty("校区ID")
    private Integer deptSchoolId;

    private String yearMonth;

    @ApiModelProperty("销售额")
    private Double amount;

}
