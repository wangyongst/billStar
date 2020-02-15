package com.tuofan.biz.sys_orgnization.domain.service;

import com.tuofan.biz.sys_configs.service.ConfigCachedUtils;
import com.tuofan.biz.sys_orgnization.domain.dao.DingRoleDao;
import com.tuofan.biz.sys_orgnization.domain.entity.DingRole;
import com.tuofan.core.constants.AppConstants;
import com.tuofan.core.persistence.service.CrudRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class DingRoleRepository extends CrudRepository<DingRoleDao, DingRole> {

    @Autowired
    private ConfigCachedUtils configCachedUtils;

    @Autowired
    private DingRoleGroupRepository dingRoleGroupRepository;

    public List<DingRole> listByRoleIds(ArrayList<Integer> ids) {
        // 针对应用角色做过滤
        List<DingRole> roles = this.listByIds(ids);
        return filterRoles(roles);
    }

    private List<DingRole> filterRoles(List<DingRole> roles) {
        final Integer groupIdFinal = dingRoleGroupRepository.getAppGroupId();
        return roles.stream().filter(ele ->
                ele.getGroupId().intValue() == groupIdFinal.intValue()
        ).collect(Collectors.toList());
    }

    public DingRole getByName(String name) {
        // 针对应用角色做过滤
        DingRole query = new DingRole();
        query.setGroupId(dingRoleGroupRepository.getAppGroupId());
        query.setName(AppConstants.adminRoleName);
        return this.get(query);
    }


}
