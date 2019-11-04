package org.mimosaframework.orm.sql;

import org.mimosaframework.orm.SQLAutonomously;

import java.util.Set;

/**
 * 组装生成SQL语句的，和SQLBuilder不一样这个类
 * 可以生成跨平台的SQL语句
 */
public abstract class Builder {
    public static final SelectBuilder select(FromBuilder... froms) {
        return new SelectBuilder(froms);
    }

    public abstract Set<Class> getAllTables();

    public abstract SQLAutonomously autonomously();
}
