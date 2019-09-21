package org.mimosaframework.orm.platform;

import org.mimosaframework.core.json.ModelObject;
import org.mimosaframework.orm.ModelObjectConvertKey;
import org.mimosaframework.orm.criteria.*;
import org.mimosaframework.orm.mapping.MappingField;
import org.mimosaframework.orm.mapping.MappingTable;
import org.mimosaframework.orm.mapping.SpecificMappingTable;
import org.mimosaframework.orm.merge.DefaultModelMerge;
import org.mimosaframework.orm.merge.MergeTree;
import org.mimosaframework.orm.merge.ModelMerge;

import java.sql.SQLException;
import java.util.*;

public class PlatformWrapperImpl implements PlatformWrapper {
    private DatabasePorter databasePorter;
    private CarryHandler carryHandler;

    public PlatformWrapperImpl(DatabasePorter databasePorter, CarryHandler carryHandler) {
        this.databasePorter = databasePorter;
        this.carryHandler = carryHandler;
    }

    public DatabasePorter getDatabasePorter() {
        return databasePorter;
    }

    public CarryHandler getCarryHandler() {
        return carryHandler;
    }

    @Override
    public void createTable(MappingTable table) throws SQLException {
        PorterStructure[] structures = databasePorter.createTable(table);
        carryHandler.doHandler(structures);
    }

    @Override
    public void dropTable(String tableName) throws SQLException {
        PorterStructure[] structures = databasePorter.dropTable(tableName);
        carryHandler.doHandler(structures);
    }

    @Override
    public void addField(String table, MappingField field) throws SQLException {
        SpecificMappingTable mytable = new SpecificMappingTable();
        mytable.setDatabaseTableName(table);
        field.setMappingTable(mytable);
        PorterStructure[] structures = databasePorter.createField(field);
        carryHandler.doHandler(structures);
    }

    @Override
    public void dropField(String table, MappingField field) throws SQLException {
        PorterStructure[] structures = databasePorter.dropField(table, field);
        carryHandler.doHandler(structures);
    }

    @Override
    public Long insert(MappingTable table, ModelObject object) throws SQLException {
        PorterStructure[] structures = this.databasePorter.insert(table, object);
        Long id = (Long) carryHandler.doHandler(structures);
        return id;
    }

    @Override
    public List<Long> inserts(MappingTable table, List<ModelObject> objects) throws SQLException {
        PorterStructure[] structures = this.databasePorter.inserts(table, objects);
        List<Long> ids = (List<Long>) carryHandler.doHandler(structures);
        return ids;
    }

    @Override
    public Integer update(MappingTable table, ModelObject object) throws SQLException {
        PorterStructure[] structures = this.databasePorter.update(table, object);
        return (Integer) carryHandler.doHandler(structures);
    }

    @Override
    public Integer update(MappingTable table, DefaultUpdate update) throws SQLException {
        PorterStructure[] structures = this.databasePorter.update(table, update);
        return (Integer) carryHandler.doHandler(structures);
    }

    @Override
    public Integer update(String sql) throws SQLException {
        SQLBuilder builder = SQLBuilderFactory.createSQLBuilder();
        builder.addSQLString(sql);
        PorterStructure[] structures = new PorterStructure[]{new PorterStructure(ChangerClassify.UPDATE, builder)};
        return (Integer) carryHandler.doHandler(structures);
    }

    @Override
    public int delete(MappingTable table, ModelObject object) throws SQLException {
        PorterStructure[] structures = this.databasePorter.delete(table, object);
        return (Integer) carryHandler.doHandler(structures);
    }

    @Override
    public int delete(MappingTable table, DefaultDelete delete) throws SQLException {
        PorterStructure[] structures = this.databasePorter.delete(table, delete);
        return (Integer) carryHandler.doHandler(structures);
    }

