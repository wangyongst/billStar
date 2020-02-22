package com.tuofan.setting.service.impl;

import com.tuofan.setting.entity.SysChargeType;
import com.tuofan.setting.mapper.SysChargeTypeMapper;
import com.tuofan.setting.service.ISysChargeTypeService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.tuofan.setting.vo.ChargeTypeV;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * <p>
 * 服务实现类
 * </p>
 *
 * @author wangyong
 * @since 2020-02-22
 */
@Service
public class SysChargeTypeServiceImpl extends ServiceImpl<SysChargeTypeMapper, SysChargeType> implements ISysChargeTypeService {

    @Override
    public List<ChargeTypeV> listV() {
        return baseMapper.listV();
    }
}
