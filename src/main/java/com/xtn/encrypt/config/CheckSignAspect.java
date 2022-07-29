package com.xtn.encrypt.config;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;

/**
 * @author xCoder
 */
@Aspect
@Component
@Slf4j
public class CheckSignAspect {

    @Pointcut("@annotation(com.xtn.encrypt.annotation.CheckSign)")
    public void pointcut() {
    }

    @Before(value = "pointcut()")
    public void before(JoinPoint joinPoint) {
        log.info("验签---------------开始");
        Object aThis = joinPoint.getThis();
        System.out.println(aThis);
        Object[] args = joinPoint.getArgs();
        System.out.println(args[0]);
        log.info("验签---------------结束");
    }

}
