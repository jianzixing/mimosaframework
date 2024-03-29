package org.mimosaframework.orm.utils;

import org.mimosaframework.core.utils.StringTools;
import org.mimosaframework.orm.i18n.I18n;

import javax.sql.DataSource;
import java.io.Serializable;
import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Map;

public class SQLUtils {

    public static DatabaseType getDatabaseType(Connection connection) throws SQLException {
        if (connection != null) {
            DatabaseMetaData metaData = connection.getMetaData();
            if (metaData.getDriverName().toUpperCase()
                    .indexOf(DatabaseType.MYSQL.name()) != -1) {
                return DatabaseType.MYSQL;
            } else if (metaData.getDriverName().toUpperCase()
                    .indexOf(DatabaseType.SQL_SERVER.name().replaceAll("_", " ")) != -1) {
                return DatabaseType.SQL_SERVER;
            } else if (metaData.getDriverName().toUpperCase().indexOf(DatabaseType.ORACLE.name()) != -1) {
                return DatabaseType.ORACLE;
            } else if (metaData.getDriverName().toUpperCase().indexOf(DatabaseType.POSTGRESQL.name()) != -1) {
                return DatabaseType.POSTGRESQL;
            } else if (metaData.getDriverName().toUpperCase().indexOf("IBM") != -1
                    && metaData.getDriverName().toUpperCase().indexOf("SQLJ") != -1) {
                return DatabaseType.DB2;
            } else if (metaData.getDriverName().toUpperCase().indexOf(DatabaseType.SQLITE.name()) != -1) {
                return DatabaseType.SQLITE;
            }
            throw new SQLException(I18n.print("not_support_db", metaData.getDriverName()));
        } else {
            throw new SQLException(I18n.print("must_create_ds"));
        }
    }

