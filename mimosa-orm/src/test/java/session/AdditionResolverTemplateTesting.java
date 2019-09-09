package session;

import org.junit.Before;
import org.junit.Test;
import org.mimosaframework.orm.*;
import org.mimosaframework.orm.exception.ContextException;
import org.mimosaframework.orm.strategy.AutoIncrementStrategy;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class AdditionResolverTemplateTesting {
    private SessionFactory sessionFactory = null;

    @Before
    public void init() throws ContextException {
        XmlAppContext context = new XmlAppContext(SessionFactoryBuilder.class.getResourceAsStream("/addition-resolver-mimosa.xml"));
        sessionFactory = context.getSessionFactoryBuilder().build();
    }
}
