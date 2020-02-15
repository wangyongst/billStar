package com.tuofan.biz.sys_orgnization.domain.service;

import com.google.common.collect.Lists;
import com.tuofan.biz.sys_orgnization.domain.dao.DingDeptUserDao;
import com.tuofan.biz.sys_orgnization.domain.entity.DingDeptUser;
import com.tuofan.core.persistence.service.CrudRepository;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import tk.mybatis.mapper.entity.Example;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class DingDeptUserRepository extends CrudRepository<DingDeptUserDao, DingDeptUser> {

    public List<DingDeptUser> listByDeptIds(List<Integer> deptIds) {
        if (CollectionUtils.isEmpty(deptIds)) {
            return Lists.newArrayList();
        }
        Example example = this.createExample();
        Example.Criteria criteria = example.and();
        criteria.andIn("deptId", deptIds);
        return this.listAllByExample(example);
    }

    public List<String> listUIDByDeptIds(List<Integer> deptIds) {
        if (CollectionUtils.isEmpty(deptIds)) {
            return Lists.newArrayList();
        }
        Example example = this.createExample();
        Example.Criteria criteria = example.and();
        criteria.andIn("deptId", deptIds);
        List<DingDeptUser> list = this.listAllByExample(example);
        return list.stream().map(DingDeptUser::getUserId).collect(Collectors.toList());
    }

    public List<DingDeptUser> listByUserIds(List<String> userIds) {
        if (CollectionUtils.isEmpty(userIds)) {
            return Lists.newArrayList();
        }
        Example example = this.createExample();
        Example.Criteria criteria = example.and();
        criteria.andIn("userId", userIds);
        return this.listAllByExample(example);
    }
}
