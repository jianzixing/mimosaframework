package org.mimosaframework.orm;

import org.mimosaframework.core.FieldFunction;
import org.mimosaframework.core.json.ModelObject;
import org.mimosaframework.core.utils.ClassUtils;
import org.mimosaframework.orm.annotation.JoinName;
import org.mimosaframework.orm.criteria.*;
import org.mimosaframework.orm.i18n.I18n;
import org.mimosaframework.orm.transaction.TransactionManager;
import org.mimosaframework.orm.utils.Model2BeanFactory;
import org.mimosaframework.orm.utils.ModelObjectToBean;

import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.*;

public class MimosaBeanSessionTemplate implements BeanSessionTemplate {
    private MimosaSessionTemplate modelSession = new MimosaSessionTemplate();
    private Model2BeanFactory model2BeanFactory = new ModelObjectToBean();
    private SessionFactory sessionFactory;

    public MimosaBeanSessionTemplate() {
    }

    public MimosaBeanSessionTemplate(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
        modelSession.setSessionFactory(sessionFactory);
    }

    public MimosaBeanSessionTemplate(SessionFactory sessionFactory, Model2BeanFactory model2BeanFactory) {
        this.model2BeanFactory = model2BeanFactory;
        this.sessionFactory = sessionFactory;
        modelSession.setSessionFactory(sessionFactory);
    }

    public SessionFactory getSessionFactory() {
        return sessionFactory;
    }

    public void setSessionFactory(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
        modelSession.setSessionFactory(sessionFactory);
    }

    @Override
    public <T> T save(T obj) {
        Object json = ModelObject.toJSON(obj);
        if (json instanceof ModelObject) {
            ModelObject model = (ModelObject) json;
            model.setObjectClass(obj.getClass());
            model.clearNull();
            modelSession.save(model);
            model2BeanFactory.toJavaObject(model, obj, true);
            return obj;
        } else {
            throw new IllegalArgumentException(I18n.print("bean_save_not_json"));
        }
    }

    @Override
    public <T> T saveOrUpdate(T obj, Object... fields) {
        if (obj == null) {
            throw new IllegalArgumentException(I18n.print("bean_save_not_json"));
        }
        Class<?> tableClass = obj.getClass();
        Object mappingBean = obj;
        Object json = ModelObject.toJSON(obj);
        if (!(json instanceof ModelObject)) {
            throw new IllegalArgumentException(I18n.print("bean_save_not_json"));
        }
        ModelObject model = (ModelObject) json;
        model.setObjectClass(tableClass);
        model.clearNull();
        modelSession.saveOrUpdate(model, fields);
        model2BeanFactory.toJavaObject(model, mappingBean, true);
        return obj;
    }

    @Override
    public <T> T saveOrUpdateSelective(T obj, FieldFunction<T>... fields) {
        List<String> list = new ArrayList<>();
        if (fields != null && fields.length > 0) {
            for (Object object : fields) {
                list.add(ClassUtils.value(object));
            }
        }
        return saveOrUpdate(obj, list);
    }

    @Override
    public <T> void save(List<T> objects) {
        List<ModelObject> saves = null;
        if (objects != null && objects.size() > 0) {
            for (T object : objects) {
                if (saves == null) saves = new ArrayList<>();
                Object json = ModelObject.toJSON(object);
                if (json instanceof ModelObject) {
                    ModelObject model = (ModelObject) json;
                    model.setObjectClass(object.getClass());
                    model.clearNull();
                    saves.add(model);
                } else {
                    throw new IllegalArgumentException(I18n.print("bean_save_not_json"));
                }
            }
            modelSession.save(saves);
        }
    }

    @Override
    public int update(Update update) {
        if (update == null || !(update instanceof Update)) {
            throw new IllegalArgumentException(I18n.print("only_update_object"));
        }
        return modelSession.update(update);
    }

    @Override
    public <T> int update(T update, Object... fields) {
        Object json = ModelObject.toJSON(update);
        if (json instanceof ModelObject) {
            ModelObject model = (ModelObject) json;
            model.setObjectClass(update.getClass());
            model.clearNull();
            return modelSession.update(model, fields);
        } else {
            throw new IllegalArgumentException(I18n.print("bean_save_not_json"));
        }
    }

