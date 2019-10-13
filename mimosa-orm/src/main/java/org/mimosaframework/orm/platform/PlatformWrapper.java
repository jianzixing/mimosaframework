package org.mimosaframework.orm.platform;

import org.mimosaframework.core.json.ModelObject;
import org.mimosaframework.orm.ModelObjectConvertKey;
import org.mimosaframework.orm.criteria.DefaultDelete;
import org.mimosaframework.orm.criteria.DefaultFunction;
import org.mimosaframework.orm.criteria.DefaultQuery;
import org.mimosaframework.orm.criteria.DefaultUpdate;
import org.mimosaframework.orm.mapping.MappingField;
import org.mimosaframework.orm.mapping.MappingTable;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;

public interface PlatformWrapper {
    void createTable(MappingTable table) throws SQLException;

    void dropTable(String tableName) throws SQLException;

    void addField(String table, MappingField field) throws SQLException;

    void dropField(String table, MappingField field) throws SQLException;

    Long insert(MappingTable table, ModelObject object) throws SQLException;

    List<Long> inserts(MappingTable table, List<ModelObject> objects) throws SQLException;

    Integer update(MappingTable table, ModelObject object) throws SQLException;

    Integer update(MappingTable table, DefaultUpdate update) throws SQLException;

    Integer update(String sql) throws SQLException;

    Integer delete(MappingTable table, ModelObject object) throws SQLException;

    Integer delete(MappingTable table, DefaultDelete delete) throws SQLException;

    List<ModelObject> select(Map<Object, MappingTable> tables, DefaultQuery query, ModelObjectConvertKey convert) throws SQLException;

    /**
     * 这个查询中没有left join参与
     *
     * @param tables
     * @param query
     * @return
     */
    List<ModelObject> select(Map<Object, MappingTable> tables, DefaultQuery query) throws SQLException;

    List<ModelObject> select(MappingTable table, DefaultFunction function) throws SQLException;

    List<ModelObject> select(String sql) throws SQLException;

    long count(Map<Object, MappingTable> tables, DefaultQuery query) throws SQLException;


    Long simpleInsert(String table, ModelObject object) throws SQLException;

    int simpleDelete(String table, ModelObject where) throws SQLException;

    int simpleUpdate(String table, ModelObject object, ModelObject where) throws SQLException;

    List<ModelObject> simpleSelect(String table, ModelObject where) throws SQLException;

    long simpleCount(String table, ModelObject where) throws SQLException;
}
