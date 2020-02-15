package com.tuofan.biz.sys_orgnization.domain.service;

import com.tuofan.biz.sys_orgnization.domain.dao.DingUserDao;
import com.tuofan.biz.sys_orgnization.domain.entity.DingUser;
import com.tuofan.biz.sys_orgnization.dto.query.UserQuery;
import com.tuofan.core.helper.ModelConvertHelper;
import com.tuofan.core.persistence.service.CrudRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;
import tk.mybatis.mapper.entity.Example;

import java.util.List;

@Service
@SuppressWarnings("unchecked")
public class DingUserRepository extends CrudRepository<DingUserDao, DingUser> {

    @Autowired
    DingDeptUserRepository dingDeptUserRepository;

    public Example loadQueryExample(UserQuery userQuery) {
        Example example = this.createExample();
        Example.Criteria criteria = example.and();
        if (!CollectionUtils.isEmpty(userQuery.getIds())) {
            criteria.andIn("id", userQuery.getIds());
        }

        if (!CollectionUtils.isEmpty(userQuery.getUserIds())) {
            criteria.andIn("userid", userQuery.getUserIds());
        }

//        if (!CollectionUtils.isEmpty(userQuery.getDeptSchoolIds())) {
//            criteria.andIn("deptSchoolId", userQuery.getDeptSchoolIds());
//        }

        if (!StringUtils.isEmpty(userQuery.getNameLike())) {
            criteria.andLike("name", "%" + userQuery.getNameLike() + "%");
        }

        DingUser query = ModelConvertHelper.convert(userQuery, DingUser.class);
        this.convertEntity2Example(query, example);
        return example;
    }

    public void processOtherEntityCondition(UserQuery userQuery) {
        if (CollectionUtils.isEmpty(userQuery.getDeptIds())) {
            userQuery.setStopQuery(true);
        }
        List<String> userIds = dingDeptUserRepository.listUIDByDeptIds(userQuery.getDeptIds());
        if (CollectionUtils.isEmpty(userIds)) {
            userQuery.setStopQuery(true);
        }
        userQuery.setUserIds(userIds);
    }

}
