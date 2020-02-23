package com.tuofan.setting.service.impl;

import com.tuofan.setting.entity.SysCharge;
import com.tuofan.setting.mapper.SysChargeMapper;
import com.tuofan.setting.service.ISysChargeService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.tuofan.setting.vo.ChargeV;
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
public class SysChargeServiceImpl extends ServiceImpl<SysChargeMapper, SysCharge> implements ISysChargeService {

    @Override
    public List<ChargeV> listV() {
        return baseMapper.listV();
    }
}
