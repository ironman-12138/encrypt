package com.xtn.encrypt.utils;

import com.fasterxml.jackson.databind.ObjectMapper;

import javax.crypto.Cipher;
import java.io.ByteArrayOutputStream;
import java.nio.charset.StandardCharsets;
import java.security.*;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.Base64;
import java.util.HashMap;
import java.util.Map;
import java.util.TreeSet;

public class RsaUtil {

    //1024大小秘钥(推荐使用)
    public final static int KEY_SIZE_1024 = 1024;

    //2048大小密钥
    public final static int KEY_SIZE_2048 = 2048;

    /**
     * RSA最大加密明文大小
     */
    private static final int MAX_ENCRYPT_BLOCK = 117;

    /**
     * RSA最大解密密文大小
     */
    private static final int MAX_DECRYPT_BLOCK = 128;

    public final static String Public_Key = "publicKey";
    public final static String Private_Key = "privateKey";
    public static final String KEY_ALGORITHM = "RSA";

    //加签算法
    public static final String SIGNATURE_ALGORITHM = "SHA1withRSA";
    //加签算法
    public static final String SIGNATURE_256 = "SHA256withRSA";

    public static Map<String, String> createPubKeyAndPriKey(int keySize) throws NoSuchAlgorithmException {

        // 生成公钥和私钥对，基于RSA算法生成对象
        KeyPairGenerator keyPairGen = KeyPairGenerator.getInstance(KEY_ALGORITHM);
        // 初始化密钥对生成器，密钥大小为1024位
        keyPairGen.initialize(keySize);
        // 生成一个密钥对，保存在keyPair中
        KeyPair keyPair = keyPairGen.generateKeyPair();
        // 得到私钥和公钥
        RSAPrivateKey privateKey = (RSAPrivateKey) keyPair.getPrivate();
        RSAPublicKey publicKey = (RSAPublicKey) keyPair.getPublic();

        Map<String, String> map = new HashMap<>();
        map.put(Private_Key, getKeyString(privateKey));
        map.put(Public_Key, getKeyString(publicKey));
        return map;
    }

    /**
     * 得到密钥字符串（经过base64编码）
     */
    private static String getKeyString(Key key) {
        byte[] keyBytes = key.getEncoded();
        return Base64.getEncoder().encodeToString(keyBytes);
    }

    /**
     * 得到公钥
     *
     * @param key 密钥字符串（经过base64编码）
     */
    public static PublicKey getPublicKey(String key) throws Exception {
        byte[] keyBytes = Base64.getDecoder().decode(key);
        X509EncodedKeySpec keySpec = new X509EncodedKeySpec(keyBytes);
        KeyFactory keyFactory = KeyFactory.getInstance(KEY_ALGORITHM);
        return keyFactory.generatePublic(keySpec);
    }

    /**
     * 得到私钥
     *
     * @param key 密钥字符串（经过base64编码）
     */
    private static PrivateKey getPrivateKey(String key) throws Exception {
        byte[] keyBytes = Base64.getDecoder().decode(key);
        PKCS8EncodedKeySpec keySpec = new PKCS8EncodedKeySpec(keyBytes);
        KeyFactory keyFactory = KeyFactory.getInstance(KEY_ALGORITHM);
        return keyFactory.generatePrivate(keySpec);
    }

    /**
     * 公钥加密
     *
     * @param publicKey 公钥
     * @param enStr     待加密字符串
     * @return Base64 加密字符串
     */
    public static String encrypt(String publicKey, String enStr) throws Exception {
        // Cipher负责完成加密或解密工作，基于RSA
        Cipher cipher = Cipher.getInstance(KEY_ALGORITHM);
        // 根据公钥，对Cipher对象进行初始化
        cipher.init(Cipher.ENCRYPT_MODE, getPublicKey(publicKey));
        // 加密，结果保存进resultBytes，并返回
        byte[] resultBytes = cipher.doFinal(enStr.getBytes());
        return Base64.getEncoder().encodeToString(resultBytes);
    }

