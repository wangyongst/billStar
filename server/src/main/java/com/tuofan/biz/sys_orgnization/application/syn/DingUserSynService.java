package com.tuofan.biz.sys_orgnization.application.syn;

import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import com.tuofan.biz.sys_configs.service.ConfigCachedUtils;
import com.tuofan.biz.sys_ding.request.DeptUserRequest;
import com.tuofan.biz.sys_ding.response.base.DeptUserListResponse;
import com.tuofan.biz.sys_orgnization.domain.entity.DingDept;
import com.tuofan.biz.sys_orgnization.domain.entity.DingDeptUser;
import com.tuofan.biz.sys_orgnization.domain.entity.DingUser;
import com.tuofan.biz.sys_orgnization.domain.service.DingDeptRepository;
import com.tuofan.biz.sys_orgnization.domain.service.DingDeptUserRepository;
import com.tuofan.biz.sys_orgnization.domain.service.DingRoleGroupRepository;
import com.tuofan.biz.sys_orgnization.domain.service.DingUserRepository;
import com.tuofan.biz.sys_orgnization.dto.SynResultVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@Slf4j
@Component
public class DingUserSynService {

    @Autowired
    DeptUserRequest deptUserRequest;

    @Autowired
    DingRoleGroupRepository dingRoleGroupRepository;

    @Autowired
    DingDeptRepository dingDeptRepository;

    @Autowired
    DingUserRepository dingUserRepository;

    @Autowired
    DingDeptUserRepository dingDeptUserRepository;

    @Autowired
    ConfigCachedUtils configCachedUtils;

    public List<SynResultVO> syn() {
        List<DingDept> deptList = dingDeptRepository.listAll(null);
        if (CollectionUtils.isEmpty(deptList)) {
            return Lists.newArrayList(new SynResultVO(DingUser.class.getName(), 0, 0, "noDeptToSynUser"));
        }
        List<DingUser> list = Lists.newArrayList();
        for (DingDept dept : deptList) {
            Integer deptId = dept.getId();
            Integer size = 100;
            Integer offset = 0;
            boolean hasMore = true;
            while (hasMore) {
                DeptUserListResponse response = deptUserRequest.listAll(deptId, offset, size);
                if (response.getErrcode() != 0) {
                    hasMore = false;
                    log.error("同步部门用户报错：{}", response);
                } else {
                    hasMore = response.isHasMore();
                    offset += size;
                    list.addAll(response.getUserlist());
                }
            }
        }
        if (CollectionUtils.isEmpty(list)) {
            return Lists.newArrayList(new SynResultVO(DingUser.class.getName(), 0, 0, "noUserFetched"));
        }
        Set<String> dingUserIds = Sets.newHashSet();
        // todo 这里需要处理 isLeader ，用户在A部门是leader，在B部门不是，然后会有重复
        List<DingUser> uniqueUsers = list.stream().filter(ele -> {
                    boolean contain = dingUserIds.contains(ele.getUserid());
                    dingUserIds.add(ele.getUserid());
                    return !contain;
                }
        ).collect(Collectors.toList());
        this.processUserDeptSchool(uniqueUsers, deptList);
        return handleSyn(uniqueUsers);
    }

    @Transactional(rollbackFor = Exception.class)
    public List<SynResultVO> handleSyn(List<DingUser> users) {
        List<SynResultVO> result = Lists.newArrayList();
        result.add(handleUserSyn(users));
        result.add(handleUserDeptSyn(users));
        return result;
    }

