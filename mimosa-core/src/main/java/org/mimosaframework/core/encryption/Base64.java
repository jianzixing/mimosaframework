package org.mimosaframework.core.encryption;

import java.io.IOException;

public class Base64 {

    public static String encode(String str) {
        return org.apache.commons.codec.binary.Base64.encodeBase64String(str.getBytes());
    }

    public static String encode(byte[] bytes) {
        return org.apache.commons.codec.binary.Base64.encodeBase64String(bytes);
    }

    public static String decode(String encode) {
        return new String(org.apache.commons.codec.binary.Base64.decodeBase64(encode));
    }

    public static String decode(String encode, String charset) throws IOException {
        return new String(org.apache.commons.codec.binary.Base64.decodeBase64(encode.getBytes(charset)));
    }

    public static byte[] decodeBase64(String encode) {
        return org.apache.commons.codec.binary.Base64.decodeBase64(encode);
    }

    public static void main(String[] args) {
//        System.out.println(org.apache.commons.codec.binary.Base64.encodeBase64URLSafeString("abc".getBytes()));
        System.out.println(encode("AFDEWS阿萨德记得发快递卡的接口离开键盘的房价将拍摄地方"));
    }
}
