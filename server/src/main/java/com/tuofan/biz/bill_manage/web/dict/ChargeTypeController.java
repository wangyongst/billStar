package com.tuofan.biz.bill_manage.web.dict;

import com.tuofan.biz.bill_manage.entity.Bill;
import com.tuofan.biz.bill_manage.entity.SysDict;
import com.tuofan.biz.bill_manage.service.BillService;
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

// 班别
@RestController
@RequestMapping("${cert.api.prefix}/chargeType")
public class ChargeTypeController {

    @Autowired
    private SysDictService sysDictService;

    @Autowired
    private BillService billService;

    @GetMapping("list")
    @LoginRequired
    @FieldConversion
    public List<SysDict> list(@RequestParam(required = false) String name) {
        SysDict query = new SysDict();
        query.setName(name);
        query.setType(CHARGE_TYPE);
        return sysDictService.listAll(query);
    }

    @PostMapping("delete")
    @LoginRequired
    public Integer delete(@RequestParam Integer id) {
        SysDict model = sysDictService.get(id);
        if (model == null) {
            throw BizException.locateByIdException();
        }
        Bill query = new Bill();
        query.setPayTypeId(id);
        List<Bill> bills = billService.listAll(query);
        if (!CollectionUtils.isEmpty(bills)) {
            throw BizException.deleteFailForRefereed();
        }
        return sysDictService.delete(model);
    }

    @PostMapping("create")
    @LoginRequired
    public Integer create(@RequestParam String name) {
        SysDict model = sysDictService.getByName(CHARGE_TYPE, name);
        if (null != model) {
            throw new BizException("101", name + " 已存在");
        }
        model = new SysDict();
        model.setName(name);
        model.setType(CHARGE_TYPE);
        return sysDictService.create(model);
    }

    @PostMapping("update")
    @LoginRequired
    public Integer update(@RequestBody @Valid SysDict updateCmd) {
        SysDict model = sysDictService.getByName(CHARGE_TYPE, updateCmd.getName());
        if (model == null) {
            return sysDictService.update(updateCmd);
        }
        if (model.getId().intValue() == updateCmd.getId().intValue()) {
            return 1;
        }
        throw new BizException("300", "收费类型" + updateCmd.getName() + "已存在");
    }
}