    private void processUserDeptSchool(List<DingUser> users, List<DingDept> depts) {
//        考虑跨部门，校区ID不用处理了
        //处理用户的校区问题
//        Map<Integer, DingDept> deptMap = depts.stream().collect(Collectors.toMap(DingDept::getId, x -> x));
//        for (DingUser user : users) {
//            user.setIsTeacher(AppConstants.intNo);
//            for (Integer deptId : user.getDepartment()) {
//                if (!deptMap.containsKey(deptId)) {
//                    continue;
//                }
//                if (user.getDeptSchoolId() != null && user.getDeptSchoolId() > 0) {
//                    break;
//                }
//                DingDept dept = deptMap.get(deptId);
//                int loopCnt = 0;
//                while ((user.getDeptSchoolId() == null || user.getDeptSchoolId() <= 0) && dept != null && loopCnt < 10) {
//                    if (dept.getParentid() != null && dept.getParentid().equals(configCachedUtils.getDingRootDeptId())) {
//                        user.setDeptSchoolId(dept.getId());
//                        user.setIsTeacher(AppConstants.intYes);
//                    } else {
//                        dept = deptMap.get(dept.getParentid());
//                    }
//                    loopCnt++;
//                }
//            }
//        }
    }

    /**
     * 用户处理
     *
     * @param users
     * @return
     */
    private SynResultVO handleUserSyn(List<DingUser> users) {


        List<DingUser> existUsers = dingUserRepository.listAll(null);
        // 只有新增
        if (CollectionUtils.isEmpty(existUsers)) {
            dingUserRepository.createList(users);
            return new SynResultVO(DingUser.class.getName(), 0, 0, null);
        }
        // 有新增和更新和删除
        List<DingUser> insertList = Lists.newArrayList();
        List<DingUser> updateList = Lists.newArrayList();
//        List<DingUser> deleteList = Lists.newArrayList();
        Map<String, DingUser> existUserMap = existUsers.stream().collect(Collectors.toMap(DingUser::getUserid, x -> x));
        // 遍历执行增删改
        for (DingUser user : users) {
            if (existUserMap.containsKey(user.getUserid())) {
                // 作比较
                DingUser existUser = existUserMap.get(user.getUserid());
                if (!user.toString().equals(existUser.toString())) {
                    user.setId(existUser.getId());
                    updateList.add(user);
                }
                continue;
            }
            user.initFieldsNotInDingDing();
            insertList.add(user);
        }
        if (!CollectionUtils.isEmpty(insertList)) {
            dingUserRepository.createList(insertList);
        }
        if (!CollectionUtils.isEmpty(updateList)) {
            dingUserRepository.updateList(updateList);
        }
        return new SynResultVO(DingUser.class.getName(), insertList.size(), updateList.size(), null);
    }

    /**
     * 用户与部门的关系处理
     *
     * @param users 用户数据,需要保证不是重复的
     * @return
     */
    private SynResultVO handleUserDeptSyn(List<DingUser> users) {
        List<DingDeptUser> list = Lists.newArrayList();
        users.forEach(ele ->
                ele.getDepartment().forEach(item ->
                        list.add(new DingDeptUser(ele.getUserid(), item))
                )
        );
        if (CollectionUtils.isEmpty(list)) {
            return new SynResultVO(DingDeptUser.class.getName(), 0, 0, "NoData");
        }
        List<DingDeptUser> existDeptUsers = dingDeptUserRepository.listAll(null);
        List<String> allKeys = list.stream().map(DingDeptUser::toString).collect(Collectors.toList());
        Map<String, DingDeptUser> existKeyMap = existDeptUsers.stream().collect(Collectors.toMap(DingDeptUser::toString, x -> x));
        List<DingDeptUser> insertList = Lists.newArrayList();
        List<DingDeptUser> deleteList = Lists.newArrayList();
        for (DingDeptUser model : list) {
            if (!existKeyMap.containsKey(model.toString())) {
                insertList.add(model);
            }
        }
        for (DingDeptUser model : existDeptUsers) {
            if (!allKeys.contains(model.toString())) {
                deleteList.add(model);
            }
        }
        if (!CollectionUtils.isEmpty(insertList)) {
            dingDeptUserRepository.createList(insertList);
        }

        if (!CollectionUtils.isEmpty(deleteList)) {
            dingDeptUserRepository.deleteList(deleteList);
        }
        return new SynResultVO(DingDeptUser.class.getName(), insertList.size(), deleteList.size(), null);
    }


}
