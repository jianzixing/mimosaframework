package org.mimosaframework.orm.platform.oracle;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.mimosaframework.core.json.ModelObject;
import org.mimosaframework.core.utils.i18n.Messages;
import org.mimosaframework.core.utils.StringTools;
import org.mimosaframework.orm.criteria.*;
import org.mimosaframework.orm.i18n.LanguageMessageFactory;
import org.mimosaframework.orm.mapping.MappingField;
import org.mimosaframework.orm.mapping.MappingTable;
import org.mimosaframework.orm.platform.*;
import org.mimosaframework.orm.utils.DatabaseTypes;

import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.*;

public class OracleDatabasePorter extends AbstractDatabasePorter {
    private static final Log logger = LogFactory.getLog(OracleDatabasePorter.class);
    private static DifferentColumn differentColumn = new OracleDifferentColumn();

    protected void buildTableField(MappingField field,
                                   SQLBuilder builder,
                                   boolean isAddPrimaryKey,
                                   boolean isAutoIncrement) {
        // Oracle不支持自动更新时间操作
        boolean isTimeForUpdate = field.isMappingFieldTimeForUpdate();
        String length = differentColumn.getTypeLength(field);

        String typeName = differentColumn.getTypeNameByClass(field.getMappingFieldType());
        if (isTimeForUpdate
                && field.getMappingFieldType() != Date.class
                && field.getMappingFieldType() != java.sql.Date.class
                && field.getMappingFieldType() != Timestamp.class) {
            typeName = "TIMESTAMP";
            length = null;
        }

        // 如果字段是自增列，在不同数据库中支持的类型不同
        if (field.isMappingAutoIncrement() && isAutoIncrement) {
            typeName = differentColumn.getAutoIncrementTypeNameByClass(field.getMappingFieldType());
            length = "38"; // 如果是自增字段就设置为数字类型并且长度38(oracle NUMBER类型的最大值)
        }
        builder.addString(typeName);

        if (StringTools.isNotEmpty(length)) {
            builder.addParenthesisString(length);
        }

        if (field.isMappingFieldPrimaryKey() && isAddPrimaryKey) {
            builder.PRIMARY().KEY();
        }

        if (!field.isMappingFieldNullable()) {
            builder.NOT().NULL();
        }

        if (field.isMappingFieldUnique()) {
            builder.UNIQUE();
        }

        if (StringTools.isNotEmpty(field.getMappingFieldDefaultValue())) {
            builder.DEFAULT().addParenthesisStart()
                    .addQuotesString(field.getMappingFieldDefaultValue())
                    .addParenthesisEnd();
        }
        if (StringTools.isNotEmpty(field.getMappingFieldComment())) {
            builder.COMMENT().addQuotesString(field.getMappingFieldComment());
        }
    }

    @Override
    protected DifferentColumn getDifferentColumn() {
        return differentColumn;
    }

    protected SQLBuilder setJoinBuilderHasAs(SQLBuilder builder) {
        return builder;
    }

