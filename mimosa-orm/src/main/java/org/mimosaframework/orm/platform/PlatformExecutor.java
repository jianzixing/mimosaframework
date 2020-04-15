package org.mimosaframework.orm.platform;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.mimosaframework.core.json.ModelObject;
import org.mimosaframework.core.utils.StringTools;
import org.mimosaframework.orm.BasicFunction;
import org.mimosaframework.orm.ModelObjectConvertKey;
import org.mimosaframework.orm.criteria.*;
import org.mimosaframework.orm.i18n.I18n;
import org.mimosaframework.orm.mapping.MappingField;
import org.mimosaframework.orm.mapping.MappingGlobalWrapper;
import org.mimosaframework.orm.mapping.MappingIndex;
import org.mimosaframework.orm.mapping.MappingTable;
import org.mimosaframework.orm.merge.DefaultObjectMerge;
import org.mimosaframework.orm.merge.MergeTree;
import org.mimosaframework.orm.merge.ObjectMerge;
import org.mimosaframework.orm.sql.*;
import org.mimosaframework.orm.sql.delete.DefaultSQLDeleteBuilder;
import org.mimosaframework.orm.sql.insert.DefaultSQLInsertBuilder;
import org.mimosaframework.orm.sql.select.DefaultSQLSelectBuilder;
import org.mimosaframework.orm.sql.stamp.*;
import org.mimosaframework.orm.sql.update.DefaultSQLUpdateBuilder;

import java.io.Serializable;
import java.sql.SQLException;
import java.util.*;

/**
 * 执行数据库数据结构校验
 * 以及CURD操作
 */
public class PlatformExecutor {
    private static final Log logger = LogFactory.getLog(PlatformExecutor.class);
    private DBRunner runner = null;
    private MappingGlobalWrapper mappingGlobalWrapper;
    private DataSourceWrapper dswrapper;

    public PlatformExecutor(MappingGlobalWrapper mappingGlobalWrapper, DataSourceWrapper dswrapper) {
        this.mappingGlobalWrapper = mappingGlobalWrapper;
        this.dswrapper = dswrapper;
        this.runner = new DefaultDBRunner(dswrapper);
    }

