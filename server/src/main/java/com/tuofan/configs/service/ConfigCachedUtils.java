package com.tuofan.configs.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.google.common.collect.Lists;
import com.tuofan.core.LocalCacheUtils;
import com.tuofan.core.BizException;
import com.tuofan.core.ModelConvertHelper;
import com.tuofan.configs.constants.ConfigNameConstants;
import com.tuofan.configs.entity.SysConfigs;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.util.List;

@Slf4j
@Component
public class ConfigCachedUtils {

    @Autowired
    private ISysConfigsService iSysConfigsService;

    /**
     * 取出全部
     *
     * @return 所有缓存值
     */
    public List<SysConfigs> listAll() {
        List<Object> list = Lists.newArrayList(LocalCacheUtils.fetchAll().values());
        return ModelConvertHelper.convertList(list, SysConfigs.class);
    }

    public String getValue(String name) {
        SysConfigs target = this.getItem(name);
        if (target != null) {
            LocalCacheUtils.putItem(name, target);
            return target.getValue();
        }
        return null;
    }

    public SysConfigs getItem(String name) {
        if (StringUtils.isEmpty(name)) {
            return null;
        }
        if (LocalCacheUtils.containsKey(name)) {
            return (SysConfigs) LocalCacheUtils.getItem(name);
        }
        QueryWrapper<SysConfigs> query = new QueryWrapper<>();
        query.eq("name", name);
        SysConfigs target = iSysConfigsService.getOne(query);
        if (target != null) {
            LocalCacheUtils.putItem(name, target);
        }
        return target;
    }

    public void saveOrUpdate(SysConfigs model) {
        iSysConfigsService.saveOrUpdate(model);
        LocalCacheUtils.putItem(model.getName(), model);
    }

    public void refreshConfigCache() {
        List<SysConfigs> list = iSysConfigsService.list();
        list.forEach(ele ->
                LocalCacheUtils.putItem(ele.getName(), ele)
        );
    }

    public Integer getDingRootDeptId() {
        String dingRootDeptIdStr = this.getValue(ConfigNameConstants.dingRootDeptId);
        if (com.tuofan.core.utils.StringUtils.isEmpty(dingRootDeptIdStr)) {
            throw new BizException("5002", "没有配置根钉部门ID（集团ID）,请现在部门表配置name=" + ConfigNameConstants.dingRootDeptId + "的配置项");
        }
        return Integer.parseInt(dingRootDeptIdStr);
    }
}
