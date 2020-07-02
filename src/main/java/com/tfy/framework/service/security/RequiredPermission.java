package com.tfy.framework.service.security;


import java.lang.annotation.*;


/**
 * @author wubaoxin
 * @date 2018.11.07
 * 控制接口签名校验开关的注解。
 * 加在类上或方法上
 */
@Target({ElementType.TYPE, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Inherited
public @interface RequiredPermission {
}
