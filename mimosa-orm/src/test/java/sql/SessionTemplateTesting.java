package sql;

import org.junit.Before;
import org.junit.Test;
import org.mimosaframework.orm.*;
import org.mimosaframework.orm.exception.ContextException;
import org.mimosaframework.orm.sql.drop.DropAnyBuilder;
import org.mimosaframework.orm.sql.drop.DropFactory;
import tables.TableUser;

public class SessionTemplateTesting {
    private static SessionTemplate template = null;

    @Before
    public void init() throws ContextException {
        if (template == null) {
            XmlAppContext context = new XmlAppContext(SessionFactoryBuilder.class.getResourceAsStream("/template-mimosa.xml"));
            SessionFactory sessionFactory = context.getSessionFactoryBuilder().build();
            template = new MimosaSessionTemplate();
            ((MimosaSessionTemplate) template).setSessionFactory(sessionFactory);
        }
    }

    @Test
    public void testAlter1() throws Exception {
        SQLAutonomously sqlAutonomously = SQLAutonomously.newInstance(
                SQLAutonomously.alter().table(TableUser.class)
                        .modify().column("abc").intType().comment("aaa")
        );
        AutoResult autoResult = template.getAutonomously(sqlAutonomously);

    }

    @Test
    public void testDrop1() throws Exception {
        SQLAutonomously sqlAutonomously = SQLAutonomously.newInstance(
                SQLAutonomously.drop().table().table(TableUser.class)
        );
        AutoResult autoResult = template.getAutonomously(sqlAutonomously);
        System.out.println(autoResult.getValue());
    }
}
