package com.tuofan.dingding.web;


import com.tuofan.configs.constants.ConfigNameConstants;
import com.tuofan.configs.service.ConfigCachedUtils;
import com.tuofan.core.BizException;
import com.tuofan.core.GlobalConstants;
import com.tuofan.core.Result;
import com.tuofan.ding.request.UserInfoByCodeRequest;
import com.tuofan.ding.response.UserInfoByCodeResponse;
import com.tuofan.dingding.service.IDingUserService;
import com.tuofan.dingding.vo.UserVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

@Slf4j
@RestController
@RequestMapping("/bill/login")
public class LoginController {


    @Autowired
    private UserInfoByCodeRequest userInfoByCodeRequest;

    @Autowired
    private IDingUserService iDingUserService;

    @Resource
    private ConfigCachedUtils configCachedUtils;

    @GetMapping("loginByCode")
    public Result list(@RequestParam String code) {
        log.info(code);
        if (code.equals("appTestCode")) {
            Result.ok(iDingUserService.getById(-1));
        } else {
            UserInfoByCodeResponse response = userInfoByCodeRequest.get(code);
            Result.ok(iDingUserService.getByUserId(response.getUserid()));
        }
        return Result.error("失败");
    }

}
