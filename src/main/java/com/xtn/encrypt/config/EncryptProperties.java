package com.xtn.encrypt.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "body.encrypt")
@Data
public class EncryptProperties {
    private String key;
}
