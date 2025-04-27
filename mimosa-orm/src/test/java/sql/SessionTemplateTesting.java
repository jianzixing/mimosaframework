package sql;

import org.junit.Before;
import org.junit.Test;
import org.mimosaframework.core.json.ModelObject;
import org.mimosaframework.orm.*;
import org.mimosaframework.orm.exception.ContextException;

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
    public void testEmpty() {
        System.out.println("");
    }

    @Test
    public void testSql1() throws Exception {
        ModelObject params = new ModelObject();
        params.put("id", 1);
        Sql sqlAutonomously = Sql.select("select * from t_cms where project_id=#{projectId} and template_id=#{templateId} " +
                                         "and del=0 and name = #{name} json->$.age = #{jsonage} order by top,publish_time desc limit #{from},#{size}", params);
        AutoResult autoResult = template.sql(sqlAutonomously);
        System.out.println(ModelObject.toJSONString(autoResult.getObjects()));
    }

//    @Test
//    public void testAlter1() throws Exception {
//        SQLAutonomously sqlAutonomously = new SQLAutonomously(
//                AlterFactory.alter().table(TableUser.class)
//                        .modify().column(TableUser.id).bigint().autoIncrement().comment("aaa")
//        );
//        AutoResult autoResult = template.getAutonomously(sqlAutonomously);
//    }
//
//    @Test
//    public void testAlter3() throws Exception {
//        SQLAutonomously sqlAutonomously = new SQLAutonomously(
//                AlterFactory.alter().table(TableUser.class)
//                        .add().column("t1").intType().comment("t+t1").after().column(TableUser.id)
//        );
//        AutoResult autoResult = template.getAutonomously(sqlAutonomously);
//    }
//
//    @Test
//    public void testAlter4() throws Exception {
//        SQLAutonomously sqlAutonomously = new SQLAutonomously(
//                AlterFactory.alter().table(TableUser.class)
//                        .drop().column("t1")
//        );
//        AutoResult autoResult = template.getAutonomously(sqlAutonomously);
//    }
//
//    @Test
//    public void testAlter5() throws Exception {
//        SQLAutonomously sqlAutonomously = new SQLAutonomously(
//                AlterFactory.alter().table(TableUser.class)
//                        .autoIncrement().value(20)
//        );
//        AutoResult autoResult = template.getAutonomously(sqlAutonomously);
//    }
//
//    @Test
//    public void testAlter6() throws Exception {
//        SQLAutonomously sqlAutonomously = new SQLAutonomously(
//                AlterFactory.alter().table(TableUser.class)
//                        .charset("utf8")
//        );
//        AutoResult autoResult = template.getAutonomously(sqlAutonomously);
//    }

//    @Test
//    public void testAlter11() throws Exception {
//        SQLAutonomously sqlAutonomously;
//        template.getAutonomously(AlterFactory.alter().table(TableUser.class)
//                .add().primary().key().column(TableUser.id).autonomously());
//
//        sqlAutonomously = new SQLAutonomously(
//                SQLAutonomously.alter().table(TableUser.class)
//                        .add().index().name(TableUser.id + "2").column(TableUser.id)
//        );
//        template.getAutonomously(sqlAutonomously);
//
//        template.getAutonomously(AlterFactory.alter().table(TableUser.class)
//                .drop().primary().key().autonomously());
//
//        sqlAutonomously = new SQLAutonomously(
//                SQLAutonomously.alter().table(TableUser.class)
//                        .drop().index().name(TableUser.id + "2")
//        );
//        template.getAutonomously(sqlAutonomously);
//    }

//    @Test
//    public void testAlter12() throws Exception {
//        SQLAutonomously sqlAutonomously = new SQLAutonomously(
//                AlterFactory.alter().database().name("mimosa").charset("utf8")
//        );
//        AutoResult autoResult = template.getAutonomously(sqlAutonomously);
//    }

//    @Test
//    public void testCreate1() throws Exception {
//        SQLAutonomously sqlAutonomously = SQLAutonomously.newInstance(
//                CreateFactory.create().table().ifNotExist().name("t_tt")
//                        .column("id").intType().autoIncrement().primary().key().comment("a")
//                        .column("name").varchar(50).not().nullable().comment("b")
//                        .tableComment("ttt")
//                        .charset("utf8")
//        );
//        AutoResult autoResult = template.getAutonomously(sqlAutonomously);
//        System.out.println(autoResult.getValue());
//    }

