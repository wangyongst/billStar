package com.tuofan.biz.bill_manage.service;

import com.github.pagehelper.PageInfo;
import com.google.common.collect.Lists;
import com.tuofan.biz.bill_manage.dao.BillDao;
import com.tuofan.biz.bill_manage.entity.*;
import com.tuofan.biz.bill_manage.vo.*;
import com.tuofan.core.advice.convert.FieldConvertUtils;
import com.tuofan.core.constants.AppConstants;
import com.tuofan.core.dto.PageRequest;
import com.tuofan.core.helper.ModelConvertHelper;
import com.tuofan.core.persistence.service.CrudRepository;
import com.tuofan.core.utils.DateTimeUtils;
import com.tuofan.core.utils.StringUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import tk.mybatis.mapper.entity.Example;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * 处理billVO 查询
 */
@Slf4j
@Service
public class BillQueryService extends CrudRepository<BillDao, Bill> {


    @Autowired
    StudentService studentService;

    @Autowired
    BillCourseService billCourseService;

    @Autowired
    CourseService courseService;

    @Autowired
    StudentCourseService studentCourseService;

    /**
     * 年报表查询
     *
     * @param query
     * @return
     */
    public List<YearSaleDto> yearReport(SaleQuery query) {
        String inCondition = String.format("(%s)", StringUtils.join(query.getDeptSchoolIds(), ","));
        return this.dao.yearReport(query.getBeginDate(), query.getEndDate(), inCondition);
    }

    /**
     * 支付类型报表
     *
     * @param query
     * @return
     */
    public List<PayTypeSaleDTO> payTypeReport(SaleQuery query) {
        String inCondition = String.format("(%s)", StringUtils.join(query.getDeptSchoolIds(), ","));
        return this.dao.payTypeReport(query.getBeginDate(), query.getEndDate(), inCondition);
    }


    public Example loadQueryExample(BillQuery billQuery) {
        Example example = this.createExample();
        Example.Criteria criteria = example.and();
        if (!CollectionUtils.isEmpty(billQuery.getDeptSchoolIds())) {
            criteria.andIn("deptSchoolId", billQuery.getDeptSchoolIds());
        }
        if (!CollectionUtils.isEmpty(billQuery.getIds())) {
            criteria.andIn("id", billQuery.getIds());
        }
        if (billQuery.getStartDate() != null) {
            criteria.andGreaterThanOrEqualTo("billTime", DateTimeUtils.getDateFirstTime(billQuery.getStartDate()));
        }
        if (billQuery.getEndDate() != null) {
            criteria.andLessThanOrEqualTo("billTime", DateTimeUtils.getDateEndTime(billQuery.getEndDate()));
        }
        // 欠费处理
        if (billQuery.getHasArrears() != null && billQuery.getHasArrears().equals(AppConstants.intYes)) {
            criteria.andGreaterThan("currentArrears", 0.0);
        }
        if (billQuery.getHasArrears() != null && billQuery.getHasArrears().equals(AppConstants.intNo)) {
            criteria.andLessThanOrEqualTo("currentArrears", 0.0);
        }
        //操作类型，多选
        if (!CollectionUtils.isEmpty(billQuery.getTypeList())) {
            criteria.andIn("type", billQuery.getTypeList());
        }
        if (!CollectionUtils.isEmpty(billQuery.getStudentIdList())) {
            criteria.andIn("studentId", billQuery.getStudentIdList());
        }

        Bill entity = ModelConvertHelper.convert(billQuery, Bill.class);
        this.convertEntity2Example(entity, example);
        return example;
    }

    public List<Bill> listAll(BillQuery billQuery) {
        Example example = this.loadQueryExample(billQuery);
        return this.listAllByExample(example);
    }

    public PageInfo<Bill> listPageBy(PageRequest<BillQuery> billQueryRequest) {
        BillQuery query = billQueryRequest.getData();
        this.processQueryParam(query);
        if (CollectionUtils.isEmpty(query.getDeptSchoolIds())) {
            query.setStop(true);
        }
        if (query.isStop()) {
            return new PageInfo<>();
        }
        Example example = this.loadQueryExample(query);
        PageRequest<Example> request = ModelConvertHelper.convert(billQueryRequest, PageRequest.class);
        request.setData(example);
        return this.listPageByExample(request);
    }

    /**
     * 查询学生-课程，对应的开票记录
     *
     * @param studentCourseId
     * @return
     */
    public List<Bill> listByStudentCourseId(Integer studentCourseId) {
        StudentCourse studentCourse = studentCourseService.get(studentCourseId);
        if (studentCourse == null) {
            return Lists.newArrayList();
        }
        List<BillCourse> billCourses = billCourseService.listByCourseId(studentCourse.getCourseId());
        Collection<Integer> billIds = billCourses.stream().map(BillCourse::getBillId).collect(Collectors.toSet());
        //  generate obj bill query
        BillQuery billQuery = new BillQuery();
        billQuery.setIds(billIds);
        billQuery.setStudentId(studentCourse.getStudentId());
        // to example
        Example example = this.loadQueryExample(billQuery);
        // do query
        return this.listAllByExample(example);
    }

