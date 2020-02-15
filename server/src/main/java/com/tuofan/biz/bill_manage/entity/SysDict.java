package com.tuofan.biz.bill_manage.entity;

import com.tuofan.biz.bill_manage.service.SysDictService;
import com.tuofan.biz.sys_orgnization.domain.service.DingUserRepository;
import com.tuofan.core.advice.convert.Converted;
import com.tuofan.core.constants.AppConstants;
import com.tuofan.core.persistence.entity.BaseEntity;
import com.tuofan.core.utils.StringUtils;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.persistence.Table;
import javax.persistence.Transient;

@Data
@EqualsAndHashCode(callSuper = true)
@Table(name = "sys_dict")
public class SysDict extends BaseEntity {

    private String type;

    private String name;

    private Integer createBy;

    // 默认值，当前值
    private Integer isDefault;

    // 父系值
    private Integer parentId;

    @Transient
    @Converted(dependProperty = "createBy", bean = DingUserRepository.class)
    private String createByName;

    private Integer updateBy;

    @Transient
    @Converted(dependProperty = "createBy", bean = DingUserRepository.class)
    private String updateByName;

    @Transient
    @Converted(dependProperty = "parentId", bean = SysDictService.class)
    private String parentName;

    public boolean isDefault() {
        return this.getIsDefault() != null && this.getIsDefault().equals(AppConstants.intYes);
    }

    public void setFullName() {
        if (StringUtils.isNotEmpty(this.getParentName())) {
            this.setName(this.getParentName() + "-" + this.getName());
        }
    }
}
