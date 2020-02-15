package com.tuofan.biz.sys_ding.response.base;

import com.tuofan.biz.sys_orgnization.domain.entity.DingUser;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.List;

@Data
@EqualsAndHashCode(callSuper = true)
public class DeptUserListResponse extends BaseResponse {

    private boolean hasMore = false;

    private List<DingUser> userlist;
}
