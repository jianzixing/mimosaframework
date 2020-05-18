package org.mimosaframework.orm;

import org.mimosaframework.core.json.ModelObject;
import org.mimosaframework.orm.annotation.JoinName;
import org.mimosaframework.orm.criteria.*;
import org.mimosaframework.orm.exception.TransactionException;
import org.mimosaframework.orm.i18n.I18n;
import org.mimosaframework.orm.transaction.Transaction;
import org.mimosaframework.orm.transaction.TransactionCallback;
import org.mimosaframework.orm.transaction.TransactionIsolationType;
import org.mimosaframework.orm.transaction.TransactionPropagationType;
import org.mimosaframework.orm.utils.Model2BeanFactory;
import org.mimosaframework.orm.utils.ModelObjectToBean;

import java.io.IOException;
import java.io.Serializable;
import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.*;

public class MimosaBeanSessionTemplate extends AbstractAuxiliaryTemplate implements BeanSessionTemplate {
    private MimosaSessionTemplate modelSession = new MimosaSessionTemplate();
    private Model2BeanFactory model2BeanFactory = new ModelObjectToBean();
    private SessionFactory sessionFactory;

    public SessionFactory getSessionFactory() {
        return sessionFactory;
    }

    public void setModel2BeanFactory(Model2BeanFactory model2BeanFactory) {
        this.model2BeanFactory = model2BeanFactory;
    }

    public void setSessionFactory(SessionFactory sessionFactory) {
        super.setSessionFactory(sessionFactory);
        this.sessionFactory = sessionFactory;
        modelSession.setSessionFactory(sessionFactory);
    }

    @Override
    public <T> T save(T obj) {
        Object json = ModelObject.toJSON(obj);
        if (json instanceof ModelObject) {
            ModelObject model = (ModelObject) json;
            model.setObjectClass(obj.getClass());
            modelSession.save(model);

            model2BeanFactory.toJavaObject(model, obj);
            return obj;
        } else {
            throw new IllegalArgumentException(I18n.print("bean_save_not_json"));
        }
    }

