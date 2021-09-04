package org.mimosaframework.orm.convert;

/**
 * @author yangankang
 */
public class DefaultMappingNamedConvert implements NamingConvert {
    private ConvertConfig config;

    @Override
    public void setConfig(ConvertConfig config) {
        this.config = config;
    }

    public String convert(String name, ConvertType type) {
        if (type.equals(ConvertType.TABLE_NAME)) {
            if (this.config != null && this.config.isUppercase()) {
                name = name.toUpperCase();
            } else {
                if (name.length() > 1) {
                    name = name.substring(0, 1).toLowerCase() + name.substring(1);
                } else {
                    name = name.toLowerCase();
                }
            }
        } else {
            if (this.config != null && this.config.isUppercase()) {
                name = name.toUpperCase();
            }
        }
        return name;
    }

    @Override
    public String prefix(String name, String prefix) {
        if (name.startsWith(prefix)) {
            return name;
        }
        if (this.config != null && this.config.isUppercase()) {
            return (prefix + name).toUpperCase();
        } else {
            return prefix + name;
        }
    }
}
