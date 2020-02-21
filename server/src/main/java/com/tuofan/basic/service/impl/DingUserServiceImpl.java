package com.tuofan.basic.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.tuofan.basic.entity.DingUser;
import com.tuofan.basic.mapper.DingUserMapper;
import com.tuofan.basic.service.IDingUserService;
import org.springframework.stereotype.Service;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author wangyong
 * @since 2020-02-21
 */
@Service
public class DingUserServiceImpl extends ServiceImpl<DingUserMapper, DingUser> implements IDingUserService {

}