    /**
     * 公钥加密
     *
     * @param publicKey 公钥
     * @param enByte     待加密字符串byte
     * @return Base64 加密字符串
     */
    public static String encrypt(String publicKey, byte[] enByte) throws Exception {
        // Cipher负责完成加密或解密工作，基于RSA
        Cipher cipher = Cipher.getInstance(KEY_ALGORITHM);
        // 根据公钥，对Cipher对象进行初始化
        cipher.init(Cipher.ENCRYPT_MODE, getPublicKey(publicKey));
        // 加密，结果保存进resultBytes，并返回
        byte[] resultBytes = cipher.doFinal(enByte);
        return Base64.getEncoder().encodeToString(resultBytes);
    }

    /**
     * 私钥 解密
     *
     * @param privateKey 私钥
     * @param deStr      使用Base64加密过的加密字符串
     * @return 字符串
     */
    public static String decrypt(String privateKey, String deStr) throws Exception {
        Cipher cipher = Cipher.getInstance(KEY_ALGORITHM);
        // 根据私钥对Cipher对象进行初始化
        cipher.init(Cipher.DECRYPT_MODE, getPrivateKey(privateKey));
        // 解密并将结果保存进resultBytes
        byte[] decBytes = cipher.doFinal(Base64.getDecoder().decode(deStr));
        return new String(decBytes);
    }

    /**
     * 私钥 解密
     *
     * @param privateKey 私钥
     * @param deByte      使用Base64加密过的加密字符串byte
     * @return 字符串
     */
    public static String decrypt(String privateKey, byte[] deByte) throws Exception {
        Cipher cipher = Cipher.getInstance(KEY_ALGORITHM);
        // 根据私钥对Cipher对象进行初始化
        cipher.init(Cipher.DECRYPT_MODE, getPrivateKey(privateKey));
        // 解密并将结果保存进resultBytes
        byte[] decBytes = cipher.doFinal(Base64.getDecoder().decode(deByte));
        return new String(decBytes);
    }

    /**
     * 私钥签名(默认算法：SIGNATURE_ALGORITHM)
     */
    public static String sign(String privateKey, String data) throws Exception {
        PrivateKey privateK = getPrivateKey(privateKey);
        Signature signature = Signature.getInstance(SIGNATURE_ALGORITHM);
        signature.initSign(privateK);
        signature.update(data.getBytes(StandardCharsets.UTF_8));
        return Base64.getEncoder().encodeToString(signature.sign());
    }

    /**
     * <p>
     * 用私钥对信息生成数字签名
     * </p>
     *
     * @param data       已加密数据
     * @param privateKey 私钥(BASE64编码)
     * @return
     * @throws Exception
     */
    public static String sign(byte[] data, String privateKey) throws Exception {
        byte[] keyBytes = Base64Util.decodeString(privateKey);
        PKCS8EncodedKeySpec pkcs8KeySpec = new PKCS8EncodedKeySpec(keyBytes);
        KeyFactory keyFactory = KeyFactory.getInstance(KEY_ALGORITHM);
        PrivateKey privateK = keyFactory.generatePrivate(pkcs8KeySpec);
        Signature signature = Signature.getInstance(SIGNATURE_256);
        signature.initSign(privateK);
        signature.update(data);
        return Base64Util.encodeByte(signature.sign());
    }

    /**
     * <p>
     * 私钥加密
     * </p>
     *
     * @param data       源数据
     * @param privateKey 私钥(BASE64编码)
     * @return
     * @throws Exception
     */
    public static byte[] encryptByPrivateKey(byte[] data, String privateKey)
            throws Exception {
        byte[] keyBytes = Base64Util.decodeString(privateKey);
        PKCS8EncodedKeySpec pkcs8KeySpec = new PKCS8EncodedKeySpec(keyBytes);
        KeyFactory keyFactory = KeyFactory.getInstance(KEY_ALGORITHM);
        Key privateK = keyFactory.generatePrivate(pkcs8KeySpec);
        Cipher cipher = Cipher.getInstance(keyFactory.getAlgorithm());
        cipher.init(Cipher.ENCRYPT_MODE, privateK);
        int inputLen = data.length;
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        int offSet = 0;
        byte[] cache;
        int i = 0;
        // 对数据分段加密
        while (inputLen - offSet > 0) {
            if (inputLen - offSet > MAX_ENCRYPT_BLOCK) {
                cache = cipher.doFinal(data, offSet, MAX_ENCRYPT_BLOCK);
            } else {
                cache = cipher.doFinal(data, offSet, inputLen - offSet);
            }
            out.write(cache, 0, cache.length);
            i++;
            offSet = i * MAX_ENCRYPT_BLOCK;
        }
        byte[] encryptedData = out.toByteArray();
        out.close();
        return encryptedData;
    }

