package com.xtn.encrypt.utils;


import lombok.extern.slf4j.Slf4j;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.SecretKeySpec;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;

/**
 * AES工具类
 */
@Slf4j
public class AesUtil {

    private final static String AES="AES";
    private static final String AES_ALGORITHM = "AES/ECB/PKCS5Padding";


    /**
     * 获取cipher
     */
    private static Cipher getCipher(byte[] key,int model) throws NoSuchPaddingException, NoSuchAlgorithmException, InvalidKeyException {
        SecretKeySpec aes = new SecretKeySpec(key, AES);
        Cipher cipher = Cipher.getInstance(AES_ALGORITHM);
        cipher.init(model,aes);
        return cipher;
    }

    /**
     * 加密
     */
    public static String encrypt(byte[] data,byte[] key) throws NoSuchPaddingException, NoSuchAlgorithmException, InvalidKeyException, IllegalBlockSizeException, BadPaddingException {
        Cipher cipher = getCipher(key, Cipher.ENCRYPT_MODE);
        return Base64.getEncoder().encodeToString(cipher.doFinal(data));
    }

    /**
     * 解密
     */
    public static byte[] decrypt(byte[] data,byte[] key) throws NoSuchPaddingException, NoSuchAlgorithmException, InvalidKeyException, IllegalBlockSizeException, BadPaddingException {
        Cipher cipher = getCipher(key, Cipher.DECRYPT_MODE);
        return cipher.doFinal(Base64.getDecoder().decode(data));
    }
}
