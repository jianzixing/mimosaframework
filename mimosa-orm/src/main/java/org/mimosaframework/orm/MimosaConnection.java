package org.mimosaframework.orm;

import javax.sql.DataSource;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.sql.*;

public class MimosaConnection implements InvocationHandler {
    private DataSource dataSource;
    private Connection conn;

    public MimosaConnection(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    private Connection createConnection() throws SQLException {
        if (conn == null) {
            conn = dataSource.getConnection();
        }
        return conn;
    }

    public static final Connection getConnection(DataSource dataSource) {
        if (dataSource != null) {
            return (Connection) Proxy.newProxyInstance(
                    Connection.class.getClassLoader(),
                    new Class[]{Connection.class},
                    new MimosaConnection(dataSource)
            );
        } else {
            throw new IllegalArgumentException("必须传入DataSource实例");
        }
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        Connection c = this.createConnection();
        Object object = method.invoke(c, args);
        return object;
    }
}
