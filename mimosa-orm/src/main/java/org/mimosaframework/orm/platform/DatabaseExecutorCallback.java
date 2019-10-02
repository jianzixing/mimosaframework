package org.mimosaframework.orm.platform;

import org.mimosaframework.core.json.ModelObject;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public interface DatabaseExecutorCallback {
    void select(Connection connection, PreparedStatement statement, ResultSet resultSet, ModelObject result) throws SQLException;
}
