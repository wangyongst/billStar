package com.tuofan.biz.bill_manage.service;

import com.tuofan.biz.bill_manage.dao.SysDictDao;
import com.tuofan.biz.bill_manage.entity.SysDict;
import com.tuofan.biz.bill_manage.vo.SysDictQuery;
import com.tuofan.core.constants.AppConstants;
import com.tuofan.core.helper.ModelConvertHelper;
import com.tuofan.core.persistence.service.CrudRepository;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import tk.mybatis.mapper.entity.Example;

import java.util.List;

import static com.tuofan.biz.bill_manage.constants.DictTypes.CHARGE_TYPE;

@Service
public class SysDictService extends CrudRepository<SysDictDao, SysDict> {
    /**
     * 根据名字查找
     *
     * @param name
     * @return
     */
    public SysDict getByName(String type, String name) {
        SysDict query = new SysDict();
        query.setName(name);
        query.setType(type);
        return this.get(query);
    }

    public SysDict getByParentIdAndName(String type, Integer parentId , String name) {
        SysDict query = new SysDict();
        query.setName(name);
        query.setType(type);
        query.setParentId(parentId);
        return this.get(query);
    }

    public List<SysDict> listPayType() {
        SysDict query = new SysDict();
        query.setType(CHARGE_TYPE);
        return this.listAll(query);
    }


    public List<SysDict> listByType(String type) {
        SysDict query = new SysDict();
        query.setType(type);
        return this.listAll(query);
    }

    /**
     * 设置为默认
     *
     * @param model 设为默认的model
     * @return 是否设置成功
     */
    public Integer setAsDefault(SysDict model) {
        this.clearDefault(model.getType());
        model.setIsDefault(AppConstants.intYes);
        return this.update(model);
    }

    private void clearDefault(String type) {
        List<SysDict> list = this.listByType(type);
        if (CollectionUtils.isEmpty(list)) {
            return;
        }
        for (SysDict dict : list) {
            if (!dict.isDefault()) {
                continue;
            }
            dict.setIsDefault(AppConstants.intNo);
            this.update(dict);
        }
    }

    @SuppressWarnings("unchecked")
    public List<SysDict> listBy(SysDictQuery query) {
        Example example = this.createExample();
        Example.Criteria criteria = example.and();
        if (!CollectionUtils.isEmpty(query.getParentIds())) {
            criteria.andIn("parentId", query.getParentIds());
        }
        SysDict entity = ModelConvertHelper.convert(query, SysDict.class);
        this.convertEntity2Example(entity, example);
        return this.listAllByExample(example);
    }

    public List<SysDict> listChildren(Integer parentId) {
        SysDict query = new SysDict();
        query.setParentId(parentId);
        return this.listAll(query);
    }

}
