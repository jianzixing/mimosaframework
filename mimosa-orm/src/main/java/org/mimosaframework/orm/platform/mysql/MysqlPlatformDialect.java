package org.mimosaframework.orm.platform.mysql;

import org.mimosaframework.core.utils.StringTools;
import org.mimosaframework.orm.mapping.MappingField;
import org.mimosaframework.orm.mapping.MappingTable;
import org.mimosaframework.orm.platform.*;
import org.mimosaframework.orm.sql.alter.DefaultSQLAlterBuilder;
import org.mimosaframework.orm.sql.stamp.*;

import java.sql.SQLException;
import java.util.List;

public class MysqlPlatformDialect extends PlatformDialect {
    private MysqlStampBuilder builder = new MysqlStampBuilder();

    public MysqlPlatformDialect() {
        registerColumnType(KeyColumnType.INT, "INT");
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
        registerColumnType(KeyColumnType.JSON, "JSON");
        registerColumnType(KeyColumnType.MEDIUMTEXT, "MEDIUMTEXT");
        registerColumnType(KeyColumnType.LONGTEXT, "LONGTEXT");
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
        StampCombineBuilder builder = this.builder.save();
        SQLBuilderCombine insertCombine = builder.getSqlBuilder(this.mappingGlobalWrapper, insert);
        return insertCombine;
    }

    protected DialectNextStep defineModifyColumn(DataDefinition definition) throws SQLException {
        TableStructure tableStructure = definition.getTableStructure();
        MappingTable mappingTable = definition.getMappingTable();
        MappingField mappingField = definition.getMappingField();
        TableColumnStructure columnStructure = definition.getColumnStructure();

        ColumnType columnType = this.getColumnType(JavaType2ColumnType
                .getColumnTypeByJava(mappingField.getMappingFieldType()));

        String tableName = mappingTable.getMappingTableName();
        String columnName = mappingField.getMappingColumnName();

        List<ColumnEditType> columnEditTypes = this.compareColumnChange(tableStructure, mappingField, columnStructure);

        int hasEdit = columnEditTypes.size();
        if (columnEditTypes.contains(ColumnEditType.TYPE_LENGTH)
                && mappingField.getMappingFieldLength() >= columnStructure.getLength()) {
            columnEditTypes.remove(ColumnEditType.TYPE_LENGTH);
        }
        columnEditTypes.remove(ColumnEditType.DEF_VALUE);
        columnEditTypes.remove(ColumnEditType.COMMENT);
        columnEditTypes.remove(ColumnEditType.AUTO_INCREMENT);

        if (hasEdit > 0 && columnEditTypes.isEmpty()) {
            DefaultSQLAlterBuilder sql = new DefaultSQLAlterBuilder();
            sql.alter().table(tableName).modify().column(columnName);
            this.setSQLType(sql, mappingField.getMappingFieldType(),
                    mappingField.getMappingFieldLength(), mappingField.getMappingFieldDecimalDigits());
            if (!mappingField.isMappingFieldNullable()) {
                sql.not();
                sql.nullable();
            }
            // 修改表时，不修改主键信息
            // List<MappingField> pks = mappingTable.getMappingPrimaryKeyFields();
            // if (pks != null && pks.size() == 1 && mappingField.isMappingFieldPrimaryKey()) {
            //     sql.primary().key();
            // }

            if (mappingField.isMappingAutoIncrement()) {
                sql.autoIncrement();
            }

            String def = mappingField.getMappingFieldDefaultValue();
            if (StringTools.isNotEmpty(def)) {
                sql.defaultValue(def);
            }

            String cmt = mappingField.getMappingFieldComment();
            if (StringTools.isNotEmpty(cmt)) {
                sql.comment(cmt);
            }

            this.runner(sql.compile());

            this.triggerIndex(definition.getMappingTable(), definition.getTableStructure(),
                    definition.getMappingField(), null);
            return DialectNextStep.NONE;
        } else {
            return DialectNextStep.REBUILD;
        }
    }

    @Override
    protected DialectNextStep defineAddColumn(DataDefinition definition) throws SQLException {
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
        return DialectNextStep.NONE;
    }

    @Override
    protected void defineCreateTableExtra(StampCreate create, MappingTable mappingTable) {
        if (StringTools.isNotEmpty(mappingTable.getEngineName())) {
            create.extra = "ENGINE=" + mappingTable.getEngineName();
        } else {
            create.extra = "ENGINE=InnoDB";
        }
    }

    @Override
    public boolean isSupportGeneratedKeys() {
        return true;
    }

    @Override
    public boolean isSupportDuplicateKeyUpdate() {
        return true;
    }
}
