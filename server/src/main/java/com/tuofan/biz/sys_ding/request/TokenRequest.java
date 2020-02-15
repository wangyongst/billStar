package com.tuofan.biz.sys_ding.request;

import com.google.common.collect.Maps;
import com.tuofan.biz.sys_configs.constants.ConfigNameConstants;
import com.tuofan.biz.sys_configs.service.ConfigCachedUtils;
import com.tuofan.biz.sys_ding.request.utils.RequestUtils;
import com.tuofan.biz.sys_ding.response.AccessToken;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
public class TokenRequest {

    @Autowired
    ConfigCachedUtils configCachedUtils;

    private String accessTokenUrl = "https://oapi.dingtalk.com/gettoken?appkey={appkey}&appsecret={appsecret}";

    @Autowired
    RequestUtils requestUtils;

    public AccessToken get() {
        Map<String, String> param = getAppConfigMap();
        return requestUtils.getWithUrlParam(accessTokenUrl, AccessToken.class, param);
    }

    private Map<String, String> getAppConfigMap() {
        Map<String, String> param = Maps.newHashMap();
        param.put("appkey", configCachedUtils.getValue(ConfigNameConstants.appKey));
        param.put("appsecret", configCachedUtils.getValue(ConfigNameConstants.appSecret));
        return param;
    }

}
