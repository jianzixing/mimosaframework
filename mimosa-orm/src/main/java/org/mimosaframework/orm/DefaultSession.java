package org.mimosaframework.orm;

import org.mimosaframework.core.json.ModelObject;
import org.mimosaframework.core.utils.AssistUtils;
import org.mimosaframework.core.utils.StringTools;
import org.mimosaframework.core.utils.i18n.Messages;
import org.mimosaframework.orm.criteria.*;
import org.mimosaframework.orm.exception.StrategyException;
import org.mimosaframework.orm.i18n.LanguageMessageFactory;
import org.mimosaframework.orm.mapping.MappingField;
import org.mimosaframework.orm.mapping.MappingGlobalWrapper;
import org.mimosaframework.orm.mapping.MappingTable;
import org.mimosaframework.orm.platform.*;
import org.mimosaframework.orm.scripting.BoundSql;
import org.mimosaframework.orm.scripting.DynamicSqlSource;
import org.mimosaframework.orm.scripting.SQLDefinedLoader;
import org.mimosaframework.orm.sql.Builder;
import org.mimosaframework.orm.sql.ResultMappingFieldUtils;
import org.mimosaframework.orm.sql.SelectBuilder;
import org.mimosaframework.orm.strategy.StrategyFactory;
import org.mimosaframework.orm.utils.Clone;
import org.mimosaframework.orm.utils.SessionUtils;
import org.mimosaframework.orm.utils.TypeCorrectUtils;

import javax.sql.DataSource;
import java.io.IOException;
import java.io.Serializable;
import java.sql.SQLException;
import java.util.*;

public class DefaultSession implements Session {
    private UpdateSkipReset updateSkipReset = new UpdateSkiptResetEmpty();
    private ContextContainer context;
    private ActionDataSourceWrapper wrapper;
    private MappingGlobalWrapper mappingGlobalWrapper;
    private ModelObjectConvertKey convert;

    public DefaultSession(ContextContainer context) {
        this.context = context;
        this.wrapper = this.context.getDefaultDataSourceWrapper(true);
        this.mappingGlobalWrapper = this.context.getMappingGlobalWrapper();
        this.convert = this.context.getModelObjectConvertKey();
        convert.setMappingGlobalWrapper(mappingGlobalWrapper);
    }


    @Override
    public ModelObject save(ModelObject objSource) {
        if (objSource == null || objSource.size() == 0) {
            throw new IllegalArgumentException(Messages.get(LanguageMessageFactory.PROJECT, DefaultSession.class, "save_empty"));
        }
        ModelObject obj = Clone.cloneModelObject(objSource);
        Class c = obj.getObjectClass();
        if (c == null) {
            throw new IllegalArgumentException(Messages.get(LanguageMessageFactory.PROJECT, DefaultSession.class, "miss_table_class"));
        }
        SessionUtils.clearModelObject(this.mappingGlobalWrapper, c, obj);
        MappingTable mappingTable = this.mappingGlobalWrapper.getMappingTable(c);
        AssistUtils.notNull(mappingTable, Messages.get(LanguageMessageFactory.PROJECT,
                DefaultSession.class, "not_found_mapping", c.getName()));

        // 开始类型矫正
        TypeCorrectUtils.correct(obj, mappingTable);

        if (mappingTable != null) {
            try {
                StrategyFactory.applyStrategy(this.context, mappingTable, obj, this);
            } catch (StrategyException e) {
                throw new IllegalArgumentException(Messages.get(LanguageMessageFactory.PROJECT,
                        DefaultSession.class, "id_strategy_error"), e.getCause());
            }
            // 转换成数据库的字段
            obj = convert.convert(c, obj);

            PlatformWrapper platformWrapper = PlatformFactory.getPlatformWrapper(wrapper);
            // 添加后如果有自增列，则返回如果没有就不返回
            Long id = null;
            try {
                id = platformWrapper.insert(mappingTable, obj);
            } catch (SQLException e) {
                throw new IllegalStateException(Messages.get(LanguageMessageFactory.PROJECT,
                        DefaultSession.class, "add_data_error"), e);
            }
            SessionUtils.applyAutoIncrementValue(mappingTable, id, objSource);

            return objSource;
        }

        return objSource;
    }

