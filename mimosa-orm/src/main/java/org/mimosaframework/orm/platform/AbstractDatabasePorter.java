package org.mimosaframework.orm.platform;

import org.mimosaframework.core.json.ModelObject;
import org.mimosaframework.core.utils.i18n.Messages;
import org.mimosaframework.core.utils.StringTools;
import org.mimosaframework.orm.BasicFunction;
import org.mimosaframework.orm.criteria.*;
import org.mimosaframework.orm.i18n.LanguageMessageFactory;
import org.mimosaframework.orm.mapping.MappingField;
import org.mimosaframework.orm.mapping.MappingTable;

import java.sql.Blob;
import java.sql.SQLException;
import java.util.*;

public abstract class AbstractDatabasePorter implements DatabasePorter {
    protected CarryHandler carryHandler;

    @Override
    public void setCarryHandler(CarryHandler carryHandler) {
        this.carryHandler = carryHandler;
    }

    protected void buildTableField(MappingField field,
                                   SQLBuilder builder,
                                   boolean isAddPrimaryKey,
                                   boolean isAutoIncrement) {
        boolean isTimeForUpdate = field.isMappingFieldTimeForUpdate();
        if (isTimeForUpdate) {
            this.setTimeForUpdateField(field, builder);
        } else {
            this.buildTableFieldStart(builder, field, isAutoIncrement);
            this.buildTableFieldPrimaryKey(builder, field, isAddPrimaryKey);
            this.buildTableFieldNotNull(builder, field);
            this.buildTableFieldAuthIncrement(builder, field, isAutoIncrement);
            this.buildTableFieldUnique(builder, field);
            this.buildTableFieldDefaultValue(builder, field);
            this.buildTableFieldComment(builder, field);
        }
    }

    protected void buildTableFieldStart(SQLBuilder builder, MappingField field, boolean isAutoIncrement) {
        this.buildDiffAutoIncrement(builder, this.getDifferentColumn(), field, isAutoIncrement);
        boolean hasLength = this.getDifferentColumn().typeHasLength(field.getMappingFieldType());
        this.buildHasLengthTableField(hasLength, field, builder);
    }

    protected void buildTableFieldPrimaryKey(SQLBuilder builder, MappingField field, boolean isAddPrimaryKey) {
        if (field.isMappingFieldPrimaryKey() && isAddPrimaryKey) {
            builder.PRIMARY().KEY();
        }
    }

    protected void buildTableFieldNotNull(SQLBuilder builder, MappingField field) {
        if (!field.isMappingFieldNullable()) {
            builder.NOT().NULL();
        }
    }

    protected void buildTableFieldAuthIncrement(SQLBuilder builder, MappingField field, boolean isAutoIncrement) {
        if (field.isMappingFieldAutoIncrement() && isAutoIncrement) {
            builder.AUTO_INCREMENT();
        }
    }

    protected void buildTableFieldUnique(SQLBuilder builder, MappingField field) {
        if (field.isMappingFieldUnique()) {
            builder.UNIQUE();
        }
    }

    protected void buildTableFieldDefaultValue(SQLBuilder builder, MappingField field) {
        if (StringTools.isNotEmpty(field.getMappingFieldDefaultValue())) {
            builder.DEFAULT().addQuotesString(field.getMappingFieldDefaultValue());
        }
    }

    protected void buildTableFieldComment(SQLBuilder builder, MappingField field) {
        if (StringTools.isNotEmpty(field.getMappingFieldComment())) {
            builder.COMMENT().addQuotesString(field.getMappingFieldComment());
        }
    }

    protected void setTimeForUpdateField(MappingField field, SQLBuilder builder) {
        //`modified_date` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '修改时间'
        builder.addString("timestamp");
        builder.NOT().NULL().DEFAULT().addString("CURRENT_TIMESTAMP").ON().UPDATE()
                .addString("CURRENT_TIMESTAMP");
        if (StringTools.isNotEmpty(field.getMappingFieldComment())) {
            builder.COMMENT().addQuotesString(field.getMappingFieldComment());
        }
    }

    protected List<String> getFieldByTable(MappingTable table) {
        Set<MappingField> fields = table.getMappingColumns();
        List<String> fieldString = new ArrayList<>(fields.size());
        for (MappingField f : fields) {
            fieldString.add(f.getDatabaseColumnName());
        }
        return fieldString;
    }

    protected boolean isContains(List<MappingField> fields, String fieldName) {
        for (MappingField field : fields) {
            if (field.getDatabaseColumnName().equalsIgnoreCase(fieldName)) {
                return true;
            }
        }
        return false;
    }

    protected SQLBuilder createTableFields(MappingTable table) {
        SQLBuilder fieldBuilder = this.createSQLBuilder();
        // 开始创建表字段
        Set<MappingField> fields = table.getMappingFields();
        Iterator<MappingField> iterator = fields.iterator();
        while (iterator.hasNext()) {
            MappingField field = iterator.next();
            fieldBuilder.addWrapString(field.getMappingColumnName());

            this.buildTableField(field, fieldBuilder, false, true);
            if (iterator.hasNext()) {
                fieldBuilder.addSplit();
            }
        }
        return fieldBuilder;
    }

    protected void createTablePrimaryKeys(SQLBuilder fieldBuilder, MappingTable table) {
        // 开始创建主键列
        Set<MappingField> fields = table.getMappingFields();
        List<MappingField> primaryKeys = table.getMappingPrimaryKeyFields();
        if (primaryKeys != null && primaryKeys.size() > 0) {
            if (fields.size() > 0) {
                fieldBuilder.addSplit();
            }
            fieldBuilder.PRIMARY().KEY();
            String[] names = new String[primaryKeys.size()];
            for (int i = 0; i < primaryKeys.size(); i++) {
                names[i] = primaryKeys.get(i).getMappingColumnName();
            }
            fieldBuilder.addParenthesisWrapString(names);
        }
    }

    protected void createTableUniqueKeys(SQLBuilder fieldBuilder, MappingTable table) {

    }

    protected void createTableDefaultCharset(SQLBuilder tableBuilder, String encoding) {
        if (StringTools.isNotEmpty(encoding)) {
            tableBuilder.DEFAULT().CHARSET().addEqualMark().addString(encoding);
        }
    }

    @Override
    public void createTable(MappingTable table) throws SQLException {
        SQLBuilder fieldBuilder = this.createTableFields(table);
        this.createTablePrimaryKeys(fieldBuilder, table);
        this.createTableUniqueKeys(fieldBuilder, table);

        SQLBuilder tableBuilder = this.createSQLBuilder();
        tableBuilder.CREATE().TABLE(null).IF().NOT().EXISTS()
                .addString(table.getMappingTableName());

        tableBuilder.symbolParenthesis(fieldBuilder);
        String engineName = table.getEngineName();
        String encoding = table.getEncoding();
        if (StringTools.isNotEmpty(engineName)) {
            tableBuilder.ENGINE().addEqualMark().addString(engineName);
        } else {
            tableBuilder.ENGINE().addEqualMark().addString("InnoDB");
        }
        this.createTableDefaultCharset(tableBuilder, encoding);

        PorterStructure tableStructure = new PorterStructure(ChangerClassify.CREATE_TABLE, tableBuilder);

        carryHandler.doHandler(tableStructure);
    }

