package sql;

import org.junit.Before;
import org.junit.Test;
import org.mimosaframework.orm.*;
import org.mimosaframework.orm.exception.ContextException;
import org.mimosaframework.orm.sql.FieldItem;
import org.mimosaframework.orm.sql.create.Columns;
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
                        .modify().column(TableUser.createdTime).datetime().comment("aaa")
        );
        AutoResult autoResult = template.getAutonomously(sqlAutonomously);

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

    @Test
    public void testDrop1() throws Exception {
        SQLAutonomously sqlAutonomously = SQLAutonomously.newInstance(
                SQLAutonomously.drop().table().table(TableUser.class)
        );
        AutoResult autoResult = template.getAutonomously(sqlAutonomously);
        System.out.println(autoResult.getValue());
    }

    @Test
    public void testInsert1() throws Exception {
        SQLAutonomously sqlAutonomously = SQLAutonomously.newInstance(
                SQLAutonomously.insert().into().table(TableUser.class)
                        .columns(TableUser.id, TableUser.createdTime)
                        .values()
                        .row(1, "2019-01-01 10:00:00")
        );
        AutoResult autoResult = template.getAutonomously(sqlAutonomously);
        System.out.println(autoResult.getValue());
    }

    @Test
    public void testSelect1() throws Exception {
        SQLAutonomously sqlAutonomously = SQLAutonomously.newInstance(
                SQLAutonomously.select()
                        .field("t", TableUser.id)
                        .field("t", TableUser.userName)
                        .count(new FieldItem("t", TableUser.id))
                        .from().table(TableUser.class, "t")
                        .left().join().table(TableUser.class, "t1").on().column("t1", TableUser.id).eq().column("t", TableUser.id)
                        .where()
                        .column("t", TableUser.id).eq().value(1)
                        .groupBy()
                        .column("t1", TableUser.id)
                        .column("t1", TableUser.age)
                        .column("t1", TableUser.createdTime)
                        .orderBy()
                        .column("t1", TableUser.id).desc()
                        .column("t1", TableUser.createdTime).asc()
                        .limit(0, 10)
        );
        AutoResult autoResult = template.getAutonomously(sqlAutonomously);
        System.out.println(autoResult.getValue());
    }

    @Test
    public void testUpdate1() throws Exception {
        SQLAutonomously sqlAutonomously = SQLAutonomously.newInstance(
                SQLAutonomously.update().table(TableUser.class)
                        .set()
                        .column(TableUser.address).eq().value("b")
                        .column(TableUser.createdTime).eq().value("2019-01-01 10:00:00")
                        .where().column(TableUser.id).eq().value(1)
        );
        AutoResult autoResult = template.getAutonomously(sqlAutonomously);
        System.out.println(autoResult.getValue());
    }

    @Test
    public void testUpdate2() throws Exception {
        SQLAutonomously sqlAutonomously = SQLAutonomously.newInstance(
                SQLAutonomously.update()
                        .table(TableUser.class, "t1")
                        .table(TableUser.class, "t2")
                        .set()
                        .column("t1", TableUser.address).eq().value("b")
                        .column("t1", TableUser.createdTime).eq().value("2019-01-01 10:00:00")
                        .where().column("t1", TableUser.id).eq().value(1)
        );
        AutoResult autoResult = template.getAutonomously(sqlAutonomously);
        System.out.println(autoResult.getValue());
    }

    @Test
    public void t() {
        Object i = 1;
        System.out.println(i instanceof Number);
    }
}