    @Override
    public ModelObject saveAndUpdate(ModelObject objSource) {
        if (objSource == null || objSource.size() == 0) {
            throw new IllegalArgumentException(Messages.get(LanguageMessageFactory.PROJECT, DefaultSession.class, "save_empty"));
        }
        if (objSource.getObjectClass() == null) {
            throw new IllegalArgumentException(Messages.get(LanguageMessageFactory.PROJECT, DefaultSession.class, "miss_table_class"));
        }
        ModelObject obj = Clone.cloneModelObject(objSource);
        Class c = obj.getObjectClass();
        MappingTable mappingTable = this.mappingGlobalWrapper.getMappingTable(c);
        AssistUtils.notNull(mappingTable, Messages.get(LanguageMessageFactory.PROJECT,
                DefaultSession.class, "not_found_mapping", c.getName()));

        List<MappingField> pks = mappingTable.getMappingPrimaryKeyFields();
        Query query = Criteria.query(c);

        int countpk = 0;
        if (pks != null) {
            for (MappingField field : pks) {
                Object pkvalue = obj.get(field.getMappingFieldName());
                if (pkvalue == null) {

                } else {
                    query.eq(field.getMappingFieldName(), obj.get(field.getMappingFieldName()));
                    countpk++;
                }
            }
        }

        if (countpk == 0) {
            this.save(obj);
        } else {
            ModelObject exist = this.get(query);
            if (exist != null) {
                this.update(obj);
            } else {
                this.save(obj);
            }
        }


        return obj;
    }

    @Override
    public void save(List<ModelObject> objectSources) {
        if (objectSources == null || objectSources.size() == 0) {
            throw new IllegalArgumentException(Messages.get(LanguageMessageFactory.PROJECT, DefaultSession.class, "save_empty"));
        }

        List<ModelObject> objects = new ArrayList<ModelObject>(objectSources);
        List<ModelObject> saves = new ArrayList<ModelObject>();
        Class tableClass = null;
        MappingTable mappingTable = null;
        SessionUtils.checkReference(objects);
        for (ModelObject object : objects) {
            if (object == null || object.size() == 0) {
                throw new IllegalArgumentException(Messages.get(LanguageMessageFactory.PROJECT,
                        DefaultSession.class, "batch_save_empty"));
            }
            Class c = object.getObjectClass();
            SessionUtils.clearModelObject(this.mappingGlobalWrapper, c, object);

            if (tableClass == null) tableClass = c;
            if (tableClass != null && tableClass != c) {
                throw new IllegalArgumentException(Messages.get(LanguageMessageFactory.PROJECT,
                        DefaultSession.class, "batch_save_table_diff",
                        tableClass.getSimpleName(), c.getSimpleName()));
            }
            tableClass = c;

            if (mappingTable == null) {
                mappingTable = this.mappingGlobalWrapper.getMappingTable(c);
            }
            AssistUtils.notNull(mappingTable, Messages.get(LanguageMessageFactory.PROJECT,
                    DefaultSession.class, "not_found_mapping", c.getName()));

            try {
                StrategyFactory.applyStrategy(this.context, mappingTable, object, this);
            } catch (StrategyException e) {
                throw new IllegalArgumentException(Messages.get(LanguageMessageFactory.PROJECT,
                        DefaultSession.class, "id_strategy_error"), e.getCause());
            }

            // 开始类型矫正
            TypeCorrectUtils.correct(object, mappingTable);
            object = convert.convert(c, object);
            if (object.size() > 0) {
                saves.add(object);
            }
        }

        PlatformWrapper platformWrapper = PlatformFactory.getPlatformWrapper(wrapper);
        // 添加后如果有自增列，则返回如果没有就不返回
        List<Long> ids = null;
        try {
            ids = platformWrapper.inserts(mappingTable, saves);
        } catch (SQLException e) {
            throw new IllegalStateException(Messages.get(LanguageMessageFactory.PROJECT,
                    DefaultSession.class, "batch_save_data_error"), e);
        }
        SessionUtils.applyAutoIncrementValue(mappingTable, ids, objectSources);
    }

