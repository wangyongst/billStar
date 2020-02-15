package com.tuofan.core.advice.auth;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
public @interface LoginRequired {


    /**
     * 超管才能进行的操作，超管的ID配在配置项里面
     *
     * @return
     */
    boolean superAdminOnly() default false;

    /**
     * 管理员才能进行的操作，管理员由超管指派
     *
     * @return
     */
    boolean adminOnly() default false;

}
