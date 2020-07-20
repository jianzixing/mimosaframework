package org.mimosaframework.orm;

import org.mimosaframework.orm.criteria.Delete;
import org.mimosaframework.orm.criteria.Function;
import org.mimosaframework.orm.criteria.Query;
import org.mimosaframework.orm.criteria.Update;

import java.io.Closeable;
import java.io.Serializable;
import java.util.List;

public interface BeanSession extends Closeable {

    <T> T save(T obj);

    <T> T saveAndUpdate(T obj);

    <T> void save(List<T> objects);

    <T> void update(T obj);

    <T> void update(List<T> objects);

    long update(Update update);

    <T> void delete(T obj);

    <T> void delete(List<T> objects);

    long delete(Delete delete);

    <T> void delete(Class<T> c, Serializable id);

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
    AutoResult getAutonomously(SQLAutonomously autonomously) throws Exception;

    AutoResult getAutonomously(TAutonomously autonomously) throws Exception;

    List<DataSourceTableName> getDataSourceNames(Class c);
}
