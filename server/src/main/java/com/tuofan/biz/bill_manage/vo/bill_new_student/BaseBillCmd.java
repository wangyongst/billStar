package com.tuofan.biz.bill_manage.vo.bill_new_student;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.Date;

@Data
public class BaseBillCmd implements Serializable {

    @NotNull(message = "金额不能为空")
    @ApiModelProperty("金额")
    Double amount;

    @ApiModelProperty("欠费金额")
    Double currentArrears;

    @NotNull(message = "是否接送 不能为空")
    @ApiModelProperty("请选择是否接送")
    Integer isTransferred;

    @NotNull(message = "请添加备注信息")
    String remark;

    @NotNull(message = "请选择支付类型")
    @ApiModelProperty("支付类型")
    Integer payTypeId;

    Integer billCreator;

    Date billTime;

    @NotNull(message = "校区不能为空")
    @ApiModelProperty("校区")
    Integer deptSchoolId;

    @NotNull(message = "学期不能为空")
    @ApiModelProperty("学期")
    Integer semesterId;

    public void initCurrentArrears() {
        if (this.getCurrentArrears() == null) {
            this.setCurrentArrears(0.0);
        }
    }

}
