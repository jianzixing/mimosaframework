package org.mimosaframework.orm.platform.sqlite;

import org.mimosaframework.core.json.ModelObject;
import org.mimosaframework.core.utils.StringTools;
import org.mimosaframework.orm.mapping.MappingField;
import org.mimosaframework.orm.platform.*;
import org.mimosaframework.orm.platform.sqlite.analysis.AnalysisItem;
import org.mimosaframework.orm.platform.sqlite.analysis.AnalysisType;
import org.mimosaframework.orm.platform.sqlite.analysis.SQLAnalysis;
import org.mimosaframework.orm.sql.StructureBuilder;
import org.mimosaframework.orm.sql.stamp.*;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class SqlitePlatformDialect extends PlatformDialect {
    private SqliteStampBuilder builder = new SqliteStampBuilder();

    public SqlitePlatformDialect() {
        registerColumnType(KeyColumnType.INT, "INTEGER");
        registerColumnType(KeyColumnType.VARCHAR, "VARCHAR", ColumnCompareType.JAVA);
        registerColumnType(KeyColumnType.CHAR, "CHAR", ColumnCompareType.JAVA);
        registerColumnType(KeyColumnType.TINYINT, "TINYINT");
        registerColumnType(KeyColumnType.SMALLINT, "SMALLINT");
        registerColumnType(KeyColumnType.BIGINT, "BIGINT");
        registerColumnType(KeyColumnType.FLOAT, "FLOAT");
        registerColumnType(KeyColumnType.DOUBLE, "DOUBLE");
        registerColumnType(KeyColumnType.DECIMAL, "DECIMAL", ColumnCompareType.JAVA);
        registerColumnType(KeyColumnType.BOOLEAN, "TINYINT");
        registerColumnType(KeyColumnType.DATE, "DATE");
        registerColumnType(KeyColumnType.TIME, "TIME");
        registerColumnType(KeyColumnType.DATETIME, "DATETIME");
        registerColumnType(KeyColumnType.TIMESTAMP, "TIMESTAMP");

        registerColumnType(KeyColumnType.BLOB, "BLOB");
        registerColumnType(KeyColumnType.MEDIUMBLOB, "MEDIUMBLOB");
        registerColumnType(KeyColumnType.LONGBLOB, "LONGBLOB");
        registerColumnType(KeyColumnType.TEXT, "TEXT");
        registerColumnType(KeyColumnType.JSON, "TEXT");
        registerColumnType(KeyColumnType.MEDIUMTEXT, "MEDIUMTEXT");
        registerColumnType(KeyColumnType.LONGTEXT, "LONGTEXT");
    }

    public List<TableStructure> getTableStructures(List<String> classTableNames) throws SQLException {
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
                tables.add(tableStructure.getTableName());
            }


            if (tableStructures != null && tableStructures.size() > 0 && tables.size() > 0) {
                List<TableColumnStructure> columnStructures = new ArrayList<>();
                List<TableConstraintStructure> constraintStructures = new ArrayList<>();
                for (ModelObject o : list) {
                    String sql = o.getString("SQL");
                    List<AnalysisItem> items = new SQLAnalysis().analysis(sql);
                    int c = 0;
                    List<AnalysisItem> fields = null;
                    String tableName = null;
                    for (int j = 0; j < items.size(); j++) {
                        AnalysisItem item = items.get(j);
                        if (item.type != AnalysisType.CHILD) {
                            if (item.text.equalsIgnoreCase("create")) c++;
                            if (item.text.equalsIgnoreCase("table")) {
                                c++;
                                tableName = items.get(j + 1).text;
                            }
                        }
                        if (item.type == AnalysisType.CHILD) {
                            fields = item.child;
                            break;
                        }
                    }
                    if (c == 2) {
                        TableColumnStructure columnStructure = new TableColumnStructure();
                        columnStructure.setTableName(tableName);
                        columnStructure.setAutoIncrement("N");
                        columnStructures.add(columnStructure);
                        int index = 0;
                        String columnName = null;
                        for (int total = 0; total < fields.size(); total++) {
                            AnalysisItem item = fields.get(total);
                            if (item.type != AnalysisType.CHILD && item.text.equalsIgnoreCase(",")) {
                                if ((fields.size() > total + 2)
                                        && fields.get(total + 1).text.equalsIgnoreCase("primary")
                                        && fields.get(total + 2).text.equalsIgnoreCase("key")) {

                                } else {
                                    columnStructure = new TableColumnStructure();
                                    columnStructure.setTableName(tableName);
                                    columnStructure.setAutoIncrement("N");
                                    columnStructures.add(columnStructure);
                                }
                                index = 0;
                                columnName = null;
                            } else {
                                if (index == 0 && item.text.equalsIgnoreCase("primary")
                                        && fields.get(total + 1).text.equalsIgnoreCase("key")) {
                                    AnalysisItem keys = fields.get(total + 2);
                                    if (keys.type == AnalysisType.CHILD) {
                                        for (AnalysisItem key : keys.child) {
                                            if (!key.text.equalsIgnoreCase(",")) {
                                                TableConstraintStructure structure = new TableConstraintStructure();
                                                structure.setTableName(tableName);
                                                structure.setColumnName(key.text);
                                                structure.setType("P");
                                                constraintStructures.add(structure);
                                            }
                                        }
                                        index += 2;
                                        total += 2;
                                    }
                                } else {
                                    if (index == 0) {
                                        columnStructure.setColumnName(item.text);
                                        columnName = item.text;
                                    } else if (index == 1) {
                                        columnStructure.setTypeName(item.text);
                                    } else {
                                        if (item.type == AnalysisType.CHILD) {
                                            List<AnalysisItem> lens = item.child;
                                            if (lens.size() == 1) {
                                                columnStructure.setLength(Integer.parseInt(lens.get(0).text));
                                            } else if (lens.size() >= 2) {
                                                columnStructure.setLength(Integer.parseInt(lens.get(0).text));
                                                columnStructure.setScale(Integer.parseInt(lens.get(lens.size() - 1).text));
                                            }
                                        } else if (item.text.equalsIgnoreCase("constraint")) {
                                            String consName = fields.get(total + 1).text;
                                            index += 1;
                                            total += 1;
                                        } else if (item.text.equalsIgnoreCase("references")) {
                                            String foreignTableName = fields.get(total + 1).text;
                                            AnalysisItem foreignColumnName = fields.get(total + 2);

                                            index += 2;
                                            total += 2;
                                        } else if (item.text.equalsIgnoreCase("null")) {
                                            if (fields.get(total - 1).text.equalsIgnoreCase("not")) {
                                                columnStructure.setIsNullable("N");
                                            } else {
                                                columnStructure.setIsNullable("Y");
                                            }
                                        } else if (item.text.equalsIgnoreCase("key")) {
                                            if (fields.get(total - 1).text.equalsIgnoreCase("primary")) {
                                                TableConstraintStructure structure = new TableConstraintStructure();
                                                structure.setTableName(tableName);
                                                structure.setColumnName(columnName);
                                                structure.setType("P");
                                                constraintStructures.add(structure);
                                            } else {

                                            }
                                        } else if (item.text.equalsIgnoreCase("default")) {
                                            String value = fields.get(total + 1).text;
                                            columnStructure.setDefaultValue(value);
                                        } else if (item.text.equalsIgnoreCase("autoincrement")) {
                                            columnStructure.setAutoIncrement("Y");
                                        }
                                    }
                                }
                                index++;
                            }
                        }
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
                        indexStructure.setIndexName(o.getString("name"));
                        indexStructure.setTableName(o.getString("tbl_name"));
                        indexStructure.setType("D");
                        indexStructure.setColumnName(o.getString("COLNAME"));
                        indexStructures.add(indexStructure);

                        String sql = o.getString("sql");
                        if (StringTools.isNotEmpty(sql)) {
                            List<AnalysisItem> items = new SQLAnalysis().analysis(sql);
                            for (AnalysisItem item : items) {
                                if (item.type == AnalysisType.CHILD) {
                                    List<AnalysisItem> fields = item.child;
                                    if (fields != null) {
                                        int m = 0;
                                        for (AnalysisItem f : fields) {
                                            if (m == 0) {
                                                indexStructure.setColumnName(f.text);
                                            } else {
                                                TableIndexStructure newS = indexStructure.clone();
                                                newS.setColumnName(f.text);
                                                indexStructures.add(newS);
                                            }
                                            m++;
                                        }
                                    }
                                } else {
                                    if (item.text.equalsIgnoreCase("unique")) {
                                        indexStructure.setType("U");
                                    }
                                }
                            }
                        }
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

    @Override
    public SQLBuilderCombine alter(StampAlter alter) {
        StampCombineBuilder builder = this.builder.alter();
        SQLBuilderCombine combine = builder.getSqlBuilder(this.mappingGlobalWrapper, alter);
        return combine;
    }

    @Override
    public SQLBuilderCombine rename(StampRename alter) {
        StampCombineBuilder builder = this.builder.rename();
        SQLBuilderCombine combine = builder.getSqlBuilder(this.mappingGlobalWrapper, alter);
        return combine;
    }

    @Override
    public SQLBuilderCombine create(StampCreate create) {
        StampCombineBuilder builder = this.builder.create();
        SQLBuilderCombine combine = builder.getSqlBuilder(this.mappingGlobalWrapper, create);
        return combine;
    }

    @Override
    public SQLBuilderCombine drop(StampDrop drop) {
        StampCombineBuilder builder = this.builder.drop();
        SQLBuilderCombine combine = builder.getSqlBuilder(this.mappingGlobalWrapper, drop);
        return combine;
    }

    @Override
    public SQLBuilderCombine insert(StampInsert insert) {
        StampCombineBuilder builder = this.builder.insert();
        SQLBuilderCombine combine = builder.getSqlBuilder(this.mappingGlobalWrapper, insert);
        return combine;
    }

    @Override
    public SQLBuilderCombine delete(StampDelete delete) {
        StampCombineBuilder builder = this.builder.delete();
        SQLBuilderCombine combine = builder.getSqlBuilder(this.mappingGlobalWrapper, delete);
        return combine;
    }

    @Override
    public SQLBuilderCombine select(StampSelect select) {
        StampCombineBuilder builder = this.builder.select();
        SQLBuilderCombine combine = builder.getSqlBuilder(this.mappingGlobalWrapper, select);
        return combine;
    }

    @Override
    public SQLBuilderCombine update(StampUpdate update) {
        StampCombineBuilder builder = this.builder.update();
        SQLBuilderCombine combine = builder.getSqlBuilder(this.mappingGlobalWrapper, update);
        return combine;
    }

    @Override
    public SQLBuilderCombine save(StampInsert insert) {
        return null;
    }

    @Override
    protected DialectNextStep defineModifyColumn(DataDefinition definition) throws SQLException {
        List<ColumnEditType> columnEditTypes = this.compareColumnChange(definition.getTableStructure(),
                definition.getMappingField(), definition.getColumnStructure());
        columnEditTypes.remove(ColumnEditType.COMMENT);
        if (columnEditTypes.size() > 0) {
            return DialectNextStep.REBUILD;
        }
        return DialectNextStep.NONE;
    }

    protected void defineAddColumnNotNullDefault(MappingField mappingField) throws SQLException {
        // sqlite 不允许添加为空字段且没有默认值
        super.defineAddColumnNotNullDefault(mappingField);
    }

    protected void defineAddColumnDefaultNull(String tableName, String columnName) throws SQLException {

    }

    @Override
    protected KeyColumnType getAutoIncrementPrimaryKeyType() {
        return KeyColumnType.INT;
    }

    @Override
    protected DialectNextStep defineDropColumn(DataDefinition definition) throws SQLException {
        return DialectNextStep.REBUILD;
    }

    @Override
    public boolean isSupportGeneratedKeys() {
        return true;
    }

    @Override
    public boolean isSelectHavingMustGroupBy() {
        return true;
    }

    @Override
    public boolean isSupportDuplicateKeyUpdate() {
        return false;
    }
}
