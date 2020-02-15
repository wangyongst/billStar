package com.tuofan.biz.sys_wechat;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("weChat")
public class WeChatController {

    @GetMapping("synDept")
    public void synDept() {

    }
}
