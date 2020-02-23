package com.tuofan.setting.mapper;

import com.tuofan.setting.entity.SysCharge;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.tuofan.setting.vo.ChargeV;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * <p>
 * Mapper 接口
 * </p>
 *
 * @author wangyong
 * @since 2020-02-22
 */
public interface SysChargeMapper extends BaseMapper<SysCharge> {

    @Select("select a.* ,b.name create_name from sys_charge a left join ding_user b on a.create_by = b.id")
    List<ChargeV> listV();
}