    @Override
    public <T> int updateSelective(T obj, FieldFunction<T>... fields) {
        List<String> list = new ArrayList<String>();
        if (fields != null && fields.length > 0) {
            for (Object object : fields) {
                list.add(ClassUtils.value(object));
            }
        }
        return this.update(obj, list.toArray());
    }

    @Override
    public <T> int update(List<T> objects, Object... fields) {
        return doUpdate(objects, fields);
    }

    @Override
    public <T> int updateSelective(List<T> objects, FieldFunction<T>... fields) {
        List<String> list = new ArrayList<>();
        if (fields != null && fields.length > 0) {
            for (Object object : fields) {
                list.add(ClassUtils.value(object));
            }
        }
        return doUpdate(objects, list.toArray());
    }

    @Override
    public <T> int modify(T obj) {
        Object json = ModelObject.toJSON(obj);
        if (json instanceof ModelObject) {
            ModelObject model = (ModelObject) json;
            model.setObjectClass(obj.getClass());
            model.clearNull();
            return modelSession.modify(model);
        } else {
            throw new IllegalArgumentException(I18n.print("bean_save_not_json"));
        }
    }

    private <T> int doUpdate(List<T> objects, Object... fields) {
        List<ModelObject> updates = null;
        if (objects != null && objects.size() > 0) {
            for (T update : objects) {
                if (updates == null) updates = new ArrayList<>();
                Object json = ModelObject.toJSON(update);
                if (json instanceof ModelObject) {
                    ModelObject model = (ModelObject) json;
                    model.setObjectClass(update.getClass());
                    model.clearNull();
                    updates.add(model);
                } else {
                    throw new IllegalArgumentException(I18n.print("bean_save_not_json"));
                }
            }
            return modelSession.update(updates, fields);
        }
        return 0;
    }

    @Override
    public <T> int delete(T delete) {
        if (delete instanceof Query || delete instanceof Update) {
            throw new IllegalArgumentException(I18n.print("only_delete_object"));
        } else if (delete instanceof Delete) {
            return modelSession.delete((Delete) delete);
        } else {
            Object json = ModelObject.toJSON(delete);
            if (json instanceof ModelObject) {
                ModelObject model = (ModelObject) json;
                model.setObjectClass(delete.getClass());
                return modelSession.delete(model);
            } else {
                throw new IllegalArgumentException(I18n.print("bean_save_not_json"));
            }
        }
    }

    @Override
    public <T> int delete(List<T> objects) {
        List<ModelObject> deletes = null;
        if (objects != null && objects.size() > 0) {
            for (T object : objects) {
                if (deletes == null) deletes = new ArrayList<>();
                Object json = ModelObject.toJSON(object);
                if (json instanceof ModelObject) {
                    ModelObject model = (ModelObject) json;
                    model.setObjectClass(object.getClass());
                    deletes.add(model);
                } else {
                    throw new IllegalArgumentException(I18n.print("bean_save_not_json"));
                }
            }
            return modelSession.delete(deletes);
        }
        return 0;
    }

    @Override
    public <T> int delete(Class<T> c, Object id) {
        return modelSession.delete(c, ClassUtils.value(id));
    }

    @Override
    public <T> T get(Class<T> c, Object id) {
        ModelObject object = modelSession.get(c, ClassUtils.value(id));
        return model2BeanFactory.toJavaObject(object, c);
    }

    @Override
    public <T> T get(Query<T> query) {
        ModelObject result = modelSession.get(query);
        if (result != null) {
            List list = this.model2JavaObject(query, Arrays.asList(result));
            if (list != null && list.size() > 0) {
                return (T) list.get(0);
            }
        }
        return null;
    }

    @Override
    public <T> List<T> list(Query<T> query) {
        List<ModelObject> results = modelSession.list(query);
        if (results != null && results.size() > 0) {
            List<T> r = this.model2JavaObject(query, results);
            if (r == null) r = new ArrayList<>();
            return r;
        }
        return new ArrayList<>();
    }

    @Override
    public long count(Query query) {
        return modelSession.count(query);
    }

    @Override
    public <T> Paging<T> paging(Query<T> query) {
        Paging paging = modelSession.paging(query);
        List<ModelObject> results = paging.getObjects();
        if (results != null && results.size() > 0) {
            List<T> r = this.model2JavaObject(query, results);
            paging.setObjects(r);
        }
        return paging;
    }

