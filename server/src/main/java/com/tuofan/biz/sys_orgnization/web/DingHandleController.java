package com.tuofan.biz.sys_orgnization.web;

import com.tuofan.biz.sys_orgnization.application.syn.*;
import com.tuofan.biz.sys_orgnization.dto.SynResultVO;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("${cert.api.prefix}/ding")
public class DingHandleController {

    @Autowired
    DingDeptSynService dingDeptSynService;

    @Autowired
    DingRoleSynService dingRoleSynService;

    @Autowired
    DingRoleUserSynService dingRoleUserSynService;

    @Autowired
    DingUserSynService dingUserSynService;

    @Autowired
    DingSynService dingSynService;


    @GetMapping("synDept")
    public SynResultVO synDept() {
        return dingDeptSynService.syn();
    }

    @GetMapping("synRole")
    public List<SynResultVO> synRole() {
        return dingRoleSynService.syn();
    }

    @GetMapping("synRoleUser")
    public List<SynResultVO> synRoleUser() {
        return dingRoleUserSynService.syn();
    }

    @GetMapping("synUser")
    public List<SynResultVO> synUser() {
        return dingUserSynService.syn();
    }

    @GetMapping("synOrganization")
    public List<SynResultVO> synAll() {
        // 注意顺序
        return dingSynService.synAll();
    }


}
