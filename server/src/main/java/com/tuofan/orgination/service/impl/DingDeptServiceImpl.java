package com.tuofan.orgination.service.impl;


import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.tuofan.orgination.entity.DingDept;
import com.tuofan.orgination.mapper.DingDeptMapper;
import com.tuofan.orgination.service.IDingDeptService;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author wangyong
 * @since 2020-02-21
 */
@Service
public class DingDeptServiceImpl extends ServiceImpl<DingDeptMapper, DingDept> implements IDingDeptService {

    @Override
    public List findByUserid(String userid) {
        return baseMapper.listByUserid(userid);
    }
}
