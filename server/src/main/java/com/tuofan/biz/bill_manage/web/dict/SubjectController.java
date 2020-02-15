package com.tuofan.biz.bill_manage.web.dict;

import com.tuofan.biz.bill_manage.constants.DictTypes;
import com.tuofan.biz.bill_manage.entity.Bill;
import com.tuofan.biz.bill_manage.entity.Course;
import com.tuofan.biz.bill_manage.entity.SysDict;
import com.tuofan.biz.bill_manage.service.BillQueryService;
import com.tuofan.biz.bill_manage.service.CourseService;
import com.tuofan.biz.bill_manage.service.SysDictService;
import com.tuofan.core.advice.auth.LoginRequired;
import com.tuofan.core.advice.convert.FieldConversion;
import com.tuofan.core.exception.BizException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

// 学期
@RestController
@RequestMapping("${cert.api.prefix}/subject")
public class SubjectController {

    @Autowired
    private SysDictService sysDictService;

    @Autowired
    private BillQueryService billQueryService;

    @Autowired
    private CourseService courseService;

    @GetMapping("list")
    @LoginRequired
    @FieldConversion
    public List<SysDict> list(@RequestParam(required = false) String name) {
        SysDict query = new SysDict();
        query.setName(name);
        query.setType(DictTypes.SUBJECT);
        return sysDictService.listAll(query);
    }

    @PostMapping("delete")
    @LoginRequired
    public Integer delete(@RequestParam Integer id) {
        SysDict model = sysDictService.get(id);
        if (model == null) {
            throw BizException.locateByIdException();
        }
        // 查询是否有业务数据关联（班别）关联
        List<SysDict> courses = sysDictService.listChildren(id);
        if (!CollectionUtils.isEmpty(courses)) {
            throw BizException.deleteFailForRefereed();
        }
        return sysDictService.delete(model);
    }

    @PostMapping("create")
    @LoginRequired
    public Integer create(@RequestParam String name) {
        SysDict model = sysDictService.getByName(DictTypes.SUBJECT, name);
        if (null != model) {
            throw new BizException("101", name + " 已存在");
        }
        model = new SysDict();
        model.setName(name);
        model.setType(DictTypes.SUBJECT);
        return sysDictService.create(model);
    }

    @PostMapping("update")
    @LoginRequired
    public Integer update(@RequestBody @Valid SysDict updateCmd) {
        SysDict model = sysDictService.getByName(DictTypes.SUBJECT, updateCmd.getName());
        if (model == null) {
            return sysDictService.update(updateCmd);
        }
        if (model.getId().intValue() == updateCmd.getId().intValue()) {
            return 1;
        }
        throw new BizException("300", "科目" + updateCmd.getName() + "已存在");
    }

}
