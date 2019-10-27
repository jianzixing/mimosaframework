package org.mimosaframework.orm.strategy;

import org.mimosaframework.core.utils.i18n.Messages;
import org.mimosaframework.orm.IDStrategy;
import org.mimosaframework.orm.Session;
import org.mimosaframework.orm.exception.StrategyException;
import org.mimosaframework.orm.i18n.LanguageMessageFactory;

import java.io.Serializable;

/**
 * @author yangankang
 */
public class AutoIncrementStrategy implements IDStrategy {
    @Override
    public Serializable get(StrategyWrapper sw, Session session) throws StrategyException {
        throw new IllegalArgumentException(Messages.get(LanguageMessageFactory.PROJECT,
                AutoIncrementStrategy.class, "special_impl"));
    }
}
