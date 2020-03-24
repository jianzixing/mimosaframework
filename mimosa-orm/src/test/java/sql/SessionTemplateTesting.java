package sql;

import org.junit.Before;
import org.junit.Test;
import org.mimosaframework.orm.*;
import org.mimosaframework.orm.exception.ContextException;
import org.mimosaframework.orm.sql.create.Columns;
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
                        .modify().column(TableUser.createdTime).intType().comment("aaa")
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

    @Test
    public void testCreate1() throws Exception {
        SQLAutonomously sqlAutonomously = SQLAutonomously.newInstance(
                SQLAutonomously.create().table().ifNotExist().name("t_tt")
                        .columns(
                                Columns.column("id").intType().autoIncrement().primary().key().comment("a"),
                                Columns.column("name").varchar(50).not().nullable().comment("b")
                        ).charset("utf8")
        );
        AutoResult autoResult = template.getAutonomously(sqlAutonomously);
        System.out.println(autoResult.getValue());
    }

    @Test
    public void testCreate2() throws Exception {
        SQLAutonomously sqlAutonomously = SQLAutonomously.newInstance(
                SQLAutonomously.create().table().ifNotExist().name(TableUser.class)
                        .columns(
                                Columns.column("id").intType().autoIncrement().primary().key().comment("a"),
                                Columns.column("name").varchar(50).not().nullable().comment("b"),
                                Columns.column(TableUser.createdTime).datetime().not().nullable().comment("b")
                        ).charset("utf8")
        );
        AutoResult autoResult = template.getAutonomously(sqlAutonomously);
        System.out.println(autoResult.getValue());
    }

    @Test
    public void testDelete1() throws Exception {
        SQLAutonomously sqlAutonomously = SQLAutonomously.newInstance(
                SQLAutonomously.delete().table(TableUser.class).from().table(TableUser.class)
                        .where().column(TableUser.id).eq().value(1)
        );
        AutoResult autoResult = template.getAutonomously(sqlAutonomously);
        System.out.println(autoResult.getValue());
    }
}
