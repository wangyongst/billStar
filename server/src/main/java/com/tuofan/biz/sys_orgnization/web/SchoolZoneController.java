package com.tuofan.biz.sys_orgnization.web;

import com.google.common.collect.Lists;
import com.tuofan.biz.sys_configs.constants.ConfigNameConstants;
import com.tuofan.biz.sys_configs.service.ConfigCachedUtils;
import com.tuofan.biz.sys_ding.request.RobotMessageSendRequest;
import com.tuofan.biz.sys_ding.request.param.robot.messsage.MarkDownMessageDTO;
import com.tuofan.biz.sys_ding.response.base.BaseResponse;
import com.tuofan.biz.sys_orgnization.domain.entity.DingDept;
import com.tuofan.biz.sys_orgnization.domain.service.DingDeptRepository;
import com.tuofan.biz.sys_orgnization.dto.DeptVO;
import com.tuofan.biz.sys_orgnization.dto.UpdateDeptCmd;
import com.tuofan.biz.sys_orgnization.dto.UserVO;
import com.tuofan.core.advice.auth.LoginRequired;
import com.tuofan.core.advice.convert.FieldConversion;
import com.tuofan.core.constants.AppConstants;
import com.tuofan.core.exception.BizException;
import com.tuofan.core.helper.ModelConvertHelper;
import com.tuofan.core.utils.StringUtils;
import com.tuofan.core.utils.UserUtils;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Api(value = "管理员设置-校区管理", tags = "管理员设置")
@RestController
@RequestMapping("${cert.api.prefix}/schoolZone")
public class SchoolZoneController {


    @Autowired
    public DingDeptRepository dingDeptRepository;

    @Autowired
    public ConfigCachedUtils configCachedUtils;

    @Autowired
    private RobotMessageSendRequest robotMessageSendRequest;

    @ApiOperation("查出所有的一级部门，即校区候选机构")
    @PostMapping("listFirstLevelDept")
    @FieldConversion
    public List<DeptVO> list() {
        DingDept query = new DingDept();
        String dingRootDeptIdStr = configCachedUtils.getValue(ConfigNameConstants.dingRootDeptId);
        if (StringUtils.isEmpty(dingRootDeptIdStr)) {
            throw new BizException("5001", "没有配置根钉部门ID（集团ID）,请现在部门表配置name=" + ConfigNameConstants.dingRootDeptId + "的配置项");
        }
        query.setParentid(Integer.parseInt(dingRootDeptIdStr));
        List<DingDept> list = dingDeptRepository.listAll(query);
        return ModelConvertHelper.convertList(list, DeptVO.class);
    }

    @ApiOperation("查出所有的校区")
    @PostMapping("listSchoolZone")
    @FieldConversion
    public List<DeptVO> listSchoolZone() {
        DingDept query = new DingDept();
        query.setParentid(configCachedUtils.getDingRootDeptId());
        query.setIsSchoolZone(AppConstants.intYes);
        List<DingDept> list = dingDeptRepository.listAll(query);
        return ModelConvertHelper.convertList(list, DeptVO.class);
    }

    @ApiOperation("查出我能看到的所有的校区")
    @PostMapping("listSchoolZoneICanSee")
    @FieldConversion
    @LoginRequired
    public List<DeptVO> listSchoolZoneICanSee() {
        UserVO loginUser = UserUtils.getLoginUser();
        if (loginUser.getIsAdmin()) {
            DingDept query = new DingDept();
            query.setParentid(configCachedUtils.getDingRootDeptId());
            query.setIsSchoolZone(AppConstants.intYes);
            List<DingDept> list = dingDeptRepository.listAll(query);
            return ModelConvertHelper.convertList(list, DeptVO.class);
        } else if (!CollectionUtils.isEmpty(loginUser.getDeptSchoolIds())) {
            List<DingDept> list = dingDeptRepository.listByIds(loginUser.getDeptSchoolIds());
            return ModelConvertHelper.convertList(list, DeptVO.class);
        } else {
            return Lists.newArrayList();
        }
    }

    @ApiOperation("更新校区电话")
    @PostMapping("updatePhone")
    public Integer updatePhone(@RequestParam Integer id, String phone) {
        DingDept dingDept = dingDeptRepository.get(id);
        if (dingDept != null) {
            dingDept.setPhone(phone);
            return dingDeptRepository.update(dingDept);
        }
        return -1;
    }

    @ApiOperation("更新校区群机器人")
    @PostMapping("updateGroupWebHook")
    public Integer updateGroupChatID(@RequestBody UpdateDeptCmd updateDeptCmd) {
        DingDept dingDept = dingDeptRepository.get(updateDeptCmd.getId());
        if (dingDept != null) {
            dingDept.setGroupWebHook(updateDeptCmd.getGroupWebHook());
            return dingDeptRepository.update(dingDept);
        }
        return -1;
    }

    @ApiOperation("置为校区")
    @PostMapping("setAsSchoolZone")
    public Integer setAsSchoolZone(@RequestParam Integer id) {
        DingDept dingDept = dingDeptRepository.get(id);
        if (dingDept == null) {
            return -1;
        }
        if (dingDept.getIsSchoolZone() == AppConstants.intYes) {
            return 0;
        }
        dingDept.setIsSchoolZone(AppConstants.intYes);
        return dingDeptRepository.update(dingDept);
    }

    @ApiOperation("置为非校区")
    @PostMapping("setAsNotSchoolZone")
    public Integer setAsNotSchoolZone(@RequestParam Integer id) {
        DingDept dingDept = dingDeptRepository.get(id);
        if (dingDept == null) {
            return -1;
        }
        if (dingDept.getIsSchoolZone() == AppConstants.intNo) {
            return 0;
        }
        dingDept.setIsSchoolZone(AppConstants.intNo);
        return dingDeptRepository.update(dingDept);
    }

    @ApiOperation("查出所有部门")
    @PostMapping("listAllDeptEntity")
    @FieldConversion
    @LoginRequired
    public List<DingDept> listAllDept() {
        return dingDeptRepository.listAll(new DingDept());
    }

    @ApiOperation("发送测试消息")
    @PostMapping("sendTestMessage")
    public BaseResponse sendTestMessage(@RequestParam Integer deptId) {
        DingDept dingDept = dingDeptRepository.get(deptId);
        if (dingDept == null) {
            throw new BizException("100", "无法根据部门ID找到部门数据");
        }
        if (StringUtils.isEmpty(dingDept.getGroupWebHook())) {
            throw new BizException("101", "校区:" + dingDept.getName() + " 没有配置群机器人地址，请先配置地址");
        }
        MarkDownMessageDTO markDownMessageDTO = MarkDownMessageDTO.of("云校管", "测试消息@" + System.currentTimeMillis());
        return robotMessageSendRequest.sendMessage(dingDept.getGroupWebHook(), markDownMessageDTO);
    }
}
