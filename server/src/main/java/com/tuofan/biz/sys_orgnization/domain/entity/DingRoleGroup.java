package com.tuofan.biz.sys_orgnization.domain.entity;

import com.tuofan.core.persistence.entity.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.List;

@Data
@EqualsAndHashCode(callSuper = true)
public class DingRoleGroup extends BaseEntity {

    private Integer groupId;

    private String name;

    private List<DingRole> roles;

}
