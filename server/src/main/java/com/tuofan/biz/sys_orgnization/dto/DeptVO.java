package com.tuofan.biz.sys_orgnization.dto;

import com.tuofan.biz.sys_orgnization.domain.service.DingUserRepository;
import com.tuofan.core.advice.convert.Converted;
import com.tuofan.core.dto.DTO;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.Date;

@Data
@EqualsAndHashCode(callSuper = true)
public class DeptVO extends DTO {

    private Integer id;

    private String name;

    @ApiModelProperty("是否校区")
    private Boolean isSchoolZone;

    @ApiModelProperty("联系电话")
    private String phone;


    private Integer createBy;

    @Converted(dependProperty = "createBy", bean = DingUserRepository.class)
    private String createByName;

    private Date createDate;

    private Integer updateBy;

    @Converted(dependProperty = "updateBy", bean = DingUserRepository.class)
    private String updateByName;

    @ApiModelProperty("机器人地址")
    private String groupWebHook;

    private Date updateDate;
}
