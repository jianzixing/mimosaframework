package org.mimosaframework.orm.platform.sqlserver;

import org.mimosaframework.core.json.ModelObject;
import org.mimosaframework.core.utils.StringTools;
import org.mimosaframework.orm.criteria.*;
import org.mimosaframework.orm.mapping.MappingField;
import org.mimosaframework.orm.mapping.MappingTable;
import org.mimosaframework.orm.platform.*;

import java.util.*;

public class SQLServerDatabasePorter extends AbstractDatabasePorter {
    private static DifferentColumn differentColumn = new SQLServerDifferentColumn();

    protected void buildTableFieldAuthIncrement(SQLBuilder builder, MappingField field, boolean isAutoIncrement) {
        if (field.isMappingFieldAutoIncrement() && isAutoIncrement) {
            // builder.AUTO_INCREMENT();
            builder.addString("IDENTITY(1,1)");
        }
    }

    protected void setTimeForUpdateField(MappingField field, SQLBuilder builder) {
        //`modified_date` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '修改时间'
        builder.addString("timestamp");
        if (StringTools.isNotEmpty(field.getMappingFieldComment())) {
            builder.COMMENT().addQuotesString(field.getMappingFieldComment());
        }
    }

    @Override
    protected DifferentColumn getDifferentColumn() {
        return differentColumn;
    }

