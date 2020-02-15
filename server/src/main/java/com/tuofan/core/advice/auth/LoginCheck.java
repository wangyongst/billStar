package com.tuofan.core.advice.auth;

import com.tuofan.biz.sys_configs.constants.ConfigNameConstants;
import com.tuofan.biz.sys_configs.service.ConfigCachedUtils;
import com.tuofan.biz.sys_orgnization.application.query.DingUserQueryService;
import com.tuofan.biz.sys_orgnization.dto.UserVO;
import com.tuofan.core.constants.AppConstants;
import com.tuofan.core.constants.LoginConstants;
import com.tuofan.core.exception.BizException;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;


@Aspect
@Component
@Order(1)
@Slf4j
public class LoginCheck {

    @Autowired
    private HttpServletRequest request;

    @Autowired
    private DingUserQueryService dingUserQueryService;

    @Resource
    ConfigCachedUtils configCachedUtils;

    @Pointcut(value = "@annotation(com.tuofan.core.advice.auth.LoginRequired)")
    private void loginCheckPoint() {

    }


    @Before("loginCheckPoint()&&@annotation(loginRequired)")
    public void doAroundMethod(LoginRequired loginRequired) throws Throwable {
        if (request.getHeader(LoginConstants.USER_ID) == null) {
            throw new BizException("101", "请先登录");
        }
        int userId = request.getIntHeader(LoginConstants.USER_ID);
        log.info("{}={}", LoginConstants.USER_ID, userId);
        UserVO userVO = dingUserQueryService.getVO(userId);

        if (userVO == null || userVO.getId() == null) {
            throw new BizException("101", "请确保您的用户信息已录入系统，微应用已完成自动登录。");
        }
        // 超管默认为是管理员
        String superAdminIdStr = configCachedUtils.getValue(ConfigNameConstants.superAdminId);
        if (userVO.getId().toString().equals(superAdminIdStr)) {
            userVO.setIsAppAdmin(AppConstants.intYes);
        }
        // 检查是否要求是超管
        if (loginRequired.superAdminOnly()) {
            if (!userVO.getId().toString().equals(superAdminIdStr)) {
                throw new BizException("102", "超级管理员才能进行该操作");
            }
        }
        // 检查是否要求是管理员
        if (loginRequired.adminOnly()) {
            if (!userVO.getIsAppAdmin().equals(AppConstants.intYes)) {
                throw new BizException("103", "管理员才能进行该操作");
            }
        }
        request.setAttribute(LoginConstants.DING_USER, userVO);
    }

}