    protected void changeTableField(MappingField field, boolean isModify) throws SQLException {
        MappingTable table = field.getMappingTable();
        if (table == null) {
            throw new IllegalArgumentException(Messages.get(LanguageMessageFactory.PROJECT,
                    AbstractDatabasePorter.class, "lack_mapping_table"));
        }
        String tableName = table.getMappingTableName();
        String fieldName = field.getMappingColumnName();

        SQLBuilder sqlBuilder = this.createSQLBuilder();
        if (isModify) {
            this.createSQLBuilder().ALTER().TABLE(tableName)
                    .MODIFY().addWrapString(fieldName);
        } else {
            sqlBuilder = this.createSQLBuilder().ALTER().TABLE(tableName)
                    .ADD().addWrapString(fieldName);
        }

        this.buildTableField(field, sqlBuilder, true, true);
        MappingField previous = field.getPrevious();
        if (previous != null) {
            sqlBuilder.AFTER().addWrapString(previous.getMappingColumnName());
        } else {
            sqlBuilder.FIRST();
        }

        PorterStructure fieldStructure = new PorterStructure(
                isModify ? ChangerClassify.UPDATE_FIELD : ChangerClassify.CREATE_FIELD, sqlBuilder);

        carryHandler.doHandler(fieldStructure);
    }

    @Override
    public void createField(MappingField field) throws SQLException {
        this.changeTableField(field, false);
    }

    @Override
    public void updateField(MappingField field) throws SQLException {
        this.changeTableField(field, true);
    }

    @Override
    public void dropField(String table, MappingField field) throws SQLException {
        SQLBuilder dropFieldBuilder = this.createSQLBuilder();
        dropFieldBuilder.ALTER().TABLE(table).DROP().addWrapString(field.getDatabaseColumnName());
        carryHandler.doHandler(new PorterStructure(ChangerClassify.DROP_FIELD, dropFieldBuilder));
    }

    @Override
    public void dropTable(String tableName) throws SQLException {
        SQLBuilder dropBuilder = this.createSQLBuilder();
        dropBuilder.DROP().TABLE(tableName);
        carryHandler.doHandler(new PorterStructure(ChangerClassify.DROP_TABLE, dropBuilder));
    }

    protected void insertAddValue(SQLBuilder insertBuilder, MappingTable table, ModelObject object) {
        List<String> inObjectFields = new ArrayList<>();
        SQLBuilder valueBuilder = this.createSQLBuilder();
        Iterator<Map.Entry<Object, Object>> iterator = object.entrySet().iterator();
        while (iterator.hasNext()) {
            Map.Entry<Object, Object> entry = iterator.next();
            Object key = entry.getKey();
            Object value = entry.getValue();
            String fieldName = String.valueOf(key);
            MappingField mappingField = table.getMappingFieldByName(fieldName);
            if (mappingField == null) {
                throw new IllegalArgumentException(Messages.get(LanguageMessageFactory.PROJECT,
                        AbstractDatabasePorter.class, "not_found_field", fieldName));
            }

            boolean isInset = this.addDataPlaceholder(valueBuilder, fieldName, value, mappingField);

            if (isInset) {
                if (iterator.hasNext()) {
                    valueBuilder.addSplit();
                }
                inObjectFields.add(mappingField.getDatabaseColumnName());
            } else {
                if (!iterator.hasNext()) {
                    valueBuilder.removeLast();
                }
            }
        }

        insertBuilder.addParenthesisWrapString(inObjectFields.toArray(new String[]{}));
        insertBuilder.VALUES();

        insertBuilder.addParenthesisStart();
        insertBuilder.addSQLBuilder(valueBuilder);
        insertBuilder.addParenthesisEnd();
    }

    protected boolean addDataPlaceholder(SQLBuilder valueBuilder, String fieldName, Object value, MappingField mappingField) {
        if (value == Keyword.NULL || value == null) {
            valueBuilder.addString("NULL");
        } else {
            /**如果浮点数小数点多长需要转换成字符串，避免出现科学计数法**/
            /*if (mappingField.getMappingFieldType() == double.class
                    || mappingField.getMappingFieldType() == Double.class
                    || mappingField.getMappingFieldType() == float.class
                    || mappingField.getMappingFieldType() == Float.class
                    || mappingField.getMappingFieldType() == long.class
                    || mappingField.getMappingFieldType() == Long.class) {
                String scientificNotationStr = "" + value;
                if (scientificNotationStr.indexOf("E") > -1) {
                    String[] s = scientificNotationStr.split("E");
                    String lenStr = s[1];
                    if (lenStr.charAt(0) < 48 || lenStr.charAt(0) > 57) {
                        lenStr = lenStr.substring(1);
                    }
                    int len = Integer.parseInt(lenStr);
                    if (len > 32) {
                        valueBuilder.addDataPlaceholder(fieldName, value);
                    } else {
                        DecimalFormat df = new DecimalFormat("#");
                        df.setMaximumFractionDigits(len);
                        valueBuilder.addDataPlaceholder(fieldName, df.format(value));
                    }
                } else {
                    valueBuilder.addDataPlaceholder(fieldName, new BigDecimal("" + value).toString());
                }
            } else {
                valueBuilder.addDataPlaceholder(fieldName, value);
            }*/

            // 将字符串转换成二进制，如果是对象则不改变
            if (mappingField.getMappingFieldType() == Blob.class) {
                if (value instanceof byte[]) {
                    valueBuilder.addDataPlaceholder(fieldName, value);
                } else if (value instanceof String) {
                    valueBuilder.addDataPlaceholder(fieldName, String.valueOf(value).getBytes());
                } else {
                    valueBuilder.addDataPlaceholder(fieldName, value);
                }
            } else {
                valueBuilder.addDataPlaceholder(fieldName, value);
            }
        }
        return true;
    }

    @Override
    public Long insert(MappingTable table, ModelObject object) throws SQLException {
        String tableName = table.getDatabaseTableName();
        SQLBuilder insertBuilder = this.createSQLBuilder().INSERT().INTO().addString(tableName);
        this.insertAddValue(insertBuilder, table, object);
        Long id = (Long) carryHandler.doHandler(new PorterStructure(ChangerClassify.ADD_OBJECT, insertBuilder));
        return id;
    }

    @Override
    public List<Long> inserts(MappingTable table, List<ModelObject> objects) throws SQLException {
        String tableName = table.getDatabaseTableName();

        SQLBuilder insertBuilder = this.createSQLBuilder().INSERT().INTO().addString(tableName);
        List<String> fields = this.getFieldByTable(table);
        if (fields.size() == 0)
            throw new IllegalArgumentException(Messages.get(LanguageMessageFactory.PROJECT,
                    AbstractDatabasePorter.class, "empty_data"));
        return insertBuildValues(objects, insertBuilder, fields);
    }

    protected List<String> clearAutoIncrement(MappingTable table) {
        List<String> fields = this.getFieldByTable(table);
        if (fields.size() == 0)
            throw new IllegalArgumentException(Messages.get(LanguageMessageFactory.PROJECT,
                    AbstractDatabasePorter.class, "empty_data"));
        // 去除自增主键列，条件是有自增列
        List<MappingField> pkfields = table.getMappingPrimaryKeyFields();
        if (pkfields != null && pkfields.size() > 0) {
            for (MappingField field : pkfields) {
                if (field.isMappingAutoIncrement()) {
                    String pkname = field.getMappingColumnName();
                    if (fields.contains(pkname)) {
                        fields.remove(pkname);
                    }
                }
            }
        }
        return fields;
    }

    protected List<Long> insertBuildValues(List<ModelObject> objects, SQLBuilder insertBuilder, List<String> fields) throws SQLException {
        insertBuilder.addParenthesisWrapString(fields.toArray(new String[]{}));
        insertBuilder.VALUES();

        Iterator<ModelObject> listIterator = objects.iterator();
        while (listIterator.hasNext()) {
            ModelObject object = listIterator.next();
            Iterator<String> iterator = fields.iterator();
            insertBuilder.addParenthesisStart();
            while (iterator.hasNext()) {
                String key = iterator.next();
                Object value = object.get(key);

                if (value == null || value == Keyword.NULL) {
                    insertBuilder.addString("NULL");
                } else {
                    insertBuilder.addDataPlaceholder(key, value);
                }

                if (iterator.hasNext()) {
                    insertBuilder.addSplit();
                }
            }
            insertBuilder.addParenthesisEnd();
            if (listIterator.hasNext()) {
                insertBuilder.addSplit();
            }
        }

        List<Long> ids = (List<Long>) carryHandler.doHandler(new PorterStructure(ChangerClassify.ADD_OBJECTS, insertBuilder));
        return ids;
    }

