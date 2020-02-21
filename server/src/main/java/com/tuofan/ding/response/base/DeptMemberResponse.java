package com.tuofan.ding.response.base;

import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.List;

@Data
@EqualsAndHashCode(callSuper = true)
public class DeptMemberResponse extends BaseResponse {

    private List<String> userIds;
}
