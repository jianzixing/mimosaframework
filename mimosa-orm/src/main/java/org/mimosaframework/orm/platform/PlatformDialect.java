package org.mimosaframework.orm.platform;

import org.mimosaframework.core.json.ModelObject;
import org.mimosaframework.core.utils.StringTools;
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
import org.mimosaframework.orm.sql.stamp.*;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.*;

public abstract class PlatformDialect {
    private Map<KeyColumnType, ColumnType> columnTypes = new HashMap<>();
    private DataSourceWrapper dataSourceWrapper;
    private DBRunner runner = null;
    protected MappingGlobalWrapper mappingGlobalWrapper;

    protected void registerColumnType(KeyColumnType type, String typeName) {
        this.columnTypes.put(type, new ColumnType(type, typeName, -1, -1));
    }

    protected void registerColumnType(KeyColumnType type, String typeName, int length) {
        this.columnTypes.put(type, new ColumnType(type, typeName, length, -1));
    }

    protected void registerColumnType(KeyColumnType type, String typeName, int length, int scale) {
        this.columnTypes.put(type, new ColumnType(type, typeName, length, scale));
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
                Object resultColumn = this.runner(column);
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
    public String getCatalogAndSchema() throws SQLException {
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
        Class table = mappingTable.getMappingClass();
        Set<MappingField> mappingFields = mappingTable.getMappingFields();
        if (mappingFields != null && mappingFields.size() > 0) {
            DefaultSQLCreateBuilder sql = new DefaultSQLCreateBuilder();
            sql.create().table().ifNotExist().name(table);
            for (MappingField mappingField : mappingFields) {
                String field = mappingField.getMappingColumnName();
                Class type = mappingField.getMappingFieldType();
                int length = mappingField.getMappingFieldLength();
                int scale = mappingField.getMappingFieldDecimalDigits();
                String defVal = mappingField.getMappingFieldDefaultValue();
                boolean nullable = mappingField.isMappingFieldNullable();
                boolean autoIncr = mappingField.isMappingAutoIncrement();
                boolean timeForUpdate = mappingField.isMappingFieldTimeForUpdate();
                String comment = mappingField.getMappingFieldComment();

                sql.column(field);
                this.setSQLType(sql, type, length, scale);
                if (!nullable) {
                    sql.not();
                    sql.nullable();
                }
                if (autoIncr) sql.autoIncrement();
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
        }
        return null;
    }

    protected void setSQLType(ColumnTypeBuilder typeBuilder,
                              Class type,
                              int length,
                              int scale) {
        JavaType2ColumnType.getColumnTypeByJava(type, typeBuilder, length, scale);
    }

    protected List<MappingField> getPrimaryKeyByMappingTable(MappingTable mappingTable) {
        return mappingTable.getMappingPrimaryKeyFields();
    }

    protected boolean isSamePrimaryKey(MappingTable mappingTable, TableStructure structure) {
        List<MappingField> mappingFields = this.getPrimaryKeyByMappingTable(mappingTable);
        List<TableConstraintStructure> constraintStructures = structure.getConstraintStructures();
        if (mappingFields.size() == constraintStructures.size()) {
            for (MappingField mappingField : mappingFields) {
                boolean is = false;
                for (TableConstraintStructure cs : constraintStructures) {
                    if (mappingField.getMappingColumnName().equalsIgnoreCase(cs.getColumnName())) {
                        is = true;
                        break;
                    }
                }
                if (!is) {
                    return false;
                }
            }
        }
        return false;
    }

    protected boolean isSameAutoIncrement(MappingTable mappingTable, TableStructure tableStructure) {
        Set<MappingField> mappingFields = mappingTable.getMappingFields();
        List<TableColumnStructure> columnStructures = tableStructure.getColumnStructures();
        for (MappingField mappingField : mappingFields) {
            for (TableColumnStructure columnStructure : columnStructures) {
                if (mappingField.isMappingAutoIncrement()
                        && columnStructure.isAutoIncrement()
                        && mappingField.getMappingColumnName().equalsIgnoreCase(columnStructure.getColumnName())) {
                    return true;
                }
            }
        }
        return false;
    }

    protected StampDrop commonDropTable(TableStructure structure) {
        DefaultSQLDropBuilder sql = new DefaultSQLDropBuilder();
        return (StampDrop) sql.drop().table().table(structure.getTableName()).compile();
    }

    protected StampAlter commonAddColumn(MappingTable mappingTable, MappingField mappingField) {
        String field = mappingField.getMappingColumnName();
        Class type = mappingField.getMappingFieldType();
        int length = mappingField.getMappingFieldLength();
        int scale = mappingField.getMappingFieldDecimalDigits();
        String defVal = mappingField.getMappingFieldDefaultValue();
        boolean pk = mappingField.isMappingFieldPrimaryKey();
        boolean nullable = mappingField.isMappingFieldNullable();
        boolean autoIncr = mappingField.isMappingAutoIncrement();
        String comment = mappingField.getMappingFieldComment();

        DefaultSQLAlterBuilder sql = new DefaultSQLAlterBuilder();
        sql.alter().table(mappingTable.getMappingClass()).add().column(field);

        this.setSQLType(sql, type, length, scale);
        if (!nullable) {
            sql.not();
            sql.nullable();
        }
        if (pk && this.getPrimaryKeyByMappingTable(mappingTable).size() == 1) {
            sql.primary();
            sql.key();
        }
        if (autoIncr) sql.autoIncrement();
        if (StringTools.isNotEmpty(defVal)) {
            sql.defaultValue(defVal);
        }
        if (StringTools.isNotEmpty(comment)) {
            sql.comment(comment);
        }

        return (StampAlter) sql.compile();
    }

    protected StampAlter commonDropColumn(MappingTable mappingTable, TableColumnStructure structure) {
        DefaultSQLAlterBuilder sql = new DefaultSQLAlterBuilder();
        return (StampAlter) sql.alter().table(mappingTable.getMappingTableName())
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
        StampCreate sql = (StampCreate) sqlCreateBuilder.create()
                .index().name(indexName).on().table(tableName).columns(columns).compile();
        return sql;
    }

    protected StampDrop commonDropIndex(MappingTable mappingTable, String indexName) {
        String tableName = mappingTable.getMappingTableName();
        DefaultSQLDropBuilder sql = new DefaultSQLDropBuilder();
        return (StampDrop) sql.drop().index().name(indexName).on().table(tableName).compile();
    }

    protected void triggerIncrAndKeys(DataDefinitionType type,
                                      MappingTable mappingTable,
                                      TableStructure tableStructure,
                                      MappingField mappingField,
                                      TableColumnStructure columnStructure) {

        if (type == DataDefinitionType.ADD_COLUMN || type == DataDefinitionType.MODIFY_INDEX) {
            if (mappingTable != null && tableStructure != null) {
                boolean isSamePK = this.isSamePrimaryKey(mappingTable, tableStructure);
                if (!isSamePK) {
                    // 重新修改主键
                    this.rebuildPrimaryKey(mappingTable, tableStructure);
                }
                boolean isSameIncr = this.isSameAutoIncrement(mappingTable, tableStructure);
                if (!isSameIncr) {
                    // 重新修改自增列
                    this.rebuildAutoIncrement(mappingTable, tableStructure);
                }
            }
        }

        if (type == DataDefinitionType.MODIFY_COLUMN
                || type == DataDefinitionType.ADD_COLUMN
                || type == DataDefinitionType.CREATE_TABLE) {
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
    }

    /**
     * 修改删除表字段时可能需要重新创建主键
     *
     * @param mappingTable
     * @param tableStructure
     */
    protected void rebuildPrimaryKey(MappingTable mappingTable, TableStructure tableStructure) {

    }

    /**
     * 修改删除表字段时可能需要重新创建自增列
     *
     * @param mappingTable
     * @param tableStructure
     */
    protected void rebuildAutoIncrement(MappingTable mappingTable, TableStructure tableStructure) {

    }

    /**
     * 修改删除表字段时可能需要重新创建单列索引
     *
     * @param mappingTable
     * @param mappingField
     * @param unique
     */
    protected void createIndex(MappingTable mappingTable, MappingField mappingField, boolean unique) {

    }

    /**
     * 修改删除表字段时可能需要重新删除单列索引
     *
     * @param mappingTable
     * @param mappingField
     */
    protected void dropIndex(MappingTable mappingTable, MappingField mappingField) {

    }

    public abstract SQLBuilderCombine alter(StampAlter alter);

    public abstract SQLBuilderCombine create(StampCreate create);

    public abstract SQLBuilderCombine drop(StampDrop drop);

    public abstract SQLBuilderCombine insert(StampInsert insert);

    public abstract SQLBuilderCombine delete(StampDelete delete);

    public abstract SQLBuilderCombine select(StampSelect select);

    public abstract SQLBuilderCombine update(StampUpdate update);

    public abstract void define(DataDefinition definition) throws SQLException;
}
