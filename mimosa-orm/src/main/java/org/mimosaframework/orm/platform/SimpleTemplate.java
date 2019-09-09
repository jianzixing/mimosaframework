package org.mimosaframework.orm.platform;

import org.mimosaframework.core.json.ModelObject;
import org.mimosaframework.orm.DynamicTable;

import java.sql.SQLException;
import java.util.List;

public interface SimpleTemplate {
    void createTable(DynamicTable table) throws SQLException;

    void dropTable(String table) throws SQLException;

    void addFields(DynamicTable table) throws SQLException;

    void dropField(DynamicTable table) throws SQLException;

    long save(String table, ModelObject object) throws SQLException;

    int delete(String table, ModelObject where) throws SQLException;

    int update(String table, ModelObject object, ModelObject where) throws SQLException;

    List<ModelObject> get(String table, ModelObject where) throws SQLException;

    long count(String table, ModelObject where) throws SQLException;

    List<ModelObject> sqlRunnerQuery(String sql) throws SQLException;

    int sqlRunnerUpdate(String sql) throws SQLException;
}
