package org.mimosaframework.orm.builder;

import org.mimosaframework.core.json.ModelObject;
import org.mimosaframework.core.utils.i18n.Messages;
import org.mimosaframework.core.utils.StringTools;
import org.mimosaframework.orm.*;
import org.mimosaframework.orm.auxiliary.FactoryBuilder;
import org.mimosaframework.orm.auxiliary.FactoryBuilderConfig;
import org.mimosaframework.orm.convert.NamingConvert;
import org.mimosaframework.orm.exception.ContextException;
import org.mimosaframework.orm.i18n.I18n;
import org.w3c.dom.*;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import javax.sql.DataSource;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.sql.SQLException;
import java.util.*;

public class XmlConfigBuilder extends AbstractConfigBuilder {
    protected DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance();
    protected static final String DEFAULT_ROOT = "mimosa";
    protected DocumentBuilder db = null;
    protected Document document = null;
    protected NodeList root = null;
    protected ApplicationSetting applicationInfo = null;
    protected BasicSetting basicInfo = new BasicSetting();
    protected CenterConfigSetting configCenterInfo = null;

    protected Set<Class> resolvers;
    protected List<IDStrategy> strategies;

    protected Map<String, DataSource> dataSources = new HashMap<String, DataSource>();
    protected Map<String, MimosaDataSource> wrappers = new HashMap<String, MimosaDataSource>();
    protected InputSource inputSource;

    protected String mappingClassPackage;
    protected Set<String> additionMappingClass;
    protected MimosaDataSource mimosaDataSource;
    protected boolean isShowSQL = false;
    protected boolean isInitBasic = false;
    protected String mappingLevel;

    public XmlConfigBuilder(InputStream inputStream) throws ContextException {
        this(inputStream != null ? new InputSource(inputStream) : null);
    }

    public XmlConfigBuilder(String uri) throws ContextException {
        this(StringTools.isEmpty(uri) ? null : new InputSource(uri));
    }

    public XmlConfigBuilder(File file) throws ContextException {
        this(file != null ? new InputSource(file.toURI().toASCIIString()) : null);
    }

    private XmlConfigBuilder(InputSource inputSource) throws ContextException {
        this.inputSource = inputSource;
        if (inputSource == null) {
            throw new ContextException(I18n.print("not_found_xml"));
        }
        try {
            this.parseXml();
        } catch (ParserConfigurationException e) {
            throw new ContextException(I18n.print("parse_xml_error"), e);
        } catch (IOException e) {
            throw new ContextException(I18n.print("parse_xml_error"), e);
        } catch (SAXException e) {
            throw new ContextException(I18n.print("parse_xml_error"), e);
        }
    }