    public void compareTableStructure(PlatformCompare compare) throws SQLException {
        PlatformDialect dialect = this.getDialect();
        List<TableStructure> structures = dialect.getTableStructures();
        List<MappingTable> mappingTables = mappingGlobalWrapper.getMappingTables();

        if (structures != null) {
            List<MappingTable> rmTab = new ArrayList<>();
            for (TableStructure structure : structures) {
                List<TableColumnStructure> columnStructures = new ArrayList<>(structure.getColumnStructures());

                MappingTable currTable = null;
                for (MappingTable mappingTable : mappingTables) {
                    String mappingTableName = mappingTable.getMappingTableName();
                    String tableName = structure.getTableName();
                    if (tableName.equalsIgnoreCase(mappingTableName)) {
                        rmTab.add(mappingTable);
                        currTable = mappingTable;
                        break;
                    }
                }

                if (currTable != null) {
                    List<MappingField> rmCol = new ArrayList<>();
                    List<TableColumnStructure> rmSCol = new ArrayList<>();
                    Set<MappingField> mappingFields = currTable.getMappingFields();
                    if (columnStructures != null && columnStructures.size() > 0) {
                        Map<MappingField, List<ColumnEditType>> updateFields = new LinkedHashMap();
                        Map<MappingField, TableColumnStructure> updateColumnsStructures = new LinkedHashMap();
                        for (TableColumnStructure columnStructure : columnStructures) {
                            if (mappingFields != null) {
                                MappingField currField = null;
                                for (MappingField field : mappingFields) {
                                    String mappingFieldName = field.getMappingColumnName();
                                    String fieldName = columnStructure.getColumnName();
                                    if (mappingFieldName.equalsIgnoreCase(fieldName)) {
                                        currField = field;
                                        rmCol.add(field);
                                        rmSCol.add(columnStructure);
                                        break;
                                    }
                                }

                                if (currField != null) {
                                    List<ColumnEditType> columnEditTypes = dialect.compareColumnChange(
                                            structure, currField, columnStructure
                                    );

                                    if (columnEditTypes.size() > 0) {
                                        // 需要修改字段
                                        if (currField.isMappingAutoIncrement() == false && columnStructure.isAutoIncrement()) {
                                            // 如果是从自增列变为非自增列，则需要先处理为非自增列，所有修改一下顺序
                                            Map<MappingField, List<ColumnEditType>> updateFieldsCopy = new LinkedHashMap<>();
                                            Map<MappingField, TableColumnStructure> updateColumnsStructuresCopy = new LinkedHashMap<>();
                                            updateFieldsCopy.put(currField, columnEditTypes);
                                            updateColumnsStructuresCopy.put(currField, columnStructure);
                                            updateFieldsCopy.putAll(updateFields);
                                            updateColumnsStructuresCopy.putAll(updateColumnsStructures);
                                            updateFields = updateFieldsCopy;
                                            updateColumnsStructures = updateColumnsStructuresCopy;
                                        } else {
                                            updateFields.put(currField, columnEditTypes);
                                            updateColumnsStructures.put(currField, columnStructure);
                                        }
                                    }
                                }
                            }
                        }

                        if (updateFields != null && updateFields.size() > 0) {
                            compare.fieldUpdate(currTable, structure, updateFields, updateColumnsStructures);
                        }

                        mappingFields.removeAll(rmCol);
                        columnStructures.removeAll(rmSCol);
                        if (mappingFields.size() > 0) {
                            // 有新添加的字段需要添加
                            compare.fieldAdd(currTable, structure, new ArrayList<MappingField>(mappingFields));
                        }
                        if (columnStructures.size() > 0) {
                            // 有多余的字段需要删除
                            compare.fieldDel(currTable, structure, columnStructures);
                        }
                    } else {
                        // 数据库的字段没有需要重新添加全部字段
                        compare.fieldAdd(currTable, structure, new ArrayList<MappingField>(mappingFields));
                    }
                }

                if (currTable != null) {
                    Set<MappingIndex> mappingIndexes = currTable.getMappingIndexes();
                    if (mappingIndexes != null) {
                        List<MappingIndex> newIndexes = new ArrayList<>();
                        List<MappingIndex> updateIndexes = new ArrayList<>();
                        List<String> updateIndexNames = new ArrayList<>();
                        for (MappingIndex index : mappingIndexes) {
                            String mappingIndexName = index.getIndexName();
                            List<TableIndexStructure> indexStructures = structure.getIndexStructures(mappingIndexName);

                            if (indexStructures != null && indexStructures.size() > 0) {
                                List<MappingField> indexMappingFields = index.getIndexColumns();
                                if (!indexStructures.get(0).getType().equalsIgnoreCase(index.getIndexType().toString())) {
                                    // 索引类型不一致需要重建索引
                                    updateIndexes.add(index);
                                    updateIndexNames.add(mappingIndexName);
                                } else {
                                    List<MappingField> rmIdxCol = new ArrayList<>();
                                    for (TableIndexStructure indexStructure : indexStructures) {
                                        String indexColumnName = indexStructure.getColumnName();
                                        for (MappingField indexMappingField : indexMappingFields) {
                                            if (indexMappingField.getMappingColumnName().equalsIgnoreCase(indexColumnName)) {
                                                rmIdxCol.add(indexMappingField);
                                            }
                                        }
                                    }
                                    indexMappingFields.removeAll(rmIdxCol);
                                    if (indexMappingFields.size() != 0) {
                                        // 需要重建索引
                                        updateIndexes.add(index);
                                        updateIndexNames.add(mappingIndexName);
                                    }
                                }
                            } else {
                                // 需要新建索引
                                newIndexes.add(index);
                            }
                        }
                        if (updateIndexes != null && updateIndexes.size() > 0) {
                            compare.indexUpdate(currTable, updateIndexes, updateIndexNames);
                        }
                        if (newIndexes != null && newIndexes.size() > 0) {
                            compare.indexAdd(currTable, newIndexes);
                        }
                    }
                }

                doDialectEnding(currTable, structure);
            }
            mappingTables.removeAll(rmTab);
            if (mappingTables.size() != 0) {
                // 映射表没有添加到数据库
                // 需要新建数据库表

                for (MappingTable mappingTable : mappingTables) {
                    compare.tableCreate(mappingTable);
                    Set<MappingIndex> mappingIndex = mappingTable.getMappingIndexes();
                    if (mappingIndex != null && mappingIndex.size() > 0) {
                        compare.indexAdd(mappingTable, new ArrayList<MappingIndex>(mappingIndex));
                    }

                    doDialectEnding(mappingTable, null);
                }
            }
        }
    }

    private PlatformDialect getDialect() {
        PlatformDialect dialect = PlatformFactory.getDialect(dswrapper);
        dialect.setMappingGlobalWrapper(this.mappingGlobalWrapper);
        return dialect;
    }

    public void createTable(MappingTable mappingTable) throws SQLException {
        PlatformDialect dialect = this.getDialect();
        dialect.define(new DataDefinition(DataDefinitionType.CREATE_TABLE, mappingTable));
    }

    public void dropTable(TableStructure tableStructure) throws SQLException {
        PlatformDialect dialect = this.getDialect();
        dialect.define(new DataDefinition(DataDefinitionType.DROP_TABLE, tableStructure));
    }

    public void createField(MappingTable mappingTable,
                            TableStructure tableStructure,
                            MappingField mappingField) throws SQLException {
        PlatformDialect dialect = this.getDialect();
        dialect.define(new DataDefinition(DataDefinitionType.ADD_COLUMN, mappingTable, tableStructure, mappingField));
    }

    public void modifyField(MappingTable mappingTable,
                            TableStructure tableStructure,
                            MappingField mappingField,
                            TableColumnStructure columnStructure) throws SQLException {
        PlatformDialect dialect = this.getDialect();
        dialect.define(new DataDefinition(DataDefinitionType.MODIFY_COLUMN, tableStructure, mappingTable, mappingField, columnStructure));
    }

