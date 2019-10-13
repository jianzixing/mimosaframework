package org.mimosaframework.orm;

import org.mimosaframework.core.json.ModelObject;
import org.mimosaframework.orm.criteria.Delete;
import org.mimosaframework.orm.criteria.Function;
import org.mimosaframework.orm.criteria.Query;
import org.mimosaframework.orm.criteria.Update;

import java.io.Closeable;
import java.io.Serializable;
import java.sql.SQLException;
import java.util.List;

public interface Session extends Closeable {

    ModelObject save(ModelObject obj);

    ModelObject saveAndUpdate(ModelObject obj);

    void save(List<ModelObject> objects);

    void update(ModelObject obj);

    void update(List<ModelObject> objects);

    long update(Update update);

    void delete(ModelObject obj);

    void delete(List<ModelObject> objects);

    long delete(Delete delete);

    void delete(Class c, Serializable id);

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
    AutoResult getAutonomously(SQLAutonomously autonomously) throws Exception;

    AutoResult getAutonomously(TAutonomously autonomously) throws Exception;

    List<DataSourceTableName> getDataSourceNames(Class c);
}
