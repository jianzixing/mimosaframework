package sql;

import org.junit.Before;
import org.junit.Test;
import org.mimosaframework.orm.*;
import org.mimosaframework.orm.exception.ContextException;
import org.mimosaframework.orm.sql.FieldItem;
import org.mimosaframework.orm.sql.Wrapper;
import tables.TablePay;
import tables.TableUser;

public class SessionTemplateTesting {
    private static SessionTemplate template = null;

    @Before
    public void init() throws ContextException {
        if (template == null) {
            String config = "/template-mimosa.xml";
            if (1 == 1) {
                config = "/oracle-template-mimosa.xml";
            }
            XmlAppContext context = new XmlAppContext(SessionFactoryBuilder.class.getResourceAsStream(config));
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
                        .drop().index().name("b")
        );
        AutoResult autoResult = template.getAutonomously(sqlAutonomously);

        sqlAutonomously = new SQLAutonomously(
                SQLAutonomously.alter().table(TableUser.class)
                        .add().fullText().index().name("b").columns(TableUser.address, TableUser.userName)
                        .comment("aaa")
        );
        autoResult = template.getAutonomously(sqlAutonomously);

        sqlAutonomously = new SQLAutonomously(
                SQLAutonomously.alter().table(TableUser.class)
                        .drop().index().name("b")
        );
        autoResult = template.getAutonomously(sqlAutonomously);
    }


    @Test
    public void testAlter9() throws Exception {
        SQLAutonomously sqlAutonomously = new SQLAutonomously(
                SQLAutonomously.alter().table(TableUser.class)
                        .add().unique().name("c").columns(TableUser.address, TableUser.userName)
                        .comment("aaa")
        );
        AutoResult autoResult = template.getAutonomously(sqlAutonomously);

        sqlAutonomously = new SQLAutonomously(
                SQLAutonomously.alter().table(TableUser.class)
                        .drop().index().name("c")
        );
        autoResult = template.getAutonomously(sqlAutonomously);
    }

    @Test
    public void testAlter10() throws Exception {
        SQLAutonomously sqlAutonomously = new SQLAutonomously(
                SQLAutonomously.alter().table(TableUser.class)
                        .add().index().name("pk").column(TableUser.id)
        );
        AutoResult autoResult = template.getAutonomously(sqlAutonomously);

        sqlAutonomously = new SQLAutonomously(
                SQLAutonomously.alter().table(TableUser.class)
                        .drop().index().name("pk")
        );
        template.getAutonomously(sqlAutonomously);
    }

    @Test
    public void testAlter11() throws Exception {
        SQLAutonomously sqlAutonomously;
        sqlAutonomously = new SQLAutonomously(
                SQLAutonomously.alter().table(TableUser.class)
                        .add().primary().key().column(TableUser.id)
        );
        AutoResult autoResult = template.getAutonomously(sqlAutonomously);

        sqlAutonomously = new SQLAutonomously(
                SQLAutonomously.alter().table(TableUser.class)
                        .add().index().name(TableUser.id + "2").column(TableUser.id)
        );
        template.getAutonomously(sqlAutonomously);

        sqlAutonomously = new SQLAutonomously(
                SQLAutonomously.alter().table(TableUser.class)
                        .drop().primary().key()
        );
        template.getAutonomously(sqlAutonomously);

        sqlAutonomously = new SQLAutonomously(
                SQLAutonomously.alter().table(TableUser.class)
                        .drop().index().name(TableUser.id + "2")
        );
        template.getAutonomously(sqlAutonomously);
    }

    @Test
    public void testAlter12() throws Exception {
        SQLAutonomously sqlAutonomously = new SQLAutonomously(
                SQLAutonomously.alter().database().name("mimosa").charset("utf8")
        );
        AutoResult autoResult = template.getAutonomously(sqlAutonomously);
    }

    @Test
    public void testCreate1() throws Exception {
        SQLAutonomously sqlAutonomously = SQLAutonomously.newInstance(
                SQLAutonomously.create().table().ifNotExist().name("t_tt")
                        .column("id").intType().autoIncrement().primary().key().comment("a")
                        .column("name").varchar(50).not().nullable().comment("b")
                        .charset("utf8")
        );
        AutoResult autoResult = template.getAutonomously(sqlAutonomously);
        System.out.println(autoResult.getValue());
    }

    @Test
    public void testCreate2() throws Exception {
        SQLAutonomously sqlAutonomously = SQLAutonomously.newInstance(
                SQLAutonomously.create().table().ifNotExist().name(TableUser.class)
                        .column("id").intType().autoIncrement().primary().key().comment("a")
                        .column("name").varchar(50).not().nullable().comment("b")
                        .column(TableUser.createdTime).datetime().not().nullable().comment("b")
                        .charset("utf8")
        );
        AutoResult autoResult = template.getAutonomously(sqlAutonomously);
        System.out.println(autoResult.getValue());
    }

    @Test
    public void testCreate3() throws Exception {
        SQLAutonomously sqlAutonomously = SQLAutonomously.newInstance(
                SQLAutonomously.create().table().ifNotExist().name("t_user_2")
                        .column("id").intType().autoIncrement().primary().key().comment("a")
                        .column("name").varchar(50).not().nullable().comment("b")
                        .column(TableUser.userName).varchar(50).not().nullable().unique().comment("b")
                        .column(TableUser.createdTime).datetime().not().nullable().comment("b")
                        .charset("utf8")
        );
        AutoResult autoResult = template.getAutonomously(sqlAutonomously);
        System.out.println(autoResult.getValue());
    }