    @Override
    public void update(ModelObject obj) {
        if (obj == null || obj.size() == 0) {
            throw new IllegalArgumentException(Messages.get(LanguageMessageFactory.PROJECT,
                    DefaultSession.class, "update_empty"));
        }
        obj = Clone.cloneModelObject(obj);
        Class c = obj.getObjectClass();
        if (c == null) {
            throw new IllegalArgumentException(Messages.get(LanguageMessageFactory.PROJECT, DefaultSession.class, "miss_table_class"));
        }
        MappingTable mappingTable = this.mappingGlobalWrapper.getMappingTable(c);
        AssistUtils.notNull(mappingTable, Messages.get(LanguageMessageFactory.PROJECT,
                DefaultSession.class, "not_found_mapping", c.getName()));

        updateSkipReset.skip(obj, mappingTable);
        SessionUtils.clearModelObject(this.mappingGlobalWrapper, obj.getObjectClass(), obj);

        List<MappingField> pks = mappingTable.getMappingPrimaryKeyFields();
        if (obj.size() - pks.size() <= 0) {
            //如果只有主键值没有需要更新的值就不执行更新操作
            return;
        }
        if (!SessionUtils.checkPrimaryKey(pks, obj)) {
            throw new IllegalArgumentException(Messages.get(LanguageMessageFactory.PROJECT,
                    DefaultSession.class, "update_set_id"));
        }

        // 开始类型矫正
        TypeCorrectUtils.correct(obj, mappingTable);

        obj = convert.convert(c, obj);
        PlatformWrapper platformWrapper = PlatformFactory.getPlatformWrapper(wrapper);
        try {
            platformWrapper.update(mappingTable, obj);
        } catch (SQLException e) {
            throw new IllegalStateException(Messages.get(LanguageMessageFactory.PROJECT,
                    DefaultSession.class, "update_fail"), e);
        }
    }

    @Override
    public void update(List<ModelObject> objects) {
        for (ModelObject o : objects) {
            this.update(o);
        }
    }

    @Override
    public long update(Update update) {
        DefaultUpdate u = (DefaultUpdate) update;
        if (u.getLogicWraps() == null || u.getValues().size() == 0) {
            throw new IllegalArgumentException(Messages.get(LanguageMessageFactory.PROJECT,
                    DefaultSession.class, "update_filter_empty"));
        }
        Class c = u.getTableClass();
        MappingTable mappingTable = this.mappingGlobalWrapper.getMappingTable(c);
        AssistUtils.notNull(mappingTable, Messages.get(LanguageMessageFactory.PROJECT,
                DefaultSession.class, "not_found_mapping", c.getName()));

        PlatformWrapper platformWrapper = PlatformFactory.getPlatformWrapper(wrapper);
        int count = 0;
        try {
            count = platformWrapper.update(mappingTable, u);
        } catch (SQLException e) {
            throw new IllegalStateException(Messages.get(LanguageMessageFactory.PROJECT,
                    DefaultSession.class, "update_fail"), e);
        }

        return count;
    }

    @Override
    public void delete(ModelObject objSource) {
        ModelObject obj = Clone.cloneModelObject(objSource);
        Class c = obj.getObjectClass();
        MappingTable mappingTable = this.mappingGlobalWrapper.getMappingTable(c);
        AssistUtils.notNull(mappingTable, Messages.get(LanguageMessageFactory.PROJECT,
                DefaultSession.class, "not_found_mapping", c.getName()));

        if (!SessionUtils.checkPrimaryKey(mappingTable.getMappingPrimaryKeyFields(), obj)) {
            throw new IllegalArgumentException(Messages.get(LanguageMessageFactory.PROJECT,
                    DefaultSession.class, "delete_id"));
        }

        PlatformWrapper platformWrapper = PlatformFactory.getPlatformWrapper(wrapper);
        obj = convert.convert(c, obj);

        try {
            platformWrapper.delete(mappingTable, obj);
        } catch (SQLException e) {
            throw new IllegalStateException(Messages.get(LanguageMessageFactory.PROJECT,
                    DefaultSession.class, "delete_fail"), e);
        }
    }

    @Override
    public void delete(List<ModelObject> objects) {
        for (ModelObject o : objects) {
            this.delete(o);
        }
    }

