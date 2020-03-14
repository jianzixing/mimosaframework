package org.mimosaframework.orm.sql.test;

public interface SelectFieldBuilder {

    SelectFromBuilder all();

    SelectFromBuilder fields(Object... fields);

    SelectFromBuilder fields(FieldItem... fields);

    // 各种函数
}
