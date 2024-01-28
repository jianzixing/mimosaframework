package org.mimosaframework.orm.scripting;

import org.mimosaframework.core.utils.StringTools;
import org.mimosaframework.orm.scripting.tags.*;
import org.w3c.dom.Document;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class XMLSQLDefiner implements SQLDefiner {
    private static final List<String> ACTION_NAMES = new ArrayList<>(5);
    private static DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();

    static {
        ACTION_NAMES.add("select");
        ACTION_NAMES.add("delete");
        ACTION_NAMES.add("update");
        ACTION_NAMES.add("insert");
        ACTION_NAMES.add("sql");
    }

    private DefinerConfigure configuration;
    private Map<String, MixedSqlNode> sqlNodeMap = new HashMap<>();

    public XMLSQLDefiner(DefinerConfigure configuration) {
        this.configuration = configuration;
    }

    @Override
    public XMapper getDefiner(InputStream inputStream, String fileName) throws ParserConfigurationException, IOException, SAXException {
        DocumentBuilder db = dbf.newDocumentBuilder();
        db.setEntityResolver(new IgnoreDTDEntityResolver());
        Document document = db.parse(inputStream);

        if (fileName != null) {
            fileName = fileName.replace(".xml", "");
        }

        NodeList mapperList = document.getElementsByTagName("mapper");
        if (mapperList != null && mapperList.getLength() > 0) {
            sqlNodeMap.clear();

            Node mapper = mapperList.item(0);
            NodeList actions = mapper.getChildNodes();
            if (actions != null) {
                String namespace = this.getStringAttribute(mapper, "namespace");
                XMapper xMapper = new XMapper();
                xMapper.setFileName(fileName);
                xMapper.setMapperName(StringTools.isNotEmpty(namespace) ? namespace : fileName);

                // 先处理sql标签
                for (int i = 0; i < actions.getLength(); i++) {
                    Node node = actions.item(i);
                    String actionName = node.getNodeName().toLowerCase();
                    String id = this.getStringAttribute(node, "id");
                    if (actionName.equals("sql")) {
                        sqlNodeMap.put(id, new MixedSqlNode(this.parseDynamicTags(node), node.getNodeName().toLowerCase()));
                    }
                }

                for (int i = 0; i < actions.getLength(); i++) {
                    Node node = actions.item(i);
                    String actionName = node.getNodeName().toLowerCase();
                    String id = this.getStringAttribute(node, "id");
                    if (ACTION_NAMES.contains(actionName)) {
                        // todo:这里解析mapper中配置的其他功能
                        xMapper.addAction(id, new MixedSqlNode(this.parseDynamicTags(node), node.getNodeName().toLowerCase()));
                    }
                }
                return xMapper;
            }
        }
        return null;
    }

    private List<SqlNode> parseDynamicTags(Node node) {
        NodeList list = node.getChildNodes();
        if (list != null && list.getLength() > 0) {
            List<SqlNode> contents = new ArrayList<>();
            for (int i = 0; i < list.getLength(); i++) {
                this.nodeToX(list.item(i), contents);
            }
            return contents;
        }
        return null;
    }

    private String getStringAttribute(Node node, String name) {
        NamedNodeMap map = node.getAttributes();
        if (map != null) {
            Node item = map.getNamedItem(name);
            if (item == null) {
                item = map.getNamedItem(name.toUpperCase());
            }
            if (item != null) {
                return item.getNodeValue();
            }
        }
        return null;
    }

    private void nodeToX(Node node, List<SqlNode> targetContents) {
        if (node != null) {
            if (node.getNodeType() == Node.TEXT_NODE) {
                targetContents.add(new TextSqlNode(node.getTextContent()));
            }

            if (node.getNodeType() == Node.ELEMENT_NODE) {
                String nodeName = node.getNodeName();
                if (nodeName.equalsIgnoreCase("include")) {
                    String refid = this.getStringAttribute(node, "refid");
                    MixedSqlNode sqlNode = this.sqlNodeMap.get(refid);
                    if (sqlNode != null) {
                        targetContents.add(sqlNode);
                    }
                }

                if (nodeName.equalsIgnoreCase("trim")) {
                    List<SqlNode> contents = parseDynamicTags(node);
                    MixedSqlNode mixedSqlNode = new MixedSqlNode(contents);

                    String prefix = this.getStringAttribute(node, "prefix");
                    String prefixOverrides = this.getStringAttribute(node, "prefixOverrides");
                    String suffix = this.getStringAttribute(node, "suffix");
                    String suffixOverrides = this.getStringAttribute(node, "suffixOverrides");
                    TrimSqlNode trim = new TrimSqlNode(configuration, mixedSqlNode, prefix, prefixOverrides, suffix, suffixOverrides);
                    targetContents.add(trim);
                }
                if (nodeName.equalsIgnoreCase("where")) {
                    List<SqlNode> contents = parseDynamicTags(node);
                    MixedSqlNode mixedSqlNode = new MixedSqlNode(contents);
                    WhereSqlNode where = new WhereSqlNode(configuration, mixedSqlNode);
                    targetContents.add(where);
                }
                if (nodeName.equalsIgnoreCase("set")) {
                    List<SqlNode> contents = parseDynamicTags(node);
                    MixedSqlNode mixedSqlNode = new MixedSqlNode(contents);
                    SetSqlNode set = new SetSqlNode(configuration, mixedSqlNode);
                    targetContents.add(set);
                }
                if (nodeName.equalsIgnoreCase("foreach")) {
                    List<SqlNode> contents = parseDynamicTags(node);
                    MixedSqlNode mixedSqlNode = new MixedSqlNode(contents);
                    String collection = this.getStringAttribute(node, "collection");
                    String item = this.getStringAttribute(node, "item");
                    String index = this.getStringAttribute(node, "index");
                    String open = this.getStringAttribute(node, "open");
                    String close = this.getStringAttribute(node, "close");
                    String separator = this.getStringAttribute(node, "separator");
                    ForEachSqlNode forEachSqlNode = new ForEachSqlNode(configuration, mixedSqlNode, collection, index, item, open, close, separator);
                    targetContents.add(forEachSqlNode);
                }
                if (nodeName.equalsIgnoreCase("if") || nodeName.equalsIgnoreCase("when")) {
                    List<SqlNode> contents = parseDynamicTags(node);
                    MixedSqlNode mixedSqlNode = new MixedSqlNode(contents);
                    String test = this.getStringAttribute(node, "test");
                    IfSqlNode ifSqlNode = new IfSqlNode(mixedSqlNode, test);
                    targetContents.add(ifSqlNode);
                }
                if (nodeName.equalsIgnoreCase("choose")) {
                    List<SqlNode> whenSqlNodes = new ArrayList<SqlNode>();
                    List<SqlNode> otherwiseSqlNodes = new ArrayList<SqlNode>();
                    handleWhenOtherwiseNodes(node, whenSqlNodes, otherwiseSqlNodes);
                    SqlNode defaultSqlNode = getDefaultSqlNode(otherwiseSqlNodes);
                    ChooseSqlNode chooseSqlNode = new ChooseSqlNode(whenSqlNodes, defaultSqlNode);
                    targetContents.add(chooseSqlNode);
                }

                if (nodeName.equalsIgnoreCase("otherwise")) {
                    List<SqlNode> contents = parseDynamicTags(node);
                    MixedSqlNode mixedSqlNode = new MixedSqlNode(contents);
                    targetContents.add(mixedSqlNode);
                }
            }
        }
    }

    private void handleWhenOtherwiseNodes(Node chooseSqlNode, List<SqlNode> ifSqlNodes, List<SqlNode> defaultSqlNodes) {
        NodeList children = chooseSqlNode.getChildNodes();
        for (int i = 0; i < children.getLength(); i++) {
            Node child = children.item(i);
            String nodeName = child.getNodeName();
            if (nodeName.equalsIgnoreCase("if") || nodeName.equalsIgnoreCase("when")) {
                this.nodeToX(child, ifSqlNodes);
            } else if (nodeName.equalsIgnoreCase("otherwise")) {
                this.nodeToX(child, defaultSqlNodes);
            }
        }
    }

    private SqlNode getDefaultSqlNode(List<SqlNode> defaultSqlNodes) {
        SqlNode defaultSqlNode = null;
        if (defaultSqlNodes.size() == 1) {
            defaultSqlNode = defaultSqlNodes.get(0);
        } else if (defaultSqlNodes.size() > 1) {
            throw new BuilderException("Too many default (otherwise) elements in choose statement.");
        }
        return defaultSqlNode;
    }

    public MixedSqlNode parseSqlNode(String action, String sql) throws ParserConfigurationException, IOException, SAXException {
        DocumentBuilder db = dbf.newDocumentBuilder();
        db.setEntityResolver(new IgnoreDTDEntityResolver());
        if ("update".equalsIgnoreCase(action)) {
            sql = "<select>" + sql + "</select>";
        } else if ("update".equalsIgnoreCase(action)) {
            sql = "<update>" + sql + "</update>";
        } else if ("insert".equalsIgnoreCase(action)) {
            sql = "<insert>" + sql + "</insert>";
        } else if ("sql".equalsIgnoreCase(action)) {
            sql = "<sql>" + sql + "</sql>";
        } else if ("delete".equalsIgnoreCase(action)) {
            sql = "<delete>" + sql + "</delete>";
        } else {
            sql = "<select>" + sql + "</select>";
        }

        Document document = db.parse(new ByteArrayInputStream(sql.getBytes()));
        Node sqlNode = document.getChildNodes().item(0);
        return new MixedSqlNode(this.parseDynamicTags(sqlNode), sqlNode.getNodeName().toLowerCase());
    }
}
