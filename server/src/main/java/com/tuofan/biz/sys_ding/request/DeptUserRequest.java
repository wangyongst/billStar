package com.tuofan.biz.sys_ding.request;

import com.tuofan.biz.sys_ding.request.utils.RequestUtils;
import com.tuofan.biz.sys_ding.response.base.DeptUserListResponse;
import com.tuofan.biz.sys_ding.service.TokenService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
@Slf4j
public class DeptUserRequest {

    @Autowired
    TokenService tokenService;

    @Autowired
    RequestUtils requestUtils;

    private String deptUserListAllUrl = "https://oapi.dingtalk.com/user/listbypage?access_token={accessToken}&department_id={deptId}&offset={offset}&size={size}";

    public DeptUserListResponse listAll(Integer deptId, Integer offset, Integer size) {
        Map<String, Object> param = tokenService.getTokenMap();
        param.put("deptId", deptId);
        param.put("offset", offset);
        param.put("size", size);
        log.info("部门用户同步，参数={}", param);
        ParameterizedTypeReference typeRef = new ParameterizedTypeReference<DeptUserListResponse>() {
        };
        return requestUtils.exchangeGet(deptUserListAllUrl, typeRef, DeptUserListResponse.class, param);
    }

}