    private SQLBuilder buildSelectLeftJoins(Map<Object, MappingTable> tables,
                                            DefaultQuery query, Map<Object, String> aliasMap,
                                            Map<Object, List<SelectFieldAliasReference>> references) {
        SQLBuilder sqlBuilder = SQLBuilderFactory.createBraceSQLBuilder();
        sqlBuilder.SELECT();
        MappingTable mainTable = tables.get(query);
        String mainTableName = mainTable.getDatabaseTableName();
        String mainTableAlias = aliasMap.get(query);

        Limit limit = query.getLimit();
        boolean hasLimit = limit != null ? true : false;
        List<Join> leftJoins = query.getLeftJoin();
        List<Order> orders = query.getOrders();

        List<Join> rootJoins = null;
        if (leftJoins == null || leftJoins.size() == 0) {
            sqlBuilder.addString("*");
        } else {
            int i = 0;
            rootJoins = new ArrayList<>(leftJoins.size());
            List<SelectFieldAliasReference> rfs = references.get(query);
            if (rfs != null) {
                Iterator<SelectFieldAliasReference> mainIterator = rfs.iterator();
                while (mainIterator.hasNext()) {
                    SelectFieldAliasReference f = mainIterator.next();
                    sqlBuilder.addTableWrapField(mainTableAlias, f.getFieldName()).AS().addWrapString(f.getFieldAliasName());
                    if (mainIterator.hasNext()) {
                        sqlBuilder.addSplit();
                    }
                }
                if (leftJoins != null && leftJoins.size() > 0) {
                    sqlBuilder.addSplit();
                }
            }
            Iterator<Join> leftJoinIterator = leftJoins.iterator();
            while (leftJoinIterator.hasNext()) {
                Join join = leftJoinIterator.next();

                List<SelectFieldAliasReference> rfsj = references.get(join);

                if (rfsj != null) {
                    String joinAliasTable = aliasMap.get(join);
                    Iterator<SelectFieldAliasReference> iterator = rfsj.iterator();
                    while (iterator.hasNext()) {
                        SelectFieldAliasReference f = iterator.next();
                        sqlBuilder.addTableWrapField(joinAliasTable, f.getFieldName()).AS().addWrapString(f.getFieldAliasName());
                        if (iterator.hasNext()) {
                            sqlBuilder.addSplit();
                        }
                    }
                }

                DefaultJoin j = (DefaultJoin) join;
                if (j.getMainTable() == query.getTableClass()) {
                    rootJoins.add(join);
                }
                if (leftJoinIterator.hasNext()) {
                    sqlBuilder.addSplit();
                }
            }
        }
        sqlBuilder.FROM();

        if (!hasLimit) {
            sqlBuilder.addWrapString(mainTableName);
            if (StringTools.isNotEmpty(mainTableAlias)) {
                sqlBuilder.addWrapString(mainTableAlias);
            }
        }

        if (hasLimit) {
            SQLBuilder sqlserverLimitBuilder = SQLBuilderFactory.createBraceSQLBuilder();
            sqlserverLimitBuilder.SELECT().addString("ROW_NUMBER() OVER");
            // SQL Server分页必须排序，如果本身设置了排序就按照设置的排序字段排序
            // 如果没有设置排序就使用主键排序，如果没有主键就按照第一个字段排序
            sqlserverLimitBuilder.addParenthesisStart();
            if (orders != null && orders.size() > 0) {
                SQLBuilder orderByBuilder = this.buildOrderBy(mainTable, query, null);
                sqlserverLimitBuilder.addSQLBuilder(orderByBuilder);
            } else {
                List<MappingField> pkfields = mainTable.getMappingPrimaryKeyFields();
                MappingField field = null;
                if (pkfields != null && pkfields.size() > 0) {
                    field = pkfields.get(0);
                } else {
                    Set<MappingField> fields = mainTable.getMappingFields();
                    for (MappingField f : fields) {
                        field = f;
                        break;
                    }
                }
                sqlserverLimitBuilder.ORDER().BY();
                sqlserverLimitBuilder.addWrapString(field.getMappingColumnName());
                sqlserverLimitBuilder.ASC();
            }
            sqlserverLimitBuilder.addParenthesisEnd();
            sqlserverLimitBuilder.AS().addWrapString("RowNumber");
            sqlserverLimitBuilder.addSplit();
            sqlserverLimitBuilder.addAsterisk();
            sqlserverLimitBuilder.FROM().addWrapString(mainTableName);

            LogicWraps<Filter> logicWraps = query.getLogicWraps();
            if (logicWraps != null) {
                sqlserverLimitBuilder.WHERE();
                SQLBuilder whereBuilder = this.buildWhereByLogicWraps(mainTable, logicWraps, null);
                sqlserverLimitBuilder.addSQLBuilder(whereBuilder);
            }

            sqlBuilder.addParenthesisStart();
            sqlBuilder.addSQLBuilder(sqlserverLimitBuilder);
            sqlBuilder.addParenthesisEnd();
            sqlBuilder.AS().addWrapString(mainTableName);
            sqlBuilder.WHERE().addWrapString("RowNumber").BETWEEN()
                    .addString((limit.getStart() + 1) + "")
                    .AND()
                    .addString((limit.getStart() + limit.getLimit()) + "");
        } else {
            if (rootJoins != null && rootJoins.size() > 0) {
                if (rootJoins != null) {
                    for (Join join : rootJoins) {
                        this.setJoinBuilder(query, join, tables, aliasMap, sqlBuilder, false);
                    }
                }
            }

            LogicWraps<Filter> logicWraps = query.getLogicWraps();
            if (logicWraps != null) {
                sqlBuilder.WHERE();
                if (leftJoins == null) {
                    SQLBuilder whereBuilder = this.buildWhereByLogicWraps(mainTable, logicWraps, null);
                    sqlBuilder.addSQLBuilder(whereBuilder);
                } else {
                    SQLBuilder whereBuilder = this.buildWhereByLogicWraps(mainTable, logicWraps, mainTableAlias);
                    sqlBuilder.addSQLBuilder(whereBuilder);
                }
            }
            if (rootJoins == null || rootJoins.size() == 0) {
                SQLBuilder orderByBuilder = this.buildOrderBy(mainTable, query, null);
                sqlBuilder.addSQLBuilder(orderByBuilder);
            } else {
                SQLBuilder orderByBuilder = this.buildOrderBy(mainTable, query, mainTableAlias);
                sqlBuilder.addSQLBuilder(orderByBuilder);
            }
        }

        return sqlBuilder;
    }

    protected String getSelectCountSymbol() {
        return "1 as count";
    }

