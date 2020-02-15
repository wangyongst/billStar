package com.tuofan.biz.bill_manage.service;

import com.google.common.collect.Lists;
import com.tuofan.biz.bill_manage.dao.CourseDao;
import com.tuofan.biz.bill_manage.entity.Course;
import com.tuofan.biz.bill_manage.vo.CourseQuery;
import com.tuofan.biz.sys_orgnization.application.query.DingUserQueryService;
import com.tuofan.core.helper.ModelConvertHelper;
import com.tuofan.core.persistence.service.CrudRepository;
import com.tuofan.core.utils.StringUtils;
import org.checkerframework.checker.units.qual.C;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import tk.mybatis.mapper.entity.Example;

import java.util.Collection;
import java.util.List;

@Service
public class CourseService extends CrudRepository<CourseDao, Course> {

    @Autowired
    DingUserQueryService dingUserQueryService;

    @Autowired
    StudentCourseService studentCourseService;

    @Autowired
    ClassTypeService classTypeService;

    public Course locateBy(Integer semesterId , Integer deptSchoolId, Integer classTypeId, Integer classIndex, Integer teacherId) {
        Course query = new Course();
        query.setSemesterId(semesterId);
        query.setDeptSchoolId(deptSchoolId);
        query.setDictCourseId(classTypeId);
        query.setCourseIndex(classIndex);
        query.setTeacherId(teacherId);
        return super.get(query);
    }

    public List<Course> listByDeptIds(List<Integer> deptIds) {
        CourseQuery query = new CourseQuery();
        query.setDeptSchoolIds(deptIds);
        return listAllBy(query);
    }

    public List<Course> listAllBy(CourseQuery query) {
        this.processCourseQuery(query);
        if (query.isStop()) {
            return Lists.newArrayList();
        }
        Example example = this.loadQueryExample(query);
        return listAllByExample(example);
    }

    public List<Course> listBySemesterId(Integer semesterId) {
        Course courseQuery = new Course();
        courseQuery.setSemesterId(semesterId);
        return this.listAll(courseQuery);
    }

    public List<Course> listBySubjectId(Integer subjectId) {
        CourseQuery courseQuery = new CourseQuery();
        List<Integer> dictClassIds = classTypeService.listIdsBySubjectIds(Lists.newArrayList(subjectId));
        if (CollectionUtils.isEmpty(dictClassIds)) {
            return Lists.newArrayList();
        }
        courseQuery.setDictCourseIdList(dictClassIds);
        return this.listAllBy(courseQuery);
    }

    public void processCourseQuery(CourseQuery query) {
        // 处理教师名字-模糊查询。
        if (StringUtils.isNotEmpty(query.getTeacherNameLike())) {
            Collection<Integer> userIds = dingUserQueryService.listUserIdByNameLike(query.getTeacherNameLike());
            if (CollectionUtils.isEmpty(userIds)) {
                query.setStop(true);
                return;
            }
            query.setTeacherIds(userIds);
        }
        // 处理课程科目-课程查询。科目在课程端
        if (!CollectionUtils.isEmpty(query.getSubjectIdList())) {
            List<Integer> dictClassIds = classTypeService.listIdsBySubjectIds(Lists.newArrayList(query.getSubjectIdList()));
            if (CollectionUtils.isEmpty(dictClassIds)) {
                query.setStop(true);
                return;
            }
            Collection targetDictClassIds = org.apache.commons.collections.CollectionUtils
                    .intersection(query.getCourseIndexList(), dictClassIds);
            query.setDictCourseIdList(targetDictClassIds);
        }
    }

    public Example loadQueryExample(CourseQuery query) {
        Example example = this.createExample();
        Example.Criteria criteria = example.and();
        if (!CollectionUtils.isEmpty(query.getDeptSchoolIds())) {
            criteria.andIn("deptSchoolId", query.getDeptSchoolIds());
        }
        if (!CollectionUtils.isEmpty(query.getCourseIndexList())) {
            criteria.andIn("courseIndex", query.getCourseIndexList());
        }
        if (!CollectionUtils.isEmpty(query.getDictCourseIdList())) {
            criteria.andIn("dictCourseId", query.getDictCourseIdList());
        }
        if (!CollectionUtils.isEmpty(query.getTeacherIds())) {
            criteria.andIn("teacherId", query.getTeacherIds());
        }
        Course course = ModelConvertHelper.convert(query, Course.class);
        this.convertEntity2Example(course, example);
        return example;
    }

    @Transactional
    public void processCourseRealStudent(List<Integer> courseIds) {
        for (Integer courseId : courseIds) {
            Integer studentNumber = studentCourseService.countCourseRealStudent(courseId);
            Course course = Course.ofUpdateNumberInstance(courseId, studentNumber);
            this.update(course);
        }
    }
}
