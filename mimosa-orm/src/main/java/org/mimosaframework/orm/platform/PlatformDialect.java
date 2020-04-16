package org.mimosaframework.orm.platform;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.mimosaframework.core.json.ModelObject;
import org.mimosaframework.core.utils.StringTools;
import org.mimosaframework.orm.i18n.I18n;
import org.mimosaframework.orm.mapping.MappingField;
import org.mimosaframework.orm.mapping.MappingGlobalWrapper;
import org.mimosaframework.orm.mapping.MappingIndex;
import org.mimosaframework.orm.mapping.MappingTable;
import org.mimosaframework.orm.sql.StructureBuilder;
import org.mimosaframework.orm.sql.UnifyBuilder;
import org.mimosaframework.orm.sql.alter.DefaultSQLAlterBuilder;
import org.mimosaframework.orm.sql.create.ColumnTypeBuilder;
import org.mimosaframework.orm.sql.create.DefaultSQLCreateBuilder;
import org.mimosaframework.orm.sql.drop.DefaultSQLDropBuilder;
import org.mimosaframework.orm.sql.insert.DefaultSQLInsertBuilder;
import org.mimosaframework.orm.sql.select.DefaultSQLSelectBuilder;
import org.mimosaframework.orm.sql.stamp.*;
import org.mimosaframework.orm.utils.LOBLoader;

import java.sql.Clob;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.*;

public abstract class PlatformDialect {
    private static final Log logger = LogFactory.getLog(PlatformDialect.class);
    private Map<KeyColumnType, ColumnType> columnTypes = new HashMap<>();
    private DBRunner runner = null;
    protected DataSourceWrapper dataSourceWrapper;
    protected MappingGlobalWrapper mappingGlobalWrapper;

    protected void registerColumnType(KeyColumnType type, String typeName) {
        this.columnTypes.put(type, new ColumnType(type, typeName,
                -1, -1, ColumnCompareType.NONE));
    }

    protected void registerColumnType(KeyColumnType type, String typeName, int length) {
        this.columnTypes.put(type, new ColumnType(type, typeName,
                length, -1, ColumnCompareType.JAVA));
    }

    protected void registerColumnType(KeyColumnType type, String typeName, int length, int scale) {
        this.columnTypes.put(type, new ColumnType(type, typeName,
                length, scale, ColumnCompareType.JAVA));
    }

    protected void registerColumnType(KeyColumnType type, String typeName, ColumnCompareType columnCompareType) {
        this.columnTypes.put(type, new ColumnType(type, typeName,
                -1, -1, columnCompareType));
    }

    protected void registerColumnType(KeyColumnType type, String typeName,
                                      int length, ColumnCompareType columnCompareType) {
        this.columnTypes.put(type, new ColumnType(type, typeName,
                length, -1, columnCompareType));
    }

    public void setDataSourceWrapper(DataSourceWrapper dswrapper) {
        this.dataSourceWrapper = dswrapper;
        this.runner = new DefaultDBRunner(dswrapper);
    }

    public void setMappingGlobalWrapper(MappingGlobalWrapper mappingGlobalWrapper) {
        this.mappingGlobalWrapper = mappingGlobalWrapper;
    }

