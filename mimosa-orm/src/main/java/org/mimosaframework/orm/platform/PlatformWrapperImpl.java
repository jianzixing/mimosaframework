package org.mimosaframework.orm.platform;

import org.mimosaframework.core.json.ModelObject;
import org.mimosaframework.core.utils.i18n.Messages;
import org.mimosaframework.core.utils.StringTools;
import org.mimosaframework.orm.ModelObjectConvertKey;
import org.mimosaframework.orm.criteria.*;
import org.mimosaframework.orm.i18n.LanguageMessageFactory;
import org.mimosaframework.orm.mapping.MappingField;
import org.mimosaframework.orm.mapping.MappingGlobalWrapper;
import org.mimosaframework.orm.mapping.MappingTable;
import org.mimosaframework.orm.mapping.SpecificMappingTable;
import org.mimosaframework.orm.merge.DefaultModelMerge;
import org.mimosaframework.orm.merge.MergeTree;
import org.mimosaframework.orm.merge.ModelMerge;
import org.mimosaframework.orm.sql.stamp.StampAction;

import java.sql.SQLException;
import java.util.*;

public class PlatformWrapperImpl implements PlatformWrapper {
    private DatabasePorter databasePorter;
    private DBRunner carryHandler;

    public PlatformWrapperImpl(DatabasePorter databasePorter, DBRunner carryHandler) {
        this.databasePorter = databasePorter;
        this.carryHandler = carryHandler;
    }

    public DatabasePorter getDatabasePorter() {
        return databasePorter;
    }

    public DBRunner getCarryHandler() {
        return carryHandler;
    }

    private void checkMappingTableInDatabase(MappingTable table) {
        if (table != null) {
            if (StringTools.isEmpty(table.getDatabaseTableName())) {
                throw new IllegalArgumentException(Messages.get(LanguageMessageFactory.PROJECT,
                        PlatformWrapperImpl.class, "not_fount_db_table",
                        table.getMappingClassName(), table.getMappingTableName()));
            }
        }
    }

    @Override
    public void createTable(MappingTable table) throws SQLException {
        this.checkMappingTableInDatabase(table);
        databasePorter.createTable(table);
    }

    @Override
    public void dropTable(String tableName) throws SQLException {
        databasePorter.dropTable(tableName);
    }

    @Override
    public void addField(String table, MappingField field) throws SQLException {
        SpecificMappingTable mytable = new SpecificMappingTable();
        mytable.setDatabaseTableName(table);
        field.setMappingTable(mytable);
        databasePorter.createField(field);
    }

    @Override
    public void dropField(String table, MappingField field) throws SQLException {
        databasePorter.dropField(table, field);
    }

    @Override
    public Long insert(MappingTable table, ModelObject object) throws SQLException {
        this.checkMappingTableInDatabase(table);
        return this.databasePorter.insert(table, object);
    }

    @Override
    public List<Long> inserts(MappingTable table, List<ModelObject> objects) throws SQLException {
        this.checkMappingTableInDatabase(table);
        return this.databasePorter.inserts(table, objects);
    }

    @Override
    public Integer update(MappingTable table, ModelObject object) throws SQLException {
        this.checkMappingTableInDatabase(table);
        return this.databasePorter.update(table, object);
    }

    @Override
    public Integer update(MappingTable table, DefaultUpdate update) throws SQLException {
        this.checkMappingTableInDatabase(table);
        return this.databasePorter.update(table, update);
    }

    @Override
    public Integer update(String sql) throws SQLException {
        SQLBuilder builder = SQLBuilderFactory.createSQLBuilder();
        builder.addSQLString(sql);
        return (Integer) carryHandler.doHandler(new JDBCTraversing(TypeForRunner.UPDATE, builder));
    }

    @Override
    public Integer delete(MappingTable table, ModelObject object) throws SQLException {
        this.checkMappingTableInDatabase(table);
        return this.databasePorter.delete(table, object);
    }

