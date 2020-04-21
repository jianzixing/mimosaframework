package org.mimosaframework.orm.convert;

public interface NamingConvert {
    String convert(String name, ConvertType type);

    String prefix(String name, String prefix);
}