    public List<TableStructure> getTableStructures() throws SQLException {
        StructureBuilder structureBuilder = new StructureBuilder();
        String schema = this.getCatalogAndSchema();
        StampAction table = structureBuilder.table(schema).compile();
        Object result = this.runner(table);
        if (result instanceof List) {
            List<TableStructure> tableStructures = new ArrayList<>();
            List<String> tables = new ArrayList<>();
            List<ModelObject> list = (List<ModelObject>) result;
            for (ModelObject o : list) {
                TableStructure tableStructure = new TableStructure();
                tableStructure.setTableSchema(o.getString("TABSCHEMA"));
                tableStructure.setTableName(o.getString("TABNAME"));
                tableStructure.setType(o.getString("TYPE"));
                tableStructure.setCount(o.getLongValue("COUNT"));
                tableStructure.setLastUsed(o.get("LASTUSED"));
                tableStructure.setComment(o.getString("COMMENT"));
                tableStructure.setCreateTime(o.get("CREATE_TIME"));
                tableStructures.add(tableStructure);
                tables.add(tableStructure.getTableName());
            }

            if (tableStructures != null && tableStructures.size() > 0) {
                StampAction column = structureBuilder.column(schema, tables).compile();
                List<TableColumnStructure> columnStructures = new ArrayList<>();
                LOBLoader.register(new LOBLoader.Loader() {
                    @Override
                    public void lob(Map map, String columnName, Object lob) {
                        if (lob instanceof Clob) {
                            try {
                                map.put(columnName, ((Clob) lob).getSubString(1, (int) ((Clob) lob).length()));
                            } catch (SQLException e) {
                                map.put(columnName, "$'clob_get_error'");
                                e.printStackTrace();
                            }
                        }
                    }
                });
                Object resultColumn = this.runner(column);
                LOBLoader.close();
                if (resultColumn instanceof List) {
                    List<ModelObject> listColumn = (List<ModelObject>) resultColumn;
                    for (ModelObject o : listColumn) {
                        TableColumnStructure columnStructure = new TableColumnStructure();
                        columnStructure.setTableSchema(o.getString("TABSCHEMA"));
                        columnStructure.setTableName(o.getString("TABNAME"));
                        columnStructure.setColumnName(o.getString("COLNAME"));
                        columnStructure.setTypeName(o.getString("TYPENAME"));
                        columnStructure.setLength(o.getIntValue("LENGTH"));
                        columnStructure.setScale(o.getIntValue("SCALE"));
                        columnStructure.setDefaultValue(o.getString("DEFAULT"));
                        columnStructure.setIsNullable(o.getString("IS_NULLABLE"));
                        columnStructure.setAutoIncrement(o.getString("AUTO_INCREMENT"));
                        columnStructure.setComment(o.getString("COMMENT"));
                        columnStructures.add(columnStructure);
                    }
                }

                StampAction index = structureBuilder.index(schema, tables).compile();
                List<TableIndexStructure> indexStructures = new ArrayList<>();
                Object resultIndex = this.runner(index);
                if (resultIndex instanceof List) {
                    List<ModelObject> listIndex = (List<ModelObject>) resultIndex;
                    for (ModelObject o : listIndex) {
                        TableIndexStructure indexStructure = new TableIndexStructure();
                        indexStructure.setTableSchema(o.getString("TABSCHEMA"));
                        indexStructure.setIndexName(o.getString("INDNAME"));
                        indexStructure.setTableName(o.getString("TABNAME"));
                        indexStructure.setType(o.getString("TYPE"));
                        indexStructure.setColumnName(o.getString("COLNAME"));
                        indexStructure.setComment(o.getString("COMMENT"));
                        indexStructures.add(indexStructure);
                    }
                }

                StampAction constraint = structureBuilder.constraint(schema, tables).compile();
                List<TableConstraintStructure> constraintStructures = new ArrayList<>();
                Object resultConstraint = this.runner(constraint);
                if (resultConstraint instanceof List) {
                    List<ModelObject> listConstraint = (List<ModelObject>) resultConstraint;
                    for (ModelObject o : listConstraint) {
                        TableConstraintStructure constraintStructure = new TableConstraintStructure();
                        constraintStructure.setTableSchema(o.getString("TABSCHEMA"));
                        constraintStructure.setConstraintName(o.getString("CONSTNAME"));
                        constraintStructure.setTableName(o.getString("TABNAME"));
                        constraintStructure.setColumnName(o.getString("COLNAME"));
                        constraintStructure.setForeignTableName(o.getString("FGNTABNAME"));
                        constraintStructure.setForeignColumnName(o.getString("FGNCOLNAME"));
                        constraintStructure.setType(o.getString("TYPE"));
                        constraintStructures.add(constraintStructure);
                    }
                }

                for (TableStructure tableStructure : tableStructures) {
                    String tableName = tableStructure.getTableName();
                    if (columnStructures != null && columnStructures.size() > 0) {
                        List<TableColumnStructure> forTable = new ArrayList<>();
                        for (TableColumnStructure columnStructure : columnStructures) {
                            if (tableName.equalsIgnoreCase(columnStructure.getTableName())) {
                                forTable.add(columnStructure);
                            }
                        }
                        tableStructure.setColumnStructures(forTable);
                    }
                    if (indexStructures != null && indexStructures.size() > 0) {
                        List<TableIndexStructure> forTable = new ArrayList<>();
                        for (TableIndexStructure indexStructure : indexStructures) {
                            if (tableName.equalsIgnoreCase(indexStructure.getTableName())) {
                                forTable.add(indexStructure);
                            }
                        }
                        tableStructure.setIndexStructures(forTable);
                    }
                    if (constraintStructures != null && constraintStructures.size() > 0) {
                        List<TableConstraintStructure> forTable = new ArrayList<>();
                        for (TableConstraintStructure constraintStructure : constraintStructures) {
                            if (tableName.equalsIgnoreCase(constraintStructure.getTableName())) {
                                forTable.add(constraintStructure);
                            }
                        }
                        tableStructure.setConstraintStructures(forTable);
                    }
                }
            }

            return tableStructures;
        }
        return null;
    }

