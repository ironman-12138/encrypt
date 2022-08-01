package com.xtn.encrypt.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.xtn.encrypt.annotation.Encrypt;
import com.xtn.encrypt.utils.AesUtil;
import com.xtn.encrypt.utils.RsaUtil;
import com.xtn.encrypt.utils.result.Result;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.core.MethodParameter;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyAdvice;

import javax.annotation.Resource;

/**
 * 对返回数据加密处理
 * @author xCoder
 */
@EnableConfigurationProperties(EncryptProperties.class)
@ControllerAdvice
public class EncryptResponse implements ResponseBodyAdvice<Result> {

    @Resource
    private EncryptProperties encryptProperties;

    //用来Object对象的转换
    private ObjectMapper objectMapper = new ObjectMapper();


    /**
     * 支持什么时候加密
     */
    @Override
    public boolean supports(MethodParameter methodParameter, Class<? extends HttpMessageConverter<?>> aClass) {
        return methodParameter.hasMethodAnnotation(Encrypt.class);
    }


    /**
     * 数据响应进行加密
     */
    @Override
    public Result beforeBodyWrite(Result r, MethodParameter methodParameter, MediaType mediaType, Class<? extends HttpMessageConverter<?>> aClass, ServerHttpRequest serverHttpRequest, ServerHttpResponse serverHttpResponse) {

        Encrypt encrypt = methodParameter.getMethodAnnotation(Encrypt.class);
        EncryptEnum encryptEnum = encrypt.encryptType();

        Object data = null;
        try {
            switch (encryptEnum) {
                case AES:
                    //获取加密key
                    String key = encryptProperties.getKey();
                    data = AesUtil.encrypt(objectMapper.writeValueAsBytes(r.getData()), key.getBytes());
                    break;
                case RSA:
                    String publicKey = encryptProperties.getPublicKey();
                    data = RsaUtil.encrypt(publicKey, objectMapper.writeValueAsBytes(r.getData()));
                    break;
            }

            //对返回数据进行加密后返回
            r.setData(data);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return r;
    }

}