    @Test
    public void testCreate4() throws Exception {
        SQLAutonomously sqlAutonomously = SQLAutonomously.newInstance(
                SQLAutonomously.create().database().name("bb")
        );
        AutoResult autoResult = template.getAutonomously(sqlAutonomously);

        sqlAutonomously = SQLAutonomously.newInstance(
                SQLAutonomously.drop().database().name("bb")
        );
        template.getAutonomously(sqlAutonomously);

    }

    @Test
    public void testCreate5() throws Exception {
        SQLAutonomously sqlAutonomously = SQLAutonomously.newInstance(
                SQLAutonomously.create().fullText().index().name("bb").on()
                        .table(TableUser.class).columns(TableUser.id)
        );
        AutoResult autoResult = template.getAutonomously(sqlAutonomously);

        sqlAutonomously = SQLAutonomously.newInstance(
                SQLAutonomously.drop().index().name("bb").on().table(TableUser.class)
        );
        template.getAutonomously(sqlAutonomously);

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
    public void testDelete2() throws Exception {
        SQLAutonomously sqlAutonomously = SQLAutonomously.newInstance(
                SQLAutonomously.delete().table("t1").from()
                        .table(TableUser.class, "t1")
                        .table(TablePay.class, "t2")
                        .where()
                        .column("t1", TableUser.id).eq().value(1)
                        .and()
                        .column("t1", TableUser.id).eq().column("t2", TablePay.userId)
        );
        AutoResult autoResult = template.getAutonomously(sqlAutonomously);
        System.out.println(autoResult.getValue());
    }

    @Test
    public void testDelete3() throws Exception {
        SQLAutonomously sqlAutonomously = SQLAutonomously.newInstance(
                SQLAutonomously.delete().table("t1").from()
                        .table(TableUser.class, "t1")
                        .table(TablePay.class, "t2")
                        .where()
                        .column("t1", TableUser.id)
                        .eq()
                        .value(1)
                        .and()
                        .column("t1", TableUser.id).eq().column("t2", TablePay.userId)
                        .or()
                        .wrapper(Wrapper.build()
                                .column("t1", TableUser.id)
                                .eq()
                                .column("t2", TablePay.userId)
                                .and()
                                .isNotNull("t1", TableUser.id)
                                .and()
                                .isNull("t1", TableUser.id)
                        )
                        .and()
                        .isNull("t1", TableUser.id)
                        .and()
                        .isNotNull("t1", TableUser.id)

        );
        AutoResult autoResult = template.getAutonomously(sqlAutonomously);
        System.out.println(autoResult.getValue());
    }

    @Test
    public void testDelete4() throws Exception {
        SQLAutonomously sqlAutonomously = SQLAutonomously.newInstance(
                SQLAutonomously.delete().from()
                        .table(TableUser.class)
                        .where()
                        .column("t1", TableUser.id)
                        .eq()
                        .value(1)
                        .and()
                        .column("t1", TableUser.id).eq().value(1)
                        .orderBy()
                        .column("t1", TableUser.id).asc()
                        .column("t1", TableUser.age).desc()
                        .limit(10)

        );
        AutoResult autoResult = template.getAutonomously(sqlAutonomously);
        System.out.println(autoResult.getValue());
    }

    @Test
    public void testDelete5() throws Exception {
        SQLAutonomously sqlAutonomously = SQLAutonomously.newInstance(
                SQLAutonomously.delete().from()
                        .table(TableUser.class)
                        .using()
                        .table(TablePay.class, "t2")
                        .where()
                        .column(TableUser.class, TableUser.id).eq().value(1)
                        .and()
                        .column(TableUser.class, TableUser.id).eq().column("t2", TablePay.userId)

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
    public void testInsert2() throws Exception {
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
                        .count(new FieldItem("t", TableUser.id)).as("c")
                        .from().table(TableUser.class, "t")
                        .left().join().table(TableUser.class, "t1").on().column("t1", TableUser.id).eq().column("t", TableUser.id)
                        .inner().join().table(TableUser.class, "t2").on().column("t2", TableUser.id).eq().column("t", TableUser.id)
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
    public void testSelect2() throws Exception {
        template.getAutonomously(
                SQLAutonomously.select()
                        .all()
                        .from()
                        .table(TableUser.class)
                        .where()
                        .column("t", TableUser.id).eq().value(1)
                        .having()
                        .count("distinct", TableUser.id).gt().value(10)
                        .and()
                        .max(new FieldItem(TableUser.id)).ne().column("t", TableUser.age)
                        .autonomously()
        );
    }

    @Test
    public void testUpdate1() throws Exception {
        SQLAutonomously sqlAutonomously = SQLAutonomously.newInstance(
                SQLAutonomously.update().table(TableUser.class)
                        .set()
                        .column(TableUser.address).eq().value("b")
                        .column(TableUser.createdTime).eq().value("2019-01-01 10:00:00")
                        .where().column(TableUser.id).eq().value(1)
                        .orderBy()
                        .column(TableUser.id).desc()
                        .column(TableUser.age).asc()
                        .limit(20)
        );
        AutoResult autoResult = template.getAutonomously(sqlAutonomously);
        System.out.println(autoResult.getValue());
    }

    @Test
    public void testUpdate2() throws Exception {
        SQLAutonomously sqlAutonomously = SQLAutonomously.newInstance(
                SQLAutonomously.update()
                        .table(TableUser.class, "t1")
                        .using()
                        .table(TableUser.class, "t2")
                        .set()
                        .column(TableUser.address).eq().value("b")
                        .column(TableUser.createdTime).eq().value("2019-01-01 10:00:00")
                        .where().column("t1", TableUser.id).eq().value(1)
        );
        AutoResult autoResult = template.getAutonomously(sqlAutonomously);
        System.out.println(autoResult.getValue());
    }
}
