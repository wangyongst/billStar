package com.tuofan.biz.bill_manage.vo;

import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

@Data
public class UpdateBillArrearsCmd {

    @NotNull(message = "票据ID不能为空")
    private Integer modelId;

    @NotNull(message = "欠费金额不能为空")
    @Min(value = 0, message = "金额需要大于0")
    private Double arrears;

}
