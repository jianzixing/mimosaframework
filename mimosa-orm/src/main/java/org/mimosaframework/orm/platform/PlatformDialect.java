package org.mimosaframework.orm.platform;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.mimosaframework.core.json.ModelObject;
import org.mimosaframework.core.utils.StringTools;
import org.mimosaframework.orm.i18n.I18n;
import org.mimosaframework.orm.mapping.*;
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
import java.text.SimpleDateFormat;
import java.util.*;

public abstract class PlatformDialect implements Dialect {
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
                                      long length, ColumnCompareType columnCompareType) {
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

    public List<TableStructure> getTableStructures(List<String> classTableNames) throws SQLException {
        return this.loadingTableStructures(null);
    }

    protected List<TableStructure> loadingTableStructures(List<String> classTableNames) throws SQLException {
        StructureBuilder structureBuilder = new StructureBuilder();
        String schema = this.getCatalogAndSchema();
        StampAction table = structureBuilder.table(schema).compile();
        Object result = this.runner(table);
        if (result instanceof List) {
            List<TableStructure> tableStructures = new ArrayList<>();
            List<String> tables = new ArrayList<>();
            List<ModelObject> list = (List<ModelObject>) result;
            for (ModelObject o : list) {
                String tableName = o.getString("TABNAME");
                TableStructure tableStructure = new TableStructure();
                tableStructure.setTableSchema(o.getString("TABSCHEMA"));
                tableStructure.setTableName(o.getString("TABNAME"));
                tableStructure.setType(o.getString("TYPE"));
                tableStructure.setCount(o.getLongValue("COUNT"));
                tableStructure.setLastUsed(o.get("LASTUSED"));
                tableStructure.setComment(o.getString("COMMENT"));
                tableStructure.setCreateTime(o.get("CREATE_TIME"));
                tableStructures.add(tableStructure);
                if (classTableNames != null && classTableNames.contains(tableName.toLowerCase())) {
                    tables.add(tableStructure.getTableName());
                }
            }

            if (tableStructures != null && tableStructures.size() > 0 && tables.size() > 0) {
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
                        constraintStructure.setConstraintName(o.getString("CONSNAME"));
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

    protected Object runner(StampAction action) throws SQLException {
        StampCombineBuilder combineBuilder = PlatformFactory
                .getStampAlterBuilder(this.dataSourceWrapper.getDatabaseTypeEnum(), action);
        SQLBuilderCombine builderCombine = combineBuilder.getSqlBuilder(this.mappingGlobalWrapper, action);
        String sql = builderCombine.getSql();
        if (StringTools.isNotEmpty(sql)) {
            JDBCTraversing jdbcTraversing = new JDBCTraversing(sql, builderCombine.getPlaceholders());
            jdbcTraversing.setTypeForRunner(TypeForRunner.SELECT);
            return this.runner(jdbcTraversing);
        }
        return null;
    }

    protected Object runner(JDBCTraversing traversing) throws SQLException {
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
                KeyColumnType aptype = this.getAutoIncrementPrimaryKeyType();
                if (autoIncr && isPk && aptype != null) {
                    JavaType2ColumnType.setBuilderType(aptype, sql, length, scale);
                } else {
                    this.setSQLType(sql, type, length, scale);
                }
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
            if (defVal != null) {
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
        sqlCreateBuilder.create();
        if (mappingIndex.getIndexType() == IndexType.U) {
            sqlCreateBuilder.unique();
        }
        sqlCreateBuilder.index().name(indexName).on().table(tableName).columns(columns);
        return sqlCreateBuilder.compile();
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

    /**
     * 判断当前两个类型是否相等，一般情况下不需要重写判断
     * 但是如果数据库的数据类型名称和配置的类型名称无法一致时需要重写
     *
     * @param columnStructure
     * @param columnType
     * @return
     */
    protected boolean compareColumnChangeType(TableColumnStructure columnStructure, ColumnType columnType) {
        return columnStructure.getTypeName().equalsIgnoreCase(columnType.getTypeName());
    }

    /**
     * 默认情况下默认值是相同的字符串然后做判断，但是某些数据库
     * 默认值不走寻常路，会在默认值外添加包装字符串
     * 比如 默认值 A 在sqlserver中为 ('A')
     * 这个时候需要重写判断
     *
     * @param defA
     * @param defB
     * @return
     */
    protected boolean compareColumnChangeDefault(String defA, String defB) {
        return false;
    }

    protected KeyColumnType getAutoIncrementPrimaryKeyType() {
        return null;
    }

    /**
     * 重要！
     * 判断数据库和当前配置的字段是否一致
     *
     * @param structure
     * @param currField
     * @param columnStructure
     * @return
     */
    public List<ColumnEditType> compareColumnChange(TableStructure structure,
                                                    MappingField currField,
                                                    TableColumnStructure columnStructure) {
        List<ColumnEditType> columnEditTypes = new ArrayList<>();
        KeyColumnType keyColumnType = JavaType2ColumnType.getColumnTypeByJava(currField.getMappingFieldType());
        ColumnType columnType = this.getColumnType(keyColumnType);
        if (columnType == null) {
            throw new IllegalArgumentException(I18n.print("platform_executor_empty_type", currField.getMappingFieldType().getSimpleName()));
        }

        if (currField.isMappingFieldTimeForUpdate()) return columnEditTypes;
        KeyColumnType pkAutoType = this.getAutoIncrementPrimaryKeyType();
        if (currField.isMappingFieldPrimaryKey()
                && currField.isMappingAutoIncrement()
                && pkAutoType != null) {
            if (!this.compareColumnChangeType(columnStructure, columnType)) {
                columnEditTypes.add(ColumnEditType.TYPE);
            }
        } else {
            if (this.compareColumnChangeType(columnStructure, columnType)) {
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
            // 自增主键不需要设置默认值
            if (!currField.isMappingAutoIncrement() && (defB == null || !defB.equals("$'clob_get_error'"))) {
                if (!(StringTools.isEmpty(defA) && StringTools.isEmpty(defB))
                        && !(StringTools.isEmpty(defA) && "0".equals(defB))) {  // int bigint 等 数据库默认值总是0
                    if ((StringTools.isNotEmpty(defA) && !defA.equals(defB)) || (StringTools.isNotEmpty(defB) && !defB.equals(defA))) {
                        boolean last = this.compareColumnChangeDefault(defA, defB);
                        if (!last) {
                            columnEditTypes.add(ColumnEditType.DEF_VALUE);
                        }
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

    public abstract SQLBuilderCombine insert(StampInsert insert) throws SQLException;

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
                return this.defineCreateTable(definition);
            }
            if (type == DataDefinitionType.DROP_TABLE) {
                return this.defineDropTable(definition);
            }
            if (type == DataDefinitionType.ADD_COLUMN) {
                return this.defineAddColumn(definition);
            }
            if (type == DataDefinitionType.MODIFY_COLUMN) {
                return this.defineModifyColumn(definition);
            }
            if (type == DataDefinitionType.DROP_COLUMN) {
                return this.defineDropColumn(definition);
            }
            if (type == DataDefinitionType.ADD_INDEX) {
                return this.defineAddIndex(definition);
            }
            if (type == DataDefinitionType.MODIFY_INDEX) {
                return this.defineModifyIndex(definition);
            }
            if (type == DataDefinitionType.DROP_INDEX) {
                return this.defineDropIndex(definition);
            }
        }

        return DialectNextStep.NONE;
    }

    protected void defineCreateTableExtra(StampCreate create, MappingTable mappingTable) {

    }

    protected DialectNextStep defineCreateTable(DataDefinition definition) throws SQLException {
        MappingTable mappingTable = definition.getMappingTable();
        StampCreate stampCreate = this.commonCreateTable(mappingTable);
        this.defineCreateTableExtra(stampCreate, mappingTable);
        this.runner(stampCreate);
        Set<MappingField> mappingFields = mappingTable.getMappingFields();
        for (MappingField mappingField : mappingFields) {
            if (mappingField.isMappingFieldUnique() || mappingField.isMappingFieldIndex()) {
                this.triggerIndex(definition.getMappingTable(),
                        null, mappingField, null);
            }
        }
        return DialectNextStep.NONE;
    }

    protected DialectNextStep defineDropTable(DataDefinition definition) throws SQLException {
        TableStructure tableStructure = definition.getTableStructure();
        StampDrop stampDrop = this.commonDropTable(tableStructure);
        this.runner(stampDrop);
        return DialectNextStep.NONE;
    }

    protected DialectNextStep defineAddColumn(DataDefinition definition) throws SQLException {
        MappingTable mappingTable = definition.getMappingTable();
        MappingField mappingField = definition.getMappingField();
        String tableName = mappingTable.getMappingTableName();
        String columnName = mappingField.getMappingColumnName();
        try {
            TableStructure structure = definition.getTableStructure();
            List<TableConstraintStructure> pks = structure.getPrimaryKey();
            List<TableColumnStructure> autos = structure.getAutoIncrement();
            if ((pks != null && pks.size() > 0 && mappingField.isMappingFieldPrimaryKey())
                    || (autos != null && autos.size() > 0 && mappingField.isMappingAutoIncrement())) {
                return DialectNextStep.REBUILD;
            } else {
                String def = mappingField.getMappingFieldDefaultValue();
                boolean nullable = mappingField.isMappingFieldNullable();

                boolean setDefault = false;
                if (!nullable && StringTools.isEmpty(def)) {
                    this.defineAddColumnNotNullDefault(mappingField);
                    setDefault = true;
                }

                try {
                    StampAlter stampAlter = this.commonAddColumn(definition.getMappingTable(), mappingField);
                    this.runner(stampAlter);

                    if (setDefault) {
                        this.defineAddColumnDefaultNull(tableName, columnName);
                    }
                } finally {
                    if (setDefault) {
                        ((SpecificMappingField) mappingField).setMappingFieldDefaultValue(null);
                    }
                }

                this.triggerIndex(definition.getMappingTable(), definition.getTableStructure(),
                        definition.getMappingField(), null);
            }
            return DialectNextStep.NONE;
        } catch (Exception e) {
            logger.error(I18n.print("dialect_add_column_error", tableName, columnName), e);
            return DialectNextStep.REBUILD;
        }
    }

    protected void defineAddColumnNotNullDefault(MappingField mappingField) throws SQLException {
        KeyColumnType type = JavaType2ColumnType.getColumnTypeByJava(mappingField.getMappingFieldType());
        if (JavaType2ColumnType.isNumber(type) || JavaType2ColumnType.isBoolean(type))
            ((SpecificMappingField) mappingField).setMappingFieldDefaultValue("0");
        else if (JavaType2ColumnType.isTime(type))
            ((SpecificMappingField) mappingField)
                    .setMappingFieldDefaultValue(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));
        else
            ((SpecificMappingField) mappingField).setMappingFieldDefaultValue("");

    }

    protected void defineAddColumnDefaultNull(String tableName, String columnName) throws SQLException {
        DefaultSQLAlterBuilder alterBuilder = new DefaultSQLAlterBuilder();
        alterBuilder.alter().table(tableName).modify().column(columnName).defaultValue("*****");
        this.runner(alterBuilder.compile());
    }

    protected DialectNextStep defineModifyColumn(DataDefinition definition) throws SQLException {
        return DialectNextStep.REBUILD;
    }

    protected DialectNextStep defineDropColumn(DataDefinition definition) throws SQLException {
        TableStructure tableStructure = definition.getTableStructure();
        List<TableConstraintStructure> constraintStructures = tableStructure.getPrimaryKey();

        TableColumnStructure columnStructure = definition.getColumnStructure();
        if (constraintStructures != null && constraintStructures.size() > 0) {
            // 如果删除的列是主键,且当前主键是多列主键,则需要重建表
            for (TableConstraintStructure structure : constraintStructures) {
                if (structure.getColumnName().equals(columnStructure.getColumnName())) {
                    return DialectNextStep.REBUILD;
                }
            }
        }
        StampAlter stampAlter = this.commonDropColumn(definition.getMappingTable(), columnStructure);
        this.runner(stampAlter);
        if (columnStructure != null) {
            columnStructure.setState(2);
        }
        this.triggerIndex(definition.getMappingTable(), definition.getTableStructure(),
                definition.getMappingField(), definition.getColumnStructure());

        return DialectNextStep.NONE;
    }

    protected DialectNextStep defineAddIndex(DataDefinition definition) throws SQLException {
        StampCreate sql = this.commonAddIndex(definition.getMappingTable(), definition.getMappingIndex());
        this.runner(sql);
        return DialectNextStep.NONE;
    }

    protected DialectNextStep defineModifyIndex(DataDefinition definition) throws SQLException {
        StampDrop stampDrop = this.commonDropIndex(definition.getMappingTable(), definition.getIndexName());
        this.runner(stampDrop);
        StampCreate sql = this.commonAddIndex(definition.getMappingTable(), definition.getMappingIndex());
        this.runner(sql);
        return DialectNextStep.NONE;
    }

    protected DialectNextStep defineDropIndex(DataDefinition definition) throws SQLException {
        StampDrop stampDrop = this.commonDropIndex(definition.getMappingTable(), definition.getIndexName());
        this.runner(stampDrop);
        return DialectNextStep.NONE;
    }

    /**
     * 是否需要重建表,如果define方法返回要求重建表
     * 则根据配置的需要是否调用并重建表
     *
     * @throws SQLException
     */
    public void rebuildTable(List<TableStructure> structures,
                             MappingTable mappingTable,
                             TableStructure tableStructure) throws SQLException {
        if (mappingTable != null && tableStructure != null) {
            String tableName = mappingTable.getMappingTableName() + "_ctmp";

            // 1.先修改原表名称
            if (structures != null) {
                for (TableStructure structure : structures) {
                    if (structure.getTableName().equalsIgnoreCase(tableName)) {
                        // 如果发现有同名的表现删除
                        DefaultSQLDropBuilder dropBuilder = new DefaultSQLDropBuilder();
                        dropBuilder.drop().table().name(tableName);
                        this.runner(dropBuilder.compile());
                    }
                }
            }
            DefaultSQLAlterBuilder renameBuilder = new DefaultSQLAlterBuilder();
            renameBuilder.alter().table(mappingTable.getMappingTableName()).rename().name(tableName);
            this.runner(renameBuilder.compile());

            // 2.再创建新表
            this.rebuildStartTable(mappingTable, mappingTable.getMappingTableName());
            DefaultSQLInsertBuilder insertBuilder = new DefaultSQLInsertBuilder();
            insertBuilder.insert().into().table(mappingTable.getMappingTableName());

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
            selectBuilder.select().fields(cols.toArray(new String[]{})).from().table(tableName);
            insertBuilder.select(selectBuilder);
            try {
                // 3.再拷贝数据
                this.runner(insertBuilder.compile());
            } catch (Exception e) {
                logger.error(I18n.print("copy_table_data_error",
                        mappingTable.getMappingTableName(),
                        e.getMessage()));
            }

            try {
                // 最后删除原表
                DefaultSQLDropBuilder dropBuilder = new DefaultSQLDropBuilder();
                dropBuilder.drop().table().name(tableName);
                this.runner(dropBuilder.compile());
            } catch (Exception e) {
                throw new IllegalArgumentException(I18n.print("dialect_reset_table_error",
                        mappingTable.getMappingTableName(),
                        e.getMessage()), e);
            }

            this.rebuildEndTable(mappingTable, tableStructure);

            Set<MappingField> mappingFields = mappingTable.getMappingFields();
            for (MappingField mappingField : mappingFields) {
                if (mappingField.isMappingFieldUnique() || mappingField.isMappingFieldIndex()) {
                    this.triggerIndex(mappingTable,
                            null, mappingField, null);
                }
            }
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

    /**
     * 是否查询分页时必须要排序字段
     * 大部分数据库都不是必须的 sqlserver 必须要
     *
     * @return
     */
    public boolean isSelectLimitMustOrderBy() {
        return false;
    }
}
