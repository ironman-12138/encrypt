package com.xtn.encrypt.config;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;

import java.util.Map;

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
    public void before(JoinPoint joinPoint) throws JsonProcessingException {
        log.info("验签---------------开始");
        Object aThis = joinPoint.getThis();
        System.out.println(aThis);
        Object[] args = joinPoint.getArgs();
        System.out.println(args[0]);

        ObjectMapper mapper = new ObjectMapper();
        Map<String, String> map = mapper.readValue(mapper.writeValueAsString(args[0]), Map.class);
        for(Map.Entry<String, String> entry:map.entrySet()){
            System.out.println(entry.getKey()+"--->"+entry.getValue());
        }
        log.info("验签---------------结束");
    }

}
