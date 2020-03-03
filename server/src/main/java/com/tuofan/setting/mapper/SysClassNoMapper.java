package com.tuofan.setting.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.tuofan.setting.entity.SysClassNo;
import com.tuofan.setting.vo.ClassNoV;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * <p>
 * Mapper 接口
 * </p>
 *
 * @author wangyong
 * @since 2020-03-01
 */
public interface SysClassNoMapper extends BaseMapper<SysClassNo> {

    @Select("select a.* ,b.name create_name from sys_class_no a left join ding_user b on a.create_by = b.id")
    List<ClassNoV> listV();

}
