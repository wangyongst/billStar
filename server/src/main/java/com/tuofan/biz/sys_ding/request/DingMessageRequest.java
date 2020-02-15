package com.tuofan.biz.sys_ding.request;

import com.tuofan.biz.sys_ding.request.param.MessageSendBody;
import com.tuofan.biz.sys_ding.request.utils.RequestUtils;
import com.tuofan.biz.sys_ding.response.base.BaseResponse;
import com.tuofan.biz.sys_ding.service.TokenService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
public class DingMessageRequest {

    @Autowired
    TokenService tokenService;

    @Autowired
    RequestUtils requestUtils;

    private String sendNotifyRequestUrl = "https://oapi.dingtalk.com/topapi/message/corpconversation/asyncsend_v2?access_token={accessToken}";

    public BaseResponse sendMessage(MessageSendBody messageSendBody) {
        Map<String, Object> param = tokenService.getTokenMap();
        BaseResponse response = requestUtils.post(sendNotifyRequestUrl, messageSendBody, BaseResponse.class, param);
        return response;
    }
}
