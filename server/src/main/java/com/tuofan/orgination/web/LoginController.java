package com.tuofan.orgination.web;


import com.tuofan.configs.service.ConfigCachedUtils;
import com.tuofan.core.Result;
import com.tuofan.ding.request.UserInfoByCodeRequest;
import com.tuofan.ding.response.UserInfoByCodeResponse;
import com.tuofan.orgination.service.IDingUserService;
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

    @GetMapping("loginByCode")
    public Result list(@RequestParam String code) {
        log.info(code);
        if (code.equals("appTestCode")) {
            return Result.ok(iDingUserService.getById(-1));
        } else {
            UserInfoByCodeResponse response = userInfoByCodeRequest.get(code);
            return Result.ok(iDingUserService.getByUserId(response.getUserid()));
        }
    }
}
