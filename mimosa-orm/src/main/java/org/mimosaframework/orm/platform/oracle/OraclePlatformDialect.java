package org.mimosaframework.orm.platform.oracle;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.mimosaframework.core.json.ModelObject;
import org.mimosaframework.orm.platform.*;
import org.mimosaframework.orm.sql.stamp.*;

import java.sql.SQLException;
import java.util.List;

public class OraclePlatformDialect extends PlatformDialect {
    private static final Log logger = LogFactory.getLog(OraclePlatformDialect.class);
    private OracleStampBuilder builder = new OracleStampBuilder();

    public OraclePlatformDialect() {
        registerColumnType(KeyColumnType.INT, "NUMBER", 10, ColumnCompareType.SELF);
        registerColumnType(KeyColumnType.VARCHAR, "VARCHAR2", ColumnCompareType.JAVA);
        registerColumnType(KeyColumnType.CHAR, "CHAR", ColumnCompareType.JAVA);
        registerColumnType(KeyColumnType.TINYINT, "NUMBER", 3, ColumnCompareType.SELF);
        registerColumnType(KeyColumnType.SMALLINT, "NUMBER", 5, ColumnCompareType.SELF);
        registerColumnType(KeyColumnType.BIGINT, "NUMBER", 19, ColumnCompareType.SELF);
        registerColumnType(KeyColumnType.FLOAT, "FLOAT");
        registerColumnType(KeyColumnType.DOUBLE, "FLOAT", 24, ColumnCompareType.SELF);
        registerColumnType(KeyColumnType.DECIMAL, "NUMBER", ColumnCompareType.JAVA);
        registerColumnType(KeyColumnType.BOOLEAN, "NUMBER", 1, ColumnCompareType.SELF);
        registerColumnType(KeyColumnType.DATE, "DATE");
        registerColumnType(KeyColumnType.TIME, "DATE");
        registerColumnType(KeyColumnType.DATETIME, "DATE");
        registerColumnType(KeyColumnType.TIMESTAMP, "DATE");

        registerColumnType(KeyColumnType.BLOB, "BLOB");
        registerColumnType(KeyColumnType.MEDIUMBLOB, "BLOB");
        registerColumnType(KeyColumnType.LONGBLOB, "BLOB");
        registerColumnType(KeyColumnType.TEXT, "CLOB");
        registerColumnType(KeyColumnType.JSON, "CLOB");
        registerColumnType(KeyColumnType.MEDIUMTEXT, "CLOB");
        registerColumnType(KeyColumnType.LONGTEXT, "CLOB");
    }

    public List<TableStructure> getTableStructures(List<String> classTableNames) throws SQLException {
        return this.loadingTableStructures(classTableNames);
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
    public SQLBuilderCombine insert(StampInsert insert) throws SQLException {
        if (insert.autoField != null && insert.autoField.type == 0) {
            StampColumn[] columns = insert.columns;
            boolean isContain = false;
            if (columns != null) {
                for (StampColumn column : columns) {
                    if (column.column.equals(insert.autoField.columnName)) {
                        isContain = true;
                    }
                }
            }
            if (!isContain) {
                StampColumn[] newColumns = new StampColumn[columns.length + 1];
                newColumns[0] = new StampColumn(insert.autoField.columnName);
                int i = 1;
                for (StampColumn column : columns) {
                    newColumns[i] = column;
                    i++;
                }

                insert.columns = newColumns;

                Object[][] values = insert.values;
                for (int j = 0; j < values.length; j++) {
                    List<ModelObject> objects = (List<ModelObject>)
                            this.runner(new JDBCTraversing("SELECT " + insert.tableName.toUpperCase() + "_SEQ.NEXTVAL AS ID FROM DUAL", null));

                    long seq = 0;
                    if (objects != null && objects.size() > 0) {
                        ModelObject id = objects.get(0);
                        seq = id.getLong("ID");
                    }

                    Object[] value = values[j];
                    Object[] newValue = new Object[value == null ? 1 : value.length + 1];
                    newValue[0] = seq;
                    int m = 1;
                    if (value != null) {
                        for (Object v : value) {
                            newValue[m] = v;
                            m++;
                        }
                    }

                    values[j] = newValue;
                }
            }
        }
        StampCombineBuilder builder = this.builder.insert();
        SQLBuilderCombine combine = builder.getSqlBuilder(this.mappingGlobalWrapper, insert);
        return combine;
    }

    @Override
    public boolean isSupportSameColumnIndex() {
        return false;
    }

    @Override
    public boolean isSupportDuplicateKeyUpdate() {
        return false;
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
        List<ColumnEditType> types = this.compareColumnChange(definition.getTableStructure(),
                definition.getMappingField(), definition.getColumnStructure());
        if (types.size() == 1 && types.get(0).equals(ColumnEditType.AUTO_INCREMENT)) {
            // oracle 没有自增列相关，所以不用修改字段且无需重建
            return DialectNextStep.NONE;
        }
        return super.defineModifyColumn(definition);
    }

    @Override
    public boolean isSupportGeneratedKeys() {
        return false;
    }
}
