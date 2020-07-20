package org.mimosaframework.orm.platform;

import org.mimosaframework.orm.mapping.MappingGlobalWrapper;
import org.mimosaframework.orm.sql.stamp.KeyColumnType;
import org.mimosaframework.orm.sql.stamp.StampAction;
import org.mimosaframework.orm.sql.stamp.StampFieldFun;
import org.mimosaframework.orm.sql.stamp.StampWhere;

import java.util.Iterator;
import java.util.List;

public class PlatformStampShare {
    protected PlatformStampCommonality commonality;

    public void setCommonality(PlatformStampCommonality commonality) {
        this.commonality = commonality;
    }

    public void addCommentSQL(MappingGlobalWrapper wrapper,
                              StampAction action,
                              Object param,
                              String commentStr,
                              int type) {
    }

    public void addCommentSQL(MappingGlobalWrapper wrapper,
                              StampAction action,
                              Object param,
                              String commentStr,
                              int type,
                              boolean isCheckHasTable) {

    }

    public void buildWhere(MappingGlobalWrapper wrapper,
                           List<SQLDataPlaceholder> placeholders,
                           StampAction stampTables,
                           StampWhere where,
                           StringBuilder sb) {

    }

    public void buildSelectFieldFun(MappingGlobalWrapper wrapper,
                                    StampAction stampTables,
                                    StampFieldFun fun,
                                    StringBuilder sb) {

    }

    public void parseValue(StringBuilder sb,
                           String keyName,
                           Object value,
                           List<SQLDataPlaceholder> placeholders) {
        if (value.getClass().isArray()) {
            Object[] values = (Object[]) value;
            sb.append("(");
            for (int i = 0; i < values.length; ) {
                sb.append("?");

                SQLDataPlaceholder placeholder = new SQLDataPlaceholder();
                placeholder.setName(keyName + "&" + i);
                placeholder.setValue(values[i]);
                placeholders.add(placeholder);

                i++;
                if (i != values.length) sb.append(",");
            }
            sb.append(")");
        } else if (value instanceof Iterable) {
            Iterable iterable = (Iterable) value;
            Iterator iteratorValue = iterable.iterator();
            int i = 0;
            sb.append("(");
            while (iteratorValue.hasNext()) {
                sb.append("?");

                SQLDataPlaceholder placeholder = new SQLDataPlaceholder();
                placeholder.setName(keyName + "&" + i);
                placeholder.setValue(iteratorValue.next());
                placeholders.add(placeholder);

                i++;
                if (iteratorValue.hasNext()) sb.append(",");
            }
            sb.append(")");
        } else {
            sb.append("?");

            SQLDataPlaceholder placeholder = new SQLDataPlaceholder();
            placeholder.setName(keyName);
            placeholder.setValue(value);
            placeholders.add(placeholder);
        }
    }

    public String getColumnType(KeyColumnType columnType, int len, int scale) {
        return null;
    }


    // oracle
    public void addAutoIncrement(MappingGlobalWrapper wrapper, Class table, String tableStr) {

    }
}
