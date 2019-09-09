package org.mimosaframework.core.encryption;

import sun.misc.BASE64Decoder;
import sun.misc.BASE64Encoder;

import java.io.IOException;

public class Base64 {

    public static final String encode(String str) {
        BASE64Encoder encoder = new BASE64Encoder();
        String encode = encoder.encode(str.getBytes());//编码
        return encode;
    }

    public static final String encode(byte[] bytes) {
        BASE64Encoder encoder = new BASE64Encoder();
        String encode = encoder.encode(bytes);//编码
        return encode;
    }

    public static final String decode(String encode) throws IOException {
        BASE64Decoder decoder = new BASE64Decoder();
        String decode = new String(decoder.decodeBuffer(encode));//解码
        return decode;
    }

    public static final String decode(String encode, String charset) throws IOException {
        BASE64Decoder decoder = new BASE64Decoder();
        String decode = new String(decoder.decodeBuffer(encode), charset);//解码
        return decode;
    }

    public static final byte[] decodeBase64(String encode) throws IOException {
        BASE64Decoder decoder = new BASE64Decoder();
        return decoder.decodeBuffer(encode);
    }

    public static void main(String[] args) {
//        System.out.println(org.apache.commons.codec.binary.Base64.encodeBase64URLSafeString("abc".getBytes()));
        System.out.println(encode("AFDEWS阿萨德记得发快递卡的接口离开键盘的房价将拍摄地方"));
    }
}
