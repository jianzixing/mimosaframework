package org.mimosaframework.orm;

import org.mimosaframework.orm.criteria.Delete;
import org.mimosaframework.orm.criteria.Function;
import org.mimosaframework.orm.criteria.Query;
import org.mimosaframework.orm.criteria.Update;

import java.io.Closeable;
import java.io.Serializable;
import java.util.List;
import java.util.Map;

public interface BeanSession extends Closeable {

    <T> T save(T obj);

    <T> T saveAndUpdate(T obj);

    <T> void save(List<T> objects);

    <T> void update(T obj);

    <T> void update(List<T> objects);

    void update(Update update);

    <T> void delete(T obj);

    <T> void delete(List<T> objects);

    void delete(Delete delete);

    <T> void delete(Class<T> c, Serializable id);

    <T> T get(Class<T> c, Serializable id);

    <T> T get(Query query);

    <T> List<T> list(Query query);

    long count(Query query);

    <T> Paging<T> paging(Query query);

    AutoResult calculate(Function function);

    <T> ZipperTable<T> getZipperTable(Class<T> c);
}
