package org.mimosaframework.orm.spring;

import org.mimosaframework.orm.MimosaDataSource;

import javax.sql.DataSource;
import java.sql.SQLException;
import java.util.LinkedHashMap;
import java.util.Map;

public class SpringDataSource {
    private String name;
    private DataSource master;
    private Map<String, DataSource> slaves;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public DataSource getMaster() {
        return master;
    }

    public void setMaster(DataSource master) {
        this.master = master;
    }

    public Map<String, DataSource> getSlaves() {
        return slaves;
    }

    public void setSlaves(Map<String, DataSource> slaves) {
        this.slaves = slaves;
    }

    public MimosaDataSource toMimosaDataSource() throws SQLException {
        return new MimosaDataSource(master, new LinkedHashMap<String, DataSource>(slaves), name);
    }

    public MimosaDataSource toMimosaDataSource(String name) throws SQLException {
        return new MimosaDataSource(master, new LinkedHashMap<String, DataSource>(slaves), name);
    }
}
