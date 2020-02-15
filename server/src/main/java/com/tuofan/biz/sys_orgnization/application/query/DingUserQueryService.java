package com.tuofan.biz.sys_orgnization.application.query;

import com.github.pagehelper.PageInfo;
import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import com.tuofan.biz.sys_orgnization.domain.entity.*;
import com.tuofan.biz.sys_orgnization.domain.service.*;
import com.tuofan.biz.sys_orgnization.dto.UserVO;
import com.tuofan.biz.sys_orgnization.dto.query.ExtUserQuery;
import com.tuofan.core.advice.convert.FieldConvertUtils;
import com.tuofan.core.constants.AppConstants;
import com.tuofan.core.dto.PageRequest;
import com.tuofan.core.exception.BizException;
import com.tuofan.core.helper.ModelConvertHelper;
import com.tuofan.core.utils.StringUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;
import tk.mybatis.mapper.entity.Example;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@Slf4j
@Component
@SuppressWarnings("unchecked")
public class DingUserQueryService {

    @Autowired
    private DingUserRepository dingUserRepository;

    @Autowired
    private DingRoleUserRepository dingRoleUserRepository;

    @Autowired
    private DingDeptUserRepository dingDeptUserRepository;

    @Autowired
    private DingRoleRepository dingRoleRepository;

    @Autowired
    private DingDeptRepository dingDeptRepository;

    public DingUser get(Integer id) {
        return dingUserRepository.get(id);
    }

    public UserVO getVO(Integer id) {
        DingUser entity = dingUserRepository.get(id);
        UserVO userVO = ModelConvertHelper.convert(entity, UserVO.class);
        this.packageUserDept(Lists.newArrayList(userVO));
        return userVO;
    }

    public List<DingUser> list(ExtUserQuery extUserQuery) {
        dingUserRepository.processOtherEntityCondition(extUserQuery);
        Example example = dingUserRepository.loadQueryExample(extUserQuery);
        return dingUserRepository.listAllByExample(example);
    }

    public PageInfo<UserVO> listPage(PageRequest<ExtUserQuery> request) {
        if (request == null) {
            request = new PageRequest<>();
        }
        if (request.getData() == null) {
            request.setData(new ExtUserQuery());
        }
        ExtUserQuery extUserQuery = request.getData();
        dingUserRepository.processOtherEntityCondition(extUserQuery);
        if (extUserQuery.isStopQuery()) {
            return new PageInfo<>();
        }
        Example example = dingUserRepository.loadQueryExample(extUserQuery);
        PageRequest<Example> examplePageRequest = ModelConvertHelper.convert(request, PageRequest.class);
        examplePageRequest.setData(example);
        PageInfo<DingUser> page = dingUserRepository.listPageByExample(examplePageRequest);
        // convert & improve result
        PageInfo<UserVO> pageResult = ModelConvertHelper.convert(page, PageInfo.class);
        List<UserVO> resultList = ModelConvertHelper.convertList(page.getList(), UserVO.class);
        //
        this.packageUserDept(resultList);
        pageResult.setList(resultList);
        return pageResult;
    }

    private void packageUserVO(List<UserVO> list) {
        this.packageUserVORole(list);
    }

    private void packageUserDept(List<UserVO> list) {
        // query role & dept
        List<String> userIds = list.stream().map(UserVO::getUserid).collect(Collectors.toList());
        List<DingDeptUser> deptUserRelations = dingDeptUserRepository.listByUserIds(userIds);
        Set<Integer> deptIds = deptUserRelations.stream().map(DingDeptUser::getDeptId).collect(Collectors.toSet());
        List<DingDept> depts = dingDeptRepository.listByIds(Lists.newArrayList(deptIds));
        // map entities
        Map<String, UserVO> userMap = list.stream().collect(Collectors.toMap(UserVO::getUserid, x -> x));
        Map<Integer, DingDept> deptMap = depts.stream().collect(Collectors.toMap(DingDept::getId, x -> x));
        // package
        for (DingDeptUser ele : deptUserRelations) {
            if (!userMap.containsKey(ele.getUserId())) {
                continue;
            }
            if (!deptMap.containsKey(ele.getDeptId())) {
                continue;
            }
            UserVO user = userMap.get(ele.getUserId());
            DingDept dept = deptMap.get(ele.getDeptId());
            if (CollectionUtils.isEmpty(user.getDepts())) {
                user.setDepts(Lists.newArrayList());
                user.setDeptSchoolIds(Lists.newArrayList());
                user.setDeptSchools(Lists.newArrayList());
            }
            user.getDepts().add(dept);
            if (dept.getIsSchoolZone().equals(AppConstants.intYes)) {
                //todo 如果部门是叶子节点，那么需要处理其上级部门校区
                user.getDeptSchools().add(dept);
                user.getDeptSchoolIds().add(dept.getId());
            }
        }
    }

