package org.mimosaframework.orm.platform.mysql;

import org.mimosaframework.core.utils.StringTools;
import org.mimosaframework.orm.mapping.MappingField;
import org.mimosaframework.orm.mapping.MappingTable;
import org.mimosaframework.orm.platform.*;
import org.mimosaframework.orm.sql.alter.AlterFactory;
import org.mimosaframework.orm.sql.alter.DefaultSQLAlterBuilder;
import org.mimosaframework.orm.sql.stamp.*;

import java.sql.SQLException;

public class MysqlPlatformDialect extends PlatformDialect {
    public MysqlPlatformDialect() {
        registerColumnType(KeyColumnType.INT, "INT");
        registerColumnType(KeyColumnType.VARCHAR, "VARCHAR");
        registerColumnType(KeyColumnType.CHAR, "CHAR");
        registerColumnType(KeyColumnType.BLOB, "BLOB");
        registerColumnType(KeyColumnType.TEXT, "TEXT");
        registerColumnType(KeyColumnType.TINYINT, "TINYINT");
        registerColumnType(KeyColumnType.SMALLINT, "SMALLINT");
        registerColumnType(KeyColumnType.BIGINT, "BIGINT");
        registerColumnType(KeyColumnType.FLOAT, "FLOAT");
        registerColumnType(KeyColumnType.DOUBLE, "DOUBLE");
        registerColumnType(KeyColumnType.DECIMAL, "DECIMAL");
        registerColumnType(KeyColumnType.BOOLEAN, "BOOLEAN");
        registerColumnType(KeyColumnType.DATE, "DATE");
        registerColumnType(KeyColumnType.TIME, "TIME");
        registerColumnType(KeyColumnType.DATETIME, "DATETIME");
        registerColumnType(KeyColumnType.TIMESTAMP, "TIMESTAMP");
    }

    @Override
    public SQLBuilderCombine alter(StampAlter alter) {
        StampCombineBuilder builder = new MysqlStampAlter();
        SQLBuilderCombine combine = builder.getSqlBuilder(this.mappingGlobalWrapper, alter);
        return combine;
    }

    @Override
    public SQLBuilderCombine create(StampCreate create) {
        StampCombineBuilder builder = new MysqlStampCreate();
        SQLBuilderCombine combine = builder.getSqlBuilder(this.mappingGlobalWrapper, create);
        return combine;
    }

    @Override
    public SQLBuilderCombine drop(StampDrop drop) {
        StampCombineBuilder builder = new MysqlStampDrop();
        SQLBuilderCombine combine = builder.getSqlBuilder(this.mappingGlobalWrapper, drop);
        return combine;
    }

    @Override
    public SQLBuilderCombine insert(StampInsert insert) {
        StampCombineBuilder builder = new MysqlStampInsert();
        SQLBuilderCombine combine = builder.getSqlBuilder(this.mappingGlobalWrapper, insert);
        return combine;
    }

    @Override
    public SQLBuilderCombine delete(StampDelete delete) {
        StampCombineBuilder builder = new MysqlStampDelete();
        SQLBuilderCombine combine = builder.getSqlBuilder(this.mappingGlobalWrapper, delete);
        return combine;
    }

    @Override
    public SQLBuilderCombine select(StampSelect select) {
        StampCombineBuilder builder = new MysqlStampSelect();
        SQLBuilderCombine combine = builder.getSqlBuilder(this.mappingGlobalWrapper, select);
        return combine;
    }

    @Override
    public SQLBuilderCombine update(StampUpdate update) {
        StampCombineBuilder builder = new MysqlStampUpdate();
        SQLBuilderCombine combine = builder.getSqlBuilder(this.mappingGlobalWrapper, update);
        return combine;
    }