    protected void parseXml() throws ParserConfigurationException, IOException, SAXException, ContextException {
        db = documentBuilderFactory.newDocumentBuilder();
        document = db.parse(inputSource);
        root = document.getElementsByTagName(DEFAULT_ROOT);
        for (int i = 0; i < root.getLength(); i++) {
            Node mimosaNode = root.item(i);
            NodeList levelOneList = mimosaNode.getChildNodes();

            // 获得当前应用名称
            NamedNodeMap namedNodeMap = mimosaNode.getAttributes();
            if (namedNodeMap == null) {
                throw new ContextException(I18n.print("must_app_name", DEFAULT_ROOT));
            }
            String appName = namedNodeMap.getNamedItem("name").getNodeValue();
            String appDesc = namedNodeMap.getNamedItem("description").getNodeValue();
            if (StringTools.isEmpty(appName)) {
                throw new ContextException(I18n.print("must_app_name", DEFAULT_ROOT));
            }
            applicationInfo = new ApplicationSetting(appName, appDesc);


            for (int j = 0; j < levelOneList.getLength(); j++) {
                Node node = levelOneList.item(j);

                if (node.getNodeName().equalsIgnoreCase("format")) {
                    NamedNodeMap attributes = node.getAttributes();
                    if (attributes.getNamedItem("showSql") != null) {
                        String showSqlString = attributes.getNamedItem("showSql").getNodeValue().trim();
                        if (showSqlString.equalsIgnoreCase("true")
                                || showSqlString.equalsIgnoreCase("yes")
                                || showSqlString.equalsIgnoreCase("1")) {
                            basicInfo.setShowSQL(true);
                            isShowSQL = true;
                        }
                    }
                }

                // 先解析所有的数据库链接配置
                if (node.getNodeName().equalsIgnoreCase("dslist")) {
                    NodeList dslist = node.getChildNodes();
                    Map<String, Map<String, String>> array = new LinkedHashMap<>();
                    for (int l = 0; l < dslist.getLength(); l++) {
                        Node ds = dslist.item(l);
                        NamedNodeMap nm = ds.getAttributes();
                        if (nm != null) {
                            if (nm.getNamedItem("name") == null) {
                                throw new ContextException(I18n.print("ds_must_name"));
                            }
                            Map<String, String> map = this.getNodeByProperties(ds);
                            if (map.size() > 0) {
                                array.put(nm.getNamedItem("name").getNodeValue().trim(), map);
                            }
                        }
                    }

                    // 初始化并且加入全局
                    this.initXmlDataSources(array);
                }
            }
        }


        this.parseXmlWrappers();

        for (int i = 0; i < root.getLength(); i++) {
            Node node = root.item(i);
            NodeList levelOneList = node.getChildNodes();

            // 后初始化,防止配置文件依赖报错
            for (int j = 0; j < levelOneList.getLength(); j++) {
                Node nodeOne = levelOneList.item(j);
                // 在解析全局数据库链接配置(不管是单机还是集群只要没有单独配置链接就使用这个)
                if (nodeOne.getNodeName().equalsIgnoreCase("datasource")) {
                    mimosaDataSource = this.parseXmlDataSourceNode(nodeOne);
                }

                if (nodeOne.getNodeName().equalsIgnoreCase("strategies")) {
                    this.parseXmlStrategy(nodeOne);
                }
            }
        }
    }

