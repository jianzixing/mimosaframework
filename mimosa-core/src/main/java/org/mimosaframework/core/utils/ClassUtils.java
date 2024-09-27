package org.mimosaframework.core.utils;

import org.mimosaframework.core.FieldFunction;
import org.mimosaframework.core.exception.ModuleException;

import java.io.Serializable;
import java.lang.invoke.SerializedLambda;
import java.lang.reflect.Method;
import java.util.function.Function;

public class ClassUtils {
    public static boolean isEqual(Object a, Object b) {
        if (a == b) return true;
        return a != null && b != null && a.getClass().equals(b.getClass());
    }

    public static Class<?> getPrimitiveClass(String typeName) {
        switch (typeName) {
            case "byte":
                return byte.class;
            case "short":
                return short.class;
            case "int":
                return int.class;
            case "long":
                return long.class;
            case "char":
                return char.class;
            case "float":
                return float.class;
            case "double":
                return double.class;
            case "boolean":
                return boolean.class;
            case "void":
                return void.class;
        }
        return null;
    }

    /**
     * 获取bean的字段名称
     */
    public static <T> String getLambdaFnName(FieldFunction<T> fn) {
        try {
            Method method = fn.getClass().getDeclaredMethod("writeReplace");
            method.setAccessible(true);
            SerializedLambda lambda = (SerializedLambda) method.invoke(fn);
            String methodName = lambda.getImplMethodName();
            if (StringTools.isNotEmpty(methodName) && (methodName.startsWith("get") || methodName.startsWith("set"))) {
                methodName = (methodName.substring(3, 4).toLowerCase()) + (methodName.length() > 4 ? methodName.substring(4) : "");
            }
            return methodName;
        } catch (Exception e) {
            throw new ModuleException(e);
        }
    }

    public static <T> String value(Object obj) {
        if (obj instanceof String) return (String) obj;
        if (obj instanceof Class) return ((Class<?>) obj).getSimpleName();
        if (obj.getClass().isEnum()) return obj.toString();
        if (obj instanceof FieldFunction) {
            String name = getLambdaFnName((FieldFunction<T>) obj);
            if (StringTools.isNotEmpty(name)) return name;
        }
        return obj instanceof Serializable ? obj.toString() : obj.getClass().getSimpleName();
    }

    public static String[] values(Object[] fields) {
        String[] fs = new String[fields.length];
        for (int i = 0; i < fields.length; i++) {
            fs[i] = value(fields[i]);
        }
        return fs;
    }
}
