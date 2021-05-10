package org.mimosaframework.core.utils;

public class NumberUtils {
    public static boolean isNumber(Object obj) {
        if (obj instanceof Integer
                || obj instanceof Long
                || obj instanceof Short
                || obj instanceof Double
                || obj instanceof Byte
                || obj instanceof Float) {
            return true;
        }
        return false;
    }

    public static boolean isNumberOutType(Object obj) {
        if (isNumber(obj)) {
            return true;
        }
        if (obj instanceof String) {
            if (StringTools.isNumber((String) obj)) {
                return true;
            }
        }
        return false;
    }

    public static NumberType getNumberType(String str) {
        if (str == null || str.equals("")) {
            return null;
        }
        byte b = 0;
        for (int i = str.length(); --i >= 0; ) {
            if (str.charAt(i) == '.') {
                b++;
                if (b > 1) {
                    return null;
                }
                continue;
            }
            if (!Character.isDigit(str.charAt(i))) {
                return null;
            }
        }
        return b == 1 ? NumberType.FLOAT : NumberType.INTEGER;
    }

    public static Integer max(int... ints) {
        Integer i = null;
        if (ints != null) {
            for (int j : ints) {
                if (i == null || i < j) {
                    i = j;
                }
            }
        }
        return i;
    }

    public static int maxIndex(int... ints) {
        int i = 0;
        int idx = 0;
        if (ints != null) {
            int k = 0;
            for (int j : ints) {
                if (i < j) {
                    i = j;
                    idx = k;
                }
                k++;
            }
        }
        return idx;
    }

    public static int minIndex(int... ints) {
        int i = Integer.MAX_VALUE;
        int idx = 0;
        if (ints != null) {
            int k = 0;
            for (int j : ints) {
                if (i > j) {
                    i = j;
                    idx = k;
                }
                k++;
            }
        }
        return idx;
    }

    public static byte[] intToBytes(int value) {
        byte[] src = new byte[4];
        src[3] = (byte) ((value >> 24) & 0xFF);
        src[2] = (byte) ((value >> 16) & 0xFF);
        src[1] = (byte) ((value >> 8) & 0xFF);
        src[0] = (byte) (value & 0xFF);
        return src;
    }

    public static int bytesToInt(byte[] src) {
        int value;
        value = (int) ((src[0] & 0xFF)
                | ((src[1] & 0xFF) << 8)
                | ((src[2] & 0xFF) << 16)
                | ((src[3] & 0xFF) << 24));
        return value;
    }

    enum NumberType {
        /**
         * 表示数字是整数
         * 对应的用Long类型即可
         */
        INTEGER,
        /**
         * 表示数字是浮点数
         * 对应的用double类型
         */
        FLOAT
    }
}
