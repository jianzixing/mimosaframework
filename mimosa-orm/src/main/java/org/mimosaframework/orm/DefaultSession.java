package org.mimosaframework.orm;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.mimosaframework.core.json.ModelObject;
import org.mimosaframework.core.utils.AssistUtils;
import org.mimosaframework.core.utils.StringTools;
import org.mimosaframework.orm.criteria.*;
import org.mimosaframework.orm.exception.StrategyException;
import org.mimosaframework.orm.i18n.I18n;
import org.mimosaframework.orm.mapping.MappingField;
import org.mimosaframework.orm.mapping.MappingGlobalWrapper;
import org.mimosaframework.orm.mapping.MappingTable;
import org.mimosaframework.orm.platform.SessionContext;
import org.mimosaframework.orm.platform.JDBCTraversing;
import org.mimosaframework.orm.platform.PlatformExecutor;
import org.mimosaframework.orm.platform.PlatformExecutorFactory;
import org.mimosaframework.orm.scripting.SQLDefinedLoader;
import org.mimosaframework.orm.sql.UnifyBuilder;
import org.mimosaframework.orm.strategy.StrategyFactory;
import org.mimosaframework.orm.transaction.Transaction;
import org.mimosaframework.orm.utils.AutonomouslyUtils;
import org.mimosaframework.orm.utils.Clone;
import org.mimosaframework.orm.utils.SessionUtils;
import org.mimosaframework.orm.utils.TypeCorrectUtils;

import javax.sql.DataSource;
import java.io.IOException;
import java.io.Serializable;
import java.math.BigDecimal;
import java.sql.SQLException;
import java.util.*;

public class DefaultSession implements Session {
    private static final Log logger = LogFactory.getLog(DefaultSession.class);
    private PlatformExecutor executor;
    private UpdateSkipReset updateSkipReset = new UpdateSkiptResetEmpty();
    private Configuration context;
    /**
     * 每个Session有且只有一个transaction，每一个transaction有且只有一个
     * Connection，Connection的获取是从transaction中获取的，transaction
     * 的实现有多种，每一种实现了不同的Connection的管理机制
     */
    private SessionContext sessionContext;
    private MappingGlobalWrapper mappingGlobalWrapper;
    private ModelObjectConvertKey convert;

    public DefaultSession(Configuration context) throws SQLException {
        this(context, MimosaDataSource.DEFAULT_DS_NAME);
    }

    public DefaultSession(Configuration context, String dsName) throws SQLException {
        this.context = context;
        this.sessionContext = this.context.newSessionContext(dsName, true);
        this.mappingGlobalWrapper = this.context.getMappingGlobalWrapper();
        this.convert = this.context.getModelObjectConvertKey();
        convert.setMappingGlobalWrapper(mappingGlobalWrapper);
        executor = PlatformExecutorFactory.getExecutor(mappingGlobalWrapper, sessionContext);
    }

    @Override
    public ModelObject save(ModelObject objSource) {
        if (objSource == null || objSource.size() == 0) {
            throw new IllegalArgumentException(I18n.print("save_empty"));
        }
        ModelObject obj = Clone.cloneModelObject(objSource);
        Class c = obj.getObjectClass();
        if (c == null) {
            throw new IllegalArgumentException(I18n.print("miss_table_class"));
        }
        SessionUtils.clearModelObject(this.mappingGlobalWrapper, c, obj);
        SessionUtils.clearPkZeroModelObject(this.mappingGlobalWrapper, c, obj);
        MappingTable mappingTable = this.mappingGlobalWrapper.getMappingTable(c);
        AssistUtils.isNull(mappingTable, I18n.print("not_found_mapping", c.getName()));

        // 开始类型矫正
        TypeCorrectUtils.correct(obj, mappingTable);

        if (mappingTable != null) {
            try {
                StrategyFactory.applyStrategy(this.context, mappingTable, obj, this);
            } catch (StrategyException e) {
                throw new IllegalArgumentException(I18n.print("id_strategy_error"), e.getCause());
            }

            // 添加后如果有自增列，则返回如果没有就不返回
            Long id = null;
            try {
                List<Long> ids = executor.inserts(mappingTable, Arrays.asList(new ModelObject[]{obj}));
                if (ids != null && ids.size() > 0) id = ids.get(0);
            } catch (SQLException e) {
                throw new IllegalStateException(I18n.print("add_data_error"), e);
            }
            SessionUtils.applyAutoIncrementValue(mappingTable, id, objSource);

            return objSource;
        }

        return objSource;
    }

