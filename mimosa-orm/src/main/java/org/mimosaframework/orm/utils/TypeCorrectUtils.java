package org.mimosaframework.orm.utils;

import org.mimosaframework.core.json.ModelObject;
import org.mimosaframework.orm.mapping.MappingField;
import org.mimosaframework.orm.mapping.MappingTable;

import java.math.BigDecimal;
import java.util.List;
import java.util.Set;

/**
 * 数据类型矫正
 * 如果有不对的数据类型则通过表字段映射将类型转换成需要的类型
 * ps: 数据都是未转换前的字段比如驼峰命名 myName
 */
public class TypeCorrectUtils {

    public static void correct(ModelObject object, MappingTable table) {
        if (object != null && table != null) {
            Set<MappingField> fields = table.getMappingFields();
            if (fields != null) {
                for (MappingField field : fields) {
                    String name = field.getMappingFieldName();
                    Object o = object.get(name);
                    setParseValue(name, o, field.getMappingFieldType(), object);
                }
            }
        }
    }

    public static void corrects(List<ModelObject> objects, MappingTable table) {
        if (objects != null && table != null) {
            for (ModelObject object : objects) {
                correct(object, table);
            }
        }
    }

    private static void setParseValue(String name, Object o, Class type, ModelObject object) {
        if (o != null) {
            if (type.equals(BigDecimal.class) && !o.getClass().equals(BigDecimal.class)) {
                object.put(name, new BigDecimal(String.valueOf(o)));
            }
            if ((type.equals(int.class) || type.equals(Integer.class)) && (!o.getClass().equals(int.class)) && !o.getClass().equals(Integer.class)) {
                if (o instanceof String) {
                    object.put(name, Integer.parseInt((String) o));
                }
            }
            if ((type.equals(long.class) || type.equals(Long.class)) && (!o.getClass().equals(long.class) && !o.getClass().equals(Long.class))) {
                if (o instanceof String) {
                    object.put(name, Long.parseLong((String) o));
                }
            }
        }
    }
}
