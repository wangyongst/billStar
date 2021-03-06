package com.tuofan.orgination.controller;


import com.tuofan.configs.constants.ConfigNameConstants;
import com.tuofan.configs.service.ISysConfigsService;
import com.tuofan.core.BizException;
import com.tuofan.core.Result;
import com.tuofan.ding.response.ApiConfig;
import com.tuofan.ding.service.JsApiTicketService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Formatter;

@Slf4j
@RestController
@RequestMapping("/bill/dingParam")
public class DingParamController {

    @Autowired
    private ISysConfigsService iSysConfigsService;

    @Autowired
    private JsApiTicketService jsApiTicketService;

    @GetMapping("get")
    public Result getSignApiConfig(String url) throws UnsupportedEncodingException {
        String urlDecoded = URLDecoder.decode(url, "UTF-8");
        log.info("URL:{},URL decoded:{}", url, urlDecoded);
        String ticket = jsApiTicketService.getTicket();
        return Result.ok(getApiConfig(ticket, urlDecoded));
    }

    @GetMapping("getCorpInfo")
    public Result getCorpInfo() {
        ApiConfig apiConfig = new ApiConfig();
        apiConfig.setAgentId(iSysConfigsService.findByName(ConfigNameConstants.agentId).getValue());
        apiConfig.setCorpId(iSysConfigsService.findByName(ConfigNameConstants.corpId).getValue());
//        apiConfig.setAppEnv(iSysConfigsService.findByName(ConfigNameConstants.appEnv).getValue());
        return Result.ok(apiConfig);
    }

    private ApiConfig getApiConfig(String ticket, String url) {
        ApiConfig apiConfig = new ApiConfig();
        apiConfig.setAgentId(iSysConfigsService.findByName(ConfigNameConstants.agentId).getValue());
        apiConfig.setCorpId(iSysConfigsService.findByName(ConfigNameConstants.corpId).getValue());
        apiConfig.setUrl(url);
        apiConfig.setTimeStamp(System.currentTimeMillis());
        apiConfig.setNonceStr("app");
        apiConfig.setSignature(sign(ticket, apiConfig));
        log.info("apiConfig:{}", apiConfig);
        return apiConfig;
    }

    private String sign(String ticket, ApiConfig apiConfig) {
        return this.sign(ticket, apiConfig.getNonceStr(), apiConfig.getTimeStamp(), apiConfig.getUrl());
    }

    private String sign(String ticket, String nonceStr, long timeStamp, String url) {
        String plain = "jsapi_ticket=" + ticket + "&noncestr=" + nonceStr + "&timestamp=" + String.valueOf(timeStamp)
                + "&url=" + url;
        try {
            MessageDigest sha1 = MessageDigest.getInstance("SHA-1");
            sha1.reset();
            sha1.update(plain.getBytes("UTF-8"));
            return byteToHex(sha1.digest());
        } catch (NoSuchAlgorithmException e) {
            throw new BizException("NoSuchAlgorithmException", e.getMessage());
        } catch (UnsupportedEncodingException e) {
            throw new BizException("OApiResultException", (e.getMessage()));
        }
    }

    private String byteToHex(final byte[] hash) {
        Formatter formatter = new Formatter();
        for (byte b : hash) {
            formatter.format("%02x", b);
        }
        String result = formatter.toString();
        formatter.close();
        return result;
    }
}
