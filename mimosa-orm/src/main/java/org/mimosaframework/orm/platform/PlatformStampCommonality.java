package org.mimosaframework.orm.platform;

import org.mimosaframework.core.utils.StringTools;
import org.mimosaframework.orm.sql.stamp.*;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public abstract class PlatformStampCommonality {
    protected String NL = "\n";
    protected String TAB = "\t";
    protected String NL_TAB = "\n\t";

    protected List<String> declares = null;
    protected List<ExecuteImmediate> builders = null;
    protected List<ExecuteImmediate> begins = null;
    protected boolean declareInBegin = false;

    protected boolean multiExecuteImmediate() {
        if ((begins != null && begins.size() > 0) || (builders != null && builders.size() > 0)) {
            return true;
        }
        return false;
    }

    protected List<ExecuteImmediate> getBuilders() {
        if (builders == null) builders = new ArrayList<>();
        return builders;
    }

    protected List<ExecuteImmediate> getBegins() {
        if (begins == null) begins = new ArrayList<>();
        return begins;
    }

    protected List<String> getDeclares() {
        if (declares == null) declares = new ArrayList<>();
        return declares;
    }

    protected String toSQLString(ExecuteImmediate ei) {
        if (this.builders != null || this.begins != null) {
            if (ei != null) {
                if (this.builders == null) this.builders = new ArrayList<>();
                this.builders.add(0, ei);
            }
            StringBuilder nsb = new StringBuilder();
            this.appendBuilderBegin(nsb);
            if (!declareInBegin && declares != null && declares.size() > 0) {
                this.appendBuilderDeclare(nsb, false);
            }
            nsb.append(NL + "BEGIN ");
            if (declareInBegin && declares != null && declares.size() > 0) {
                this.appendBuilderDeclare(nsb, true);
            }
            this.appendBuilders(nsb, begins);
            this.appendBuilders(nsb, builders);
            this.appendBuilderEnd(nsb);
            return nsb.toString();
        } else {
            return ei.sql;
        }
    }

    protected void appendBuilderDeclare(StringBuilder nsb, boolean isIn) {
        nsb.append(NL + "DECLARE ");
        for (String s : declares) {
            nsb.append(NL_TAB + (isIn ? TAB : "") + s + ";");
        }
    }

    private void appendBuilders(StringBuilder nsb, List<ExecuteImmediate> builders) {
        if (builders != null) {
            for (ExecuteImmediate item : builders) {
                if (StringTools.isNotEmpty(item.sql)
                        || StringTools.isNotEmpty(item.procedure)) {
                    if (StringTools.isNotEmpty(item.procedure)) {
                        if (item.procedure.equalsIgnoreCase("BEGIN")) {
                            nsb.append(NL_TAB + item.procedure);
                        } else {
                            nsb.append(NL_TAB + item.procedure + ";");
                        }
                    } else {
                        this.appendBuilderWrapper(item, nsb);
                    }
                }
            }
        }
    }

    protected void appendBuilderWrapper(ExecuteImmediate item, StringBuilder nsb) {
        if (StringTools.isNotEmpty(item.preview)) {
            nsb.append(NL_TAB + item.preview + " EXECUTE IMMEDIATE ");
        } else {
            nsb.append(NL_TAB + "EXECUTE IMMEDIATE ");
        }

        if (StringTools.isNotEmpty(item.end)) {
            nsb.append("'" + item.sql + "';");
            nsb.append(item.end + ";");
        } else {
            nsb.append("'" + item.sql + "'; ");
        }
    }

    protected void appendBuilderBegin(StringBuilder nsb) {

    }

    protected void appendBuilderEnd(StringBuilder nsb) {
        nsb.append(NL + "END;");
    }


    protected void parseValue(StringBuilder sb,
                              String keyName,
                              Object value,
                              List<SQLDataPlaceholder> placeholders) {
        if (value.getClass().isArray()) {
            Object[] values = (Object[]) value;
            sb.append("(");
            for (int i = 0; i < values.length; i++) {
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

    protected PlatformDialect getDialect() {
        return null;
    }

    protected String getColumnType(KeyColumnType columnType, int len, int scale) {
        ColumnType ct = this.getDialect().getColumnType(columnType);
        if (ct.getCompareType() == ColumnCompareType.NONE) {
            return ct.getTypeName();
        } else if (ct.getCompareType() == ColumnCompareType.JAVA) {
            if (columnType == KeyColumnType.DECIMAL) {
                return ct.getTypeName() + "(" + len + "," + scale + ")";
            }
            return ct.getTypeName() + "(" + len + ")";
        } else if (ct.getCompareType() == ColumnCompareType.SELF) {
            return ct.getTypeName() + "(" + ct.getLength() + ")";
        }
        return null;
    }
}
