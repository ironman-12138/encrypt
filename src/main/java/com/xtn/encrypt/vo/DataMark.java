package com.xtn.encrypt.vo;

import com.xtn.encrypt.annotation.DataMasking;
import com.xtn.encrypt.config.dataMasking.DataMaskingFunc;
import lombok.Data;

@Data
public class DataMark {

    @DataMasking(maskFunc = DataMaskingFunc.ALL_MASK)
    private String name;

    @DataMasking(maskFunc = DataMaskingFunc.PHONE_MASK)
    private String phone;

    @DataMasking(maskFunc = DataMaskingFunc.ID_CARD_MASK)
    private String idCard;

}