    public void dropField(MappingTable mappingTable, TableStructure tableStructure,
                          TableColumnStructure columnStructure) throws SQLException {
        PlatformDialect dialect = this.getDialect();
        dialect.define(new DataDefinition(DataDefinitionType.DROP_COLUMN, mappingTable, tableStructure, columnStructure));
    }

    public void createIndex(MappingTable mappingTable, MappingIndex mappingIndex) throws SQLException {
        PlatformDialect dialect = this.getDialect();
        dialect.define(new DataDefinition(DataDefinitionType.ADD_INDEX, mappingTable, mappingIndex));
    }

    public void dropIndex(MappingTable mappingTable, String indexName) throws SQLException {
        PlatformDialect dialect = this.getDialect();
        dialect.define(new DataDefinition(DataDefinitionType.DROP_INDEX, mappingTable, indexName));
    }

    public void doDialectEnding(MappingTable mappingTable, TableStructure structure) throws SQLException {
        PlatformDialect dialect = this.getDialect();
        dialect.ending(mappingTable, structure);
    }

    public List<Long> inserts(MappingTable table, List<ModelObject> objects) throws SQLException {
        PlatformDialect dialect = this.getDialect();
        if (objects != null && objects.size() > 0) {
            Set<MappingField> fields = table.getMappingFields();
            DefaultSQLInsertBuilder insertBuilder = new DefaultSQLInsertBuilder();
            insertBuilder.insert().into().table(table.getMappingTableName());
            String[] columns = null;
            Set<Object> keys = null;
            int i = 0;
            if (objects.size() == 1) {
                ModelObject object = objects.get(0);
                keys = object.keySet();
                columns = new String[keys.size()];
                for (Object key : keys) {
                    MappingField mappingField = table.getMappingFieldByJavaName(String.valueOf(key));
                    if (mappingField == null) {
                        throw new IllegalArgumentException(I18n
                                .print("miss_executor_mapping_field", String.valueOf(key)));
                    }
                    columns[i] = mappingField.getMappingColumnName();
                    i++;
                }
            } else {
                keys = new LinkedHashSet<>();
                columns = new String[fields.size()];
                i = 0;
                for (MappingField mappingField : fields) {
                    String javaName = mappingField.getMappingFieldName();
                    String columnName = mappingField.getMappingColumnName();
                    keys.add(javaName);
                    columns[i] = columnName;
                    i++;
                }
            }
            insertBuilder.columns(columns).values();
            for (ModelObject object : objects) {
                Object[] values = new Object[keys.size()];
                i = 0;
                for (Object key : keys) {
                    values[i] = object.get(key);
                    i++;
                }
                insertBuilder.row(values);
            }

            SQLBuilderCombine combine = dialect.insert(insertBuilder.compile());
            Object object = this.runner.doHandler(new JDBCTraversing(TypeForRunner.INSERT,
                    combine.getSql(), combine.getPlaceholders()));
            return (List<Long>) object;
        }
        return null;
    }

    public Integer update(MappingTable table, DefaultUpdate update) throws SQLException {
        PlatformDialect dialect = this.getDialect();
        Wraps<Filter> wraps = update.getLogicWraps();
        Map<Object, Object> sets = update.getValues();
        DefaultSQLUpdateBuilder updateBuilder = new DefaultSQLUpdateBuilder();
        updateBuilder.update().table(table.getMappingTableName());
        Iterator<Map.Entry<Object, Object>> iterator = sets.entrySet().iterator();
        while (iterator.hasNext()) {
            Map.Entry<Object, Object> entry = iterator.next();
            Object key = entry.getKey();
            MappingField field = table.getMappingFieldByJavaName(String.valueOf(key));
            if (field != null) {
                updateBuilder.set(field.getMappingColumnName(), entry.getValue());
            }
        }

        if (wraps != null) updateBuilder.where();
        this.buildWraps(updateBuilder, table, wraps, false);
        SQLBuilderCombine combine = dialect.update(updateBuilder.compile());
        return (Integer) this.runner.doHandler(new JDBCTraversing(TypeForRunner.UPDATE,
                combine.getSql(), combine.getPlaceholders()));
    }

    public Integer delete(MappingTable table, DefaultDelete delete) throws SQLException {
        PlatformDialect dialect = this.getDialect();
        Wraps<Filter> wraps = delete.getLogicWraps();
        DefaultSQLDeleteBuilder deleteBuilder = new DefaultSQLDeleteBuilder();
        deleteBuilder.delete().from().table(table.getMappingTableName());

        if (wraps != null) deleteBuilder.where();
        this.buildWraps(deleteBuilder, table, wraps, false);
        SQLBuilderCombine combine = dialect.delete(deleteBuilder.compile());
        return (Integer) this.runner.doHandler(new JDBCTraversing(TypeForRunner.DELETE,
                combine.getSql(), combine.getPlaceholders()));
    }