    public Object runner(StampAction action) throws SQLException {
        StampCombineBuilder combineBuilder = PlatformFactory
                .getStampAlterBuilder(this.dataSourceWrapper.getDatabaseTypeEnum(), action);
        SQLBuilderCombine builderCombine = combineBuilder.getSqlBuilder(this.mappingGlobalWrapper, action);
        JDBCTraversing jdbcTraversing = new JDBCTraversing(builderCombine.getSql(), builderCombine.getPlaceholders());
        jdbcTraversing.setTypeForRunner(TypeForRunner.SELECT);
        return this.runner(jdbcTraversing);
    }

    public Object runner(JDBCTraversing traversing) throws SQLException {
        if (this.runner != null) {
            return this.runner.doHandler(traversing);
        }
        return null;
    }

    /**
     * 供应商          Catalog支持                        Schema支持
     * Oracle         不支持                              Oracle User ID
     * MySQL          不支持                              数据库名
     * MS SQL Server  数据库名                            对象属主名，2005版开始有变
     * DB2            指定数据库对象时，Catalog部分省略      Catalog属主名
     * Sybase         数据库名                            数据库属主名
     * Informix       不支持                              不需要
     * PointBase      不支持                              数据库名
     *
     * @return
     */
    protected String getCatalogAndSchema() throws SQLException {
        Connection connection = null;
        try {
            connection = dataSourceWrapper.getConnection();
            String catalog = null;
            try {
                catalog = connection.getCatalog();
            } catch (Exception exception) {

            }
            String schema = null;
            try {
                schema = connection.getSchema();
            } catch (Exception exception) {

            }
            return (StringTools.isNotEmpty(catalog) ? catalog : "") +
                    (StringTools.isNotEmpty(schema) ? (StringTools.isNotEmpty(catalog) ? "." : "") + schema : "");
        } finally {
            dataSourceWrapper.close();
        }
    }

    public ColumnType getColumnType(KeyColumnType type) {
        return columnTypes.get(type);
    }

    protected StampCreate commonCreateTable(MappingTable mappingTable) {
        return this.commonCreateTable(mappingTable, null, false);
    }

