package com.tuofan.core.cache;

import com.google.common.collect.Maps;
import lombok.extern.slf4j.Slf4j;

import java.util.Map;

/**
 * 本地缓存
 */
@Slf4j
public class LocalCacheUtils {

    private static Map<String, Object> cacheMap = Maps.newHashMap();

    /**
     * 取出全部
     *
     * @return 所有缓存值
     */
    public static Map<String, Object> fetchAll() {
        return LocalCacheUtils.cacheMap;
    }

    /**
     * 塞值
     *
     * @param key   键
     * @param value 值
     */
    public static void putItem(String key, Object value) {
        LocalCacheUtils.cacheMap.put(key, value);
    }

    /**
     * 获取值
     *
     * @param key 键
     */
    public static Object getItem(String key) {
        log.info("缓存取值：{}", key);
        return LocalCacheUtils.cacheMap.get(key);
    }

    /**
     * 是否含有某个键值
     *
     * @param key 键
     */
    public static boolean containsKey(String key) {
        return LocalCacheUtils.cacheMap.containsKey(key);
    }

}
