package com.tuofan.biz.sys_orgnization.domain.service;

import com.google.common.collect.Lists;
import com.tuofan.biz.sys_orgnization.domain.dao.DingRoleUserDao;
import com.tuofan.biz.sys_orgnization.domain.entity.DingRoleUser;
import com.tuofan.core.persistence.service.CrudRepository;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import tk.mybatis.mapper.entity.Example;

import java.util.List;

@Service
public class DingRoleUserRepository extends CrudRepository<DingRoleUserDao, DingRoleUser> {

    public List<DingRoleUser> listByRoleIds(List<Integer> roleIds) {
        if(CollectionUtils.isEmpty(roleIds)){
            return Lists.newArrayList();
        }
        Example example = this.createExample();
        Example.Criteria criteria = example.and();
        criteria.andIn("roleId",roleIds);
        return this.listAllByExample(example);
    }

    public List<DingRoleUser> listByUserIds(List<String> userIds) {
        if (CollectionUtils.isEmpty(userIds)) {
            return Lists.newArrayList();
        }
        Example example = this.createExample();
        Example.Criteria criteria = example.and();
        criteria.andIn("userid", userIds);
        return this.listAllByExample(example);
    }

}