    protected StampCreate commonCreateTable(MappingTable mappingTable,
                                            String tableName,
                                            boolean isSkipAuto) {
        if (StringTools.isEmpty(tableName)) tableName = mappingTable.getMappingTableName();
        Set<MappingField> mappingFields = mappingTable.getMappingFields();
        if (mappingFields != null && mappingFields.size() > 0) {
            DefaultSQLCreateBuilder sql = new DefaultSQLCreateBuilder();
            sql.create().table().ifNotExist().name(tableName);
            for (MappingField mappingField : mappingFields) {
                String field = mappingField.getMappingColumnName();
                Class type = mappingField.getMappingFieldType();
                int length = mappingField.getMappingFieldLength();
                int scale = mappingField.getMappingFieldDecimalDigits();
                String defVal = mappingField.getMappingFieldDefaultValue();
                boolean nullable = mappingField.isMappingFieldNullable();
                boolean autoIncr = mappingField.isMappingAutoIncrement();
                boolean isPk = mappingField.isMappingFieldPrimaryKey();
                boolean timeForUpdate = mappingField.isMappingFieldTimeForUpdate();
                String comment = mappingField.getMappingFieldComment();

                sql.column(field);
                this.setSQLType(sql, type, length, scale);
                if (!nullable) {
                    sql.not();
                    sql.nullable();
                }
                if (isPk) sql.primary().key();
                if (autoIncr && !isSkipAuto) sql.autoIncrement();
                if (StringTools.isNotEmpty(defVal)) {
                    sql.defaultValue(defVal);
                }
                if (StringTools.isNotEmpty(comment)) {
                    sql.comment(comment);
                }

                if (timeForUpdate) sql.timeForUpdate();
            }

            if (StringTools.isNotEmpty(mappingTable.getEncoding())) {
                sql.charset(mappingTable.getEncoding());
            }

            return (StampCreate) ((UnifyBuilder) sql).compile();
        } else {
            throw new IllegalArgumentException(I18n.print("platform_dialect_miss_fields",
                    mappingTable.getMappingTableName()));
        }
    }

    protected void setSQLType(ColumnTypeBuilder typeBuilder,
                              Class type,
                              int length,
                              int scale) {
        JavaType2ColumnType.getColumnTypeByJava(type, typeBuilder, length, scale);
    }

    protected StampDrop commonDropTable(TableStructure structure) {
        DefaultSQLDropBuilder sql = new DefaultSQLDropBuilder();
        return sql.drop().table().table(structure.getTableName()).compile();
    }

    protected StampAlter commonAddColumn(MappingTable mappingTable, MappingField mappingField) {
        String field = mappingField.getMappingColumnName();
        Class type = mappingField.getMappingFieldType();
        int length = mappingField.getMappingFieldLength();
        int scale = mappingField.getMappingFieldDecimalDigits();
        String defVal = mappingField.getMappingFieldDefaultValue();
        boolean nullable = mappingField.isMappingFieldNullable();
        boolean autoIncr = mappingField.isMappingAutoIncrement();
        String comment = mappingField.getMappingFieldComment();
        boolean isTimeForUpdate = mappingField.isMappingFieldTimeForUpdate();

        DefaultSQLAlterBuilder sql = new DefaultSQLAlterBuilder();
        sql.alter().table(mappingTable.getMappingClass()).add().column(field);

        if (isTimeForUpdate) {
            sql.timeForUpdate();
        } else {
            this.setSQLType(sql, type, length, scale);
            if (!nullable) {
                sql.not();
                sql.nullable();
            }
            if (StringTools.isNotEmpty(defVal)) {
                sql.defaultValue(defVal);
            }
            if (StringTools.isNotEmpty(comment)) {
                sql.comment(comment);
            }
        }

        return sql.compile();
    }

    protected StampAlter commonDropColumn(MappingTable mappingTable, TableColumnStructure structure) {
        DefaultSQLAlterBuilder sql = new DefaultSQLAlterBuilder();
        return sql.alter().table(mappingTable.getMappingTableName())
                .drop().column(structure.getColumnName()).compile();
    }

    protected StampCreate commonAddIndex(MappingTable mappingTable, MappingIndex mappingIndex) {
        String tableName = mappingTable.getMappingTableName();
        String indexName = mappingIndex.getIndexName();
        List<MappingField> fields = mappingIndex.getIndexColumns();
        String[] columns = new String[fields.size()];
        int i = 0;
        for (MappingField field : fields) {
            columns[i] = field.getMappingColumnName();
            i++;
        }
        DefaultSQLCreateBuilder sqlCreateBuilder = new DefaultSQLCreateBuilder();
        StampCreate sql = sqlCreateBuilder.create()
                .index().name(indexName).on().table(tableName).columns(columns).compile();
        return sql;
    }

    protected StampDrop commonDropIndex(MappingTable mappingTable, String indexName) {
        String tableName = mappingTable.getMappingTableName();
        DefaultSQLDropBuilder sql = new DefaultSQLDropBuilder();
        return sql.drop().index().name(indexName).on().table(tableName).compile();
    }

