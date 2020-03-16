package org.mimosaframework.orm.sql.test;

public interface UsingAliasNameBuilder {
    WhereBuilder using(FromItem... tables);
}
