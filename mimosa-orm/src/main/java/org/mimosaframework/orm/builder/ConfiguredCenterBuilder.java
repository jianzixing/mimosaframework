package org.mimosaframework.orm.builder;

import org.mimosaframework.core.json.ModelArray;
import org.mimosaframework.core.json.ModelObject;
import org.mimosaframework.orm.IDStrategy;
import org.mimosaframework.orm.MimosaDataSource;
import org.mimosaframework.orm.exception.ContextException;
import org.mimosaframework.orm.external.ConfiguredCenterInterface;
import org.mimosaframework.orm.platform.SessionContext;

import javax.sql.DataSource;
import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.sql.SQLException;
import java.util.*;

public class ConfiguredCenterBuilder extends AbstractConfigBuilder {
    private Map<String, DataSource> dataSources = new HashMap<String, DataSource>();
    private Map<String, MimosaDataSource> wrappers = new HashMap<String, MimosaDataSource>();
    public static final String SERVER_NAME = "configured-center";
    private String host;
    private int port = 9920;
    private String app;
    private ConfiguredCenterInterface center;

    private String mappingClassPackage;
    private Set<String> additionMappingClass;
    private boolean isShowSQL = false;
    private boolean isTransactionManagedByMimosa = false;
    private boolean clusterEnable = false;
    private MimosaDataSource mimosaDataSource;

    public ConfiguredCenterBuilder() {
    }

    public ConfiguredCenterBuilder(String app, String host, int port) throws ContextException {
        this.host = host;
        this.app = app;
        this.port = port;

        this.connection();
    }

    private void connection() throws ContextException {
        try {
            center = (ConfiguredCenterInterface) Naming.lookup("rmi://" + host + ":" + port + "/" + SERVER_NAME);
        } catch (NotBoundException e) {
            throw new ContextException("初始化配置中心出错", e);
        } catch (MalformedURLException e) {
            throw new ContextException("初始化配置中心出错", e);
        } catch (RemoteException e) {
            throw new ContextException("初始化配置中心出错", e);
        }
    }


    @Override
    protected String getMappingClassPackage() {
        return this.mappingClassPackage;
    }

    @Override
    protected Set<String> getAdditionMappingClass() {
        return this.additionMappingClass;
    }

    private void parseGlobalDatabase() throws ContextException {
        SessionContext wrapper = new SessionContext();
        wrapper.setDataSource(this.mimosaDataSource);
        // this.checkDBMapping(wrapper);
    }

    private MimosaDataSource getDataSourceFromString(String json) throws ContextException {
        ModelObject map = ModelObject.parseObject(json);
        return this.getDataSourceFromModel(map);
    }

    private MimosaDataSource getDataSourceFromModel(ModelObject map) throws ContextException {
        String name = map.getString("name");
        ModelObject master = map.getModelObject("master");
        ModelArray slaves = map.getModelArray("slaves");
        MimosaDataSource mimosaDataSource = null;
        if (master != null) {
            DataSource ds = super.getDataSourceFromProperties(master);

            Map<String, DataSource> slaveList = new LinkedHashMap<>(slaves.size());
            for (int i = 0; i < slaves.size(); i++) {
                ModelObject slave = slaves.getModelObject(i);
                DataSource sds = super.getDataSourceFromProperties(slave);
                // slaveList.put(sds);
            }

            try {
                mimosaDataSource = new MimosaDataSource(ds, slaveList, name);
            } catch (SQLException e) {
                throw new ContextException("初始化DataSource出错", e);
            }
        }
        return mimosaDataSource;
    }

    @Override
    public ApplicationSetting getApplication() {
        return null;
    }

    @Override
    public CenterConfigSetting getCenterInfo() throws ContextException {
        return null;
    }

    @Override
    public List<MimosaDataSource> getDataSources() {
        return null;
    }

    @Override
    public Set<Class> getResolvers() {
        return null;
    }

    @Override
    public List<? extends IDStrategy> getStrategies() {
        return null;
    }

    @Override
    public BasicSetting getBasicInfo() {
        return null;
    }

    @Override
    public List<String> getMappers() {
        return null;
    }
}
