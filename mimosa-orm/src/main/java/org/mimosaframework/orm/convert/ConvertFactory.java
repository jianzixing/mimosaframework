package org.mimosaframework.orm.convert;

import java.util.LinkedHashMap;
import java.util.Map;

public final class ConvertFactory {
    private static final Map<String, Class<? extends NamingConvert>> convert = new LinkedHashMap<String, Class<? extends NamingConvert>>();

    static {
        convert.put("H2U", H2UMappingNamedConvert.class);
        convert.put("DEFAULT", DefaultMappingNamedConvert.class);
    }

    public static NamingConvert getDefaultConvert() {
        return new DefaultMappingNamedConvert();
    }

    public static NamingConvert getConvert(String name) {
        Class<? extends NamingConvert> c = convert.get(name);
        if (c != null) {
            try {
                return c.newInstance();
            } catch (InstantiationException e) {
                e.printStackTrace();
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        }
        return null;
    }
}