//    @Test
//    public void testCreate2() throws Exception {
//        SQLAutonomously sqlAutonomously = SQLAutonomously.newInstance(
//                CreateFactory.create().table().ifNotExist().name(TableUser.class)
//                        .column("id").intType().autoIncrement().primary().key().comment("a")
//                        .column("name").varchar(50).not().nullable().comment("b")
//                        .column(TableUser.createdTime).datetime().not().nullable().comment("b")
//                        .charset("utf8")
//        );
//        AutoResult autoResult = template.getAutonomously(sqlAutonomously);
//        System.out.println(autoResult.getValue());
//    }

//    @Test
//    public void testCreate3() throws Exception {
//        SQLAutonomously sqlAutonomously = SQLAutonomously.newInstance(
//                CreateFactory.create().table().ifNotExist().name("t_user_2")
//                        .column("id").intType().autoIncrement().primary().key().comment("a")
//                        .column("name").varchar(50).not().nullable().comment("b")
//                        .column(TableUser.userName).varchar(50).not().nullable().comment("b")
//                        .column(TableUser.createdTime).datetime().not().nullable().comment("b")
//                        .charset("utf8")
//        );
//        AutoResult autoResult = template.getAutonomously(sqlAutonomously);
//        System.out.println(autoResult.getValue());
//    }

//    @Test
//    public void testCreate4() throws Exception {
//        SQLAutonomously sqlAutonomously = SQLAutonomously.newInstance(
//                CreateFactory.create().database().name("bb").charset("utf8")
//        );
//        AutoResult autoResult = template.getAutonomously(sqlAutonomously);
//
//        sqlAutonomously = SQLAutonomously.newInstance(
//                DropFactory.drop().database().name("bb")
//        );
//        template.getAutonomously(sqlAutonomously);
//
//    }

//    @Test
//    public void testCreate5() throws Exception {
//        SQLAutonomously sqlAutonomously = SQLAutonomously.newInstance(
//                CreateFactory.create().index().name("bb").on()
//                        .table(TableUser.class).columns(TableUser.address)
//        );
//        AutoResult autoResult = template.getAutonomously(sqlAutonomously);
//
//        sqlAutonomously = SQLAutonomously.newInstance(
//                DropFactory.drop().index().name("bb").on().table(TableUser.class)
//        );
//        template.getAutonomously(sqlAutonomously);
//
//    }

//    @Test
//    public void testDelete1() throws Exception {
//        SQLAutonomously sqlAutonomously = SQLAutonomously.newInstance(
//                SQLAutonomously.delete().from().table(TableUser.class)
//                        .where().column(TableUser.id).eq().value(1)
//        );
//        AutoResult autoResult = template.getAutonomously(sqlAutonomously);
//        System.out.println(autoResult.getValue());
//    }

//    @Test
//    public void testDelete2() throws Exception {
//        SQLAutonomously sqlAutonomously = SQLAutonomously.newInstance(
//                SQLAutonomously.delete().from()
//                        .table(TablePay.class)
//                        .where()
//                        .column("t1", TableUser.id).eq().value(1)
//                        .and()
//                        .column("t1", TableUser.id).eq().value(1)
//                        .or()
//                        .wrapper(Wrapper.build()
//                                .column("t1", TableUser.id).eq().value(1)
//                                .and()
//                                .isNotNull("t1", TableUser.id)
//                                .and()
//                                .isNull("t1", TableUser.id)
//                        )
//                        .and()
//                        .isNull("t1", TableUser.id)
//                        .and()
//                        .isNotNull("t1", TableUser.id)
//
//        );
//        AutoResult autoResult = template.getAutonomously(sqlAutonomously);
//        System.out.println(autoResult.getValue());
//    }

//    @Test
//    public void testDelete3() throws Exception {
//        SQLAutonomously sqlAutonomously = SQLAutonomously.newInstance(
//                SQLAutonomously.delete().from()
//                        .table(TableUser.class)
//                        .where()
//                        .column("t1", TableUser.id)
//                        .eq()
//                        .value(1)
//                        .and()
//                        .column("t1", TableUser.id).eq().value(1)
//        );
//        AutoResult autoResult = template.getAutonomously(sqlAutonomously);
//        System.out.println(autoResult.getValue());
//    }

//    @Test
//    public void testDelete4() throws Exception {
//        SQLAutonomously sqlAutonomously = SQLAutonomously.newInstance(
//                SQLAutonomously.delete().from()
//                        .table(TableUser.class)
//                        .where()
//                        .column(TableUser.class, TableUser.id).eq().value(1)
//                        .and()
//                        .column(TableUser.class, TableUser.id).eq().column(TableUser.age)
//
//        );
//        AutoResult autoResult = template.getAutonomously(sqlAutonomously);
//        System.out.println(autoResult.getValue());
//    }


