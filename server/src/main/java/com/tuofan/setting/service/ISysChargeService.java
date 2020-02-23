package com.tuofan.setting.service;

import com.tuofan.setting.entity.SysCharge;
import com.baomidou.mybatisplus.extension.service.IService;
import com.tuofan.setting.vo.ChargeV;

import java.util.List;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author wangyong
 * @since 2020-02-22
 */
public interface ISysChargeService extends IService<SysCharge> {

    List<ChargeV> listV();

}
