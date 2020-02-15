package com.tuofan.biz.sys_orgnization.domain.entity;

import com.tuofan.core.persistence.entity.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.persistence.Column;

@Data
@EqualsAndHashCode(callSuper = true)
public class DingRoleUser extends BaseEntity {

    @Column(name = "user_id")
    private String userid;

    private Integer roleId;

    public String generateKey() {
        return roleId + "&" + userid;
    }

}
