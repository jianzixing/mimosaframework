package org.mimosaframework.orm.convert;

/**
 * @author yangankang
 */
public class DefaultMappingNamedConvert implements MappingNamedConvert {
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
}
