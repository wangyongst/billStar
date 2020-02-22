package com.tuofan.orgination.controller;

import com.tuofan.core.Result;
import com.tuofan.orgination.application.DingDeptSynService;
import com.tuofan.orgination.application.DingUserSynService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/bill/ding")
public class DingHandleController {

    @Autowired
    DingDeptSynService dingDeptSynService;

    @Autowired
    DingUserSynService dingUserSynService;


    @GetMapping("synDept")
    public Result synDept() {
        return dingDeptSynService.syn();
    }

    @GetMapping("synUser")
    public Result synUser() {
        return dingUserSynService.syn();
    }


}