    @Override
    public Long simpleInsert(String table, ModelObject object) throws SQLException {
        SQLBuilder sqlBuilder = this.createSQLBuilder();
        sqlBuilder.INSERT().INTO().addWrapString(table);

        Iterator<Map.Entry<Object, Object>> iterator = object.entrySet().iterator();
        SQLBuilder valueBuilder = this.createSQLBuilder();
        sqlBuilder.addParenthesisStart();
        while (iterator.hasNext()) {
            Map.Entry<Object, Object> entry = iterator.next();
            Object key = entry.getKey();
            Object value = entry.getValue();
            if (key != null && value != null) {
                sqlBuilder.addWrapString(String.valueOf(key));
                if (value == Keyword.NULL) {
                    valueBuilder.addString("NULL");
                } else {
                    valueBuilder.addDataPlaceholder(String.valueOf(key), value);
                }
            }
            if (iterator.hasNext()) {
                sqlBuilder.addSplit();
                valueBuilder.addSplit();
            }
        }
        sqlBuilder.addParenthesisEnd();
        sqlBuilder.VALUES();
        sqlBuilder.addParenthesisStart();
        sqlBuilder.addSQLBuilder(valueBuilder);
        sqlBuilder.addParenthesisEnd();

        Long id = (Long) carryHandler.doHandler(new PorterStructure(ChangerClassify.ADD_OBJECT, sqlBuilder));
        return id;
    }

    @Override
    public Integer update(MappingTable table, ModelObject object) throws SQLException {
        String tableName = table.getDatabaseTableName();
        List<MappingField> primaryKeyFields = table.getMappingPrimaryKeyFields();
        SQLBuilder updateBuilder = this.createSQLBuilder();
        updateBuilder.UPDATE().addString(tableName).SET();

        boolean isFirstWhere = true;
        boolean isFirstSet = true;
        SQLBuilder updateWhereBuilder = this.createSQLBuilder();
        Iterator<Map.Entry<Object, Object>> iterator = object.entrySet().iterator();

        while (iterator.hasNext()) {
            Map.Entry<Object, Object> entry = iterator.next();
            Object key = entry.getKey();
            Object value = entry.getValue();
            String fieldName = String.valueOf(key);

            if (this.isContains(primaryKeyFields, fieldName)) {
                if (isFirstWhere) {
                    updateWhereBuilder.addWrapString(fieldName).addEqualMark();
                    updateWhereBuilder.addDataPlaceholder(fieldName, value);
                } else {
                    updateWhereBuilder.AND();
                    updateWhereBuilder.addWrapString(fieldName).addEqualMark();
                    updateWhereBuilder.addDataPlaceholder(fieldName, value);
                }
                isFirstWhere = false;
            } else {
                if (!isFirstSet) {
                    updateBuilder.addSplit();
                }
                updateBuilder.addWrapString(fieldName)
                        .addEqualMark();
                if (value == Keyword.NULL) {
                    updateBuilder.addString("NULL");
                } else {
                    updateBuilder.addDataPlaceholder(fieldName, value);
                }
                isFirstSet = false;
            }
        }

        updateBuilder.WHERE().addSQLBuilder(updateWhereBuilder);
        return (Integer) carryHandler.doHandler(new PorterStructure(ChangerClassify.UPDATE_OBJECT, updateBuilder));
    }

    @Override
    public Integer update(MappingTable table, DefaultUpdate update) throws SQLException {
        String tableName = table.getDatabaseTableName();
        SQLBuilder sqlBuilder = this.createSQLBuilder();
        sqlBuilder.UPDATE().addString(tableName).SET();

        Map<Object, Object> values = update.getValues();
        if (values != null) {
            Iterator<Map.Entry<Object, Object>> iterator = values.entrySet().iterator();
            while (iterator.hasNext()) {
                Map.Entry<Object, Object> entry = iterator.next();
                Object key = entry.getKey();
                Object value = entry.getValue();
                String fieldName = String.valueOf(key);
                MappingField mappingField = table.getMappingFieldByName(fieldName);
                String columnName = mappingField.getDatabaseColumnName();

                if (value instanceof UpdateSetValue) {
                    UpdateSetValue usv = (UpdateSetValue) value;
                    if (usv.getObject().equals(UpdateSpecialType.ADD_SELF)) {
                        sqlBuilder.addWrapString(columnName);
                        sqlBuilder.addEqualMark();
                        sqlBuilder.addWrapString(columnName);
                        sqlBuilder.addString("+");
                        sqlBuilder.addString("" + usv.getStep());
                    }
                    if (usv.getObject().equals(UpdateSpecialType.SUB_SELF)) {
                        sqlBuilder.addWrapString(columnName);
                        sqlBuilder.addEqualMark();
                        sqlBuilder.addWrapString(columnName);
                        sqlBuilder.addString("-");
                        sqlBuilder.addString("" + usv.getStep());
                    }
                } else {
                    sqlBuilder.addWrapString(columnName);
                    sqlBuilder.addEqualMark();
                    sqlBuilder.addDataPlaceholder(fieldName, value);
                }

                if (iterator.hasNext()) {
                    sqlBuilder.addSplit();
                }
            }

            this.selectBuildWhere(table, sqlBuilder, update.getLogicWraps());
            return (Integer) carryHandler.doHandler(new PorterStructure(ChangerClassify.UPDATE, sqlBuilder));
        }
        return null;
    }

    private void selectBuildWhere(MappingTable table, SQLBuilder sqlBuilder, LogicWraps<Filter> logicWraps) {
        if (logicWraps != null) {
            sqlBuilder.WHERE();

            SQLBuilder whereBuilder = this.buildWhereByLogicWraps(table, logicWraps, null);
            sqlBuilder.addSQLBuilder(whereBuilder);
        }
    }

    @Override
    public Integer simpleUpdate(String table, ModelObject object, ModelObject where) throws SQLException {
        SQLBuilder sqlBuilder = this.createSQLBuilder();
        sqlBuilder.UPDATE().addWrapString(table).SET();

        Iterator<Map.Entry<Object, Object>> iterator = object.entrySet().iterator();
        while (iterator.hasNext()) {
            this.simpleBuildValue(sqlBuilder, iterator);
            if (iterator.hasNext()) {
                sqlBuilder.addSplit();
            }
        }

        sqlBuilder.WHERE();

        this.simpleSelectCountBuilder(sqlBuilder, where);
        return (Integer) carryHandler.doHandler(new PorterStructure(ChangerClassify.UPDATE, sqlBuilder));
    }

    @Override
    public Integer delete(MappingTable table, ModelObject object) throws SQLException {
        String tableName = table.getDatabaseTableName();
        List<MappingField> primaryKeyFields = table.getMappingPrimaryKeyFields();
        SQLBuilder deleteBuilder = this.createSQLBuilder();
        deleteBuilder.DELETE().FROM().addString(tableName);

        boolean isFirstWhere = true;
        SQLBuilder deleteWhereBuilder = this.createSQLBuilder();
        Iterator<Map.Entry<Object, Object>> iterator = object.entrySet().iterator();

        while (iterator.hasNext()) {
            Map.Entry<Object, Object> entry = iterator.next();
            Object key = entry.getKey();
            Object value = entry.getValue();
            String fieldName = String.valueOf(key);

            if (this.isContains(primaryKeyFields, fieldName)) {
                if (isFirstWhere) {
                    deleteWhereBuilder.addWrapString(fieldName).addEqualMark();
                    deleteWhereBuilder.addDataPlaceholder(fieldName, value);
                } else {
                    deleteWhereBuilder.AND();
                    deleteWhereBuilder.addWrapString(fieldName).addEqualMark();
                    deleteWhereBuilder.addDataPlaceholder(fieldName, value);
                }
                isFirstWhere = false;
            }
        }

        deleteBuilder.WHERE().addSQLBuilder(deleteWhereBuilder);
        return (Integer) carryHandler.doHandler(new PorterStructure(ChangerClassify.DELETE_OBJECT, deleteBuilder));
    }

