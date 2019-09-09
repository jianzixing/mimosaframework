package org.mimosaframework.core.utils;

public class ClassUtils {
    public static boolean isEqual(Object a, Object b) {
        if (a == b) return true;
        if (a == null && b == null) return true;
        if (a != null && b != null && a.getClass().equals(b.getClass())) return true;
        return false;
    }
}
