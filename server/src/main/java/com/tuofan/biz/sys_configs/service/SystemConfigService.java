package com.tuofan.biz.sys_configs.service;

import com.tuofan.biz.sys_configs.dao.SystemConfigDao;
import com.tuofan.biz.sys_configs.entity.SystemConfig;
import com.tuofan.core.persistence.service.CrudRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/**
 * 不要操作，使用 ConfigCacheUtils 写入缓存处理
 */
@Slf4j
@Service
public class SystemConfigService extends CrudRepository<SystemConfigDao, SystemConfig> {

}
