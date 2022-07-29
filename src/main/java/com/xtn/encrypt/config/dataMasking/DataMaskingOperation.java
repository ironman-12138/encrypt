package com.xtn.encrypt.config.dataMasking;

public interface DataMaskingOperation {

    String MASK_CHAR = "*";

    String mask(String content);

}