    public List<ModelObject> select(DefaultQuery query, ModelObjectConvertKey convert) throws SQLException {
        PlatformDialect dialect = this.getDialect();
        Wraps<Filter> logicWraps = query.getLogicWraps();
        List<Join> joins = query.getJoins();
        List<Order> orders = query.getOrders();
        Map<Class, List<String>> fields = query.getFields();
        Map<Class, List<String>> excludes = query.getExcludes();

        Limit limit = query.getLimit();
        Class<?> tableClass = query.getTableClass();
        boolean isMaster = query.isMaster();
        String slaveName = query.getSlaveName();

        dswrapper.setMaster(isMaster);
        dswrapper.setSlaveName(slaveName);

        Map<Join, String> alias = null;
        Map<Object, List<SelectFieldAliasReference>> fieldAlias = null;
        int i = 1, j = 1;
        boolean hasJoin = false;
        if (joins != null && joins.size() > 0) {
            hasJoin = true;
            MappingTable mappingTable = this.mappingGlobalWrapper.getMappingTable(tableClass);
            Set<MappingField> mappingFields = mappingTable.getMappingFields();
            if (fieldAlias == null) fieldAlias = new HashMap<>();
            List<SelectFieldAliasReference> fieldAliasList = new ArrayList<>();
            for (MappingField field : mappingFields) {
                SelectFieldAliasReference reference = new SelectFieldAliasReference();
                reference.setFieldName(field.getMappingColumnName());
                reference.setFieldAliasName("F" + j);
                reference.setJavaFieldName(field.getMappingFieldName());
                reference.setTableAliasName("T");
                reference.setTableClass(tableClass);
                reference.setMainClass(tableClass);
                reference.setPrimaryKey(field.isMappingFieldPrimaryKey());
                fieldAliasList.add(reference);
            }
            fieldAlias.put(query, fieldAliasList);

            for (Join join : joins) {
                DefaultJoin defaultJoin = (DefaultJoin) join;
                Class table = defaultJoin.getTable();
                mappingTable = this.mappingGlobalWrapper.getMappingTable(table);
                if (alias == null) alias = new HashMap<>();
                if (fieldAlias == null) fieldAlias = new HashMap<>();
                String joinAliasName = "T" + i;
                alias.put(join, joinAliasName);

                mappingFields = mappingTable.getMappingFields();
                fieldAliasList = new ArrayList<>();
                for (MappingField field : mappingFields) {
                    SelectFieldAliasReference reference = new SelectFieldAliasReference();
                    reference.setFieldName(field.getMappingColumnName());
                    reference.setFieldAliasName("F" + j);
                    reference.setJavaFieldName(field.getMappingFieldName());
                    reference.setTableAliasName(joinAliasName);
                    reference.setTableClass(table);
                    reference.setMainClass(defaultJoin.getMainClass());
                    reference.setPrimaryKey(field.isMappingFieldPrimaryKey());
                    fieldAliasList.add(reference);
                }
                fieldAlias.put(join, fieldAliasList);
                j++;
                i++;
            }
        }

        DefaultSQLSelectBuilder select = new DefaultSQLSelectBuilder();
        select.select();

        if (limit != null && joins != null && joins.size() > 0) {
            select.fields("T", "*");
        } else {
            if (hasJoin) {
                this.buildSelectField(select, alias, fieldAlias);
            } else {
                select.all();
            }
        }
        MappingTable mappingTable = this.mappingGlobalWrapper.getMappingTable(tableClass);
        select.from().table(mappingTable.getMappingTableName(), "T");
        this.buildJoinQuery(select, alias, joins, false);
        if (logicWraps != null) select.where();
        this.buildWraps(select, mappingTable, logicWraps, hasJoin);

        this.buildOrderBy(select, orders, mappingTable, (limit != null && joins != null && joins.size() > 0));

        if (limit != null) {
            select.limit(limit.getStart(), limit.getLimit());
        }

        if (limit != null && joins != null && joins.size() > 0) {
            DefaultSQLSelectBuilder selectWrap = new DefaultSQLSelectBuilder();
            selectWrap.select();
            this.buildSelectField(selectWrap, alias, fieldAlias);
            selectWrap.from().table(select, "T");
            this.buildJoinQuery(select, alias, joins, false);
            this.buildOrderBy(select, orders, mappingTable, (limit != null && joins != null && joins.size() > 0));

            select = selectWrap;
        }

        SQLBuilderCombine combine = dialect.select(select.compile());
        Object result = this.runner.doHandler(new JDBCTraversing(TypeForRunner.SELECT,
                combine.getSql(), combine.getPlaceholders()));

        return this.buildMergeObjects(fieldAlias, query, convert, (List<ModelObject>) result);
    }

