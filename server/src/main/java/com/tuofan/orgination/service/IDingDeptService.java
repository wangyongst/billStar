package com.tuofan.orgination.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.tuofan.orgination.entity.DingDept;

import java.util.List;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author wangyong
 * @since 2020-02-21
 */
public interface IDingDeptService extends IService<DingDept> {

    List findByUserid(String userid);

}
