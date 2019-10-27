package org.mimosaframework.orm;

import org.mimosaframework.core.utils.i18n.Messages;
import org.mimosaframework.orm.exception.ContextException;
import org.mimosaframework.orm.i18n.LanguageMessageFactory;

public class MimosaSessionFactoryBuilder implements SessionFactoryBuilder {
    private ContextContainer contextValues;

    public MimosaSessionFactoryBuilder(ContextContainer contextValues) {
        this.contextValues = contextValues;
    }

    @Override
    public SessionFactory build() throws ContextException {
        if (this.contextValues == null) {
            throw new ContextException(Messages.get(LanguageMessageFactory.PROJECT,
                    MimosaSessionFactoryBuilder.class, "init_context"));
        }
        return new MimosaSessionFactory(this.contextValues);
    }
}