    public long count(DefaultQuery query) throws SQLException {
        PlatformDialect dialect = this.getDialect();
        Wraps<Filter> logicWraps = query.getLogicWraps();
        List<Join> joins = query.getJoins();

        Class<?> tableClass = query.getTableClass();
        boolean isMaster = query.isMaster();
        String slaveName = query.getSlaveName();

        dswrapper.setMaster(isMaster);
        dswrapper.setSlaveName(slaveName);

        Map<Join, String> alias = null;
        int i = 1;
        boolean hasJoins = false;
        if (joins != null && joins.size() > 0) {
            hasJoins = true;
            for (Join join : joins) {
                if (alias == null) alias = new HashMap<>();
                String joinAliasName = "T" + i;
                alias.put(join, joinAliasName);
                i++;
            }
        }

        DefaultSQLSelectBuilder select = new DefaultSQLSelectBuilder();
        select.select();
        MappingTable mappingTable = mappingGlobalWrapper.getMappingTable(tableClass);
        List<MappingField> pks = mappingTable.getMappingPrimaryKeyFields();
        Serializable[] params = new Serializable[pks.size()];
        for (int k = 0; k < pks.size(); k++) {
            if (hasJoins) {
                params[k] = new FieldItem("T", pks.get(k).getMappingColumnName());
            } else {
                params[k] = new FieldItem(pks.get(k).getMappingColumnName());
            }
        }
        select.count(params).as("count");

        if (hasJoins) {
            select.from().table(mappingTable.getMappingTableName(), "T");
        } else {
            select.from().table(mappingTable.getMappingTableName());
        }
        this.buildJoinQuery(select, alias, joins, true);
        if (logicWraps != null) select.where();
        this.buildWraps(select, mappingTable, logicWraps, hasJoins);

        SQLBuilderCombine combine = dialect.select(select.compile());
        Object result = this.runner.doHandler(new JDBCTraversing(TypeForRunner.SELECT,
                combine.getSql(), combine.getPlaceholders()));
        List<ModelObject> objects = (List<ModelObject>) result;
        if (objects != null && objects.size() > 0) {
            return objects.get(0).getIntValue("count");
        } else {
            return 0;
        }
    }

    public List<ModelObject> function(DefaultFunction f) throws SQLException {
        PlatformDialect dialect = this.getDialect();
        List<FunctionField> funs = f.getFuns();
        Class tableClass = f.getTableClass();
        Wraps<Filter> logicWraps = f.getLogicWraps();
        List groups = f.getGroupBy();
        List<Order> orders = f.getOrderBy();
        List<HavingField> havingFields = f.getHavingFields();

        boolean isMaster = f.isMaster();
        String slaveName = f.getSlaveName();
        dswrapper.setMaster(isMaster);
        dswrapper.setSlaveName(slaveName);

        MappingTable mappingTable = mappingGlobalWrapper.getMappingTable(tableClass);

        DefaultSQLSelectBuilder select = new DefaultSQLSelectBuilder();
        select.select();
        for (FunctionField fun : funs) {
            Object field = fun.getField();
            BasicFunction function = fun.getFunction();
            String alias = fun.getAlias();
            boolean distinct = fun.isDistinct();
            // 处理精度
            int scale = fun.getScale();
            String avgCountName = fun.getAvgCountName();

            MappingField mappingField = mappingTable.getMappingFieldByJavaName(String.valueOf(field));
            String columnName = mappingField.getMappingColumnName();

            if (function == BasicFunction.AVG) {
                if (distinct) {
                    select.avg("DISTINCT", columnName);
                } else {
                    select.avg(columnName);
                }
            }
            if (function == BasicFunction.COUNT) {
                if (distinct) {
                    select.count("DISTINCT", columnName);
                } else {
                    select.count(columnName);
                }
            }
            if (function == BasicFunction.MAX) {
                if (distinct) {
                    select.max("DISTINCT", columnName);
                } else {
                    select.max(columnName);
                }
            }
            if (function == BasicFunction.MIN) {
                if (distinct) {
                    select.min("DISTINCT", columnName);
                } else {
                    select.min(columnName);
                }
            }
            if (function == BasicFunction.SUM) {
                if (distinct) {
                    select.sum("DISTINCT", columnName);
                } else {
                    select.sum(columnName);
                }
            }
            if (StringTools.isNotEmpty(alias)) {
                select.as(alias);
            }
        }
        select.from().table(mappingTable.getMappingTableName());
        if (logicWraps != null) select.where();
        this.buildWraps(select, mappingTable, logicWraps, false);
        if (groups != null && groups.size() > 0) {
            select.groupBy();
            for (Object field : groups) {
                MappingField mappingField = mappingTable.getMappingFieldByJavaName(String.valueOf(field));
                String columnName = mappingField.getMappingColumnName();
                select.column(columnName);
            }
        }

        if (havingFields != null && havingFields.size() > 0) {
            select.having();
            for (HavingField field : havingFields) {
                BasicFunction function = field.getFunction();
                boolean distinct = field.isDistinct();
                DefaultFilter filter = (DefaultFilter) field.getFilter();
                if (filter == null) {
                    throw new IllegalArgumentException(I18n.print("fun_miss_filter"));
                }
                Object key = filter.getKey();
                String symbol = filter.getSymbol();
                Object value = filter.getValue();
                Object startValue = filter.getStartValue();
                Object endValue = filter.getEndValue();

                MappingField mappingField = mappingTable.getMappingFieldByJavaName(String.valueOf(key));
                String columnName = mappingField.getMappingColumnName();

                if (!symbol.equalsIgnoreCase("isNull")
                        && symbol.equalsIgnoreCase("isNotNull")) {
                    if (function == BasicFunction.AVG) {
                        if (distinct) {
                            select.avg("DISTINCT", columnName);
                        } else {
                            select.avg(columnName);
                        }
                    }
                    if (function == BasicFunction.COUNT) {
                        if (distinct) {
                            select.count("DISTINCT", columnName);
                        } else {
                            select.count(columnName);
                        }
                    }
                    if (function == BasicFunction.MAX) {
                        if (distinct) {
                            select.max("DISTINCT", columnName);
                        } else {
                            select.max(columnName);
                        }
                    }
                    if (function == BasicFunction.MIN) {
                        if (distinct) {
                            select.min("DISTINCT", columnName);
                        } else {
                            select.min(columnName);
                        }
                    }
                    if (function == BasicFunction.SUM) {
                        if (distinct) {
                            select.sum("DISTINCT", columnName);
                        } else {
                            select.sum(columnName);
                        }
                    }
                }


                if (symbol.equalsIgnoreCase("like")) {
                    select.like().value(value);
                } else if (symbol.equalsIgnoreCase("in")) {
                    select.in().value(value);
                } else if (symbol.equalsIgnoreCase("notIn")) {
                    select.nin().value(value);
                } else if (symbol.equalsIgnoreCase("isNull")) {
                    select.isNull(columnName);
                } else if (symbol.equalsIgnoreCase("notNull")) {
                    select.isNotNull(columnName);
                } else if (symbol.equalsIgnoreCase("between")) {
                    select.between().section((Serializable) startValue, (Serializable) endValue);
                } else { // A='B' 或者 A!='B' 或者 A>2 A>=2 或者 A<2 A<=2
                    select.eq().value(value);
                }
            }
        }

        this.buildOrderBy(select, orders, mappingTable, false);

        SQLBuilderCombine combine = dialect.select(select.compile());
        Object result = this.runner.doHandler(new JDBCTraversing(TypeForRunner.SELECT,
                combine.getSql(), combine.getPlaceholders()));

        return (List<ModelObject>) result;
    }