    @Override
    public long delete(Delete delete) {
        DefaultDelete d = (DefaultDelete) delete;
        if (d.getLogicWraps() == null) {
            throw new IllegalArgumentException(Messages.get(LanguageMessageFactory.PROJECT,
                    DefaultSession.class, "delete_filter_empty"));
        }

        Class c = d.getTableClass();
        MappingTable mappingTable = this.mappingGlobalWrapper.getMappingTable(c);
        AssistUtils.notNull(mappingTable, Messages.get(LanguageMessageFactory.PROJECT,
                DefaultSession.class, "not_found_mapping", c.getName()));

        PlatformWrapper platformWrapper = PlatformFactory.getPlatformWrapper(wrapper);
        try {
            return platformWrapper.delete(mappingTable, d);
        } catch (SQLException e) {
            throw new IllegalStateException(Messages.get(LanguageMessageFactory.PROJECT,
                    DefaultSession.class, "delete_fail"), e);
        }
    }

    @Override
    public void delete(Class c, Serializable id) {
        MappingTable mappingTable = this.mappingGlobalWrapper.getMappingTable(c);
        AssistUtils.notNull(mappingTable, Messages.get(LanguageMessageFactory.PROJECT,
                DefaultSession.class, "not_found_mapping", c.getName()));

        List<MappingField> pks = mappingTable.getMappingPrimaryKeyFields();

        if (pks.size() != 1) {
            throw new IllegalArgumentException(Messages.get(LanguageMessageFactory.PROJECT,
                    DefaultSession.class, "delete_only_pk", c.getSimpleName(), "" + pks.size()));
        }

        Delete delete = new DefaultDelete(c, this);
        delete.addFilter().eq(pks.get(0).getMappingFieldName(), id);
        this.delete(delete);
    }

    @Override
    public ModelObject get(Class c, Serializable id) {
        MappingTable mappingTable = this.mappingGlobalWrapper.getMappingTable(c);
        AssistUtils.notNull(mappingTable, Messages.get(LanguageMessageFactory.PROJECT,
                DefaultSession.class, "not_found_mapping", c.getName()));

        List<MappingField> pks = mappingTable.getMappingPrimaryKeyFields();
        if (pks.size() != 1) {
            throw new IllegalArgumentException(Messages.get(LanguageMessageFactory.PROJECT,
                    DefaultSession.class, "delete_only_pk", c.getSimpleName(), "" + pks.size()));
        }

        Query query = new DefaultQuery(c);
        query.eq(pks.get(0).getMappingFieldName(), id);
        List results = this.list(query);

        if (results != null) {
            if (results.size() > 1) {
                throw new IllegalArgumentException(Messages.get(LanguageMessageFactory.PROJECT,
                        DefaultSession.class, "query_only_pk", "" + results.size()));
            } else if (results.size() == 1) {
                return (ModelObject) results.get(0);
            }
        }
        return null;
    }

    @Override
    public ModelObject get(Query query) {
        if (query != null) {
            query.limit(0, 1); // 强制取一条
            List<ModelObject> objects = this.list(query);
            if (objects == null || objects.size() == 0) return null;
            return objects.get(0);
        }
        return null;
    }

    @Override
    public List<ModelObject> list(Query query) {
        DefaultQuery dq = (DefaultQuery) query;
        dq.checkQuery();
        wrapper.setMaster(dq.isMaster());
        wrapper.setSlaveName(dq.getSlaveName());

        SessionUtils.processQueryExcludes(this.mappingGlobalWrapper, dq);
        Map<Object, MappingTable> tables = SessionUtils.getUsedMappingTable(this.mappingGlobalWrapper, dq);

        PlatformWrapper platformWrapper = PlatformFactory.getPlatformWrapper(wrapper);
        List<ModelObject> objects = null;
        try {
            objects = platformWrapper.select(tables, dq, convert);
        } catch (SQLException e) {
            throw new IllegalStateException(Messages.get(LanguageMessageFactory.PROJECT,
                    DefaultSession.class, "get_data_fail"), e);
        }

        return objects;
    }

    @Override
    public long count(Query query) {
        DefaultQuery dq = (DefaultQuery) query;
        dq.checkQuery();
        wrapper.setMaster(dq.isMaster());
        wrapper.setSlaveName(dq.getSlaveName());

        Map<Object, MappingTable> tables = SessionUtils.getUsedMappingTable(this.mappingGlobalWrapper, dq);
        PlatformWrapper platformWrapper = PlatformFactory.getPlatformWrapper(wrapper);
        long count = 0;
        try {
            count = platformWrapper.count(tables, dq);
        } catch (SQLException e) {
            throw new IllegalStateException(Messages.get(LanguageMessageFactory.PROJECT,
                    DefaultSession.class, "get_data_count_fail"), e);
        }

        return count;
    }

