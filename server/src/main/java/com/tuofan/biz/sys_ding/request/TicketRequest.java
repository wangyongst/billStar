package com.tuofan.biz.sys_ding.request;

import com.tuofan.biz.sys_ding.request.utils.RequestUtils;
import com.tuofan.biz.sys_ding.response.TicketResponse;
import com.tuofan.biz.sys_ding.service.TokenService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
public class TicketRequest {

    @Autowired
    TokenService tokenService;

    private String ticketUrl = "https://oapi.dingtalk.com/get_jsapi_ticket?access_token={accessToken}";

    @Autowired
    RequestUtils requestUtils;

    public TicketResponse get() {
        Map<String, Object> param = tokenService.getTokenMap();
        return requestUtils.getWithUrlParam(ticketUrl, TicketResponse.class, param);
    }

}
