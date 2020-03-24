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
    protected byte body = 0;
    protected String lastPlaceholderName;

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
