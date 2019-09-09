package org.mimosaframework.orm.convert;

/**
 * @author yangankang
 */
public class DefaultMappingNamedConvert implements MappingNamedConvert {
    public String convert(String name) {
        return name;
    }

    public String reverse(String name) {
        return name;
    }
}
