package com.tuofan.ding.response.base;

import com.tuofan.basic.entity.DingUser;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.List;

@Data
@EqualsAndHashCode(callSuper = true)
public class DeptUserListResponse extends BaseResponse {

    private boolean hasMore = false;

    private List<DingUser> userlist;
}
