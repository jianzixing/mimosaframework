package sql;

import org.junit.Before;
import org.junit.Test;
import org.mimosaframework.core.json.ModelObject;
import org.mimosaframework.orm.*;
import org.mimosaframework.orm.exception.ContextException;
import org.mimosaframework.orm.sql.FieldItem;
import org.mimosaframework.orm.sql.Wrapper;
import tables.TablePay;
import tables.TableUser;

import java.util.Date;
import java.util.List;

public class SessionTemplateTesting {
    private static SessionTemplate template = null;

    @Before
    public void init() throws ContextException {
        if (template == null) {
            String config = "/template-mimosa.xml";
            if (1 == 2) {
                config = "/db2-template-mimosa.xml";
            }
            if (1 == 2) {
                config = "/oracle-template-mimosa.xml";
            }
            if (1 == 2) {
                config = "/sqlserver-template-mimosa.xml";
            }
            if (1 == 2) {
                config = "/postgresql-template-mimosa.xml";
            }
            if (1 == 2) {
                config = "/sqlite-template-mimosa.xml";
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
                        .modify().column(TableUser.id).bigint().autoIncrement().comment("aaa")
        );
        AutoResult autoResult = template.getAutonomously(sqlAutonomously);
    }

    @Test
    public void testAlter2() throws Exception {
        template.getAutonomously(SQLAutonomously.alter().table(TableUser.class)
                .change().oldColumn(TableUser.createdTime)
                .newColumn("created_time2").datetime().comment("aaa").autonomously());

        template.getAutonomously(SQLAutonomously.alter().table(TableUser.class)
                .rename().column().oldColumn("created_time2").to().newColumn("created_time").autonomously());
    }

    @Test
    public void testAlter3() throws Exception {
        SQLAutonomously sqlAutonomously = new SQLAutonomously(
                SQLAutonomously.alter().table(TableUser.class)
                        .add().column("t1").intType().comment("t+t1").after().column(TableUser.id)
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
        template.getAutonomously(SQLAutonomously.alter().table(TableUser.class)
                .add().index().name("a").columns(TableUser.address, TableUser.age)
                .comment("aaa").autonomously());
        template.getAutonomously(SQLAutonomously.alter().table(TableUser.class)
                .drop().index().name("a").autonomously());
    }

    @Test
    public void testAlter8() throws Exception {
        SQLAutonomously sqlAutonomously = new SQLAutonomously(
                SQLAutonomously.alter().table(TableUser.class)
                        .add().index().name("b").columns(TableUser.address, TableUser.userName)
                        .comment("aaa")
        );
        AutoResult autoResult = template.getAutonomously(sqlAutonomously);

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
//        SQLAutonomously sqlAutonomously;
        template.getAutonomously(SQLAutonomously.alter().table(TableUser.class)
                .add().primary().key().column(TableUser.id).autonomously());

//        sqlAutonomously = new SQLAutonomously(
//                SQLAutonomously.alter().table(TableUser.class)
//                        .add().index().name(TableUser.id + "2").column(TableUser.id)
//        );
//        template.getAutonomously(sqlAutonomously);

        template.getAutonomously(SQLAutonomously.alter().table(TableUser.class)
                .drop().primary().key().autonomously());

//        sqlAutonomously = new SQLAutonomously(
//                SQLAutonomously.alter().table(TableUser.class)
//                        .drop().index().name(TableUser.id + "2")
//        );
//        template.getAutonomously(sqlAutonomously);
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
                        .tableComment("ttt")
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
                        .column(TableUser.userName).varchar(50).not().nullable().comment("b")
                        .column(TableUser.createdTime).datetime().not().nullable().comment("b")
                        .charset("utf8")
        );
        AutoResult autoResult = template.getAutonomously(sqlAutonomously);
        System.out.println(autoResult.getValue());
    }

    @Test
    public void testCreate4() throws Exception {
        SQLAutonomously sqlAutonomously = SQLAutonomously.newInstance(
                SQLAutonomously.create().database().name("bb").charset("utf8")
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
                SQLAutonomously.create().index().name("bb").on()
                        .table(TableUser.class).columns(TableUser.address)
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
                SQLAutonomously.delete().from().table(TableUser.class)
                        .where().column(TableUser.id).eq().value(1)
        );
        AutoResult autoResult = template.getAutonomously(sqlAutonomously);
        System.out.println(autoResult.getValue());
    }

    @Test
    public void testDelete2() throws Exception {
        SQLAutonomously sqlAutonomously = SQLAutonomously.newInstance(
                SQLAutonomously.delete().from()
                        .table(TablePay.class)
                        .where()
                        .column("t1", TableUser.id).eq().value(1)
                        .and()
                        .column("t1", TableUser.id).eq().value(1)
                        .or()
                        .wrapper(Wrapper.build()
                                .column("t1", TableUser.id).eq().value(1)
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
    public void testDelete3() throws Exception {
        SQLAutonomously sqlAutonomously = SQLAutonomously.newInstance(
                SQLAutonomously.delete().from()
                        .table(TableUser.class)
                        .where()
                        .column("t1", TableUser.id)
                        .eq()
                        .value(1)
                        .and()
                        .column("t1", TableUser.id).eq().value(1)
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
                        .column(TableUser.class, TableUser.id).eq().value(1)
                        .and()
                        .column(TableUser.class, TableUser.id).eq().column(TableUser.age)

        );
        AutoResult autoResult = template.getAutonomously(sqlAutonomously);
        System.out.println(autoResult.getValue());
    }


    @Test
    public void testDrop1() throws Exception {
        AutoResult autoResult = template.getAutonomously(SQLAutonomously.drop().table().table(TableUser.class).autonomously());
        System.out.println(autoResult.getValue());
    }

    @Test
    public void testInsert1() throws Exception {
        SQLAutonomously sqlAutonomously = SQLAutonomously.newInstance(
                SQLAutonomously.insert().into().table(TableUser.class)
                        .columns(TableUser.userName, TableUser.createdTime)
                        .values()
                        .row("a", new Date())
        );
        AutoResult autoResult = template.getAutonomously(sqlAutonomously);
        System.out.println(autoResult.getValue());
    }

    @Test
    public void testInsert2() throws Exception {
        SQLAutonomously sqlAutonomously = SQLAutonomously.newInstance(
                SQLAutonomously.insert().into().table(TableUser.class)
                        .columns(TableUser.userName, TableUser.createdTime)
                        .values()
                        .row("a1", new Date())
                        .row("a2", new Date())
                        .row("a3", new Date())
        );
        AutoResult autoResult = template.getAutonomously(sqlAutonomously);
        System.out.println(autoResult.getValue());
    }

    @Test
    public void testSelect1() throws Exception {
        SQLAutonomously sqlAutonomously = SQLAutonomously.newInstance(
                SQLAutonomously.select()
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
                        .field(TableUser.id)
                        .from()
                        .table(TableUser.class)
                        .where()
                        .column(TableUser.id).eq().value(1)
                        .groupBy().column(TableUser.id)
                        .having()
                        .count("distinct", TableUser.id).gt().value(10)
                        .autonomously()
        );
    }

    @Test
    public void testUpdate1() throws Exception {
        SQLAutonomously sqlAutonomously = SQLAutonomously.newInstance(
                SQLAutonomously.update()
                        .table(TableUser.class)
                        .set()
                        .column(TableUser.address).eq().value("b")
                        .column(TableUser.createdTime).eq().value(new Date())
                        .where().column(TableUser.id).eq().value(1)
        );
        AutoResult autoResult = template.getAutonomously(sqlAutonomously);
        System.out.println(autoResult.getValue());
    }

    @Test
    public void tableStructure() throws Exception {
        AutoResult result = template.getAutonomously(SQLAutonomously.structure().table("mimosa").autonomously());
        List<String> tables = result.getStrings("TABNAME");
        List<ModelObject> ob1 = result.getObjects();
        if (ob1 != null) {
            for (ModelObject o : ob1) System.out.println("TABLE  " + o);
        }
        System.out.println();

        result = template.getAutonomously(SQLAutonomously.structure().column(tables).autonomously());
        List<ModelObject> ob2 = result.getObjects();
        if (ob2 != null) {
            for (ModelObject o : ob2) System.out.println("COLUMN  " + o);
        }
        System.out.println();

        result = template.getAutonomously(SQLAutonomously.structure().index(tables).autonomously());
        List<ModelObject> ob3 = result.getObjects();
        if (ob3 != null) {
            for (ModelObject o : ob3) System.out.println("INDEX  " + o);
        }
    }
}
