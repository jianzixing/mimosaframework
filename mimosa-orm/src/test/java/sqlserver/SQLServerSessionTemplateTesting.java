package sqlserver;

import org.junit.Before;
import org.junit.Test;
import org.mimosaframework.core.json.ModelObject;
import org.mimosaframework.core.utils.AssistUtils;
import org.mimosaframework.orm.*;
import org.mimosaframework.orm.criteria.Criteria;
import org.mimosaframework.orm.exception.ContextException;
import program.service.SessionTemplateServiceTesting;
import tables.TableMultiKey1;
import tables.TableMultiKey2;
import tables.TableOrder;
import tables.TableUser;

import java.sql.SQLException;
import java.util.Date;

public class SQLServerSessionTemplateTesting {
    private SessionTemplate template = null;

    @Before
    public void init() throws ContextException {
        XmlAppContext context = new XmlAppContext(SessionFactoryBuilder.class.getResourceAsStream("/sqlserver-template-mimosa.xml"));
        SessionFactory sessionFactory = context.getSessionFactoryBuilder().build();
        template = new MimosaSessionTemplate();
        ((MimosaSessionTemplate) template).setSessionFactory(sessionFactory);
    }

    @Test
    public void testSaveNormalTemplate() {
        SessionTemplateServiceTesting.testSaveNormalTemplate(template);
    }

    @Test
    public void testSaveAndUpdateTemplate() {
        SessionTemplateServiceTesting.testSaveAndUpdateTemplate(template);
    }

    @Test
    public void testSavesTemplate() {
        SessionTemplateServiceTesting.testSavesTemplate(template);
    }

    @Test
    public void testUpdateTemplate() {
        SessionTemplateServiceTesting.testUpdateTemplate(template);
    }

    @Test
    public void testUpdatesTemplate() {
        SessionTemplateServiceTesting.testUpdatesTemplate(template);
    }

    @Test
    public void testUseUpdateTemplate() {
        SessionTemplateServiceTesting.testUseUpdateTemplate(template);
    }

    @Test
    public void testDeleteTemplate() {
        SessionTemplateServiceTesting.testDeleteTemplate(template);
    }

    @Test
    public void testDeletesTemplate() {
        SessionTemplateServiceTesting.testDeletesTemplate(template);
    }

    @Test
    public void testUseDeleteTemplate() {
        SessionTemplateServiceTesting.testUseDeleteTemplate(template);
    }

    @Test
    public void testDeleteKeyTemplate() {
        SessionTemplateServiceTesting.testDeleteKeyTemplate(template);
    }

    @Test
    public void testGetKeyTemplate() {
        SessionTemplateServiceTesting.testGetKeyTemplate(template);
    }

    @Test
    public void testGetByQueryTemplate() {
        SessionTemplateServiceTesting.testGetByQueryTemplate(template);
    }

    @Test
    public void testListByQueryTemplate_1() {
        SessionTemplateServiceTesting.testListByQueryTemplate_1(template);
    }

    @Test
    public void testCountTemplate() {
        SessionTemplateServiceTesting.testCountTemplate(template);
    }

    @Test
    public void testCalculateTemplate() {
        SessionTemplateServiceTesting.testCalculateTemplate(template);
    }

    @Test
    public void testAddSelf() {
        ModelObject object = new ModelObject(TableUser.class);
        object.put(TableUser.userName, "yangankang");
        object.put(TableUser.password, "123456");
        object.put(TableUser.realName, "杨安康");
        object.put(TableUser.address, "北京朝阳区");
        object.put(TableUser.age, 25);
        object.put(TableUser.level, 10);
        object.put(TableUser.createdTime, new Date());
        template.save(object);
        template.update(Criteria.update(TableUser.class).eq(TableUser.id, object.getLongValue(TableUser.id)).addSelf(TableUser.age));
        object = template.get(TableUser.class, object.getLongValue(TableUser.id));
        if (object.getIntValue(TableUser.age) != 26) {
            AssistUtils.error("自加出错");
        }
        template.update(Criteria.update(TableUser.class).eq(TableUser.id, object.getLongValue(TableUser.id)).subSelf(TableUser.age, 2));
        object = template.get(TableUser.class, object.getLongValue(TableUser.id));
        if (object.getIntValue(TableUser.age) != 24) {
            AssistUtils.error("自减出错");
        }
    }

    @Test
    public void testBetween() {
        template.list(Criteria.query(TableUser.class).between(TableUser.id, 10, 15));
    }

