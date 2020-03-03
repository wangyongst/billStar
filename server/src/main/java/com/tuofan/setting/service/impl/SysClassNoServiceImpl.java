package com.tuofan.setting.service.impl;

import com.tuofan.setting.entity.SysClassNo;
import com.tuofan.setting.mapper.SysClassNoMapper;
import com.tuofan.setting.service.ISysClassNoService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.tuofan.setting.vo.ClassV;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * <p>
 * 服务实现类
 * </p>
 *
 * @author wangyong
 * @since 2020-03-01
 */
@Service
public class SysClassNoServiceImpl extends ServiceImpl<SysClassNoMapper, SysClassNo> implements ISysClassNoService {

    @Override
    public List listV() {
        return baseMapper.listV();
    }
}