    public static DatabaseType getDatabaseType(DataSource dataSource) throws SQLException {
        if (dataSource != null) {
            Connection connection = null;
            try {
                connection = DataSourceUtils.getConnection(dataSource);
                return SQLUtils.getDatabaseType(connection);
            } finally {
                try {
                    if (connection != null) connection.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
        return null;
    }

    public static void recordMappingToMap(String fieldClassName, String fieldName, ResultSet rs, Map fieldValue)
            throws SQLException {
        // fieldName = fieldName.toLowerCase();
        // 优先规则：常用类型靠前
        if (fieldClassName.equals("java.lang.String")) {
            String s = rs.getString(fieldName);
            if (rs.wasNull()) {
                fieldValue.put(fieldName, null);
            } else {
                fieldValue.put(fieldName, s);
            }
        } else if (fieldClassName.equals("java.lang.Integer")) {
            int s = rs.getInt(fieldName);
            if (rs.wasNull()) {
                fieldValue.put(fieldName, null);
            } else {
                fieldValue.put(fieldName, s);// 早期jdk需要包装，jdk1.5后不需要包装
            }
        } else if (fieldClassName.equals("java.lang.Long")) {
            long s = rs.getLong(fieldName);
            if (rs.wasNull()) {
                fieldValue.put(fieldName, null);
            } else {
                fieldValue.put(fieldName, s);
            }
        } else if (fieldClassName.equals("java.lang.Boolean")) {
            boolean s = rs.getBoolean(fieldName);
            if (rs.wasNull()) {
                fieldValue.put(fieldName, null);
            } else {
                fieldValue.put(fieldName, s);
            }
        } else if (fieldClassName.equals("java.lang.Short")) {
            short s = rs.getShort(fieldName);
            if (rs.wasNull()) {
                fieldValue.put(fieldName, null);
            } else {
                fieldValue.put(fieldName, s);
            }
        } else if (fieldClassName.equals("java.lang.Float")) {
            float s = rs.getFloat(fieldName);
            if (rs.wasNull()) {
                fieldValue.put(fieldName, null);
            } else {
                fieldValue.put(fieldName, s);
            }
        } else if (fieldClassName.equals("java.lang.Double")) {
            double s = rs.getDouble(fieldName);
            if (rs.wasNull()) {
                fieldValue.put(fieldName, null);
            } else {
                fieldValue.put(fieldName, s);
            }
        } else if (fieldClassName.equals("java.sql.Timestamp")) {
            java.sql.Timestamp s = rs.getTimestamp(fieldName);
            if (rs.wasNull()) {
                fieldValue.put(fieldName, null);
            } else {
                fieldValue.put(fieldName, s);
            }
        } else if (fieldClassName.equals("java.sql.Date") || fieldClassName.equals("java.util.Date")) {
            java.util.Date s = rs.getDate(fieldName);
            if (rs.wasNull()) {
                fieldValue.put(fieldName, null);
            } else {
                fieldValue.put(fieldName, s);
            }
        } else if (fieldClassName.equals("java.sql.Time")) {
            java.sql.Time s = rs.getTime(fieldName);
            if (rs.wasNull()) {
                fieldValue.put(fieldName, null);
            } else {
                fieldValue.put(fieldName, s);
            }
        } else if (fieldClassName.equals("java.lang.Byte")) {
            byte s = rs.getByte(fieldName);
            if (rs.wasNull()) {
                fieldValue.put(fieldName, null);
            } else {
                fieldValue.put(fieldName, new Byte(s));
            }
        } else if (fieldClassName.equals("[B") || fieldClassName.equals("byte[]")) {
            // byte[]出现在SQL Server中
            byte[] s = rs.getBytes(fieldName);
            if (rs.wasNull()) {
                fieldValue.put(fieldName, null);
            } else {
                fieldValue.put(fieldName, s);
            }
        } else if (fieldClassName.equals("java.math.BigDecimal")) {
            BigDecimal s = rs.getBigDecimal(fieldName);
            if (rs.wasNull()) {
                fieldValue.put(fieldName, null);
            } else {
                fieldValue.put(fieldName, s);
            }
        } else if (fieldClassName.equals("java.lang.Object") || fieldClassName.equals("oracle.sql.STRUCT")) {
            Object s = rs.getObject(fieldName);
            if (rs.wasNull()) {
                fieldValue.put(fieldName, null);
            } else {
                fieldValue.put(fieldName, s);
            }
        } else if (fieldClassName.equals("java.sql.Array") || fieldClassName.equals("oracle.sql.ARRAY")) {
            java.sql.Array s = rs.getArray(fieldName);
            if (rs.wasNull()) {
                fieldValue.put(fieldName, null);
            } else {
                fieldValue.put(fieldName, s);
            }
        } else if (fieldClassName.equals("java.sql.Clob")) {
            java.sql.Clob s = rs.getClob(fieldName);
            if (rs.wasNull()) {
                fieldValue.put(fieldName, null);
            } else {
                LOBLoader.Loader loader = LOBLoader.currentLoader();
                if (loader != null) loader.lob(fieldValue, fieldName, s);
            }
        } else if (fieldClassName.equals("java.sql.Blob")) {
            java.sql.Blob s = rs.getBlob(fieldName);
            if (rs.wasNull()) {
                fieldValue.put(fieldName, null);
            } else {
                LOBLoader.Loader loader = LOBLoader.currentLoader();
                if (loader != null) loader.lob(fieldValue, fieldName, s);
            }
        } else {// 对于其它任何未知类型的处理
            Object s = rs.getObject(fieldName);
            if (rs.wasNull()) {
                fieldValue.put(fieldName, null);
            } else {
                fieldValue.put(fieldName, s);
            }
        }
    }

    public static void checkAsName(Serializable str) {
        if (str == null) return;
        String as = str.toString();
        if (StringTools.isNotEmpty(as)) {
            char[] chars = as.toCharArray();
            for (char c : chars) {
                if (!(c >= 'a' && c <= 'z' || c >= 'A' && c <= 'Z' || c >= '0' && c <= '9' || c == '_')) {
                    throw new IllegalArgumentException(I18n.print("query_as_not_legal", as));
                }
            }
        }
    }
}