    @Override
    public Paging<ModelObject> paging(Query query) {
        Paging paging = new Paging();
        long count = this.count(query);
        List<ModelObject> objects = this.list(query);
        paging.setCount(count);
        paging.setObjects(objects);
        return paging;
    }

    @Override
    public ZipperTable<ModelObject> getZipperTable(Class c) {
        MappingTable mappingTable = this.mappingGlobalWrapper.getMappingTable(c);
        AssistUtils.notNull(mappingTable, Messages.get(LanguageMessageFactory.PROJECT,
                DefaultSession.class, "not_found_mapping", c.getName()));

        MimosaDataSource ds = this.wrapper.getDataSource();
        return new SingleZipperTable<ModelObject>(context, c, ds, mappingTable.getDatabaseTableName());
    }

    @Override
    public AutoResult calculate(Function function) {
        DefaultFunction f = (DefaultFunction) function;
        if (f.getTableClass() == null) {
            throw new IllegalArgumentException(Messages.get(LanguageMessageFactory.PROJECT,
                    DefaultSession.class, "not_fount_class"));
        }

        if (f.getFuns() == null || f.getFuns().size() == 0) {
            throw new IllegalArgumentException(Messages.get(LanguageMessageFactory.PROJECT,
                    DefaultSession.class, "not_found_query"));
        }
        MappingTable mappingTable = this.mappingGlobalWrapper.getMappingTable(f.getTableClass());
        AssistUtils.notNull(mappingTable, Messages.get(LanguageMessageFactory.PROJECT,
                DefaultSession.class, "not_found_mapping", f.getTableClass().getName()));

        Set<MappingField> fields = mappingTable.getMappingFields();
        Iterator<FunctionField> iterator = f.getFuns().iterator();
        while (iterator.hasNext()) {
            FunctionField entry = iterator.next();
            Object key = entry.getField();
            String fieldName = String.valueOf(key);
            boolean isContains = false;
            for (MappingField field : fields) {
                if (field.getMappingFieldName().equals(fieldName)) {
                    isContains = true;
                    break;
                }
            }
            if (!isContains) {
                throw new IllegalArgumentException(Messages.get(LanguageMessageFactory.PROJECT,
                        DefaultSession.class, "include_not_exist"));
            }
        }
        wrapper.setMaster(f.isMaster());
        wrapper.setSlaveName(f.getSlaveName());
        PlatformWrapper platformWrapper = PlatformFactory.getPlatformWrapper(wrapper);
        try {
            List<ModelObject> objects = platformWrapper.select(mappingTable, f);
            if (objects != null) {
                return new AutoResult(this.convert, objects);
            }
            return null;
        } catch (SQLException e) {
            throw new IllegalStateException(Messages.get(LanguageMessageFactory.PROJECT,
                    DefaultSession.class, "query_data_fail"), e);
        }
    }

    @Override
    public AutoResult getAutonomously(SQLAutonomously autonomously) throws Exception {
        String sql = autonomously.getSql();
        Builder builder = autonomously.getBuilder();

        boolean isMaster = autonomously.isMaster();
        String slaveName = autonomously.getSlaveName();

        if (StringTools.isEmpty(sql) && builder != null) {
            List<SQLAutonomously.LinkAutonomously> ds = autonomously.getDataSourceLinks();
            if (ds != null && ds.size() > 0) {
                SQLAutonomously.LinkAutonomously link = ds.get(0);
                sql = link.getSql();
                String dsName = link.getDataSourceName();
                isMaster = autonomously.isMaster(dsName);
                slaveName = autonomously.getSlaveName(dsName);
            }
        }
        if (StringTools.isNotEmpty(sql) || builder != null) {
            wrapper.setMaster(isMaster);
            wrapper.setSlaveName(slaveName);
            PlatformWrapper platformWrapper = PlatformFactory.getPlatformWrapper(wrapper);
            List<ModelObject> objects = null;
            if (builder != null) {
                if (builder instanceof SelectBuilder) {
                    SelectBuilder selectBuilder = (SelectBuilder) builder;
                    Set<Class> tables = selectBuilder.getAllTables();
                    Map<Class, MappingTable> mappingTables = new HashMap<>();
                    for (Class c : tables) {
                        mappingTables.put(c, this.mappingGlobalWrapper.getMappingTable(c));
                    }
                    objects = platformWrapper.select(selectBuilder, mappingTables);
                    ResultMappingFieldUtils.convert(selectBuilder, convert, objects, mappingTables);
                }
            } else {
                objects = platformWrapper.select(sql);
            }

            if (objects != null) {
                Map<String, List<ModelObject>> result = new LinkedHashMap<>(1);
                result.put(MimosaDataSource.DEFAULT_DS_NAME, objects);
                return new AutoResult(convert, result);
            }
        }
        return null;
    }

