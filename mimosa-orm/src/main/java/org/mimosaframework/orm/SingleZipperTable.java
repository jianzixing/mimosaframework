package org.mimosaframework.orm;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.mimosaframework.core.json.ModelObject;
import org.mimosaframework.orm.i18n.I18n;
import org.mimosaframework.orm.platform.SQLBuilder;
import org.mimosaframework.orm.platform.SQLBuilderFactory;
import org.mimosaframework.orm.utils.SQLUtils;

import java.io.IOException;
import java.sql.*;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class SingleZipperTable<T> implements ZipperTable<T> {
    private static final Log logger = LogFactory.getLog(SingleZipperTable.class);
    private List<Connection> connections = new ArrayList<>();
    private ContextContainer context;
    private MimosaDataSource ds;
    private String dbTableName;
    private Class c;
    private int fetchSize = Integer.MIN_VALUE;

    public SingleZipperTable(ContextContainer context, Class c, MimosaDataSource ds, String dbTableName) {
        this.c = c;
        this.ds = ds;
        this.context = context;
        this.dbTableName = dbTableName;
    }

    @Override
    public Iterator<T> iterator() {
        try {
            Connection c = ds.getMaster().getConnection();
            connections.add(c);
            return new SingleZipperTableIterator(c);
        } catch (SQLException e) {
            throw new IllegalStateException(I18n.print("get_ds_fail"), e);
        }
    }

    @Override
    public void close() throws IOException {
        for (Connection c : connections) {
            try {
                c.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void setFetchSize(int size) {
        logger.warn(I18n.print("mysql_version"));
        this.fetchSize = size;
    }

    class SingleZipperTableIterator implements Iterator<T> {
        private Connection connection;
        private PreparedStatement statement;
        private SQLBuilder sql = SQLBuilderFactory.createSQLBuilder().SELECT().addString("*").FROM();
        private ResultSet resultSet;
        private int fieldCount;
        private ResultSetMetaData resultSetMetaData;

        public SingleZipperTableIterator(Connection connection) throws SQLException {
            this.connection = connection;
            sql.addString(dbTableName);
            this.statement = this.connection.prepareStatement(sql.toSQLString().getSql(), ResultSet.TYPE_FORWARD_ONLY, ResultSet.CONCUR_READ_ONLY);
            this.statement.setFetchSize(fetchSize);
            this.statement.setFetchDirection(ResultSet.FETCH_REVERSE);
            this.resultSet = this.statement.executeQuery();
            resultSetMetaData = this.resultSet.getMetaData();
            fieldCount = resultSetMetaData.getColumnCount();
        }

        @Override
        public boolean hasNext() {
            try {
                boolean hasNext = this.resultSet.next();
                if (!hasNext) {
                    this.statement.close();
                }
                return hasNext;
            } catch (SQLException e) {
                throw new IllegalStateException(I18n.print("next_fail"), e);
            }
        }

        @Override
        public T next() {
            ModelObject object = new ModelObject(true);
            try {
                for (int i = 1; i <= fieldCount; i++) {
                    String fieldClassName = resultSetMetaData.getColumnClassName(i);
                    //获得查询后的列名称，并非表列名称
                    String fieldName = resultSetMetaData.getColumnLabel(i);
                    SQLUtils.recordMappingToMap(fieldClassName, fieldName, this.resultSet, object);
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
            context.getModelObjectConvertKey().reconvert(c, object);
            return (T) object;
        }

        @Override
        public void remove() {
            throw new IllegalStateException(I18n.print("zipper_not_allow_del"));
        }
    }
}
