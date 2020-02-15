package com.tuofan.biz.sys_orgnization.application.syn;

import com.google.common.collect.Lists;
import com.tuofan.biz.sys_orgnization.dto.SynResultVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class DingSynService {

    @Autowired
    DingDeptSynService dingDeptSynService;

    @Autowired
    DingRoleSynService dingRoleSynService;

    @Autowired
    DingRoleUserSynService dingRoleUserSynService;

    @Autowired
    DingUserSynService dingUserSynService;

    public List<SynResultVO> synAll() {
        // 注意顺序
        List<SynResultVO> result = Lists.newArrayList();
//        result.addAll(dingRoleSynService.syn());
//        result.addAll(dingRoleUserSynService.syn());
        result.add(dingDeptSynService.syn());
        result.addAll(dingUserSynService.syn());
        return result;
    }
}
