package org.mimosaframework.orm.platform;

import org.mimosaframework.core.json.ModelObject;
import org.mimosaframework.orm.criteria.DefaultFunction;
import org.mimosaframework.orm.criteria.DefaultQuery;
import org.mimosaframework.orm.mapping.MappingTable;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;

public interface SelectDatabasePorter {
    /**
     * 直接查询数据，这里不能带limit，需要先确定好要查询的主表主键
     *
     * @param tables
     * @param query
     * @return
     */
    SelectResult select(Map<Object, MappingTable> tables, DefaultQuery query) throws SQLException;

    List<ModelObject> select(MappingTable table, DefaultFunction function) throws SQLException;

    List<ModelObject> count(Map<Object, MappingTable> tables, DefaultQuery query) throws SQLException;

    /**
     * 查询出所需要的主表的主键值
     * 然后再通过主键值进行join查询
     *
     * @param tables
     * @param query
     * @return
     */
    List<ModelObject> selectPrimaryKey(Map<Object, MappingTable> tables, DefaultQuery query) throws SQLException;

    List<ModelObject> simpleSelect(String table, ModelObject where) throws SQLException;

    List<ModelObject> simpleCount(String table, ModelObject where) throws SQLException;
}
