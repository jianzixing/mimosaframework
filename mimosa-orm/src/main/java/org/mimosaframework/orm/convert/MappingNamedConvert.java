package org.mimosaframework.orm.convert;

public interface MappingNamedConvert {

    String convert(String name);

    String reverse(String name);
}
