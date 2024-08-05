package org.mimosaframework.orm.criteria;

import org.mimosaframework.core.FieldFunction;
import org.mimosaframework.core.utils.ClassUtils;
import org.mimosaframework.orm.annotation.Column;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


/**
 * @author yangankang
 */
public final class Criteria {

    public static Query query(Class<?> c) {
        return new DefaultQuery(c);
    }

    public static LogicQuery logicQuery(Class<?> c) {
        return new DefaultQuery(c);
    }

    public static LogicDelete logicDelete(Class<?> c) {
        return new DefaultDelete(c);
    }

    public static LogicUpdate logicDUpdate(Class<?> c) {
        return new DefaultUpdate(c);
    }

    public static DefaultFilter filter() {
        return new DefaultFilter();
    }

    public static Join join(Class<?> c) {
        return new DefaultJoin(c, 0);
    }

    public static Join left(Class<?> c) {
        return new DefaultJoin(c, 0);
    }

    public static Join inner(Class<?> c) {
        return new DefaultJoin(c, 1);
    }

    public static Update update(Class<?> c) {
        return new DefaultUpdate(c);
    }

    public static Delete delete(Class<?> c) {
        return new DefaultDelete(c);
    }

    public static Function<LogicFunction> fun(Class<?> c) {
        return new DefaultFunction(c);
    }

    public static WrapsLinked linked() {
        return new DefaultWrapsLinked();
    }

    public static LogicWrapsLinked logicLinked() {
        return new DefaultWrapsLinked();
    }

    public static LogicWrapsLinked linked(WrapsLinked l) {
        DefaultWrapsLinked linked = new DefaultWrapsLinked();
        linked.linked(l);
        return linked;
    }

    public static <T> UpdateObject<T> wrapUpdate(T t) {
        return new UpdateObject<>(t);
    }

    public static AsField as(String alias, Object field) {
        field = ClassUtils.value(field);
        return new AsField(alias, field);
    }

    public static Keyword NULL() {
        return Keyword.NULL;
    }

    public static List<String> fields(Class<?> t) {
        Class<?> superClass = t.getSuperclass();
        List<String> list = new ArrayList<>();
        if (superClass != null) {
            list.addAll(fields(superClass));
        }
        Field[] fields = t.getDeclaredFields();
        for (Field field : fields) {
            if (field.getAnnotation(Column.class) != null) {
                list.add(field.getName());
            }
        }
        return list;
    }

    public static List<String> nonFieldList(Class<?> t, String... fields) {
        return nonFieldList(t, Arrays.asList(fields));
    }

    public static List<String> nonFieldList(Class<?> t, List<String> fields) {
        List<String> list = fields(t);
        list.removeAll(fields);
        return list;
    }

    public static <T> List<String> nonFields(Class<T> t, FieldFunction<T>... fields) {
        return nonFields(t, Arrays.asList(fields));
    }

    public static <T> List<String> nonFields(Class<T> t, List<FieldFunction<T>> fields) {
        List<String> list = fields(t);
        List<String> newFields = new ArrayList<>();
        for (FieldFunction<T> f : fields) {
            newFields.add(ClassUtils.value(f));
        }
        list.removeAll(newFields);
        return list;
    }
}
