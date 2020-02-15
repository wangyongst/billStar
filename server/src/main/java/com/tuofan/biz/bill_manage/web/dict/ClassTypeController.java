package com.tuofan.biz.bill_manage.web.dict;

import com.tuofan.biz.bill_manage.entity.Course;
import com.tuofan.biz.bill_manage.entity.SysDict;
import com.tuofan.biz.bill_manage.service.CourseService;
import com.tuofan.biz.bill_manage.service.SysDictService;
import com.tuofan.biz.bill_manage.vo.CreateClassTypeCmd;
import com.tuofan.biz.bill_manage.vo.UpdateClassTypeCmd;
import com.tuofan.core.advice.auth.LoginRequired;
import com.tuofan.core.advice.convert.FieldConversion;
import com.tuofan.core.advice.convert.FieldConvertUtils;
import com.tuofan.core.exception.BizException;
import com.tuofan.core.helper.ModelConvertHelper;
import com.tuofan.core.utils.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.CollectionUtils;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

import static com.tuofan.biz.bill_manage.constants.DictTypes.CLASS_TYPE;

// 班别
@RestController
@RequestMapping("${cert.api.prefix}/classType")
public class ClassTypeController {

    @Autowired
    private SysDictService sysDictService;

    @Autowired
    CourseService courseService;

    @GetMapping("list")
    @LoginRequired
    @FieldConversion
    public List<SysDict> list(@RequestParam(required = false) String name) {
        SysDict query = new SysDict();
        query.setName(name);
        query.setType(CLASS_TYPE);
        return sysDictService.listAll(query);
    }

    @GetMapping("listVO")
    @LoginRequired
    public List<SysDict> listVO(@RequestParam(required = false) String name) {
        SysDict query = new SysDict();
        query.setName(name);
        query.setType(CLASS_TYPE);
        List<SysDict> list = sysDictService.listAll(query);
        FieldConvertUtils.convertList(list);
        list.forEach(SysDict::setFullName);
        return list;
    }

    @PostMapping("delete")
    @LoginRequired
    public Integer delete(@RequestParam Integer id) {
        SysDict model = sysDictService.get(id);
        if (model == null) {
            throw BizException.locateByIdException();
        }
        Course query = new Course();
        query.setDictCourseId(id);
        List<Course> list = courseService.listAll(query);
        if (!CollectionUtils.isEmpty(list)) {
            throw BizException.deleteFailForRefereed();
        }
        return sysDictService.delete(model);
    }

    @PostMapping("create")
    @LoginRequired
    public Integer create(@RequestBody @Validated CreateClassTypeCmd createClassTypeCmd, BindingResult bindingResult) {
        if (bindingResult.hasFieldErrors()) {
            throw new BizException("101", bindingResult.getFieldError().getDefaultMessage());
        }
        SysDict model = sysDictService.getByParentIdAndName(CLASS_TYPE, createClassTypeCmd.getParentId(), createClassTypeCmd.getName());
        if (null != model) {
            throw new BizException("101", "已存在同科目-班级数据");
        }
        model = new SysDict();
        model.setType(CLASS_TYPE);
        model.setName(createClassTypeCmd.getName());
        model.setParentId(createClassTypeCmd.getParentId());
        return sysDictService.create(model);
    }

    @PostMapping("update")
    @LoginRequired
    public Integer update(@RequestBody @Valid UpdateClassTypeCmd updateCmd) {
        SysDict model = sysDictService.getByParentIdAndName(CLASS_TYPE, updateCmd.getParentId(), updateCmd.getName());
        if (model == null) {
            model = ModelConvertHelper.convert(updateCmd,SysDict.class);
            return sysDictService.update(model);
        }
        if (model.getId().intValue() == updateCmd.getId().intValue()) {
            return 1;
        }
        throw new BizException("300", "已存在同科目-班级数据");
    }
}
