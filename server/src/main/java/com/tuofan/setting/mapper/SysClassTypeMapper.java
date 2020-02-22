package com.tuofan.setting.mapper;

import com.tuofan.setting.entity.SysClassType;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.tuofan.setting.vo.ClassTypeV;
import com.tuofan.setting.vo.SubjectV;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * <p>
 * Mapper 接口
 * </p>
 *
 * @author wangyong
 * @since 2020-02-23
 */
public interface SysClassTypeMapper extends BaseMapper<SysClassType> {

    @Select("select a.* ,b.name create_name,c.name subject_name from sys_class_type a left join ding_user b on a.create_by = b.id left join sys_subject c on a.subject_id = c.id")
    List<ClassTypeV> listV();

}
