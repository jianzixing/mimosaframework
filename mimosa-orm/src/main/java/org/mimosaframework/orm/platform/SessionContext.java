package org.mimosaframework.orm.platform;

import org.mimosaframework.orm.Configuration;
import org.mimosaframework.orm.MimosaDataSource;
import org.mimosaframework.orm.transaction.Transaction;
import org.mimosaframework.orm.utils.DatabaseType;

public class SessionContext {
    private Configuration contextValues;
    private MimosaDataSource dataSource;
    private boolean isMaster = true;
    private String slaveName;
    private Transaction transaction;

    public SessionContext() {
    }

    public SessionContext(Configuration contextValues) {
        this.contextValues = contextValues;
    }

    public String getSlaveName() {
        return slaveName;
    }

    public void setSlaveName(String slaveName) {
        this.slaveName = slaveName;
    }

    public JDBCExecutor getDBChanger() {
        return new DefaultJDBCExecutor(this);
    }

    public DatabaseType getDatabaseTypeEnum() {
        return this.dataSource.getDatabaseTypeEnum();
    }

    public MimosaDataSource getDataSource() {
        return dataSource;
    }

    public void setDataSource(MimosaDataSource dataSource) {
        this.dataSource = dataSource;
    }

    public boolean isMaster() {
        return isMaster;
    }

    public void setMaster(boolean master) {
        isMaster = master;
    }

    public boolean isShowSql() {
        return contextValues != null ? contextValues.isShowSQL() : false;
    }

    public boolean isIgnoreEmptySlave() {
        return contextValues != null ? contextValues.isIgnoreEmptySlave() : true;
    }

    public Transaction getTransaction() {
        return transaction;
    }

    public void setTransaction(Transaction transaction) {
        this.transaction = transaction;
    }

    public SessionContext newSessionContext() {
        SessionContext dataSourceWrapper = new SessionContext();
        dataSourceWrapper.contextValues = contextValues;
        dataSourceWrapper.dataSource = dataSource;
        dataSourceWrapper.isMaster = isMaster;
        dataSourceWrapper.slaveName = slaveName;
        dataSourceWrapper.transaction = transaction;
        return dataSourceWrapper;
    }
}