    /**
     * 检查表的索引
     *
     * @param mappingTable
     * @param tableStructure
     * @param mappingField
     * @param columnStructure
     */
    protected void triggerIndex(MappingTable mappingTable,
                                TableStructure tableStructure,
                                MappingField mappingField,
                                TableColumnStructure columnStructure) throws SQLException {
        if (mappingField != null) {
            boolean onlyPK = false;
            if (tableStructure != null) {
                onlyPK = tableStructure.isOnlyPrimaryKey(mappingField.getMappingColumnName());
            }
            boolean unique = false;
            if (tableStructure != null) {
                unique = tableStructure.isOnlyUniqueIndex(mappingField.getMappingColumnName());
            }
            boolean index = false;
            if (tableStructure != null) {
                index = tableStructure.isOnlyNormalIndex(mappingField.getMappingColumnName());
            }

            List<MappingField> pkFields = mappingTable.getMappingPrimaryKeyFields();
            if (!(pkFields != null && pkFields.size() == 1 && mappingField.isMappingFieldPrimaryKey())
                    && mappingField.isMappingFieldUnique() != unique) {
                if (mappingField.isMappingFieldUnique()) {
                    this.createIndex(mappingTable, mappingField, true);
                }
                if (unique) {
                    this.dropIndex(mappingTable, mappingField);
                }
            }

            if (!(pkFields != null && pkFields.size() == 1 && mappingField.isMappingFieldPrimaryKey())
                    && !mappingField.isMappingFieldUnique()) {
                if (mappingField.isMappingFieldIndex() != index) {
                    this.createIndex(mappingTable, mappingField, false);
                }
            }

            if (((pkFields != null && pkFields.size() == 1 && mappingField.isMappingFieldPrimaryKey()) && (unique || index))
                    || (mappingField.isMappingFieldUnique() && index)) {
                this.dropIndex(mappingTable, mappingField);
            }
        }
    }

    /**
     * 修改删除表字段时可能需要重新创建单列索引
     *
     * @param mappingTable
     * @param mappingField
     * @param unique
     */
    protected void createIndex(MappingTable mappingTable, MappingField mappingField, boolean unique) throws SQLException {

    }

    /**
     * 修改删除表字段时可能需要重新删除单列索引
     *
     * @param mappingTable
     * @param mappingField
     */
    protected void dropIndex(MappingTable mappingTable, MappingField mappingField) throws SQLException {

    }

    public List<ColumnEditType> compareColumnChange(TableStructure structure,
                                                    MappingField currField,
                                                    TableColumnStructure columnStructure) {
        List<ColumnEditType> columnEditTypes = new ArrayList<>();
        KeyColumnType keyColumnType = JavaType2ColumnType.getColumnTypeByJava(currField.getMappingFieldType());
        ColumnType columnType = this.getColumnType(keyColumnType);
        if (columnType == null) {
            throw new IllegalArgumentException(I18n.print("platform_executor_empty_type", currField.getMappingFieldType().getSimpleName()));
        }
        if (columnStructure.getTypeName().equalsIgnoreCase(columnType.getTypeName())) {
            if (columnType.getCompareType() == ColumnCompareType.JAVA
                    && (columnStructure.getLength() != currField.getMappingFieldLength()
                    || columnStructure.getScale() != currField.getMappingFieldDecimalDigits())) {
                columnEditTypes.add(ColumnEditType.TYPE_LENGTH);
            }
            if (columnType.getCompareType() == ColumnCompareType.SELF
                    && (columnStructure.getLength() != columnType.getLength())) {
                columnEditTypes.add(ColumnEditType.TYPE_LENGTH);
            }
        } else {
            columnEditTypes.add(ColumnEditType.TYPE);
        }

        boolean isPk = structure.isPrimaryKeyColumn(columnStructure.getColumnName());
        if (keyColumnType != KeyColumnType.TIMESTAMP) {
            boolean nullable = columnStructure.isNullable();
            if (nullable != currField.isMappingFieldNullable()) {
                columnEditTypes.add(ColumnEditType.ISNULL);
            }

            String defA = currField.getMappingFieldDefaultValue();
            String defB = columnStructure.getDefaultValue();
            // 如果返回当前字符串则表示获取默认值出错了
            // 获取默认值出错后不再对比
            if (defB == null || !defB.equals("$'clob_get_error'")) {
                if (!(StringTools.isEmpty(defA) && StringTools.isEmpty(defB))
                        && !(StringTools.isEmpty(defA) && "0".equals(defB))) {  // int bigint 等 数据库默认值总是0
                    if ((StringTools.isNotEmpty(defA) && !defA.equals(defB)) || (StringTools.isNotEmpty(defB) && !defB.equals(defA))) {
                        boolean last = false;
                        // 由于某些数据库(eg:db2)的default值区分数据类型所以会加入字符串包装，这里判断一下
                        if (defB != null && defB.startsWith("'") && defB.endsWith("'")) {
                            defB = defB.substring(1, defB.length() - 1);
                            if (defB.equals(defA)) {
                                last = true;
                            }
                        }
                        if (!last) columnEditTypes.add(ColumnEditType.DEF_VALUE);
                    }
                }
            }
        }

        if (columnStructure.isAutoIncrement() != currField.isMappingAutoIncrement()) {
            columnEditTypes.add(ColumnEditType.AUTO_INCREMENT);
        }

        String cmtA = currField.getMappingFieldComment();
        String cmtB = columnStructure.getComment();
        if ((StringTools.isNotEmpty(cmtA) && !cmtA.equals(cmtB)) || (StringTools.isNotEmpty(cmtB) && !cmtB.equals(cmtA))) {
            columnEditTypes.add(ColumnEditType.COMMENT);
        }
        if (currField.isMappingFieldPrimaryKey() != isPk) {
            columnEditTypes.add(ColumnEditType.PRIMARY_KEY);
        }
        return columnEditTypes;
    }