    @Override
    public ModelObject saveAndUpdate(ModelObject objSource) {
        if (objSource == null || objSource.size() == 0) {
            throw new IllegalArgumentException(I18n.print("save_empty"));
        }
        if (objSource.getObjectClass() == null) {
            throw new IllegalArgumentException(I18n.print("miss_table_class"));
        }
        ModelObject obj = Clone.cloneModelObject(objSource);
        Class c = obj.getObjectClass();
        MappingTable mappingTable = this.mappingGlobalWrapper.getMappingTable(c);
        AssistUtils.isNull(mappingTable, I18n.print("not_found_mapping", c.getName()));

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
            throw new IllegalArgumentException(I18n.print("save_empty"));
        }

        List<ModelObject> objects = new ArrayList<ModelObject>(objectSources);
        List<ModelObject> saves = new ArrayList<ModelObject>();
        Class tableClass = null;
        MappingTable mappingTable = null;
        SessionUtils.checkReference(objects);
        for (ModelObject object : objects) {
            if (object == null || object.size() == 0) {
                throw new IllegalArgumentException(I18n.print("batch_save_empty"));
            }
            Class c = object.getObjectClass();
            SessionUtils.clearModelObject(this.mappingGlobalWrapper, c, object);

            if (tableClass == null) tableClass = c;
            if (tableClass != null && tableClass != c) {
                throw new IllegalArgumentException(I18n.print("batch_save_table_diff",
                        tableClass.getSimpleName(), c.getSimpleName()));
            }
            tableClass = c;

            if (mappingTable == null) {
                mappingTable = this.mappingGlobalWrapper.getMappingTable(c);
            }
            AssistUtils.isNull(mappingTable, I18n.print("not_found_mapping", c.getName()));

            try {
                StrategyFactory.applyStrategy(this.context, mappingTable, object, this);
            } catch (StrategyException e) {
                throw new IllegalArgumentException(I18n.print("id_strategy_error"), e.getCause());
            }

            // 开始类型矫正
            TypeCorrectUtils.correct(object, mappingTable);
            if (object.size() > 0) {
                saves.add(object);
            }
        }

