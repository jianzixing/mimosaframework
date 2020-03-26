package org.mimosaframework.orm.sql;

import java.util.Iterator;

public abstract class AbstractValueSQLBuilder
        extends AbstractSQLBuilder
        implements AbsValueBuilder {

    @Override
    public Object value(Object value) {
        if (value instanceof Iterable) {
            this.sqlBuilder.addParenthesisStart();
            Iterator iterator = ((Iterable) value).iterator();
            int i = 0;
            while (iterator.hasNext()) {
                this.sqlBuilder.addDataPlaceholder(this.lastPlaceholderName + "&" + i, iterator.next());
                if (iterator.hasNext()) this.sqlBuilder.addSplit();
                i++;
            }
            this.sqlBuilder.addParenthesisEnd();
        } else if (value.getClass().isArray()) {
            this.sqlBuilder.addParenthesisStart();
            Object[] objects = (Object[]) value;
            for (int i = 0; i < objects.length; i++) {
                this.sqlBuilder.addDataPlaceholder(this.lastPlaceholderName + "&" + i, objects[i]);
                if (i != objects.length - 1) this.sqlBuilder.addSplit();
            }
            this.sqlBuilder.addParenthesisEnd();
        } else {
            this.sqlBuilder.addDataPlaceholder(this.lastPlaceholderName, value);
        }
        return this;
    }
}
