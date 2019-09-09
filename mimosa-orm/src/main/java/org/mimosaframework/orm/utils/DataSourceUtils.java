package org.mimosaframework.orm.utils;

import org.mimosaframework.orm.MimosaConnection;

import javax.sql.DataSource;
import java.sql.Connection;

public class DataSourceUtils {

    public static final Connection getConnection(DataSource ds) {
        return MimosaConnection.getConnection(ds);
    }
}