        // 添加后如果有自增列，则返回如果没有就不返回
        List<Long> ids = null;
        try {
            ids = executor.inserts(mappingTable, saves);
        } catch (SQLException e) {
            throw new IllegalStateException(I18n.print("batch_save_data_error"), e);
        }
        SessionUtils.applyAutoIncrementValue(mappingTable, ids, objectSources);
    }

    @Override
    public void update(ModelObject obj) {
        if (obj == null || obj.size() == 0) {
            throw new IllegalArgumentException(I18n.print("update_empty"));
        }
        obj = Clone.cloneModelObject(obj);
        Class c = obj.getObjectClass();
        if (c == null) {
            throw new IllegalArgumentException(I18n.print("miss_table_class"));
        }
        MappingTable mappingTable = this.mappingGlobalWrapper.getMappingTable(c);
        AssistUtils.isNull(mappingTable, I18n.print("not_found_mapping", c.getName()));

        updateSkipReset.skip(obj, mappingTable);
        SessionUtils.clearModelObject(this.mappingGlobalWrapper, obj.getObjectClass(), obj);

        List<MappingField> pks = mappingTable.getMappingPrimaryKeyFields();
        if (obj.size() - pks.size() <= 0) {
            //如果只有主键值没有需要更新的值就不执行更新操作
            return;
        }
        if (!SessionUtils.checkPrimaryKey(pks, obj)) {
            throw new IllegalArgumentException(I18n.print("update_set_id"));
        }

        // 开始类型矫正
        TypeCorrectUtils.correct(obj, mappingTable);
        try {
            Update update = SessionUtils.buildUpdateByModel(mappingTable, obj);
            executor.update(mappingTable, (DefaultUpdate) update);
        } catch (SQLException e) {
            throw new IllegalStateException(I18n.print("update_fail"), e);
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
            throw new IllegalArgumentException(I18n.print("update_filter_empty"));
        }
        Class c = u.getTableClass();
        MappingTable mappingTable = this.mappingGlobalWrapper.getMappingTable(c);
        AssistUtils.isNull(mappingTable, I18n.print("not_found_mapping", c.getName()));

        int count = 0;
        try {
            count = executor.update(mappingTable, u);
        } catch (SQLException e) {
            throw new IllegalStateException(I18n.print("update_fail"), e);
        }

        return count;
    }

    @Override
    public void delete(ModelObject objSource) {
        ModelObject obj = Clone.cloneModelObject(objSource);
        Class c = obj.getObjectClass();
        MappingTable mappingTable = this.mappingGlobalWrapper.getMappingTable(c);
        AssistUtils.isNull(mappingTable, I18n.print("not_found_mapping", c.getName()));

        if (!SessionUtils.checkPrimaryKey(mappingTable.getMappingPrimaryKeyFields(), obj)) {
            throw new IllegalArgumentException(I18n.print("delete_id"));
        }

        try {
            Delete delete = SessionUtils.buildDeleteByModel(mappingTable, obj);
            executor.delete(mappingTable, (DefaultDelete) delete);
        } catch (SQLException e) {
            throw new IllegalStateException(I18n.print("delete_fail"), e);
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
            throw new IllegalArgumentException(I18n.print("delete_filter_empty"));
        }

        Class c = d.getTableClass();
        MappingTable mappingTable = this.mappingGlobalWrapper.getMappingTable(c);
        AssistUtils.isNull(mappingTable, I18n.print("not_found_mapping", c.getName()));

        try {
            return executor.delete(mappingTable, d);
        } catch (SQLException e) {
            throw new IllegalStateException(I18n.print("delete_fail"), e);
        }
    }

    @Override
    public void delete(Class c, Serializable id) {
        MappingTable mappingTable = this.mappingGlobalWrapper.getMappingTable(c);
        AssistUtils.isNull(mappingTable, I18n.print("not_found_mapping", c.getName()));

        List<MappingField> pks = mappingTable.getMappingPrimaryKeyFields();

        if (pks.size() != 1) {
            throw new IllegalArgumentException(I18n.print("delete_only_pk", c.getSimpleName(), "" + pks.size()));
        }

        Delete delete = new DefaultDelete(c);
        delete.eq(pks.get(0).getMappingFieldName(), id);
        this.delete(delete);
    }

    @Override
    public ModelObject get(Class c, Serializable id) {
        MappingTable mappingTable = this.mappingGlobalWrapper.getMappingTable(c);
        AssistUtils.isNull(mappingTable, I18n.print("not_found_mapping", c.getName()));

        List<MappingField> pks = mappingTable.getMappingPrimaryKeyFields();
        if (pks.size() != 1) {
            throw new IllegalArgumentException(I18n.print("delete_only_pk", c.getSimpleName(), "" + pks.size()));
        }

        Query query = new DefaultQuery(c);
        query.eq(pks.get(0).getMappingFieldName(), id);
        List results = this.list(query);

        if (results != null) {
            if (results.size() > 1) {
                throw new IllegalArgumentException(I18n.print("query_only_pk", "" + results.size()));
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
        sessionContext.setMaster(dq.isMaster());
        sessionContext.setSlaveName(dq.getSlaveName());
        if (dq.isForUpdate()) {
            Set<Join> joins = dq.getJoins();
            if (joins != null && joins.size() > 0) {
                throw new IllegalArgumentException(I18n.print("for_update_join_fail"));
            }
            Set<OrderBy> orders = dq.getOrderBy();
            if (orders != null && orders.size() > 0) {
                throw new IllegalArgumentException(I18n.print("for_update_order_fail"));
            }
            Limit limit = dq.getLimit();
            if (limit != null) {
                throw new IllegalArgumentException(I18n.print("for_update_limit_fail"));
            }
        }

        List<ModelObject> objects = null;
        try {
            objects = executor.select(dq, convert);
        } catch (SQLException e) {
            throw new IllegalStateException(I18n.print("get_data_fail"), e);
        }

        return objects;
    }

    @Override
    public long count(Query query) {
        DefaultQuery dq = (DefaultQuery) query;
        dq.checkQuery();
        sessionContext.setMaster(dq.isMaster());
        sessionContext.setSlaveName(dq.getSlaveName());

        long count = 0;
        try {
            count = executor.count(dq);
        } catch (SQLException e) {
            throw new IllegalStateException(I18n.print("get_data_count_fail"), e);
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
        AssistUtils.isNull(mappingTable, I18n.print("not_found_mapping", c.getName()));

        MimosaDataSource ds = this.sessionContext.getDataSource();
        return new SingleZipperTable<ModelObject>(context, c, ds, mappingTable.getMappingTableName());
    }

    @Override
    public AutoResult calculate(Function function) {
        DefaultFunction f = (DefaultFunction) function;
        if (f.getTableClass() == null) {
            throw new IllegalArgumentException(I18n.print("not_fount_class"));
        }

        if (f.getFuns() == null || f.getFuns().size() == 0) {
            throw new IllegalArgumentException(I18n.print("not_found_query"));
        }
        MappingTable mappingTable = this.mappingGlobalWrapper.getMappingTable(f.getTableClass());
        AssistUtils.isNull(mappingTable, I18n.print("not_found_mapping", f.getTableClass().getName()));

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
                throw new IllegalArgumentException(I18n.print("include_not_exist"));
            }
        }
        sessionContext.setMaster(f.isMaster());
        sessionContext.setSlaveName(f.getSlaveName());
        try {
            List<ModelObject> objects = executor.function(f);
            if (objects != null) {
                Set<FunctionField> funs = f.getFuns();
                if (funs != null) {
                    for (FunctionField field : funs) {
                        if (field.getScale() != 0) {
                            for (ModelObject object : objects) {
                                try {
                                    BigDecimal bigDecimal = object.getBigDecimal(String.valueOf(field.getAlias()));
                                    bigDecimal.setScale(field.getScale(), BigDecimal.ROUND_HALF_UP);
                                    object.put(String.valueOf(field.getAlias()), bigDecimal.doubleValue());
                                } catch (Exception e) {
                                    logger.error("calc scale error : " + e.getMessage());
                                }
                            }
                        }
                    }
                }
                return new AutoResult(this.convert, objects);
            }
            return null;
        } catch (SQLException e) {
            throw new IllegalStateException(I18n.print("query_data_fail"), e);
        }
    }

    @Override
    public AutoResult getAutonomously(SQLAutonomously autonomously) throws Exception {
        String sql = autonomously.getSql();
        UnifyBuilder builder = autonomously.getBuilder();

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
            sessionContext.setMaster(isMaster);
            sessionContext.setSlaveName(slaveName);
            if (builder != null) {
                Object r = executor.dialect(builder.compile());
                return new AutoResult(r);
            } else {
                Object object = executor.original(new JDBCTraversing(sql));
                Map<String, Object> result = new LinkedHashMap<>(1);
                result.put(MimosaDataSource.DEFAULT_DS_NAME, object);
                return new AutoResult(convert, result);
            }
        }
        return null;
    }

    @Override
    public AutoResult getAutonomously(TAutonomously autonomously) throws Exception {
        SQLDefinedLoader definedLoader = this.context.getDefinedLoader();
        if (definedLoader != null) {
            JDBCTraversing structure = AutonomouslyUtils.parseStructure(definedLoader,
                    autonomously.getName(), autonomously.getParameter());
            if (structure != null) {
                Object object = executor.original(structure);
                // 这里返回的原生的字段，不会逆向转换
                return new AutoResult(convert, object);
            } else {
                throw new IllegalArgumentException(I18n.print("not_support_action"));
            }
        } else {
            throw new IllegalArgumentException(I18n.print("not_found_file_sql"));
        }
    }

    @Override
    public List<DataSourceTableName> getDataSourceNames(Class c) {
        MimosaDataSource mimosaDataSource = this.sessionContext.getDataSource();
        List<DataSourceTableName> names = new ArrayList<>();

        MappingTable mappingTable = this.mappingGlobalWrapper.getMappingTable(c);
        AssistUtils.isNull(mappingTable, I18n.print("not_found_mapping", c.getName()));

        DataSourceTableName dataSourceTableName = new DataSourceTableName(mimosaDataSource.getName(), mappingTable.getMappingTableName());
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
            Transaction transaction = this.sessionContext.getTransaction();
            if (transaction != null) {
                transaction.close();
            }
        } catch (SQLException e) {
            throw new IOException(I18n.print("close_db_fail"), e);
        }
    }
}
