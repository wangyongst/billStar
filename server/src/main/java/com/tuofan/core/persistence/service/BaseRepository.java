package com.tuofan.core.persistence.service;

import com.tuofan.core.persistence.dao.BaseDao;
import com.tuofan.core.persistence.entity.BaseEntity;
import com.tuofan.core.utils.SysBeanUtils;
import com.tuofan.core.utils.ReflectionUtils;
import com.tuofan.core.utils.StringUtils;
import com.tuofan.core.utils.ValidateUtils;
import org.apache.ibatis.type.EnumOrdinalTypeHandler;
import org.springframework.beans.factory.annotation.Autowired;
import tk.mybatis.mapper.entity.Example;
import tk.mybatis.mapper.mapperhelper.EntityHelper;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

@SuppressWarnings({"unused", "rawtypes", "WeakerAccess", "unchecked"})
public abstract class BaseRepository<D extends BaseDao<T>, T extends BaseEntity> {

    /**
     * 持久层对象
     */
    @Autowired
    protected D dao;

    private static final Map<String, Class> CLASS_GENRIC_TYPE_MAP = new HashMap<>();

    private static final Object LOCK = new Object();

    String WHITE_SPACE = " ";


    public Example convertEntity2Example(T entity) {
        Example example = createExample();
        if (entity != null) {
            convertEntity2Example(entity, example);
        }
        return example;
    }

    public void convertEntity2Example(T entity, Example example) {
        // example 中已存在条件的字段
        List<String> existFields = getExampleExistFields(example);
        // 实体类的字段遍历，并把满足example中不存在而且值不为空的字段拼成example的条件
        EntityHelper.getColumns(this.getClassGenericType()).stream().filter(x -> !existFields.contains(x.getProperty()))
                .forEach(x -> {
                    Object fieldValue;
                    if (x.getTypeHandler() != null && EnumOrdinalTypeHandler.class.equals(x.getTypeHandler())) {
                        // 枚举默认取的是name,如果存序号，则取序号
                        fieldValue = ((Enum) SysBeanUtils.getRawProperty(entity, x.getProperty())).ordinal();
                    } else {
                        fieldValue = SysBeanUtils.getProperty(entity, x.getProperty());
                    }

                    if (ValidateUtils.isEmpty(fieldValue)) {
                        return;
                    }

                    if (ValidateUtils.isNotEmpty(example.getOredCriteria())) {
                        Example.Criteria criteria = example.getOredCriteria().get(0);
                        criteria.andEqualTo(x.getProperty(), fieldValue);
                    } else {
                        Example.Criteria criteria = example.createCriteria();
                        criteria.andEqualTo(x.getProperty(), fieldValue);
                    }
                });
    }

    public Example createExample() {
        Example example = new Example(getClassGenericType());
//        Example.Criteria criteria = example.createCriteria();
        /*
         * 此处可以加拦截器,做更多处理
         */
        return example;
    }

    protected Class getClassGenericType() {
        String name = this.getClass().getName();
        Class aClass = CLASS_GENRIC_TYPE_MAP.get(name);
        if (aClass == null) {
            synchronized (LOCK) {
                aClass = CLASS_GENRIC_TYPE_MAP.get(name);
                if (aClass == null) {
                    CLASS_GENRIC_TYPE_MAP.put(name, ReflectionUtils.getClassGenricType(this.getClass(), 1));
                }
            }
        }
        return CLASS_GENRIC_TYPE_MAP.get(name);
    }

    public List<String> getExampleExistFields(Example example) {
        return example.getOredCriteria().stream().filter(Objects::nonNull).flatMap(x -> x.getAllCriteria().stream())
                .filter(Objects::nonNull).map(Example.Criterion::getCondition).filter(x -> {
                    if (StringUtils.isEmpty(x)) {
                        return false;
                    }
                    return x.contains(WHITE_SPACE);
                }).map(x -> x.split(WHITE_SPACE)[0]).filter(Objects::nonNull).map(x -> {
                    String camelFieldName = StringUtils.underlineToCamel(x);
                    return StringUtils.replace(camelFieldName, "`", "");
                }).collect(Collectors.toList());
    }

    protected Example.Criteria getCriteriaFromExample(Example example) {
        Example.Criteria criteria;
        if (!example.getOredCriteria().isEmpty()) {
            criteria = example.getOredCriteria().get(0);
        } else {
            criteria = example.createCriteria();
        }
        return criteria;
    }

}
