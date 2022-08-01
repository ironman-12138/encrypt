package com.xtn.encrypt.utils;

import org.springframework.util.StringUtils;

import java.util.Map;
import java.util.TreeSet;

/**
 * @author xCoder
 */
public class StringUtil {

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

    public static String parseByte2HexStr(byte[] buf) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0, j = buf.length; i < j; i++) {
            String hex = Integer.toHexString(buf[i] & 0xFF);
            if (hex.length() == 1) {
                hex = '0' + hex;
            }
            sb.append(hex.toUpperCase());
        }
        return sb.toString();
    }

    public static byte[] parseHexStr2Byte(String hexStr) {
        if (!StringUtils.hasText(hexStr)) {
            return null;
        }
        byte[] result = new byte[hexStr.length() / 2];
        for (int i = 0, j = hexStr.length(); i < j / 2; i++) {
            int high = Integer.parseInt(hexStr.substring(i << 1, (i << 1) + 1), 16);
            int low = Integer.parseInt(hexStr.substring((i << 1) + 1, (i << 1) + 2), 16);
            result[i] = (byte) ((high << 4) + low);
        }
        return result;
    }

}
