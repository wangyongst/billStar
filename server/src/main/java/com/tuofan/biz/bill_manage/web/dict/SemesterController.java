package com.tuofan.biz.bill_manage.web.dict;

import com.tuofan.biz.bill_manage.constants.DictTypes;
import com.tuofan.biz.bill_manage.entity.Bill;
import com.tuofan.biz.bill_manage.entity.Course;
import com.tuofan.biz.bill_manage.entity.SysDict;
import com.tuofan.biz.bill_manage.service.BillQueryService;
import com.tuofan.biz.bill_manage.service.BillService;
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

import static com.tuofan.biz.bill_manage.constants.DictTypes.CHARGE_TYPE;

// 学期
@RestController
@RequestMapping("${cert.api.prefix}/semester")
public class SemesterController {

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
        query.setType(DictTypes.SEMESTER);
        return sysDictService.listAll(query);
    }

    @PostMapping("delete")
    @LoginRequired
    public Integer delete(@RequestParam Integer id) {
        SysDict model = sysDictService.get(id);
        if (model == null) {
            throw BizException.locateByIdException();
        }
        // 查询是否有courses关联
        List<Course> courses = courseService.listBySemesterId(id);
        if (!CollectionUtils.isEmpty(courses)) {
            throw BizException.deleteFailForRefereed();
        }
        // 查询是否有bill关联
        List<Bill> bills = billQueryService.listBySemesterId(id);
        if (!CollectionUtils.isEmpty(bills)) {
            throw BizException.deleteFailForRefereed();
        }
        return sysDictService.delete(model);
    }

    @PostMapping("create")
    @LoginRequired
    public Integer create(@RequestParam String name) {
        SysDict model = sysDictService.getByName(DictTypes.SEMESTER, name);
        if (null != model) {
            throw new BizException("101", name + " 已存在");
        }
        model = new SysDict();
        model.setName(name);
        model.setType(DictTypes.SEMESTER);
        return sysDictService.create(model);
    }

    @PostMapping("update")
    @LoginRequired
    public Integer update(@RequestBody @Valid SysDict updateCmd) {
        SysDict model = sysDictService.getByName(DictTypes.SEMESTER, updateCmd.getName());
        if (model == null) {
            return sysDictService.update(updateCmd);
        }
        if (model.getId().intValue() == updateCmd.getId().intValue()) {
            return 1;
        }
        throw new BizException("300", "学期" + updateCmd.getName() + "已存在");
    }

    @PostMapping("setAsDefault")
    @LoginRequired
    public Integer setAsDefault(@RequestParam Integer id) {
        SysDict model = sysDictService.get(id);
        if (model == null) {
            throw BizException.locateByIdException();
        }
        if (!model.getType().equals(DictTypes.SEMESTER)) {
            throw new BizException("300", "信息有误，数据操作权限越界");
        }
        return sysDictService.setAsDefault(model);
    }
}
