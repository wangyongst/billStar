package com.tuofan.biz.bill_manage.service;

import com.tuofan.biz.bill_manage.constants.DictTypes;
import com.tuofan.biz.bill_manage.entity.SysDict;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.List;

@Service
public class SemesterService {

    @Autowired
    private SysDictService sysDictService;

    /**
     * 获取当前学期
     *
     * @return 当前学期
     */
    public SysDict getCurrentSemester() {
        List<SysDict> semesterList = sysDictService.listByType(DictTypes.SEMESTER);
        if (CollectionUtils.isEmpty(semesterList)) {
            return null;
        }
        for (SysDict model : semesterList) {
            if (model.isDefault()) {
                return model;
            }
        }
        return null;
    }

}
