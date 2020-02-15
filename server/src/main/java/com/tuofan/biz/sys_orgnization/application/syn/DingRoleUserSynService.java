package com.tuofan.biz.sys_orgnization.application.syn;

import com.google.common.collect.Lists;
import com.tuofan.biz.sys_ding.request.RoleUserRequest;
import com.tuofan.biz.sys_ding.response.base.HasMosResponse;
import com.tuofan.biz.sys_orgnization.domain.entity.DingRole;
import com.tuofan.biz.sys_orgnization.domain.entity.DingRoleUser;
import com.tuofan.biz.sys_orgnization.domain.service.DingRoleRepository;
import com.tuofan.biz.sys_orgnization.domain.service.DingRoleUserRepository;
import com.tuofan.biz.sys_orgnization.dto.SynResultVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.util.List;

@Slf4j
@Component
public class DingRoleUserSynService {

    @Autowired
    RoleUserRequest roleUserRequest;

    @Autowired
    DingRoleUserRepository dingRoleUserRepository;

    @Autowired
    DingRoleRepository dingRoleRepository;

    @Transactional(rollbackFor = Exception.class)
    public List<SynResultVO> syn() {
        List<SynResultVO> result = Lists.newArrayList();
        List<DingRole> list = dingRoleRepository.listAll(null);
        if (CollectionUtils.isEmpty(list)) {
            result.add(new SynResultVO(DingRoleUser.class.getName(), 0, 0, "没有角色需要同步角色用户"));
            return result;
        }
        // 循环拉用户角色数据
        List<DingRoleUser> dingRoleUsers = Lists.newArrayList();
        for (DingRole dingRole : list) {
            Integer roleId = dingRole.getId();
            Integer size = 100;
            Integer offset = 0;
            boolean hasMore = true;
            while (hasMore) {
                HasMosResponse<DingRoleUser> response = roleUserRequest.listHasMoreAll(size, offset, roleId);
                hasMore = response.isHasMore();
                offset = offset + size;
                List<DingRoleUser> tmp = response.getList();
                tmp.forEach(ele -> ele.setRoleId(roleId));
                dingRoleUsers.addAll(tmp);
            }
        }
        if (CollectionUtils.isEmpty(dingRoleUsers)) {
            result.add(new SynResultVO(DingRoleUser.class.getName(), 0, 0, "需要同步角色用户"));
            return result;
        }
        result.add(synDingRoleUser(dingRoleUsers));
        return result;
    }

    @Transactional
    public SynResultVO synDingRoleUser(List<DingRoleUser> dingRoleUsers) {
        List<DingRoleUser> existRoleUsers = dingRoleUserRepository.listAll(null);
        if (CollectionUtils.isEmpty(existRoleUsers)) {
            dingRoleUserRepository.createList(dingRoleUsers);
            return new SynResultVO(DingRoleUser.class.getName(), dingRoleUsers.size(), 0, null);
        }
        // 做重复性检测
        List<String> existKeys = Lists.newArrayList();
        for (DingRoleUser ele : existRoleUsers) {
            existKeys.add(ele.generateKey());
        }
        // 整理需要删除的数据
        List<String> allKeys = Lists.newArrayList();
        for (DingRoleUser ele : dingRoleUsers) {
            allKeys.add(ele.generateKey());
        }
        List<DingRoleUser> deleteList = Lists.newArrayList();
        for (DingRoleUser ele : existRoleUsers) {
            if (!allKeys.contains(ele.generateKey())) {
                deleteList.add(ele);
            }
        }
        List<DingRoleUser> insertList = Lists.newArrayList();
        for (DingRoleUser ele : dingRoleUsers) {
            if (!existKeys.contains(ele.generateKey())) {
                insertList.add(ele);
            }
        }
        if(!CollectionUtils.isEmpty(insertList)){
            dingRoleUserRepository.createList(insertList);
        }
        if(!CollectionUtils.isEmpty(deleteList)){
            dingRoleUserRepository.deleteList(deleteList);
        }
        return new SynResultVO(DingRoleUser.class.getName(), insertList.size(), 0, null);
    }


}
