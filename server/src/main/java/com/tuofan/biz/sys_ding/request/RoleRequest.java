package com.tuofan.biz.sys_ding.request;

import com.tuofan.biz.sys_ding.request.param.BaseRequestBody;
import com.tuofan.biz.sys_ding.request.utils.RequestUtils;
import com.tuofan.biz.sys_ding.response.base.BaseResponse;
import com.tuofan.biz.sys_ding.response.base.HasMosResponse;
import com.tuofan.biz.sys_ding.service.TokenService;
import com.tuofan.biz.sys_orgnization.domain.entity.DingRoleGroup;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
@SuppressWarnings("unchecked")
public class RoleRequest {

    @Autowired
    TokenService tokenService;

    @Autowired
    RequestUtils requestUtils;

    private String roleListAllUrl = "https://oapi.dingtalk.com/topapi/role/list?access_token={accessToken}";

    public HasMosResponse<DingRoleGroup> listHasMoreAll(int size, int offset) {
        Map<String, Object> param = tokenService.getTokenMap();
        BaseRequestBody requestBody = new BaseRequestBody(size, offset);
        ParameterizedTypeReference typeRef = new ParameterizedTypeReference<BaseResponse<HasMosResponse<DingRoleGroup>>>() {
        };
        BaseResponse response = requestUtils.exchangePost(roleListAllUrl, typeRef, requestBody, param);
        return (HasMosResponse<DingRoleGroup>) response.getResult();
    }

}
