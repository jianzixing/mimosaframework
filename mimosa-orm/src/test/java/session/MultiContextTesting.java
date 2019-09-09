package session;

import mytables.TableTestOrder;
import mytables.TableTestUser;
import org.junit.Test;
import org.mimosaframework.orm.*;
import org.mimosaframework.orm.criteria.Criteria;
import org.mimosaframework.orm.exception.ContextException;

public class MultiContextTesting {

    @Test
    public void init() throws ContextException {
        XmlAppContext context1 = new XmlAppContext(SessionFactoryBuilder.class.getResourceAsStream("/multi-1.xml"));
        SessionFactory sessionFactory1 = context1.getSessionFactoryBuilder().build();

        XmlAppContext context2 = new XmlAppContext(SessionFactoryBuilder.class.getResourceAsStream("/multi-2.xml"));
        SessionFactory sessionFactory2 = context2.getSessionFactoryBuilder().build();

        XmlAppContext context3 = new XmlAppContext(SessionFactoryBuilder.class.getResourceAsStream("/multi-3.xml"));
        SessionFactory sessionFactory3 = context3.getSessionFactoryBuilder().build();

        SessionTemplate template1 = new MimosaSessionTemplate();
        ((MimosaSessionTemplate) template1).setSessionFactory(sessionFactory1);
        SessionTemplate template2 = new MimosaSessionTemplate();
        ((MimosaSessionTemplate) template2).setSessionFactory(sessionFactory2);
        SessionTemplate template3 = new MimosaSessionTemplate();
        ((MimosaSessionTemplate) template3).setSessionFactory(sessionFactory3);

        boolean succ = false;
        try {
            template1.get(TableTestOrder.class, 1);
        } catch (Exception e) {
            succ = true;
        }
        if (!succ) {
            throw new IllegalArgumentException("本来不应该在当前容器的表");
        }

        template2.get(TableTestUser.class, 1);
        template3.get(TableTestOrder.class, 1);

        succ = false;
        try {
            template2.get(Criteria.query(TableTestUser.class).eq(TableTestUser.id, 1)
                    .subjoin(TableTestOrder.class).eq(TableTestOrder.userId, TableTestUser.id).query());
        } catch (Exception e) {
            succ = true;
        }
        if (!succ) {
            throw new IllegalArgumentException("本来不应该在当前容器的表");
        }
    }

}
