package session;

import com.alibaba.druid.pool.DruidDataSource;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.mimosaframework.core.json.ModelObject;
import org.mimosaframework.core.utils.AssistUtils;
import org.mimosaframework.core.utils.RandomUtils;
import org.mimosaframework.orm.*;
import org.mimosaframework.orm.criteria.Criteria;
import org.mimosaframework.orm.exception.ContextException;
import program.service.SessionTemplateServiceTesting;
import tables.TablePay;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Set;

public class DistributeSessionTemplateTesting {
    private SessionTemplate template = null;

    @Before
    public void init() throws ContextException {
        XmlAppContext context = new XmlAppContext(SessionFactoryBuilder.class.getResourceAsStream("/distributed-template-mimosa.xml"));
        SessionFactory sessionFactory = context.getSessionFactoryBuilder().build();
        template = new MimosaSessionTemplate();
        ((MimosaSessionTemplate) template).setSessionFactory(sessionFactory);

        System.out.println("当前初始化的DataSource数量 " + MimosaDataSource.getDataSourceSize());
    }

    @After
    public void close() {
        Set<DataSource> dataSources = MimosaDataSource.getAllDataSources();
        for (DataSource ds : dataSources) {
            DruidDataSource dds = (DruidDataSource) ds;
            dds.close();
        }
        dataSources.clear();
    }

    @Test
    public void initTest() {
        System.out.println("ok");
    }

    @Test
    public void testSaveNormalTemplate() {
        SessionTemplateServiceTesting.testSaveNormalTemplate(template);
        for (int i = 0; i < 1000; i++) {
            SessionTemplateServiceTesting.testSaveNormalTemplate_1(template);
            SessionTemplateServiceTesting.testSaveNormalTemplate_2(template, null);
        }

        for (int i = 0; i < 1000; i++) {
            List<ModelObject> pays = SessionTemplateServiceTesting.testGetNormalTemplate_1(template, 100);
            List<Long> list = new ArrayList<>();
            for (ModelObject o : pays) {
                Long id = o.getLongValue(TablePay.id);
                if (list.contains(id)) {
                    AssistUtils.error("有重复ID");
                }
                list.add(o.getLongValue(TablePay.id));
                System.out.println(o);
            }
        }
    }

    @Test
    public void testSaveAndUpdateTemplate() {
        SessionTemplateServiceTesting.testSaveAndUpdateTemplate(template);
        List<Long> ids = new ArrayList<>();
        for (int i = 0; i < 1000; i++) {
            SessionTemplateServiceTesting.testSaveNormalTemplate_2(template, ids);
        }
        for (Long id : ids) {
            ModelObject o = template.get(TablePay.class, id);
            if (o == null) {
                AssistUtils.error("分表保存取出来是空的");
            }
            o.put(TablePay.status, 80);
            o.put(TablePay.payMoney, 90.90);
            template.update(o);

            ModelObject o2 = template.get(TablePay.class, id);
            if (o2 == null) {
                AssistUtils.error("分表保存取出来是空的");
            }
            if (!o.getString(TablePay.status).equalsIgnoreCase(o2.getString(TablePay.status))
                    || o.getString(TablePay.payMoney).equalsIgnoreCase(o2.getString(TablePay.payMoney))) {
                AssistUtils.error("修改失败了");
            }
        }
    }

    @Test
    public void testSavesTemplate() {
        SessionTemplateServiceTesting.testSavesTemplate(template);
        for (int i = 0; i < 10; i++) {
            SessionTemplateServiceTesting.testSaveNormalTemplate_1(template);
            SessionTemplateServiceTesting.testSaveNormalTemplate_2(template, null);
        }
    }

    @Test
    public void testUpdateTemplate() {
        for (int i = 0; i < 10; i++) {
            SessionTemplateServiceTesting.testUpdateTemplate(template);
            ModelObject object = new ModelObject(TablePay.class);
            object.put(TablePay.userId, 0);
            object.put(TablePay.orderId, 0);
            object.put(TablePay.payMoney, RandomUtils.randomNumber(3));
            object.put(TablePay.status, RandomUtils.randomNumber(2));
            object.put(TablePay.createdTime, new Date());
            template.save(object);

            long id = object.getLongValue(TablePay.id);
            ModelObject old = template.get(TablePay.class, id);
            old.put(TablePay.payMoney, RandomUtils.randomNumber(3));
            old.put(TablePay.status, RandomUtils.randomNumber(2));
            template.update(old);

            ModelObject a = template.get(TablePay.class, id);
            if (!a.getDouble(TablePay.payMoney).equals(old.getDouble(TablePay.payMoney))
                    || !a.getInteger(TablePay.status).equals(old.getInteger(TablePay.status))) {
                AssistUtils.error("分表更新出现不一致");
            }
        }
    }