    private void buildSelectField(DefaultSQLSelectBuilder select,
                                  Map<Join, String> alias,
                                  Map<Object, List<SelectFieldAliasReference>> fieldAlias) {
        if (fieldAlias != null && fieldAlias.size() > 0) {
            Iterator<Map.Entry<Object, List<SelectFieldAliasReference>>> iterator = fieldAlias.entrySet().iterator();
            while (iterator.hasNext()) {
                Map.Entry<Object, List<SelectFieldAliasReference>> entry = iterator.next();
                Object key = entry.getKey();
                String tableAliasName = alias.get(key);
                List<SelectFieldAliasReference> value = entry.getValue();
                for (SelectFieldAliasReference reference : value) {
                    select.fields(tableAliasName, reference.getFieldName(), reference.getFieldAliasName());
                }
            }
        }
    }

    private void buildJoinQuery(DefaultSQLSelectBuilder select,
                                Map<Join, String> alias,
                                List<Join> joins,
                                boolean onlyInnerJoin) {
        if (joins != null && joins.size() > 0) {
            for (Join j : joins) {
                DefaultJoin join = (DefaultJoin) j;
                List<JoinOnFilter> ons = join.getOns();
                if (ons != null && ons.size() > 0 && (onlyInnerJoin && join.getJoinType() == 1)) {
                    Class<?> table = join.getTable();
                    Class<?> mainTable = join.getMainTable();
                    String aliasName = alias.get(j);
                    Join parent = join.getParentJoin();
                    String parentAliasName = null;
                    if (parent != null) {
                        parentAliasName = alias.get(parent);
                    } else {
                        parentAliasName = "T";
                    }

                    MappingTable mainMappingTable = this.mappingGlobalWrapper.getMappingTable(mainTable);
                    MappingTable mappingTable = this.mappingGlobalWrapper.getMappingTable(table);

                    if (join.getJoinType() == 0) { // left join
                        select.left().join();
                    }
                    if (join.getJoinType() == 1) { // inner join
                        select.inner().join();
                    }

                    select.table(mappingTable.getMappingTableName(), aliasName);
                    select.on();
                    Iterator<JoinOnFilter> iterator = ons.iterator();
                    while (iterator.hasNext()) {
                        JoinOnFilter filter = iterator.next();
                        if (filter.isOn()) {
                            OnField field = filter.getOnField();
                            MappingField mainMappingField = mainMappingTable.getMappingFieldByJavaName(String.valueOf(field.getKey()));
                            MappingField mappingField = mainMappingTable.getMappingFieldByJavaName(String.valueOf(field.getValue()));

                            String columnName = mainMappingField.getMappingColumnName();
                            String value = mappingField.getMappingColumnName();
                            String symbol = field.getSymbol();

                            // 最好校验每一个参数值
                            if (symbol.equalsIgnoreCase("like")) {
                                select.column(aliasName, columnName).like().column(parentAliasName, value);
                            } else if (symbol.equalsIgnoreCase("in")) {
                                select.column(aliasName, columnName).in().column(parentAliasName, value);
                            } else if (symbol.equalsIgnoreCase("notIn")) {
                                select.column(aliasName, columnName).nin().column(parentAliasName, value);
                            } else if (symbol.equalsIgnoreCase("isNull")) {
                                select.isNull(aliasName, columnName);
                            } else if (symbol.equalsIgnoreCase("notNull")) {
                                select.isNotNull(aliasName, columnName);
                            } else { // A='B' 或者 A!='B' 或者 A>2 A>=2 或者 A<2 A<=2
                                select.column(aliasName, columnName).eq().column(parentAliasName, value);
                            }
                        } else {
                            DefaultFilter f = (DefaultFilter) filter.getFilter();
                            MappingField mappingField = mainMappingTable.getMappingFieldByJavaName(String.valueOf(f.getKey()));
                            String symbol = f.getSymbol();
                            String columnName = mappingField.getMappingColumnName();
                            Object value = f.getValue();
                            Object startValue = f.getStartValue();
                            Object endValue = f.getEndValue();

                            // 最好校验每一个参数值
                            if (symbol.equalsIgnoreCase("like")) {
                                select.column(aliasName, columnName).like().value(value);
                            } else if (symbol.equalsIgnoreCase("in")) {
                                select.column(aliasName, columnName).in().value(value);
                            } else if (symbol.equalsIgnoreCase("notIn")) {
                                select.column(aliasName, columnName).nin().value(value);
                            } else if (symbol.equalsIgnoreCase("isNull")) {
                                select.isNull(aliasName, columnName);
                            } else if (symbol.equalsIgnoreCase("notNull")) {
                                select.isNotNull(aliasName, columnName);
                            } else if (symbol.equalsIgnoreCase("between")) {
                                select.column(aliasName, columnName)
                                        .between().section((Serializable) startValue, (Serializable) endValue);
                            } else { // A='B' 或者 A!='B' 或者 A>2 A>=2 或者 A<2 A<=2
                                select.column(aliasName, columnName).eq().value(value);
                            }
                        }
                        if (iterator.hasNext()) {
                            select.and();
                        }
                    }
                }
            }
        }
    }

