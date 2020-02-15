package com.tuofan.biz.sys_configs.entity;

import com.tuofan.core.persistence.entity.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.persistence.Table;


@Data
@EqualsAndHashCode(callSuper = true)
@Table(name = "sys_configs")
public class SystemConfig extends BaseEntity {

    private String name;

    private String value;

    private String remark;
}
