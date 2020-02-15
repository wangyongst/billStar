package com.tuofan.biz.sys_orgnization.application.syn;

import com.google.common.collect.Lists;
import com.tuofan.biz.sys_ding.request.RoleRequest;
import com.tuofan.biz.sys_ding.response.base.HasMosResponse;
import com.tuofan.biz.sys_orgnization.domain.entity.DingRole;
import com.tuofan.biz.sys_orgnization.domain.entity.DingRoleGroup;
import com.tuofan.biz.sys_orgnization.domain.service.DingRoleGroupRepository;
import com.tuofan.biz.sys_orgnization.domain.service.DingRoleRepository;
import com.tuofan.biz.sys_orgnization.dto.SynResultVO;
import com.tuofan.core.exception.BizException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Slf4j
@Component
public class DingRoleSynService {

    @Autowired
    RoleRequest roleRequest;

    @Autowired
    DingRoleGroupRepository dingRoleGroupRepository;

    @Autowired
    DingRoleRepository dingRoleRepository;

    public List<SynResultVO> syn() {
        List<SynResultVO> result = Lists.newArrayList();
        List<DingRoleGroup> groups = Lists.newArrayList();
        // 循环请求
        int offset = 0;
        int size = 100;
        boolean hasMore = true;
        while (hasMore) {
            HasMosResponse<DingRoleGroup> response = roleRequest.listHasMoreAll(size, offset);
            if (response == null) {
                throw new BizException("ErrorInPost", "result is null");
            }
            hasMore = response.isHasMore();
            offset += size;
            groups.addAll(response.getList());
        }
        log.info("groups:{}", groups);
        if (CollectionUtils.isEmpty(groups)) {
            result.add(new SynResultVO("没有拉到待同步数据"));
            return result;
        }
        result.addAll(handleSyn(groups));
        return result;
    }

    @Transactional(rollbackFor = Exception.class)
    public List<SynResultVO> handleSyn(List<DingRoleGroup> groups) {
        List<SynResultVO> result = Lists.newArrayList();
        result.add(this.handleRoleGroupSyn(groups));
        result.add(this.handleRoleSyn(groups));
        return result;
    }

    /**
     * 处理权限组同步
     *
     * @param groups 同步到的数据
     */
    private SynResultVO handleRoleGroupSyn(List<DingRoleGroup> groups) {
        List<DingRoleGroup> existGroups = dingRoleGroupRepository.listAll(null);
        if (CollectionUtils.isEmpty(existGroups)) {
            dingRoleGroupRepository.createList(groups);
            return new SynResultVO(DingRoleGroup.class.getName(), groups.size(), 0, null);
        }
        Map<Integer, DingRoleGroup> existGroupMap = existGroups.stream().collect(Collectors.toMap(DingRoleGroup::getGroupId, x -> x));
        List<DingRoleGroup> insertList = Lists.newArrayList();
        List<DingRoleGroup> updateList = Lists.newArrayList();
        for (DingRoleGroup group : groups) {
            if (!existGroupMap.containsKey(group.getGroupId())) {
                insertList.add(group);
                continue;
            }
            DingRoleGroup existGroup = existGroupMap.get(group.getGroupId());
            if (!group.getName().equals(existGroup.getName())) {
                updateList.add(group);
            }
        }
        if (!CollectionUtils.isEmpty(insertList)) {
            dingRoleGroupRepository.createList(insertList);
        }
        if (!CollectionUtils.isEmpty(updateList)) {
            dingRoleGroupRepository.updateList(updateList);
        }
        return new SynResultVO(DingRoleGroup.class.getName(), insertList.size(), updateList.size(), null);
    }

    /**
     * 处理权限同步
     *
     * @param groups 同步到的数据
     */
    private SynResultVO handleRoleSyn(List<DingRoleGroup> groups) {
        List<DingRole> roles = Lists.newArrayList();
        for (DingRoleGroup group : groups) {
            group.getRoles().forEach(ele -> ele.setGroupId(group.getGroupId()));
            roles.addAll(group.getRoles());
        }
        List<DingRole> existRoles = dingRoleRepository.listAll(null);
        if (CollectionUtils.isEmpty(existRoles)) {
            dingRoleRepository.createList(roles);
            return new SynResultVO(DingRole.class.getName(), roles.size(), 0, null);
        }
        Map<Integer, DingRole> existRoleMap = existRoles.stream().collect(Collectors.toMap(DingRole::getId, x -> x));
        List<DingRole> insertList = Lists.newArrayList();
        List<DingRole> updateList = Lists.newArrayList();
        for (DingRole role : roles) {
            if (!existRoleMap.containsKey(role.getId())) {
                insertList.add(role);
                continue;
            }
            DingRole existRole = existRoleMap.get(role.getId());
            if (!existRole.getName().equals(role.getName())) {
                updateList.add(role);
            }
        }
        if (!CollectionUtils.isEmpty(insertList)) {
            dingRoleRepository.createList(insertList);
        }
        if (!CollectionUtils.isEmpty(updateList)) {
            dingRoleRepository.updateList(updateList);
        }
        return new SynResultVO(DingRole.class.getName(), insertList.size(), updateList.size(), null);
    }

}