//    @Test
//    public void testDrop1() throws Exception {
//        AutoResult autoResult = template.getAutonomously(DropFactory.drop().table().table(TableUser.class).autonomously());
//        System.out.println(autoResult.getValue());
//    }

//    @Test
//    public void testInsert1() throws Exception {
//        SQLAutonomously sqlAutonomously = SQLAutonomously.newInstance(
//                SQLAutonomously.insert().into().table(TableUser.class)
//                        .columns(TableUser.userName, TableUser.createdTime)
//                        .values()
//                        .row("a", new Date())
//        );
//        AutoResult autoResult = template.getAutonomously(sqlAutonomously);
//        System.out.println(autoResult.getValue());
//    }

//    @Test
//    public void testInsert2() throws Exception {
//        SQLAutonomously sqlAutonomously = SQLAutonomously.newInstance(
//                SQLAutonomously.insert().into().table(TableUser.class)
//                        .columns(TableUser.userName, TableUser.createdTime)
//                        .values()
//                        .row("a1", new Date())
//                        .row("a2", new Date())
//                        .row("a3", new Date())
//        );
//        AutoResult autoResult = template.getAutonomously(sqlAutonomously);
//        System.out.println(autoResult.getValue());
//    }

//    @Test
//    public void testSelect1() throws Exception {
//        SQLAutonomously sqlAutonomously = SQLAutonomously.newInstance(
//                SQLAutonomously.select()
//                        .count(new FieldItem("t", TableUser.id)).as("c")
//                        .from().table(TableUser.class, "t")
//                        .left().join().table(TableUser.class, "t1").on().column("t1", TableUser.id).eq().column("t", TableUser.id)
//                        .inner().join().table(TableUser.class, "t2").on().column("t2", TableUser.id).eq().column("t", TableUser.id)
//                        .where()
//                        .column("t", TableUser.id).eq().value(1)
//                        .groupBy()
//                        .column("t1", TableUser.id)
//                        .column("t1", TableUser.age)
//                        .column("t1", TableUser.createdTime)
//                        .orderBy()
//                        .column("t1", TableUser.id).desc()
//                        .column("t1", TableUser.createdTime).asc()
//                        .limit(0, 10)
//        );
//        AutoResult autoResult = template.getAutonomously(sqlAutonomously);
//        System.out.println(autoResult.getValue());
//    }

//    @Test
//    public void testSelect2() throws Exception {
//        template.getAutonomously(
//                SQLAutonomously.select()
//                        .fields(TableUser.id)
//                        .from()
//                        .table(TableUser.class)
//                        .where()
//                        .column(TableUser.id).eq().value(1)
//                        .groupBy().column(TableUser.id)
//                        .having()
//                        .count("distinct", TableUser.id).gt().value(10)
//                        .autonomously()
//        );
//    }

//    @Test
//    public void testUpdate1() throws Exception {
//        SQLAutonomously sqlAutonomously = SQLAutonomously.newInstance(
//                SQLAutonomously.update()
//                        .table(TableUser.class)
//                        .set(TableUser.address, "b")
//                        .set(TableUser.createdTime, new Date())
//                        .where().column(TableUser.id).eq().value(1)
//        );
//        AutoResult autoResult = template.getAutonomously(sqlAutonomously);
//        System.out.println(autoResult.getValue());
//    }

//    @Test
//    public void tableStructure() throws Exception {
//        AutoResult result = template.getAutonomously(SQLAutonomously.structure().table("mimosa").autonomously());
//        List<String> tables = result.getStrings("TABNAME");
//        List<ModelObject> ob1 = result.getObjects();
//        if (ob1 != null) {
//            for (ModelObject o : ob1) System.out.println("TABLE  " + o);
//        }
//        System.out.println();
//
//        result = template.getAutonomously(SQLAutonomously.structure().column(tables).autonomously());
//        List<ModelObject> ob2 = result.getObjects();
//        if (ob2 != null) {
//            for (ModelObject o : ob2) System.out.println("COLUMN  " + o);
//        }
//        System.out.println();
//
//        result = template.getAutonomously(SQLAutonomously.structure().index(tables).autonomously());
//        List<ModelObject> ob3 = result.getObjects();
//        if (ob3 != null) {
//            for (ModelObject o : ob3) System.out.println("INDEX  " + o);
//        }
//
//        result = template.getAutonomously(SQLAutonomously.structure().constraint(tables).autonomously());
//        List<ModelObject> ob4 = result.getObjects();
//        if (ob4 != null) {
//            for (ModelObject o : ob4) System.out.println("CONSTRAINT  " + o);
//        }
//    }
}