    public abstract SQLBuilderCombine alter(StampAlter alter);

    public abstract SQLBuilderCombine create(StampCreate create);

    public abstract SQLBuilderCombine drop(StampDrop drop);

    public abstract SQLBuilderCombine insert(StampInsert insert);

    public abstract SQLBuilderCombine delete(StampDelete delete);

    public abstract SQLBuilderCombine select(StampSelect select);

    public abstract SQLBuilderCombine update(StampUpdate update);

    /**
     * 处理单个单元相关的属性
     *
     * @param definition
     * @throws SQLException
     */
    public DialectNextStep define(DataDefinition definition) throws SQLException {
        if (definition != null) {
            DataDefinitionType type = definition.getType();
            if (type == DataDefinitionType.CREATE_TABLE) {
                MappingTable mappingTable = definition.getMappingTable();
                StampCreate stampCreate = this.commonCreateTable(mappingTable);
                if (StringTools.isNotEmpty(mappingTable.getEngineName())) {
                    stampCreate.extra = "ENGINE=InnoDB";
                }
                this.runner(stampCreate);
                Set<MappingField> mappingFields = mappingTable.getMappingFields();
                for (MappingField mappingField : mappingFields) {
                    if (mappingField.isMappingFieldUnique() || mappingField.isMappingFieldIndex()) {
                        this.triggerIndex(definition.getMappingTable(),
                                null, mappingField, null);
                    }
                }
            }
            if (type == DataDefinitionType.DROP_TABLE) {
                TableStructure tableStructure = definition.getTableStructure();
                StampDrop stampDrop = this.commonDropTable(tableStructure);
                this.runner(stampDrop);
            }
            if (type == DataDefinitionType.ADD_COLUMN) {
                TableStructure structure = definition.getTableStructure();
                List<TableConstraintStructure> pks = structure.getPrimaryKey();
                List<TableColumnStructure> autos = structure.getAutoIncrement();
                MappingField mappingField = definition.getMappingField();
                if ((pks != null && pks.size() > 0 && mappingField.isMappingFieldPrimaryKey())
                        || (autos != null && autos.size() > 0 && mappingField.isMappingAutoIncrement())) {
                    return DialectNextStep.REBUILD;
                } else {
                    StampAlter stampAlter = this.commonAddColumn(definition.getMappingTable(), mappingField);
                    this.runner(stampAlter);

                    this.triggerIndex(definition.getMappingTable(), definition.getTableStructure(),
                            definition.getMappingField(), null);
                }
            }
            if (type == DataDefinitionType.MODIFY_COLUMN) {
                DialectNextStep step = this.defineModifyField(definition);
                return step;
            }
            if (type == DataDefinitionType.DROP_COLUMN) {
                TableColumnStructure columnStructure = definition.getColumnStructure();
                StampAlter stampAlter = this.commonDropColumn(definition.getMappingTable(), columnStructure);
                this.runner(stampAlter);
                if (columnStructure != null) {
                    columnStructure.setState(2);
                }
                this.triggerIndex(definition.getMappingTable(), definition.getTableStructure(),
                        definition.getMappingField(), definition.getColumnStructure());
            }
            if (type == DataDefinitionType.ADD_INDEX) {
                StampCreate sql = this.commonAddIndex(definition.getMappingTable(), definition.getMappingIndex());
                this.runner(sql);
            }
            if (type == DataDefinitionType.MODIFY_INDEX) {
                StampDrop stampDrop = this.commonDropIndex(definition.getMappingTable(), definition.getIndexName());
                this.runner(stampDrop);
                StampCreate sql = this.commonAddIndex(definition.getMappingTable(), definition.getMappingIndex());
                this.runner(sql);
            }
            if (type == DataDefinitionType.DROP_INDEX) {
                StampDrop stampDrop = this.commonDropIndex(definition.getMappingTable(), definition.getIndexName());
                this.runner(stampDrop);
            }
        }

        return DialectNextStep.NONE;
    }

