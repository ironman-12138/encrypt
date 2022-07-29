package com.xtn.encrypt.annotation;

import com.xtn.encrypt.config.dataMasking.DataMaskingFunc;

import java.lang.annotation.*;

/**
 * 数据脱敏注解
 * @author xCoder 2022-7-5
 */
@Target({ElementType.FIELD, ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface DataMasking {

    DataMaskingFunc maskFunc() default DataMaskingFunc.NO_MASK;

}