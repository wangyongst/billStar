package com.tuofan.biz.bill_manage.service;

import com.github.pagehelper.PageInfo;
import com.tuofan.biz.bill_manage.dao.StudentDao;
import com.tuofan.biz.bill_manage.entity.Student;
import com.tuofan.biz.bill_manage.vo.StudentQuery;
import com.tuofan.core.constants.AppConstants;
import com.tuofan.core.dto.PageRequest;
import com.tuofan.core.helper.ModelConvertHelper;
import com.tuofan.core.persistence.service.CrudRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;
import tk.mybatis.mapper.entity.Example;

import java.util.List;

@Slf4j
@Service
public class StudentService extends CrudRepository<StudentDao, Student> {

//    public Student getByDeptIdAndMobile(Integer deptSchoolId, String mobile) {
//        Student query = new Student();
//        query.setMobile(mobile);
//        query.setDeptSchoolId(deptSchoolId);
//        return this.get(query);
//    }

    @Autowired
    BillService billService;

    @Autowired
    BillQueryService billQueryService;

    @Autowired
    StudentCourseService studentCourseService;

    /**
     * 校区+姓名+手机号唯一确定学生
     *
     * @param deptSchoolId 校区
     * @param name         姓名
     * @param mobile       手机号
     * @return 学生信息
     */
    public Student getByUniqueParam(Integer deptSchoolId, String name, String mobile) {
        Student query = new Student();
        query.setName(name);
        query.setMobile(mobile);
        query.setDeptSchoolId(deptSchoolId);
        return this.get(query);
    }


    public PageInfo<Student> listQueryPage(PageRequest<StudentQuery> request) {
        StudentQuery studentQuery = request.getData();
        if (studentQuery.isStopQuery()) {
            return new PageInfo<>();
        }
        Example example = this.loadQueryExample(request.getData());
        PageRequest<Example> pageRequest = ModelConvertHelper.convert(request, PageRequest.class);
        pageRequest.setData(example);
        return this.listPageByExample(pageRequest);
    }

    public Example loadQueryExample(StudentQuery query) {
        Example example = this.createExample();
        Example.Criteria criteria = example.and();
        // 有欠费
        if (query.getHasArrears() != null && query.getHasArrears().equals(AppConstants.intYes)) {
            criteria.andGreaterThan("currentArrears", 0.0);
        }
        if (query.getHasArrears() != null && query.getHasArrears().equals(AppConstants.intNo)) {
            criteria.andEqualTo("currentArrears", 0.0);
        }
        if (!CollectionUtils.isEmpty(query.getDeptSchoolIds())) {
            criteria.andIn("deptSchoolId", query.getDeptSchoolIds());
        }
        if (!StringUtils.isEmpty(query.getMobileLike())) {
            criteria.andLike("mobile", "%" + query.getMobileLike() + "%");
        }
        if (!StringUtils.isEmpty(query.getNameLike())) {
            criteria.andLike("name", "%" + query.getNameLike() + "%");
        }
        Student student = ModelConvertHelper.convert(query, Student.class);
        this.convertEntity2Example(student, example);
        return example;
    }

    @Transactional
    public Integer deleteWithBills(Student origin) {
        Integer billSize = billService.deleteByStudentID(origin.getId());
        studentCourseService.deleteByStudentID(origin.getId());
        log.info("删除票据数据，条数 = {}", billSize);
        return this.delete(origin);
    }

    public List<Student> listByQuery(StudentQuery studentQuery) {
        Example example = this.loadQueryExample(studentQuery);
        return this.listAllByExample(example);
    }

//    public void combineArrears(List<Student> students) {
//        if (CollectionUtils.isEmpty(students)) {
//            return;
//        }
//        List<Integer> studentIds = students.stream().map(Student::getId).collect(Collectors.toList());
//        BillQuery billQuery = new BillQuery();
//        billQuery.setStudentIdList(studentIds);
//        billQuery.setMinCurrentArrears(0.0);
//        List<Bill> bills = billQueryService.listAll(billQuery);
//        Map<Integer, Student> studentMap = students.stream().collect(Collectors.toMap(Student::getId, x -> x));
//        for (Bill bill : bills) {
//            if (!studentMap.containsKey(bill.getStudentId())) {
//                continue;
//            }
//            Student student = studentMap.get(bill.getStudentId());
//            if (student.getCurrentArrears() == null) {
//                student.setCurrentArrears(0.0);
//            }
//            student.setCurrentArrears(student.getCurrentArrears() + bill.getCurrentArrears());
//        }
//    }
//
//    public void combineArrears(Student student) {
//        if (student == null) {
//            return;
//        }
//        this.combineArrears(Lists.newArrayList(student));
//    }
}