    private void buildOrderBy(DefaultSQLSelectBuilder select,
                              List<Order> orders,
                              MappingTable mappingTable,
                              boolean isInnerSelect) {
        if (orders != null) {
            for (Order order : orders) {
                boolean isAsc = order.isAsc();
                Object field = order.getField();
                MappingField mappingField = mappingTable.getMappingFieldByJavaName(String.valueOf(field));
                if (isInnerSelect) {
                    select.orderBy().column("T", mappingField.getMappingColumnName());
                } else {
                    select.orderBy().column(mappingField.getMappingColumnName());
                }
                if (isAsc) select.asc();
                else select.desc();
            }
        }
    }

    private List<ModelObject> buildMergeObjects(Map<Object, List<SelectFieldAliasReference>> references,
                                                DefaultQuery query,
                                                ModelObjectConvertKey convert,
                                                List<ModelObject> os) {
        List<SelectFieldAliasReference> selectFields = null;
        if (references != null) {
            selectFields = new ArrayList<>();
            Iterator<Map.Entry<Object, List<SelectFieldAliasReference>>> iterator = references.entrySet().iterator();
            while (iterator.hasNext()) {
                Map.Entry<Object, List<SelectFieldAliasReference>> entry = iterator.next();
                List<SelectFieldAliasReference> ref = entry.getValue();
                selectFields.addAll(ref);
            }
        }

        List<MergeTree> mergeTrees = new ArrayList();
        MergeTree top = new MergeTree();
        top.setMainTable(query.getTableClass());
        top.setSelfTable(query.getTableClass());
        if (references != null) {
            List<SelectFieldAliasReference> mainFields = references.get(query);
            if (mainFields != null) {
                top.setMapperSelectFields(mainFields);
            }
        }
        mergeTrees.add(top);

        List<Join> joins = query.getJoins();

        if (joins != null) {
            for (Join join : joins) {
                MergeTree jm = new MergeTree();
                jm.setJoin(join);
                DefaultJoin j = (DefaultJoin) join;
                if (!j.isMulti()) {
                    jm.setMulti(false);
                }
                Class<?> c1 = j.getMainTable();
                Class<?> c2 = j.getTable();
                jm.setExternalConnectionName(j.getAliasName());

                jm.setMainTable(c1);
                jm.setSelfTable(c2);
                //  jm.setTableAliasName(j.getTableClassAliasName());

                if (references != null) {
                    List<SelectFieldAliasReference> fields = references.get(join);
                    jm.setMapperSelectFields(fields);
                }

                if (((DefaultJoin) join).getParentJoin() == null) {
                    top.addChildren(jm);
                    jm.setParent(top);
                } else {
                    for (MergeTree m : mergeTrees) {
                        if (m.getJoin() == ((DefaultJoin) join).getParentJoin()) {
                            m.addChildren(jm);
                            jm.setParent(m);
                        }
                    }
                }

                mergeTrees.add(jm);
            }
        }

        ObjectMerge modelMerge = new DefaultObjectMerge();
        modelMerge.setMergeTree(top);
        modelMerge.setMapperSelectFields(selectFields);
        modelMerge.setMappingNamedConvert(convert);
        return modelMerge.getMergeAfterObjects(os, query.getTableClass());
    }