    @Override
    public Integer delete(MappingTable table, DefaultDelete delete) throws SQLException {
        this.checkMappingTableInDatabase(table);
        return this.databasePorter.delete(table, delete);
    }

    private boolean isNeedSelectPrimaryKey(DefaultQuery query) {
        Limit limit = query.getLimit();
        if (limit != null && (query.hasInnerJoin() || query.hasLeftJoin())) {
            return true;
        }
        return false;
    }

    @Override
    public List<ModelObject> select(Map<Object, MappingTable> tables, DefaultQuery query, ModelObjectConvertKey convert) throws SQLException {
        if (tables != null) {
            Set<Map.Entry<Object, MappingTable>> entries = tables.entrySet();
            for (Map.Entry<Object, MappingTable> entry : entries) {
                this.checkMappingTableInDatabase(entry.getValue());
            }
        }

        if (this.isNeedSelectPrimaryKey(query)) {
            List<ModelObject> ids = this.databasePorter.selectPrimaryKey(tables, query);
            if (ids != null && ids.size() > 0) {
                DefaultQuery newQuery = (DefaultQuery) query.clone();
                // 清除分页信息inner join信息和where条件
                newQuery.removeLimit();
                newQuery.clearFilters();
                // 然后将inner join转换成left join
                List<Join> innerJoins = query.getInnerJoin();
                if (innerJoins != null && innerJoins.size() > 0) {
                    for (Join join : innerJoins) {
                        newQuery.addSubjoin(join);
                    }
                }

                MappingTable table = tables.get(query);

                List<MappingField> fields = table.getMappingPrimaryKeyFields();
                if (fields.size() == 1) {
                    Set idvalues = new LinkedHashSet();
                    for (ModelObject idobject : ids) {
                        idvalues.add(idobject.get(fields.get(0).getDatabaseColumnName()));
                    }
                    newQuery.in(fields.get(0).getMappingFieldName(), new ArrayList<>(idvalues));
                } else if (fields.size() > 1) {
                    for (ModelObject id : ids) {
                        LogicLinked logicLinked = LogicLinked.getInstance();
                        for (MappingField f : fields) {
                            logicLinked.and(Criteria.filter().eq(f.getMappingFieldName(), id.get(f.getDatabaseColumnName())));
                        }
                        newQuery.orLinked(logicLinked);
                    }
                }
                //重新组建tables
                tables = new LinkedHashMap<>(tables);
                tables.put(newQuery, tables.get(query));
                tables.remove(query);
                SelectResult selectResult = this.databasePorter.select(tables, newQuery);
                return buildMergeObjects(selectResult.getStructure().getReferences(), newQuery, convert, selectResult.getObjects());
            }
            return null;
        } else {
            SelectResult selectResult = this.databasePorter.select(tables, query);
            if (selectResult != null) {
                return buildMergeObjects(selectResult.getStructure().getReferences(), query, convert, selectResult.getObjects());
            }
            return null;
        }
    }

    @Override
    public List<ModelObject> select(Map<Object, MappingTable> tables, DefaultQuery query) throws SQLException {
        if (tables != null) {
            Set<Map.Entry<Object, MappingTable>> entries = tables.entrySet();
            for (Map.Entry<Object, MappingTable> entry : entries) {
                this.checkMappingTableInDatabase(entry.getValue());
            }
        }

        query.clearLeftJoin();
        List<ModelObject> objects = this.databasePorter.selectPrimaryKey(tables, query);
        return objects;
    }

    @Override
    public List<ModelObject> select(MappingTable table, DefaultFunction function) throws SQLException {
        this.checkMappingTableInDatabase(table);

        List<ModelObject> objects = this.databasePorter.select(table, function);
        return objects;
    }

    @Override
    public List<ModelObject> select(String sql) throws SQLException {
        SQLBuilder builder = SQLBuilderFactory.createSQLBuilder();
        builder.addSQLString(sql);
        List<ModelObject> objects = (List<ModelObject>) carryHandler.doHandler(new JDBCTraversing(TypeForRunner.SELECT, builder));
        return objects;
    }

