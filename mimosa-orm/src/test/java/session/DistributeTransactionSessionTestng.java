package session;

import org.mimosaframework.core.json.ModelObject;
import org.mimosaframework.orm.*;
import org.mimosaframework.orm.exception.ContextException;
import org.mimosaframework.orm.exception.TransactionException;
import org.mimosaframework.orm.transaction.Transaction;
import org.testng.annotations.BeforeSuite;
import org.testng.annotations.Test;
import program.service.TransactionSessionServiceTesting;
import tables.TableUser;

import java.util.Date;

/**
 * 测试事物是否关闭
 */
public class DistributeTransactionSessionTestng {

    private SessionTemplate template = null;

    @BeforeSuite
    public void init() throws ContextException {
        XmlAppContext context = new XmlAppContext(SessionFactoryBuilder.class.getResourceAsStream("/distributed-template-mimosa.xml"));
        SessionFactory sessionFactory = context.getSessionFactoryBuilder().build();
        template = new MimosaSessionTemplate();
        ((MimosaSessionTemplate) template).setSessionFactory(sessionFactory);
    }

    @Test(invocationCount = 10000, threadPoolSize = 10)
    public void service() throws TransactionException {
        TransactionSessionServiceTesting.testNested(template);
    }
}