    private void buildWraps(Object builder,
                            MappingTable table,
                            Wraps<Filter> wraps,
                            boolean hasJoins) {
        if (wraps != null) {
            Iterator<WrapsObject<Filter>> wrapsIterator = wraps.iterator();
            while (wrapsIterator.hasNext()) {
                WrapsObject<Filter> filter = wrapsIterator.next();
                DefaultFilter where = (DefaultFilter) filter.getWhere();
                Wraps link = filter.getLink();
                CriteriaLogic logic = filter.getLogic();

                if (where != null) {
                    Object key = where.getKey();
                    Object value = where.getValue();
                    Object startValue = where.getStartValue();
                    Object endValue = where.getEndValue();
                    String symbol = where.getSymbol();

                    MappingField field = table.getMappingFieldByJavaName(String.valueOf(key));
                    String columnName = field.getMappingColumnName();

                    // 最好校验每一个参数值
                    if (symbol.equalsIgnoreCase("like")) {
                        if (hasJoins) ((AbsWhereColumnBuilder) builder).column("T", columnName);
                        else ((AbsWhereColumnBuilder) builder).column(columnName);
                        ((OperatorBuilder) builder).like();
                        ((AbsValueBuilder) builder).value(value);
                    } else if (symbol.equalsIgnoreCase("in")) {
                        if (hasJoins) ((AbsWhereColumnBuilder) builder).column("T", columnName);
                        else ((AbsWhereColumnBuilder) builder).column(columnName);
                        ((OperatorBuilder) builder).in();
                        ((AbsValueBuilder) builder).value(value);
                    } else if (symbol.equalsIgnoreCase("notIn")) {
                        if (hasJoins) ((AbsWhereColumnBuilder) builder).column("T", columnName);
                        else ((AbsWhereColumnBuilder) builder).column(columnName);
                        ((OperatorBuilder) builder).nin();
                        ((AbsValueBuilder) builder).value(value);
                    } else if (symbol.equalsIgnoreCase("isNull")) {
                        if (hasJoins)
                            ((OperatorFunctionBuilder) builder).isNull("T", columnName);
                        else
                            ((OperatorFunctionBuilder) builder).isNull(columnName);
                    } else if (symbol.equalsIgnoreCase("notNull")) {
                        if (hasJoins)
                            ((OperatorFunctionBuilder) builder).isNotNull("T", columnName);
                        else
                            ((OperatorFunctionBuilder) builder).isNotNull(columnName);
                    } else if (symbol.equalsIgnoreCase("between")) {
                        if (hasJoins) ((AbsWhereColumnBuilder) builder).column("T", columnName);
                        else ((AbsWhereColumnBuilder) builder).column(columnName);
                        ((OperatorBuilder) builder).between();
                        ((BetweenValueBuilder) builder).section((Serializable) startValue, (Serializable) endValue);
                    } else { // A='B' 或者 A!='B' 或者 A>2 A>=2 或者 A<2 A<=2
                        if (hasJoins) ((AbsWhereColumnBuilder) builder).column("T", columnName);
                        else ((AbsWhereColumnBuilder) builder).column(columnName);
                        ((OperatorBuilder) builder).eq();
                        ((AbsValueBuilder) builder).value(value);
                    }
                } else if (link != null) {
                    this.buildWraps(builder, table, link, hasJoins);
                }

                if (wrapsIterator.hasNext()) {
                    if (logic.equals(CriteriaLogic.AND)) {
                        ((AndBuilder) builder).and();
                    }
                    if (logic.equals(CriteriaLogic.OR)) {
                        ((OrBuilder) builder).or();
                    }
                }
            }
        }
    }

    public Object dialect(StampAction stampAction) throws SQLException {
        PlatformDialect dialect = this.getDialect();
        SQLBuilderCombine combine = null;
        if (stampAction instanceof StampAlter) {
            combine = dialect.alter((StampAlter) stampAction);
        }
        if (stampAction instanceof StampCreate) {
            combine = dialect.create((StampCreate) stampAction);
        }
        if (stampAction instanceof StampDrop) {
            combine = dialect.drop((StampDrop) stampAction);
        }
        if (stampAction instanceof StampInsert) {
            combine = dialect.insert((StampInsert) stampAction);
        }
        if (stampAction instanceof StampDelete) {
            combine = dialect.delete((StampDelete) stampAction);
        }
        if (stampAction instanceof StampSelect) {
            combine = dialect.select((StampSelect) stampAction);
        }
        if (stampAction instanceof StampUpdate) {
            combine = dialect.update((StampUpdate) stampAction);
        }

        if (combine != null) {
            return new DefaultDBRunner(dswrapper).doHandler(new JDBCTraversing(combine.getSql(), combine.getPlaceholders()));
        }
        return null;
    }

    public Object original(JDBCTraversing traversing) throws SQLException {
        return this.runner.doHandler(traversing);
    }
}
