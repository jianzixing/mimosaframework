package org.mimosaframework.core.utils;

import java.util.UUID;

public class RandomUtils {
    private static final String LETTER = "abcdefghijklmnopqrstuvwxyz";
    private static final String LETTER_CHARS = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ";
    private static final String HEX_CHARS = "0123456789abcdef";
    private static final String NUMBER_CHARS = "0123456789";

    private static String[] chars = new String[]{"a", "b", "c", "d", "e", "f",
            "g", "h", "i", "j", "k", "l", "m", "n", "o", "p", "q", "r", "s",
            "t", "u", "v", "w", "x", "y", "z", "0", "1", "2", "3", "4", "5",
            "6", "7", "8", "9", "A", "B", "C", "D", "E", "F", "G", "H", "I",
            "J", "K", "L", "M", "N", "O", "P", "Q", "R", "S", "T", "U", "V",
            "W", "X", "Y", "Z"};

    /**
     * 生成 min 和 max 之间的一个数，包含两个数字本身
     *
     * @param min
     * @param max
     * @return
     */
    public static long randomNumber(long min, long max) {
        long s = Math.round(Math.random() * (max - min) + min);
        return s;
    }

    public static int randomNumber(int min, int max) {
        int s = (int) Math.round(Math.random() * (max - min) + min);
        return s;
    }

    public static long randomNumber(int len) {
        long max = (long) (Math.pow(10, len) - 1);
        long min = (long) (Math.pow(10, len - 1));
        long s = Math.round(Math.random() * (max - min) + min);
        return s;
    }

    public static String randomDecimalNumber(double min, double max, int dot) {
        double s = Math.random() * (max - min) + min;
        return String.format("%." + dot + "f", s);
    }

    /**
     * 随机len长度的小写字母
     *
     * @param len
     * @return
     */
    public static String randomLetter(int len) {
        char[] chars = new char[len];
        for (int i = 0; i < len; i++) {
            chars[i] = LETTER.charAt((int) (Math.random() * 26));
        }
        return new String(chars);
    }

    public static String randomLetter(int start, int end) {
        return randomLetter((int) randomNumber(start, end));
    }

    /**
     * 随机len长度的大写字母
     *
     * @param len
     * @return
     */
    public static String randomUpper(int len) {
        return randomLetter(len).toUpperCase();
    }

    public static String randomUpper(int start, int end) {
        return randomUpper((int) randomNumber(start, end));
    }

    /**
     * 随机len长度忽略大小写的字母
     *
     * @param len
     * @return
     */
    public static String randomIgnoreCaseLetter(int len) {
        char[] chars = new char[len];
        for (int i = 0; i < len; i++) {
            chars[i] = LETTER_CHARS.charAt((int) (Math.random() * 52));
        }
        return new String(chars);
    }

    public static String randomIgnoreCaseLetter(int start, int end) {
        return randomIgnoreCaseLetter((int) randomNumber(start, end));
    }

    public static String randomAlphanumericLetter(int len) {
        String nchar = NUMBER_CHARS + LETTER;
        char[] chars = new char[len];
        for (int i = 0; i < len; i++) {
            chars[i] = nchar.charAt((int) (Math.random() * nchar.length()));
        }
        return new String(chars);
    }

    public static String randomAlphanumericUpper(int len) {
        String nchar = NUMBER_CHARS + LETTER.toUpperCase();
        char[] chars = new char[len];
        for (int i = 0; i < len; i++) {
            chars[i] = nchar.charAt((int) (Math.random() * nchar.length()));
        }
        return new String(chars);
    }

    public static String randomIgnoreCaseAlphanumeric(int len) {
        String nchar = NUMBER_CHARS + LETTER_CHARS;
        char[] chars = new char[len];
        for (int i = 0; i < len; i++) {
            chars[i] = nchar.charAt((int) (Math.random() * nchar.length()));
        }
        return new String(chars);
    }

    /**
     * 随机len长度的中文字符串
     *
     * @param len
     * @return
     */
    public static String randomChineseCharacters(int len) {
        char[] chars = new char[len];
        for (int i = 0; i < len; i++) {
            char c = (char) (0x4e00 + (int) (Math.random() * (0x9fa5 - 0x4e00 + 1)));
            chars[i] = c;
        }
        return new String(chars);
    }

    public static String randomChineseCharacters(int start, int end) {
        return randomChineseCharacters((int) randomNumber(start, end));
    }

    public static String uuid() {
        return UUID.randomUUID().toString().replaceAll("-", "");
    }

    public static String randomHexString(int len) {
        if (len > 0) {
            char[] chars = new char[len];
            long firstIndex = randomNumber(0, 14);
            chars[0] = HEX_CHARS.substring(1, HEX_CHARS.length()).toCharArray()[(int) firstIndex];
            if (len > 1) {
                for (int i = 1; i < len; i++) {
                    chars[i] = HEX_CHARS.toCharArray()[(int) randomNumber(0, 15)];
                }
            }
            return new String(chars);
        }
        return "";
    }


    public static String uuidShort() {
        //调用Java提供的生成随机字符串的对象：32位，十六进制，中间包含-
        StringBuffer shortBuffer = new StringBuffer();
        String uuid = UUID.randomUUID().toString().replace("-", "");

        for (int i = 0; i < 8; i++) {                       //分为8组
            String str = uuid.substring(i * 4, i * 4 + 4);  //每组4位
            int x = Integer.parseInt(str, 16);              //将4位str转化为int 16进制下的表示

            //用该16进制数取模62（十六进制表示为314（14即E）），结果作为索引取出字符
            shortBuffer.append(chars[x % 0x3E]);
        }
        return shortBuffer.toString();
    }
}
