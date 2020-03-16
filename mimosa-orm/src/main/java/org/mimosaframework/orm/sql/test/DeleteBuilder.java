package org.mimosaframework.orm.sql.test;

public interface DeleteBuilder {
    DeleteWhereBuilder from(Class table);

    WhereBuilder from(Class table, String aliasName);

    UsingAliasNameBuilder from(String aliasName);
}
