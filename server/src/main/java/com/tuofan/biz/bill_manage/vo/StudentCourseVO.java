package com.tuofan.biz.bill_manage.vo;

import com.tuofan.biz.bill_manage.entity.Course;
import com.tuofan.biz.bill_manage.entity.Student;
import com.tuofan.biz.sys_orgnization.domain.service.DingDeptRepository;
import com.tuofan.core.advice.convert.Converted;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;

@Data
public class StudentCourseVO {

    @ApiModelProperty("数据ID")
    private Integer id;

    @ApiModelProperty("校区ID")
    private Integer deptSchoolId;

    @ApiModelProperty("校区名字")
    @Converted(dependProperty = "deptSchoolId", bean = DingDeptRepository.class)
    private String deptSchoolName;

    private Integer studentId;

    private Integer courseId;

    private Integer isActive;

    private Date beginTime; // 开始时间

    private Date expireTime; // 过期时间

    private Date riseClassTime; // 升班时间

    private String remark; // 备注信息

    @ApiModelProperty
    private Student student;

    @ApiModelProperty
    private Course course;

}
