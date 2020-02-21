package com.tuofan.core.utils;

import com.google.common.collect.Maps;
import com.tuofan.core.BizException;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.beanutils.BeanUtilsBean;
import org.apache.commons.beanutils.NestedNullException;
import org.apache.commons.beanutils.PropertyUtilsBean;
import org.springframework.beans.BeanWrapper;
import org.springframework.beans.BeanWrapperImpl;
import org.springframework.beans.BeansException;

import java.beans.PropertyDescriptor;
import java.lang.reflect.InvocationTargetException;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * 扩展BeanUtils工具.
 *
 * @author tuofan
 */
@SuppressWarnings("unused")
@Slf4j
public class SysBeanUtils extends org.apache.commons.beanutils.BeanUtils {

    public static Map<String, Object> toMap(Object bean) {
        Map<String, Object> params = Maps.newHashMap();
        try {
            PropertyUtilsBean propertyUtilsBean = new PropertyUtilsBean();
            PropertyDescriptor[] descriptors = propertyUtilsBean.getPropertyDescriptors(bean);
            for (PropertyDescriptor descriptor : descriptors) {
                String name = descriptor.getName();
                if (!"class".equals(name)) {
                    if (Map.class.isAssignableFrom(descriptor.getPropertyType())) {
                        Map map = (Map) descriptor.getReadMethod().invoke(bean);
                        if (map != null) {
                            parseMap(params, name + ".", map);
                        }
                    } else {
                        params.put(name, propertyUtilsBean.getNestedProperty(bean, name));
                    }
                }
            }
        } catch (Exception e) {
            log.error("bean toMap exception.bean=" + bean, e);
        }
        return params;
    }

    private static void parseMap(Map<String, Object> params, String prefix, Map<?, ?> map) {
        for (Map.Entry entry : map.entrySet()) {
            params.put(prefix + entry.getKey(), entry.getValue());
        }
    }

    public static Object cloneBean(Object bean) {
        try {
            return BeanUtilsBean.getInstance().cloneBean(bean);
        } catch (IllegalAccessException | InvocationTargetException | NoSuchMethodException
                | InstantiationException e) {
            // ignore
        }
        return null;
    }

    public static void copyProperties(Object dest, Object orig) {
        try {
            BeanUtilsBean.getInstance().copyProperties(dest, orig);
        } catch (IllegalAccessException | InvocationTargetException e) {
            log.error("copyProperties fail,dest class=" + dest.getClass() + ",orig class=" + orig.getClass(), e);
            throw new BizException(e);
        }
    }

    public static void copyProperty(Object bean, String name, Object value) {
        try {
            BeanUtilsBean.getInstance().copyProperty(bean, name, value);
        } catch (IllegalAccessException | InvocationTargetException e) {
            log.error("copyProperty fail,bean=" + bean.getClass() + ",name=" + name + ",value=" + value, e);
            throw new BizException(e);
        }
    }

    /**
     * 忽略null属性，用spring 的BeanUtils
     *
     * @param dest
     * @param orig
     */
    public static void copyPropertiesIgnoreNull(Object dest, Object orig) {
        try {
            org.springframework.beans.BeanUtils.copyProperties(orig, dest, getNullPropertyNames(orig));
        } catch (BeansException e) {
            log.error("copyProperties fail,dest class=" + dest.getClass() + ",orig class=" + orig.getClass(), e);
            throw new BizException(e);
        }
    }

    public static String[] getNullPropertyNames(Object source) {
        final BeanWrapper src = new BeanWrapperImpl(source);
        PropertyDescriptor[] pds = src.getPropertyDescriptors();

        Set<String> emptyNames = new HashSet<String>();
        for (PropertyDescriptor pd : pds) {
            Object srcValue = src.getPropertyValue(pd.getName());
            if (srcValue == null)
                emptyNames.add(pd.getName());
        }
        String[] result = new String[emptyNames.size()];
        return emptyNames.toArray(result);
    }

    public static Map<String, String> describe(Object bean) {
        try {
            return BeanUtilsBean.getInstance().describe(bean);
        } catch (IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
            log.error("describe fail,bean=" + bean.getClass(), e);
        }
        return null;
    }

    public static String[] getArrayProperty(Object bean, String name) {
        try {
            return BeanUtilsBean.getInstance().getArrayProperty(bean, name);
        } catch (IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
            log.error("getArrayProperty fail,bean=" + bean.getClass() + ",name=" + name, e);
        }
        return new String[0];
    }

    public static String getIndexedProperty(Object bean, String name) {
        try {
            return BeanUtilsBean.getInstance().getIndexedProperty(bean, name);
        } catch (IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
            log.error("getIndexedProperty fail,bean=" + bean.getClass() + ",name=" + name, e);
        }
        return null;
    }

    public static String getIndexedProperty(Object bean, String name, int index) {
        try {
            return BeanUtilsBean.getInstance().getIndexedProperty(bean, name, index);
        } catch (IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
            log.error("getIndexedProperty fail,bean=" + bean.getClass() + ",name=" + name + ",index=" + index, e);
        }
        return null;
    }

    public static String getMappedProperty(Object bean, String name) {
        try {
            return BeanUtilsBean.getInstance().getMappedProperty(bean, name);
        } catch (IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
            log.error("getMappedProperty fail,bean=" + bean.getClass() + ",name=" + name, e);
        }
        return null;
    }

    public static String getMappedProperty(Object bean, String name, String key) {
        try {
            return BeanUtilsBean.getInstance().getMappedProperty(bean, name, key);
        } catch (IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
            log.error("getMappedProperty fail,bean=" + bean.getClass() + ",name=" + name + ",key=" + key, e);
        }
        return null;
    }

    public static String getNestedProperty(Object bean, String name) {
        try {
            return BeanUtilsBean.getInstance().getNestedProperty(bean, name);
        } catch (IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
            log.error("getNestedProperty fail,bean=" + bean.getClass() + ",name=" + name, e);
        }
        return null;
    }

    public static Object getRawProperty(Object bean, String name) {
        try {
            return BeanUtilsBean.getInstance().getPropertyUtils().getProperty(bean, name);
        } catch (Exception e) {
            log.error("getRawProperty fail,bean=" + bean.getClass() + ",name=" + name, e);
        }
        return null;
    }

    public static String getProperty(Object bean, String name) {
        try {
            return BeanUtilsBean.getInstance().getProperty(bean, name);
        } catch (NestedNullException nestedNullException) {
            // ignore
        } catch (Exception e) {
            log.error("getProperty fail,bean=" + bean.getClass() + ",name=" + name, e);
        }
        return null;
    }

    public static String getSimpleProperty(Object bean, String name) {
        try {
            return BeanUtilsBean.getInstance().getSimpleProperty(bean, name);
        } catch (IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
            log.error("getSimpleProperty fail,bean=" + bean.getClass() + ",name=" + name, e);
        }
        return null;
    }

    public static void populate(Object bean, Map<String, ?> properties) {
        try {
            BeanUtilsBean.getInstance().populate(bean, properties);
        } catch (IllegalAccessException | InvocationTargetException e) {
            log.error("populate fail,bean=" + bean.getClass() + ",properties=" + properties, e);
        }
    }

    public static void setProperty(Object bean, String name, Object value) {
        try {
            BeanUtilsBean.getInstance().setProperty(bean, name, value);
        } catch (IllegalAccessException | InvocationTargetException e) {
            log.error("setProperty fail,bean=" + bean.getClass() + ",name=" + name + ",value=" + value, e);
        }
    }

}