    protected DialectNextStep defineModifyField(DataDefinition definition) throws SQLException {
        return DialectNextStep.REBUILD;
    }

    /**
     * 是否需要重建表,如果define方法返回要求重建表
     * 则根据配置的需要是否调用并重建表
     *
     * @throws SQLException
     */
    public void rebuildTable(MappingTable mappingTable, TableStructure tableStructure) throws SQLException {
        if (mappingTable != null && tableStructure != null) {
            String tableName = mappingTable.getMappingTableName() + "_ctmp";
            this.rebuildStartTable(mappingTable, tableName);
            DefaultSQLInsertBuilder insertBuilder = new DefaultSQLInsertBuilder();
            insertBuilder.insert().into().table(tableName);

            Set<MappingField> fields = mappingTable.getMappingFields();
            List<TableColumnStructure> columns = tableStructure.getColumnStructures();

            List<String> cols = new ArrayList<>();
            for (MappingField field : fields) {
                for (TableColumnStructure c : columns) {
                    if (field.getMappingColumnName().equals(c.getColumnName())) {
                        cols.add(field.getMappingColumnName());
                    }
                }
            }
            insertBuilder.columns(cols.toArray(new String[]{}));
            DefaultSQLSelectBuilder selectBuilder = new DefaultSQLSelectBuilder();
            selectBuilder.select().fields(cols.toArray(new String[]{})).from().table(mappingTable.getMappingTableName());
            insertBuilder.select(selectBuilder);
            try {
                this.runner(insertBuilder.compile());
            } catch (Exception e) {
                logger.error(I18n.print("copy_table_data_error",
                        mappingTable.getMappingTableName(),
                        e.getMessage()));
            }

            DefaultSQLDropBuilder dropBuilder = new DefaultSQLDropBuilder();
            dropBuilder.drop().table().name(mappingTable.getMappingTableName());
            this.runner(dropBuilder.compile());

            DefaultSQLAlterBuilder renameBuilder = new DefaultSQLAlterBuilder();
            renameBuilder.alter().table(tableName).rename().name(mappingTable.getMappingTableName());
            this.runner(renameBuilder.compile());

            this.rebuildEndTable(mappingTable, tableStructure);
        }
    }

    protected void rebuildStartTable(MappingTable mappingTable, String tableName) throws SQLException {
        if (mappingTable != null) {
            StampCreate create = this.commonCreateTable(mappingTable, tableName, false);
            this.runner(create);
        }
    }

    protected void rebuildEndTable(MappingTable mappingTable, TableStructure tableStructure) throws SQLException {

    }

    /**
     * 数据库驱动是否支持返回添加后的ID
     *
     * @return
     */
    public abstract boolean isSupportGeneratedKeys();
}
