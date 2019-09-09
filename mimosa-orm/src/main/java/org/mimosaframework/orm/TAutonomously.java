package org.mimosaframework.orm;

import org.mimosaframework.core.json.ModelObject;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class TAutonomously implements Serializable {
    private List<MapperAutonomously> dataSourceMappers;
    /**
     * SQL文件的名称,以.作为分隔
     * 比如User.getUser表示User.xml中名称为getUser的SQL语句
     */
    private String name;
    private ModelObject parameter;
    private boolean isMaster = true;
    private String slaveName;

    public static TAutonomously newInstance() {
        return new TAutonomously();
    }

    public static TAutonomously newInstance(String name) {
        return new TAutonomously(name);
    }

    public static TAutonomously newInstance(String name, ModelObject parameter) {
        return new TAutonomously(name, parameter);
    }

    public TAutonomously() {
    }

    public TAutonomously(String name) {
        this.name = name;
    }

    public TAutonomously(String name, ModelObject parameter) {
        this.name = name;
        this.parameter = parameter;
    }

    public void add(String name) {
        if (this.dataSourceMappers == null) this.dataSourceMappers = new ArrayList<>();
        this.dataSourceMappers.add(new MapperAutonomously(name));
    }

    public void add(String name, ModelObject parameter) {
        if (this.dataSourceMappers == null) this.dataSourceMappers = new ArrayList<>();
        this.dataSourceMappers.add(new MapperAutonomously(name, parameter));
    }

    public void add(String name, ModelObject parameter, String dataSourceName) {
        if (this.dataSourceMappers == null) this.dataSourceMappers = new ArrayList<>();
        this.dataSourceMappers.add(new MapperAutonomously(name, parameter, dataSourceName));
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public ModelObject getParameter() {
        return parameter;
    }

    public void setParameter(ModelObject parameter) {
        this.parameter = parameter;
    }

    public boolean isMaster() {
        return isMaster;
    }

    public void setMaster(boolean master) {
        isMaster = master;
    }

    public String getSlaveName() {
        return slaveName;
    }

    public void setSlaveName(String slaveName) {
        this.slaveName = slaveName;
    }

    public List<MapperAutonomously> getDataSourceMappers() {
        return dataSourceMappers;
    }

    public static MapperAutonomously buildMapperAutonomously() {
        return new MapperAutonomously();
    }

    public static class MapperAutonomously {
        private String dataSourceName = MimosaDataSource.DEFAULT_DS_NAME;
        private String name;
        private ModelObject parameter;
        private boolean master = true;
        private String slaveDataSourceName;

        public MapperAutonomously() {
        }

        public MapperAutonomously(String name) {
            this.name = name;
        }

        public MapperAutonomously(String name, ModelObject parameter) {
            this.name = name;
            this.parameter = parameter;
        }

        public MapperAutonomously(String name, ModelObject parameter, String dataSourceName) {
            this.dataSourceName = dataSourceName;
            this.name = name;
            this.parameter = parameter;
        }

        public String getDataSourceName() {
            return dataSourceName;
        }

        public void setDataSourceName(String dataSourceName) {
            this.dataSourceName = dataSourceName;
        }

        public ModelObject getParameter() {
            return parameter;
        }

        public void setParameter(ModelObject parameter) {
            this.parameter = parameter;
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

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }
    }
}
