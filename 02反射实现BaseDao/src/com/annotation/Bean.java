package com.annotation;

import java.lang.annotation.*;

/**
 *  类与表对应关系的注解
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface Bean {
//    注解的一个属性，在使用时用于注解的括号中赋值
    String value();
}
