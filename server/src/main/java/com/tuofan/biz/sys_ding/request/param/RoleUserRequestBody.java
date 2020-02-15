package com.tuofan.biz.sys_ding.request.param;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
public class RoleUserRequestBody extends BaseRequestBody {

    private Integer role_id;

    public RoleUserRequestBody(int size, int offset, int roleId) {
        super(size, offset);
        this.setRole_id(roleId);
    }
}
