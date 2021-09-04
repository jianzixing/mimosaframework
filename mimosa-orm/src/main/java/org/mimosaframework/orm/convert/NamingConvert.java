package org.mimosaframework.orm.convert;

public interface NamingConvert {
    void setConfig(ConvertConfig config);

    String convert(String name, ConvertType type);

    String prefix(String name, String prefix);
}