    private <T> T model2JavaObject(Query query, List<ModelObject> objects) {
        DefaultQuery defaultQuery = (DefaultQuery) query;
        Set<Join> joins = defaultQuery.getTopJoin();
        Class tableClass = defaultQuery.getTableClass();
        return (T) this.model2JavaObject(tableClass, joins, objects);
    }

    private List model2JavaObject(Class tableClass, Set<Join> joins, List<ModelObject> objects) {
        if (joins != null && joins.size() > 0 && objects != null && objects.size() > 0) {
            // 处理一下
            this.processIgnoreJoin(joins);
            for (Join j : joins) {
                DefaultJoin join = (DefaultJoin) j;
                String aliasName = join.getAliasName();

                Class currClass = tableClass;
                while (!currClass.equals(Object.class)) {
                    Field[] fields = currClass.getDeclaredFields();
                    currClass = currClass.getSuperclass();
                    for (Field field : fields) {
                        String fieldName = field.getName();
                        JoinName joinName = field.getAnnotation(JoinName.class);

                        Class fieldType = field.getType();
                        Type type = field.getGenericType();
                        Class genericType = null;
                        if (type instanceof ParameterizedType) {
                            ParameterizedType pt = (ParameterizedType) type;
                            // 获取字段第一个泛型
                            genericType = (Class<?>) pt.getActualTypeArguments()[0];
                        }

                        if (fieldName.equals(aliasName)
                                || (joinName != null && joinName.value().equals(aliasName))) {
                            for (ModelObject object : objects) {
                                Object child = object.get(aliasName);
                                if (child != null) {
                                    if (child instanceof List) {
                                        List childBeans = this.model2JavaObject(Collection.class.isAssignableFrom(fieldType) ? genericType : fieldType,
                                                join.getChildJoin(), this.checkAndCovertList(child, true));
                                        if (childBeans != null && childBeans.size() > 0) {
                                            if (Collection.class.isAssignableFrom(fieldType)) {
                                                object.put(field.getName(), this.list2Collection(childBeans, fieldType));
                                            } else if (fieldType.isArray()) {
                                                object.put(field.getName(), childBeans.toArray());
                                            } else {
                                                object.put(field.getName(), childBeans.get(0));
                                            }
                                        }
                                    } else {
                                        List childBeans = this.model2JavaObject(Collection.class.isAssignableFrom(fieldType) ? genericType : fieldType,
                                                join.getChildJoin(), this.checkAndCovertList(child, false));
                                        if (childBeans != null && childBeans.size() > 0) {
                                            if (Collection.class.isAssignableFrom(fieldType)) {
                                                object.put(field.getName(), this.list2Collection(childBeans, fieldType));
                                            } else if (fieldType.isArray()) {
                                                object.put(field.getName(), childBeans.toArray());
                                            } else {
                                                object.put(field.getName(), childBeans.get(0));
                                            }
                                        }
                                    }
                                }
                            }
                        } else {
                            for (ModelObject object : objects) {
                                Object child = object.get(fieldType);
                                if (child != null) {
                                    if (child instanceof List) {
                                        List childBeans = this.model2JavaObject(fieldType, join.getChildJoin(),
                                                this.checkAndCovertList(child, true));
                                        if (fieldType.isArray()) {
                                            object.put(field.getName(), childBeans.toArray());
                                        } else {
                                            if (childBeans != null && childBeans.size() > 0) {
                                                object.put(field.getName(), childBeans.get(0));
                                            }
                                        }
                                    } else {
                                        List childBeans = this.model2JavaObject(fieldType, join.getChildJoin(),
                                                this.checkAndCovertList(child, false));

                                        if (childBeans != null && childBeans.size() > 0) {
                                            if (fieldType.isArray()) {
                                                object.put(field.getName(), childBeans.toArray());
                                            } else {
                                                object.put(field.getName(), childBeans.get(0));
                                            }
                                        }
                                    }
                                }
                            }

                            if (genericType != null && Collection.class.isAssignableFrom(fieldType)) {
                                for (ModelObject object : objects) {
                                    Object child = object.get(genericType);
                                    if (child != null) {
                                        if (child instanceof List) {
                                            List childBeans = this.model2JavaObject(genericType, join.getChildJoin(),
                                                    this.checkAndCovertList(child, true));
                                            if (childBeans != null && childBeans.size() > 0) {
                                                object.put(field.getName(), this.list2Collection(childBeans, fieldType));
                                            }
                                        } else {
                                            List childBeans = this.model2JavaObject(genericType, join.getChildJoin(),
                                                    this.checkAndCovertList(child, false));

                                            if (childBeans != null && childBeans.size() > 0) {
                                                object.put(field.getName(), this.list2Collection(childBeans, fieldType));
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }

        if (tableClass != null && objects != null && objects.size() > 0) {
            List beans = new ArrayList();
            for (Object object : objects) {
                // 由于某个表多次join会得到相同的结果key，但是这里遍历join的时候会把已经转换好的beans传入
                if (object instanceof ModelObject) {
                    beans.add(model2BeanFactory.toJavaObject((ModelObject) object, tableClass));
                } else if (tableClass.isAssignableFrom(object.getClass())) {
                    beans.add(object);
                }
            }
            return beans;
        }
        return null;
    }

    private List checkAndCovertList(Object child, boolean isList) {
        if (isList) {
            if (child instanceof List) {
                return (List<ModelObject>) child;
            }
        } else {
            if (child instanceof Map) {
                return Arrays.asList(child);
            }
        }
        return null;
    }

    private void processIgnoreJoin(Set<Join> joins) {
        List<Join> ignoreJoins = new ArrayList<>();
        List<Join> rms = new ArrayList<>();
        for (Join j : joins) {
            DefaultJoin join = (DefaultJoin) j;
            if (join.isIgnore()) {
                rms.add(j);
                ignoreJoins.addAll(join.getChildJoin());
            }
        }
        if (ignoreJoins.size() > 0) {
            joins.addAll(ignoreJoins);
        }
        if (rms.size() > 0) joins.removeAll(rms);
        boolean has = false;
        for (Join j : joins) {
            DefaultJoin join = (DefaultJoin) j;
            if (join.isIgnore()) {
                has = true;
                break;
            }
        }
        if (has) {
            processIgnoreJoin(joins);
        }
    }

    private Object list2Collection(List beans, Class c) {
        if (c.isAssignableFrom(List.class)) {
            return beans;
        }
        if (c.isAssignableFrom(Set.class)) {
            return new LinkedHashSet<>(beans);
        }
        return null;
    }

    @Override
    public AutoResult calculate(Function function) {
        return modelSession.calculate(function);
    }

    @Override
    public <T> ZipperTable<T> getZipperTable(Class<T> c) {
        return (ZipperTable<T>) modelSession.getZipperTable(c);
    }

    @Override
    @Deprecated
    public AutoResult getAutonomously(SQLAutonomously autonomously) throws Exception {
        return modelSession.getAutonomously(autonomously);
    }

    @Override
    public AutoResult sql(SQLAutonomously autonomously) {
        return modelSession.sql(autonomously);
    }

    @Override
    @Deprecated
    public AutoResult getAutonomously(TAutonomously autonomously) throws Exception {
        return modelSession.getAutonomously(autonomously);
    }

    @Override
    public AutoResult mapper(TAutonomously autonomously) {
        return modelSession.mapper(autonomously);
    }

    @Override
    public List<DataSourceTableName> getDataSourceNames(Class c) {
        return modelSession.getDataSourceNames(c);
    }


    @Override
    public Query buildQuery(Class clazz) {
        Query query = new DefaultQuery(this, clazz);
        return query;
    }


    @Override
    public Delete buildDelete(Class clazz) {
        Delete delete = new DefaultDelete(this, clazz);
        return delete;
    }

    @Override
    public Update buildUpdate(Class clazz) {
        Update update = new DefaultUpdate(this, clazz);
        return update;
    }

    @Override
    public void close() throws IOException {
        modelSession.close();
    }

    @Override
    public TransactionManager beginTransaction() {
        return modelSession.beginTransaction();
    }

    @Override
    public TransactionManager beginTransaction(Object config) {
        return modelSession.beginTransaction(config);
    }

    @Override
    public <T> T execute(TransactionExecutor<T> executor) {
        return modelSession.execute(executor);
    }
}
