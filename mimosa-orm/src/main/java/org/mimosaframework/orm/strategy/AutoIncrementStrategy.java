package org.mimosaframework.orm.strategy;

import org.mimosaframework.orm.IDStrategy;
import org.mimosaframework.orm.Session;
import org.mimosaframework.orm.exception.StrategyException;
import org.mimosaframework.orm.i18n.I18n;

import java.io.Serializable;

/**
 * @author yangankang
 */
public class AutoIncrementStrategy implements IDStrategy {
    @Override
    public Serializable get(StrategyWrapper sw, Session session) throws StrategyException {
        throw new IllegalArgumentException(I18n.print("special_impl"));
    }
}
