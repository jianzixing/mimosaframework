package org.mimosaframework.orm.sql.test;

public interface FieldFunBuilder<T> {

    T count(FieldItem fieldItem);

    T max(FieldItem fieldItem);

    T avg(FieldItem fieldItem);

    T sum(FieldItem fieldItem);

    T min(FieldItem fieldItem);

    T concat(FieldItem... fieldItem);

    T substring(FieldItem fieldItem, int pos, int len);
}