    @Test
    public void testMutiJoin() {
        template.list(Criteria.query(TableUser.class).eq(TableUser.id, 1)
                .subjoin(TableOrder.class).eq(TableOrder.userId, TableUser.id).query()
                .subjoin(TableOrder.class).eq(TableOrder.userId, TableUser.id).query()
        );
    }

    @Test
    public void testInnerJoin() {
        ModelObject object = new ModelObject(TableUser.class);
        object.put(TableUser.id, 1);
        object.put(TableUser.userName, "yangankang");
        object.put(TableUser.password, "123456");
        object.put(TableUser.realName, "杨安康");
        object.put(TableUser.address, "北京朝阳区");
        object.put(TableUser.age, 25);
        object.put(TableUser.level, 10);
        object.put(TableUser.createdTime, new Date());
        template.saveAndUpdate(object);
        ModelObject order = new ModelObject(TableOrder.class);
        order.put(TableOrder.id, 1);
        order.put(TableOrder.address, "abc");
        order.put(TableOrder.userId, 1);
        template.saveAndUpdate(order);
        order.put(TableOrder.id, 2);
        order.put(TableOrder.address, "def");
        template.saveAndUpdate(order);
        order.put(TableOrder.id, 3);
        order.put(TableOrder.address, "def");
        order.put(TableOrder.userId, 2);
        template.saveAndUpdate(order);
        order.put(TableOrder.id, 4);
        order.put(TableOrder.address, "def");
        order.put(TableOrder.userId, 3);
        template.saveAndUpdate(order);

        Paging objects = template.paging(Criteria.query(TableUser.class).in(TableUser.id, 1, 2, 3)
                .subjoin(TableOrder.class).eq(TableOrder.userId, TableUser.id).query()
                .subjoin(TableOrder.class).eq(TableOrder.userId, TableUser.id).query()
        );
        System.out.println(objects.getCount());
        System.out.println(objects.getObjects());
    }

    @Test
    public void testAutonomously() throws Exception {
        AutoResult autonomously = template.getAutonomously(new SQLAutonomously("select * from t_user where \"id\">1 and \"id\" <10"));
        System.out.println(autonomously.getObjects());
        autonomously = template.getAutonomously(new SQLAutonomously("select count(1) as c from t_user where \"id\">1 and \"id\" <10"));
        System.out.println(autonomously.getObjects());
    }

    @Test
    public void testSourceModel() {
        ModelObject object = new ModelObject(TableUser.class);
        object.put(TableUser.id, 20);
        object.put(TableUser.userName, "yangankang_test_save_n");
        object.put(TableUser.password, "123456");
        object.put(TableUser.realName, "杨安康");
        object.put(TableUser.address, "北京朝阳区");
        object.put(TableUser.age, 25);
        object.put(TableUser.level, 10);
        object.put(TableUser.createdTime, new Date());
        object.put("hello", "yes");
        template.saveAndUpdate(object);
        if (object.getString("hello") == null
                || !object.getString("hello").equals("yes")) {
            throw new IllegalArgumentException("被清除了");
        }
    }

    @Test
    public void testMultiKeyInnerJoin() {
        ModelObject o1 = new ModelObject(TableMultiKey1.class);
        o1.put(TableMultiKey1.id1, 1);
        o1.put(TableMultiKey1.id2, 2);
        template.saveAndUpdate(o1);
        o1 = new ModelObject(TableMultiKey1.class);
        o1.put(TableMultiKey1.id1, 3);
        o1.put(TableMultiKey1.id2, 4);
        template.saveAndUpdate(o1);
        o1 = new ModelObject(TableMultiKey2.class);
        o1.put(TableMultiKey2.id3, 1);
        o1.put(TableMultiKey2.id4, 2);
        template.saveAndUpdate(o1);
        o1 = new ModelObject(TableMultiKey2.class);
        o1.put(TableMultiKey2.id3, 3);
        o1.put(TableMultiKey2.id4, 4);
        template.saveAndUpdate(o1);

        Paging paging = template.paging(Criteria.query(TableMultiKey1.class)
                .subjoin(TableMultiKey2.class)
                .eq(TableMultiKey2.id3, TableMultiKey1.id1)
                .eq(TableMultiKey2.id4, TableMultiKey1.id2).query());

        System.out.println(paging.getCount());
        System.out.println(paging.getObjects());
    }

    @Test
    public void testCalculate() {
        ModelObject object = template.calculate(Criteria.fun(TableUser.class)
                .addFunction(BasicFunction.COUNT, TableUser.age)
                .childGroupBy(TableUser.userName)
                .gt(TableUser.id, 10));
        System.out.println(object);
    }

    @Test
    public void testJavaTypes() throws SQLException {
        SessionTemplateServiceTesting.addJavaTypes(template);
    }
}