    @Override
    public AutoResult getAutonomously(TAutonomously autonomously) throws Exception {
        SQLDefinedLoader definedLoader = this.context.getDefinedLoader();
        if (definedLoader != null) {
            DynamicSqlSource sqlSource = definedLoader.getDynamicSqlSource(autonomously.getName());
            if (sqlSource == null) {
                throw new IllegalArgumentException(Messages.get(LanguageMessageFactory.PROJECT,
                        DefaultSession.class, "not_found_file_sql"));
            }
            CarryHandler carryHandler = PlatformFactory.getCarryHandler(wrapper);
            BoundSql boundSql = sqlSource.getBoundSql(autonomously.getParameter());
            String action = boundSql.getAction();

            PorterStructure structure = null;
            if (action.equalsIgnoreCase("select")) {
                structure = new PorterStructure(boundSql.getSql(), boundSql.getDataPlaceholders());
                structure.setChangerClassify(ChangerClassify.SELECT);
            } else if (action.equalsIgnoreCase("update")) {
                structure = new PorterStructure(boundSql.getSql(), boundSql.getDataPlaceholders());
                structure.setChangerClassify(ChangerClassify.UPDATE);
            } else if (action.equalsIgnoreCase("delete")) {
                structure = new PorterStructure(boundSql.getSql(), boundSql.getDataPlaceholders());
                structure.setChangerClassify(ChangerClassify.DELETE);
            } else if (action.equalsIgnoreCase("insert")) {
                structure = new PorterStructure(boundSql.getSql(), boundSql.getDataPlaceholders());
                structure.setChangerClassify(ChangerClassify.ADD_OBJECT);
            }

            if (structure != null) {
                Object object = carryHandler.doHandler(structure);
                // 这里返回的原生的字段，不会逆向转换
                return new AutoResult(convert, object);
            } else {
                throw new IllegalArgumentException(Messages.get(LanguageMessageFactory.PROJECT,
                        DefaultSession.class, "not_support_action"));
            }
        } else {
            throw new IllegalArgumentException(Messages.get(LanguageMessageFactory.PROJECT,
                    DefaultSession.class, "not_found_file_sql"));
        }
    }

    @Override
    public List<DataSourceTableName> getDataSourceNames(Class c) {
        ActionDataSourceWrapper wrapper = this.context.getDefaultDataSourceWrapper(false);
        MimosaDataSource mimosaDataSource = wrapper.getDataSource();

        List<DataSourceTableName> names = new ArrayList<>();

        MappingTable mappingTable = this.mappingGlobalWrapper.getMappingTable(c);
        AssistUtils.notNull(mappingTable, Messages.get(LanguageMessageFactory.PROJECT,
                DefaultSession.class, "not_found_mapping", c.getName()));

        DataSourceTableName dataSourceTableName = new DataSourceTableName(mimosaDataSource.getName(), mappingTable.getDatabaseTableName());
        if (mimosaDataSource.getSlaves() != null) {
            List<String> slaves = new ArrayList<>();
            Set<Map.Entry<String, DataSource>> set = mimosaDataSource.getSlaves().entrySet();
            for (Map.Entry<String, DataSource> entry : set) {
                slaves.add(entry.getKey());
            }
            dataSourceTableName.setSlaves(slaves);
        }

        names.add(dataSourceTableName);
        return names;
    }

    @Override
    public void close() throws IOException {
        try {
            this.wrapper.close();
        } catch (SQLException e) {
            throw new IOException(Messages.get(LanguageMessageFactory.PROJECT,
                    DefaultSession.class, "close_db_fail"), e);
        }
    }
}
