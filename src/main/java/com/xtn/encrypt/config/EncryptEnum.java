package com.xtn.encrypt.config;

public enum EncryptEnum {

    AES("AES加密"),
    RSA("RSA加密");

    private String content;

    EncryptEnum(String content) {
        this.content = content;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}
