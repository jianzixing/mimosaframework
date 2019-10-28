package org.mimosaframework.core.utils;

public class ClassUtils {
    public static boolean isEqual(Object a, Object b) {
        if (a == b) return true;
        if (a == null && b == null) return true;
        if (a != null && b != null && a.getClass().equals(b.getClass())) return true;
        return false;
    }

    public static final Class<?> getPrimitiveClass(String typeName) {
        if (typeName.equals("byte"))
            return byte.class;
        if (typeName.equals("short"))
            return short.class;
        if (typeName.equals("int"))
            return int.class;
        if (typeName.equals("long"))
            return long.class;
        if (typeName.equals("char"))
            return char.class;
        if (typeName.equals("float"))
            return float.class;
        if (typeName.equals("double"))
            return double.class;
        if (typeName.equals("boolean"))
            return boolean.class;
        if (typeName.equals("void"))
            return void.class;
        return null;
    }
}