    /*
     *私钥签名(默认算法:SIGNATURE_256)
     */
    public static String signSHA256(String privateKey, String data) throws Exception {
        return sign(privateKey, data, SIGNATURE_256);
    }

    /**
     * 私钥签名(选择签名算法)
     */
    public static String sign(String privateKey, String data, String type) throws Exception {
        PrivateKey privateK = getPrivateKey(privateKey);
        Signature signature = Signature.getInstance(type);
        signature.initSign(privateK);
        signature.update(data.getBytes());
        return Base64.getEncoder().encodeToString(signature.sign());
    }


    /**
     * 私钥签名
     */
    public static byte[] signGetByte(String privateKey, String data) throws Exception {
        PrivateKey privateK = getPrivateKey(privateKey);
        Signature signature = Signature.getInstance(SIGNATURE_ALGORITHM);
        signature.initSign(privateK);
        signature.update(data.getBytes());
        return signature.sign();
    }

    /**
     * 公钥验签（默认签名算法：SIGNATURE_ALGORITHM）
     */
    public static boolean verify(String data, String publicKey, String sign) throws Exception {
        return verify(data, publicKey, sign, SIGNATURE_ALGORITHM);
    }

    /**
     * 公钥验签（默认签名算法：SIGNATURE_256）
     */
    public static boolean verifySHA256(String data, String publicKey, String sign) throws Exception {
        return verify(data, publicKey, sign, SIGNATURE_256);
    }

    /**
     * 公钥验签(选择签名算法)
     */
    public static boolean verify(String data, String publicKey, String sign, String type) throws Exception {
        PublicKey publicK = getPublicKey(publicKey);
        Signature signature = Signature.getInstance(type);
        signature.initVerify(publicK);
        signature.update(data.getBytes());
        return signature.verify(Base64.getDecoder().decode(sign));
    }


    /**
     * 公钥加密
     *
     * @param publicKey 公钥
     * @param enStr     待加密字符串
     * @return Base64 加密字符串
     */
    public static String encryptPart(String publicKey, String enStr) throws Exception {
        // Cipher负责完成加密或解密工作，基于RSA
        Cipher cipher = Cipher.getInstance("RSA/ECB/NoPadding");
        // 根据公钥，对Cipher对象进行初始化
        cipher.init(Cipher.ENCRYPT_MODE, getPublicKey(publicKey));
        // 加密，结果保存进resultBytes，并返回
        byte[] bydata = enStr.getBytes();
        int inputLen = bydata.length;
        byte[] encryData;

        try (ByteArrayOutputStream out = new ByteArrayOutputStream()) {
            int offset = 0;
            int i = 0;
            for (; inputLen - offset > 0; offset = ++i * 256) {
                byte[] cache;

                if (inputLen - offset > 256) {
                    cache = cipher.doFinal(bydata, offset, 256);
                } else {
                    cache = cipher.doFinal(bydata, offset, inputLen - offset);
                }

                out.write(cache, 0, cache.length);
            }
            encryData = out.toByteArray();
        }
        return Base64.getEncoder().encodeToString(encryData);
    }

