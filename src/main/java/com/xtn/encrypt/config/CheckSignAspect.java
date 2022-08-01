package com.xtn.encrypt.config;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.xtn.encrypt.utils.RsaUtil;
import com.xtn.encrypt.utils.StringUtil;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.Map;

/**
 * @author xCoder
 */
@Aspect
@Component
@Slf4j
public class CheckSignAspect {

    @Resource
    private EncryptProperties encryptProperties;

    @Pointcut("@annotation(com.xtn.encrypt.annotation.CheckSign)")
    public void pointcut() {
    }

    @Before(value = "pointcut()")
    public void before(JoinPoint joinPoint) throws Exception {
        log.info("---------------验签开始---------------");
        Object[] args = joinPoint.getArgs();

        ObjectMapper mapper = new ObjectMapper();
        Map<String, Object> map = mapper.readValue(mapper.writeValueAsString(args[0]), Map.class);
        String sign = (String) map.get("sign");
        map.remove("sign");
        boolean verify = RsaUtil.verifySHA256(StringUtil.getVerifySignData(map), encryptProperties.getPublicKey(), sign);
        System.out.println("验证结果:" + verify);
        if (!verify) {
            throw new Exception("验签失败");
        }
        log.info("---------------验签结束---------------");
    }

}
