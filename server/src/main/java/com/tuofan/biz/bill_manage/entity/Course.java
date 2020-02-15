package com.tuofan.biz.bill_manage.entity;

import com.google.common.collect.Maps;
import com.tuofan.biz.bill_manage.service.ClassTypeService;
import com.tuofan.biz.bill_manage.service.SysDictService;
import com.tuofan.biz.sys_orgnization.domain.service.DingDeptRepository;
import com.tuofan.biz.sys_orgnization.domain.service.DingUserRepository;
import com.tuofan.core.advice.convert.Converted;
import com.tuofan.core.persistence.entity.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.persistence.Table;
import javax.persistence.Transient;
import java.text.DecimalFormat;
import java.util.Map;

@Data
@EqualsAndHashCode(callSuper = true)
@Table(name = "t_course")
public class Course extends BaseEntity {

    //所属校区ID
    private Integer deptSchoolId;

    //关联教师ID
    private Integer teacherId;

    //学期ID
    private Integer semesterId;

    //字典表-班级名字（启蒙）
    private Integer dictCourseId;

    //班别（1,2,3,4,5,6...15）
    private Integer courseIndex;

    //上课时间（每周三10点到12点）
    private String regularTime;

    //教室号
    private String classroomNo;

    // 课程标准人数
    private Integer studentNumber;

    // 课程标准人数
    private Integer currentStudentNumber;

    @Transient
    private String fullClassRate;

    //  Transient fields

    @Transient
    private String courseLabel;

    @Transient
    @Converted(dependProperty = "dictCourseId", bean = ClassTypeService.class, refMethod = "listFullNameByIds")
    private String dictCourseName;

    @Transient
    @Converted(dependProperty = "teacherId", bean = DingUserRepository.class)
    private String teacherName;

    @Transient
    @Converted(dependProperty = "semesterId", bean = SysDictService.class)
    private String semesterName;

    @Transient
    @Converted(dependProperty = "deptSchoolId", bean = DingDeptRepository.class)
    private String deptSchoolName;

    @Transient
    private String courseIndexName;

    @Transient
    @Converted(dependProperty = "createBy", bean = DingUserRepository.class)
    private String createByName;

    @Transient
    @Converted(dependProperty = "createBy", bean = DingUserRepository.class)
    private String updateByName;

    public void setCourseIndex(Integer index) {
        Map<Integer, String> map = getCourseIndexMap();
        this.courseIndex = index;
        if (map.keySet().contains(index)) {
            this.courseIndexName = map.get(index);
        }
    }

    public void setCourseLabel() {
        this.courseLabel = String.format("教室号：%s 上课时间：%s", this.getClassroomNo(), this.getRegularTime());
    }

    public void setClassroomNo(String classroomNo) {
        this.classroomNo = classroomNo;
        this.setCourseLabel();
    }

    public void setRegularTime(String regularTime) {
        this.regularTime = regularTime;
        this.setCourseLabel();
    }

    public void calFillClassRate() {
        if (this.getStudentNumber() == null || this.getStudentNumber() == 0) {
            this.setFullClassRate("-");
            return;
        }
        if (this.getCurrentStudentNumber() == null || this.getCurrentStudentNumber() == 0) {
            this.setFullClassRate("0");
            return;
        }
        Double rate = (Double.valueOf(this.getCurrentStudentNumber()) / Double.valueOf(this.getStudentNumber())) * 100;
        DecimalFormat df = new DecimalFormat("######0.00");
        this.setFullClassRate(df.format(rate) + "%");
    }

    public static Map<Integer, String> getCourseIndexMap() {
        Map<Integer, String> map = Maps.newHashMap();
        map.put(1, "一");
        map.put(2, "二");
        map.put(3, "三");
        map.put(4, "四");
        map.put(5, "五");
        map.put(6, "六");
        map.put(7, "七");
        map.put(8, "八");
        map.put(9, "九");
        map.put(10, "十");
//        map.put(11, "十一");
//        map.put(12, "十二");
//        map.put(13, "十三");
//        map.put(14, "十四");
//        map.put(15, "十五");
        return map;
    }

    public static Course ofUpdateNumberInstance(Integer id, Integer currentStudentNumber) {
        Course course = new Course();
        course.setId(id);
        course.setCurrentStudentNumber(currentStudentNumber);
        return course;
    }
}
