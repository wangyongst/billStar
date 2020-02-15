package com.tuofan.biz.bill_manage.service;

import com.github.pagehelper.PageInfo;
import com.google.common.collect.Lists;
import com.tuofan.biz.bill_manage.dao.StudentCourseDao;
import com.tuofan.biz.bill_manage.entity.*;
import com.tuofan.biz.bill_manage.vo.*;
import com.tuofan.core.advice.convert.FieldConvertUtils;
import com.tuofan.core.constants.AppConstants;
import com.tuofan.core.dto.PageRequest;
import com.tuofan.core.helper.ModelConvertHelper;
import com.tuofan.core.persistence.service.CrudRepository;
import com.tuofan.core.utils.StringUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import tk.mybatis.mapper.entity.Example;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@Slf4j
@Service
public class StudentCourseService extends CrudRepository<StudentCourseDao, StudentCourse> {

    @Autowired
    StudentService studentService;

    @Autowired
    CourseService courseService;


    public void processQueryParams(StudentCourseQuery query) {
        // 学生相关信息处理
        if (StringUtils.isNotEmpty(query.getStudentNameLike()) ||
                StringUtils.isNotEmpty(query.getStudentMobileLike())) {
            StudentQuery studentQuery = new StudentQuery();
            studentQuery.setNameLike(query.getStudentNameLike());
            studentQuery.setMobileLike(query.getStudentMobileLike());
            List<Student> students = studentService.listByQuery(studentQuery);
            if (CollectionUtils.isEmpty(students)) {
                log.info("查询studentCourse中断：没能根据参数query={}找到学生student", query);
                query.setStopQuery(true);
                return;
            }
            Set<Integer> studentIds = students.stream().map(Student::getId).collect(Collectors.toSet());
            query.setStudentIdList(studentIds);
        }
        // 课程相关信息处理
        if (!CollectionUtils.isEmpty(query.getCourseIndexList()) ||
                !CollectionUtils.isEmpty(query.getDictCourseIdList())) {
            CourseQuery courseQuery = new CourseQuery();
            courseQuery.setCourseIndexList(query.getCourseIndexList());
            courseQuery.setDictCourseIdList(query.getDictCourseIdList());
            List<Course> courses = courseService.listAllBy(courseQuery);
            if (CollectionUtils.isEmpty(courses)) {
                log.info("查询studentCourse中断：没能根据参数query={}找到课程course", query);
                query.setStopQuery(true);
                return;
            }
            Set<Integer> courseIds = courses.stream().map(Course::getId).collect(Collectors.toSet());
            query.setCourseIdList(courseIds);
        }
    }

    private Example loadQueryExample(StudentCourseQuery query) {
        Example example = this.createExample();
        Example.Criteria criteria = example.and();
        if (!CollectionUtils.isEmpty(query.getCourseIdList())) {
            criteria.andIn("deptSchoolId", query.getDeptSchoolIds());
        }
        if (!CollectionUtils.isEmpty(query.getStudentIdList())) {
            criteria.andIn("studentId", query.getStudentIdList());
        }
        if (!CollectionUtils.isEmpty(query.getCourseIdList())) {
            criteria.andIn("courseId", query.getCourseIdList());
        }
        // 过期。过期时间 < 当前时间
        if (query.getIsExpired() != null && query.getIsExpired().equals(AppConstants.intYes)) {
            criteria.andLessThan("expireTime", new Date());
        }
        // 在校。过期时间 >= 当前时间
        if (query.getIsExpired() != null && query.getIsExpired().equals(AppConstants.intNo)) {
            criteria.andGreaterThanOrEqualTo("expireTime", new Date());
        }
        StudentCourse studentCourse = ModelConvertHelper.convert(query, StudentCourse.class);
        this.convertEntity2Example(studentCourse, example);
        return example;
    }

    /**
     * 分页处理
     *
     * @param request 分页参数
     * @return 分页结果
     */
    public PageInfo<StudentCourse> pageBy(PageRequest<StudentCourseQuery> request) {
        PageRequest<Example> pageRequest = ModelConvertHelper.convert(request, PageRequest.class);
        this.processQueryParams(request.getData());
        Example example = this.loadQueryExample(request.getData());
        pageRequest.setData(example);
        return this.listPageByExample(pageRequest);
    }

    /**
     * 根据学生ID查询列表
     *
     * @param studentId 学生ID
     * @return 学生课程列表
     */
    public List<StudentCourse> listByStudentId(Integer studentId) {
        StudentCourse query = new StudentCourse();
        query.setStudentId(studentId);
        return this.listAll(query);
    }

    /**
     * 根据课程ID查询列表
     *
     * @param courseId 课程ID
     * @return 学生课程列表
     */
    public List<StudentCourse> listByCourseId(Integer courseId) {
        StudentCourse query = new StudentCourse();
        query.setCourseId(courseId);
        return this.listAll(query);
    }