    private void processQueryParam(BillQuery query) {
        List<Integer> courseIds = Lists.newArrayList();
        if (StringUtils.isNotEmpty(query.getStudentName())) {
            StudentQuery studentQuery = new StudentQuery();
            studentQuery.setNameLike(query.getStudentName());
            List<Student> students = studentService.listByQuery(studentQuery);
            if (CollectionUtils.isEmpty(students)) {
                log.info("查询Bill中断：没能根据参数billQuery={}找到学生", query);
                query.setStop(true);
                return;
            }
            Set<Integer> studentIds = students.stream().map(Student::getId).collect(Collectors.toSet());
            query.setStudentIdList(studentIds);
        }
        // 先查课程表 course
        if (!CollectionUtils.isEmpty(query.getCourseIndexList()) ||
                !CollectionUtils.isEmpty(query.getDictCourseIdList()) ||
                !CollectionUtils.isEmpty(query.getSubjectIdList())) {
            CourseQuery courseQuery = new CourseQuery();
            courseQuery.setCourseIndexList(query.getCourseIndexList());
            courseQuery.setDictCourseIdList(query.getDictCourseIdList());
            courseQuery.setSubjectIdList(query.getSubjectIdList());
            List<Course> courses = courseService.listAllBy(courseQuery);
            if (CollectionUtils.isEmpty(courses)) {
                log.info("查询Bill中断：没能根据参数billQuery={}找到课程course", query);
                query.setStop(true);
                return;
            }
            courseIds = courses.stream().map(Course::getId).collect(Collectors.toList());
        }
        // 再用课程表结果，查 billCourse
        if (query.getExpireEndTime() != null) {
            BillCourseQuery billCourseQuery = new BillCourseQuery();
            billCourseQuery.setCourseIdList(courseIds);
            billCourseQuery.setExpireEndDate(query.getExpireEndTime());
            List<BillCourse> billCourses = billCourseService.listBillCourse(billCourseQuery);
            if (CollectionUtils.isEmpty(billCourses)) {
                log.info("查询Bill中断：没能根据参数billQuery={}找到票据课程-billCourse", query);
                query.setStop(true);
                return;
            }
            Set<Integer> billIds = billCourses.stream().map(BillCourse::getBillId).collect(Collectors.toSet());
            query.setIds(billIds);
        }
    }

    public BillVO getVO(Integer id) {
        Bill bill = super.get(id);
        if (bill == null) {
            return null;
        }
        return processBillInformation(bill);
    }


    private BillVO processBillInformation(Bill bill) {

        List<Bill> bills = Lists.newArrayList(bill);
        return processBillInformation(bills).get(0);
    }

    /**
     * 有限次查询条件下，处理列表课程数据的查询
     *
     * @param list
     * @return
     */
    public List<BillVO> processBillInformation(List<Bill> list) {
        List<BillVO> bills = ModelConvertHelper.convertList(list, BillVO.class);
        // student information
        List<Integer> studentIds = bills.stream().map(BillVO::getStudentId).collect(Collectors.toList());
        List<Student> students = studentService.listByIds(studentIds);
        Map<Integer, Student> studentMap = students.stream().collect(Collectors.toMap(Student::getId, x -> x));
        bills.forEach(ele -> {
            if (studentMap.keySet().contains(ele.getStudentId())) {
                ele.setStudent(studentMap.get(ele.getStudentId()));
            }
        });
        // bill - courses
        List<Integer> billIds = bills.stream().map(BillVO::getId).collect(Collectors.toList());
        List<BillCourse> billCoursesRecords = billCourseService.listByBillIds(billIds);
        if (CollectionUtils.isEmpty(billCoursesRecords)) {
            log.error("no bill course relation records");
            return bills;
        }
        // courses
        List<Integer> courseIds = billCoursesRecords.stream().map(BillCourse::getCourseId).collect(Collectors.toList());
        List<Course> courses = courseService.listByIds(courseIds);
        if (CollectionUtils.isEmpty(courses)) {
            log.error("no bill course records");
            return bills;
        }
        FieldConvertUtils.convertList(courses);
        Map<Integer, Course> courseMap = courses.stream().collect(Collectors.toMap(Course::getId, x -> x));
        Map<Integer, BillVO> billMap = bills.stream().collect(Collectors.toMap(BillVO::getId, x -> x));
        billCoursesRecords.forEach(ele -> {
            if (courseMap.containsKey(ele.getCourseId()) && billMap.containsKey(ele.getBillId())) {
                BillVO bill = billMap.get(ele.getBillId());
                BillCourseVO billCourse = ModelConvertHelper.convert(courseMap.get(ele.getCourseId()), BillCourseVO.class);
                billCourse.setCourseId(ele.getCourseId());
                billCourse.setExpireTime(ele.getExpireTime());
                billCourse.setBeginTime(ele.getBeginTime());
                billCourse.setRiseClassTime(ele.getRiseClassTime());
                if (CollectionUtils.isEmpty(bill.getCourses())) {
                    bill.setCourses(Lists.newArrayList());
                }
                bill.getCourses().add(billCourse);
            }
        });
        return bills;
    }

    public List<Bill> listBySemesterId(Integer semesterId) {
        Bill query = new Bill();
        query.setSemesterId(semesterId);
        return this.listAll(query);
    }

    public List<SaleDTO> listSaleDTO(SaleTimeQuery saleTimeQuery) {
        List<SaleDTO> list = this.dao.saleReport(saleTimeQuery);
        list.add(SaleDTO.ofTotal(list));
        return list;
    }

}