    public SQLBuilder buildSingleSelect(DefaultQuery query, MappingTable mappingTable, boolean isSelectPrimaryKey) {
        Limit limit = query.getLimit();
        boolean hasLimit = limit != null ? true : false;
        SQLBuilder sqlBuilder = SQLBuilderFactory.createBraceSQLBuilder();
        sqlBuilder.SELECT();

        if (isSelectPrimaryKey) {
            List<MappingField> fields = mappingTable.getMappingPrimaryKeyFields();
            if (fields != null) {
                Iterator<MappingField> iterator = fields.iterator();
                while (iterator.hasNext()) {
                    MappingField field = iterator.next();
                    sqlBuilder.addWrapString(field.getMappingColumnName());
                    if (iterator.hasNext()) {
                        sqlBuilder.addSplit();
                    }
                }
            } else {
                sqlBuilder.addString("*");
            }
        } else {
            sqlBuilder.addString("*");
        }

        if (!hasLimit) {
            sqlBuilder.FROM().addWrapString(mappingTable.getDatabaseTableName());
            LogicWraps<Filter> logicWraps = query.getLogicWraps();
            if (logicWraps != null) {
                sqlBuilder.WHERE();
                SQLBuilder whereBuilder = this.buildWhereByLogicWraps(mappingTable, logicWraps, null);
                sqlBuilder.addSQLBuilder(whereBuilder);
            }
            SQLBuilder orderByBuilder = this.buildOrderBy(mappingTable, query, null);
            sqlBuilder.addSQLBuilder(orderByBuilder);
        } else {
            String mainTableName = mappingTable.getDatabaseTableName();
            List<Order> orders = query.getOrders();
            SQLBuilder sqlserverLimitBuilder = SQLBuilderFactory.createBraceSQLBuilder();
            sqlserverLimitBuilder.SELECT().addString("ROW_NUMBER() OVER");
            // SQL Server分页必须排序，如果本身设置了排序就按照设置的排序字段排序
            // 如果没有设置排序就使用主键排序，如果没有主键就按照第一个字段排序
            sqlserverLimitBuilder.addParenthesisStart();
            if (orders != null && orders.size() > 0) {
                SQLBuilder orderByBuilder = this.buildOrderBy(mappingTable, query, null);
                sqlserverLimitBuilder.addSQLBuilder(orderByBuilder);
            } else {
                List<MappingField> pkfields = mappingTable.getMappingPrimaryKeyFields();
                MappingField field = null;
                if (pkfields != null && pkfields.size() > 0) {
                    field = pkfields.get(0);
                } else {
                    Set<MappingField> fields = mappingTable.getMappingFields();
                    for (MappingField f : fields) {
                        field = f;
                        break;
                    }
                }
                sqlserverLimitBuilder.ORDER().BY();
                sqlserverLimitBuilder.addWrapString(field.getMappingColumnName());
                sqlserverLimitBuilder.ASC();
            }
            sqlserverLimitBuilder.addParenthesisEnd();
            sqlserverLimitBuilder.AS().addWrapString("RowNumber");
            sqlserverLimitBuilder.addSplit();
            sqlserverLimitBuilder.addAsterisk();
            sqlserverLimitBuilder.FROM().addWrapString(mainTableName);

            LogicWraps<Filter> logicWraps = query.getLogicWraps();
            if (logicWraps != null) {
                sqlserverLimitBuilder.WHERE();
                SQLBuilder whereBuilder = this.buildWhereByLogicWraps(mappingTable, logicWraps, null);
                sqlserverLimitBuilder.addSQLBuilder(whereBuilder);
            }

            sqlBuilder.FROM();
            sqlBuilder.addParenthesisStart();
            sqlBuilder.addSQLBuilder(sqlserverLimitBuilder);
            sqlBuilder.addParenthesisEnd();
            sqlBuilder.AS().addWrapString(mainTableName);
            sqlBuilder.WHERE().addWrapString("RowNumber").BETWEEN()
                    .addString((limit.getStart() + 1) + "")
                    .AND()
                    .addString((limit.getStart() + limit.getLimit()) + "");
        }
        return sqlBuilder;
    }

    @Override
    public PorterStructure[] createTable(MappingTable table) {
        SQLBuilder fieldBuilder = this.createTableFields(table);
        super.createTablePrimaryKeys(fieldBuilder, table);

        SQLBuilder tableBuilder = SQLBuilderFactory.createBraceSQLBuilder();
        tableBuilder.CREATE().TABLE(null).addString(table.getMappingTableName());

        tableBuilder.symbolParenthesis(fieldBuilder);
        String encoding = table.getEncoding();
        if (StringTools.isNotEmpty(encoding)) {
            tableBuilder.CHARACTER().SET().addString(encoding);
        }

        PorterStructure tableStructure = new PorterStructure(ChangerClassify.CREATE_TABLE, tableBuilder);
        return new PorterStructure[]{tableStructure};
    }

