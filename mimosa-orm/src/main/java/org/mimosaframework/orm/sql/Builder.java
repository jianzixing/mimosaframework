package org.mimosaframework.orm.sql;

import java.util.Set;

/**
 * 组装生成SQL语句的，和SQLBuilder不一样这个类
 * 可以生成跨平台的SQL语句
 */
public abstract class Builder {
    public static final SelectBuilder select(Object... froms) {
        return new SelectBuilder(froms);
    }

    public abstract Set<Class> getAllTables();
}
