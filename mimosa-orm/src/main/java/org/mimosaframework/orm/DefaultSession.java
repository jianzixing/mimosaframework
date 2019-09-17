package org.mimosaframework.orm;

import org.mimosaframework.core.json.ModelObject;
import org.mimosaframework.core.utils.StringTools;
import org.mimosaframework.orm.criteria.*;
import org.mimosaframework.orm.exception.StrategyException;
import org.mimosaframework.orm.mapping.MappingField;
import org.mimosaframework.orm.mapping.MappingGlobalWrapper;
import org.mimosaframework.orm.mapping.MappingTable;
import org.mimosaframework.orm.platform.*;
import org.mimosaframework.orm.scripting.BoundSql;
import org.mimosaframework.orm.scripting.DynamicSqlSource;
import org.mimosaframework.orm.scripting.SQLDefinedLoader;
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
    private ContextValues context;
    private ActionDataSourceWrapper wrapper;
    private MappingGlobalWrapper mappingDatabaseWrapper;
    private ModelObjectConvertKey convert;

    public DefaultSession(ContextValues context) {
        this.context = context;
        this.wrapper = this.context.getDefaultDataSource().newDataSourceWrapper();
        this.mappingDatabaseWrapper = this.context.getMappingGlobalWrapper();
        this.convert = this.context.getModelObjectConvertKey();
    }


    @Override
    public ModelObject save(ModelObject objSource) {
        if (objSource == null || objSource.size() == 0) {
            throw new IllegalArgumentException("保存的对象不能为空");
        }
        ModelObject obj = Clone.cloneModelObject(objSource);
        Class c = obj.getObjectClass();
        if (c == null) {
            throw new IllegalArgumentException("请先使用setObjectClass设置对象映射类");
        }
        SessionUtils.clearModelObject(this.mappingDatabaseWrapper, c, obj);
        MappingTable mappingTable = this.mappingDatabaseWrapper.getSingleDatabaseTable(wrapper, c);

        // 开始类型矫正
        TypeCorrectUtils.correct(obj, mappingTable);

        if (mappingTable != null) {
            try {
                StrategyFactory.applyStrategy(this.context, mappingTable, obj, this, false);
            } catch (StrategyException e) {
                throw new IllegalArgumentException("使用ID生成策略出错", e.getCause());
            }
            // 转换成数据库的字段
            obj = convert.convert(obj);

            PlatformWrapper platformWrapper = PlatformFactory.getPlatformWrapper(wrapper);
            // 添加后如果有自增列，则返回如果没有就不返回
            Long id = null;
            try {
                id = platformWrapper.insert(mappingTable, obj);
            } catch (SQLException e) {
                throw new IllegalStateException("添加数据失败", e);
            }
            SessionUtils.applyAutoIncrementValue(mappingTable, id, objSource);

            return objSource;
        }

        return objSource;
    }

    @Override
    public ModelObject saveAndUpdate(ModelObject objSource) {
        if (objSource == null || objSource.size() == 0) {
            throw new IllegalArgumentException("保存的对象不能为空");
        }
        if (objSource.getObjectClass() == null) {
            throw new IllegalArgumentException("请先使用setObjectClass设置对象映射类");
        }
        ModelObject obj = Clone.cloneModelObject(objSource);
        Class c = obj.getObjectClass();
        MappingTable mappingTable = this.mappingDatabaseWrapper.getMappingTable(c);

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
            throw new IllegalArgumentException("保存的对象不能为空");
        }

        List<ModelObject> objects = new ArrayList<ModelObject>(objectSources);
        List<ModelObject> saves = new ArrayList<ModelObject>();
        Class tableClass = null;
        MappingTable mappingTable = null;
        SessionUtils.checkReference(objects);
        for (ModelObject object : objects) {
            if (object == null || object.size() == 0) {
                throw new IllegalArgumentException("批量保存列表中存在空对象");
            }
            Class c = object.getObjectClass();
            SessionUtils.clearModelObject(this.mappingDatabaseWrapper, c, object);

            if (tableClass == null) tableClass = c;
            if (tableClass != null && tableClass != c) {
                throw new IllegalArgumentException("批量保存时所有对象表必须一致,[" + tableClass.getSimpleName() + "]和[" + c.getSimpleName() + "]不一致");
            }
            tableClass = c;

            if (mappingTable == null) {
                mappingTable = this.mappingDatabaseWrapper.getSingleDatabaseTable(wrapper, c);
            }
            try {
                StrategyFactory.applyStrategy(this.context, mappingTable, object, this, false);
            } catch (StrategyException e) {
                throw new IllegalArgumentException("使用ID生成策略出错", e.getCause());
            }

            // 开始类型矫正
            TypeCorrectUtils.correct(object, mappingTable);
            object = convert.convert(object);
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
            throw new IllegalStateException("批量添加数据失败", e);
        }
        SessionUtils.applyAutoIncrementValue(mappingTable, ids, objectSources);
    }

    @Override
    public void update(ModelObject obj) {
        if (obj == null || obj.size() == 0) {
            throw new IllegalArgumentException("更新对象不能为空");
        }
        obj = Clone.cloneModelObject(obj);
        Class c = obj.getObjectClass();
        MappingTable mappingTable = this.mappingDatabaseWrapper.getSingleDatabaseTable(wrapper, c);

        updateSkipReset.skip(obj, mappingTable);
        SessionUtils.clearModelObject(this.mappingDatabaseWrapper, obj.getObjectClass(), obj);

        List<MappingField> pks = mappingTable.getMappingPrimaryKeyFields();
        if (obj.size() - pks.size() <= 0) {
            //如果只有主键值没有需要更新的值就不执行更新操作
            return;
        }
        if (!SessionUtils.checkPrimaryKey(pks, obj)) {
            throw new IllegalArgumentException("修改一个对象必须设置主键的值");
        }

        // 开始类型矫正
        TypeCorrectUtils.correct(obj, mappingTable);

        obj = convert.convert(obj);
        PlatformWrapper platformWrapper = PlatformFactory.getPlatformWrapper(wrapper);
        try {
            platformWrapper.update(mappingTable, obj);
        } catch (SQLException e) {
            throw new IllegalStateException("更新数据失败", e);
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
            throw new IllegalArgumentException("使用条件更新数据,过滤条件和要设置的值都不能为空");
        }
        Class c = u.getTableClass();
        MappingTable mappingTable = this.mappingDatabaseWrapper.getSingleDatabaseTable(wrapper, c);
        PlatformWrapper platformWrapper = PlatformFactory.getPlatformWrapper(wrapper);
        int count = 0;
        try {
            count = platformWrapper.update(mappingTable, u);
        } catch (SQLException e) {
            throw new IllegalStateException("更新数据失败", e);
        }

        return count;
    }

    @Override
    public void delete(ModelObject objSource) {
        ModelObject obj = Clone.cloneModelObject(objSource);
        Class c = obj.getObjectClass();
        MappingTable mappingTable = this.mappingDatabaseWrapper.getSingleDatabaseTable(wrapper, c);
        if (!SessionUtils.checkPrimaryKey(mappingTable.getMappingPrimaryKeyFields(), obj)) {
            throw new IllegalArgumentException("删除一个对象必须设置主键的值");
        }

        PlatformWrapper platformWrapper = PlatformFactory.getPlatformWrapper(wrapper);
        obj = convert.convert(obj);

        try {
            platformWrapper.delete(mappingTable, obj);
        } catch (SQLException e) {
            throw new IllegalStateException("删除数据失败", e);
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
            throw new IllegalArgumentException("使用条件删除数据,过滤条件不能为空");
        }

        Class c = d.getTableClass();
        MappingTable mappingTable = this.mappingDatabaseWrapper.getSingleDatabaseTable(wrapper, c);
        PlatformWrapper platformWrapper = PlatformFactory.getPlatformWrapper(wrapper);
        try {
            return platformWrapper.delete(mappingTable, d);
        } catch (SQLException e) {
            throw new IllegalStateException("删除数据失败", e);
        }
    }

    @Override
    public void delete(Class c, Serializable id) {
        MappingTable mappingTable = this.mappingDatabaseWrapper.getSingleDatabaseTable(wrapper, c);
        List<MappingField> pks = mappingTable.getMappingPrimaryKeyFields();

        if (pks.size() != 1) {
            throw new IllegalArgumentException("当前方法只允许删除主键存在且唯一的对象," +
                    "[" + c.getSimpleName() + "]的主键数量为" + pks.size());
        }

        Delete delete = new DefaultDelete(c, this);
        delete.addFilter().eq(pks.get(0).getMappingFieldName(), id);
        this.delete(delete);
    }

    @Override
    public ModelObject get(Class c, Serializable id) {
        MappingTable mappingTable = this.mappingDatabaseWrapper.getSingleDatabaseTable(wrapper, c);
        List<MappingField> pks = mappingTable.getMappingPrimaryKeyFields();
        if (pks.size() != 1) {
            throw new IllegalArgumentException("当前方法只允许查询主键存在且唯一的对象," +
                    "[" + c.getSimpleName() + "]的主键数量为" + pks.size());
        }

        Query query = new DefaultQuery(c);
        query.eq(pks.get(0).getMappingFieldName(), id);
        List results = this.list(query);

        if (results != null) {
            if (results.size() > 1) {
                throw new IllegalArgumentException("当前方法只允许查询主键唯一的值，查询结果数量" + results.size() + "不匹配");
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

        SessionUtils.processQueryExcludes(this.mappingDatabaseWrapper, dq);
        Map<Object, MappingTable> tables = SessionUtils.getUsedMappingTable(this.mappingDatabaseWrapper, dq);

        PlatformWrapper platformWrapper = PlatformFactory.getPlatformWrapper(wrapper);
        List<ModelObject> objects = null;
        try {
            objects = platformWrapper.select(tables, dq, this.context.getConvert());
        } catch (SQLException e) {
            throw new IllegalStateException("获取数据失败", e);
        }

        return objects;
    }

    @Override
    public long count(Query query) {
        DefaultQuery dq = (DefaultQuery) query;
        dq.checkQuery();
        wrapper.setMaster(dq.isMaster());
        wrapper.setSlaveName(dq.getSlaveName());

        Map<Object, MappingTable> tables = SessionUtils.getUsedMappingTable(this.mappingDatabaseWrapper, dq);
        PlatformWrapper platformWrapper = PlatformFactory.getPlatformWrapper(wrapper);
        long count = 0;
        try {
            count = platformWrapper.count(tables, dq);
        } catch (SQLException e) {
            throw new IllegalStateException("获取数据条数失败", e);
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
        MappingTable mappingTable = this.mappingDatabaseWrapper.getSingleDatabaseTable(wrapper, c);
        MimosaDataSource ds = this.wrapper.getDataSource();
        return new SingleZipperTable<ModelObject>(context, ds, mappingTable.getDatabaseTableName());
    }

    @Override
    public ModelObject calculate(Function function) {
        DefaultFunction f = (DefaultFunction) function;
        if (f.getTableClass() == null) {
            throw new IllegalArgumentException("没有找到查询映射类");
        }

        if (f.getFuns() == null || f.getFuns().size() == 0) {
            throw new IllegalArgumentException("没有找到查询条件");
        }
        MappingTable mappingTable = this.mappingDatabaseWrapper.getSingleDatabaseTable(wrapper, f.getTableClass());
        if (mappingTable == null) {
            throw new IllegalArgumentException("没有找到该类的映射表");
        }
        Set<MappingField> fields = mappingTable.getMappingFields();
        Iterator<DefaultFunction.FunctionField> iterator = f.getFuns().iterator();
        while (iterator.hasNext()) {
            DefaultFunction.FunctionField entry = iterator.next();
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
                throw new IllegalArgumentException("查询字段中包含不存在的字段");
            }
        }
        wrapper.setMaster(f.isMaster());
        wrapper.setSlaveName(f.getSlaveName());
        PlatformWrapper platformWrapper = PlatformFactory.getPlatformWrapper(wrapper);
        try {
            return platformWrapper.select(mappingTable, f);
        } catch (SQLException e) {
            throw new IllegalStateException("查询数据失败", e);
        }
    }

    @Override
    public AutoResult getAutonomously(SQLAutonomously autonomously) throws Exception {
        String sql = autonomously.getSql();
        boolean isMaster = autonomously.isMaster();
        String slaveName = autonomously.getSlaveName();

        if (StringTools.isEmpty(sql)) {
            List<SQLAutonomously.LinkAutonomously> ds = autonomously.getDataSourceLinks();
            if (ds != null && ds.size() > 0) {
                SQLAutonomously.LinkAutonomously link = ds.get(0);
                sql = link.getSql();
                String dsName = link.getDataSourceName();
                isMaster = autonomously.isMaster(dsName);
                slaveName = autonomously.getSlaveName(dsName);
            }
        }
        if (StringTools.isNotEmpty(sql)) {
            wrapper.setMaster(isMaster);
            wrapper.setSlaveName(slaveName);
            PlatformWrapper platformWrapper = PlatformFactory.getPlatformWrapper(wrapper);
            List<ModelObject> objects = platformWrapper.select(sql);

            Map<String, List<ModelObject>> result = new LinkedHashMap<>(1);
            result.put(MimosaDataSource.DEFAULT_DS_NAME, objects);
            return new AutoResult(result);
        }
        return null;
    }

    @Override
    public AutoResult getAutonomously(TAutonomously autonomously) throws Exception {
        SQLDefinedLoader definedLoader = this.context.getDefinedLoader();
        if (definedLoader != null) {
            DynamicSqlSource sqlSource = definedLoader.getDynamicSqlSource(autonomously.getName());
            if (sqlSource == null) {
                throw new IllegalArgumentException("没有发现配置文件SQL");
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
                PorterStructure[] structures = new PorterStructure[]{structure};
                Object object = carryHandler.doHandler(structures);
                if (object instanceof List) {
                    List<ModelObject> list = (List<ModelObject>) object;
                    for (ModelObject i : list) {
                        convert.reconvert(i);
                    }
                }
                return new AutoResult(object);
            } else {
                throw new IllegalArgumentException("不支持的动作标签,当前仅支持select,update,delete,insert");
            }
        } else {
            throw new IllegalArgumentException("没有发现配置文件SQL");
        }
    }

    @Override
    public List<DataSourceTableName> getDataSourceNames(Class c) {
        ActionDataSourceWrapper wrapper = this.context.getDefaultDataSource();
        MimosaDataSource mimosaDataSource = wrapper.getDataSource();

        List<DataSourceTableName> names = new ArrayList<>();

        MappingTable mappingTable = this.mappingDatabaseWrapper.getSingleDatabaseTable(wrapper, c);
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
            throw new IOException("关闭数据库连接出错", e);
        }
    }
}