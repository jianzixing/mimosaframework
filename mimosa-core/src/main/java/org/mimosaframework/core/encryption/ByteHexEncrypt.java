package org.mimosaframework.core.encryption;

import java.io.UnsupportedEncodingException;

public class ByteHexEncrypt {

    private static final char[] HEX_DIGITS = {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'A', 'B', 'C', 'D', 'E', 'F'};

    public static String byteArrayToHex(byte[] byteArray) {
        // 首先初始化一个字符数组，用来存放每个16进制字符
        // new一个字符数组，这个就是用来组成结果字符串的（解释一下：一个byte是八位二进制，也就是2位十六进制字符（2的8次方等于16的2次方））
        char[] resultCharArray = new char[byteArray.length * 2];
        // 遍历字节数组，通过位运算（位运算效率高），转换成字符放到字符数组中去
        int index = 0;
        for (byte b : byteArray) {
            resultCharArray[index++] = HEX_DIGITS[b >>> 4 & 0xf];
            resultCharArray[index++] = HEX_DIGITS[b & 0xf];
        }
        // 字符数组组合成字符串返回
        return new String(resultCharArray);
    }

    public static String byteToHex(byte b) {
        char[] Digit = {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'A',
                'B', 'C', 'D', 'E', 'F'};
        char[] ob = new char[2];
        ob[0] = Digit[(b >>> 4) & 0xf];
        ob[1] = Digit[b & 0xf];
        String s = new String(ob);
        return s;
    }

    public static byte[] hexStringToBytes(String hexString) {
        if (hexString == null || hexString.equals("")) {
            return null;
        }

        hexString = hexString.toUpperCase();
        int length = hexString.length() / 2;
        char[] hexChars = hexString.toCharArray();
        byte[] d = new byte[length];
        for (int i = 0; i < length; i++) {
            int pos = i * 2;
            d[i] = (byte) (charToByte(hexChars[pos]) << 4 | charToByte(hexChars[pos + 1]));
        }
        return d;
    }

    private static byte charToByte(char c) {
        return (byte) "0123456789ABCDEF".indexOf(c);
    }

    public static String toHex(String str) {
        return ByteHexEncrypt.byteArrayToHex(str.getBytes());
    }

    public static String toHex(String str, String charset) throws UnsupportedEncodingException {
        return ByteHexEncrypt.byteArrayToHex(str.getBytes(charset));
    }

    public static String toStr(String hexStr) {
        byte[] bytes = ByteHexEncrypt.hexStringToBytes(hexStr);
        return new String(bytes);
    }

    public static String toStr(String hexStr, String charset) throws UnsupportedEncodingException {
        byte[] bytes = ByteHexEncrypt.hexStringToBytes(hexStr);
        return new String(bytes, charset);
    }

    public static void main(String[] args) {
        String hex = ByteHexEncrypt.byteArrayToHex("你好".getBytes());
        byte[] bytes = ByteHexEncrypt.hexStringToBytes(hex);
        System.out.println(hex);
        System.out.println(new String(bytes));
    }
}
