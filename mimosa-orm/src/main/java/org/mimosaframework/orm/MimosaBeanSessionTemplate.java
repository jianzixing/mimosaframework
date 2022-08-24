package org.mimosaframework.orm;

import org.mimosaframework.core.json.ModelObject;
import org.mimosaframework.orm.annotation.JoinName;
import org.mimosaframework.orm.criteria.*;
import org.mimosaframework.orm.i18n.I18n;
import org.mimosaframework.orm.transaction.Transaction;
import org.mimosaframework.orm.transaction.TransactionCallback;
import org.mimosaframework.orm.transaction.TransactionIsolationType;
import org.mimosaframework.orm.transaction.TransactionManager;
import org.mimosaframework.orm.utils.Model2BeanFactory;
import org.mimosaframework.orm.utils.ModelObjectToBean;

import java.io.IOException;
import java.io.Serializable;
import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.sql.SQLException;
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
    public <T> T saveOrUpdate(T obj) {
        Object json = ModelObject.toJSON(obj);
        if (json instanceof ModelObject) {
            ModelObject model = (ModelObject) json;
            model.setObjectClass(obj.getClass());
            model.clearNull();
            modelSession.saveOrUpdate(model);
            model2BeanFactory.toJavaObject(model, obj, true);
            return obj;
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
    public <T> int update(T update) {
        if (update instanceof Query || update instanceof Delete) {
            throw new IllegalArgumentException(I18n.print("only_update_object"));
        } else if (update instanceof Update) {
            return modelSession.update((Update) update);
        } else {
            Object obj = update;
            if (update instanceof UpdateObject) {
                obj = ((UpdateObject) obj).get();
            }
            Object json = ModelObject.toJSON(obj);
            if (json instanceof ModelObject) {
                ModelObject model = (ModelObject) json;
                model.setObjectClass(obj.getClass());
                model.clearNull();
                if (update instanceof UpdateObject) {
                    Serializable[] fields = ((UpdateObject) update).getNullFields();
                    Serializable[] retains = ((UpdateObject) update).getRetainFields();
                    if (fields != null) {
                        for (Serializable f : fields) {
                            if (f != null) {
                                model.put(f.toString(), null);
                            }
                        }
                    }
                    if (retains != null) {
                        for (Serializable f : retains) {
                            if (f != null) {
                                model.put(f.toString(), model.get(f));
                            }
                        }
                    }
                }
                return modelSession.update(model);
            } else {
                throw new IllegalArgumentException(I18n.print("bean_save_not_json"));
            }
        }
    }

    @Override
    public <T> int update(List<T> objects) {
        return update(objects, null);
    }

    @Override
    public <T> int update(List<T> objects, UpdateObject updateObject) {
        List<ModelObject> updates = null;
        if (objects != null && objects.size() > 0) {
            Serializable[] commonFields = updateObject != null ? updateObject.getNullFields() : null;
            Serializable[] commonRetains = updateObject != null ? updateObject.getRetainFields() : null;

            for (T update : objects) {
                if (updates == null) updates = new ArrayList<>();
                Object object = update;
                if (update instanceof UpdateObject) {
                    object = ((UpdateObject) object).get();
                }
                Object json = ModelObject.toJSON(object);
                if (json instanceof ModelObject) {
                    ModelObject model = (ModelObject) json;
                    model.setObjectClass(object.getClass());
                    model.clearNull();
                    if (update instanceof UpdateObject) {
                        Serializable[] fields = ((UpdateObject) update).getNullFields();
                        if (fields == null) fields = commonFields;
                        Serializable[] retains = ((UpdateObject) update).getRetainFields();
                        if (retains == null) retains = commonRetains;
                        if (fields != null) {
                            for (Serializable f : fields) {
                                if (f != null) {
                                    model.put(f.toString(), null);
                                }
                            }
                        }
                        if (retains != null) {
                            for (Serializable f : retains) {
                                if (f != null) {
                                    model.put(f.toString(), model.get(f));
                                }
                            }
                        }
                    }
                    updates.add(model);
                } else {
                    throw new IllegalArgumentException(I18n.print("bean_save_not_json"));
                }
            }
            return modelSession.update(updates);
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
    public <T> int delete(Class<T> c, Serializable id) {
        return modelSession.delete(c, id);
    }

    @Override
    public <T> T get(Class<T> c, Serializable id) {
        ModelObject object = modelSession.get(c, id);
        return model2BeanFactory.toJavaObject(object, c);
    }

    @Override
    public <T> T get(Query query) {
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