    @Test
    public void testUpdatesTemplate() {
        SessionTemplateServiceTesting.testUpdatesTemplate(template);
    }

    @Test
    public void testUseUpdateTemplate() {
        SessionTemplateServiceTesting.testUseUpdateTemplate(template);

        for (int i = 0; i < 10; i++) {
            ModelObject object = new ModelObject(TablePay.class);
            object.put(TablePay.userId, 0);
            object.put(TablePay.orderId, 0);
            object.put(TablePay.payMoney, RandomUtils.randomNumber(3));
            object.put(TablePay.status, RandomUtils.randomNumber(2));
            object.put(TablePay.createdTime, new Date());
            template.save(object);

            long id = object.getLongValue(TablePay.id);
            long pm = RandomUtils.randomNumber(3);

            template.update(Criteria.update(TablePay.class).eq(TablePay.id, id).value(TablePay.payMoney, pm));
            ModelObject o = template.get(TablePay.class, object.getLongValue(TablePay.id));
            if (!o.getDouble(TablePay.payMoney).equals(new Double(pm))) {
                AssistUtils.error("更新后的对象不正确");
            }

            pm = RandomUtils.randomNumber(3);
            template.update(Criteria.update(TablePay.class).in(TablePay.id, id).value(TablePay.payMoney, pm));
            o = template.get(TablePay.class, object.getLongValue(TablePay.id));
            if (!o.getDouble(TablePay.payMoney).equals(new Double(pm))) {
                AssistUtils.error("更新后的对象不正确");
            }

            pm = RandomUtils.randomNumber(3);
            template.update(Criteria.update(TablePay.class).like(TablePay.id, id).value(TablePay.payMoney, pm));
            o = template.get(TablePay.class, object.getLongValue(TablePay.id));
            if (!o.getDouble(TablePay.payMoney).equals(new Double(pm))) {
                AssistUtils.error("更新后的对象不正确");
            }
        }
    }

    @Test
    public void testDeleteTemplate() {
        SessionTemplateServiceTesting.testDeleteTemplate(template);
        for (int i = 0; i < 10; i++) {
            ModelObject object = new ModelObject(TablePay.class);
            object.put(TablePay.userId, 0);
            object.put(TablePay.orderId, 0);
            object.put(TablePay.payMoney, RandomUtils.randomNumber(3));
            object.put(TablePay.status, RandomUtils.randomNumber(2));
            object.put(TablePay.createdTime, new Date());
            template.save(object);
            template.delete(object);
            ModelObject o = template.get(TablePay.class, object.getLongValue(TablePay.id));
            if (o != null) {
                AssistUtils.error("应该被删除确还存在");
            }
        }
    }

    @Test
    public void testDeletesTemplate() {
        SessionTemplateServiceTesting.testDeletesTemplate(template);
    }

    @Test
    public void testUseDeleteTemplate() {
        SessionTemplateServiceTesting.testUseDeleteTemplate(template);

        for (int i = 0; i < 10; i++) {
            ModelObject object = new ModelObject(TablePay.class);
            object.put(TablePay.userId, 0);
            object.put(TablePay.orderId, 0);
            object.put(TablePay.payMoney, RandomUtils.randomNumber(3));
            object.put(TablePay.status, RandomUtils.randomNumber(2));
            object.put(TablePay.createdTime, new Date());
            template.save(object);

            long id = object.getLongValue(TablePay.id);

            template.delete(Criteria.delete(TablePay.class).eq(TablePay.id, id));
            ModelObject o = template.get(TablePay.class, object.getLongValue(TablePay.id));
            if (o != null) {
                AssistUtils.error("应该被删除确还存在");
            }

            template.delete(Criteria.delete(TablePay.class).in(TablePay.id, id));
            o = template.get(TablePay.class, object.getLongValue(TablePay.id));
            if (o != null) {
                AssistUtils.error("应该被删除确还存在");
            }

            template.delete(Criteria.delete(TablePay.class).like(TablePay.id, id));
            o = template.get(TablePay.class, object.getLongValue(TablePay.id));
            if (o != null) {
                AssistUtils.error("应该被删除确还存在");
            }
        }
    }

