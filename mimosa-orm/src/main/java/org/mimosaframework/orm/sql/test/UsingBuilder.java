package org.mimosaframework.orm.sql.test;

public interface UsingBuilder extends UsingAliasNameBuilder {
    WhereBuilder using(Class... table);
}
