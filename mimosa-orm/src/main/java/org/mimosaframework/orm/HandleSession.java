package org.mimosaframework.orm;

import org.mimosaframework.core.json.ModelObject;
import org.mimosaframework.orm.criteria.Delete;
import org.mimosaframework.orm.criteria.Function;
import org.mimosaframework.orm.criteria.Query;
import org.mimosaframework.orm.criteria.Update;

import java.io.Closeable;
import java.io.Serializable;
import java.util.List;

public interface HandleSession extends Closeable {

    ModelObject save(ModelObject obj);

    ModelObject saveOrUpdate(ModelObject obj, Object... fields);

    void save(List<ModelObject> objects);

    int update(ModelObject obj, Object... fields);

    int update(List<ModelObject> objects, Object... fields);

    int update(Update update);

    int delete(ModelObject obj);

    int delete(List<ModelObject> objects);

    int delete(Delete delete);

    int delete(Class c, Serializable id);

    ModelObject get(Class c, Serializable id);

    ModelObject get(Query query);

    List<ModelObject> list(Query query);

    long count(Query query);

    Paging<ModelObject> paging(Query query);

    ZipperTable<ModelObject> getZipperTable(Class c);

    AutoResult calculate(Function function);

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
