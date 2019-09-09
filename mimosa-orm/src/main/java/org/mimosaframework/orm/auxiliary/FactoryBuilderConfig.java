package org.mimosaframework.orm.auxiliary;

import java.util.LinkedHashMap;
import java.util.Map;

public class FactoryBuilderConfig {
    private Map<String, String> values;

    public Map<String, String> getValues() {
        return values;
    }

    public void setValues(Map<String, String> values) {
        this.values = values;
    }

    public void addValue(String name, String value) {
        if (values == null) {
            values = new LinkedHashMap<>();
        }
        values.put(name, value);
    }

    public String getValue(String name) {
        if (this.values != null) {
            return this.values.get(name);
        }
        return null;
    }
}
