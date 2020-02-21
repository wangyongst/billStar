package com.tuofan.dingding.web;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.tuofan.dingding.entity.DingDept;
import com.tuofan.dingding.service.IDingDeptService;
import com.tuofan.configs.service.ConfigCachedUtils;
import com.tuofan.core.Result;
import com.tuofan.ding.request.RobotMessageSendRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
    public Result update(DingDept dingDept) {
        DingDept saved = iDingDeptService.getById(dingDept.getId());
        if (dingDept != null) {
            if (dingDept.getIsSchoolZone() != null) saved.setIsSchoolZone(dingDept.getIsSchoolZone());
            if (dingDept.getPhone() != null) saved.setPhone(dingDept.getPhone());
            if (dingDept.getGroupWebHook() != null) saved.setGroupWebHook(dingDept.getGroupWebHook());
        }
        return Result.ok();
    }

//    @PostMapping("sendTestMessage")
//    public BaseResponse sendTestMessage(@RequestParam Integer deptId) {
//        DingDept dingDept = dingDeptRepository.get(deptId);
//        if (dingDept == null) {
//            throw new BizException("100", "无法根据部门ID找到部门数据");
//        }
//        if (StringUtils.isEmpty(dingDept.getGroupWebHook())) {
//            throw new BizException("101", "校区:" + dingDept.getName() + " 没有配置群机器人地址，请先配置地址");
//        }
//        MarkDownMessageDTO markDownMessageDTO = MarkDownMessageDTO.of("云校管", "测试消息@" + System.currentTimeMillis());
//        return robotMessageSendRequest.sendMessage(dingDept.getGroupWebHook(), markDownMessageDTO);
//    }
}
