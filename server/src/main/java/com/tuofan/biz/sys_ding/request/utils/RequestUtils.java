package com.tuofan.biz.sys_ding.request.utils;

import com.google.common.collect.Lists;
import com.tuofan.biz.sys_ding.response.base.BaseResponse;
import com.tuofan.core.exception.BizException;
import com.tuofan.core.utils.JsonUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.Map;

@Slf4j
@Component
@SuppressWarnings("unchecked")
public class RequestUtils {

    @Autowired
    private RestTemplate restTemplate;

    public <T> T post(String url, Object request, Class<T> responseClass, Map<String, ?> uriVariables) {
        ResponseEntity<T> responseEntity = restTemplate.postForEntity(url, request, responseClass, uriVariables);
        preHandle(responseEntity);
        return responseEntity.getBody();
    }

    public <T> T get(String url, Class<T> responseClass) {
        ResponseEntity<T> responseEntity = restTemplate.getForEntity(url, responseClass);
        preHandle(responseEntity);
        return responseEntity.getBody();
    }

    public <T> T getWithUrlParam(String url, Class<T> responseClass, Map<String, ?> map) {
        ResponseEntity<T> responseEntity = restTemplate.getForEntity(url, responseClass, map);
        preHandle(responseEntity);
        return responseEntity.getBody();
    }

    public <T> T exchangePost(String url, ParameterizedTypeReference typeReference, Object postObj, Map uriVars) {
        HttpEntity entity = new HttpEntity(postObj);
        ResponseEntity<T> responseEntity = restTemplate.exchange(url, HttpMethod.POST, entity, typeReference, uriVars);
        preHandle(responseEntity);
        return responseEntity.getBody();
    }

    public <T> T exchangeGet(String url, ParameterizedTypeReference typeReference, Object postObj, Map uriVars) {
        HttpEntity entity = new HttpEntity(postObj);
        ResponseEntity<T> responseEntity = restTemplate.exchange(url, HttpMethod.GET, entity, typeReference, uriVars);
        preHandle(responseEntity);
        return responseEntity.getBody();
    }

    private void preHandle(ResponseEntity entity) {
        log.info("ResponseEntity:{}", entity);
        if (!HttpStatus.OK.equals(entity.getStatusCode())) {
            log.error("接口请求失败:{}", entity);
            throw new BizException("101", JsonUtils.toJsonString(entity));
        }
        BaseResponse baseResponse = (BaseResponse) entity.getBody();
        if (baseResponse.getErrcode() != BaseResponse.OK) {
            log.error("接口请求错误，提示:{},响应整体:{}", baseResponse.toErrString(), baseResponse);
            List<Integer> ignoreExceptions = Lists.newArrayList(50004, 40009, 60003);
            if (!ignoreExceptions.contains(baseResponse.getErrcode())) {
                throw new BizException("102", baseResponse.toErrString());
            } else {
                log.error("接口请求错误不抛出异常，跳过，信息={}", baseResponse);
            }
        }
    }


}
