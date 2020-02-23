package com.tuofan.orgination.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.tuofan.orgination.entity.DingUser;

import java.util.List;

/**
 * <p>
 * 服务类
 * </p>
 *
 * @author wangyong
 * @since 2020-02-21
 */
public interface IDingUserService extends IService<DingUser> {

    DingUser getByUserId(String userId);

    IPage pageTeacher(IPage page, QueryWrapper queryWrapper);

    List listAllTeacher(QueryWrapper queryWrapper);

}
