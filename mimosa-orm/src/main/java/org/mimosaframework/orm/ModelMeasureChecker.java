package org.mimosaframework.orm;

import org.mimosaframework.core.exception.ModelCheckerException;
import org.mimosaframework.core.json.ModelObject;
import org.mimosaframework.core.json.ModelObjectChecker;
import org.mimosaframework.core.utils.StringTools;
import org.mimosaframework.orm.annotation.Column;
import org.mimosaframework.orm.i18n.I18n;
import org.mimosaframework.orm.mapping.MappingField;
import org.mimosaframework.orm.mapping.MappingTable;

import java.text.DecimalFormat;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author yangankang
 */
public class ModelMeasureChecker implements ModelObjectChecker {
    private Map<Class, MappingTable> tables;

    public ModelMeasureChecker(Set<MappingTable> mappingTables) {
        if (tables == null && mappingTables != null) {
            tables = new HashMap(mappingTables.size());
            for (MappingTable table : mappingTables) {
                tables.put(table.getMappingClass(), table);
            }
        }
    }

    @Override
    public void checker(ModelObject object, String[] removed) throws ModelCheckerException {
        object.clearEmpty();
        this.checkerValid(object, removed);
    }

    @Override
    public void checkerUpdate(ModelObject object, String[] removed) throws ModelCheckerException {
        if (tables == null || object.getObjectClass() == null) {
            throw new ModelCheckerException(null, Code.NULL_OBJ.toString(), I18n.print("not_found_mapping_class"));
        }
        object.clearEmpty();

        Class c = object.getObjectClass();
        MappingTable table = tables.get(c);
        if (table == null) {
            throw new ModelCheckerException(null, Code.NULL_OBJ.toString(), I18n.print("not_in_table", c.getSimpleName()));
        }

        Set<MappingField> fields = table.getMappingFields();
        List<String> removedList = null;

        for (MappingField field : fields) {
            if (field.isMappingFieldPrimaryKey()) {
                if (object.containsKey(field.getMappingFieldName())) {
                    String value = object.getString(field.getMappingFieldName());
                    if (value == null && "".equals(value)) {
                        throw new ModelCheckerException(field.getMappingFieldName(),
                                Code.PK_NULL.toString(), I18n.print("pk_must", field.getMappingFieldName()));
                    }
                } else {
                    throw new ModelCheckerException(field.getMappingFieldName(),
                            Code.PK_NULL.toString(), I18n.print("pk_must", field.getMappingFieldName()));
                }
            }

            if (removed != null) {
                if (removedList == null) {
                    removedList = new ArrayList<>();
                }
                for (String o : removed) {
                    removedList.add(o);
                }
            }
            if (!object.containsKey(field.getMappingFieldName())) {
                if (removedList == null) {
                    removedList = new ArrayList<>();
                }
                removedList.add(field.getMappingFieldName());
            }
        }

        if (removedList != null) {
            removed = removedList.toArray(new String[]{});
        }
        this.checkerValid(object, removed);
    }

    private void checkerValid(ModelObject object, String[] removed) throws ModelCheckerException {
        if (tables == null || object.getObjectClass() == null) {
            throw new ModelCheckerException(null, Code.NULL_OBJ.toString(), I18n.print("not_found_mapping_class"));
        }
        Class c = object.getObjectClass();
        MappingTable table = tables.get(c);
        if (table == null) {
            throw new ModelCheckerException(null, Code.NULL_OBJ.toString(), I18n.print("not_in_table", c.getSimpleName()));
        }

        Set<MappingField> fields = table.getMappingFields();

        for (MappingField field : fields) {
            String javaName = field.getMappingFieldName();

            if (removed != null) {
                boolean hasRemoved = false;
                for (Object rm : removed) {
                    if (String.valueOf(rm).equals(javaName)) {
                        hasRemoved = true;
                        break;
                    }
                }
                if (hasRemoved) continue;
            }

            int length = field.getMappingFieldLength();
            Column column = field.getMappingFieldAnnotation();
            boolean isNullable = column.nullable();
            String defaultValue = column.defaultValue();
            Class type = field.getMappingFieldType();
            long extMinLength = column.extMinLength();
            String extDecimalFormat = column.extDecimalFormat();
            String extRegExp = column.extRegExp();

            if (String.class.isAssignableFrom(type)) {
                Object o = object.get(javaName);
                if (o != null && String.valueOf(o).length() > length) {
                    throw new ModelCheckerException(javaName, Code.MAX_LENGTH.toString(), I18n.print("field_max_len", javaName));
                }

                if (extMinLength > -1 && o != null && String.valueOf(o).length() < extMinLength) {
                    throw new ModelCheckerException(javaName, Code.MIN_LENGTH.toString(), I18n.print("field_min_len", javaName));
                }
            }

            if (!isNullable && StringTools.isEmpty(defaultValue) && object.get(javaName) == null && StringTools.isEmpty(defaultValue)) {
                throw new ModelCheckerException(javaName, Code.NULL_VALUE.toString(), I18n.print("field_empty", javaName));
            }

            /*如果更新的时候这个就不能给加入了*/
//            if (defaultValue != null && object.get(javaName) == null) {
//                object.put(javaName, defaultValue);
//            }

            if (extDecimalFormat != null && !extDecimalFormat.equals("")) {
                Object o = object.get(javaName);
                if (o != null) {
                    try {
                        Double dn = Double.parseDouble(String.valueOf(o));
                        DecimalFormat decimalFormat = new DecimalFormat(extDecimalFormat);
                        String s = decimalFormat.format(dn);
                        object.put(javaName, Double.parseDouble(s));
                    } catch (Exception e) {
                        e.printStackTrace();
                        throw new ModelCheckerException(javaName, Code.PARSE_NUMBER.toString(), I18n.print("field_format", javaName));
                    }
                }
            }

            if (extRegExp != null && !extRegExp.equals("")) {
                String s = object.getString(javaName);
                if (s != null) {
                    Pattern regex = Pattern.compile(extRegExp);
                    Matcher matcher = regex.matcher(s);
                    if (!matcher.matches()) {
                        throw new ModelCheckerException(javaName, Code.REG_EXP_MATCH.toString(), I18n.print("field_regx", javaName, s, extRegExp));
                    }
                }
            }
        }
    }

    public enum Code {
        NULL_OBJ,
        NULL_RESOLVER,
        MAX_LENGTH,
        MIN_LENGTH,
        NULL_VALUE,
        PARSE_NUMBER,
        REG_EXP_MATCH,
        PK_NULL
    }
}
