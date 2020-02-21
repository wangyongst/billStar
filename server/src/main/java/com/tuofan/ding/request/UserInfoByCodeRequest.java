package com.tuofan.ding.request;


import com.tuofan.ding.request.utils.RequestUtils;
import com.tuofan.ding.response.UserInfoByCodeResponse;
import com.tuofan.ding.service.TokenService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
public class UserInfoByCodeRequest {

    @Autowired
    TokenService tokenService;

    private String userInfoUrl = "https://oapi.dingtalk.com/user/getuserinfo?access_token={accessToken}&code={code}";

    @Autowired
    RequestUtils requestUtils;

    public UserInfoByCodeResponse get(String code) {
        Map<String, Object> param = tokenService.getTokenMap();
        param.put("code",code);
        return requestUtils.getWithUrlParam(userInfoUrl, UserInfoByCodeResponse.class, param);
    }


}
