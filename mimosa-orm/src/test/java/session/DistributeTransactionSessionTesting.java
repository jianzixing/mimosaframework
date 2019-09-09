package session;

import org.junit.Before;
import org.junit.Test;
import org.mimosaframework.core.json.ModelObject;
import org.mimosaframework.core.utils.RandomUtils;
import org.mimosaframework.orm.*;
import org.mimosaframework.orm.exception.ContextException;
import org.mimosaframework.orm.exception.MimosaException;
import org.mimosaframework.orm.exception.TransactionException;
import program.service.TransactionSessionServiceTesting;

import java.io.IOException;
import java.sql.SQLException;

public class DistributeTransactionSessionTesting {
    private SessionTemplate template = null;

    @Before
    public void init() throws ContextException {
        XmlAppContext context = new XmlAppContext(SessionFactoryBuilder.class.getResourceAsStream("/distributed-template-mimosa.xml"));
        SessionFactory sessionFactory = context.getSessionFactoryBuilder().build();
        template = new MimosaSessionTemplate();
        ((MimosaSessionTemplate) template).setSessionFactory(sessionFactory);
    }

    @Test
    public void transForNormal() throws MimosaException, IOException, SQLException {
        TransactionSessionServiceTesting.transForNormal(template);
    }

    @Test
    public void transForNormalSave() throws SQLException {
        TransactionSessionServiceTesting.transForNormalSave(template);
    }

    @Test
    public void transDeleteFail() throws Exception {
        TransactionSessionServiceTesting.transDeleteFail(template);
    }

    @Test
    public void transDeleteSucc() throws SQLException {
        TransactionSessionServiceTesting.transDeleteSucc(template);
    }

    @Test
    public void transUpdateFail() throws TransactionException {
        TransactionSessionServiceTesting.transUpdateFail(template);
    }


    @Test
    public void transUpdateSucc() throws TransactionException {
        TransactionSessionServiceTesting.transUpdateSucc(template);
    }

    @Test
    public void testSupports() {
        TransactionSessionServiceTesting.testSupports(template);
    }

    @Test
    public void testMandatory() {
        TransactionSessionServiceTesting.testMandatory(template);
    }

    @Test
    public void testRequiresNew() {
        TransactionSessionServiceTesting.testRequiresNew(template);
    }

    @Test
    public void testNotSupported() {
        TransactionSessionServiceTesting.testNotSupported(template);
    }

    @Test
    public void testNever() throws TransactionException {
        TransactionSessionServiceTesting.testNever(template);
    }

    @Test
    public void testNested() throws TransactionException {
        TransactionSessionServiceTesting.testNested(template);
    }

    @Test
    public void test2() throws TransactionException {
        TransactionSessionServiceTesting.test2(template);
    }
}
