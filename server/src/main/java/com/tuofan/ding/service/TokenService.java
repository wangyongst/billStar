package com.tuofan.ding.service;

import com.google.common.collect.Maps;
import com.tuofan.core.BizException;
import com.tuofan.configs.entity.SysConfigs;
import com.tuofan.configs.service.ConfigCachedUtils;
import com.tuofan.ding.request.TokenRequest;
import com.tuofan.ding.response.AccessToken;
import com.tuofan.ding.response.base.BaseResponse;
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

    private boolean isTokenExpired(SysConfigs config) {
        if (config == null) {
            return true;
        }
        if (System.currentTimeMillis() - config.getUpdateDate().getTime() > 7000 * 1000) {
            return true;
        }
        return false;
    }

    public String getToken() {
        SysConfigs config = configCachedUtils.getItem(ConfigNameDingToken);
        if (config == null) {
            String token = obtainTokenFromRemote();
            config = new SysConfigs();
            config.setName(ConfigNameDingToken);
            config.setValue(token);
            config.setUpdateDate(new Date());
            configCachedUtils.saveOrUpdate(config);
        }
        if (this.isTokenExpired(config)) {
            String token = obtainTokenFromRemote();
            config.setValue(token);
            configCachedUtils.saveOrUpdate(config);
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
        if (token.getErrcode()== BaseResponse.OK) {
            return token.getAccess_token();
        }
        throw new BizException("TOKEN_ERROR", "obtain token error , Code:" + token.getErrcode() + ",Msg:" + token.getErrmsg());
    }

}
