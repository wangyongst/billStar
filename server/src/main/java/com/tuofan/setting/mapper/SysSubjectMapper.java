package com.tuofan.setting.mapper;

import com.tuofan.setting.entity.SysSubject;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.tuofan.setting.vo.SubjectV;
import org.apache.ibatis.annotations.Select;

import javax.security.auth.Subject;
import java.util.List;

/**
 * <p>
 * Mapper 接口
 * </p>
 *
 * @author wangyong
 * @since 2020-02-22
 */
public interface SysSubjectMapper extends BaseMapper<SysSubject> {

    @Select("select a.* ,b.name create_name from sys_subject a left join ding_user b on a.create_by = b.id")
    List<SubjectV> listV();

}
