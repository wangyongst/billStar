package com.tuofan.biz.sys_orgnization.domain.service;

import com.google.common.collect.Lists;
import com.tuofan.biz.sys_configs.service.ConfigCachedUtils;
import com.tuofan.biz.sys_orgnization.domain.dao.DingDeptDao;
import com.tuofan.biz.sys_orgnization.domain.entity.DingDept;
import com.tuofan.core.constants.AppConstants;
import com.tuofan.core.persistence.service.CrudRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class DingDeptRepository extends CrudRepository<DingDeptDao, DingDept> {

    @Autowired
    ConfigCachedUtils configCachedUtils;

    /**
     * 查询所有的校区
     *
     * @return 所有校区
     */
    public List<DingDept> listSchoolZone() {
        DingDept query = new DingDept();
        query.setIsSchoolZone(AppConstants.intYes);
        query.setParentid(configCachedUtils.getDingRootDeptId());
        return this.listAll(query);
    }

    /**
     * 查询所有的校区ID
     *
     * @return 所有校区ID
     */
    public List<Integer> listSchoolZoneId() {
        List<DingDept> list = this.listSchoolZone();
        if (CollectionUtils.isEmpty(list)) {
            return Lists.newArrayList();
        } else {
            return list.stream().map(DingDept::getId).collect(Collectors.toList());
        }
    }
}
