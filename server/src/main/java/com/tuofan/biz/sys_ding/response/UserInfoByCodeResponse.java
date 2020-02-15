package com.tuofan.biz.sys_ding.response;

import com.tuofan.biz.sys_ding.response.base.BaseResponse;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
public class UserInfoByCodeResponse extends BaseResponse {

	private String userid;

}
