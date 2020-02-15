package com.tuofan.core.advice.convert;

import com.github.pagehelper.PageInfo;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.tuofan.core.dto.DTO;
import com.tuofan.core.utils.ReflectionUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.CollectionUtils;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.List;
import java.util.Map;

@Component
@Aspect
@Slf4j
public class FieldConvertAspect {

    @Pointcut("@annotation(com.tuofan.core.advice.convert.FieldConversion)")
    public void fieldConvertAnnotation() {

    }

    @AfterReturning(value = "fieldConvertAnnotation()&&@annotation(anno)", returning = "returnValue")
    public void doAfterReturning(final JoinPoint joinPoint, Object returnValue, FieldConversion anno) {
        if (returnValue == null) {
            return;
        }
        // 获取返回值类型
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        Method m = signature.getMethod();
        Type rawType = m.getGenericReturnType();
        if (rawType instanceof ParameterizedType) {
            Type returnClazz = ((ParameterizedType) rawType).getRawType();
            // 返回值类型为 Object，List，PageInfo的 最后都转化为List处理
            if (returnClazz.getTypeName().equals(List.class.getName())) {
                List list = (List) returnValue;
                this.processList(list, anno.convertParentFields(), anno.convertEmbedFields(), anno.parentDepth());
                return;
            } else if (returnClazz.getTypeName().equals(PageInfo.class.getName())) {
                List list = ((PageInfo) returnValue).getList();
                this.processList(list, anno.convertParentFields(), anno.convertEmbedFields(), anno.parentDepth());
                return;
            }
        } else {
            List list = Lists.newArrayList(returnValue);
            list.add(returnValue);
            this.processList(list, anno.convertParentFields(), anno.convertEmbedFields(), anno.parentDepth());
            return;
        }
    }

    /**
     * 处理列表
     *
     * @param list
     */
    public void processList(List list, boolean processParent, boolean processEmbed, int parentDepth) {
        if (CollectionUtils.isEmpty(list)) {
            return;
        }
        FieldConvertUtils.convertList(list, processParent, processEmbed, parentDepth);
        if (processEmbed) {
            Map<Field, List<Object>> filedListMap = extractFieldObjectMap(list);
            for (Map.Entry<Field, List<Object>> entry : filedListMap.entrySet()) {
                processList(entry.getValue(), false, false, 0);
            }
        }
    }

    /**
     * 获取值对象列名，值对象列值数组Map
     *
     * @param list
     * @return
     */
    private Map<Field, List<Object>> extractFieldObjectMap(List list) {
        Map<Field, List<Object>> filedListMap = Maps.newHashMap();
        for (Object returnValue : list) {
            for (Field field : returnValue.getClass().getDeclaredFields()) {
                if (!field.getType().isInstance(DTO.class)) {
                    continue;
                }
                Object key = (Object) ReflectionUtils.getFieldValue(returnValue, field.getName());
                if (filedListMap.containsKey(field)) {
                    filedListMap.get(field).add(key);
                } else {
                    filedListMap.put(field, Lists.newArrayList(key));
                }
            }
        }
        return filedListMap;
    }

}