    @Override
    public void define(DataDefinition definition) throws SQLException {
        if (definition != null) {
            DataDefinitionType type = definition.getType();
            if (type == DataDefinitionType.CREATE_TABLE) {
                MappingTable mappingTable = definition.getMappingTable();
                StampCreate stampCreate = this.commonCreateTable(mappingTable);
                if (StringTools.isNotEmpty(mappingTable.getEngineName())) {
                    stampCreate.extra = "ENGINE=InnoDB";
                }
                this.runner(stampCreate);
            }
            if (type == DataDefinitionType.DROP_TABLE) {
                TableStructure tableStructure = definition.getTableStructure();
                StampDrop stampDrop = this.commonDropTable(tableStructure);
                this.runner(stampDrop);
            }
            if (type == DataDefinitionType.ADD_COLUMN) {
                StampAlter stampAlter = this.commonAddColumn(definition.getMappingTable(), definition.getMappingField());
                this.runner(stampAlter);
            }
            if (type == DataDefinitionType.MODIFY_COLUMN) {
                TableStructure tableStructure = definition.getTableStructure();
                MappingTable mappingTable = definition.getMappingTable();
                MappingField mappingField = definition.getMappingField();
                TableColumnStructure columnStructure = definition.getColumnStructure();
                ColumnType columnType = this.getColumnType(JavaType2ColumnType
                        .getColumnTypeByJava(mappingField.getMappingFieldType()));

                String tableName = mappingTable.getMappingTableName();
                String columnName = mappingField.getMappingColumnName();
                DefaultSQLAlterBuilder sql = (DefaultSQLAlterBuilder) AlterFactory.alter().table(tableName)
                        .modify().column(columnName);

                if (columnStructure.getTypeName()
                        .equalsIgnoreCase(columnType.getTypeName())
                        || columnStructure.getLength() != mappingField.getMappingFieldLength()
                        || columnStructure.getScale() != mappingField.getDatabaseColumnDecimalDigits()) {
                    // ColumnEditType.TYPE
                    this.setSQLType(sql, mappingField.getMappingFieldType(),
                            mappingField.getMappingFieldLength(), mappingField.getMappingFieldDecimalDigits());
                }

                if (StringTools.isEmpty(columnStructure.getIsNullable()) != mappingField.isMappingFieldNullable()
                        || columnStructure.isNullable() != mappingField.isMappingFieldNullable()) {
                    // ColumnEditType.ISNULL
                    if (!mappingField.isMappingFieldNullable()) {
                        sql.not();
                        sql.nullable();
                    }
                }

                if (mappingField.isMappingFieldPrimaryKey() == tableStructure.isPrimaryKeyColumn(columnStructure.getColumnName())) {
                    // ColumnEditType.PRIMARY_KEY
                    if (mappingField.isMappingFieldPrimaryKey()) {
                        
                    }
                }

                if ((StringTools.isEmpty(columnStructure.getDefaultValue()) && StringTools.isNotEmpty(mappingField.getMappingFieldDefaultValue()))
                        || (StringTools.isNotEmpty(columnStructure.getDefaultValue())
                        && !columnStructure.getDefaultValue().equals(mappingField.getMappingFieldDefaultValue()))) {
                    // ColumnEditType.DEF_VALUE
                    if (StringTools.isNotEmpty(mappingField.getMappingFieldDefaultValue())) {
                        sql.defaultValue(mappingField.getMappingFieldDefaultValue());
                    }
                }
                if (StringTools.isNotEmpty(columnStructure.getAutoIncrement()) == mappingField.isMappingAutoIncrement()
                        || !columnStructure.isAutoIncrement() != mappingField.isMappingAutoIncrement()) {
                    // ColumnEditType.AUTO_INCREMENT
                    if (mappingField.isMappingAutoIncrement()) {
                        sql.autoIncrement();
                    }
                }
                if ((StringTools.isEmpty(columnStructure.getComment()) && StringTools.isNotEmpty(mappingField.getMappingFieldComment()))
                        || (StringTools.isNotEmpty(columnStructure.getComment())
                        && !columnStructure.getComment().equals(mappingField.getMappingFieldComment()))) {
                    // ColumnEditType.COMMENT
                    if (StringTools.isNotEmpty(mappingField.getMappingFieldComment())) {
                        sql.comment(mappingField.getMappingFieldComment());
                    }
                }
            }
            if (type == DataDefinitionType.DROP_COLUMN) {
                StampAlter stampAlter = this.commonDropColumn(definition.getMappingTable(), definition.getColumnStructure());
                this.runner(stampAlter);
            }
            if (type == DataDefinitionType.ADD_INDEX) {
                StampCreate sql = this.commonAddIndex(definition.getMappingTable(), definition.getMappingIndex());
                this.runner(sql);
            }
            if (type == DataDefinitionType.MODIFY_INDEX) {
                StampDrop stampDrop = this.commonDropIndex(definition.getMappingTable(), definition.getIndexStructure());
                this.runner(stampDrop);
                StampCreate sql = this.commonAddIndex(definition.getMappingTable(), definition.getMappingIndex());
                this.runner(sql);
            }
            if (type == DataDefinitionType.DROP_INDEX) {
                StampDrop stampDrop = this.commonDropIndex(definition.getMappingTable(), definition.getIndexStructure());
                this.runner(stampDrop);
            }
        }
    }
}
