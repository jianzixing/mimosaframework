package org.mimosaframework.spring.mvc;

import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.Map;

@ConfigurationProperties("mimosa.mvc")
public class MimosaMvcProperties {
    private Map<String, String> prefix;
    private Map<String, String> replaces;

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
