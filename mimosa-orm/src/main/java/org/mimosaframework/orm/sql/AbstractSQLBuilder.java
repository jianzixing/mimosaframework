package org.mimosaframework.orm.sql;

public abstract class AbstractSQLBuilder {

    /**
     * 当前SQL体部分，比如SELECT ...  FROM ... WHERE ...
     * SELECT 是第一部分
     * FROM 是第二部分
     * WHERE 是第三部分
     */
    protected byte body = 0;

    /**
     * 自由使用当前语句是什么类型操作
     * 比如 drop table 和 drop database 区分开
     */
    protected int type = 0;

    public AbstractSQLBuilder() {

    }
}
