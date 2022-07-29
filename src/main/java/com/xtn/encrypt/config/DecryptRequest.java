package com.xtn.encrypt.config;

import com.xtn.encrypt.annotation.Decrypt;
import com.xtn.encrypt.annotation.Encrypt;
import com.xtn.encrypt.utils.AESUtil;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.core.MethodParameter;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpInputMessage;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.RequestBodyAdviceAdapter;

import javax.annotation.Resource;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Type;

/**
 * 对传参数据解密处理
 * @author xCoder
 */
@EnableConfigurationProperties(EncryptProperties.class)
@ControllerAdvice //局数据处理组件
public class DecryptRequest extends RequestBodyAdviceAdapter {

    //引入加密属性类 主要是为了获取配置文件中的密钥
    @Resource
    private EncryptProperties encryptProperties;


    /**
     * 配置支持条件
     * 这里是只有方法或者参数有Decrypt注解的时候才生效
     */
    @Override
    public boolean supports(MethodParameter methodParameter, Type type, Class<? extends HttpMessageConverter<?>> aClass) {
        return methodParameter.hasMethodAnnotation(Decrypt.class) || methodParameter.hasParameterAnnotation(Decrypt.class);
    }


    /**
     * 使用aop，在读取请求参数的之前，对请求参数进行解密
     */
    @Override
    public HttpInputMessage beforeBodyRead(HttpInputMessage inputMessage, MethodParameter parameter, Type targetType, Class<? extends HttpMessageConverter<?>> converterType) throws IOException {

        byte[] decrypt = null;
        //读取请求参数成为字节body
        byte[] body = new byte[inputMessage.getBody().available()];
        inputMessage.getBody().read(body);

        Decrypt encrypt = parameter.getParameterAnnotation(Decrypt.class);
        EncryptEnum encryptEnum = encrypt.encryptType();

        try {
            switch (encryptEnum) {
                case AES:
                    //获取密钥的字节
                    byte[] keyByte = encryptProperties.getKey().getBytes();
                    decrypt = AESUtil.decrypt(body, keyByte);
                    break;
                case RSA:
                    break;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        try {
            //将解密的字节放入字节数组输入流中
            ByteArrayInputStream batis = new ByteArrayInputStream(decrypt);

            //返回HttpInputMessage
            return new HttpInputMessage() {
                @Override
                public InputStream getBody() throws IOException {
                    return batis;
                }

                @Override
                public HttpHeaders getHeaders() {
                    return inputMessage.getHeaders();
                }
            };
        } catch (Exception e) {
            e.printStackTrace();
        }

        //如果不需要进行处理的话，则直接调用父类的beforeBodyRead，相当于直接返回inputMessage，不做处理
        return super.beforeBodyRead(inputMessage, parameter, targetType, converterType);
    }
}