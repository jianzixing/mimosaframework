package program.service;

import org.mimosaframework.core.json.ModelObject;
import org.mimosaframework.core.utils.AssistUtils;
import org.mimosaframework.core.utils.RandomUtils;
import org.mimosaframework.orm.BasicFunction;
import org.mimosaframework.orm.SessionTemplate;
import org.mimosaframework.orm.criteria.Criteria;
import tables.TablePay;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class DistributeSessionTemplateServiceTesting {

    public static void testSaveNormalTemplate(final SessionTemplate template) {
        SessionTemplateServiceTesting.testSaveNormalTemplate(template);
        for (int i = 0; i < 10; i++) {
            SessionTemplateServiceTesting.testSaveNormalTemplate_1(template);
            SessionTemplateServiceTesting.testSaveNormalTemplate_2(template, null);
        }
        List<ModelObject> pays = SessionTemplateServiceTesting.testGetNormalTemplate_1(template, 100);
        List<Long> list = new ArrayList<>();
        for (ModelObject o : pays) {
            Long id = o.getLongValue(TablePay.id);
            if (list.contains(id)) {
                AssistUtils.error("有重复ID");
            }
            list.add(o.getLongValue(TablePay.id));
        }
    }

    public static void testSaveAndUpdateTemplate(final SessionTemplate template) {
        SessionTemplateServiceTesting.testSaveAndUpdateTemplate(template);
        List<Long> ids = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
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

    public static void testSavesTemplate(final SessionTemplate template) {
        SessionTemplateServiceTesting.testSavesTemplate(template);
        for (int i = 0; i < 10; i++) {
            SessionTemplateServiceTesting.testSaveNormalTemplate_1(template);
            SessionTemplateServiceTesting.testSaveNormalTemplate_2(template, null);
        }
    }

    public static void testUpdateTemplate(final SessionTemplate template) {
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

    public static void testUpdatesTemplate(final SessionTemplate template) {
        SessionTemplateServiceTesting.testUpdatesTemplate(template);
    }

    public static void testUseUpdateTemplate(final SessionTemplate template) {
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

    public static void testDeleteTemplate(final SessionTemplate template) {
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

    public static void testDeletesTemplate(final SessionTemplate template) {
        SessionTemplateServiceTesting.testDeletesTemplate(template);
    }

    public static void testUseDeleteTemplate(final SessionTemplate template) {
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

    public static void testDeleteKeyTemplate(final SessionTemplate template) {
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

    public static void testGetKeyTemplate(final SessionTemplate template) {
        SessionTemplateServiceTesting.testGetKeyTemplate(template);
    }

    public static void testGetByQueryTemplate(final SessionTemplate template) {
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

    public static void testListByQueryTemplate_1(final SessionTemplate template) {
        for (int i = 0; i < 10; i++) {
            SessionTemplateServiceTesting.testListByQueryTemplate_1(template);
        }
    }

    public static void testCountTemplate(final SessionTemplate template) {
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
            long c2 = template.count(Criteria.query(TablePay.class));
            long c3 = template.count(Criteria.query(TablePay.class).lte(TablePay.payMoney, 150));
            if (c1 + c3 != c2) {
                // AssistUtils.error("查询的数量不相等");
            }
        }
    }

    public static void testCalculateTemplate(final SessionTemplate template) {
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
//        System.out.println(avg.toJSONString());
    }

    public static void concurrentTestSaveNormalTemplate(final SessionTemplate template) {
        testSaveNormalTemplate(template);
        testSaveAndUpdateTemplate(template);
        testSavesTemplate(template);
        testUpdateTemplate(template);
        testUpdatesTemplate(template);
        testUseUpdateTemplate(template);
        testDeleteTemplate(template);
        testDeletesTemplate(template);
        testUseDeleteTemplate(template);
        testDeleteKeyTemplate(template);
        testGetKeyTemplate(template);
        testGetByQueryTemplate(template);
        testListByQueryTemplate_1(template);
        testCountTemplate(template);
        testCalculateTemplate(template);
    }

}
