package com.tuofan.setting.service;

import com.tuofan.setting.entity.SysChargeType;
import com.baomidou.mybatisplus.extension.service.IService;
import com.tuofan.setting.vo.ChargeTypeV;

import java.util.List;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author wangyong
 * @since 2020-02-22
 */
public interface ISysChargeTypeService extends IService<SysChargeType> {

    List<ChargeTypeV> listV();

}
