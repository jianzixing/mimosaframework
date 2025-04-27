package org.mimosaframework.orm;

import org.mimosaframework.core.json.ModelObject;
import org.mimosaframework.core.utils.AssistUtils;
import org.mimosaframework.orm.i18n.I18n;
import org.mimosaframework.orm.sql.UnifyBuilder;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Sql implements Serializable {
    private List<LinkAutonomously> dataSourceLinks;
    private String sql;
    private boolean isMaster = true;
    private String slaveName;
    private ModelObject parameter;
    private Action action;

    public Sql() {
    }

    public Sql(String sql, ModelObject parameter) {
        this.sql = sql;
        this.parameter = parameter;
    }

    public Sql(String sql, ModelObject parameter, String slaveName) {
        this.sql = sql;
        this.slaveName = slaveName;
        this.parameter = parameter;
    }

    public Sql(String sql) {
        this.sql = sql;
    }

    public Sql(String sql, boolean isMaster) {
        this.sql = sql;
        this.isMaster = isMaster;
    }

    public Sql(String sql, String slaveName) {
        this.sql = sql;
        this.isMaster = true;
        this.slaveName = slaveName;
    }

    public static Sql newInstance() {
        return new Sql();
    }

    public static Sql newInstance(String sql) {
        return new Sql(sql);
    }

    public static Sql newInstance(String sql, ModelObject parameter) {
        return new Sql(sql, parameter);
    }

    public static Sql newInstance(String sql, boolean isMaster) {
        return new Sql(sql, isMaster);
    }

    public static Sql newInstance(String sql, String slaveName) {
        return new Sql(sql, slaveName);
    }

    public static Sql build() {
        return new Sql();
    }

    public static Sql build(String sql) {
        return new Sql(sql);
    }

    public static Sql build(String sql, ModelObject parameter) {
        return new Sql(sql, parameter);
    }

    public static Sql build(String sql, boolean isMaster) {
        return new Sql(sql, isMaster);
    }

    public static Sql build(String sql, String slaveName) {
        return new Sql(sql, slaveName);
    }

    // select start

    public static Sql select(String sql) {
        Sql autonomously = new Sql(sql);
        autonomously.setAction(Action.select);
        return autonomously;
    }

    public static Sql select(String sql, ModelObject parameter) {
        Sql autonomously = new Sql(sql);
        autonomously.setAction(Action.select);
        autonomously.setParameter(parameter);
        return autonomously;
    }

    public static Sql select(String sql, boolean isMaster) {
        Sql autonomously = new Sql(sql);
        autonomously.setAction(Action.select);
        autonomously.setMaster(isMaster);
        return autonomously;
    }

    public static Sql select(String sql, String slaveName) {
        Sql autonomously = new Sql(sql);
        autonomously.setAction(Action.select);
        autonomously.setSlaveName(slaveName);
        return autonomously;
    }

    public static Sql select(String sql, ModelObject parameter, boolean isMaster) {
        Sql autonomously = new Sql(sql);
        autonomously.setAction(Action.select);
        autonomously.setMaster(isMaster);
        autonomously.setParameter(parameter);
        return autonomously;
    }

    public static Sql select(String sql, ModelObject parameter, String slaveName) {
        Sql autonomously = new Sql(sql);
        autonomously.setAction(Action.select);
        autonomously.setSlaveName(slaveName);
        autonomously.setParameter(parameter);
        return autonomously;
    }

    // delete start


    public static Sql delete(String sql) {
        Sql autonomously = new Sql(sql);
        autonomously.setAction(Action.delete);
        return autonomously;
    }

    public static Sql delete(String sql, ModelObject parameter) {
        Sql autonomously = new Sql(sql);
        autonomously.setAction(Action.delete);
        autonomously.setParameter(parameter);
        return autonomously;
    }

    public static Sql delete(String sql, boolean isMaster) {
        Sql autonomously = new Sql(sql);
        autonomously.setAction(Action.delete);
        autonomously.setMaster(isMaster);
        return autonomously;
    }

    public static Sql delete(String sql, String slaveName) {
        Sql autonomously = new Sql(sql);
        autonomously.setAction(Action.delete);
        autonomously.setSlaveName(slaveName);
        return autonomously;
    }

    public static Sql delete(String sql, ModelObject parameter, boolean isMaster) {
        Sql autonomously = new Sql(sql);
        autonomously.setAction(Action.delete);
        autonomously.setMaster(isMaster);
        autonomously.setParameter(parameter);
        return autonomously;
    }

    public static Sql delete(String sql, ModelObject parameter, String slaveName) {
        Sql autonomously = new Sql(sql);
        autonomously.setAction(Action.delete);
        autonomously.setSlaveName(slaveName);
        autonomously.setParameter(parameter);
        return autonomously;
    }

    // update start


    public static Sql update(String sql) {
        Sql autonomously = new Sql(sql);
        autonomously.setAction(Action.update);
        return autonomously;
    }

    public static Sql update(String sql, ModelObject parameter) {
        Sql autonomously = new Sql(sql);
        autonomously.setAction(Action.update);
        autonomously.setParameter(parameter);
        return autonomously;
    }

    public static Sql update(String sql, boolean isMaster) {
        Sql autonomously = new Sql(sql);
        autonomously.setAction(Action.update);
        autonomously.setMaster(isMaster);
        return autonomously;
    }

    public static Sql update(String sql, String slaveName) {
        Sql autonomously = new Sql(sql);
        autonomously.setAction(Action.update);
        autonomously.setSlaveName(slaveName);
        return autonomously;
    }

    public static Sql update(String sql, ModelObject parameter, boolean isMaster) {
        Sql autonomously = new Sql(sql);
        autonomously.setAction(Action.update);
        autonomously.setMaster(isMaster);
        autonomously.setParameter(parameter);
        return autonomously;
    }

    public static Sql update(String sql, ModelObject parameter, String slaveName) {
        Sql autonomously = new Sql(sql);
        autonomously.setAction(Action.update);
        autonomously.setSlaveName(slaveName);
        autonomously.setParameter(parameter);
        return autonomously;
    }

    // insert start


    public static Sql insert(String sql) {
        Sql autonomously = new Sql(sql);
        autonomously.setAction(Action.insert);
        return autonomously;
    }

    public static Sql insert(String sql, ModelObject parameter) {
        Sql autonomously = new Sql(sql);
        autonomously.setAction(Action.insert);
        autonomously.setParameter(parameter);
        return autonomously;
    }

    public static Sql insert(String sql, boolean isMaster) {
        Sql autonomously = new Sql(sql);
        autonomously.setAction(Action.insert);
        autonomously.setMaster(isMaster);
        return autonomously;
    }

    public static Sql insert(String sql, String slaveName) {
        Sql autonomously = new Sql(sql);
        autonomously.setAction(Action.insert);
        autonomously.setSlaveName(slaveName);
        return autonomously;
    }

    public static Sql insert(String sql, ModelObject parameter, boolean isMaster) {
        Sql autonomously = new Sql(sql);
        autonomously.setAction(Action.insert);
        autonomously.setMaster(isMaster);
        autonomously.setParameter(parameter);
        return autonomously;
    }

    public static Sql insert(String sql, ModelObject parameter, String slaveName) {
        Sql autonomously = new Sql(sql);
        autonomously.setAction(Action.insert);
        autonomously.setSlaveName(slaveName);
        autonomously.setParameter(parameter);
        return autonomously;
    }

    // sql start


    public static Sql sql(String sql) {
        Sql autonomously = new Sql(sql);
        autonomously.setAction(Action.sql);
        return autonomously;
    }

    public static Sql sql(String sql, ModelObject parameter) {
        Sql autonomously = new Sql(sql);
        autonomously.setAction(Action.sql);
        autonomously.setParameter(parameter);
        return autonomously;
    }

    public static Sql sql(String sql, boolean isMaster) {
        Sql autonomously = new Sql(sql);
        autonomously.setAction(Action.sql);
        autonomously.setMaster(isMaster);
        return autonomously;
    }

    public static Sql sql(String sql, String slaveName) {
        Sql autonomously = new Sql(sql);
        autonomously.setAction(Action.sql);
        autonomously.setSlaveName(slaveName);
        return autonomously;
    }

    public static Sql sql(String sql, ModelObject parameter, boolean isMaster) {
        Sql autonomously = new Sql(sql);
        autonomously.setAction(Action.sql);
        autonomously.setMaster(isMaster);
        autonomously.setParameter(parameter);
        return autonomously;
    }

    public static Sql sql(String sql, ModelObject parameter, String slaveName) {
        Sql autonomously = new Sql(sql);
        autonomously.setAction(Action.sql);
        autonomously.setSlaveName(slaveName);
        autonomously.setParameter(parameter);
        return autonomously;
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
        AssistUtils.isNull(dataSourceName, I18n.print("not_empty"));
        if (dataSourceLinks == null) dataSourceLinks = new ArrayList<>();
        dataSourceLinks.add(new LinkAutonomously(dataSourceName, sql));
    }

    public void add(String dataSourceName, String sql, boolean isMaster) {
        AssistUtils.isNull(dataSourceName, I18n.print("not_empty"));
        if (dataSourceLinks == null) dataSourceLinks = new ArrayList<>();
        dataSourceLinks.add(new LinkAutonomously(dataSourceName, sql, isMaster));
    }

    public void add(String dataSourceName, String sql, String slaveName) {
        AssistUtils.isNull(dataSourceName, I18n.print("not_empty"));
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

    public void setMaster(boolean master) {
        isMaster = master;
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

    public void setSlaveName(String slaveName) {
        this.slaveName = slaveName;
    }

    public ModelObject getParameter() {
        return parameter;
    }

    public void setParameter(ModelObject parameter) {
        this.parameter = parameter;
    }

    public Action getAction() {
        return action;
    }

    public void setAction(Action action) {
        this.action = action;
    }

    public static LinkAutonomously buildLinkAutonomously() {
        return new LinkAutonomously();
    }

    /**
     * 不同数据源执行不同的SQL语句
     */
    public static class LinkAutonomously {
        private String dataSourceName = MimosaDataSource.DEFAULT_DS_NAME;
        private boolean master = true;
        private String slaveDataSourceName;
        private String sql;
        private UnifyBuilder builder;

        public LinkAutonomously() {
        }

        public LinkAutonomously(String sql) {
            this.sql = sql;
        }

        public LinkAutonomously(String sql, boolean master) {
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

        public LinkAutonomously(UnifyBuilder builder) {
            this.builder = builder;
        }

        public LinkAutonomously(UnifyBuilder builder, boolean master) {
            this.master = master;
            this.builder = builder;
        }


        public LinkAutonomously(String dataSourceName, UnifyBuilder builder) {
            this.dataSourceName = dataSourceName;
            this.builder = builder;
        }

        public LinkAutonomously(String dataSourceName, UnifyBuilder builder, boolean master) {
            this.dataSourceName = dataSourceName;
            this.master = master;
            this.builder = builder;
        }

        public LinkAutonomously(String dataSourceName, UnifyBuilder builder, String slaveDataSourceName) {
            this.dataSourceName = dataSourceName;
            this.slaveDataSourceName = slaveDataSourceName;
            this.builder = builder;
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

        public UnifyBuilder getBuilder() {
            return builder;
        }
    }

    public enum Action {
        select, update, insert, sql, delete
    }
}
