package org.mimosaframework.orm.convert;

/**
 * @author yangankang
 */
public class DefaultMappingNamedConvert implements MappingNamedConvert {
    public String convert(String tableName, ConvertType type) {
        if (type.equals(ConvertType.TABLE_NAME)) {
            if (tableName.length() > 1) {
                tableName = tableName.substring(0, 1).toLowerCase() + tableName.substring(1);
            } else {
                tableName = tableName.toLowerCase();
            }
        }
        return tableName;
    }
}
