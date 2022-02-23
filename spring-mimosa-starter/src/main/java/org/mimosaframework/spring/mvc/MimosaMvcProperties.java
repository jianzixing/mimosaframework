package org.mimosaframework.spring.mvc;

import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.Map;

@ConfigurationProperties("mimosa.mvc")
public class MimosaMvcProperties {
    private Class curdImplementClass;
    private Map<String, String> prefixs;
    private Map<String, String> replaces;
    private String[] packages;

    public String[] getPackages() {
        return packages;
    }

    public void setPackages(String[] packages) {
        this.packages = packages;
    }

    public Class getCurdImplementClass() {
        return curdImplementClass;
    }

    public void setCurdImplementClass(Class curdImplementClass) {
        this.curdImplementClass = curdImplementClass;
    }

    public Map<String, String> getPrefixs() {
        return prefixs;
    }

    public void setPrefixs(Map<String, String> prefixs) {
        this.prefixs = prefixs;
    }

    public Map<String, String> getReplaces() {
        return replaces;
    }

    public void setReplaces(Map<String, String> replaces) {
        this.replaces = replaces;
    }
}
