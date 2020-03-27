package org.mimosaframework.orm.sql;

import org.mimosaframework.core.utils.StringTools;
import org.mimosaframework.core.utils.i18n.Messages;
import org.mimosaframework.orm.i18n.LanguageMessageFactory;
import org.mimosaframework.orm.platform.SQLMappingField;
import org.mimosaframework.orm.platform.SkipBlankPlaceholder;

import java.io.Serializable;

public abstract class AbstractFunSQLBuilder
        extends AbstractValueSQLBuilder
        implements FieldFunBuilder, AsBuilder {

    @Override
    public Object as(String aliasName) {
        this.sqlBuilder.AS().addWrapString(aliasName);
        return this;
    }

    protected void funStart() {
    }

    protected void funEnd() {
    }

    @Override
    public Object count(Serializable... params) {
        this.funStart();
        if (params.length > 0) {
            Serializable param = params[0];
            this.sqlBuilder.addBlankHolder(new SkipBlankPlaceholder("COUNT"));
            this.sqlBuilder.addParenthesisStart();
            if ("distinct".equalsIgnoreCase((param + "").trim()) && params.length > 1) {
                this.sqlBuilder.addString("DISTINCT");
                this.setCountItem(params[1]);
            } else {
                this.setCountItem(param);
            }
            this.sqlBuilder.addParenthesisEnd();
        } else {
            this.sqlBuilder.addString("COUNT(1)");
        }
        this.funEnd();
        return this;
    }

    private void setCountItem(Serializable param) {
        if (param instanceof FieldItem) {
            this.setSQLMappingField((FieldItem) param);
        } else if (param.equals("*")) {
            this.sqlBuilder.addString("*");
        } else if (param instanceof Number) {
            this.sqlBuilder.addString("" + param);
        } else if (param instanceof FunItem) {
            throw new IllegalArgumentException(
                    Messages.get(LanguageMessageFactory.PROJECT,
                            AbstractFunSQLBuilder.class, "not_allow_fun", "count")
            );
        } else {
            this.sqlBuilder.addMappingField(new SQLMappingField(param));
        }
    }

    @Override
    public Object max(Serializable... params) {
        this.funStart();
        if (params.length > 0) {
            Serializable param = params[0];
            this.sqlBuilder.addBlankHolder(new SkipBlankPlaceholder("MAX"));
            this.sqlBuilder.addParenthesisStart();
            if ("distinct".equalsIgnoreCase((param + "").trim()) && params.length > 1) {
                this.sqlBuilder.addString("DISTINCT");
                this.setCountItem(params[1]);
            } else {
                this.setCountItem(param);
            }
            this.sqlBuilder.addParenthesisEnd();
        } else {
            this.sqlBuilder.addString("MAX(1)");
        }
        this.funEnd();
        return this;
    }

    @Override
    public Object avg(Serializable... params) {
        this.funStart();
        if (params.length > 0) {
            Serializable param = params[0];
            this.sqlBuilder.addBlankHolder(new SkipBlankPlaceholder("AVG"));
            this.sqlBuilder.addParenthesisStart();
            if ("distinct".equalsIgnoreCase((param + "").trim()) && params.length > 1) {
                this.sqlBuilder.addString("DISTINCT");
                this.setCountItem(params[1]);
            } else {
                this.setCountItem(param);
            }
            this.sqlBuilder.addParenthesisEnd();
        } else {
            this.sqlBuilder.addString("AVG(1)");
        }
        this.funEnd();
        return this;
    }

    @Override
    public Object sum(Serializable... params) {
        this.funStart();
        if (params.length > 0) {
            Serializable param = params[0];
            this.sqlBuilder.addBlankHolder(new SkipBlankPlaceholder("SUM"));
            this.sqlBuilder.addParenthesisStart();
            if ("distinct".equalsIgnoreCase((param + "").trim()) && params.length > 1) {
                this.sqlBuilder.addString("DISTINCT");
                this.setCountItem(params[1]);
            } else {
                this.setCountItem(param);
            }
            this.sqlBuilder.addParenthesisEnd();
        } else {
            this.sqlBuilder.addString("SUM(1)");
        }
        this.funEnd();
        return this;
    }

    @Override
    public Object min(Serializable... params) {
        this.funStart();
        if (params.length > 0) {
            Serializable param = params[0];
            this.sqlBuilder.addBlankHolder(new SkipBlankPlaceholder("MIN"));
            this.sqlBuilder.addParenthesisStart();
            if ("distinct".equalsIgnoreCase((param + "").trim()) && params.length > 1) {
                this.sqlBuilder.addString("DISTINCT");
                this.setCountItem(params[1]);
            } else {
                this.setCountItem(param);
            }
            this.sqlBuilder.addParenthesisEnd();
        } else {
            this.sqlBuilder.addString("MIN(1)");
        }
        this.funEnd();
        return this;
    }

    @Override
    public Object concat(Serializable... params) {
        this.funStart();
        this.sqlBuilder.addBlankHolder(new SkipBlankPlaceholder("CONCAT"));
        this.sqlBuilder.addParenthesisStart();
        int i = 0;
        for (Serializable param : params) {
            if (param instanceof FieldItem) {
                this.setSQLMappingField((FieldItem) param);
            } else if (param instanceof Number) {
                this.sqlBuilder.addString("" + param);
            } else {
                this.sqlBuilder.addMappingField(new SQLMappingField(param));
            }
            if (i != params.length) this.sqlBuilder.addSplit();
        }
        this.sqlBuilder.addParenthesisEnd();
        this.funEnd();
        return this;
    }

    @Override
    public Object substring(Serializable param, int pos, int len) {
        this.funStart();
        this.sqlBuilder.addBlankHolder(new SkipBlankPlaceholder("SUBSTRING"));
        this.sqlBuilder.addParenthesisStart();
        if (param instanceof FieldItem) {
            this.setSQLMappingField((FieldItem) param);
        }
        this.sqlBuilder.addSplit().addString("" + pos).addSplit().addString("" + len);
        this.sqlBuilder.addParenthesisEnd();
        this.funEnd();
        return this;
    }

    private void setSQLMappingField(FieldItem fieldItem) {
        if (StringTools.isNotEmpty(fieldItem.getTableAliasName())) {
            this.sqlBuilder.addMappingField(new SQLMappingField(fieldItem.getTableAliasName(), fieldItem.getField()));
        } else {
            this.sqlBuilder.addMappingField(new SQLMappingField(fieldItem.getTable(), fieldItem.getField()));
        }
    }
}
