package com.tuofan.biz.bill_manage.service;

import com.google.common.collect.Lists;
import com.tuofan.biz.bill_manage.dao.BillCourseDao;
import com.tuofan.biz.bill_manage.entity.Bill;
import com.tuofan.biz.bill_manage.entity.BillCourse;
import com.tuofan.biz.bill_manage.entity.StudentCourse;
import com.tuofan.biz.bill_manage.vo.BillCourseQuery;
import com.tuofan.core.persistence.service.CrudRepository;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import tk.mybatis.mapper.entity.Example;

import java.util.Date;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class BillCourseService extends CrudRepository<BillCourseDao, BillCourse> {

    public List<BillCourse> listByBillId(Integer billId) {
        List<Integer> billIds = Lists.newArrayList(billId);
        Example example = this.createExample();
        Example.Criteria criteria = example.and();
        criteria.andIn("billId", billIds);
        return this.listAllByExample(example);
    }

    public List<BillCourse> listByBillIds(List<Integer> billIds) {
        if (CollectionUtils.isEmpty(billIds)) {
            return Lists.newArrayList();
        }
        Example example = this.createExample();
        Example.Criteria criteria = example.and();
        criteria.andIn("billId", billIds);
        return this.listAllByExample(example);
    }

    public void deleteByBillId(Integer billId) {
        BillCourse query = new BillCourse();
        query.setBillId(billId);
        List<BillCourse> billCourses = this.listAll(query);
        if (CollectionUtils.isEmpty(billCourses)) {
            return;
        }
        this.deleteList(billCourses);
    }

    public Set<Integer> listBillIdBeforeExpireTime(Date expireEndTime) {
        Example example = this.createExample();
        Example.Criteria criteria = example.and();
        criteria.andLessThanOrEqualTo("expireTime", expireEndTime);
        List<BillCourse> list = this.listAllByExample(example);
        return list.stream().map(BillCourse::getBillId).collect(Collectors.toSet());
    }

    public List<BillCourse> listBillCourse(BillCourseQuery billCourseQuery) {
        Example example = this.loadQueryExample(billCourseQuery);
        return this.listAllByExample(example);
    }

    public List<BillCourse> listByCourseId(Integer courseId) {
        BillCourse query = new BillCourse();
        query.setCourseId(courseId);
        return this.listAll(query);
    }

    private Example loadQueryExample(BillCourseQuery billCourseQuery) {
        Example example = this.createExample();
        Example.Criteria criteria = example.and();
        if (billCourseQuery.getExpireEndDate() != null) {
            criteria.andLessThanOrEqualTo("expireTime", billCourseQuery.getExpireEndDate());
        }
        if (!CollectionUtils.isEmpty(billCourseQuery.getCourseIdList())) {
            criteria.andIn("courseId", billCourseQuery.getCourseIdList());
        }
        return example;
    }


}
