package com.tuofan.ding.request;

import com.google.common.collect.Maps;
import com.tuofan.ding.request.param.robot.messsage.MarkDownMessageDTO;
import com.tuofan.ding.request.utils.RequestUtils;
import com.tuofan.ding.response.base.BaseResponse;
import com.tuofan.ding.service.TokenService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class RobotMessageSendRequest {

    @Autowired
    TokenService tokenService;

    @Autowired
    RequestUtils requestUtils;

    public BaseResponse sendMessage(String messageUrl, MarkDownMessageDTO messageDTO) {
        return requestUtils.post(messageUrl, messageDTO, BaseResponse.class, Maps.newHashMap());
    }

}
