package org.mimosaframework.orm.mapping;

import java.sql.SQLException;

public interface StartCompareMapping {
    NotMatchObject doMapping() throws SQLException;
}
