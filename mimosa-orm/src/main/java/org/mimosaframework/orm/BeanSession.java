package org.mimosaframework.orm;

import org.mimosaframework.orm.criteria.Function;
import org.mimosaframework.orm.criteria.Query;
import org.mimosaframework.orm.criteria.UpdateObject;

import java.io.Closeable;
import java.io.Serializable;
import java.util.List;

public interface BeanSession extends Closeable {

    <T> T save(T obj);

    <T> T saveOrUpdate(T obj);

    <T> void save(List<T> objects);

    <T> int update(T obj);

    <T> int update(List<T> objects);

    <T> int edit(T obj, Serializable... fields);

    <T> int edit(List<T> objects, Serializable... fields);

    <T> int delete(T obj);

    <T> int delete(List<T> objects);

    <T> int delete(Class<T> c, Serializable id);

    <T> T get(Class<T> c, Serializable id);

    <T> T get(Query query);

    <T> List<T> list(Query query);

    long count(Query query);

    <T> Paging<T> paging(Query query);

    AutoResult calculate(Function function);

    <T> ZipperTable<T> getZipperTable(Class<T> c);

    /**
     * 指定在哪些数据源上执行SQL
     * 得到的结果会汇总然后给使用者
     *
     * @param autonomously
     * @return
     * @throws Exception
     */
    @Deprecated
    AutoResult getAutonomously(SQLAutonomously autonomously) throws Exception;

    AutoResult sql(SQLAutonomously autonomously);

    @Deprecated
    AutoResult getAutonomously(TAutonomously autonomously) throws Exception;

    AutoResult mapper(TAutonomously autonomously);

    List<DataSourceTableName> getDataSourceNames(Class c);
}
