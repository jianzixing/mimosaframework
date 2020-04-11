package org.mimosaframework.orm.platform;

import org.mimosaframework.core.json.ModelObject;

import java.sql.SQLException;
import java.util.List;

public interface JDBCExecutor {
    void setDatabaseExecutorCallback(DatabaseExecutorCallback callback);

    boolean execute(JDBCTraversing structure) throws SQLException;

    int delete(JDBCTraversing structure) throws SQLException;

    List<Long> insert(JDBCTraversing structure) throws SQLException;

    void inserts(BatchPorterStructure structure) throws SQLException;

    List<ModelObject> select(JDBCTraversing structure) throws SQLException;

    int update(JDBCTraversing structure) throws SQLException;
}