    private SQLBuilder buildSelectLeftJoins(Map<Object, MappingTable> tables,
                                            DefaultQuery query, Map<Object, String> aliasMap,
                                            Map<Object, List<SelectFieldAliasReference>> references) {
        SQLBuilder sqlBuilder = SQLBuilderFactory.createQMSQLBuilder();
        sqlBuilder.SELECT();
        MappingTable mainTable = tables.get(query);
        String mainTableName = mainTable.getDatabaseTableName();
        String mainTableAlias = aliasMap.get(query);

        List<Join> leftJoins = query.getLeftJoin();

        // 如果有分页则使用子查询分页
        String oracleLimitSubTable = "ocl_sub";
        boolean hasLimit = false;
        if (query.getLimit() != null) hasLimit = true;
        List<Join> rootJoins = null;

        if (leftJoins == null || leftJoins.size() == 0) {
            if (hasLimit) {
                sqlBuilder.addWrapString(oracleLimitSubTable);
                sqlBuilder.addString(".*");
            } else {
                sqlBuilder.addString("*");
            }
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

        if (rootJoins != null) {
            for (Join join : rootJoins) {
                this.setJoinBuilder(query, join, tables, aliasMap, sqlBuilder, false);
            }
        }

        if (hasLimit) {
            LogicWraps<Filter> logicWraps = query.getLogicWraps();
            sqlBuilder.addParenthesisStart();
            SQLBuilder oracleChildSelect = SQLBuilderFactory.createQMSQLBuilder();
            oracleChildSelect.SELECT().addWrapString(mainTableName).addString(".*")
                    .addSplit().addString("rownum rn").FROM().addWrapString(mainTableName);
            if (logicWraps != null) {
                oracleChildSelect.WHERE();
                if (leftJoins == null) {
                    SQLBuilder whereBuilder = this.buildWhereByLogicWraps(mainTable, logicWraps, null);
                    oracleChildSelect.addSQLBuilder(whereBuilder);
                } else {
                    SQLBuilder whereBuilder = this.buildWhereByLogicWraps(mainTable, logicWraps, mainTableAlias);
                    oracleChildSelect.addSQLBuilder(whereBuilder);
                }
            }

            SQLBuilder orderByBuilder = this.buildOrderBy(mainTable, query, null);
            oracleChildSelect.addSQLBuilder(orderByBuilder);

//            SQLBuilder limitBuilder = this.buildLimit(query);
//            sqlBuilder.addSQLBuilder(limitBuilder);

            sqlBuilder.addSQLBuilder(oracleChildSelect);
            sqlBuilder.addParenthesisEnd();
            sqlBuilder.addWrapString(oracleLimitSubTable);
            Limit limit = query.getLimit();
            sqlBuilder.WHERE().addString("rn").BETWEEN()
                    .addString(limit.getStart() + "")
                    .AND()
                    .addString((limit.getStart() + limit.getLimit()) + "");
        } else {
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

            SQLBuilder orderByBuilder = this.buildOrderBy(mainTable, query, null);
            sqlBuilder.addSQLBuilder(orderByBuilder);
        }

        SQLBuilder orderByBuilder = this.buildOrderBy(mainTable, query, mainTableAlias);
        sqlBuilder.addSQLBuilder(orderByBuilder);
        return sqlBuilder;
    }

    public SQLBuilder buildSingleSelect(DefaultQuery query, MappingTable mappingTable, boolean isSelectPrimaryKey) {
        // 如果有分页则使用子查询分页
        String oracleLimitSubTable = "ocl_sub";
        Limit limit = query.getLimit();
        boolean hasLimit = limit != null ? true : false;

        String tableName = mappingTable.getDatabaseTableName();
        SQLBuilder sqlBuilder = SQLBuilderFactory.createQMSQLBuilder();
        sqlBuilder.SELECT();
        if (hasLimit) {
            if (isSelectPrimaryKey) {
                List<MappingField> fields = mappingTable.getMappingPrimaryKeyFields();
                if (fields != null) {
                    Iterator<MappingField> iterator = fields.iterator();
                    while (iterator.hasNext()) {
                        MappingField field = iterator.next();
                        sqlBuilder.addTableWrapField(oracleLimitSubTable, field.getMappingColumnName());
                        if (iterator.hasNext()) {
                            sqlBuilder.addSplit();
                        }
                    }
                } else {
                    sqlBuilder.addString("*");
                }
            } else {
                sqlBuilder.addWrapString(oracleLimitSubTable);
                sqlBuilder.addString(".*");
            }
        } else {
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
        }
        if (hasLimit) {
            sqlBuilder.FROM();
            sqlBuilder.addParenthesisStart();
            SQLBuilder oracleChildBuilder = SQLBuilderFactory.createQMSQLBuilder();
            oracleChildBuilder.SELECT().addWrapString(tableName).addString(".*")
                    .addSplit().addString("rownum rn").FROM().addWrapString(tableName);
            LogicWraps<Filter> logicWraps = query.getLogicWraps();
            if (logicWraps != null) {
                oracleChildBuilder.WHERE();
                SQLBuilder whereBuilder = this.buildWhereByLogicWraps(mappingTable, logicWraps, null);
                oracleChildBuilder.addSQLBuilder(whereBuilder);
            }
            SQLBuilder orderByBuilder = this.buildOrderBy(mappingTable, query, null);
            oracleChildBuilder.addSQLBuilder(orderByBuilder);

            sqlBuilder.addSQLBuilder(oracleChildBuilder);
            sqlBuilder.addParenthesisEnd();
            sqlBuilder.addWrapString(oracleLimitSubTable);
            sqlBuilder.WHERE().addString("rn").BETWEEN()
                    .addString(limit.getStart() + "")
                    .AND()
                    .addString((limit.getStart() + limit.getLimit()) + "");
            return sqlBuilder;
        } else {
            sqlBuilder.FROM().addWrapString(tableName);
            LogicWraps<Filter> logicWraps = query.getLogicWraps();
            if (logicWraps != null) {
                sqlBuilder.WHERE();
                SQLBuilder whereBuilder = this.buildWhereByLogicWraps(mappingTable, logicWraps, null);
                sqlBuilder.addSQLBuilder(whereBuilder);
            }
            SQLBuilder orderByBuilder = this.buildOrderBy(mappingTable, query, null);
            sqlBuilder.addSQLBuilder(orderByBuilder);
//        SQLBuilder limitBuilder = this.buildLimit(query);
//        sqlBuilder.addSQLBuilder(limitBuilder);
            return sqlBuilder;
        }
    }

    /**
     * Oracle 不支持自增列需要使用触发器
     * <p>
     * --创建自增列
     * create sequence Student_stuId_Seq
     * increment by 1
     * start with 1
     * minvalue 1
     * maxvalue 999999999;
     * <p>
     * --创建触发器
     * create or replace trigger T_SYS_USER_USER_ID_TRIGGER
     * before insert on SYS_USERS
     * for each row
     * begin
     * select T_SYS_USER_SEQ.nextval into :new.user_id from dual;
     * end T_SYS_USER_USER_ID_TRIGGER;
     *
     * @return
     */
    private SQLBuilder createOracleSequence(MappingTable table, MappingField field) {
        String name = table.getMappingTableName().toUpperCase() + "_SEQ";
        SQLBuilder builder = SQLBuilderFactory.createQMSQLBuilder();
        builder.addString("create sequence " + name + " increment by 1 start with 1 minvalue 1 maxvalue 9999999999999999999");
        return builder;
    }

    private SQLBuilder dropOracleTrigger(MappingTable table) {
        SQLBuilder builder = SQLBuilderFactory.createQMSQLBuilder();
        builder.addString("DROP TRIGGER TAI_" + table.getMappingTableName().toUpperCase());
        return builder;
    }

    private SQLBuilder dropOracleSequence(MappingTable table) {
        String seqName = table.getMappingTableName().toUpperCase() + "_SEQ";
        SQLBuilder builder = SQLBuilderFactory.createQMSQLBuilder();
        builder.addString("DROP SEQUENCE " + seqName);
        return builder;
    }

    private SQLBuilder createOracleIncrementTrigger(MappingTable table, MappingField field) {
        String seqName = table.getMappingTableName().toUpperCase() + "_SEQ";
        SQLBuilder builder = SQLBuilderFactory.createQMSQLBuilder();
        builder.addString("create or replace trigger"
                + " TAI_" + table.getMappingTableName().toUpperCase()
                + " before insert on " + table.getMappingTableName().toUpperCase()
                + " for each row"
                + " begin"
                + " select " + seqName + ".nextval into :new." + field.getMappingColumnName() + " from dual;"
                + " end " + "TAI_" + table.getMappingTableName().toUpperCase() + "");
        return builder;
    }

    @Override
    public void createTable(MappingTable table) throws SQLException {
        SQLBuilder fieldBuilder = SQLBuilderFactory.createQMSQLBuilder();
        List<MappingField> primaryKeys = table.getMappingPrimaryKeyFields();

        SQLBuilder AUTO_INCREMENT_SQL_1 = null;
        // 开始创建表字段
        Set<MappingField> fields = table.getMappingFields();
        Iterator<MappingField> iterator = fields.iterator();
        while (iterator.hasNext()) {
            MappingField field = iterator.next();
            fieldBuilder.addWrapString(field.getMappingColumnName());

            if (primaryKeys.size() <= 1) {
                this.buildTableField(field, fieldBuilder, true, true);
            } else {
                this.buildTableField(field, fieldBuilder, false, true);
            }
            if (field.isMappingAutoIncrement()) {
                AUTO_INCREMENT_SQL_1 = this.createOracleSequence(table, field);
            }
            if (iterator.hasNext()) {
                fieldBuilder.addSplit();
            }
        }

        // 开始创建主键列
        if (primaryKeys != null && primaryKeys.size() > 1) {
            if (fields.size() > 0) {
                fieldBuilder.addSplit();
            }
            fieldBuilder.CONSTRAINT();
            String[] names = new String[primaryKeys.size()];
            StringBuilder sb = new StringBuilder();
            for (int i = 0; i < primaryKeys.size(); i++) {
                names[i] = primaryKeys.get(i).getMappingColumnName();
                if (i == 0) sb.append(names[i]);
                else sb.append("_" + names[i]);
            }
            fieldBuilder.addWrapString(sb.toString());
            fieldBuilder.PRIMARY().KEY();
            fieldBuilder.addParenthesisWrapString(names);
        }

        SQLBuilder tableBuilder = SQLBuilderFactory.createQMSQLBuilder();
        tableBuilder.CREATE().TABLE(null).addString(table.getMappingTableName());

        tableBuilder.symbolParenthesis(fieldBuilder);
        String engineName = table.getEngineName();
        String encoding = table.getEncoding();
//        if (StringUtils.isNotEmpty(engineName)) {
//            tableBuilder.ENGINE().addEqualMark().addString(engineName);
//        } else {
//            tableBuilder.ENGINE().addEqualMark().addString("InnoDB");
//        }
//        if (StringUtils.isNotEmpty(encoding)) {
//            tableBuilder.DEFAULT().CHARSET().addEqualMark().addString(encoding);
//        }

        PorterStructure tableStructure = new PorterStructure(TypeForRunner.CREATE_TABLE, tableBuilder);
        if (AUTO_INCREMENT_SQL_1 != null) {
            carryHandler.doHandler(tableStructure);
            carryHandler.doHandler(new PorterStructure(TypeForRunner.SILENT, this.dropOracleSequence(table)));
            carryHandler.doHandler(new PorterStructure(TypeForRunner.SILENT, AUTO_INCREMENT_SQL_1));
        } else {
            carryHandler.doHandler(tableStructure);
        }
    }

    @Override
    public Long insert(MappingTable table, ModelObject object) throws SQLException {
        String tableName = table.getDatabaseTableName();
        SQLBuilder insertBuilder = SQLBuilderFactory.createQMSQLBuilder().INSERT().INTO().addString(tableName);

        List<String> inObjectFields = new ArrayList<>();
        SQLBuilder valueBuilder = SQLBuilderFactory.createQMSQLBuilder();
        Iterator<Map.Entry<Object, Object>> iterator = object.entrySet().iterator();

        // 这里判断是否有自增列，如果有就使用oracle的特殊添加方式
        // SEQ.nextval
        // 这里使用两次SQL方式，第一次执行 SELECT SEQ.nextval AS id FROM dual 获取自增ID
        // 这里使用两次SQL方式，第二次执行 插入语句
        String autoIncrementValue = null;
        MappingField autoIncrementField = null;
        Set<MappingField> fields = table.getMappingFields();
        if (fields != null) {
            for (MappingField field : fields) {
                if (field.isMappingAutoIncrement()) {
                    autoIncrementValue = "select " + tableName.trim().toUpperCase() + "_SEQ.nextval as ID from dual";
                    autoIncrementField = field;
                }
            }
        }

        boolean isAutoIncrement = false;
        while (iterator.hasNext()) {
            Map.Entry<Object, Object> entry = iterator.next();
            Object key = entry.getKey();
            Object value = entry.getValue();

            String fieldName = String.valueOf(key);
            MappingField field = table.getMappingFieldByName(fieldName);
            // 如果是自增列，则使用sequences添加
            if (field != null && field.isMappingAutoIncrement()) {
                // 如果在添加的数据中已经存在主键数据，则放弃自动增长
                isAutoIncrement = true;
            }

            this.addDataPlaceholder(valueBuilder, fieldName, value, field);

            if (iterator.hasNext()) {
                valueBuilder.addSplit();
            }
            MappingField mappingField = table.getMappingFieldByName(fieldName);
            if (mappingField == null) {
                throw new IllegalArgumentException("没有找到字段" + fieldName + "映射字段");
            }
            inObjectFields.add(mappingField.getDatabaseColumnName());
        }

        if (!isAutoIncrement && autoIncrementValue != null) {
            inObjectFields.add(0, autoIncrementField.getMappingFieldName());
        }

        insertBuilder.addParenthesisWrapString(inObjectFields.toArray(new String[]{}));
        insertBuilder.VALUES();

        insertBuilder.addParenthesisStart();
        if (!isAutoIncrement && autoIncrementValue != null) {
            // insertBuilder.addString(autoIncrementValue);
            String fieldName = autoIncrementField.getMappingColumnName();
            insertBuilder.addDataPlaceholder(new AISQLDataPlaceholder(fieldName, autoIncrementField, autoIncrementValue));
            if (valueBuilder.size() > 0) {
                insertBuilder.addSplit();
            }
        }
        insertBuilder.addSQLBuilder(valueBuilder);
        insertBuilder.addParenthesisEnd();

        this.resetSeqValue(table, tableName, object);

        Long id = (Long) carryHandler.doHandler(new PorterStructure(TypeForRunner.ADD_OBJECT, insertBuilder));
        return id;
    }

    @Override
    public List<Long> inserts(MappingTable table, List<ModelObject> objects) throws SQLException {
        String tableName = table.getDatabaseTableName();
        List<String> fields = this.getFieldByTable(table);

        String autoIncrementValue = null;
        MappingField autoIncrementField = null;
        Set<MappingField> mappingFields = table.getMappingFields();
        if (fields != null) {
            for (MappingField field : mappingFields) {
                if (field.isMappingAutoIncrement()) {
                    autoIncrementValue = "select " + tableName.trim().toUpperCase() + "_SEQ.nextval as ID from dual";
                    autoIncrementField = field;
                }
            }
        }

        if (fields.size() == 0) throw new IllegalArgumentException("添加数据库的表或者数据是空的");

        SQLBuilder insertBuilder = SQLBuilderFactory.createQMSQLBuilder();
        insertBuilder.INSERT().INTO().addString(tableName);
        insertBuilder.addParenthesisWrapString(fields.toArray(new String[]{}));
        insertBuilder.VALUES();
        Iterator<String> iterator = fields.iterator();
        insertBuilder.addParenthesisStart();
        while (iterator.hasNext()) {
            String key = iterator.next();
            insertBuilder.questionMark();
            if (iterator.hasNext()) {
                insertBuilder.addSplit();
            }
        }
        insertBuilder.addParenthesisEnd();

        AIBatchPorterStructure structure = new AIBatchPorterStructure(TypeForRunner.ADD_OBJECTS, insertBuilder, objects, fields);
        structure.setSql(autoIncrementValue);
        structure.setField(autoIncrementField);

        if (objects != null) {
            for (ModelObject object : objects) {
                this.resetSeqValue(table, tableName, object);
            }
        }

        List<Long> ids = (List<Long>) carryHandler.doHandler(structure);
        return ids;
    }

    private void resetSeqValue(MappingTable table, String tableName, ModelObject object) {
        // 如果插入新数据带有主键值，这个时候就无法确认当前的自增序列和数据库
        // 中的主键知否重复，所以需要重置序列值。
        try {
            Set<MappingField> fields = table.getMappingFields();
            if (fields != null) {
                for (MappingField field : fields) {
                    if (field.isMappingAutoIncrement()) {
                        Object v = object.get(field.getMappingColumnName());
                        if (v != null && !"".equals(v)) {
                            // select max(id) from table
                            SQLBuilder max = this.createSQLBuilder().SELECT().addFun("max", field.getMappingColumnName(), "max")
                                    .FROM().addString(tableName);
                            List<ModelObject> objects = (List<ModelObject>) carryHandler.doHandler(new PorterStructure(TypeForRunner.SELECT, max));
                            if (objects != null && objects.size() > 0) {
                                ModelObject o = objects.get(0);
                                Long maxValue = o.getLong("max");
                                if (maxValue != null) {
                                    maxValue = maxValue + 1;
                                    logger.warn(Messages.get(LanguageMessageFactory.PROJECT,
                                            OracleDatabasePorter.class, "reset_incr_field"));

                                    // alter sequence seq_name increment by 1
                                    SQLBuilder resetSeq = this.createSQLBuilder().ALTER().addString("sequence")
                                            .addString(tableName.trim().toUpperCase() + "_SEQ")
                                            .addString("increment").addString("by").addString("" + maxValue);
                                    carryHandler.doHandler(new PorterStructure(TypeForRunner.UPDATE, resetSeq));
                                }
                            }
                        }
                    }
                }
            }
        } catch (Exception e) {
            throw new IllegalArgumentException(Messages.get(LanguageMessageFactory.PROJECT,
                    OracleDatabasePorter.class, "reset_incr_field_error"), e);
        }
    }

    @Override
    public SelectResult select(Map<Object, MappingTable> tables, DefaultQuery query) throws SQLException {
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

        /**
         * 如果有inner join 那么先执行join语句查询出当前表的主键列表
         * 然后拿主键列表再去执行 select left join 语句
         * 所以如果有 left join 需要上层执行两遍 select 方法
         * 第一遍获取到inner join语句然后转变查询条件改成in
         */
        SQLBuilder builder = null;
        if (innerJoins != null && innerJoins.size() > 0) {
            builder = this.buildSelectInnerJoin(tables, query, aliasMap, true, false);

            PorterStructure structure = new PorterStructure(TypeForRunner.SELECT_PRIMARY_KEY, builder);
            List<ModelObject> objects = (List<ModelObject>) carryHandler.doHandler(structure);
            return new SelectResult(objects, structure);
        } else {
            // 生成字段别名
            Map<Object, List<SelectFieldAliasReference>> references = null;
            if (leftJoins != null && leftJoins.size() > 0) {
                references = this.getFieldAliasReference(tables, aliasMap, query.getFields());
            }
            builder = this.buildSelectLeftJoins(tables, query, aliasMap, references);

            PorterStructure structure = new PorterStructure(TypeForRunner.SELECT, builder, references);
            List<ModelObject> objects = (List<ModelObject>) carryHandler.doHandler(structure);
            return new SelectResult(objects, structure);
        }
    }

    @Override
    public List<ModelObject> selectPrimaryKey(Map<Object, MappingTable> tables, DefaultQuery query) throws SQLException {
        MappingTable table = tables.get(query);
        SQLBuilder sqlBuilder = this.buildSingleSelect(query, table, true);

        return (List<ModelObject>) carryHandler.doHandler(new PorterStructure(TypeForRunner.SELECT_PRIMARY_KEY, sqlBuilder));
    }

    @Override
    protected void countTableAsBuilder(SQLBuilder countBuilder) {
        countBuilder.addWrapString("tb");
    }

    @Override
    protected SQLBuilder createSQLBuilder() {
        return SQLBuilderFactory.createQMSQLBuilder();
    }

    @Override
    protected DatabaseTypes getDatabaseType() {
        return DatabaseTypes.ORACLE;
    }

    protected SQLBuilder buildSelectFromAs(SQLBuilder sqlBuilder) {
        return sqlBuilder;
    }
}
