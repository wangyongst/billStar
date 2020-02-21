package com.tuofan.ding.response;


import com.tuofan.ding.response.base.BaseResponse;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
public class UserInfoByCodeResponse extends BaseResponse {

	private String userid;

}
