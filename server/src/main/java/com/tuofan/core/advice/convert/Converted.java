package com.tuofan.core.advice.convert;

import io.swagger.annotations.ApiParam;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
public @interface Converted {
    @ApiParam("根据当前类实体字典型key的字段，如 userType,默认为空，表示用自己的值当key")
    String dependProperty() ;

    @ApiParam("形式为：List<T> refMethod(List<dependProperty>) , 或者Map<String,String> refMethod(List<dependProperty>)的关联实体方法")
    String refMethod() default "listByIds";

    @ApiParam("关联实体组成Map的key值")
    String refKey() default "id";

    @ApiParam("如果method返回值为List<T>使用T.getLabel()如T.getName() 给当前关联实体赋值")
    String refLabel() default "name";

    @ApiParam("未能成功转换给的默认值")
    String defaultValue() default "";

    @ApiParam("BeanService,如 UserService.class")
    Class<? extends Object> bean();
}