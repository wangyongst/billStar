package com.tuofan.biz.bill_manage.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

@Data
public class SaleDTO {

    @ApiModelProperty("校区ID")
    private Integer deptSchoolId;

    @ApiModelProperty("校区名")
    private String deptSchoolName;

    @ApiModelProperty("销售额")
    private Double amount;

    public static SaleDTO ofTotal(List<SaleDTO> list) {
        SaleDTO total = new SaleDTO();
        total.setAmount(0.0);
        total.setDeptSchoolName("总计");
        total.setDeptSchoolId(-1);
        list.forEach(ele -> total.setAmount(total.getAmount() + ele.getAmount()));
        return total;
    }

}
