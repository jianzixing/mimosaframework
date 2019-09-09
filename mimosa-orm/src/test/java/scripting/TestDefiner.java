package scripting;

import org.junit.Before;
import org.junit.Test;
import org.mimosaframework.core.json.ModelObject;
import org.mimosaframework.orm.*;
import org.mimosaframework.orm.criteria.Criteria;
import org.mimosaframework.orm.exception.ContextException;
import org.mimosaframework.orm.exception.MimosaException;
import tables.TableUser;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

public class TestDefiner {
    private SessionFactory sessionFactory;

    @Before
    public void init() throws ContextException {
        XmlAppContext context = new XmlAppContext(SessionFactoryBuilder.class.getResourceAsStream("/scripting/tm.xml"));
        sessionFactory = context.getSessionFactoryBuilder().build();
    }

    @Test
    public void test() throws Exception {
        Session session = sessionFactory.openSession();
        ModelObject parameter = new ModelObject();
        parameter.put("table", "t_user");
        parameter.put("w1", "1");
        parameter.put("w2", "ankang");
        Object r1 = session.getAutonomously(new TAutonomously("t1.get", parameter));
        Object r2 = session.getAutonomously(new TAutonomously("t1.update", parameter));
        System.out.println(r1);
        System.out.println(r2);
        session.close();
    }

    @Test
    public void test2() throws Exception {
        Session session = sessionFactory.openSession();
        Object r1 = session.getAutonomously(new TAutonomously("t1.get2"));
        System.out.println(r1);
        session.close();
    }

    @Test
    public void test3() throws Exception {
        Session session = sessionFactory.openSession();
        AutoResult r1 = session.getAutonomously(new TAutonomously("t1.get2"));
        System.out.println(r1.getNumbers(TableUser.id));

        List<ModelObject> objs = session.list(Criteria.query(TableUser.class).in(TableUser.id, r1.getNumbers(TableUser.id)));
        System.out.println(objs);
        session.close();
    }

    @Test
    public void test4() throws Exception {
        Session session = sessionFactory.openSession();
        AutoResult r1 = session.getAutonomously(new TAutonomously("t1.get3"));
        System.out.println(r1.longValue());
        session.close();
    }

    @Test
    public void test5() throws Exception {
        Session session = sessionFactory.openSession();
        AutoResult r1 = session.getAutonomously(new TAutonomously("t1.get3"));
        System.out.println(r1.doubleValue());
        session.close();
    }

    @Test
    public void test6() throws Exception {
        Session session = sessionFactory.openSession();
        AutoResult r1 = session.getAutonomously(new TAutonomously("t1.get3"));
        System.out.println(r1.intValue());
        session.close();
    }

    @Test
    public void test7() throws Exception {
        Session session = sessionFactory.openSession();
        ModelObject parameter = new ModelObject();
        parameter.put("a", 2);
        parameter.put("b", 1);
        parameter.put("c", 3);
        AutoResult r1 = session.getAutonomously(new TAutonomously("t1.get5", parameter));
        System.out.println(r1.intValue());
        session.close();
    }
}
