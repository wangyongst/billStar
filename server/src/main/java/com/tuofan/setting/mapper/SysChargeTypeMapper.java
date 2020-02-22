package com.tuofan.setting.mapper;

import com.tuofan.setting.entity.SysChargeType;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.tuofan.setting.vo.ChargeTypeV;
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
public interface SysChargeTypeMapper extends BaseMapper<SysChargeType> {

    @Select("select a.* ,b.name create_name from sys_charge_type a left join ding_user b on a.create_by = b.id")
    List<ChargeTypeV> listV();
}
