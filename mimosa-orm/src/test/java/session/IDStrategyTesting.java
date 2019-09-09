package session;

import org.junit.Before;
import org.mimosaframework.orm.MimosaSessionFactoryBuilder;
import org.mimosaframework.orm.SessionFactory;
import org.mimosaframework.orm.SessionFactoryBuilder;
import org.mimosaframework.orm.XmlAppContext;
import org.mimosaframework.orm.exception.ContextException;

public class IDStrategyTesting {
    private SessionFactory sessionFactory = null;

    @Before
    public void init() throws ContextException {
        XmlAppContext context = new XmlAppContext(SessionFactoryBuilder.class.getResourceAsStream("/local-mimosa.xml"));
        sessionFactory = context.getSessionFactoryBuilder().build();
    }

}