    /**
     * 分表时删除更新需要考虑用户自定义的策略字段
     */
    @Test
    public void testDeleteKeyTemplate() {
        SessionTemplateServiceTesting.testDeleteKeyTemplate(template);
        for (int i = 0; i < 10; i++) {
            ModelObject object = new ModelObject(TablePay.class);
            object.put(TablePay.userId, 0);
            object.put(TablePay.orderId, 0);
            object.put(TablePay.payMoney, RandomUtils.randomNumber(3));
            object.put(TablePay.status, RandomUtils.randomNumber(2));
            object.put(TablePay.createdTime, new Date());
            template.save(object);
            template.delete(TablePay.class, object.getLongValue(TablePay.id));
            ModelObject o = template.get(TablePay.class, object.getLongValue(TablePay.id));
            if (o != null) {
                AssistUtils.error("应该被删除确还存在");
            }
        }
    }

    @Test
    public void testGetKeyTemplate() {
        SessionTemplateServiceTesting.testGetKeyTemplate(template);
    }

    @Test
    public void testGetByQueryTemplate() {
        SessionTemplateServiceTesting.testGetByQueryTemplate(template);
        for (int i = 0; i < 10; i++) {
            ModelObject object = new ModelObject(TablePay.class);
            object.put(TablePay.userId, 0);
            object.put(TablePay.orderId, 0);
            object.put(TablePay.payMoney, RandomUtils.randomNumber(3));
            object.put(TablePay.status, RandomUtils.randomNumber(2));
            object.put(TablePay.createdTime, new Date());
            template.save(object);
            ModelObject o = template.get(Criteria.query(TablePay.class).eq(TablePay.id, object.getLongValue(TablePay.id)));
            if (!o.getDouble(TablePay.payMoney).equals(object.getDouble(TablePay.payMoney))) {
                AssistUtils.error("更新后的对象不正确");
            }
        }
    }

    @Test
    public void testListByQueryTemplate_1() {
        for (int i = 0; i < 10; i++) {
            SessionTemplateServiceTesting.testListByQueryTemplate_1(template);
        }
    }

    @Test
    public void testCountTemplate() {
        SessionTemplateServiceTesting.testCountTemplate(template);
        for (int i = 0; i < 10; i++) {
            ModelObject object = new ModelObject(TablePay.class);
            object.put(TablePay.userId, 0);
            object.put(TablePay.orderId, 0);
            object.put(TablePay.payMoney, RandomUtils.randomNumber(3));
            object.put(TablePay.status, RandomUtils.randomNumber(2));
            object.put(TablePay.createdTime, new Date());
            template.save(object);
            long c1 = template.count(Criteria.query(TablePay.class).gt(TablePay.payMoney, 150));
            long c2 = template.count(Criteria.query(TablePay.class).isNotNull(TablePay.payMoney));
            long c3 = template.count(Criteria.query(TablePay.class).lte(TablePay.payMoney, 150));
            if (c1 + c3 != c2) {
                AssistUtils.error("查询的数量不相等");
            }
            System.out.println(c2);
        }
    }

    @Test
    public void testCalculateTemplate() {
        SessionTemplateServiceTesting.testCalculateTemplate(template);

        ModelObject object = new ModelObject(TablePay.class);
        object.put(TablePay.userId, 0);
        object.put(TablePay.orderId, 0);
        object.put(TablePay.payMoney, RandomUtils.randomNumber(3));
        object.put(TablePay.status, RandomUtils.randomNumber(2));
        object.put(TablePay.createdTime, new Date());
        template.save(object);

        ModelObject avg = template.calculate(
                Criteria.fun(TablePay.class)
                        .addFunction(BasicFunction.AVG, TablePay.payMoney, "avg")
                        .addFunction(BasicFunction.SUM, TablePay.payMoney, "sum")
                        .addFunction(BasicFunction.COUNT, TablePay.id)
                        .addFunction(BasicFunction.MAX, TablePay.payMoney, "max")
                        .addFunction(BasicFunction.MIN, TablePay.payMoney, "min")
        );
        double r = avg.getDoubleValue("avg");
        if (r <= 0) {
            AssistUtils.error("计算平均值出错");
        }
        System.out.println(avg.toJSONString());
    }

    @Test
    public void concurrentTestSaveNormalTemplate() {
        SessionTemplateServiceTesting.concurrentTestSaveNormalTemplate(template);
    }

    @Test
    public void testAutonomously() throws Exception {

    }
}
