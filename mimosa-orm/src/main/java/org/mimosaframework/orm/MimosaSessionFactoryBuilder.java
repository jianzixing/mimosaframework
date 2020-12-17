package org.mimosaframework.orm;

import org.mimosaframework.orm.exception.ContextException;
import org.mimosaframework.orm.i18n.I18n;

public class MimosaSessionFactoryBuilder implements SessionFactoryBuilder {
    private Configuration contextValues;

    public MimosaSessionFactoryBuilder(Configuration contextValues) {
        this.contextValues = contextValues;
    }

    @Override
    public SessionFactory build() throws ContextException {
        if (this.contextValues == null) {
            throw new ContextException(I18n.print("init_context"));
        }
        return new MimosaSessionFactory(this.contextValues);
    }
}
