package com.tuofan.biz.sys_ding.request;

import com.tuofan.biz.sys_ding.response.DeptListResponse;
import com.tuofan.biz.sys_orgnization.domain.entity.DingDept;
import com.tuofan.biz.sys_ding.request.utils.RequestUtils;
import com.tuofan.biz.sys_ding.service.TokenService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

@Component
public class DeptRequest {

    @Autowired
    TokenService tokenService;

    @Autowired
    RequestUtils requestUtils;

    private String deptListAllUrl = "https://oapi.dingtalk.com/department/list?access_token={accessToken}";

    public List<DingDept> listAll() {
        Map<String, Object> param = tokenService.getTokenMap();
        DeptListResponse deptListResponse = requestUtils.getWithUrlParam(deptListAllUrl, DeptListResponse.class, param);
        return deptListResponse.getDepartment();
    }

}
