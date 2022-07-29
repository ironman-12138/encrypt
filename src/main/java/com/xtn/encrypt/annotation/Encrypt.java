package com.xtn.encrypt.annotation;

import com.xtn.encrypt.config.EncryptEnum;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 加密注解
 * @author xCoder
 */
@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
public @interface Encrypt {

    EncryptEnum encryptType() default EncryptEnum.AES;

}