    @Override
    public PorterStructure[] insert(MappingTable table, ModelObject object) {
        String tableName = table.getDatabaseTableName();

        // 如果自增主键保存时有值
        // 就必须设置  set IDENTITY_INSERT mimosa.dbo.t_user on 然后关闭
        boolean hasAutoIncrement = false;
        List<MappingField> fields = table.getMappingPrimaryKeyFields();
        if (fields != null && fields.size() > 0) {
            for (MappingField field : fields) {
                if (field.isMappingAutoIncrement()) {
                    String ainame = field.getMappingColumnName();
                    if (object.get(ainame) != null) {
                        hasAutoIncrement = true;
                    }
                }
            }
        }


        SQLBuilder insertBuilder = SQLBuilderFactory.createBraceSQLBuilder();
        if (hasAutoIncrement) {
            insertBuilder.SET().addString("IDENTITY_INSERT").addWrapString(tableName).ON().addEndMark();
        }
        insertBuilder.INSERT().INTO().addString(tableName);
        this.insertAddValue(insertBuilder, table, object);
        insertBuilder.addEndMark();

        if (hasAutoIncrement) {
            insertBuilder.SET().addString("IDENTITY_INSERT").addWrapString(tableName).OFF().addEndMark();
        }

        return new PorterStructure[]{new PorterStructure(ChangerClassify.ADD_OBJECT, insertBuilder)};
    }

    @Override
    public PorterStructure[] inserts(MappingTable table, List<ModelObject> objects) {
        String tableName = table.getDatabaseTableName();

        SQLBuilder insertBuilder = SQLBuilderFactory.createBraceSQLBuilder().INSERT().INTO().addString(tableName);
        List<String> fields = this.clearAutoIncrement(table);
        return insertBuildValues(objects, insertBuilder, fields);
    }

    @Override
    public PorterStructure[] select(Map<Object, MappingTable> tables, DefaultQuery query) {
        List innerJoins = query.getInnerJoin();
        List leftJoins = query.getLeftJoin();

        // 如果有inner join但是在进入查询之前已经被clear掉了所以这里清理一下，防止多余的别名和字段
        tables = this.clearNotExistMappingTable(tables, query);

        // 生成所有别名
        Map<Object, String> aliasMap = new LinkedHashMap<>(tables.size());
        if ((innerJoins != null && innerJoins.size() > 0) || (leftJoins != null && leftJoins.size() > 0)) {
            int startAliasNumber = 0;
            Iterator<Map.Entry<Object, MappingTable>> iterator = tables.entrySet().iterator();
            while (iterator.hasNext()) {
                Map.Entry<Object, MappingTable> next = iterator.next();
                aliasMap.put(next.getKey(), "t" + (++startAliasNumber));
            }
        }

        // 生成字段别名
        Map<Object, List<SelectFieldAliasReference>> references = null;
        if (leftJoins != null && leftJoins.size() > 0) {
            references = this.getFieldAliasReference(tables, aliasMap, query.getFields());
        }
        SQLBuilder builder = this.buildSelectLeftJoins(tables, query, aliasMap, references);
        return new PorterStructure[]{new PorterStructure(ChangerClassify.SELECT, builder, references)};
    }

    @Override
    public PorterStructure[] selectPrimaryKey(Map<Object, MappingTable> tables, DefaultQuery query) {
        MappingTable table = tables.get(query);
        SQLBuilder sqlBuilder = this.buildSingleSelect(query, table, true);
        return new PorterStructure[]{new PorterStructure(ChangerClassify.SELECT_PRIMARY_KEY, sqlBuilder)};
    }

    @Override
    protected void countTableAsBuilder(SQLBuilder countBuilder) {
        countBuilder.addWrapString("tb");
    }

    @Override
    protected SQLBuilder createSQLBuilder() {
        return SQLBuilderFactory.createBraceSQLBuilder();
    }
}
