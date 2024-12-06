package org.mimosaframework.spring.mvc;

import org.mimosaframework.springmvc.CurdImplement;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.Map;

@ConfigurationProperties("mimosa.mvc")
public class MimosaMvcProperties {
    private Class<? extends CurdImplement> curdImplementClass;
    private Map<String, String> prefix;
    private Map<String, String> replaces;

    public Class<? extends CurdImplement> getCurdImplementClass() {
        return curdImplementClass;
    }

    public void setCurdImplementClass(Class<? extends CurdImplement> curdImplementClass) {
        this.curdImplementClass = curdImplementClass;
    }

    public Map<String, String> getPrefix() {
        return prefix;
    }

    public void setPrefix(Map<String, String> prefix) {
        this.prefix = prefix;
    }

    public Map<String, String> getReplaces() {
        return replaces;
    }

    public void setReplaces(Map<String, String> replaces) {
        this.replaces = replaces;
    }
}
