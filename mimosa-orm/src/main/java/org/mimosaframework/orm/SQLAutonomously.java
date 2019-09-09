package org.mimosaframework.orm;

import org.mimosaframework.core.utils.AssistUtils;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class SQLAutonomously implements Serializable {
    private List<LinkAutonomously> dataSourceLinks;
    private String sql;
    private boolean isMaster = true;
    private String slaveName;

    public SQLAutonomously() {
    }

    public SQLAutonomously(String sql) {
        this.sql = sql;
    }

    public static SQLAutonomously newInstance(String sql) {
        return new SQLAutonomously(sql);
    }

    public static SQLAutonomously newInstance(String sql, boolean isMaster) {
        return new SQLAutonomously(sql, isMaster);
    }

    public static SQLAutonomously newInstance(String sql, String slaveName) {
        return new SQLAutonomously(sql, slaveName);
    }

    public SQLAutonomously(String sql, boolean isMaster) {
        this.sql = sql;
        this.isMaster = isMaster;
    }

    public SQLAutonomously(String sql, String slaveName) {
        this.sql = sql;
        this.isMaster = true;
        this.slaveName = slaveName;
    }

    public static SQLAutonomously newInstance() {
        return new SQLAutonomously();
    }

    public void add(String sql) {
        if (dataSourceLinks == null) dataSourceLinks = new ArrayList<>();
        dataSourceLinks.add(new LinkAutonomously(sql));
    }

    public void add(String sql, boolean isMaster) {
        if (dataSourceLinks == null) dataSourceLinks = new ArrayList<>();
        dataSourceLinks.add(new LinkAutonomously(sql, isMaster));
    }

    public void add(String dataSourceName, String sql) {
        AssistUtils.notNull(dataSourceName, "要执行的数据源不能为空(多级可以用.表示分层,比如 app.default)");
        if (dataSourceLinks == null) dataSourceLinks = new ArrayList<>();
        dataSourceLinks.add(new LinkAutonomously(dataSourceName, sql));
    }

    public void add(String dataSourceName, String sql, boolean isMaster) {
        AssistUtils.notNull(dataSourceName, "要执行的数据源不能为空(多级可以用.表示分层,比如 app.default)");
        if (dataSourceLinks == null) dataSourceLinks = new ArrayList<>();
        dataSourceLinks.add(new LinkAutonomously(dataSourceName, sql, isMaster));
    }

    public void add(String dataSourceName, String sql, String slaveName) {
        AssistUtils.notNull(dataSourceName, "要执行的数据源不能为空(多级可以用.表示分层,比如 app.default)");
        if (dataSourceLinks == null) dataSourceLinks = new ArrayList<>();
        dataSourceLinks.add(new LinkAutonomously(dataSourceName, sql, slaveName));
    }

    public void add(LinkAutonomously autonomously) {
        if (dataSourceLinks == null) dataSourceLinks = new ArrayList<>();
        dataSourceLinks.add(autonomously);
    }

    public List<LinkAutonomously> getDataSourceLinks() {
        return dataSourceLinks;
    }

    public String getSql() {
        return sql;
    }

    public boolean isMaster() {
        return isMaster;
    }

    public boolean isMaster(String dsname) {
        if (dataSourceLinks != null) {
            for (LinkAutonomously autonomously : this.dataSourceLinks) {
                if (autonomously.getDataSourceName().equals(dsname)) {
                    return autonomously.isMaster();
                }
            }
        }
        return true;
    }

    public String getSlaveName() {
        return slaveName;
    }

    public String getSlaveName(String dsname) {
        if (dataSourceLinks != null) {
            for (LinkAutonomously autonomously : this.dataSourceLinks) {
                if (autonomously.getDataSourceName().equals(dsname)) {
                    return autonomously.getSlaveDataSourceName();
                }
            }
        }
        return null;
    }


    public static LinkAutonomously buildLinkAutonomously() {
        return new LinkAutonomously();
    }

    public static class LinkAutonomously {
        private String dataSourceName = MimosaDataSource.DEFAULT_DS_NAME;
        private boolean master = true;
        private String slaveDataSourceName;
        private String sql;

        public LinkAutonomously() {
        }

        public LinkAutonomously(String sql) {
            this.sql = sql;
        }

        public LinkAutonomously(String sql, boolean master) {
            this.dataSourceName = dataSourceName;
            this.master = master;
            this.sql = sql;
        }


        public LinkAutonomously(String dataSourceName, String sql) {
            this.dataSourceName = dataSourceName;
            this.sql = sql;
        }

        public LinkAutonomously(String dataSourceName, String sql, boolean master) {
            this.dataSourceName = dataSourceName;
            this.master = master;
            this.sql = sql;
        }

        public LinkAutonomously(String dataSourceName, String sql, String slaveDataSourceName) {
            this.dataSourceName = dataSourceName;
            this.slaveDataSourceName = slaveDataSourceName;
            this.sql = sql;
            this.master = false;
        }

        public String getDataSourceName() {
            return dataSourceName;
        }

        public void setDataSourceName(String dataSourceName) {
            this.dataSourceName = dataSourceName;
        }

        public boolean isMaster() {
            return master;
        }

        public void setMaster(boolean master) {
            this.master = master;
        }

        public String getSlaveDataSourceName() {
            return slaveDataSourceName;
        }

        public void setSlaveDataSourceName(String slaveDataSourceName) {
            this.slaveDataSourceName = slaveDataSourceName;
        }

        public String getSql() {
            return sql;
        }

        public void setSql(String sql) {
            this.sql = sql;
        }
    }
}
