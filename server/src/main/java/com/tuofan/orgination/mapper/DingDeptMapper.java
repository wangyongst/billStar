package com.tuofan.orgination.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.tuofan.orgination.entity.DingDept;
import com.tuofan.orgination.entity.DingDeptUser;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * <p>
 * Mapper 接口
 * </p>
 *
 * @author wangyong
 * @since 2020-02-21
 */
@Mapper
public interface DingDeptMapper extends BaseMapper<DingDept> {

    @Select("select school.* from ding_dept school left join ding_dept_user b on school.id = b.dept_id where b.userid = #{userid}")
    List<DingDept> listByUserid(String userid);
}
