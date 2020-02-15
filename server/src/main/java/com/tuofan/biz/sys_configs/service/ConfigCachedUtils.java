package com.tuofan.biz.sys_configs.service;

import com.google.common.collect.Lists;
import com.tuofan.biz.sys_configs.constants.ConfigNameConstants;
import com.tuofan.biz.sys_configs.entity.SystemConfig;
import com.tuofan.core.cache.LocalCacheUtils;
import com.tuofan.core.exception.BizException;
import com.tuofan.core.helper.ModelConvertHelper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.util.List;

@Slf4j
@Component
public class ConfigCachedUtils {

    @Autowired
    SystemConfigService systemConfigService;

    /**
     * 取出全部
     *
     * @return 所有缓存值
     */
    public List<SystemConfig> listAll() {
        List<Object> list = Lists.newArrayList(LocalCacheUtils.fetchAll().values());
        return ModelConvertHelper.convertList(list, SystemConfig.class);
    }

    public String getValue(String name) {
        SystemConfig target = this.getItem(name);
        if (target != null) {
            LocalCacheUtils.putItem(name, target);
            return target.getValue();
        }
        return null;
    }

    public SystemConfig getItem(String name) {
        if (StringUtils.isEmpty(name)) {
            return null;
        }
        if (LocalCacheUtils.containsKey(name)) {
            return (SystemConfig) LocalCacheUtils.getItem(name);
        }
        SystemConfig query = new SystemConfig();
        query.setName(name);
        SystemConfig target = systemConfigService.get(query);
        if (target != null) {
            LocalCacheUtils.putItem(name, target);
        }
        return target;
    }

    public Integer create(SystemConfig model) {
        Integer id = systemConfigService.create(model);
        LocalCacheUtils.putItem(model.getName(), model);
        return id;
    }

    public int update(SystemConfig target) {
        int result = systemConfigService.update(target);
        LocalCacheUtils.putItem(target.getName(), target);
        return result;
    }

    /**
     * 刷新config的缓存里面
     */
    public void refreshConfigCache() {
        List<SystemConfig> list = systemConfigService.listAll(null);
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
