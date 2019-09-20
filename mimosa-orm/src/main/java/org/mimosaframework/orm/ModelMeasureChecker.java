package org.mimosaframework.orm;

import org.mimosaframework.core.exception.ModelCheckerException;
import org.mimosaframework.core.json.ModelObject;
import org.mimosaframework.core.json.ModelObjectChecker;
import org.mimosaframework.orm.annotation.Column;
import org.mimosaframework.orm.mapping.DefaultDisassembleMappingClass;
import org.mimosaframework.orm.mapping.DisassembleMappingClass;
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
        if (tables == null) {
            tables = new HashMap(mappingTables.size());
            for (MappingTable table : mappingTables) {
                tables.put(table.getMappingClass(), table);
            }
        }
    }

    @Override
    public void checker(ModelObject object, Object[] removed) throws ModelCheckerException {
        object.clearEmpty();
        if (tables == null || object.getObjectClass() == null) {
            throw new ModelCheckerException(null, Code.NULL_OBJ, "映射类不存在或者ModelObject没有指定映射类");
        }
        Class c = object.getObjectClass();
        MappingTable table = tables.get(c);
        if (table == null) {
            throw new ModelCheckerException(null, Code.NULL_OBJ, "当前类" + c.getSimpleName() + "没有在映射类列表中");
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
            Class type = column.type();
            long extMinLength = column.extMinLength();
            String extDecimalFormat = column.extDecimalFormat();
            String extRegExp = column.extRegExp();

            if (String.class.isAssignableFrom(type)) {
                Object o = object.get(javaName);
                if (o != null && String.valueOf(o).length() > length) {
                    throw new ModelCheckerException(javaName, Code.MAX_LENGTH, "字段 " + javaName + " 太长无法校验通过");
                }

                if (extMinLength > -1 && o != null && String.valueOf(o).length() < extMinLength) {
                    throw new ModelCheckerException(javaName, Code.MIN_LENGTH, "字段 " + javaName + " 太短无法校验通过");
                }
            }

            if (!isNullable && defaultValue == null && object.get(javaName) == null) {
                throw new ModelCheckerException(javaName, Code.NULL_VALUE, "字段 " + javaName + " 不能为空无法校验通过");
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
                        throw new ModelCheckerException(javaName, Code.PARSE_NUMBER, "字段 " + javaName + " " + o + "转化数字格式出错");
                    }
                }
            }

            if (extRegExp != null && !extRegExp.equals("")) {
                String s = object.getString(javaName);
                if (s != null) {
                    Pattern regex = Pattern.compile(extRegExp);
                    Matcher matcher = regex.matcher(s);
                    if (!matcher.matches()) {
                        throw new ModelCheckerException(javaName, Code.REG_EXP_MATCH, "字段 " + javaName + " " + s + " 匹配正则表达式 " + extRegExp + " 失败");
                    }
                }
            }
        }
    }

    @Override
    public void checkerUpdate(ModelObject object, Object[] removed) throws ModelCheckerException {
        if (tables == null || object.getObjectClass() == null) {
            throw new ModelCheckerException(null, Code.NULL_OBJ, "映射类不存在或者ModelObject没有指定映射类");
        }

        Class c = object.getObjectClass();
        MappingTable table = tables.get(c);
        if (table == null) {
            throw new ModelCheckerException(null, Code.NULL_OBJ, "当前类" + c.getSimpleName() + "没有在映射类列表中");
        }

        Set<MappingField> fields = table.getMappingFields();
        List<Object> removedList = null;

        for (MappingField field : fields) {
            if (field.isMappingFieldPrimaryKey()) {
                if (object.containsKey(field.getMappingFieldName())) {
                    String value = object.getString(field.getMappingFieldName());
                    if (value == null && "".equals(value)) {
                        throw new ModelCheckerException(field.getMappingFieldName(),
                                Code.PK_NULL, "主键 " + field.getMappingFieldName() + " 更新时必须存在");
                    }
                } else {
                    throw new ModelCheckerException(field.getMappingFieldName(),
                            Code.PK_NULL, "主键 " + field.getMappingFieldName() + " 更新时必须存在");
                }
            }

            if (removed != null) {
                if (removedList == null) {
                    removedList = new ArrayList<>();
                }
                for (Object o : removed) {
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
            removed = removedList.toArray();
        }
        this.checker(object, removed);
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
