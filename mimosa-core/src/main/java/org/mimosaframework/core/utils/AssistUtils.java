package org.mimosaframework.core.utils;

public class AssistUtils {
    public static void notNull(Object o, String s) {
        if (o == null) {
            throw new IllegalArgumentException(s != null ? s : "参数不能是空的");
        }
    }

    public static void error(String msg) {
        throw new IllegalArgumentException(msg);
    }
}
