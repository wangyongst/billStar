package com.tuofan.biz.sys_ding.request;

import com.tuofan.biz.sys_ding.request.param.RoleUserRequestBody;
import com.tuofan.biz.sys_ding.request.utils.RequestUtils;
import com.tuofan.biz.sys_ding.response.base.BaseResponse;
import com.tuofan.biz.sys_ding.response.base.HasMosResponse;
import com.tuofan.biz.sys_ding.service.TokenService;
import com.tuofan.biz.sys_orgnization.domain.entity.DingRoleUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
@SuppressWarnings("unchecked")
public class RoleUserRequest {

    @Autowired
    TokenService tokenService;

    @Autowired
    RequestUtils requestUtils;

    private String roleListAllUrl = "https://oapi.dingtalk.com/topapi/role/simplelist?access_token={accessToken}";

    public HasMosResponse<DingRoleUser> listHasMoreAll(int size, int offset, int roleId) {
        Map<String, Object> param = tokenService.getTokenMap();
        RoleUserRequestBody requestBody = new RoleUserRequestBody(size, offset, roleId);
        ParameterizedTypeReference typeRef = new ParameterizedTypeReference<BaseResponse<HasMosResponse<DingRoleUser>>>() {
        };
        BaseResponse response = requestUtils.exchangePost(roleListAllUrl, typeRef, requestBody, param);
        return (HasMosResponse<DingRoleUser>) response.getResult();
    }

}
