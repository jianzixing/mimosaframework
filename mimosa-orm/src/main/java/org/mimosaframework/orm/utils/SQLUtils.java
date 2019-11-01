package org.mimosaframework.orm.utils;

import org.mimosaframework.core.utils.i18n.Messages;
import org.mimosaframework.orm.i18n.LanguageMessageFactory;

import javax.sql.DataSource;
import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Map;

public class SQLUtils {

    public static DatabaseTypeEnum getDatabaseType(Connection connection) throws SQLException {
        if (connection != null) {
            DatabaseMetaData metaData = connection.getMetaData();
            if (metaData.getDriverName().toUpperCase()
                    .indexOf(DatabaseTypeEnum.MYSQL.name()) != -1) {
                return DatabaseTypeEnum.MYSQL;
            } else if (metaData.getDriverName().toUpperCase()
                    .indexOf(DatabaseTypeEnum.SQL_SERVER.name().replaceAll("_", " ")) != -1) {
                return DatabaseTypeEnum.SQL_SERVER;
            } else if (metaData.getDriverName().toUpperCase().indexOf(DatabaseTypeEnum.ORACLE.name()) != -1) {
                return DatabaseTypeEnum.ORACLE;
            } else if (metaData.getDriverName().toUpperCase().indexOf(DatabaseTypeEnum.POSTGRESQL.name()) != -1) {
                return DatabaseTypeEnum.POSTGRESQL;
            } else if (metaData.getDriverName().toUpperCase().indexOf("IBM") != -1
                    && metaData.getDriverName().toUpperCase().indexOf("SQLJ") != -1) {
                return DatabaseTypeEnum.DB2;
            } else if (metaData.getDriverName().toUpperCase().indexOf(DatabaseTypeEnum.SQLITE.name()) != -1) {
                return DatabaseTypeEnum.SQLITE;
            }
            throw new SQLException(Messages.get(LanguageMessageFactory.PROJECT,
                    SQLUtils.class, "not_support_db", metaData.getDriverName()));
        } else {
            throw new SQLException(Messages.get(LanguageMessageFactory.PROJECT,
                    SQLUtils.class, "must_create_ds"));
        }
    }

    public static DatabaseTypeEnum getDatabaseType(DataSource dataSource) throws SQLException {
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
            if (rs.wasNull()) {
                fieldValue.put(fieldName, null);
            } else {
                String s = rs.getString(fieldName);
                fieldValue.put(fieldName, s);
            }
        } else if (fieldClassName.equals("java.lang.Integer")) {
            if (rs.wasNull()) {
                fieldValue.put(fieldName, null);
            } else {
                int s = rs.getInt(fieldName);
                fieldValue.put(fieldName, s);// 早期jdk需要包装，jdk1.5后不需要包装
            }
        } else if (fieldClassName.equals("java.lang.Long")) {
            if (rs.wasNull()) {
                fieldValue.put(fieldName, null);
            } else {
                long s = rs.getLong(fieldName);
                fieldValue.put(fieldName, s);
            }
        } else if (fieldClassName.equals("java.lang.Boolean")) {
            if (rs.wasNull()) {
                fieldValue.put(fieldName, null);
            } else {
                boolean s = rs.getBoolean(fieldName);
                fieldValue.put(fieldName, s);
            }
        } else if (fieldClassName.equals("java.lang.Short")) {
            if (rs.wasNull()) {
                fieldValue.put(fieldName, null);
            } else {
                short s = rs.getShort(fieldName);
                fieldValue.put(fieldName, s);
            }
        } else if (fieldClassName.equals("java.lang.Float")) {
            if (rs.wasNull()) {
                fieldValue.put(fieldName, null);
            } else {
                float s = rs.getFloat(fieldName);
                fieldValue.put(fieldName, s);
            }
        } else if (fieldClassName.equals("java.lang.Double")) {
            if (rs.wasNull()) {
                fieldValue.put(fieldName, null);
            } else {
                double s = rs.getDouble(fieldName);
                fieldValue.put(fieldName, s);
            }
        } else if (fieldClassName.equals("java.sql.Timestamp")) {
            if (rs.wasNull()) {
                fieldValue.put(fieldName, null);
            } else {
                java.sql.Timestamp s = rs.getTimestamp(fieldName);
                fieldValue.put(fieldName, s);
            }
        } else if (fieldClassName.equals("java.sql.Date") || fieldClassName.equals("java.util.Date")) {
            if (rs.wasNull()) {
                fieldValue.put(fieldName, null);
            } else {
                java.util.Date s = rs.getDate(fieldName);
                fieldValue.put(fieldName, s);
            }
        } else if (fieldClassName.equals("java.sql.Time")) {
            if (rs.wasNull()) {
                fieldValue.put(fieldName, null);
            } else {
                java.sql.Time s = rs.getTime(fieldName);
                fieldValue.put(fieldName, s);
            }
        } else if (fieldClassName.equals("java.lang.Byte")) {
            if (rs.wasNull()) {
                fieldValue.put(fieldName, null);
            } else {
                byte s = rs.getByte(fieldName);
                fieldValue.put(fieldName, new Byte(s));
            }
        } else if (fieldClassName.equals("[B") || fieldClassName.equals("byte[]")) {
            // byte[]出现在SQL Server中
            if (rs.wasNull()) {
                fieldValue.put(fieldName, null);
            } else {
                byte[] s = rs.getBytes(fieldName);
                fieldValue.put(fieldName, s);
            }
        } else if (fieldClassName.equals("java.math.BigDecimal")) {
            if (rs.wasNull()) {
                fieldValue.put(fieldName, null);
            } else {
                BigDecimal s = rs.getBigDecimal(fieldName);
                fieldValue.put(fieldName, s);
            }
        } else if (fieldClassName.equals("java.lang.Object") || fieldClassName.equals("oracle.sql.STRUCT")) {
            if (rs.wasNull()) {
                fieldValue.put(fieldName, null);
            } else {
                Object s = rs.getObject(fieldName);
                fieldValue.put(fieldName, s);
            }
        } else if (fieldClassName.equals("java.sql.Array") || fieldClassName.equals("oracle.sql.ARRAY")) {
            if (rs.wasNull()) {
                fieldValue.put(fieldName, null);
            } else {
                java.sql.Array s = rs.getArray(fieldName);
                fieldValue.put(fieldName, s);
            }
        } else if (fieldClassName.equals("java.sql.Clob")) {
            if (rs.wasNull()) {
                fieldValue.put(fieldName, null);
            } else {
                java.sql.Clob s = rs.getClob(fieldName);
                fieldValue.put(fieldName, s);
            }
        } else if (fieldClassName.equals("java.sql.Blob")) {
            if (rs.wasNull()) {
                fieldValue.put(fieldName, null);
            } else {
                java.sql.Blob s = rs.getBlob(fieldName);
                fieldValue.put(fieldName, s);
            }
        } else {// 对于其它任何未知类型的处理
            if (rs.wasNull()) {
                fieldValue.put(fieldName, null);
            } else {
                Object s = rs.getObject(fieldName);
                fieldValue.put(fieldName, s);
            }
        }
    }
}
