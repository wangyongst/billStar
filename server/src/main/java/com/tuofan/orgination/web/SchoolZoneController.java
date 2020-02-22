package com.tuofan.orgination.web;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.tuofan.core.BizException;
import com.tuofan.ding.request.param.robot.messsage.MarkDownMessageDTO;
import com.tuofan.ding.response.base.BaseResponse;
import com.tuofan.orgination.entity.DingDept;
import com.tuofan.orgination.service.IDingDeptService;
import com.tuofan.configs.service.ConfigCachedUtils;
import com.tuofan.core.Result;
import com.tuofan.ding.request.RobotMessageSendRequest;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/bill/schoolZone")
public class SchoolZoneController {


    @Autowired
    public IDingDeptService iDingDeptService;

    @Autowired
    public ConfigCachedUtils configCachedUtils;

    @Autowired
    private RobotMessageSendRequest robotMessageSendRequest;

    @GetMapping("list")
    public Result list() {
        return Result.ok(iDingDeptService.list());
    }

    @GetMapping("listSchoolZone")
    public Result listSchoolZone() {
        QueryWrapper query = new QueryWrapper();
        query.eq("is_school_zone", 1);
        return Result.ok(iDingDeptService.list(query));
    }

    @PostMapping("update")
    public Result update(@RequestBody DingDept dingDept) {
        DingDept saved = iDingDeptService.getById(dingDept.getId());
        if (dingDept != null) {
            if (dingDept.getIsSchoolZone() != null) saved.setIsSchoolZone(dingDept.getIsSchoolZone());
            if (dingDept.getPhone() != null) saved.setPhone(dingDept.getPhone());
            if (dingDept.getGroupWebHook() != null) saved.setGroupWebHook(dingDept.getGroupWebHook());
        }
        iDingDeptService.saveOrUpdate(saved);
        return Result.ok();
    }

    @PostMapping("sendTestMessage")
    public Result sendTestMessage(@RequestParam Integer deptId) {
        DingDept dingDept = iDingDeptService.getById(deptId);
        if (dingDept == null) {
            return Result.error("无法根据部门ID找到部门数据");
        }
        if (StringUtils.isEmpty(dingDept.getGroupWebHook())) {
            return Result.error(" 没有配置群机器人地址，请先配置地址");
        }
        MarkDownMessageDTO markDownMessageDTO = MarkDownMessageDTO.of("云校管", "测试消息@" + System.currentTimeMillis());
        robotMessageSendRequest.sendMessage(dingDept.getGroupWebHook(), markDownMessageDTO);
        return Result.ok();
    }
}
