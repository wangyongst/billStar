package com.tuofan.biz.sys_orgnization.domain.service;

import com.tuofan.biz.sys_configs.constants.ConfigNameConstants;
import com.tuofan.biz.sys_configs.service.ConfigCachedUtils;
import com.tuofan.biz.sys_orgnization.domain.dao.DingRoleGroupDao;
import com.tuofan.biz.sys_orgnization.domain.entity.DingRoleGroup;
import com.tuofan.core.exception.BizException;
import com.tuofan.core.persistence.service.CrudRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class DingRoleGroupRepository extends CrudRepository<DingRoleGroupDao, DingRoleGroup> {

    @Autowired
    private ConfigCachedUtils configCachedUtils;

    public DingRoleGroup getByName(String name) {
        // 针对应用角色做过滤
        DingRoleGroup query = new DingRoleGroup();
        query.setName(name);
        return this.get(query);
    }

    public Integer getAppGroupId(){
        String parentRoleName = configCachedUtils.getValue(ConfigNameConstants.appRoleGroupName);
        if (parentRoleName != null) {
            DingRoleGroup roleParent = this.getByName(parentRoleName);
            if (roleParent == null) {
                throw new BizException("808", "应用角色组没有同步过来");
            }
            return roleParent.getGroupId();
        }else{
            throw new BizException("807", "请在配置表设置应用角色组");
        }
    }

}
