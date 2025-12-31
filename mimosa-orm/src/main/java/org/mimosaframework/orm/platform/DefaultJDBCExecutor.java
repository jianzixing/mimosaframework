package org.mimosaframework.orm.platform;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.mimosaframework.core.json.ModelObject;
import org.mimosaframework.orm.MimosaDataSource;
import org.mimosaframework.orm.criteria.Keyword;
import org.mimosaframework.orm.i18n.I18n;
import org.mimosaframework.orm.transaction.Transaction;
import org.mimosaframework.orm.transaction.TransactionManagerUtils;
import org.mimosaframework.orm.utils.SQLUtils;

import java.sql.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

public class DefaultJDBCExecutor implements JDBCExecutor {
    private static final Log logger = LogFactory.getLog(DefaultJDBCExecutor.class);
    private SessionContext sessionContext;
    private boolean isIgnoreEmptySlave;
    private boolean isMaster;
    private boolean isShowSql;
    private DatabaseExecutorCallback callback;

    public DefaultJDBCExecutor(SessionContext sessionContext) {
        this.sessionContext = sessionContext;
        this.isShowSql = sessionContext.isShowSql();
        this.isIgnoreEmptySlave = sessionContext.isIgnoreEmptySlave();
        this.isMaster = sessionContext.isMaster();
    }

    @Override
    public void setDatabaseExecutorCallback(DatabaseExecutorCallback callback) {
        this.callback = callback;
    }

