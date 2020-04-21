package org.mimosaframework.orm.convert;

/**
 * @author yangankang
 */
public class DefaultMappingNamedConvert implements NamingConvert {
    public String convert(String name, ConvertType type) {
        if (type.equals(ConvertType.TABLE_NAME)) {
            if (name.length() > 1) {
                name = name.substring(0, 1).toLowerCase() + name.substring(1);
            } else {
                name = name.toLowerCase();
            }
        }
        return name;
    }

    @Override
    public String prefix(String name, String prefix) {
        if (name.startsWith(prefix)) {
            return name;
        }
        return prefix + name;
    }
}