    /**
     * 业务处理。学生课程。没有就插入，有就更新。
     *
     */
    public void processStudentCourse(Integer deptSchoolId) {
        ServletRequestAttributes servletRequestAttributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        HttpServletRequest request = servletRequestAttributes.getRequest();
        Object billObj = request.getAttribute("bill");
        if(billObj == null){
            log.error("无法于上下文取到 bill");
            return;
        }
        Object studentObj = request.getAttribute("student");
        if(studentObj == null){
            log.error("无法于上下文取到 student");
            return;
        }
        Object billCoursesObj = request.getAttribute("billCourses");
        if(billCoursesObj == null){
            log.error("无法于上下文取到 billCourses");
            return;
        }
        Student student = (Student) studentObj;
        Bill bill = (Bill) billObj;
        List<BillCourse> billCourseList =(List<BillCourse>) billCoursesObj;
        Integer studentId = student.getId();
        Integer billId = bill.getId();
        // 开始处理
        List<StudentCourse> list = this.listByStudentId(studentId);
        Map<Integer, StudentCourse> courseIdMap = list.stream().collect(Collectors.toMap(StudentCourse::getCourseId, x -> x));
        List<StudentCourse> insertList = Lists.newArrayList();
        List<StudentCourse> updateList = Lists.newArrayList();
        for (BillCourse billCourse : billCourseList) {
            if (billCourse.getCourseId() == null) {
                continue;
            }
            if (!courseIdMap.keySet().contains(billCourse.getCourseId())) {
                insertList.add(StudentCourse.ofCreateInstance(billCourse, studentId, deptSchoolId));
                continue;
            }
            StudentCourse existStudentCourse = courseIdMap.get(billCourse.getCourseId());
            existStudentCourse.updateTimeFrom(billCourse);
            updateList.add(existStudentCourse);
        }
        int insertCnt = 0, updateCnt = 0;
        if (!CollectionUtils.isEmpty(insertList)) {
            insertCnt = this.createList(insertList);
        }
        if (!CollectionUtils.isEmpty(updateList)) {
            updateCnt = this.updateList(updateList);
        }
        log.info("学生课程处理。学生ID={}, 票据ID={}，新增数={} ,修改数={}", studentId, billId, insertCnt, updateCnt);
    }


    /**
     * 处理列表数据的关联字段
     *
     * @param list 列表数据
     */
    public List<StudentCourseVO> doListCombine(List<StudentCourse> list) {
        List<StudentCourseVO> voList = ModelConvertHelper.convertList(list, StudentCourseVO.class);
        this.doListCombine(voList, false);
        return voList;
    }

    /**
     * 私有方法-处理列表数据的关联字段
     *
     * @param list     列表数据
     * @param isSingle 是否单个
     */
    public List<StudentCourseVO> doListCombine(List<StudentCourseVO> list, boolean isSingle) {
        if (isSingle) {
            // 查询为单个的时候，处理关联的字段
        }
        if (CollectionUtils.isEmpty(list)) {
            return list;
        }
        this.processStudentCombine(list);
        this.processCourseCombine(list);
        return list;
    }

    /**
     * 处理学生信息查询
     *
     * @param list list
     */
    private List<StudentCourseVO> processStudentCombine(List<StudentCourseVO> list) {
        Set<Integer> studentIds = list.stream().map(StudentCourseVO::getStudentId).collect(Collectors.toSet());
        if (CollectionUtils.isEmpty(studentIds)) {
            return list;
        }
        List<Student> students = studentService.listByIds(studentIds);
        if (CollectionUtils.isEmpty(students)) {
            return list;
        }
        Map<Integer, Student> studentMap = students.stream().collect(Collectors.toMap(Student::getId, x -> x));
        for (StudentCourseVO vo : list) {
            Student student = studentMap.get(vo.getStudentId());
            if (student == null) {
                continue;
            }
            vo.setStudent(student);
        }
        return list;
    }

    /**
     * 处理课程信息查询
     *
     * @param list list
     */
    public List<StudentCourseVO> processCourseCombine(List<StudentCourseVO> list) {
        Set<Integer> courseIds = list.stream().map(StudentCourseVO::getCourseId).collect(Collectors.toSet());
        if (CollectionUtils.isEmpty(courseIds)) {
            return list;
        }
        List<Course> courses = courseService.listByIds(courseIds);
        if (CollectionUtils.isEmpty(courses)) {
            return list;
        }
        FieldConvertUtils.convertList(courses);
        Map<Integer, Course> courseMap = courses.stream().collect(Collectors.toMap(Course::getId, x -> x));
        for (StudentCourseVO vo : list) {
            Course course = courseMap.get(vo.getCourseId());
            if (course == null) {
                continue;
            }
            vo.setCourse(course);
        }
        return list;
    }

    /**
     * 处理单个数据的关联字段
     *
     * @param entity 单个数据
     */
    public StudentCourseVO doEntityCombine(StudentCourse entity) {
        if (entity == null) {
            return null;
        }
        List<StudentCourse> list = Lists.newArrayList(entity);
        List<StudentCourseVO> voList = ModelConvertHelper.convertList(list, StudentCourseVO.class);
        this.doListCombine(voList, true);
        return voList.get(0);
    }

    public int deleteByStudentID(Integer studentId) {
        StudentCourse query = new StudentCourse();
        query.setStudentId(studentId);
        List<StudentCourse> list = this.listAll(query);
        if (org.apache.commons.collections.CollectionUtils.isEmpty(list)) {
            return 0;
        }
        return this.deleteList(list);
    }

    public Integer countCourseRealStudent(Integer courseId) {
        StudentCourse countQuery = new StudentCourse();
        countQuery.setIsActive(AppConstants.intYes);
        countQuery.setCourseId(courseId);
        return this.dao.selectCount(countQuery);
    }

    public List<SubjectStudentDTO> subjectStudentReport(SubjectStudentQuery query) {
        return this.dao.subjectStudentReport(query);
    }


}
