package com.tuofan.biz.sys_ding.service;

import com.google.common.collect.Maps;
import com.tuofan.biz.sys_configs.entity.SystemConfig;
import com.tuofan.biz.sys_configs.service.ConfigCachedUtils;
import com.tuofan.biz.sys_ding.request.TokenRequest;
import com.tuofan.biz.sys_ding.response.AccessToken;
import com.tuofan.biz.sys_ding.response.base.BaseResponse;
import com.tuofan.core.exception.BizException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.Map;

@Slf4j
@Component
public class TokenService {

    @Autowired
    private TokenRequest tokenRequest;

    @Autowired
    private ConfigCachedUtils configCachedUtils;

    public static String ConfigNameDingToken = "dingToken";

    private boolean isTokenExpired(SystemConfig config) {
        if (config == null) {
            return true;
        }
        if (System.currentTimeMillis() - config.getUpdateDate().getTime() > 7000 * 1000) {
            return true;
        }
        return false;
    }

    public String getToken() {
        SystemConfig config = configCachedUtils.getItem(ConfigNameDingToken);
        if (config == null) {
            String token = obtainTokenFromRemote();
            config = new SystemConfig();
            config.setName(ConfigNameDingToken);
            config.setValue(token);
            config.setUpdateDate(new Date());
            configCachedUtils.create(config);
        }
        if (this.isTokenExpired(config)) {
            String token = obtainTokenFromRemote();
            config.setValue(token);
            configCachedUtils.update(config);
            log.info("DingDing accessToken refreshed");
        }
        return config.getValue();
    }

    public Map<String, Object> getTokenMap() {
        Map<String, Object> map = Maps.newHashMap();
        map.put("accessToken", this.getToken());
        return map;
    }

    private String obtainTokenFromRemote() {
        AccessToken token = tokenRequest.get();
        if (token.getErrcode()==BaseResponse.OK) {
            return token.getAccess_token();
        }
        throw new BizException("TOKEN_ERROR", "obtain token error , Code:" + token.getErrcode() + ",Msg:" + token.getErrmsg());
    }

}
