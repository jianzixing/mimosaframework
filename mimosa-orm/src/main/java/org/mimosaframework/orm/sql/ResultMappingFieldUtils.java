package org.mimosaframework.orm.sql;

import org.mimosaframework.core.json.ModelObject;
import org.mimosaframework.orm.ModelObjectConvertKey;
import org.mimosaframework.orm.mapping.MappingField;
import org.mimosaframework.orm.mapping.MappingTable;

import java.util.*;

public class ResultMappingFieldUtils {
    /**
     * 通过Select的字段，转换查询的结果字段
     *
     * @param selectBuilder
     * @param convert
     * @param objects
     * @param mappingTables
     */
    public static void convert(SelectBuilder selectBuilder,
                               ModelObjectConvertKey convert,
                               List<ModelObject> objects,
                               Map<Class, MappingTable> mappingTables) {

        if (objects != null && objects.size() > 0) {
            Map<String, String> tableFields = new HashMap<>();
            List<FromBuilder> froms = selectBuilder.getFroms();
            Class defaultTableClass = froms.iterator().next().getTable();

            List<Object> restricts = selectBuilder.getRestrict();
            if (restricts != null) {
                for (Object restrict : restricts) {
                    if (restrict instanceof GroupBuilder) {
                        Class groupTableClass = ((GroupBuilder) restrict).getTable();
                        List<Object> groupFields = ((GroupBuilder) restrict).getFields();
                        if (groupTableClass == null) groupTableClass = defaultTableClass;

                        if (groupFields != null) {
                            MappingTable mappingTable = mappingTables.get(groupTableClass);
                            for (Object groupField : groupFields) {
                                String name = String.valueOf(groupField);
                                MappingField mappingField = mappingTable.getMappingFieldByName(name);
                                tableFields.put(mappingField.getMappingColumnName(), mappingField.getMappingFieldName());
                            }
                        }
                    }
                }
            }

            if (tableFields.size() <= 0) {
                Iterator<FromBuilder> iterator = froms.iterator();
                while (iterator.hasNext()) {
                    FromBuilder entry = iterator.next();
                    Class tableClass = entry.getTable();
                    List<Object> selectFields = entry.getFields();

                    MappingTable mappingTable = mappingTables.get(tableClass);

                    if (selectFields == null || selectFields.size() == 0) {
                        Set<MappingField> mappingFields = mappingTable.getMappingFields();
                        for (MappingField mappingField : mappingFields) {
                            tableFields.put(mappingField.getMappingColumnName(), mappingField.getMappingFieldName());
                        }
                    } else {
                        for (Object object : selectFields) {
                            if (!(object instanceof SelectBuilder)
                                    && !(object instanceof FunBuilder)
                                    && !(object instanceof FieldBuilder)) {
                                String name = String.valueOf(object);
                                MappingField mappingField = mappingTable.getMappingFieldByName(name);
                                if (mappingField != null) {
                                    tableFields.put(mappingField.getMappingColumnName(), mappingField.getMappingFieldName());
                                }
                            }
                        }
                    }
                }
            }

            for (ModelObject object : objects) {
                Set<Object> iterator = object.keySet();
                Set<Object> keyset = new LinkedHashSet<>(iterator);
                for (Object key : keyset) {
                    if (tableFields.get(String.valueOf(key)) != null) {
                        object.put(tableFields.get(String.valueOf(key)), object.get(key));
                        object.remove(key);
                    }
                }
            }
        }
    }
}
