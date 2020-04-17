package org.mimosaframework.orm.platform.oracle;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.mimosaframework.core.utils.StringTools;
import org.mimosaframework.orm.i18n.I18n;
import org.mimosaframework.orm.mapping.MappingField;
import org.mimosaframework.orm.mapping.MappingTable;
import org.mimosaframework.orm.mapping.SpecificMappingField;
import org.mimosaframework.orm.platform.*;
import org.mimosaframework.orm.platform.mysql.*;
import org.mimosaframework.orm.sql.alter.DefaultSQLAlterBuilder;
import org.mimosaframework.orm.sql.create.CreateFactory;
import org.mimosaframework.orm.sql.drop.DropFactory;
import org.mimosaframework.orm.sql.stamp.*;

import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class OraclePlatformDialect extends PlatformDialect {
    private static final Log logger = LogFactory.getLog(OraclePlatformDialect.class);

    public OraclePlatformDialect() {
        registerColumnType(KeyColumnType.INT, "NUMBER", 10, ColumnCompareType.SELF);
        registerColumnType(KeyColumnType.VARCHAR, "VARCHAR2", ColumnCompareType.JAVA);
        registerColumnType(KeyColumnType.CHAR, "CHAR", ColumnCompareType.JAVA);
        registerColumnType(KeyColumnType.TINYINT, "NUMBER", 3, ColumnCompareType.SELF);
        registerColumnType(KeyColumnType.SMALLINT, "NUMBER", 5, ColumnCompareType.SELF);
        registerColumnType(KeyColumnType.BIGINT, "NUMBER", 19, ColumnCompareType.SELF);
        registerColumnType(KeyColumnType.FLOAT, "FLOAT");
        registerColumnType(KeyColumnType.DOUBLE, "FLOAT", 24, ColumnCompareType.SELF);
        registerColumnType(KeyColumnType.DECIMAL, "DECIMAL", ColumnCompareType.JAVA);
        registerColumnType(KeyColumnType.BOOLEAN, "NUMBER", 1, ColumnCompareType.SELF);
        registerColumnType(KeyColumnType.DATE, "DATE");
        registerColumnType(KeyColumnType.TIME, "DATE");
        registerColumnType(KeyColumnType.DATETIME, "DATE");
        registerColumnType(KeyColumnType.TIMESTAMP, "DATE");

        registerColumnType(KeyColumnType.BLOB, "BLOB");
        registerColumnType(KeyColumnType.MEDIUMBLOB, "BLOB");
        registerColumnType(KeyColumnType.LONGBLOB, "BLOB");
        registerColumnType(KeyColumnType.TEXT, "CLOB");
        registerColumnType(KeyColumnType.MEDIUMTEXT, "CLOB");
        registerColumnType(KeyColumnType.LONGTEXT, "CLOB");
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
                    KeyColumnType type = JavaType2ColumnType.getColumnTypeByJava(mappingField.getMappingFieldType());
                    if (JavaType2ColumnType.isNumber(type) || JavaType2ColumnType.isBoolean(type))
                        ((SpecificMappingField) mappingField).setMappingFieldDefaultValue("0");
                    else if (JavaType2ColumnType.isTime(type))
                        ((SpecificMappingField) mappingField)
                                .setMappingFieldDefaultValue(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));
                    else
                        ((SpecificMappingField) mappingField).setMappingFieldDefaultValue("");

                    setDefault = true;
                }

                try {
                    StampAlter stampAlter = this.commonAddColumn(definition.getMappingTable(), mappingField);
                    this.runner(stampAlter);

                    if (setDefault) {
                        DefaultSQLAlterBuilder alterBuilder = new DefaultSQLAlterBuilder();
                        alterBuilder.alter().table(tableName).modify().column(columnName).defaultValue("*****");
                        this.runner(alterBuilder.compile());
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

    @Override
    public boolean isSupportGeneratedKeys() {
        return true;
    }

    @Override
    protected void createIndex(MappingTable mappingTable, MappingField mappingField, boolean unique) throws SQLException {
        String tableName = mappingTable.getMappingTableName();
        String indexName = "idx_" + mappingField.getMappingColumnName();
        StampAction stampAction = CreateFactory.create()
                .index().name(indexName).on().table(tableName)
                .columns(mappingField.getMappingColumnName()).compile();
        this.runner(stampAction);
    }

    @Override
    protected void dropIndex(MappingTable mappingTable, MappingField mappingField) throws SQLException {
        String tableName = mappingTable.getMappingTableName();
        String indexName = "idx_" + mappingField.getMappingColumnName();
        StampAction stampAction = DropFactory.drop().index()
                .name(indexName).on().table(tableName).compile();
        this.runner(stampAction);
    }
}
