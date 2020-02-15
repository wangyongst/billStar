package com.tuofan.biz.sys_orgnization.web;

import com.tuofan.biz.sys_configs.constants.ConfigNameConstants;
import com.tuofan.biz.sys_configs.service.ConfigCachedUtils;
import com.tuofan.biz.sys_ding.request.UserInfoByCodeRequest;
import com.tuofan.biz.sys_ding.response.UserInfoByCodeResponse;
import com.tuofan.biz.sys_orgnization.application.query.DingUserQueryService;
import com.tuofan.biz.sys_orgnization.dto.UserVO;
import com.tuofan.core.constants.AppConstants;
import com.tuofan.core.exception.BizException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

@Slf4j
@RestController
@RequestMapping("${cert.api.prefix}/login")
public class LoginController {


    @Autowired
    UserInfoByCodeRequest userInfoByCodeRequest;

    @Autowired
    public DingUserQueryService dingUserQueryService;

    @Resource
    ConfigCachedUtils configCachedUtils;

    @GetMapping("loginByCode")
    public UserVO list(@RequestParam String code) {
        log.info(code);
        // 用于非钉钉环境的测试
        UserVO userVO = new UserVO();
        if (code.equals("appTestCode")) {
            userVO = dingUserQueryService.getVO(-1);
            if (userVO == null || userVO.getId() == null) {
                throw new BizException("80008", "自动登录失败");
            }
        } else {
            UserInfoByCodeResponse response = userInfoByCodeRequest.get(code);
            userVO = dingUserQueryService.getVOByUserId(response.getUserid());
        }
        String superAdminIdStr = configCachedUtils.getValue(ConfigNameConstants.superAdminId);
        if (userVO.getId().toString().equals(superAdminIdStr)) {
            userVO.setIsAppAdmin(AppConstants.intYes);
        }
        return userVO;
    }

}