    private void close(Connection connection, Statement statement) {
        if (statement != null) {
            try {
                statement.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        Transaction transaction = this.sessionContext.getTransaction();
        if (transaction != null && this.isMaster) {
            // 如果是从事务对象中获取到的则不需要做任何事，关闭操作交给了DefaultSession
            // 从事务对象中获取连接并不代表已经开启了事务，而是资源统一交给Transaction管理
        } else {
            // 如果事务为空或者使用的是从数据库链接则直接关闭
            try {
                connection.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private Connection getConnection() throws SQLException {
        if (this.isMaster) {
            Transaction transaction = this.sessionContext.getTransaction();
            if (transaction != null) {
                Connection connection = transaction.getConnection();
                // 如果存在事务则加入到mimosa实现的事务中去
                TransactionManagerUtils.bindIfTransactional(transaction);
                return connection;
            } else {
                throw new NullPointerException("execute sql but can't get connection with miss transaction object");
            }
        } else {
            MimosaDataSource dataSource = sessionContext.getDataSource();
            return dataSource.getConnection(false, sessionContext.getSlaveName(), this.isIgnoreEmptySlave);
        }
    }


    private void logger(JDBCTraversing structure) {
        if (isShowSql && structure.isShowSQL()) {
            SQLBuilder sqlBuilder = structure.getSqlBuilder();
            String sqlStr = structure.getSql();

            if (sqlBuilder != null) {
                SQLBuilderCombine sql = sqlBuilder.toSQLString();

                StringBuilder sb = new StringBuilder();
                sb.append("\r\nSQL-LOG Action: " + structure.getTypeForRunner().name());
                sb.append("\r\nSQL-LOG String: ");
                sb.append(sql.getSql());

                if (structure instanceof BatchPorterStructure) {
                    List<ModelObject> objects = ((BatchPorterStructure) structure).getObjects();
                    List<String> fields = ((BatchPorterStructure) structure).getFields();
                    if (objects != null) {
                        sb.append("\r\nSQL-LOG Params: ");
                        for (ModelObject object : objects) {
                            sb.append("\r\n");
                            Iterator<String> iterator = fields.iterator();
                            while (iterator.hasNext()) {
                                String f = iterator.next();
                                sb.append(f + "=" + object.get(f));
                                if (iterator.hasNext()) {
                                    sb.append(" , ");
                                }
                            }
                        }
                    }
                } else {
                    List<SQLDataPlaceholder> placeholders = sql.getPlaceholders();
                    if (placeholders != null) {
                        sb.append("\r\nSQL-LOG Params: ");
                        Iterator<SQLDataPlaceholder> iterator = placeholders.iterator();
                        while (iterator.hasNext()) {
                            SQLDataPlaceholder placeholder = iterator.next();
                            if (placeholder.getValue() instanceof List) {
                                sb.append(placeholder.getName() + " = " + placeholder.getValue());
                            } else {
                                sb.append(placeholder.getName() + " = " + placeholder.getValue());
                            }
                            if (iterator.hasNext()) {
                                sb.append(" , ");
                            }
                        }
                    }
                }
                sb.append("\r\n");
                logger.info(sb.toString());
            }

            if (sqlStr != null) {
                StringBuilder sb = new StringBuilder();
                sb.append(sqlStr);

                List<SQLDataPlaceholder> sqlDataPlaceholders = structure.getSqlDataPlaceholders();
                if (sqlDataPlaceholders != null) {
                    sb.append("\r\nSQL-LOG Params: ");
                    Iterator<SQLDataPlaceholder> iterator = sqlDataPlaceholders.iterator();
                    while (iterator.hasNext()) {
                        SQLDataPlaceholder mapping = iterator.next();
                        sb.append(mapping.getName() + "=" + mapping.getValue());
                        if (iterator.hasNext()) {
                            sb.append(" , ");
                        }
                    }
                }
                logger.info(sb.toString());
            }
        }
    }

    private PreparedStatement replacePlaceholder(Connection connection, JDBCTraversing structure, boolean gk) throws SQLException {
        String sql = null;
        List<SQLDataPlaceholder> placeholders = null;

        SQLBuilder sqlBuilder = structure.getSqlBuilder();
        if (sqlBuilder != null) {
            SQLBuilderCombine sqlBuilderCombine = sqlBuilder.toSQLString();
            placeholders = sqlBuilderCombine.getPlaceholders();
            sql = sqlBuilderCombine.getSql();
        } else {
            sql = structure.getSql();
            placeholders = structure.getSqlDataPlaceholders();
        }

        PreparedStatement statement;
        if (gk) {
            statement = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
        } else {
            statement = connection.prepareStatement(sql);
        }

        if (placeholders != null) {
            int i = 0;
            for (SQLDataPlaceholder placeholder : placeholders) {
                i++;
                Object value = this.getFormatValue(placeholder.getValue());
                if (value != null && value.getClass().isEnum()) {
                    statement.setObject(i, String.valueOf(value));
                } else {
                    statement.setObject(i, value);
                }
            }
        }
        return statement;
    }

    private Object getFormatValue(Object value) {
        if (value != null) {
            if (value.getClass() == Date.class) {
                value = new Timestamp(((Date) value).getTime());
            }
            if (Keyword.NULL == value) {
                return null;
            }
        }
        return value;
    }

    private String getStructureSql(JDBCTraversing structure) {
        SQLBuilder sqlBuilder = structure.getSqlBuilder();
        if (sqlBuilder != null) {
            return sqlBuilder.toSQLString().getSql();
        } else {
            return structure.getSql();
        }
    }

    @Override
    public boolean execute(JDBCTraversing structure) throws SQLException {
        logger(structure);
        Connection connection = null;
        PreparedStatement statement = null;
        try {
            // String sql = this.getStructureSql(structure);
            connection = this.getConnection();
            // statement = connection.prepareStatement(sql);
            statement = replacePlaceholder(connection, structure, false);
            return statement.execute();
        } finally {
            this.close(connection, statement);
        }
    }

    @Override
    public int delete(JDBCTraversing structure) throws SQLException {
        return executeUpdateMethod(structure);
    }

    @Override
    public List<Long> insert(JDBCTraversing structure) throws SQLException {
        logger(structure);
        Connection connection = null;
        PreparedStatement statement = null;
        try {
            connection = this.getConnection();
            statement = replacePlaceholder(connection, structure, true);
            statement.executeUpdate();

            PlatformDialect dialect = PlatformFactory.getDialect(sessionContext);

            if (dialect.isSupportGeneratedKeys()) {
                ResultSet rs = statement.getGeneratedKeys();
                List<Long> ids = new ArrayList<Long>();
                while (rs.next()) {
                    long id = rs.getLong(1);
                    ids.add(id);
                }
                return ids;
            } else {
                return null;
            }
        } finally {
            this.close(connection, statement);
        }
    }

    @Override
    public void inserts(BatchPorterStructure structure) throws SQLException {
        logger(structure);
        Connection connection = null;
        PreparedStatement statement = null;

        try {
            connection = this.getConnection();
            SQLBuilder sqlBuilder = structure.getSqlBuilder();
            SQLBuilderCombine sql = sqlBuilder.toSQLString();
            statement = connection.prepareStatement(sql.getSql());
            List<ModelObject> objects = structure.getObjects();

            List<String> fields = structure.getFields();
            if (objects != null && objects.size() > 0) {
                for (int c = 0; c < objects.size(); c++) {
                    ModelObject object = objects.get(c);
                    for (int i = 0; i < fields.size(); i++) {
                        Object value = this.getFormatValue(object.get(fields.get(i)));
                        statement.setObject(i + 1, value);
                    }
                    statement.addBatch();
                    if (c % 500 == 0) {
                        statement.executeBatch();
                    }
                }
            }
            statement.executeBatch();
        } finally {
            this.close(connection, statement);
        }
    }

    @Override
    public List<ModelObject> select(JDBCTraversing structure) throws SQLException {
        logger(structure);
        Connection connection = null;
        PreparedStatement statement = null;
        try {
            connection = this.getConnection();
            statement = replacePlaceholder(connection, structure, false);
            // ResultSet rs = statement.executeQuery();
            boolean success = statement.execute();
            List result = null;
            if (success) {
                ResultSet rs = statement.getResultSet();
                if (rs != null) {
                    ResultSetMetaData rsmd = rs.getMetaData();
                    int fieldCount = rsmd.getColumnCount();
                    result = new ArrayList();
                    while (rs.next()) {
                        ModelObject object = new ModelObject(true);
                        for (int i = 1; i <= fieldCount; i++) {
                            String fieldClassName = rsmd.getColumnClassName(i);
                            //获得查询后的列名称，并非表列名称
                            String fieldName = rsmd.getColumnLabel(i);
                            SQLUtils.recordMappingToMap(fieldClassName, fieldName, rs, object);
                        }
                        if (callback != null) callback.select(connection, statement, rs, object);
                        result.add(object);
                    }
                } else {
                    logger.warn(I18n.print("result_set_empty"));
                }
            }
            return result;
        } finally {
            this.close(connection, statement);
        }
    }

    @Override
    public int update(JDBCTraversing structure) throws SQLException {
        return executeUpdateMethod(structure);
    }

    private int executeUpdateMethod(JDBCTraversing structure) throws SQLException {
        logger(structure);
        Connection connection = null;
        PreparedStatement statement = null;
        try {
            connection = this.getConnection();
            statement = replacePlaceholder(connection, structure, false);
            return statement.executeUpdate();
        } finally {
            this.close(connection, statement);
        }
    }
}
