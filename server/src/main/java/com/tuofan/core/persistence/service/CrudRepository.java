package com.tuofan.core.persistence.service;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.Lists;
import com.tuofan.core.persistence.dao.BaseDao;
import com.tuofan.core.dto.PageRequest;
import com.tuofan.core.persistence.entity.BaseEntity;
import com.tuofan.core.utils.StringUtils;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import tk.mybatis.mapper.entity.Example;

import java.util.Collection;
import java.util.List;

@SuppressWarnings({"unused", "rawtypes", "WeakerAccess", "unchecked"})
public abstract class CrudRepository<D extends BaseDao<T>, T extends BaseEntity> extends BaseRepository {

    /**
     * 持久层对象
     */
    @Autowired
    protected D dao;

    protected static final String DEFAULT_ORDER_BY = "update_date DESC , create_date DESC";

    /**
     * 抽象实现，根据ID获取数据
     *
     * @param id
     * @return
     */
    public T get(Integer id) {
        if (id == null) {
            return null;
        }
        return this.dao.selectByPrimaryKey(id);
    }


    public Integer MAX_BATCH = 100;

    /**
     * 获取单条数据
     */
    public T get(T entity) {
        if (entity == null) {
            return null;
        }
        Example example = convertEntity2Example(entity);
        return dao.selectOneByExample(example);
    }

    /**
     * 查询列表数据
     */
    public List<T> listAll(T query) {
        Example example = convertEntity2Example(query);
        this.setDefaultOrderBy(example);
        return dao.selectByExample(example);
    }

    /**
     * 查询列表数据
     */
    public List<T> listAllByExample(Example example) {
        if (example == null) {
            example = createExample();
        }
        this.setDefaultOrderBy(example);
        return dao.selectByExample(example);
    }

    private void setDefaultOrderBy(Example example) {
        if (StringUtils.isBlank(example.getOrderByClause())) {
            example.setOrderByClause("update_date DESC , create_date DESC");
        }
    }

    /**
     * 保存数据（插入或更新）
     */
    @Transactional(rollbackFor = Exception.class)
    public int create(T entity) {
        entity.preInsert();
        return dao.insertSelective(entity);
    }

    /**
     * 保存数据（插入或更新）
     */
    @Transactional(rollbackFor = Exception.class)
    public int createList(List<T> entities) {
        List<List<T>> groups = Lists.partition(entities, MAX_BATCH);
        int cnt = 0;
        for (List<T> group : groups) {
            group.forEach(T::preInsert);
            cnt += dao.insertList(group);
        }
        return cnt;
    }

    /**
     * 保存数据（插入或更新）
     */
    @Transactional(rollbackFor = Exception.class)
    public int update(T entity) {
        entity.preUpdate();
        return dao.updateByPrimaryKeySelective(entity);
    }

    /**
     * 保存数据（插入或更新）
     */
    @Transactional(rollbackFor = Exception.class)
    public int updateList(List<T> list) {
        int cnt = 0;
        for (T ele : list) {
            ele.preUpdate();
            cnt += dao.updateByPrimaryKeySelective(ele);
        }
        return cnt;
    }


    /**
     * 保存数据（插入或更新）
     */
    @Transactional(rollbackFor = Exception.class)
    public int deleteList(List<T> list) {
        int cnt = 0;
        for (T ele : list) {
            cnt += dao.deleteByPrimaryKey(ele);
        }
        return cnt;
    }

    /**
     * 保存数据（插入或更新）
     */
    @Transactional(rollbackFor = Exception.class)
    public int delete(T ele) {
        return dao.deleteByPrimaryKey(ele);
    }

    /**
     * 查询分页数据
     */
    public PageInfo<T> listPage(PageRequest<T> request) {
        T data = request.getData();
        if (StringUtils.isEmpty(request.getOrderBy())) {
            request.setOrderBy(DEFAULT_ORDER_BY);
        }
        Example example = convertEntity2Example(data);
        PageHelper.startPage(request.getPageNo(), request.getPageSize(), request.getOrderBy());
        return new PageInfo<>(dao.selectByExample(example));
    }

    /**
     * 通过PageRequest<Example>来查询分页的数据，往往这个方法和<br/>
     */
    public PageInfo<T> listPageByExample(PageRequest<Example> request) {
        if (request.getData() == null) {
            request.setData(createExample());
        }
        if (StringUtils.isEmpty(request.getOrderBy())) {
            request.setOrderBy(DEFAULT_ORDER_BY);
        }
        PageHelper.startPage(request.getPageNo(), request.getPageSize(), request.getOrderBy());
        return new PageInfo<>(dao.selectByExample(request.getData()));
    }

    /**
     * 通过主键查询多条记录
     */
    @Transactional(readOnly = true, rollbackFor = Exception.class)
    public List<T> listByIds(Collection<Integer> ids) {
        if (CollectionUtils.isEmpty(ids)) {
            return ImmutableList.of();
        }
        Example example = getInIdsExample(ids);
        return dao.selectByExample(example);
    }

    protected Example getInIdsExample(Collection<Integer> ids) {
        return getInExample("id", ids);
    }

    protected Example getInExample(String field, Collection<Integer> values) {
        Example example = createExample();
        Example.Criteria criteria = example.and();
        criteria.andIn(field, values);
        return example;
    }


}
