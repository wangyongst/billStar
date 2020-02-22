package com.tuofan.ding.request;

import com.google.common.collect.Maps;

import com.tuofan.configs.constants.ConfigNameConstants;
import com.tuofan.configs.service.ISysConfigsService;
import com.tuofan.ding.request.utils.RequestUtils;
import com.tuofan.ding.response.AccessToken;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
public class TokenRequest {

    @Autowired
    private ISysConfigsService iSysConfigsService;

    private String accessTokenUrl = "https://oapi.dingtalk.com/gettoken?appkey={appkey}&appsecret={appsecret}";

    @Autowired
    RequestUtils requestUtils;

    public AccessToken get() {
        Map<String, String> param = getAppConfigMap();
        return requestUtils.getWithUrlParam(accessTokenUrl, AccessToken.class, param);
    }

    private Map<String, String> getAppConfigMap() {
        Map<String, String> param = Maps.newHashMap();
        param.put("appkey", iSysConfigsService.findByName(ConfigNameConstants.appKey).getValue());
        param.put("appsecret", iSysConfigsService.findByName(ConfigNameConstants.appSecret).getValue());
        return param;
    }

}
