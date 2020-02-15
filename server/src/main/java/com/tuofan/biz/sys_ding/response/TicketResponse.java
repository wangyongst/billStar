package com.tuofan.biz.sys_ding.response;

import com.tuofan.biz.sys_ding.response.base.BaseResponse;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
public class TicketResponse extends BaseResponse {

	private String ticket;

	private Integer expires_in;

}
