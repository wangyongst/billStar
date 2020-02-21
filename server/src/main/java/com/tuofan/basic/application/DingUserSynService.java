package com.tuofan.basic.application;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import com.tuofan.configs.service.ConfigCachedUtils;
import com.tuofan.core.Result;
import com.tuofan.ding.request.DeptUserRequest;
import com.tuofan.ding.response.base.DeptMemberResponse;
import com.tuofan.ding.response.base.DeptUserListResponse;
import com.tuofan.basic.entity.DingDept;
import com.tuofan.basic.entity.DingDeptUser;
import com.tuofan.basic.entity.DingUser;
import com.tuofan.basic.service.IDingDeptService;
import com.tuofan.basic.service.IDingDeptUserService;
import com.tuofan.basic.service.IDingUserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@Slf4j
@Component
public class DingUserSynService {

    @Autowired
    private DeptUserRequest deptUserRequest;

    @Autowired
    private IDingDeptService iDingDeptService;

    @Autowired
    IDingUserService iDingUserService;

    @Autowired
    IDingDeptUserService iDingDeptUserService;

    @Autowired
    ConfigCachedUtils configCachedUtils;

    public Result syn() {
        List<DingDept> deptList = iDingDeptService.list();
        if (CollectionUtils.isEmpty(deptList)) {
            return Result.error("没有数据需要同步处理");
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
                } else {
                    hasMore = response.isHasMore();
                    offset += size;
                    list.addAll(response.getUserlist());
                }
            }
        }
        if (CollectionUtils.isEmpty(list)) {
            return Result.error("没有数据需要同步处理");
        }
        Set<String> dingUserIds = Sets.newHashSet();
        // todo 这里需要处理 isLeader ，用户在A部门是leader，在B部门不是，然后会有重复
        List<DingUser> uniqueUsers = list.stream().filter(ele -> {
                    boolean contain = dingUserIds.contains(ele.getUserId());
                    dingUserIds.add(ele.getUserId());
                    return !contain;
                }
        ).collect(Collectors.toList());
        handleUserSyn(uniqueUsers);
        handleUserDeptSyn(deptList);
        return Result.ok();
    }

    private void handleUserSyn(List<DingUser> users) {
        List<DingUser> existUsers = iDingUserService.list();
        if (CollectionUtils.isEmpty(existUsers)) {
            iDingUserService.saveBatch(users);
        }
        List<DingUser> insertList = Lists.newArrayList();
        List<DingUser> updateList = Lists.newArrayList();
        Map<String, DingUser> existUserMap = existUsers.stream().collect(Collectors.toMap(DingUser::getUserId, x -> x));
        for (DingUser user : users) {
            if (!existUserMap.containsKey(user.getUserId())) {
                insertList.add(user);
                continue;
            }
            DingUser existEle = existUserMap.get(user.getUserId());
            updateList.add(makeUser(user, existEle));
        }
        if (!CollectionUtils.isEmpty(insertList)) {
            iDingUserService.saveBatch(insertList);
        }
        if (!CollectionUtils.isEmpty(updateList)) {
            iDingUserService.saveOrUpdateBatch(updateList);
        }
    }

    public DingUser makeUser(DingUser o, DingUser t) {
        o.setIsAppAdmin(t.getIsAppAdmin());
        o.setIsTeacher(t.getIsTeacher());
        return o;
    }


    private void handleUserDeptSyn(List<DingDept> deptList) {
        QueryWrapper query = new QueryWrapper();
        query.eq("1", 1);
        iDingDeptUserService.remove(query);
        List<DingDeptUser> dduList = Lists.newArrayList();
        for (DingDept dd : deptList) {
            DeptMemberResponse dmr = deptUserRequest.listAllMember(dd.getId());
            for (String userid : dmr.getUserIds()) {
                DingDeptUser ddu = new DingDeptUser();
                ddu.setDeptId(dd.getId());
                ddu.setUserId(userid);
                dduList.add(ddu);
            }
        }

        iDingDeptUserService.saveBatch(dduList);
    }
}
