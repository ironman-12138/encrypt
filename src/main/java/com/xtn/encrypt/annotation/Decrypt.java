package com.xtn.encrypt.annotation;

import com.xtn.encrypt.config.EncryptEnum;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 解密注解
 * @author xCoder
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.METHOD, ElementType.PARAMETER, ElementType.FIELD})
public @interface Decrypt {

    EncryptEnum encryptType() default EncryptEnum.AES;

}

