package com.tuofan.orgination.mapper;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Constants;
import com.tuofan.orgination.entity.DingUser;
import com.tuofan.orgination.vo.TeacherV;
import org.apache.ibatis.annotations.*;

import java.util.List;

/**
 * <p>
 * Mapper 接口
 * </p>
 *
 * @author wangyong
 * @since 2020-02-21
 */
public interface DingUserMapper extends BaseMapper<DingUser> {

    @Select("select distinctrow teacher.* from ding_user teacher inner join ding_dept_user mid on teacher.userid = mid.userid inner join ding_dept school on mid.dept_id = school.id ${ew.customSqlSegment}")
    @Results(id = "teacherV", value = {
            @Result(column = "userid", property = "schoolList", javaType = List.class, many = @Many(select = "com.tuofan.orgination.mapper.DingDeptMapper.listByUserid"))
    })
    IPage<TeacherV> pageTeacher(IPage page, @Param(Constants.WRAPPER) QueryWrapper wrapper);

    @Select("select distinctrow teacher.* from ding_user teacher right join ding_dept_user mid on teacher.userid = mid.userid ${ew.customSqlSegment} ")
    List<DingUser> listAllTeacher(@Param(Constants.WRAPPER) QueryWrapper wrapper);
}
