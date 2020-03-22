package org.mimosaframework.orm.sql;

public interface FieldFunBuilder<T> {

    T count(FieldItem fieldItem);

    T max(FieldItem fieldItem);

    T avg(FieldItem fieldItem);

    T sum(FieldItem fieldItem);

    T min(FieldItem fieldItem);

    T concat(FieldItem... fieldItems);

    T concat(String fieldAliasName, FieldItem... fieldItems);

    T substring(FieldItem fieldItem, int pos, int len);
}
