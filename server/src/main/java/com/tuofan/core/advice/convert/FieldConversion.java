package com.tuofan.core.advice.convert;

import io.swagger.annotations.ApiParam;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface FieldConversion {

    @ApiParam("是否处理父类")
    boolean convertParentFields() default false;

    @ApiParam("父类处理深度")
    int parentDepth() default 3;

    @ApiParam("是否处理为值对象的子类")
    boolean convertEmbedFields() default true;

}
