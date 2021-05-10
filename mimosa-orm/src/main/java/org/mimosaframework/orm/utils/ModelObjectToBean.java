package org.mimosaframework.orm.utils;

import org.mimosaframework.core.json.ModelObject;
import org.mimosaframework.core.utils.StringTools;
import org.mimosaframework.orm.annotation.Column;
import org.mimosaframework.orm.i18n.I18n;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.math.BigDecimal;

public class ModelObjectToBean implements Model2BeanFactory {
    public <T> T toJavaObject(ModelObject object, Class<T> tClass) {
        try {
            T t = tClass.newInstance();
            toJavaObject(object, t);
            return t;
        } catch (Exception e) {
            if (e instanceof RuntimeException) {
                throw (RuntimeException) e;
            } else {
                throw new RuntimeException(I18n.print("model_to_bean_error_2"), e);
            }
        }
    }

    public <T> void toJavaObject(ModelObject object, T obj) {
        if (object != null) {
            String fieldName = null;
            Object value = null;
            try {
                Class c = obj.getClass();
                Field[] fields = c.getDeclaredFields();
                for (Field field : fields) {
                    Column column = field.getAnnotation(Column.class);
                    String keyName = null;
                    if (column != null && StringTools.isNotEmpty(column.name())) {
                        keyName = column.name();
                    }

                    if (StringTools.isEmpty(keyName)) {
                        keyName = field.getName();
                    }

                    Object param = object.get(keyName);
                    Object r = type2type(param, field.getType());
                    value = r;

                    fieldName = field.getName();
                    String setName = fieldName.substring(0, 1).toUpperCase() + fieldName.substring(1);
                    Method method = c.getMethod("set" + setName, field.getType());

                    if (method != null) {
                        method.invoke(obj, r);
                    } else {
                        if (fieldName.startsWith("is")) {
                            setName = fieldName.substring(2);
                            method = c.getMethod("set" + setName, field.getType());
                            if (method != null) {
                                method.invoke(obj, r);
                            }
                        }
                    }

                    if (method == null) {
                        field.setAccessible(true);
                        field.set(obj, r);
                    }
                }
            } catch (Exception e) {
                throw new RuntimeException(I18n.print("model_to_bean_error",
                        fieldName, "" + value), e);
            }
        }
    }

    protected static Object type2type(Object source, Class targetType) {
        if (source != null) {
            Class sourceType = source.getClass();
            if (sourceType == targetType) {
                return source;
            }
            if (Number.class.isAssignableFrom(sourceType)) {
                if (targetType.equals(int.class) || targetType.equals(Integer.class))
                    return ((Number) source).intValue();
                if (targetType.equals(short.class) || targetType.equals(Short.class))
                    return ((Number) source).shortValue();
                if (targetType.equals(byte.class) || targetType.equals(Byte.class))
                    return ((Number) source).byteValue();
                if (targetType.equals(long.class) || targetType.equals(Long.class))
                    return ((Number) source).longValue();
                if (targetType.equals(float.class) || targetType.equals(Float.class))
                    return ((Number) source).floatValue();
                if (targetType.equals(double.class) || targetType.equals(Double.class))
                    return ((Number) source).doubleValue();
                if (targetType.equals(String.class)) return "" + source;
                if (targetType.equals(BigDecimal.class)) return new BigDecimal("" + source);
                if (targetType.equals(boolean.class) || targetType.equals(Boolean.class)) {
                    if (((Number) source).intValue() == 0) return false;
                    else return true;
                }
            }
            if (sourceType.equals(String.class)) {
                if (targetType.equals(int.class) || targetType.equals(Integer.class))
                    return Integer.parseInt((String) source);
                if (targetType.equals(short.class) || targetType.equals(Short.class))
                    return Short.parseShort((String) source);
                if (targetType.equals(byte.class) || targetType.equals(Byte.class))
                    return Byte.parseByte((String) source);
                if (targetType.equals(long.class) || targetType.equals(Long.class))
                    return Long.parseLong((String) source);
                if (targetType.equals(float.class) || targetType.equals(Float.class))
                    return Float.parseFloat((String) source);
                if (targetType.equals(double.class) || targetType.equals(Double.class))
                    return Double.parseDouble((String) source);
                if (targetType.equals(String.class)) return "" + source;
                if (targetType.equals(BigDecimal.class)) return new BigDecimal((String) source);
                if (targetType.equals(boolean.class) || targetType.equals(Boolean.class)) {
                    if (Integer.parseInt((String) source) == 0) return false;
                    else return true;
                }
            }
        }
        if (targetType.isPrimitive()) {
            return 0;
        }
        return source;
    }
}
