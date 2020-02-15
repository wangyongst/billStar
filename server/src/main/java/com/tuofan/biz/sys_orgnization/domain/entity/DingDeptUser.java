package com.tuofan.biz.sys_orgnization.domain.entity;

import com.tuofan.core.persistence.entity.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.persistence.Column;

@Data
@EqualsAndHashCode(callSuper = true)
public class DingDeptUser extends BaseEntity {

    @Column(name = "user_id")
    private String userId;

    private Integer deptId;

    public String toString() {
        return String.format("userID:%s,deptId:%s", userId, deptId);
    }

    public DingDeptUser() {

    }

    public DingDeptUser(String userId, Integer deptId) {
        this.deptId = deptId;
        this.userId = userId;
    }
}
