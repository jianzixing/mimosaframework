package org.mimosaframework.orm.scripting;

import org.mimosaframework.core.utils.StringTools;

import java.util.HashMap;
import java.util.Map;

public class XMapper {
    private String fileName;
    private String mapperName;
    private Map<String, SqlNode> actions;

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public String getMapperName() {
        return mapperName;
    }

    public void setMapperName(String mapperName) {
        this.mapperName = mapperName;
    }

    public void addAction(String name, SqlNode sqlNode) {
        if (StringTools.isNotEmpty(name) && sqlNode != null) {
            if (actions == null) {
                actions = new HashMap<>();
            }
            actions.put(name, sqlNode);
        }
    }

    public SqlNode getAction(String name) {
        if (this.actions != null) {
            return this.actions.get(name);
        }
        return null;
    }

    public Map<String, SqlNode> getActions() {
        return actions;
    }
}
