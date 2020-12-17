package org.mimosaframework.orm.transaction;

import java.sql.SQLException;

public interface TransactionManager {
    void commit() throws SQLException;

    void rollback() throws SQLException;
}