    protected void parseXmlWrappers() throws ContextException {
        for (int i = 0; i < root.getLength(); i++) {
            Node node = root.item(i);
            NodeList levelOneList = node.getChildNodes();
            // 后初始化,防止配置文件依赖报错
            for (int j = 0; j < levelOneList.getLength(); j++) {
                Node nodeOne = levelOneList.item(j);
                // 在解析全局数据库链接配置(不管是单机还是集群只要没有单独配置链接就使用这个)
                if (nodeOne.getNodeName().equalsIgnoreCase("wrappers")) {
                    NodeList ns = nodeOne.getChildNodes();
                    if (ns != null) {
                        for (int s = 0; s < ns.getLength(); s++) {
                            Node item = ns.item(s);
                            NamedNodeMap nm = item.getAttributes();
                            if (nm != null) {
                                String name = nm.getNamedItem("name") != null ? nm.getNamedItem("name").getNodeValue().trim() : null;
                                String master = nm.getNamedItem("master") != null ? nm.getNamedItem("master").getNodeValue().trim() : null;
                                String slaves = nm.getNamedItem("slaves") != null ? nm.getNamedItem("slaves").getNodeValue().trim() : null;
                                if (name == null) {
                                    throw new ContextException(I18n.print("wrapper_must_name"));
                                }
                                if (StringTools.isNotEmpty(master)) {

                                    DataSource ds = dataSources.get(master);
                                    if (ds == null) {
                                        throw new ContextException(I18n.print("not_fount_master", master));
                                    }

                                    Map<String, DataSource> slaveList = new LinkedHashMap<>();
                                    if (StringTools.isNotEmpty(slaves)) {
                                        String[] strings = slaves.split(",");
                                        for (String sl : strings) {
                                            String[] nv = sl.split(":");
                                            if (nv.length == 1) {
                                                slaveList.put(nv[0], dataSources.get(nv[0]));
                                            } else {
                                                slaveList.put(nv[0], dataSources.get(nv[1]));
                                            }
                                        }
                                    }
                                    try {
                                        MimosaDataSource selfMDS = new MimosaDataSource(ds, slaveList, name);
                                        wrappers.put(name, selfMDS);
                                    } catch (SQLException e) {
                                        throw new ContextException(I18n.print("ds_type_fail"), e);
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    protected void parseXmlStrategy(Node nodeOne) throws ContextException {
        NodeList list = nodeOne.getChildNodes();
        for (int i = 0; i < list.getLength(); i++) {
            Node node = list.item(i);
            if (node.getNodeName().equalsIgnoreCase("strategy")) {
                NamedNodeMap map = node.getAttributes();
                Node clazz = map.getNamedItem("class");

                if (clazz == null) {
                    throw new IllegalArgumentException(I18n.print("strategy_class_must_be"));
                }

                try {
                    if (strategies == null) {
                        strategies = new ArrayList<>();
                    }
                    Class c = Class.forName(clazz.getNodeValue().trim());

                    if (IDStrategy.class.isAssignableFrom(c)) {
                        Class<IDStrategy> idStrategy = c;
                        Map properties = this.getNodeByProperties(node);
                        if (properties != null && properties.size() > 0) {
                            strategies.add(ModelObject.toJavaObject(new ModelObject(properties), idStrategy));
                        } else {
                            strategies.add(idStrategy.newInstance());
                        }
                    } else {
                        throw new IllegalArgumentException(I18n.print("strategy_must_ext_id"));
                    }
                } catch (ClassNotFoundException e) {
                    throw new IllegalArgumentException(I18n.print("strategy_impl_must"), e);
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                } catch (InstantiationException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    protected Map<String, String> getNodeByProperties(Node ds) {
        NodeList properties = ds.getChildNodes();
        Map<String, String> map = new LinkedHashMap<String, String>();
        for (int p = 0; p < properties.getLength(); p++) {
            Node pitem = properties.item(p);
            NamedNodeMap nm = pitem.getAttributes();
            if (nm != null) {
                String nodeName = pitem.getNodeName();
                if ("property".equals(nodeName)) {
                    String name = nm.getNamedItem("name") != null ? nm.getNamedItem("name").getNodeValue() : null;
                    String value = nm.getNamedItem("value") != null ? nm.getNamedItem("value").getNodeValue() : null;
                    if (StringTools.isEmpty(value)) {
                        value = pitem.getTextContent();
                    }
                    map.put(name.trim(), value.trim());
                }
            }
        }
        return map;
    }

    protected void initXmlDataSources(Map<String, Map<String, String>> array) throws ContextException {
        if (array.size() > 0) {
            for (Map.Entry<String, Map<String, String>> map : array.entrySet()) {
                DataSource ds = this.getDataSourceFromProperties(map.getValue());
                if (ds != null) {
                    this.dataSources.put(map.getKey(), ds);
                }
            }
        }
    }

    protected DataSource getDataSourceFromProperties(Node node, StringHolder holder) throws ContextException {
        Map<String, String> map = this.getNodeByProperties(node);
        if (holder != null) holder.setName(map.get("name"));
        return this.getDataSourceFromProperties(map);
    }

    protected String parseXmlDataSourceName(Node node) throws ContextException {
        NamedNodeMap attribute = node.getAttributes();
        if (attribute != null) {
            Node wrapper = attribute.getNamedItem("wrapper");
            if (wrapper != null) {
                return wrapper.getNodeValue().trim();
            }
        }
        // 先找子元素如果当前元素不是datasource的话
        if (!node.getNodeName().equalsIgnoreCase("datasource")) {
            NodeList list = node.getChildNodes();
            if (list != null) {
                for (int i = 0; i < list.getLength(); i++) {
                    if (list.item(i).getNodeName().equalsIgnoreCase("datasource")) {
                        node = list.item(i);
                    }
                }
            }
        }

        if (node.getNodeName().equalsIgnoreCase("datasource")) {
            NamedNodeMap nm = node.getAttributes();

            if (nm != null && nm.getNamedItem("wrapper") != null) {
                String wrapperName = nm.getNamedItem("wrapper").getNodeValue().trim();
                return wrapperName;
            }
        }
        return null;
    }

    protected MimosaDataSource parseXmlDataSourceNode(Node node) throws ContextException {

        // 先找子元素如果当前元素不是datasource的话
        if (!node.getNodeName().equalsIgnoreCase("datasource")) {
            NodeList list = node.getChildNodes();
            if (list != null) {
                for (int i = 0; i < list.getLength(); i++) {
                    if (list.item(i).getNodeName().equalsIgnoreCase("datasource")) {
                        node = list.item(i);
                    }
                }
            }
        }

        if (node.getNodeName().equalsIgnoreCase("datasource")) {
            MimosaDataSource mimosaDataSource = null;


            NamedNodeMap nm = node.getAttributes();
            if (nm != null && nm.getNamedItem("wrapper") != null) {
                String wrapperName = nm.getNamedItem("wrapper").getNodeValue().trim();
                MimosaDataSource ds = this.wrappers.get(wrapperName);
                if (ds == null) {
                    throw new ContextException(I18n.print("not_fount_wrapper_name"));
                }
                return ds;
            }

            DataSource dataSource = null;

            if (nm != null && nm.getNamedItem("master") != null) {
                String master = nm.getNamedItem("master").getNodeValue().trim();
                if (StringTools.isNotEmpty(master)) {
                    DataSource ds = this.dataSources.get(master);
                    if (ds == null) {
                        throw new ContextException(I18n.print("not_fount_data_source", master));
                    }
                    dataSource = ds;
                }
            } else {
                NodeList child = node.getChildNodes();
                for (int i = 0; i < child.getLength(); i++) {
                    Node masterNode = child.item(i);
                    NamedNodeMap masterNodeMap = masterNode.getAttributes();
                    if (masterNode.getNodeName().equalsIgnoreCase("master")) {
                        if (masterNodeMap != null && masterNodeMap.getNamedItem("ds") != null) {
                            String master = masterNodeMap.getNamedItem("ds").getNodeValue().trim();
                            DataSource ds = this.dataSources.get(master);
                            if (ds == null) {
                                throw new ContextException(I18n.print("not_fount_data_source", master));
                            }
                            dataSource = ds;
                        } else {
                            DataSource ds = this.getDataSourceFromProperties(child.item(i), null);
                            if (ds == null) {
                                throw new ContextException(I18n.print("init_data_source_fail"));
                            } else {
                                dataSource = ds;
                            }
                        }
                    }
                }
            }


            Map<String, DataSource> slaveList = new LinkedHashMap<>();
            if (nm != null && nm.getNamedItem("slaves") != null) {
                String slaves = nm.getNamedItem("slaves").getNodeValue().trim();
                if (StringTools.isNotEmpty(slaves)) {
                    String[] strings = slaves.split(",");
                    for (String s : strings) {
                        if (StringTools.isNotEmpty(s)) {
                            String[] nv = s.split(":");
                            if (nv.length == 1) {
                                DataSource ds = this.dataSources.get(nv[0]);
                                if (ds == null) {
                                    throw new ContextException(I18n.print("not_fount_slave", s));
                                }
                                slaveList.put("default", ds);
                            } else {
                                DataSource ds = this.dataSources.get(nv[1]);
                                if (ds == null) {
                                    throw new ContextException(I18n.print("not_fount_slave", s));
                                }
                                slaveList.put(nv[0], ds);
                            }
                        }
                    }
                }
            } else {
                NodeList child = node.getChildNodes();
                for (int i = 0; i < child.getLength(); i++) {
                    Node slaveNode = child.item(i);
                    NamedNodeMap slaveNodeMap = slaveNode.getAttributes();
                    if (slaveNode.getNodeName().equalsIgnoreCase("slave")) {
                        if (slaveNodeMap != null && slaveNodeMap.getNamedItem("ds") != null) {
                            String stringSlaves = slaveNodeMap.getNamedItem("ds").getNodeValue().trim();
                            String[] strings = stringSlaves.split(",");
                            for (String s : strings) {
                                if (StringTools.isNotEmpty(s)) {
                                    String[] nv = s.split(":");
                                    if (nv.length == 1) {
                                        DataSource ds = this.dataSources.get(nv[0]);
                                        if (ds == null) {
                                            throw new ContextException(I18n.print("not_fount_slave", s));
                                        }
                                        slaveList.put("default", ds);
                                    } else {
                                        DataSource ds = this.dataSources.get(nv[1]);
                                        if (ds == null) {
                                            throw new ContextException(I18n.print("not_fount_slave", s));
                                        }
                                        slaveList.put(nv[0], ds);
                                    }
                                }
                            }
                        } else {
                            StringHolder holder = new StringHolder();
                            DataSource ds = this.getDataSourceFromProperties(child.item(i), holder);
                            if (ds == null) {
                                throw new ContextException(I18n.print("init_data_source_fail"));
                            } else {
                                if (StringTools.isNotEmpty(holder.getName())) {
                                    slaveList.put(holder.getName(), ds);
                                } else {
                                    slaveList.put("default", ds);
                                }
                            }
                        }
                    }
                }
            }

            // 加入datasource标签以上情况都不存在则直接判断是否本身是数据连接配置
            if (dataSource == null) {
                Map<String, String> map = this.getNodeByProperties(node);
                dataSource = this.getDataSourceFromProperties(map);
            }

            try {
                mimosaDataSource = new MimosaDataSource(dataSource, slaveList, "default");
                mimosaDataSource.getMaster();
                wrappers.put(mimosaDataSource.getName(), mimosaDataSource);
            } catch (SQLException e) {
                throw new ContextException(I18n.print("ds_type_fail"), e);
            }

            return mimosaDataSource;
        }
        return null;
    }

    /**
     * 获取某个一个xml节点配置，首先判断属性是否有name值
     * 如果没有就判断子节点是否是文本节点如果是文本节点就是值
     * 如果子节点只有value标签那value标签就是值
     *
     * @param node
     * @param name
     * @return
     */
    protected String getXmlNodeAny(Node node, String name) {
        NamedNodeMap attr = node.getAttributes();
        if (attr != null && attr.getNamedItem(name) != null) {
            return attr.getNamedItem(name).getNodeValue().trim();
        } else {
            NodeList list = node.getChildNodes();
            for (int i = 0; i < list.getLength(); i++) {
                Node c = list.item(i);
                String value = null;
                if (c.getNodeType() == Node.TEXT_NODE) {
                    value = c.getTextContent().trim();
                }
                if (StringTools.isNotEmpty(value)) {
                    return value;
                }
                if (c.getNodeName().equalsIgnoreCase(name)) {
                    NamedNodeMap ca = c.getAttributes();
                    if (ca != null && ca.getNamedItem("value") != null) {
                        return ca.getNamedItem("value").getNodeValue().trim();
                    } else {
                        return c.getTextContent().trim();
                    }
                }
            }
        }
        return null;
    }

    @Override
    protected String getMappingClassPackage() {
        return mappingClassPackage;
    }

    @Override
    protected Set<String> getAdditionMappingClass() {
        return additionMappingClass;
    }

    @Override
    public ApplicationSetting getApplication() {
        return applicationInfo;
    }

    @Override
    public CenterConfigSetting getCenterInfo() throws ContextException {
        if (this.configCenterInfo != null) {
            return this.configCenterInfo;
        }
        for (int i = 0; i < root.getLength(); i++) {
            Node node = root.item(i);
            NodeList levelOneList = node.getChildNodes();

            configCenterInfo = new CenterConfigSetting();
            for (int j = 0; j < levelOneList.getLength(); j++) {
                Node item = levelOneList.item(j);
                if (item.getNodeName().equalsIgnoreCase("center")) {
                    String centerHost = this.getXmlNodeAny(item, "server");
                    String centerPort = this.getXmlNodeAny(item, "port");
                    String centerClientName = this.getXmlNodeAny(item, "clientName");

                    if (StringTools.isEmpty(centerHost)
                            || StringTools.isEmpty(centerPort)
                            || StringTools.isEmpty(centerClientName)) {
                        throw new ContextException(I18n.print("must_center_info"));
                    }

                    if (!StringTools.isNumber(centerPort)) {
                        throw new ContextException(I18n.print("must_center_info_port"));
                    }

                    configCenterInfo.setCenterHost(centerHost);
                    configCenterInfo.setCenterPort(Integer.parseInt(centerPort));
                    configCenterInfo.setClientName(centerClientName);
                }

            }
        }
        return configCenterInfo;
    }

    @Override
    public List<MimosaDataSource> getDataSources() {
        List<MimosaDataSource> dataSources = new ArrayList<>();
        Iterator<Map.Entry<String, MimosaDataSource>> iterator = this.wrappers.entrySet().iterator();
        while (iterator.hasNext()) {
            dataSources.add(iterator.next().getValue());
        }
        return dataSources;
    }

    @Override
    public Set<Class> getResolvers() throws ContextException {
        if (this.resolvers != null) {
            return this.resolvers;
        }
        for (int i = 0; i < root.getLength(); i++) {
            Node mimosaNode = root.item(i);
            NodeList levelOneList = mimosaNode.getChildNodes();
            for (int j = 0; j < levelOneList.getLength(); j++) {
                Node node = levelOneList.item(j);
                if (node.getNodeName().equalsIgnoreCase("mapping")) {
                    NamedNodeMap attributes = node.getAttributes();
                    Node attrScan = attributes.getNamedItem("scan");
                    Node levelNode = attributes.getNamedItem("level");
                    if (levelNode != null) {
                        mappingLevel = levelNode.getNodeValue();
                    }
                    String scan = null;
                    if (attrScan != null) {
                        scan = attrScan.getNodeValue();
                    }

                    NodeList values = node.getChildNodes();
                    Set<String> cls = new LinkedHashSet<String>();
                    if (values != null && values.getLength() > 0) {
                        for (int k = 0; k < values.getLength(); k++) {
                            Node item = values.item(k);
                            if (item.getNodeName().equalsIgnoreCase("value")) {
                                cls.add(item.getTextContent().trim());
                            }
                        }
                    }

                    if (StringTools.isNotEmpty(scan)) this.mappingClassPackage = scan;
                    if (cls.size() > 0) this.additionMappingClass = cls;


                    // 开始解析所有的映射类
                    resolvers = super.getMappingClass();
                }
            }
        }
        return this.resolvers;
    }

    @Override
    public List<? extends IDStrategy> getStrategies() {
        return strategies;
    }

    @Override
    public BasicSetting getBasicInfo() throws ContextException {
        Boolean ignoreEmptySlave = true;
        if (!isInitBasic) {
            for (int i = 0; i < root.getLength(); i++) {
                Node mimosaNode = root.item(i);
                NodeList levelOneList = mimosaNode.getChildNodes();
                for (int j = 0; j < levelOneList.getLength(); j++) {
                    Node node = levelOneList.item(j);
                    if (node.getNodeName().equalsIgnoreCase("convert")) {
                        NamedNodeMap attributes = node.getAttributes();
                        Node name = attributes.getNamedItem("name");
                        Node className = attributes.getNamedItem("class");

                        Map<String, String> properties = this.getNodeByProperties(node);

                        NamingConvert convert = this.getConvert(className != null ? className.getNodeValue() : null,
                                name != null ? name.getNodeValue() : null, properties);
                        basicInfo.setConvert(convert);
                    }

                    if (node.getNodeName().equalsIgnoreCase("mapping")) {
                        NamedNodeMap attributes = node.getAttributes();
                        Node attrScan = attributes.getNamedItem("scan");
                        Node levelNode = attributes.getNamedItem("level");
                        if (levelNode != null) {
                            mappingLevel = levelNode.getNodeValue();
                        }
                    }

                    if (node.getNodeName().equalsIgnoreCase("datasource")) {
                        NamedNodeMap attributes = node.getAttributes();
                        Node ies = attributes.getNamedItem("ignoreEmptySlave");
                        if (ies != null) {
                            ignoreEmptySlave = super.isStringTrue(ies.getNodeValue());
                        }
                    }

                    if (node.getNodeName().equalsIgnoreCase("interceptSession")) {
                        String sessionClass = this.getXmlNodeAny(node, "value");
                        try {
                            Class c = Class.forName(sessionClass);
                            AbstractInterceptSession session = (AbstractInterceptSession) c.newInstance();
                            this.basicInfo.setInterceptSession(session);
                        } catch (Exception e) {
                            throw new ContextException(I18n.print("init_intercept_session_error"), e);
                        }
                    }
                }
            }

            this.isShowSQL = basicInfo.isShowSQL();
            if (StringTools.isNotEmpty(mappingLevel)) {
                try {
                    MappingLevel ml = MappingLevel.valueOf(mappingLevel);
                    basicInfo.setMappingLevel(ml);
                } catch (Exception e) {
                    throw new IllegalArgumentException(I18n.print("mapping_level_not_found", mappingLevel), e);
                }
            }
            basicInfo.setIgnoreEmptySlave(ignoreEmptySlave);
            isInitBasic = true;
        }
        return this.basicInfo;
    }

    @Override
    public List<String> getMappers() {
        List<String> mps = new ArrayList<>();
        for (int i = 0; i < root.getLength(); i++) {
            Node mimosaNode = root.item(i);
            if (mimosaNode != null) {
                NodeList list = mimosaNode.getChildNodes();
                for (int j = 0; j <= list.getLength(); j++) {
                    Node node = list.item(j);
                    if (node != null && node.getNodeName().equalsIgnoreCase("mapper")) {
                        mps.add(this.getXmlNodeAny(node, "value"));
                    }
                    if (node != null && node.getNodeName().equalsIgnoreCase("mappers")) {
                        NodeList mappers = node.getChildNodes();
                        for (int k = 0; k < mappers.getLength(); k++) {
                            Node mapperItem = mappers.item(k);
                            if (mapperItem != null && mapperItem.getNodeName().equalsIgnoreCase("value")) {
                                mps.add(this.getXmlNodeAny(mapperItem, "value"));
                            }
                        }
                    }
                }
            }
        }
        return mps;
    }

    protected String getAttrByName(Node node, String name) {
        NamedNodeMap map = node.getAttributes();
        if (map != null) {
            Node attr = map.getNamedItem(name);
            if (attr != null) {
                return attr.getNodeValue();
            }
        }
        return null;
    }

    @Override
    public List<FactoryBuilder> getAuxFactoryBuilder() throws ClassNotFoundException, IllegalAccessException, InstantiationException {
        List<FactoryBuilder> mps = new ArrayList<>();
        for (int i = 0; i < root.getLength(); i++) {
            Node mimosaNode = root.item(i);
            if (mimosaNode != null) {
                NodeList list = mimosaNode.getChildNodes();
                for (int j = 0; j <= list.getLength(); j++) {
                    Node node = list.item(j);
                    if (node != null && node.getNodeName().equalsIgnoreCase("AuxBuilder")) {
                        FactoryBuilderConfig config = new FactoryBuilderConfig();
                        NodeList properties = node.getChildNodes();
                        if (properties != null) {
                            for (int k = 0; k < properties.getLength(); k++) {
                                Node np = properties.item(k);
                                if (np.getNodeType() == Node.ELEMENT_NODE) {
                                    String name = this.getAttrByName(np, "name");
                                    config.addValue(name, this.getXmlNodeAny(np, "value"));
                                }
                            }
                        }

                        String className = this.getXmlNodeAny(node, "value");
                        Class c = Class.forName(className);
                        FactoryBuilder factoryBuilder = (FactoryBuilder) c.newInstance();
                        factoryBuilder.loadConfig(config);
                        mps.add(factoryBuilder);
                    }
                }
            }
        }
        return mps;
    }
}