    /**
     * 对参数进行按ascii码排序生成验签data
     *
     * @param paramsMap 参数map
     * @return 按照ascii码排序好的字符串
     */
    public static String getVerifySignData(Map<String, Object> paramsMap) {
        TreeSet<String> treeSet = new TreeSet(paramsMap.keySet());
        StringBuilder content = new StringBuilder();
        int index = 0;
        for (String key : treeSet) {
            String value = (String) paramsMap.get(key);
            if (key != null && key.length() != 0 && value != null && value.length() != 0) {
                content.append(index == 0 ? "" : "&").append(key).append("=").append(value);
                index++;
            }
        }
        return content.toString();
    }

    public static void main(String[] args) throws Exception {

        Map<String, Object> params = new HashMap<>();
        String privateKey = "MIICdwIBADANBgkqhkiG9w0BAQEFAASCAmEwggJdAgEAAoGBAI4dSHvMEp8MLR8NR6WIRftlEk+zvW1TiPKJUhOFleEwl+yQxHB7C1odRCn7PK0qZsDbjgmROHbfxOCc7yincbOeQbqAsiRSllCZuWQZHKTd53zVewip7mVFqnco4Y6D3kDEVgL8IRRDdXCVZCY1gP8q6YZw3f7H4MM0es9bm0ZtAgMBAAECgYAuF8FSpbXEcCaSU7RRNAm+p+FwiFOACf/46iD8w+8AwsoDqFCOSto7uBJLq3jakU4SbyN6ZudxZtJsJ3pKLUTrCRea08P4HTOnkXgi0prbCk5/N6YbQZBwa6xHhmEZrku8JYZbVHlHTkMoZQhjFHNXG7dyNAtxU7ZawnmckcCA4QJBAMlsS6MHom6MWJ1RTQAjhLFYuFIitAh92AEWUi/PmSAPOg6xSEVkUP0AXfH2SP4jWX1dGLGkqoAi3aNseEP1D88CQQC0nwt9PCQIUTWvI1dtBIXz+25mLvY2WeLGyUprt98LS3rkPxIRigmdn6zkKpxpSzOcm8X1+oqI+VzWR0tMGTkDAkEAq4pFaFTqKwfvuBszXrjhG0CZyamwdcLnIRXnbLx6hmkxYq3tBNwIdU/Isnazd2yXnEKa8z9vzazj4XhRfF7K1QJATOBczYLpezjFdrO+JEKGmR/tD0TxupRrd1x5dnueTXpk5f0KPbjY+uPZfIu+YvqFDyUmDtoCWOZ54yIZJFyf7QJBAJR0c4V0zFSv9Kv8ipyV8Fpsx+LeFQVep1U4P8Wa5CiO+3lKMdq2kD7nF+w4HFUnCfxWpZy2ZpDX6hi4uWYpjsI=";
        String publicKey = "MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQCOHUh7zBKfDC0fDUeliEX7ZRJPs71tU4jyiVIThZXhMJfskMRwewtaHUQp+zytKmbA244JkTh238TgnO8op3GznkG6gLIkUpZQmblkGRyk3ed81XsIqe5lRap3KOGOg95AxFYC/CEUQ3VwlWQmNYD/KumGcN3+x+DDNHrPW5tGbQIDAQAB";

//        params.put("deviceCode", "test");
//        params.put("data", "123456");
//        params.put("dataType", "1");
//        params.put("time", "20210101121212");
//        params.put("remark", "无");
//
        String verifySignData = getVerifySignData(params);
        System.out.println("生成的排序——>" + verifySignData);
        //生成签名
        String sign = signSHA256(privateKey, verifySignData);
        System.out.println("生成的签名-->" + sign);
        //验签
        boolean verify = verifySHA256(verifySignData, publicKey, sign);
        System.out.println("验签的结果-->" + verify);

        params = new HashMap<>();
        params.put("name", "xxx");
        params.put("timestamp", "11111123333");

        verifySignData = getVerifySignData(params);
        System.out.println("生成的排序——>" + verifySignData);
        //生成签名
        sign = signSHA256(privateKey, verifySignData);
        System.out.println("生成的签名-->" + sign);
        //验签
        verify = verifySHA256(verifySignData, publicKey, sign);
        System.out.println("验签的结果-->" + verify);

    }
}
