package org.mimosaframework.orm.convert;

import java.util.LinkedHashMap;
import java.util.Map;

public final class ConvertFactory {
    private static final Map<String, Class<? extends MappingNamedConvert>> convert = new LinkedHashMap<String, Class<? extends MappingNamedConvert>>();

    static {
        convert.put("H2U", H2UMappingNamedConvert.class);
        convert.put("DEFAULT", DefaultMappingNamedConvert.class);
    }

    public static MappingNamedConvert getDefaultConvert() {
        return new DefaultMappingNamedConvert();
    }

    public static MappingNamedConvert getConvert(String name) {
        Class<? extends MappingNamedConvert> c = convert.get(name);
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