    @Override
    public Integer delete(MappingTable table, DefaultDelete delete) throws SQLException {
        String tableName = table.getDatabaseTableName();
        SQLBuilder sqlBuilder = this.createSQLBuilder();
        sqlBuilder.DELETE().FROM().addString(tableName);

        selectBuildWhere(table, sqlBuilder, delete.getLogicWraps());
        return (Integer) carryHandler.doHandler(new PorterStructure(ChangerClassify.DELETE, sqlBuilder));
    }

    @Override
    public Integer simpleDelete(String table, ModelObject where) throws SQLException {
        SQLBuilder sqlBuilder = this.createSQLBuilder();
        sqlBuilder.DELETE().FROM().TABLE(table).WHERE();
        this.simpleSelectCountBuilder(sqlBuilder, where);
        return (Integer) carryHandler.doHandler(new PorterStructure(ChangerClassify.DELETE, sqlBuilder));
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
                Object queryOrJoin = next.getKey();
                aliasMap.put(queryOrJoin, "t" + (++startAliasNumber));
            }
        }

        // 开始检查查询条件中是否设置了查询字段
        // 如果设置了查询字段则只生成查询字段中的别名
        Map<Class, List<String>> fields = query.getFields();

        Map<Object, List<SelectFieldAliasReference>> references = null;
        if (leftJoins != null && leftJoins.size() > 0) {
            references = this.getFieldAliasReference(tables, aliasMap, fields);
        }

        /**
         * 这里如果带有limit则在join查询时一对多的关系会查询不完整
         * 需要先查询主键后再使用当前查询
         */
        SQLBuilder builder = this.buildSelect(tables, query, aliasMap, references);

        PorterStructure structure = new PorterStructure(ChangerClassify.SELECT, builder, references);
        List<ModelObject> objects = (List<ModelObject>) carryHandler.doHandler(structure);
        return new SelectResult(objects, structure);
    }

    protected Map<Object, MappingTable> clearNotExistMappingTable(Map<Object, MappingTable> tables, DefaultQuery query) {
        List<Join> innerJoins = query.getInnerJoin();
        List<Join> leftJoins = query.getLeftJoin();
        Map<Object, MappingTable> newTables = new LinkedHashMap<>();
        if (tables.containsKey(query)) newTables.put(query, tables.get(query));
        if (innerJoins != null) {
            for (Join join : innerJoins) {
                if (tables.containsKey(join)) newTables.put(join, tables.get(join));
            }
        }
        if (leftJoins != null) {
            for (Join join : leftJoins) {
                if (tables.containsKey(join)) newTables.put(join, tables.get(join));
            }
        }
        return newTables;
    }

    @Override
    public List<ModelObject> select(MappingTable table, DefaultFunction function) throws SQLException {
        SQLBuilder sqlBuilder = this.createSQLBuilder();
        sqlBuilder.SELECT();
        List groupBys = function.getGroupBy();
        List<Order> orderBys = function.getOrderBy();
        List childGroupBys = function.getChildGroupBy();

        SQLBuilder funPSQLBuilder = this.createSQLBuilder();
        SQLBuilder funCSQLBuilder = this.createSQLBuilder();

        List<FunctionField> funs = function.getFuns();
        Iterator<FunctionField> iterator = funs.iterator();
        while (iterator.hasNext()) {
            FunctionField field = iterator.next();
            BasicFunction fun = field.getFunction();
            Object f = field.getField();
            String alias = field.getAlias();
            if (StringTools.isEmpty(alias)
                    && !(f instanceof Integer || f instanceof Long || f instanceof Double
                    || f instanceof Short || f instanceof Byte || f instanceof Float)) {
                alias = String.valueOf(f);
            }
            if (f instanceof Integer) {
                funPSQLBuilder.addFun(fun.name(), f, alias);
                funCSQLBuilder.addFun(fun.name(), f, alias);
            } else {
                MappingField funField = table.getMappingFieldByName(String.valueOf(f));
                if (funField != null) {
                    funPSQLBuilder.addFun(fun.name(), funField.getMappingColumnName(), alias);
                    funCSQLBuilder.addFun(fun.name(), funField.getMappingColumnName(), funField.getMappingColumnName());
                } else {
                    throw new IllegalArgumentException(Messages.get(LanguageMessageFactory.PROJECT,
                            AbstractDatabasePorter.class, "not_found_field", String.valueOf(f)));
                }
            }

            if (iterator.hasNext()) {
                funPSQLBuilder.addSplit();
                funCSQLBuilder.addSplit();
            }
        }

        sqlBuilder.addSQLBuilder(funPSQLBuilder);
        sqlBuilder.FROM();
        if (childGroupBys != null) {
            sqlBuilder.addParenthesisStart();
            sqlBuilder.SELECT();
            sqlBuilder.addSQLBuilder(funCSQLBuilder);
            sqlBuilder.FROM().addWrapString(table.getDatabaseTableName());

            LogicWraps wraps = function.getLogicWraps();
            if (wraps != null) {
                SQLBuilder where = this.buildWhereByLogicWraps(table, wraps, null);
                sqlBuilder.WHERE();
                sqlBuilder.addSQLBuilder(where);
            }

            sqlBuilder.GROUP().BY();
            Iterator groupIterator = childGroupBys.iterator();
            while (groupIterator.hasNext()) {
                Object f = groupIterator.next();
                MappingField funField = table.getMappingFieldByName(String.valueOf(f));
                if (funField == null) {
                    sqlBuilder.addWrapString(String.valueOf(f));
                } else {
                    sqlBuilder.addWrapString(funField.getMappingColumnName());
                }
                if (groupIterator.hasNext()) {
                    sqlBuilder.addSplit();
                }
            }

            sqlBuilder.addParenthesisEnd();
            this.buildSelectFromAs(sqlBuilder).addString("t");

            if (orderBys != null) {
                sqlBuilder.ORDER().BY();
                Iterator<Order> orderIterator = orderBys.iterator();
                while (orderIterator.hasNext()) {
                    Order order = orderIterator.next();
                    MappingField funField = table.getMappingFieldByName(String.valueOf(order.getField()));
                    if (funField == null) {
                        sqlBuilder.addWrapString(String.valueOf(order.getField()));
                    } else {
                        sqlBuilder.addWrapString(funField.getMappingColumnName());
                    }
                    if (order.isAsc()) {
                        sqlBuilder.ASC();
                    } else {
                        sqlBuilder.DESC();
                    }
                    if (orderIterator.hasNext()) {
                        sqlBuilder.addSplit();
                    }
                }
            }
        } else {
            sqlBuilder.addWrapString(table.getDatabaseTableName());

            LogicWraps wraps = function.getLogicWraps();
            if (wraps != null) {
                SQLBuilder where = this.buildWhereByLogicWraps(table, wraps, null);
                sqlBuilder.WHERE();
                sqlBuilder.addSQLBuilder(where);
            }

            if (groupBys != null) {
                sqlBuilder.GROUP().BY();
                Iterator groupIterator = groupBys.iterator();
                while (groupIterator.hasNext()) {
                    Object f = groupIterator.next();
                    MappingField funField = table.getMappingFieldByName(String.valueOf(f));
                    if (funField == null) {
                        sqlBuilder.addWrapString(String.valueOf(f));
                    } else {
                        sqlBuilder.addWrapString(funField.getMappingColumnName());
                    }
                    if (groupIterator.hasNext()) {
                        sqlBuilder.addSplit();
                    }
                }
            }
        }

        List<ModelObject> objects = (List<ModelObject>) carryHandler.doHandler(new PorterStructure(ChangerClassify.SELECT, sqlBuilder));
        return objects;
    }

    protected void countTableAsBuilder(SQLBuilder countBuilder) {
        countBuilder.AS().addWrapString("tb");
    }

    @Override
    public List<ModelObject> count(Map<Object, MappingTable> tables, DefaultQuery query) throws SQLException {
        SQLBuilder countBuilder = this.createSQLBuilder();
        countBuilder.SELECT().addFun("COUNT", 1, "count")
                .FROM();

        List innerJoins = query.getInnerJoin();
        Map<Object, String> aliasMap = null;
        if (innerJoins != null && innerJoins.size() > 0) {
            int startAliasNumber = 0;
            aliasMap = new LinkedHashMap<>(tables.size());
            Iterator<Map.Entry<Object, MappingTable>> iterator = tables.entrySet().iterator();
            while (iterator.hasNext()) {
                Map.Entry<Object, MappingTable> next = iterator.next();
                aliasMap.put(next.getKey(), "t" + (++startAliasNumber));
            }
        }

        MappingTable table = tables.get(query);
        String tableName = table.getDatabaseTableName();

        if (innerJoins != null && innerJoins.size() > 0) {
            SQLBuilder builder = this.buildSelectInnerJoin(tables, query, aliasMap, false, false);
            countBuilder.addParenthesisStart();
            countBuilder.addSQLBuilder(builder);
            countBuilder.addParenthesisEnd();
            this.countTableAsBuilder(countBuilder);
        } else {
            countBuilder.addWrapString(tableName);
            LogicWraps<Filter> logicWraps = query.getLogicWraps();
            if (logicWraps != null) {
                SQLBuilder whereBuilder = this.buildWhereByLogicWraps(table, logicWraps, null);
                countBuilder.WHERE();
                countBuilder.addSQLBuilder(whereBuilder);
            }
        }

        List<ModelObject> objects = (List<ModelObject>) carryHandler.doHandler(new PorterStructure(ChangerClassify.COUNT, countBuilder));
        return objects;
    }

    @Override
    public List<ModelObject> selectPrimaryKey(Map<Object, MappingTable> tables, DefaultQuery query) throws SQLException {
        int startAliasNumber = 0;
        Map<Object, String> aliasMap = new LinkedHashMap<>(tables.size());
        Iterator<Map.Entry<Object, MappingTable>> iterator = tables.entrySet().iterator();
        while (iterator.hasNext()) {
            Map.Entry<Object, MappingTable> next = iterator.next();
            aliasMap.put(next.getKey(), "t" + (++startAliasNumber));
        }
        SQLBuilder builder = this.buildSelectInnerJoin(tables, query, aliasMap, true, false);
        return (List<ModelObject>) carryHandler.doHandler(new PorterStructure(ChangerClassify.SELECT_PRIMARY_KEY, builder));
    }

    protected void simpleBuildValue(SQLBuilder sqlBuilder, Iterator<Map.Entry<Object, Object>> iterator) {
        Map.Entry<Object, Object> entry = iterator.next();
        Object key = entry.getKey();
        Object value = entry.getValue();
        if (key != null && value != null) {
            sqlBuilder.addWrapString(String.valueOf(key));
            sqlBuilder.addEqualMark();
            if (value == Keyword.NULL) {
                sqlBuilder.addString("NULL");
            } else {
                sqlBuilder.addDataPlaceholder(String.valueOf(key), value);
            }
        }
    }

    protected void simpleSelectCountBuilder(SQLBuilder sqlBuilder, ModelObject where) {
        Iterator<Map.Entry<Object, Object>> iterator = where.entrySet().iterator();
        while (iterator.hasNext()) {
            this.simpleBuildValue(sqlBuilder, iterator);
            if (iterator.hasNext()) {
                sqlBuilder.AND();
            }
        }
    }

    @Override
    public List<ModelObject> simpleSelect(String table, ModelObject where) throws SQLException {
        SQLBuilder sqlBuilder = this.createSQLBuilder();
        sqlBuilder.SELECT().addString("*").FROM().TABLE(table).WHERE();
        this.simpleSelectCountBuilder(sqlBuilder, where);

        List<ModelObject> objects = (List<ModelObject>) carryHandler.doHandler(new PorterStructure(ChangerClassify.SELECT, sqlBuilder));
        return objects;
    }

    @Override
    public List<ModelObject> simpleCount(String table, ModelObject where) throws SQLException {
        SQLBuilder sqlBuilder = this.createSQLBuilder();
        sqlBuilder.SELECT().addString("COUNT(1)").FROM().TABLE(table).WHERE();
        this.simpleSelectCountBuilder(sqlBuilder, where);
        List<ModelObject> objects = (List<ModelObject>) carryHandler.doHandler(new PorterStructure(ChangerClassify.COUNT, sqlBuilder));
        return objects;
    }

    protected Map<Object, List<SelectFieldAliasReference>> getFieldAliasReference(
            Map<Object, MappingTable> tables,
            Map<Object, String> aliasMap,
            Map<Class, List<String>> selectFields) {

        Iterator<Map.Entry<Object, MappingTable>> iterator = tables.entrySet().iterator();
        Map<Object, List<SelectFieldAliasReference>> map = new LinkedHashMap<>(tables.size());
        int i = 0;
        while (iterator.hasNext()) {
            Map.Entry<Object, MappingTable> entry = iterator.next();
            MappingTable table = entry.getValue();
            Object key = entry.getKey();
            Class mainClass = null;
            Class tableClass = null;
            if (key instanceof Query) {
                tableClass = ((DefaultQuery) key).getTableClass();
            }
            if (key instanceof Join) {
                mainClass = ((DefaultJoin) key).getMainTable();
                tableClass = ((DefaultJoin) key).getTable();
            }

            String tableAliasName = aliasMap.get(key);
            List<String> selectClassFields = null;
            if (selectFields != null && selectFields.size() > 0) {
                selectClassFields = selectFields.get(tableClass);
            }

            Set<MappingField> fields = table.getMappingColumns();
            if (fields != null) {
                List<SelectFieldAliasReference> references = new ArrayList<>(fields.size());
                for (MappingField field : fields) {
                    String mappingFieldName = field.getMappingFieldName();
                    if (selectClassFields != null && !selectClassFields.contains(mappingFieldName)) {
                        // 假如调用者设置了查询的字段，且这个字段不在查询字段中则跳过
                        // 是否这里还需要判断一下关联字段必须查询
                        continue;
                    }
                    SelectFieldAliasReference reference = new SelectFieldAliasReference(mainClass, tableClass);
                    reference.setTableAliasName(tableAliasName);
                    reference.setFieldName(field.getDatabaseColumnName());
                    reference.setFieldAliasName("f" + (i++));
                    reference.setJavaFieldName(mappingFieldName);
                    reference.setPrimaryKey(field.isMappingFieldPrimaryKey());
                    references.add(reference);
                }
                map.put(key, references);
            }
        }
        return map;
    }


    protected void buildDiffAutoIncrement(SQLBuilder builder, DifferentColumn differentColumn, MappingField field, boolean isAutoIncrement) {
        String typeName = differentColumn.getTypeNameByClass(field.getMappingFieldType());
        // 如果字段是自增列，在不同数据库中支持的类型不同
        if (field.isMappingAutoIncrement() && isAutoIncrement) {
            typeName = differentColumn.getAutoIncrementTypeNameByClass(field.getMappingFieldType());
        }
        builder.addString(typeName);
    }

    protected void buildHasLengthTableField(boolean hasLength, MappingField field, SQLBuilder builder) {
        if (hasLength && !field.isMappingAutoIncrement()) {
            String len = this.getDifferentColumn().getTypeLength(field);
            builder.addParenthesisString(len);
        }
    }

    protected SQLBuilder buildWhereByFilter(MappingTable table, DefaultFilter filter, String tableAliasName) {
        Object key = filter.getKey();
        String fieldName = String.valueOf(key);
        MappingField mappingField = table.getMappingFieldByName(fieldName);

        if (mappingField != null) {
            String columnName = mappingField.getDatabaseColumnName();
            SQLBuilder whereBuilder = this.createSQLBuilder();
            if (StringTools.isNotEmpty(tableAliasName)) {
                whereBuilder.addTableWrapField(tableAliasName, mappingField.getDatabaseColumnName());
            } else {
                whereBuilder.addWrapString(mappingField.getDatabaseColumnName());
            }

            String symbol = filter.getSymbol();
            Object value = filter.getValue();
            Object start = filter.getStartValue();
            Object end = filter.getEndValue();

            if (symbol.equalsIgnoreCase("like")) {
                whereBuilder.LIKE().addDataPlaceholder(columnName, value);
            } else if (symbol.equalsIgnoreCase("in") || symbol.equalsIgnoreCase("notIn")) {
                if (symbol.equalsIgnoreCase("notIn")) {
                    whereBuilder.NOT().IN();
                } else {
                    whereBuilder.IN();
                }

                if (value.getClass().isArray()) {
                    Object[] values = (Object[]) value;

                    whereBuilder.addParenthesisStart();
                    boolean isFirst = true;
                    for (Object v : values) {
                        if (!isFirst) {
                            whereBuilder.addSplit();
                        }
                        whereBuilder.addDataPlaceholder(columnName, v);
                        isFirst = false;
                    }
                }
                if (value instanceof Iterable) {
                    Iterable iterable = (Iterable) value;
                    Iterator iteratorValue = iterable.iterator();
                    whereBuilder.addParenthesisStart();
                    while (iteratorValue.hasNext()) {
                        Object next = iteratorValue.next();
                        if (next instanceof Map.Entry) {
                            next = ((Map.Entry) next).getValue();
                        }
                        whereBuilder.addDataPlaceholder(columnName, next);
                        if (iteratorValue.hasNext()) {
                            whereBuilder.addSplit();
                        }
                    }
                }

                whereBuilder.addParenthesisEnd();

            } else if (symbol.equalsIgnoreCase("isNull")) {
                whereBuilder.IS().NULL();
            } else if (symbol.equalsIgnoreCase("notNull")) {
                whereBuilder.IS().NOT().NULL();
            } else if (symbol.equalsIgnoreCase("between")) {
                whereBuilder.addString(">=").addDataPlaceholder(columnName, start);
                whereBuilder.AND();
                if (StringTools.isNotEmpty(tableAliasName)) {
                    whereBuilder.addTableWrapField(tableAliasName, mappingField.getDatabaseColumnName());
                } else {
                    whereBuilder.addWrapString(mappingField.getDatabaseColumnName());
                }
                whereBuilder.addString("<=").addDataPlaceholder(columnName, end);
            } else { // A='B' 或者 A!='B' 或者 A>2 A>=2 或者 A<2 A<=2
                whereBuilder.addString(symbol);
                if (value == Keyword.NULL) {
                    whereBuilder.addString("NULL");
                } else {
                    whereBuilder.addDataPlaceholder(columnName, value);
                }
            }

            return whereBuilder;
        }
        return null;
    }

    protected void buildJoinOnField(SQLBuilder builder,
                                    List<OnField> onFields,
                                    MappingTable mainTable,
                                    MappingTable joinTable,
                                    String selfAliasName,
                                    String mainAliasName) {
        Iterator<OnField> onFieldIterator = onFields.iterator();
        while (onFieldIterator.hasNext()) {
            OnField of = onFieldIterator.next();
            Object key = of.getKey();
            String symbol = of.getSymbol();
            Object value = of.getValue();
            MappingField onSelfField = joinTable.getMappingFieldByName(String.valueOf(key));
            MappingField onMainField = mainTable.getMappingFieldByName(String.valueOf(value));

            if (onSelfField == null) {
                throw new IllegalArgumentException(Messages.get(LanguageMessageFactory.PROJECT,
                        AbstractDatabasePorter.class, "not_found_table_field",
                        joinTable.getMappingClass().getSimpleName(), "" + key));
            }
            if (onMainField == null) {
                throw new IllegalArgumentException(Messages.get(LanguageMessageFactory.PROJECT,
                        AbstractDatabasePorter.class, "not_found_table_field",
                        joinTable.getMappingClass().getSimpleName(), "" + value));
            }

            builder.addTableWrapField(selfAliasName, onSelfField.getDatabaseColumnName());
            builder.addString(symbol);
            builder.addTableWrapField(mainAliasName, onMainField.getDatabaseColumnName());
            if (onFieldIterator.hasNext()) {
                builder.AND();
            }
        }
    }

    private void buildJoinValueField(SQLBuilder builder,
                                     List<Filter> valueFilters,
                                     MappingTable joinTable,
                                     String selfAliasName) {
        Iterator<Filter> filterIterator = valueFilters.iterator();
        while (filterIterator.hasNext()) {
            DefaultFilter filter = (DefaultFilter) filterIterator.next();
            SQLBuilder filterBuilder = this.buildWhereByFilter(joinTable, filter, selfAliasName);
            builder.addSQLBuilder(filterBuilder);
            if (filterIterator.hasNext()) {
                builder.AND();
            }
        }
    }

    protected SQLBuilder setJoinBuilderHasAs(SQLBuilder builder) {
        builder.AS();
        return builder;
    }

    protected void setJoinBuilder(Object main,
                                  Join self,
                                  Map<Object, MappingTable> tables,
                                  Map<Object, String> aliasMap,
                                  SQLBuilder builder,
                                  boolean isInnerJoin) {
        if (self != null) {
            MappingTable mainTable = tables.get(main);
            String mainAliasName = aliasMap.get(main);

            DefaultJoin j = (DefaultJoin) self;
            MappingTable joinTable = tables.get(self);
            String selfAliasName = aliasMap.get(self);
            String selfTableName = joinTable.getDatabaseTableName();
            if (isInnerJoin) {
                builder.INNER().JOIN().addWrapString(selfTableName);
                this.setJoinBuilderHasAs(builder).addWrapString(selfAliasName);
            } else {
                builder.LEFT().JOIN().addWrapString(selfTableName);
                this.setJoinBuilderHasAs(builder).addWrapString(selfAliasName);
            }

            List<OnField> onFields = j.getOnFilters();
            List<Filter> valueFilters = j.getValueFilters();
            builder.ON();

            if (onFields != null && onFields.size() > 0) {
                this.buildJoinOnField(builder, onFields, mainTable, joinTable, selfAliasName, mainAliasName);
            }
            if (onFields != null && onFields.size() > 0 && valueFilters != null && valueFilters.size() > 0) {
                builder.AND();
            }
            if (valueFilters != null && valueFilters.size() > 0) {
                this.buildJoinValueField(builder, valueFilters, joinTable, selfAliasName);
            }

            Set<Join> children = j.getChildJoin();
            if (children != null) {
                for (Join join : children) {
                    this.setJoinBuilder(self, join, tables, aliasMap, builder, isInnerJoin);
                }
            }
        }
    }

    protected SQLBuilder buildSelect(Map<Object, MappingTable> tables,
                                     DefaultQuery query, Map<Object, String> aliasMap,
                                     Map<Object, List<SelectFieldAliasReference>> references) {
        SQLBuilder sqlBuilder = this.createSQLBuilder();
        sqlBuilder.SELECT();

        MappingTable mainTable = tables.get(query);
        String mainTableName = mainTable.getDatabaseTableName();
        String mainTableAlias = aliasMap.get(query);

        List<Join> leftJoins = query.getLeftJoin();
        List<Join> innerJoin = query.getInnerJoin();
        if (!this.hasJoins(query)) {
            Map<Class, List<String>> fields = query.getFields();
            if (fields == null || fields.size() == 0
                    || fields.get(query.getTableClass()) == null
                    || fields.get(query.getTableClass()).size() == 0) {
                sqlBuilder.addString("*");
            } else {
                List<String> strFields = fields.get(query.getTableClass());
                Set<MappingField> mappingFields = mainTable.getMappingFields();
                List<String> cntFields = new ArrayList<>();
                for (MappingField mappingField : mappingFields) {
                    String mappingFieldName = mappingField.getMappingFieldName();
                    if (strFields.contains(mappingFieldName)) {
                        cntFields.add(mappingField.getMappingColumnName());
                    }
                }

                Iterator<String> iterator = cntFields.iterator();
                while (iterator.hasNext()) {
                    sqlBuilder.addWrapString(iterator.next());
                    if (iterator.hasNext()) {
                        sqlBuilder.addSplit();
                    }
                }
            }
        } else {
            List<SelectFieldAliasReference> mainRfs = references.get(query);
            if (this.buildFieldsSQL(mainRfs, sqlBuilder, mainTableAlias) && this.hasJoins(query)) {
                sqlBuilder.addSplit();
            }

            if (hasInnerJoin(query)) {
                this.buildJoinFieldsSQL(references, aliasMap, sqlBuilder, innerJoin);
                if (hasLeftJoin(query)) {
                    sqlBuilder.addSplit();
                }
            }
            if (hasLeftJoin(query)) {
                this.buildJoinFieldsSQL(references, aliasMap, sqlBuilder, leftJoins);
            }
        }

        sqlBuilder.FROM();
        sqlBuilder.addWrapString(mainTableName);
        if (StringTools.isNotEmpty(mainTableAlias)) {
            sqlBuilder.AS().addWrapString(mainTableAlias);
        }

        if (this.hasInnerJoin(query))
            for (Join join : innerJoin) {
                DefaultJoin j = (DefaultJoin) join;
                if (j.getMainTable() == query.getTableClass()) { // 没有关联的join排除
                    this.setJoinBuilder(query, join, tables, aliasMap, sqlBuilder, true);
                }
            }
        if (this.hasLeftJoin(query))
            for (Join join : leftJoins) {
                DefaultJoin j = (DefaultJoin) join;
                if (j.getMainTable() == query.getTableClass()) { // 没有关联的join排除
                    this.setJoinBuilder(query, join, tables, aliasMap, sqlBuilder, false);
                }
            }

        LogicWraps<Filter> logicWraps = query.getLogicWraps();
        if (logicWraps != null) {
            sqlBuilder.WHERE();
            if (!this.hasJoins(query)) {
                SQLBuilder whereBuilder = this.buildWhereByLogicWraps(mainTable, logicWraps, null);
                sqlBuilder.addSQLBuilder(whereBuilder);
            } else {
                SQLBuilder whereBuilder = this.buildWhereByLogicWraps(mainTable, logicWraps, mainTableAlias);
                sqlBuilder.addSQLBuilder(whereBuilder);
            }
        }
        if (!this.hasJoins(query)) {
            SQLBuilder orderByBuilder = this.buildOrderBy(mainTable, query, null);
            sqlBuilder.addSQLBuilder(orderByBuilder);
        } else {
            SQLBuilder orderByBuilder = this.buildOrderBy(mainTable, query, mainTableAlias);
            sqlBuilder.addSQLBuilder(orderByBuilder);
        }

        SQLBuilder limitBuilder = this.buildLimit(query);
        sqlBuilder.addSQLBuilder(limitBuilder);

        return sqlBuilder;
    }

    protected SQLBuilder buildLimit(DefaultQuery query) {
        Limit o = query.getLimit();
        if (o != null) {
            SQLBuilder limitBuilder = this.createSQLBuilder();

            long start = o.getStart();
            long limit = this.getLimitDefault(query, o);


            if (start > 0 || limit > 0) {
                limitBuilder.LIMIT()
                        .addDataPlaceholder("Page Start", start)
                        .addSplit()
                        .addDataPlaceholder("Page Limit", limit);
            }
            return limitBuilder;
        }
        return null;
    }

    protected long getLimitDefault(DefaultQuery query, Limit l) {
        long start = l.getStart();
        long limit = l.getLimit();

        List<Order> orders = query.getOrders();
        if (orders != null && orders.size() > 0) {
            /**
             * 假设有排序则限制提交数量，防止查询数据过大！
             * 在Mysql 5.7以上子查询的排序如果没有limit会被优化掉
             */
            if (start <= 0 && limit <= 0) {
                limit = 1000;
            }
        }

        return limit;
    }

    protected SQLBuilder buildOrderBy(MappingTable table, DefaultQuery query, String aliasTableName) {
        List<Order> orders = query.getOrders();
        if (orders != null && orders.size() > 0) {
            SQLBuilder orderBuilder = this.createSQLBuilder();
            orderBuilder.ORDER().BY();

            Iterator<Order> it = orders.iterator();
            while (it.hasNext()) {
                Order order = it.next();
                String fieldName = String.valueOf(order.getField());
                MappingField field = table.getMappingFieldByName(fieldName);
                if (field == null) {
                    throw new IllegalArgumentException(Messages.get(LanguageMessageFactory.PROJECT,
                            AbstractDatabasePorter.class, "order_not_in_table", fieldName));
                }
                String columnName = field.getDatabaseColumnName();

                if (StringTools.isNotEmpty(aliasTableName)) {
                    orderBuilder.addTableWrapField(aliasTableName, columnName);
                } else {
                    orderBuilder.addWrapString(columnName);
                }
                if (order.isAsc()) {
                    orderBuilder.ASC();
                } else {
                    orderBuilder.DESC();
                }
                if (it.hasNext()) {
                    orderBuilder.addSplit();
                }
            }
            return orderBuilder;
        }
        return null;
    }

    protected SQLBuilder buildWhereByLogicWraps(MappingTable table, LogicWraps<Filter> logicWraps, String tableAliasName) {
        if (logicWraps != null && table != null) {
            SQLBuilder whereBuilder = this.createSQLBuilder();

            Iterator<LogicWrapObject<Filter>> iterator = logicWraps.iterator();
            while (iterator.hasNext()) {
                LogicWrapObject<Filter> lwo = iterator.next();
                Filter filter = lwo.getWhere();
                if (filter != null) {
                    SQLBuilder filterBuilder = this.buildWhereByFilter(table, (DefaultFilter) filter, tableAliasName);
                    if (filterBuilder != null) {
                        whereBuilder.addSQLBuilder(filterBuilder);
                    }
                }

                LogicWraps linked = lwo.getLink();
                if (linked != null && filter != null) {
                    whereBuilder.AND();
                }
                if (linked != null) {
                    SQLBuilder child = this.buildWhereByLogicWraps(table, linked, tableAliasName);
                    if (child != null) {
                        whereBuilder.addParenthesisStart();
                        whereBuilder.addSQLBuilder(child);
                        whereBuilder.addParenthesisEnd();
                    }
                }
                if (iterator.hasNext() && (filter != null || linked != null)) {
                    if (lwo.getLogic() == CriteriaLogic.AND) whereBuilder.AND();
                    if (lwo.getLogic() == CriteriaLogic.OR) whereBuilder.OR();
                }
            }
            return whereBuilder;
        }
        return null;
    }

    protected void buildJoinFieldsSQL(Map<Object, List<SelectFieldAliasReference>> references,
                                      Map<Object, String> aliasMap,
                                      SQLBuilder sqlBuilder,
                                      List<Join> joins) {
        Iterator<Join> iterator = joins.iterator();
        while (iterator.hasNext()) {
            Join join = iterator.next();
            List<SelectFieldAliasReference> rfsj = references.get(join);

            if (rfsj != null) {
                String joinAliasTable = aliasMap.get(join);
                Iterator<SelectFieldAliasReference> iteratorAlias = rfsj.iterator();
                while (iteratorAlias.hasNext()) {
                    SelectFieldAliasReference f = iteratorAlias.next();
                    sqlBuilder.addTableWrapField(joinAliasTable, f.getFieldName()).AS().addWrapString(f.getFieldAliasName());
                    if (iteratorAlias.hasNext()) {
                        sqlBuilder.addSplit();
                    }
                }
            }
            if (iterator.hasNext()) {
                sqlBuilder.addSplit();
            }
        }
    }

    protected boolean buildFieldsSQL(List<SelectFieldAliasReference> rfs, SQLBuilder sqlBuilder, String tableAlias) {
        if (rfs != null) {
            Iterator<SelectFieldAliasReference> iterator = rfs.iterator();
            while (iterator.hasNext()) {
                SelectFieldAliasReference f = iterator.next();
                sqlBuilder.addTableWrapField(tableAlias, f.getFieldName()).AS().addWrapString(f.getFieldAliasName());
                if (iterator.hasNext()) {
                    sqlBuilder.addSplit();
                }
            }
            return true;
        }
        return false;
    }

    protected SQLBuilder buildSelectFromAs(SQLBuilder sqlBuilder) {
        sqlBuilder.AS();
        return sqlBuilder;
    }

    /**
     * 组建inner join查询
     *
     * @param tables
     * @param query
     * @param aliasMap
     * @param isSelect            是否是count查询
     * @param isSelectTableFields 是否查询主表的所有字段
     * @return
     */
    protected SQLBuilder buildSelectInnerJoin(Map<Object, MappingTable> tables,
                                              DefaultQuery query,
                                              Map<Object, String> aliasMap,
                                              boolean isSelect,
                                              boolean isSelectTableFields) {
        SQLBuilder sqlBuilder = this.createSQLBuilder();
        sqlBuilder.SELECT();
        List<Join> innerJoins = query.getInnerJoin();
        MappingTable mainTable = tables.get(query);
        String mainTableName = mainTable.getDatabaseTableName();
        String mainTableAlias = aliasMap.get(query);

        List<MappingField> pks = mainTable.getMappingPrimaryKeyFields();
        if (isSelect) {
            /**
             * 如果是查询表数据则，将要查询的表数据的字段加入到语句中去
             * 如果不是则表示查询主表的主键值，只把主键字段加入到语句中
             */
            if (isSelectTableFields) {
                Set<MappingField> columns = mainTable.getMappingColumns();
                if (columns != null) {
                    Iterator<MappingField> iterator = columns.iterator();
                    while (iterator.hasNext()) {
                        MappingField field = iterator.next();
                        String columnName = field.getDatabaseColumnName();
                        sqlBuilder.addTableWrapField(mainTableAlias, columnName)
                                .AS().addWrapString(columnName);
                        if (iterator.hasNext()) {
                            sqlBuilder.addSplit();
                        }
                    }
                }
            } else {
                if (pks != null) {
                    Iterator<MappingField> iterator = pks.iterator();
                    while (iterator.hasNext()) {
                        MappingField field = iterator.next();
                        String columnName = field.getDatabaseColumnName();
                        sqlBuilder.addTableWrapField(mainTableAlias, columnName)
                                .AS().addWrapString(columnName);
                        if (iterator.hasNext()) {
                            sqlBuilder.addSplit();
                        }
                    }
                }
            }
        } else {
            // 如果不是查询就是count数量
            sqlBuilder.addString(this.getSelectCountSymbol());
        }

        sqlBuilder.FROM().addWrapString(mainTableName).AS().addWrapString(mainTableAlias);
        if (this.hasInnerJoin(query)) {
            for (Join join : innerJoins) {
                DefaultJoin j = (DefaultJoin) join;
                MappingTable joinTable = tables.get(join);
                String joinTableName = joinTable.getDatabaseTableName();
                String joinTableAlias = aliasMap.get(join);

                sqlBuilder.addSplit();
                sqlBuilder.addWrapString(joinTableName).AS().addWrapString(joinTableAlias);
            }
        }

        LogicWraps<Filter> logicWraps = query.getLogicWraps();
        if (this.hasInnerJoin(query) || logicWraps != null) {
            sqlBuilder.WHERE();
        }

        boolean isHasJoinWhere = false;
        if (this.hasInnerJoin(query)) {
            Iterator<Join> iterator = innerJoins.iterator();
            while (iterator.hasNext()) {
                Join join = iterator.next();
                DefaultJoin j = (DefaultJoin) join;
                MappingTable joinTable = tables.get(join);
                String joinTableName = joinTable.getDatabaseTableName();
                String joinTableAlias = aliasMap.get(join);
                List<OnField> onFields = j.getOnFilters();
                List<Filter> valueFilters = j.getValueFilters();

                if (j.getMainTable() == query.getTableClass()) {
                    if (onFields != null && onFields.size() > 0) {
                        this.buildJoinOnField(sqlBuilder, onFields, mainTable, joinTable, joinTableAlias, mainTableAlias);
                        isHasJoinWhere = true;
                    }
                    if (onFields != null && onFields.size() > 0 && valueFilters != null && valueFilters.size() > 0) {
                        sqlBuilder.AND();
                    }
                    if (valueFilters != null && valueFilters.size() > 0) {
                        this.buildJoinValueField(sqlBuilder, valueFilters, joinTable, joinTableAlias);
                        isHasJoinWhere = true;
                    }
                } else {
                    // 暂时不考虑了
                }
            }
        }

        if (logicWraps != null) {
            SQLBuilder whereBuilder = this.buildWhereByLogicWraps(mainTable, logicWraps, mainTableAlias);
            if (whereBuilder != null && logicWraps.size() > 0) {
                if (isHasJoinWhere) {
                    sqlBuilder.AND().addSQLBuilder(whereBuilder);
                } else {
                    sqlBuilder.addSQLBuilder(whereBuilder);
                }
            }
        }
        // 添加Group By 主键
        if (pks != null) {
            Iterator<MappingField> iterator = pks.iterator();
            sqlBuilder.GROUP().BY();
            while (iterator.hasNext()) {
                MappingField field = iterator.next();
                String columnName = field.getDatabaseColumnName();
                sqlBuilder.addTableWrapField(mainTableAlias, columnName);
                if (iterator.hasNext()) {
                    sqlBuilder.addSplit();
                }
            }
        }
        if (isSelect) {
            SQLBuilder orderByBuilder = this.buildOrderBy(mainTable, query, mainTableAlias);
            sqlBuilder.addSQLBuilder(orderByBuilder);
            SQLBuilder limitBuilder = this.buildLimit(query);
            sqlBuilder.addSQLBuilder(limitBuilder);
        }

        return sqlBuilder;
    }

    protected abstract DifferentColumn getDifferentColumn();

    protected SQLBuilder createSQLBuilder() {
        SQLBuilder builder = SQLBuilderFactory.createSQLBuilder();
        return builder;
    }

    protected String getSelectCountSymbol() {
        return "1";
    }

    protected boolean hasJoins(DefaultQuery query) {
        return !((query.getLeftJoin() == null || query.getLeftJoin().size() == 0) && (query.getInnerJoin() == null || query.getInnerJoin().size() == 0));
    }

    protected boolean hasLeftJoin(DefaultQuery query) {
        return !(query.getLeftJoin() == null || query.getLeftJoin().size() == 0);
    }

    protected boolean hasInnerJoin(DefaultQuery query) {
        return !(query.getInnerJoin() == null || query.getInnerJoin().size() == 0);
    }
}
