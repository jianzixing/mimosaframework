package org.mimosaframework.orm.sql.test;

public interface SelectFromBuilder {
    WhereFromBuilder from(Class table);

    WhereFromBuilder from(Class table, String aliasName);

    WhereFromBuilder from(FromItem... tables);
}
