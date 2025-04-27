package org.mimosaframework.orm;

import org.mimosaframework.core.FieldFunction;
import org.mimosaframework.orm.criteria.Function;
import org.mimosaframework.orm.criteria.Query;
import org.mimosaframework.orm.criteria.Update;

import java.io.Closeable;
import java.util.List;

public interface BeanSession extends Closeable {

    <T> T save(T obj);

    <T> T saveOrUpdate(T obj, Object... fields);

    <T> T saveOrUpdateSelective(T obj, FieldFunction<T>... fields);

    <T> void save(List<T> objects);

    int update(Update update);

    <T> int update(T obj, Object... fields);

    <T> int updateSelective(T obj, FieldFunction<T>... fields);

    <T> int update(List<T> objects, Object... fields);

    <T> int updateSelective(List<T> objects, FieldFunction<T>... fields);

    <T> int modify(T obj);

    <T> int delete(T obj);

    <T> int delete(List<T> objects);

    <T> int delete(Class<T> c, Object id);

    <T> T get(Class<T> c, Object id);

    <T> T get(Query<T> query);

    <T> List<T> list(Query<T> query);

    long count(Query query);

    <T> Paging<T> paging(Query<T> query);

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
    AutoResult getAutonomously(Sql autonomously) throws Exception;

    AutoResult sql(Sql autonomously);

    @Deprecated
    AutoResult getAutonomously(Mapper autonomously) throws Exception;

    AutoResult mapper(Mapper autonomously);

    List<DataSourceTableName> getDataSourceNames(Class c);
}