    @Override
    public long count(Map<Object, MappingTable> tables, DefaultQuery query) throws SQLException {
        if (tables != null) {
            Set<Map.Entry<Object, MappingTable>> entries = tables.entrySet();
            for (Map.Entry<Object, MappingTable> entry : entries) {
                this.checkMappingTableInDatabase(entry.getValue());
            }
        }

        List<ModelObject> objects = this.databasePorter.count(tables, query);
        if (objects != null && objects.size() > 0) {
            return objects.get(0).getLongValue("count");
        }
        return 0;
    }

    @Override
    public Object execute(MappingGlobalWrapper mappingGlobalWrapper, StampAction stampAction) throws SQLException {
        return this.databasePorter.execute(mappingGlobalWrapper, stampAction);
    }

    private List<ModelObject> buildMergeObjects(Map<Object, List<SelectFieldAliasReference>> references,
                                                DefaultQuery query,
                                                ModelObjectConvertKey convert,
                                                List<ModelObject> os) {
        List<SelectFieldAliasReference> selectFields = null;
        if (references != null) {
            selectFields = new ArrayList<>();
            Iterator<Map.Entry<Object, List<SelectFieldAliasReference>>> iterator = references.entrySet().iterator();
            while (iterator.hasNext()) {
                Map.Entry<Object, List<SelectFieldAliasReference>> entry = iterator.next();
                List<SelectFieldAliasReference> ref = entry.getValue();
                selectFields.addAll(ref);
            }
        }

        List<MergeTree> mergeTrees = new ArrayList();
        MergeTree top = new MergeTree();
        top.setMainTable(query.getTableClass());
        top.setSelfTable(query.getTableClass());
        if (references != null) {
            List<SelectFieldAliasReference> mainFields = references.get(query);
            if (mainFields != null) {
                top.setMapperSelectFields(mainFields);
            }
        }
        mergeTrees.add(top);

        List<Join> leftJoin = query.getLeftJoin();
        List<Join> innerJoin = query.getInnerJoin();
        int cap = 0;
        if (leftJoin != null) cap += leftJoin.size();
        if (innerJoin != null) cap += innerJoin.size();
        List<Join> joins = new ArrayList<>(cap);
        if (leftJoin != null) joins.addAll(leftJoin);
        if (innerJoin != null) joins.addAll(innerJoin);

        if (joins != null) {
            for (Join join : joins) {
                MergeTree jm = new MergeTree();
                jm.setJoin(join);
                DefaultJoin j = (DefaultJoin) join;
                if (!j.isMulti()) {
                    jm.setMulti(false);
                }
                Class<?> c1 = j.getMainTable();
                Class<?> c2 = j.getTable();
                jm.setExternalConnectionName(j.getAliasName());

                jm.setMainTable(c1);
                jm.setSelfTable(c2);
                jm.setTableAliasName(j.getTableClassAliasName());

                if (references != null) {
                    List<SelectFieldAliasReference> fields = references.get(join);
                    jm.setMapperSelectFields(fields);
                }

                if (((DefaultJoin) join).getParentJoin() == null) {
                    top.addChildren(jm);
                    jm.setParent(top);
                } else {
                    for (MergeTree m : mergeTrees) {
                        if (m.getJoin() == ((DefaultJoin) join).getParentJoin()) {
                            m.addChildren(jm);
                            jm.setParent(m);
                        }
                    }
                }

                mergeTrees.add(jm);
            }
        }

        ModelMerge modelMerge = new DefaultModelMerge();
        modelMerge.setMergeTree(top);
        modelMerge.setMapperSelectFields(selectFields);
        modelMerge.setMappingNamedConvert(convert);
        return modelMerge.getMergeAfterObjects(os, query.getTableClass());
    }
}
