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
}
