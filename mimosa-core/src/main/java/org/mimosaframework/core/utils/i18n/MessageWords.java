package org.mimosaframework.core.utils.i18n;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class MessageWords {
    private String project;
    private Map<String, String> words;

    public MessageWords(String project, Map<String, String> words) {
        this.project = project;
        this.words = words;
    }

    public String getProject() {
        return project;
    }

    public void setProject(String project) {
        this.project = project;
    }

    public Map<String, String> getWords() {
        Map<String, String> map = new HashMap<>();
        if (words != null) {
            Iterator<Map.Entry<String, String>> iterator = words.entrySet().iterator();
            while (iterator.hasNext()) {
                Map.Entry<String, String> entry = iterator.next();
                map.put(project + "_" + entry.getKey(), entry.getValue());
            }
        }
        return map;
    }

    public void setWords(Map<String, String> words) {
        this.words = words;
    }
}
