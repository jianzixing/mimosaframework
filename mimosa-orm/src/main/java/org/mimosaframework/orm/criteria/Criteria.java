package org.mimosaframework.orm.criteria;

import org.mimosaframework.core.FieldFunction;
import org.mimosaframework.core.utils.ClassUtils;
import org.mimosaframework.orm.annotation.Column;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;


/**
 * @author yangankang
 */
public final class Criteria {

    public static <T> Query<T> query(Class<T> c) {
        return new DefaultQuery<T>(c);
    }

    public static <T> LogicQuery<T> logicQuery(Class<T> c) {
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

    public static <T> Join inner(Class<T> c) {
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

    public static AsField as(String alias, Object field) {
        field = ClassUtils.value(field);
        return new AsField(alias, field);
    }

    public static Keyword NULL() {
        return Keyword.NULL;
    }

    public static <T> Object[] fields(Class<T> t) {
        Class<?> superClass = t.getSuperclass();
        List<Object> list = new ArrayList<>();
        if (superClass != null) {
            list.addAll(Arrays.asList(fields(superClass)));
        }
        Field[] fields = t.getDeclaredFields();
        for (Field field : fields) {
            if (field.getAnnotation(Column.class) != null) {
                list.add(field.getName());
            }
        }
        return list.toArray();
    }

    public static <T> Object[] nonFieldList(Class<T> t, Object... fields) {
        return nonFieldList(t, Arrays.asList(fields));
    }

    public static <T> Object[] nonFieldList(Class<T> t, List<Object> fields) {
        Object[] list = fields(t);
        List<Object> rs = new ArrayList<>(Arrays.asList(list));
        if (fields != null && !fields.isEmpty()) {
            rs.removeAll(fields.stream().map(ClassUtils::value).collect(Collectors.toList()));
        }
        return rs.toArray();
    }

    public static <T> Object[] nonFields(Class<T> t, FieldFunction<T>... fields) {
        return nonFields(t, Arrays.asList(fields));
    }

    public static <T> Object[] nonFields(Class<T> t, List<FieldFunction<T>> fields) {
        List<Object> list = new ArrayList<>(Arrays.asList(fields(t)));
        List<Object> newFields = new ArrayList<>();
        for (FieldFunction<T> f : fields) {
            newFields.add(ClassUtils.value(f));
        }
        list.removeAll(newFields);
        return list.toArray();
    }
}
