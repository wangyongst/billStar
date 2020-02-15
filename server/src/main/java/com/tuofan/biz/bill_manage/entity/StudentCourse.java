package com.tuofan.biz.bill_manage.entity;

import com.tuofan.core.constants.AppConstants;
import com.tuofan.core.persistence.entity.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.persistence.Table;
import java.util.Date;

/**
 * 记录学生-课程关系
 */
@Table(name = "t_student_course")
@Data
@EqualsAndHashCode(callSuper = true)
public class StudentCourse extends BaseEntity {

    private Integer deptSchoolId;

    private Integer studentId;

    private Integer courseId;

    private Date beginTime; // 开始时间

    private Date expireTime; // 过期时间

    private Date riseClassTime; // 升班时间

    private Integer isActive; // 状态，有效为1;

    private String remark; // 备注信息

    /**
     * 新增一个创建的实例
     *
     * @param billCourse   参数
     * @param studentId    学生ID
     * @param deptSchoolId 校区ID
     * @return 实例
     */
    public static StudentCourse ofCreateInstance(BillCourse billCourse, Integer studentId, Integer deptSchoolId) {
        StudentCourse studentCourse = new StudentCourse();
        studentCourse.setDeptSchoolId(deptSchoolId);
        studentCourse.setCourseId(billCourse.getCourseId());
        studentCourse.setBeginTime(billCourse.getBeginTime());
        studentCourse.setExpireTime(billCourse.getExpireTime());
        studentCourse.setRiseClassTime(billCourse.getRiseClassTime());
        studentCourse.setStudentId(studentId);
        studentCourse.setIsActive(AppConstants.intYes);
        return studentCourse;
    }

    /**
     * 更新几个时间参数
     *
     * @param billCourse 依据的参数
     */
    public void updateTimeFrom(BillCourse billCourse) {
        this.setBeginTime(billCourse.getBeginTime());
        this.setExpireTime(billCourse.getExpireTime());
        this.setRiseClassTime(billCourse.getRiseClassTime());
    }

}
