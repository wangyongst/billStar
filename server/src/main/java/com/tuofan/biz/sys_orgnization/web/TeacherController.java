package com.tuofan.biz.sys_orgnization.web;

import com.github.pagehelper.PageInfo;
import com.google.common.collect.Lists;
import com.tuofan.biz.sys_configs.service.ConfigCachedUtils;
import com.tuofan.biz.sys_orgnization.application.query.DingUserQueryService;
import com.tuofan.biz.sys_orgnization.domain.entity.DingUser;
import com.tuofan.biz.sys_orgnization.domain.service.DingDeptRepository;
import com.tuofan.biz.sys_orgnization.domain.service.DingUserRepository;
import com.tuofan.biz.sys_orgnization.dto.UserVO;
import com.tuofan.biz.sys_orgnization.dto.query.ExtUserQuery;
import com.tuofan.core.advice.auth.LoginRequired;
import com.tuofan.core.advice.convert.FieldConversion;
import com.tuofan.core.constants.AppConstants;
import com.tuofan.core.dto.PageRequest;
import com.tuofan.core.helper.ModelConvertHelper;
import com.tuofan.core.utils.UserUtils;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@Api(value = "管理员设置-教师管理")
@RestController()
@RequestMapping("${cert.api.prefix}/teacher")
public class TeacherController {


    @Autowired
    public DingDeptRepository dingDeptRepository;

    @Autowired
    public DingUserRepository dingUserRepository;

    @Autowired
    private DingUserQueryService dingUserQueryService;

    @Autowired
    public ConfigCachedUtils configCachedUtils;


    @ApiOperation("查出所有员工")
    @PostMapping("listUser")
    @FieldConversion
    public PageInfo<UserVO> listUser(@RequestBody PageRequest<ExtUserQuery> queryPage) {
        if (queryPage == null) {
            queryPage = new PageRequest<>();
        }
        if (queryPage.getData() == null) {
            queryPage.setData(new ExtUserQuery());
        }
        return dingUserQueryService.listPage(queryPage);
    }

    @ApiOperation("查出所有的校区下的员工,可填校区ID，教师名字，手机号")
    @PostMapping("listSchoolZoneUser")
    @FieldConversion
    public PageInfo<UserVO> listSchoolZoneUser(@RequestBody PageRequest<ExtUserQuery> queryPage) {
        if (queryPage == null) {
            queryPage = new PageRequest<>();
        }
        if (queryPage.getData() == null) {
            queryPage.setData(new ExtUserQuery());
        }
//        if (CollectionUtils.isEmpty(queryPage.getData().getDeptSchoolIds())) {
//            // 不传则在全部校区里面查
//            queryPage.getData().setDeptSchoolIds(dingDeptRepository.listSchoolZoneId());
//        }
        return dingUserQueryService.listPage(queryPage);
    }

    @ApiOperation("查出所有的教师，可传校区ID，不传默认全部")
    @PostMapping("listTeacher")
    @FieldConversion
    public List<UserVO> listTeacher(@RequestParam(required = false) Integer deptSchoolId) {
        ExtUserQuery extUserQuery = new ExtUserQuery();
        extUserQuery.setDeptIds(Lists.newArrayList(deptSchoolId));
        extUserQuery.setIsTeacher(AppConstants.intYes);
        List<DingUser> list = dingUserQueryService.list(extUserQuery);
        return ModelConvertHelper.convertList(list, UserVO.class);
    }

    @ApiOperation("查出我能看到的所有教师。超管看全部，校管和教师看本校区")
    @PostMapping("listTeacherICanSee")
    @LoginRequired
    @FieldConversion
    public List<UserVO> listTeacherICanSee() {
        // 需要知道用户是否管理员
        List<Integer> schoolZoneIds = Lists.newArrayList();
        UserVO loginUser = UserUtils.getLoginUser();
        if (loginUser.getIsAdmin()) {
            // 校区管理员-查看全部
            schoolZoneIds = dingDeptRepository.listSchoolZoneId();
        } else {
            // 查看本校区
            schoolZoneIds = loginUser.getDeptSchoolIds();
        }
        if (CollectionUtils.isEmpty(schoolZoneIds)) {
            log.warn("当前用户身份取到的可见校区为空，loginUser={}", loginUser);
            return Lists.newArrayList();
        }
        ExtUserQuery extUserQuery = new ExtUserQuery();
        extUserQuery.setDeptIds(schoolZoneIds);
        extUserQuery.setIsTeacher(AppConstants.intYes);
        List<DingUser> list = dingUserQueryService.list(extUserQuery);
        return ModelConvertHelper.convertList(list, UserVO.class);
    }


    @ApiOperation("置为教师")
    @PostMapping("setAsTeacher")
    public Integer setAsTeacher(@RequestParam Integer id) {
        DingUser user = dingUserRepository.get(id);
        if (user == null) {
            return -1;
        }
        if (user.getIsTeacher() != null && user.getIsTeacher() == AppConstants.intYes) {
            return 0;
        }
        user.setIsTeacher(AppConstants.intYes);
        return dingUserRepository.update(user);
    }

    @ApiOperation("置为非教师")
    @PostMapping("setAsNotTeacher")
    public Integer setAsNotTeacher(@RequestParam Integer id) {
        DingUser user = dingUserRepository.get(id);
        if (user == null) {
            return -1;
        }
        if (user.getIsTeacher() != null && user.getIsTeacher() == AppConstants.intNo) {
            return 0;
        }
        user.setIsTeacher(AppConstants.intNo);
        return dingUserRepository.update(user);
    }

    @ApiOperation("置为非系统管理员")
    @PostMapping("setAsNotAppAdmin")
    @LoginRequired(superAdminOnly = true)
    public Integer setAsNotAppAdmin(@RequestParam Integer id) {
        DingUser user = dingUserRepository.get(id);
        if (user == null) {
            return -1;
        }
        if (user.getIsAppAdmin() != null && user.getIsAppAdmin() == AppConstants.intNo) {
            return 0;
        }
        user.setIsAppAdmin(AppConstants.intNo);
        return dingUserRepository.update(user);
    }

    @ApiOperation("置为系统管理员")
    @PostMapping("setAsAppAdmin")
    @LoginRequired(superAdminOnly = true)
    public Integer setAsAppAdmin(@RequestParam Integer id) {
        DingUser user = dingUserRepository.get(id);
        if (user == null) {
            return -1;
        }
        if (user.getIsAppAdmin() != null && user.getIsAppAdmin() == AppConstants.intYes) {
            return 0;
        }
        user.setIsAppAdmin(AppConstants.intYes);
        return dingUserRepository.update(user);
    }

}
