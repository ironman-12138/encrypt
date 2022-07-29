package com.xtn.encrypt.config.dataMasking;

import org.springframework.util.StringUtils;

/**
 * 脱敏转换器
 * @author xCoder
 */
public enum DataMaskingFunc {

    /**
     * 不脱敏
     */
    NO_MASK((str) -> {
        return str;
    }),

    /**
     * 全部脱敏
     */
    ALL_MASK((str) -> {
        if (StringUtils.hasLength(str)) {
            StringBuilder sb = new StringBuilder();
            for (int i = 0; i < str.length(); i++) {
                sb.append(DataMaskingOperation.MASK_CHAR);
            }
            return sb.toString();
        } else {
            return str;
        }
    }),

    /**
     * 手机号脱敏
     */
    PHONE_MASK((str) -> {
        if (StringUtils.hasLength(str)) {
            return str.replaceAll("(\\d{3})\\d*(\\d{4})", "$1****$2");
        } else {
            return str;
        }
    }),

    /**
     * 身份证脱敏
     */
    ID_CARD_MASK((str) -> {
        if (StringUtils.hasLength(str)) {
            return str.replaceAll("(?<=\\w{3})\\w(?=\\w{4})", DataMaskingOperation.MASK_CHAR);
        } else {
            return str;
        }
    });

    private final DataMaskingOperation operation;

    private DataMaskingFunc(DataMaskingOperation operation) {
        this.operation = operation;
    }

    public DataMaskingOperation operation() {
        return this.operation;
    }

}
