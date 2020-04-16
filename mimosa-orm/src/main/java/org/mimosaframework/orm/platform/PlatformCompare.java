package org.mimosaframework.orm.platform;

import java.sql.SQLException;

public interface PlatformCompare {
    void checking(CompareUpdateTableMate tableMate) throws SQLException;
}