    @Override
    public List<ModelObject> select(Map<Object, MappingTable> tables, DefaultQuery query, ModelObjectConvertKey convert) throws SQLException {
        if (query.hasInnerJoin() || query.hasLeftJoin()) {
            PorterStructure[] structures = this.databasePorter.selectPrimaryKey(tables, query);

            List<ModelObject> ids = (List<ModelObject>) carryHandler.doHandler(structures);
            if (ids != null && ids.size() > 0) {
                DefaultQuery newQuery = (DefaultQuery) query.clone();
                // 清楚分页信息inner join信息和where条件
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
                PorterStructure[] newStructure = this.databasePorter.select(tables, newQuery);
                List<ModelObject> objects = (List<ModelObject>) carryHandler.doHandler(newStructure);
                return buildMergeObjects(newStructure[0].getReferences(), newQuery, convert, objects);
            }
            return null;
        } else {
            PorterStructure[] structures = this.databasePorter.select(tables, query);
            List<ModelObject> objects = (List<ModelObject>) carryHandler.doHandler(structures);
            if (objects != null) {
                return buildMergeObjects(structures[0].getReferences(), query, convert, objects);
            }
            return null;
        }
    }

    @Override
    public List<ModelObject> select(Map<Object, MappingTable> tables, DefaultQuery query) throws SQLException {
        query.clearLeftJoin();
        PorterStructure[] structures = this.databasePorter.selectPrimaryKey(tables, query);
        List<ModelObject> objects = (List<ModelObject>) carryHandler.doHandler(structures);
        return objects;
    }

    @Override
    public ModelObject select(MappingTable table, DefaultFunction function) throws SQLException {
        PorterStructure[] structures = this.databasePorter.select(table, function);
        List<ModelObject> objects = (List<ModelObject>) carryHandler.doHandler(structures);
        if (objects != null && objects.size() > 0) {
            return objects.get(0);
        }
        return null;
    }

    @Override
    public List<ModelObject> select(String sql) throws SQLException {
        SQLBuilder builder = SQLBuilderFactory.createSQLBuilder();
        builder.addSQLString(sql);
        PorterStructure[] structures = new PorterStructure[]{new PorterStructure(ChangerClassify.SELECT, builder)};
        List<ModelObject> objects = (List<ModelObject>) carryHandler.doHandler(structures);
        return objects;
    }

    @Override
    public long count(Map<Object, MappingTable> tables, DefaultQuery query) throws SQLException {
        PorterStructure[] structures = this.databasePorter.count(tables, query);
        List<ModelObject> objects = (List<ModelObject>) carryHandler.doHandler(structures);
        if (objects != null && objects.size() > 0) {
            return objects.get(0).getLongValue("count");
        }
        return 0;
    }

    @Override
    public Long simpleInsert(String table, ModelObject object) throws SQLException {
        PorterStructure[] structures = this.databasePorter.simpleInsert(table, object);
        Long id = (Long) carryHandler.doHandler(structures);
        return id;
    }

    @Override
    public int simpleDelete(String table, ModelObject where) throws SQLException {
        PorterStructure[] structures = this.databasePorter.simpleDelete(table, where);
        return (Integer) carryHandler.doHandler(structures);
    }

    @Override
    public int simpleUpdate(String table, ModelObject object, ModelObject where) throws SQLException {
        PorterStructure[] structures = this.databasePorter.simpleUpdate(table, object, where);
        return (Integer) carryHandler.doHandler(structures);
    }

    @Override
    public List<ModelObject> simpleSelect(String table, ModelObject where) throws SQLException {
        PorterStructure[] structures = this.databasePorter.simpleSelect(table, where);
        List<ModelObject> objects = (List<ModelObject>) carryHandler.doHandler(structures);
        return objects;
    }

    @Override
    public long simpleCount(String table, ModelObject where) throws SQLException {
        PorterStructure[] structures = this.databasePorter.simpleCount(table, where);
        List<ModelObject> objects = (List<ModelObject>) carryHandler.doHandler(structures);
        if (objects != null && objects.size() > 0) {
            objects.get(0).getLongValue("count");
        }
        return 0;
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
