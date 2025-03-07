package org.mimosaframework.spring.mvc;

import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.Map;

@ConfigurationProperties("mimosa.mvc")
public class MimosaMvcProperties {
    private String prefix;
    private Map<String, String> replaces;

    public String getPrefix() {
        return prefix;
    }

    public void setPrefix(String prefix) {
        this.prefix = prefix;
    }

    public Map<String, String> getReplaces() {
        return replaces;
    }

    public void setReplaces(Map<String, String> replaces) {
        this.replaces = replaces;
    }
}
