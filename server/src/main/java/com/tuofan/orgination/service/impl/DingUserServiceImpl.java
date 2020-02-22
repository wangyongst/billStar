package com.tuofan.orgination.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.tuofan.orgination.entity.DingUser;
import com.tuofan.orgination.mapper.DingUserMapper;
import com.tuofan.orgination.service.IDingUserService;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 服务实现类
 * </p>
 *
 * @author wangyong
 * @since 2020-02-21
 */
@Service
public class DingUserServiceImpl extends ServiceImpl<DingUserMapper, DingUser> implements IDingUserService {

    @Override
    public DingUser getByUserId(String userId) {
        QueryWrapper query = new QueryWrapper();
        query.eq("user_id", userId);
        return baseMapper.selectOne(query);
    }
}
