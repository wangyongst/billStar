package com.tuofan.biz.bill_manage.service;

import com.tuofan.biz.bill_manage.constants.DictTypes;
import com.tuofan.biz.bill_manage.entity.SysDict;
import com.tuofan.biz.bill_manage.vo.SysDictQuery;
import com.tuofan.core.advice.convert.FieldConvertUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ClassTypeService {

    @Autowired
    SysDictService sysDictService;

    /**
     * 根据【科目ID-list】获取课程
     *
     * @param subjectIds 科目ID-list
     * @return 课程-list
     */
    public List<SysDict> listBySubjectIds(List<Integer> subjectIds) {
        SysDictQuery classTypeQuery = new SysDictQuery();
        classTypeQuery.setParentIds(subjectIds);
        classTypeQuery.setType(DictTypes.SEMESTER);
        return sysDictService.listBy(classTypeQuery);
    }

    /**
     * 根据【科目Id-list】获取课程Id-list
     *
     * @param subjectIds 科目Id-list
     * @return 课程-list
     */
    public List<Integer> listIdsBySubjectIds(List<Integer> subjectIds) {
        List<SysDict> list = this.listBySubjectIds(subjectIds);
        return list.stream().map(SysDict::getId).collect(Collectors.toList());
    }

    /**
     * 根据【科目Id-list】获取课程Id-list
     *
     * @param ids 科目Id-list
     * @return 课程-list
     */
    public List<SysDict> listFullNameByIds(List<Integer> ids) {
        List<SysDict> list = sysDictService.listByIds(ids);
        FieldConvertUtils.convertList(list);
        list.forEach(SysDict::setFullName);
        return list;
    }

}
