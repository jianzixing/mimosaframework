package org.mimosaframework.orm;

import org.mimosaframework.core.json.ModelObject;
import org.mimosaframework.orm.annotation.JoinName;
import org.mimosaframework.orm.criteria.*;
import org.mimosaframework.orm.i18n.I18n;

import java.io.IOException;
import java.io.Serializable;
import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.*;

public class MimosaBeanSessionTemplate implements BeanSessionTemplate {
    private MimosaSessionTemplate modelSession = new MimosaSessionTemplate();
    private SessionFactory sessionFactory;

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
            modelSession.save(model);
            return (T) ModelObject.toJavaObject(model, obj.getClass());
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
            return (T) ModelObject.toJavaObject(model, obj.getClass());
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
    public void update(Update update) {
        modelSession.update(update);
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
    public void delete(Delete delete) {
        modelSession.delete(delete);
    }

    @Override
    public <T> void delete(Class<T> c, Serializable id) {
        modelSession.delete(c, id);
    }

    @Override
    public <T> T get(Class<T> c, Serializable id) {
        ModelObject object = modelSession.get(c, id);
        return ModelObject.toJavaObject(object, c);
    }

    @Override
    public <T> T get(Query query) {
        ModelObject result = modelSession.get(query);
        return this.model2JavaObject(query, Arrays.asList(result));
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
        Map<ModelObject, Map<Field, Object>> objectLinked = null;
        if (joins != null && joins.size() > 0 && objects != null && objects.size() > 0) {
            if (objectLinked == null) objectLinked = new HashMap<>();
            for (Join j : joins) {
                DefaultJoin join = (DefaultJoin) j;
                String aliasName = join.getAliasName();

                Field[] fields = tableClass.getDeclaredFields();
                for (Field field : fields) {
                    String fieldName = field.getName();
                    JoinName joinName = field.getAnnotation(JoinName.class);

                    Class fieldType = field.getDeclaringClass();
                    Type type = field.getGenericType();
                    ParameterizedType pt = (ParameterizedType) type;
                    // 获取字段第一个泛型
                    Class genericType = (Class<?>) pt.getActualTypeArguments()[0];

                    for (ModelObject object : objects) {
                        Map<Field, Object> map = objectLinked.get(object);
                        if (map == null) {
                            map = new HashMap<>();
                            objectLinked.put(object, map);
                        }
                        Object child = object.get(fieldType);
                        if (child != null) {
                            if (child instanceof List) {
                                List childBeans = this.model2JavaObject(fieldType, join.getChildJoin(),
                                        (List<ModelObject>) child);
                                if (fieldType.isArray()) {
                                    map.put(field, childBeans.toArray());
                                } else {
                                    if (childBeans != null && childBeans.size() > 0) {
                                        map.put(field, childBeans.get(0));
                                    }
                                }
                            } else {
                                List childBeans = this.model2JavaObject(fieldType, join.getChildJoin(),
                                        Arrays.asList((ModelObject) child));

                                if (childBeans != null && childBeans.size() > 0) {
                                    if (fieldType.isArray()) {
                                        map.put(field, childBeans.toArray());
                                    } else {
                                        map.put(field, childBeans.get(0));
                                    }
                                }
                            }
                        }
                    }

                    if (genericType != null && fieldType.isAssignableFrom(Collection.class)) {
                        for (ModelObject object : objects) {
                            Map<Field, Object> map = objectLinked.get(object);
                            if (map == null) {
                                map = new HashMap<>();
                                objectLinked.put(object, map);
                            }

                            Object child = object.get(genericType);
                            if (child instanceof List) {
                                List childBeans = this.model2JavaObject(fieldType, join.getChildJoin(),
                                        (List<ModelObject>) child);
                                if (childBeans != null && childBeans.size() > 0) {
                                    map.put(field, this.list2Collection(childBeans, fieldType));
                                }
                            } else {
                                List childBeans = this.model2JavaObject(fieldType, join.getChildJoin(),
                                        Arrays.asList((ModelObject) child));

                                if (childBeans != null && childBeans.size() > 0) {
                                    map.put(field, this.list2Collection(childBeans, fieldType));
                                }
                            }
                        }
                    }


                    if (fieldName.equals(aliasName)
                            || (joinName != null && joinName.value().equals(aliasName))) {
                        for (ModelObject object : objects) {
                            Map<Field, Object> map = objectLinked.get(object);
                            if (map == null) {
                                map = new HashMap<>();
                                objectLinked.put(object, map);
                            }
                            Object child = object.get(aliasName);
                            if (child instanceof List) {
                                List childBeans = this.model2JavaObject(fieldType, join.getChildJoin(),
                                        (List<ModelObject>) child);
                                if (childBeans != null && childBeans.size() > 0) {
                                    if (fieldType.isAssignableFrom(Collection.class)) {
                                        map.put(field, this.list2Collection(childBeans, fieldType));
                                    } else if (fieldType.isArray()) {
                                        map.put(field, childBeans.toArray());
                                    } else {
                                        map.put(field, childBeans.get(0));
                                    }
                                }
                            } else {
                                List childBeans = this.model2JavaObject(fieldType, join.getChildJoin(),
                                        Arrays.asList((ModelObject) child));
                                if (childBeans != null && childBeans.size() > 0) {
                                    if (fieldType.isAssignableFrom(Collection.class)) {
                                        map.put(field, this.list2Collection(childBeans, fieldType));
                                    } else if (fieldType.isArray()) {
                                        map.put(field, childBeans.toArray());
                                    } else {
                                        map.put(field, childBeans.get(0));
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
                beans.add(ModelObject.toJavaObject(object, tableClass));
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
    public Query query(Class clazz) {
        Query query = new DefaultQuery(clazz);
        return query;
    }


    @Override
    public Delete delete(Class clazz) {
        Delete delete = new DefaultDelete(clazz);
        return delete;
    }

    @Override
    public Update update(Class clazz) {
        Update update = new DefaultUpdate(clazz);
        return update;
    }

    @Override
    public void close() throws IOException {
        modelSession.close();
    }
}
