package org.mimosaframework.orm.sql;

import org.mimosaframework.orm.mapping.MappingGlobalWrapper;
import org.mimosaframework.orm.mapping.MappingTable;
import org.mimosaframework.orm.platform.SQLBuilder;
import org.mimosaframework.orm.platform.SQLBuilderCombine;

public abstract class AbstractSQLBuilder
        implements
        SQLMappingChannel {

    protected MappingGlobalWrapper mappingGlobalWrapper;
    protected SQLBuilder sqlBuilder;

    /**
     * 当前SQL体部分，比如SELECT ...  FROM ... WHERE ...
     * SELECT 是第一部分
     * FROM 是第二部分
     * WHERE 是第三部分
     */
    protected byte body = 0;

    /**
     * 判断当前SQL操作是否是映射类操作
     * 如果是映射类操作，则字段使用映射类字段
     */
    protected boolean isMappingTable = false;

    protected String lastPlaceholderName;

    /**
     * 自由使用当前语句是什么类型操作
     * 比如 drop table 和 drop database 区分开
     */
    protected int type = 0;

    public AbstractSQLBuilder() {
        this.sqlBuilder = this.createSQLBuilder();
    }

    protected abstract SQLBuilder createSQLBuilder();

    public SQLBuilderCombine getPlanSql() {
        return this.sqlBuilder.toSQLString(mappingGlobalWrapper);
    }

    public void setMappingGlobalWrapper(MappingGlobalWrapper mappingGlobalWrapper) {
        this.mappingGlobalWrapper = mappingGlobalWrapper;
    }

    @Override
    public MappingTable getMappingTableByClass(Class table) {
        if (this.mappingGlobalWrapper != null) {
            MappingTable mappingTable = this.mappingGlobalWrapper.getMappingTable(table);
            return mappingTable;
        }
        return null;
    }
}
