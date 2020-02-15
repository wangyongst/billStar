package com.tuofan.biz.bill_manage.vo;

import com.tuofan.biz.bill_manage.service.SysDictService;
import com.tuofan.biz.sys_orgnization.domain.service.DingDeptRepository;
import com.tuofan.biz.sys_orgnization.domain.service.DingUserRepository;
import com.tuofan.core.advice.convert.Converted;
import io.swagger.annotations.ApiModel;
import lombok.Data;

import java.util.Date;

@Data
@ApiModel(value = "billVO", description = "开票信息")
public class BillCourseVO {

    Date expireTime;

    private Integer courseId;

    private Integer type;

    private Integer teacherId;//关联教师ID

    private Integer dictCourseId;//字典表-班级名字（启蒙）

    private Integer courseIndex;//班别（1,2,3,4,5,6...15）

    private String regularTime;//上课时间（每周三10点到12点）

    private String classroomNo;//教室号

    @Converted(dependProperty = "dictCourseId", bean = SysDictService.class)
    private String dictCourseName;

    @Converted(dependProperty = "teacherId", bean = DingUserRepository.class)
    private String teacherName;

    @Converted(dependProperty = "deptSchoolId", bean = DingDeptRepository.class)
    private String deptSchoolName;

    private String courseIndexName;

    private String courseLabel;

    private Date beginTime;// 开始时间

    private Date riseClassTime;// 升班时间


}
