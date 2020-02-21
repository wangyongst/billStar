package com.tuofan.core;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.tuofan.core.advice.convert.Converted;
import com.tuofan.core.global.SpringContextHolder;
import com.tuofan.core.utils.ReflectionUtils;
import com.tuofan.core.utils.StringUtils;
import com.tuofan.core.utils.SysBeanUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.ArrayUtils;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Created by tuofan on 2018-9-21.
 */
@Slf4j
@SuppressWarnings("unchecked")
public class FieldConvertUtils {
    private FieldConvertUtils() {
    }

    /**
     * 转换-只转换当前对象的数据
     *
     * @param list
     */
    public static void convertList(List list) {
        convertList(list, false, false, 0);
    }

    /**
     * @param list
     * @param includeParent 是否处理父类的字段
     * @param includeEmbed  是否处理Embeded字段
     * @param parentDepth   包含父类的深度
     */
    public static void convertList(List list, boolean includeParent, boolean includeEmbed, int parentDepth) {
        if (CollectionUtils.isEmpty(list)) {
            return;
        }
        Field[] fields = FieldConvertUtils.getFields(list.get(0), parentDepth, includeParent);
        if (fields == null) {
            return;
        }
        Map<Field, Map<Integer, String>> refBeanValueMap = FieldConvertUtils.getFieldFeignValueMap(list, fields);
        FieldConvertUtils.convertValue(list, fields, refBeanValueMap);
    }


    private static Field[] getFields(Object object, int maxLoop, boolean processParent) {
        if (!processParent) {
            return object.getClass().getDeclaredFields();
        }
        Field[] fields = {};

        Class<?> clazz = object.getClass();

        for (int i = 0; i < maxLoop && clazz != Object.class; clazz = clazz.getSuperclass()) {
            try {
                fields = ArrayUtils.addAll(fields, clazz.getDeclaredFields());
            } catch (Exception e) {
                // 这里甚么都不要做！并且这里的异常必须这样写，不能抛出去。
                // 如果这里的异常打印或者往外抛，则就不会执行clazz = clazz.getSuperclass(),最后就不会进入到父类中了
            }
        }

        return fields;
    }

    /**
     * 获取列表中要转换所有key和value
     *
     * @param list
     * @param fields
     * @return
     */
    private static Map<Field, Map<Integer, String>> getFieldFeignValueMap(List list, Field[] fields) {
        // 存放每个字段，转换前和转换后的对应值
        Map<Field, Map<Integer, String>> refFiledValueMap = Maps.newHashMap();

        for (Field field : fields) {
            Converted converted = field.getAnnotation(Converted.class);
            if (converted == null) {
                continue;
            }
            List<Integer> keyList = extractList(list, field);
            Map<Integer, String> keys2ValuesMap = convertKeys2Values(keyList, field);
            refFiledValueMap.put(field, keys2ValuesMap);
        }

        return refFiledValueMap;
    }

    private static Map<Integer, String> convertKeys2Values(List<Integer> keys, Field field) {
        if (CollectionUtils.isEmpty(keys)) {
            return Maps.newHashMap();
        }
        Converted converted = field.getAnnotation(Converted.class);

        Object beanService = SpringContextHolder.getBean(converted.bean());

        Object[] args = {keys};

        Method m = getMethod(converted.bean(), converted.refMethod());
        ParameterizedType rawType = (ParameterizedType) m.getGenericReturnType();
        Type actualType = rawType.getRawType();
        if (actualType.getTypeName().equals(Map.class.getName())) {
            return (Map<Integer, String>) ReflectionUtils.invokeMethodByName(beanService,
                    converted.refMethod(), args);
        } else if (actualType.getTypeName().equals(List.class.getName())) {
            List listT = (List) ReflectionUtils.invokeMethodByName(beanService, converted.refMethod(), args);
            if (CollectionUtils.isEmpty(listT)) {
                return Maps.newHashMap();
            }
            return convertList2MapFilterNull(listT, converted.refKey(),
                    converted.refLabel());
        } else {
            return Maps.newHashMap();
        }
    }


    private static <K, V> Map<K, V> convertList2MapFilterNull(List<?> list, String keyProperty, String valueProperty) {
        return (Map) list.stream().filter((x) -> {
            return null != SysBeanUtils.getRawProperty(x, valueProperty) && null != SysBeanUtils.getRawProperty(x, keyProperty);
        }).collect(Collectors.toMap((x) -> {
            return  SysBeanUtils.getRawProperty(x, keyProperty);
        }, (x) -> {
            return  SysBeanUtils.getRawProperty(x, valueProperty);
        }));
    }

    private static List<Integer> extractList(List list, Field field) {
        List<Integer> keyList = Lists.newArrayList();
        Converted converted = field.getAnnotation(Converted.class);
        for (Object returnValue : list) {
            if (converted != null) {
                // 获取要转换的key值
                Integer key = extractKey(returnValue, field, converted);
                if (key ==null ) {
                    continue;
                }
                keyList.add(key);
            }
        }
        return keyList;
    }

    /**
     * 获取key，注解上有dependProperty属性，则取这个属性的值，否则就是当前filed的值
     *
     * @param returnValue
     * @param field
     * @param converted
     * @return
     */
    private static Integer extractKey(Object returnValue, Field field, Converted converted) {
        Integer key;
        if (StringUtils.isBlank(converted.dependProperty())) {
            key = (Integer) ReflectionUtils.getFieldValue(returnValue, field.getName());
        } else {
            key = (Integer) ReflectionUtils.getFieldValue(returnValue, converted.dependProperty());
        }
        return key;
    }

    /**
     * 根据取到的值进行转换
     *
     * @param list
     * @param refFiledValueMap
     */
    private static void convertValue(List list, Field[] fields, Map<Field, Map<Integer, String>> refFiledValueMap) {
        for (Object returnValue : list) {
            for (Field field : fields) {
                Converted converted = field.getAnnotation(Converted.class);
                if (converted == null) {
                    continue;
                }
                Integer key = extractKey(returnValue, field, converted);
                if (refFiledValueMap.containsKey(field) && refFiledValueMap.get(field).containsKey(key)) {
                    ReflectionUtils.setFieldValue(returnValue, field.getName(), refFiledValueMap.get(field).get(key));
                } else {
                    ReflectionUtils.setFieldValue(returnValue, field.getName(), converted.defaultValue());
                }
            }
        }
    }

    /**
     * @param clazzT
     * @param methodName
     * @return
     */
    private static Method getMethod(Class clazzT, String methodName) {
        if (clazzT == null || clazzT == Object.class || StringUtils.isEmpty(methodName)) {
            return null;
        }
        for (; clazzT.getSuperclass() != Object.class; clazzT = clazzT.getSuperclass()) {
            Method[] methods = clazzT.getDeclaredMethods();
            for (Method m : methods) {
                if (m.getName().equals(methodName)) {
                    return m;
                }
            }
        }
        return null;
    }
}
