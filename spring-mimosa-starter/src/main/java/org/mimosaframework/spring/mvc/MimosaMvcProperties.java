package org.mimosaframework.spring.mvc;

import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.Map;

@ConfigurationProperties("mimosa.mvc")
public class MimosaMvcProperties {
    private String prefix;
    private String module;
    private Map<String, String> replaces;

    public String getPrefix() {
        return prefix;
    }

    public void setPrefix(String prefix) {
        this.prefix = prefix;
    }

    public String getModule() {
        return module;
    }

    public void setModule(String module) {
        this.module = module;
    }

    public Map<String, String> getReplaces() {
        return replaces;
    }

    public void setReplaces(Map<String, String> replaces) {
        this.replaces = replaces;
    }
}
