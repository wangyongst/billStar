package com.tuofan.biz.bill_manage.dao;

import com.tuofan.biz.bill_manage.entity.StudentCourse;
import com.tuofan.biz.bill_manage.vo.SubjectStudentDTO;
import com.tuofan.biz.bill_manage.vo.SubjectStudentQuery;
import com.tuofan.core.persistence.dao.BaseDao;

import java.util.List;

public interface StudentCourseDao extends BaseDao<StudentCourse> {

    List<SubjectStudentDTO> subjectStudentReport(SubjectStudentQuery query);

}