    private void packageUserVORole(List<UserVO> list) {
        // query role & dept
//        List<String> userIds = list.stream().map(UserVO::getUserid).collect(Collectors.toList());
//        List<DingRoleUser> roleUserRelations = dingRoleUserRepository.listByUserIds(userIds);
//        Set<Integer> roleIds = roleUserRelations.stream().map(DingRoleUser::getRoleId).collect(Collectors.toSet());
//        List<DingRole> roles = dingRoleRepository.listByRoleIds(Lists.newArrayList(roleIds));
//         map entities
//        Map<String, UserVO> userMap = list.stream().collect(Collectors.toMap(UserVO::getUserid, x -> x));
//        Map<Integer, DingRole> roleMap = roles.stream().collect(Collectors.toMap(DingRole::getId, x -> x));
        // package
//        for (DingRoleUser ele : roleUserRelations) {
//            if (!userMap.containsKey(ele.getUserid())) {
//                continue;
//            }
//            if (!roleMap.containsKey(ele.getRoleId())) {
//                continue;
//            }
//            UserVO user = userMap.get(ele.getUserid());
//            DingRole role = roleMap.get(ele.getRoleId());
//            if (null == user.getRoles()) {
//                user.setRoles(Sets.newHashSet());
//            }
//            user.getRoles().add(ModelConvertHelper.convert(role, RoleVO.class));
//        }
//        // 标记管理员
//        list.forEach(ele -> {
//            if (!CollectionUtils.isEmpty(ele.getRoles())) {
//                ele.getRoles().forEach(role -> {
//                    if (role.getName().equals(AppConstants.adminRoleName)) {
//                        ele.setIsAdmin(AppConstants.yes);
//                    }
//                });
//            }
//        });
    }

//    /**
//     * dept role to user query
//     *
//     * @param extQuery
//     * @return
//     */
//    private UserQuery generateUserQuery(ExtUserQuery extQuery) {
//        UserQuery userQuery = ModelConvertHelper.convert(extQuery, UserQuery.class);
//        userQuery.setStopQuery(false);
//        return userQuery;
//    }


    public UserVO getVOByUserId(String userId) {
        DingUser query = new DingUser();
        query.setUserid(userId);
        DingUser dingUser = dingUserRepository.get(query);
        if (dingUser == null) {
            log.error("userId:{}", userId);
            throw new BizException("606", "系统查无此用户");
        }
        UserVO userVO = ModelConvertHelper.convert(dingUser, UserVO.class);
        userVO.initCollectionElements();
        this.packageUserDept(Lists.newArrayList(userVO));
        FieldConvertUtils.convertList(Lists.newArrayList(userVO));
        return userVO;
    }

    public Set<String> listUserIdByRoleName(String roleName) {
        DingRole role = dingRoleRepository.getByName(roleName);
        if (role == null) {
            return Sets.newHashSet();
        }
        List<DingRoleUser> list = dingRoleUserRepository.listByRoleIds(Lists.newArrayList(role.getId()));
        if (CollectionUtils.isEmpty(list)) {
            return Sets.newHashSet();
        }
        return list.stream().map(DingRoleUser::getUserid).collect(Collectors.toSet());
    }

    public List<DingUser> listByNameLike(String nameLike) {
        if (StringUtils.isEmpty(nameLike)) {
            return Lists.newArrayList();
        }
        Example example = dingUserRepository.createExample();
        Example.Criteria criteria = example.and();
        criteria.andLike("name", "%" + nameLike + "%");
        return dingUserRepository.listAllByExample(example);
    }

    public List<Integer> listUserIdByNameLike(String nameLike) {
        List<DingUser> users = this.listByNameLike(nameLike);
        return users.stream().map(DingUser::getId).collect(Collectors.toList());
    }

}
