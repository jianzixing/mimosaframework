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
        SQLAutonomously sqlAutonomously = new SQLAutonomously(
                SQLAutonomously.alter().table(TableUser.class)
                        .modify().column(TableUser.createdTime).datetime().comment("aaa")
        );
        AutoResult autoResult = template.getAutonomously(sqlAutonomously);
    }

    @Test
    public void testAlter2() throws Exception {
        SQLAutonomously sqlAutonomously = new SQLAutonomously(
                SQLAutonomously.alter().table(TableUser.class)
                        .change().oldColumn(TableUser.createdTime)
                        .newColumn(TableUser.createdTime).datetime().comment("aaa")
        );
        AutoResult autoResult = template.getAutonomously(sqlAutonomously);
    }

    @Test
    public void testAlter3() throws Exception {
        SQLAutonomously sqlAutonomously = new SQLAutonomously(
                SQLAutonomously.alter().table(TableUser.class)
                        .add().column("t1").intType().unique().comment("t+t1")
        );
        AutoResult autoResult = template.getAutonomously(sqlAutonomously);
    }

    @Test
    public void testAlter4() throws Exception {
        SQLAutonomously sqlAutonomously = new SQLAutonomously(
                SQLAutonomously.alter().table(TableUser.class)
                        .drop().column("t1")
        );
        AutoResult autoResult = template.getAutonomously(sqlAutonomously);
    }

    @Test
    public void testAlter5() throws Exception {
        SQLAutonomously sqlAutonomously = new SQLAutonomously(
                SQLAutonomously.alter().table(TableUser.class)
                        .autoIncrement().value(20)
        );
        AutoResult autoResult = template.getAutonomously(sqlAutonomously);
    }

    @Test
    public void testAlter6() throws Exception {
        SQLAutonomously sqlAutonomously = new SQLAutonomously(
                SQLAutonomously.alter().table(TableUser.class)
                        .charset("utf8")
        );
        AutoResult autoResult = template.getAutonomously(sqlAutonomously);
    }

    @Test
    public void testAlter7() throws Exception {
        SQLAutonomously sqlAutonomously = new SQLAutonomously(
                SQLAutonomously.alter().table(TableUser.class)
                        .add().index().name("a").columns(TableUser.address, TableUser.age)
                        .comment("aaa")
        );
        AutoResult autoResult = template.getAutonomously(sqlAutonomously);
    }

    @Test
    public void testAlter8() throws Exception {
        SQLAutonomously sqlAutonomously = new SQLAutonomously(
                SQLAutonomously.alter().table(TableUser.class)
                        .add().fullText().index().name("b").columns(TableUser.address, TableUser.userName)
                        .comment("aaa")
        );
        AutoResult autoResult = template.getAutonomously(sqlAutonomously);
    }


    @Test
    public void testAlter9() throws Exception {
        SQLAutonomously sqlAutonomously = new SQLAutonomously(
                SQLAutonomously.alter().table(TableUser.class)
                        .add().unique().name("c").columns(TableUser.address, TableUser.userName)
                        .comment("aaa")
        );
        AutoResult autoResult = template.getAutonomously(sqlAutonomously);
    }

    @Test
    public void testAlter10() throws Exception {
        SQLAutonomously sqlAutonomously = new SQLAutonomously(
                SQLAutonomously.alter().table(TableUser.class)
                        .add().index().name("pk").column(TableUser.id)
        );
        AutoResult autoResult = template.getAutonomously(sqlAutonomously);
    }

    @Test
    public void testAlter11() throws Exception {
        SQLAutonomously sqlAutonomously = new SQLAutonomously(
                SQLAutonomously.alter().table(TableUser.class)
                        .drop().primary().key()
        );
        AutoResult autoResult = template.getAutonomously(sqlAutonomously);
    }

    @Test
    public void testAlter12() throws Exception {
        SQLAutonomously sqlAutonomously = new SQLAutonomously(
                SQLAutonomously.alter().table(TableUser.class)
                        .add().primary().key().column(TableUser.id)
        );
        AutoResult autoResult = template.getAutonomously(sqlAutonomously);
    }

    @Test
    public void testAlter13() throws Exception {
        SQLAutonomously sqlAutonomously = new SQLAutonomously(
                SQLAutonomously.alter().database().name("mimosa").charset("utf8")
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
