package org.mimosaframework.core.encryption;

import java.security.MessageDigest;

public class SHAUtils {

    public static String sha256(String str) {
        try {
            return sha("SHA-256", str.getBytes("UTF-8"));
        } catch (Exception e) {
            throw new RuntimeException("SHA加密出错", e);
        }
    }

    public static String sha512(String str) {
        try {
            return sha("SHA-512", str.getBytes("UTF-8"));
        } catch (Exception e) {
            throw new RuntimeException("SHA加密出错", e);
        }
    }

    public static String sha256(byte[] bytes) {
        try {
            return sha("SHA-256", bytes);
        } catch (Exception e) {
            throw new RuntimeException("SHA加密出错", e);
        }
    }

    public static String sha512(byte[] bytes) {
        try {
            return sha("SHA-512", bytes);
        } catch (Exception e) {
            throw new RuntimeException("SHA加密出错", e);
        }
    }

    private static String sha(String type, byte[] bytes) {
        MessageDigest messageDigest;
        String encodeStr = "";
        try {
            messageDigest = MessageDigest.getInstance(type);
            messageDigest.update(bytes);
            encodeStr = byte2Hex(messageDigest.digest());
        } catch (Exception e) {
            throw new RuntimeException("SHA加密出错", e);
        }
        return encodeStr;
    }

    /**
     * 将byte转为16进制
     *
     * @param bytes
     * @return
     */
    private static String byte2Hex(byte[] bytes) {
        StringBuffer stringBuffer = new StringBuffer();
        String temp = null;
        for (int i = 0; i < bytes.length; i++) {
            temp = Integer.toHexString(bytes[i] & 0xFF);
            if (temp.length() == 1) {
                //1得到一位的进行补0操作
                stringBuffer.append("0");
            }
            stringBuffer.append(temp);
        }
        return stringBuffer.toString();
    }
}