    @Override
    public <T> T saveAndUpdate(T obj) {
        Object json = ModelObject.toJSON(obj);
        if (json instanceof ModelObject) {
            ModelObject model = (ModelObject) json;
            model.setObjectClass(obj.getClass());
            modelSession.saveAndUpdate(model);
            return (T) model2BeanFactory.toJavaObject(model, obj.getClass());
        } else {
            throw new IllegalArgumentException(I18n.print("bean_save_not_json"));
        }
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
                    saves.add(model);
                } else {
                    throw new IllegalArgumentException(I18n.print("bean_save_not_json"));
                }
            }
            modelSession.save(saves);
        }
    }

    @Override
    public <T> void update(T obj) {
        Object json = ModelObject.toJSON(obj);
        if (json instanceof ModelObject) {
            ModelObject model = (ModelObject) json;
            model.setObjectClass(obj.getClass());
            modelSession.update(model);
        } else {
            throw new IllegalArgumentException(I18n.print("bean_save_not_json"));
        }
    }

    @Override
    public <T> void update(List<T> objects) {
        List<ModelObject> updates = null;
        if (objects != null && objects.size() > 0) {
            for (T object : objects) {
                if (updates == null) updates = new ArrayList<>();
                Object json = ModelObject.toJSON(object);
                if (json instanceof ModelObject) {
                    ModelObject model = (ModelObject) json;
                    model.setObjectClass(object.getClass());
                    updates.add(model);
                } else {
                    throw new IllegalArgumentException(I18n.print("bean_save_not_json"));
                }
            }
            modelSession.update(updates);
        }
    }

    @Override
    public long update(Update update) {
        return modelSession.update(update);
    }

    @Override
    public <T> void delete(T obj) {
        Object json = ModelObject.toJSON(obj);
        if (json instanceof ModelObject) {
            ModelObject model = (ModelObject) json;
            model.setObjectClass(obj.getClass());
            modelSession.delete(model);
        } else {
            throw new IllegalArgumentException(I18n.print("bean_save_not_json"));
        }
    }

    @Override
    public <T> void delete(List<T> objects) {
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
            modelSession.delete(deletes);
        }
    }

    @Override
    public long delete(Delete delete) {
        return modelSession.delete(delete);
    }

    @Override
    public <T> void delete(Class<T> c, Serializable id) {
        modelSession.delete(c, id);
    }

    @Override
    public <T> T get(Class<T> c, Serializable id) {
        ModelObject object = modelSession.get(c, id);
        return model2BeanFactory.toJavaObject(object, c);
    }

    @Override
    public <T> T get(Query query) {
        ModelObject result = modelSession.get(query);
        List list = this.model2JavaObject(query, Arrays.asList(result));
        if (list != null && list.size() > 0) {
            return (T) list.get(0);
        }
        return null;
    }

    @Override
    public <T> List<T> list(Query query) {
        List<ModelObject> results = modelSession.list(query);
        if (results != null && results.size() > 0) {
            List<T> r = this.model2JavaObject(query, results);
            return r;
        }
        return null;
    }

    @Override
    public long count(Query query) {
        return modelSession.count(query);
    }

    @Override
    public <T> Paging<T> paging(Query query) {
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
            for (Join j : joins) {
                DefaultJoin join = (DefaultJoin) j;
                String aliasName = join.getAliasName();

                Field[] fields = tableClass.getDeclaredFields();
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
                                            join.getChildJoin(), (List<ModelObject>) child);
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
                                            join.getChildJoin(), Arrays.asList((ModelObject) child));
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
                                            (List<ModelObject>) child);
                                    if (fieldType.isArray()) {
                                        object.put(field.getName(), childBeans.toArray());
                                    } else {
                                        if (childBeans != null && childBeans.size() > 0) {
                                            object.put(field.getName(), childBeans.get(0));
                                        }
                                    }
                                } else {
                                    List childBeans = this.model2JavaObject(fieldType, join.getChildJoin(),
                                            Arrays.asList((ModelObject) child));

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
                                                (List<ModelObject>) child);
                                        if (childBeans != null && childBeans.size() > 0) {
                                            object.put(field.getName(), this.list2Collection(childBeans, fieldType));
                                        }
                                    } else {
                                        List childBeans = this.model2JavaObject(genericType, join.getChildJoin(),
                                                Arrays.asList((ModelObject) child));

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

        if (tableClass != null && objects != null && objects.size() > 0) {
            List beans = new ArrayList();
            for (ModelObject object : objects) {
                beans.add(model2BeanFactory.toJavaObject(object, tableClass));
            }
            return beans;
        }
        return null;
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
    public AutoResult getAutonomously(SQLAutonomously autonomously) throws Exception {
        return modelSession.getAutonomously(autonomously);
    }

    @Override
    public AutoResult getAutonomously(TAutonomously autonomously) throws Exception {
        return modelSession.getAutonomously(autonomously);
    }

    @Override
    public List<DataSourceTableName> getDataSourceNames(Class c) {
        return modelSession.getDataSourceNames(c);
    }


    @Override
    public Query query(Class clazz) {
        Query query = new DefaultQuery(this, clazz);
        return query;
    }


    @Override
    public Delete delete(Class clazz) {
        Delete delete = new DefaultDelete(this, clazz);
        return delete;
    }

    @Override
    public Update update(Class clazz) {
        Update update = new DefaultUpdate(this, clazz);
        return update;
    }

    @Override
    public void close() throws IOException {
        modelSession.close();
    }

    @Override
    public Transaction beginTransaction() throws TransactionException {
        return modelSession.beginTransaction();
    }

    @Override
    public Transaction createTransaction() {
        return modelSession.createTransaction();
    }

    @Override
    public <T> T execute(TransactionCallback<T> callback) throws TransactionException {
        return modelSession.execute(callback);
    }

    @Override
    public <T> T execute(TransactionCallback<T> callback, TransactionPropagationType pt) throws TransactionException {
        return modelSession.execute(callback, pt);
    }

    @Override
    public <T> T execute(TransactionCallback<T> callback, TransactionIsolationType it) throws TransactionException {
        return modelSession.execute(callback, it);
    }

    @Override
    public <T> T execute(TransactionCallback<T> callback, TransactionPropagationType pt, TransactionIsolationType it) throws TransactionException {
        return modelSession.execute(callback, pt, it);
    }
}
