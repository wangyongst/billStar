package com.tuofan.biz.sys_orgnization.domain.entity;

import com.tuofan.core.persistence.entity.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
public class DingRole extends BaseEntity {

    private Integer id;

    private Integer groupId;

    private String name;

}
