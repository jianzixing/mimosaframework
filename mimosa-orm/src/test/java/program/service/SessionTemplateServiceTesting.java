package program.service;

import org.mimosaframework.core.json.ModelArray;
import org.mimosaframework.core.json.ModelObject;
import org.mimosaframework.core.utils.AssistUtils;
import org.mimosaframework.core.utils.RandomUtils;
import org.mimosaframework.orm.BasicFunction;
import org.mimosaframework.orm.SessionTemplate;
import org.mimosaframework.orm.criteria.Criteria;
import tables.*;

import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

public class SessionTemplateServiceTesting {

    public static void testSaveNormalTemplate(SessionTemplate template) {
        ModelObject object = new ModelObject(TableUser.class);
        object.put(TableUser.userName, "yangankang");
        object.put(TableUser.password, "123456");
        object.put(TableUser.realName, "杨安康");
        object.put(TableUser.address, "北京朝阳区");
        object.put(TableUser.age, 25);
        object.put(TableUser.level, 10);
        object.put(TableUser.createdTime, new Date());
        template.save(object);

    }

    public static void testSaveNormalTemplate_1(SessionTemplate template) {
        ModelObject object = new ModelObject(TableOrder.class);
        object.put(TableOrder.userId, 0);
        object.put(TableOrder.productId, 0);
        object.put(TableOrder.address, RandomUtils.randomChineseCharacters(3, 8));
        object.put(TableOrder.phone, RandomUtils.randomNumber(14));
        object.put(TableOrder.createdTime, new Date());
        template.save(object);

    }

    public static void testSaveNormalTemplate_2(SessionTemplate template, List<Long> ids) {
        ModelObject object = new ModelObject(TablePay.class);
        object.put(TablePay.userId, 0);
        object.put(TablePay.orderId, 0);
        object.put(TablePay.payMoney, RandomUtils.randomNumber(3));
        object.put(TablePay.status, RandomUtils.randomNumber(2));
        object.put(TablePay.createdTime, new Date());
        template.save(object);
        if (ids != null) {
            ids.add(object.getLongValue(TablePay.id));
        }

    }

    public static List<ModelObject> testGetNormalTemplate_1(SessionTemplate template, int count) {
        return template.list(Criteria.query(TablePay.class).limit(0, 100).order(TablePay.id, false));
    }

    public static void testSaveAndUpdateTemplate(SessionTemplate template) {
        ModelObject object = new ModelObject(TableUser.class);
        object.put(TableUser.userName, "yangankang");
        object.put(TableUser.password, "123456");
        object.put(TableUser.realName, "杨安康");
        object.put(TableUser.address, "北京朝阳区");
        object.put(TableUser.age, 25);
        object.put(TableUser.level, 10);
        object.put(TableUser.createdTime, new Date());
        template.save(object);
        long id = object.getLongValue(TableUser.id);
        if (id == 0) {
            AssistUtils.error("保存对象失败");
        }
        object.put(TableUser.realName, "QMT");
        object.put(TableUser.address, "中心城市");
        object.put(TableUser.age, 18);
        template.saveAndUpdate(object);

    }

    public static void testSavesTemplate(SessionTemplate template) {
        ModelObject object = new ModelObject(TableUser.class);
        object.put(TableUser.userName, "yangankang");
        object.put(TableUser.password, "123456");
        object.put(TableUser.realName, "杨安康");
        object.put(TableUser.address, "北京朝阳区");
        object.put(TableUser.age, 25);
        object.put(TableUser.level, 10);
        object.put(TableUser.createdTime, new Date());
        List<ModelObject> objects = new ArrayList<>();
        objects.add(object);
        object = new ModelObject(TableUser.class);
        object.put(TableUser.userName, "yangankang");
        object.put(TableUser.realName, "杨安康");
        object.put(TableUser.address, "北京朝阳区");
        object.put(TableUser.age, 25);
        object.put(TableUser.level, 10);
        object.put(TableUser.createdTime, new Date());
        object.put(TableUser.password, "123");
        objects.add(object);
        object = new ModelObject(TableUser.class);
        object.put(TableUser.userName, "yangankang");
        object.put(TableUser.realName, "杨安康");
        object.put(TableUser.address, "北京朝阳区");
        object.put(TableUser.age, 25);
        object.put(TableUser.level, 10);
        object.put(TableUser.createdTime, new Date());
        object.put(TableUser.password, "456");
        objects.add(object);
        object = new ModelObject(TableUser.class);
        object.put(TableUser.userName, "yangankang");
        object.put(TableUser.realName, "杨安康");
        object.put(TableUser.address, "北京朝阳区");
        object.put(TableUser.age, 25);
        object.put(TableUser.level, 10);
        object.put(TableUser.createdTime, new Date());
        object.put(TableUser.password, "789");
        objects.add(object);
        object = new ModelObject(TableUser.class);
        object.put(TableUser.userName, "yangankang");
        object.put(TableUser.realName, "杨安康");
        object.put(TableUser.address, "北京朝阳区");
        object.put(TableUser.age, 25);
        object.put(TableUser.level, 10);
        object.put(TableUser.createdTime, new Date());
        object.put(TableUser.password, "0");
        objects.add(object);
        template.save(objects);

//        ModelObject db = template.get(TableUser.class, objects.get(0).getLongValue(TableUser.id));
//        if (!db.getString(TableUser.password).equals(objects.get(0).getString(TableUser.password))) {
//            throw new IllegalArgumentException("ID 赋值不对");
//        }
//        db = template.get(TableUser.class, objects.get(1).getLongValue(TableUser.id));
//        if (!db.getString(TableUser.password).equals(objects.get(1).getString(TableUser.password))) {
//            throw new IllegalArgumentException("ID 赋值不对");
//        }
//        db = template.get(TableUser.class, objects.get(2).getLongValue(TableUser.id));
//        if (!db.getString(TableUser.password).equals(objects.get(2).getString(TableUser.password))) {
//            throw new IllegalArgumentException("ID 赋值不对");
//        }
//        db = template.get(TableUser.class, objects.get(3).getLongValue(TableUser.id));
//        if (!db.getString(TableUser.password).equals(objects.get(3).getString(TableUser.password))) {
//            throw new IllegalArgumentException("ID 赋值不对");
//        }
//        db = template.get(TableUser.class, objects.get(4).getLongValue(TableUser.id));
//        if (!db.getString(TableUser.password).equals(objects.get(4).getString(TableUser.password))) {
//            throw new IllegalArgumentException("ID 赋值不对");
//        }
    }

    public static void testUpdateTemplate(SessionTemplate template) {
        ModelObject object = new ModelObject(TableUser.class);
        object.put(TableUser.userName, "yangankang");
        object.put(TableUser.password, "123456");
        object.put(TableUser.realName, "杨安康");
        object.put(TableUser.address, "北京朝阳区");
        object.put(TableUser.age, 25);
        object.put(TableUser.level, 10);
        object.put(TableUser.createdTime, new Date());
        template.save(object);

        String s1 = RandomUtils.randomChineseCharacters(3, 6);
        String s2 = RandomUtils.randomChineseCharacters(6, 10);
        long s3 = RandomUtils.randomNumber(3, 10);
        object.put(TableUser.realName, s1);
        object.put(TableUser.address, s2);
        object.put(TableUser.level, s3);
        template.update(object);
        ModelObject o = template.get(TableUser.class, object.getLongValue(TableUser.id));
        if (!o.getString(TableUser.realName).equals(s1)
                || !o.getString(TableUser.address).equals(s2)
                || o.getLongValue(TableUser.level) != s3) {
            AssistUtils.error("没有修改成功");
        }
    }

    public static void testUpdatesTemplate(SessionTemplate template) {
        ModelObject object = new ModelObject(TableUser.class);
        object.put(TableUser.userName, "yangankang");
        object.put(TableUser.password, "123456");
        object.put(TableUser.realName, "杨安康");
        object.put(TableUser.address, "北京朝阳区");
        object.put(TableUser.age, 25);
        object.put(TableUser.level, 10);
        object.put(TableUser.createdTime, new Date());
        template.save(object);

        String s1 = RandomUtils.randomChineseCharacters(3, 6);
        String s2 = RandomUtils.randomChineseCharacters(6, 10);
        long s3 = RandomUtils.randomNumber(3, 10);
        object.put(TableUser.realName, s1);
        object.put(TableUser.address, s2);
        object.put(TableUser.level, s3);
        List<ModelObject> objects = new ArrayList<>();
        objects.add(object);
        template.update(objects);
        ModelObject o = template.get(TableUser.class, object.getLongValue(TableUser.id));
        if (!o.getString(TableUser.realName).equals(s1)
                || !o.getString(TableUser.address).equals(s2)
                || o.getLongValue(TableUser.level) != s3) {
            AssistUtils.error("没有修改成功");
        }

    }

    public static void testUseUpdateTemplate(SessionTemplate template) {
        ModelObject object = new ModelObject(TableUser.class);
        String userName = RandomUtils.randomChineseCharacters(15, 20);
        object.put(TableUser.userName, userName);
        object.put(TableUser.password, "123456");
        object.put(TableUser.realName, "杨安康");
        object.put(TableUser.address, "北京朝阳区");
        object.put(TableUser.age, 25);
        object.put(TableUser.level, 10);
        object.put(TableUser.createdTime, new Date());
        template.save(object);

        for (int i = 0; i < 5; i++) {
            String address = RandomUtils.randomChineseCharacters(6, 10);
            if (i == 0) {
                template.update(Criteria.update(TableUser.class).eq(TableUser.userName, userName).value(TableUser.address, address));
                ModelObject o = template.get(TableUser.class, object.getLongValue(TableUser.id));
                if (!o.getString(TableUser.address).equals(address)) {
                    AssistUtils.error("更新后的对象不正确");
                }
            } else if (i == 1) {
                template.update(Criteria.update(TableUser.class).in(TableUser.userName, userName).value(TableUser.address, address));
                ModelObject o = template.get(TableUser.class, object.getLongValue(TableUser.id));
                if (!o.getString(TableUser.address).equals(address)) {
                    AssistUtils.error("更新后的对象不正确");
                }
            } else {
                template.update(Criteria.update(TableUser.class).like(TableUser.userName, "%" + userName + "%").value(TableUser.address, address));
                ModelObject o = template.get(TableUser.class, object.getLongValue(TableUser.id));
                if (!o.getString(TableUser.address).equals(address)) {
                    AssistUtils.error("更新后的对象不正确");
                }
            }
        }
    }

    public static void testDeleteTemplate(SessionTemplate template) {
        ModelObject object = new ModelObject(TableUser.class);
        String userName = RandomUtils.randomChineseCharacters(15, 20);
        object.put(TableUser.userName, userName);
        object.put(TableUser.password, "123456");
        object.put(TableUser.realName, "杨安康");
        object.put(TableUser.address, "北京朝阳区");
        object.put(TableUser.age, 25);
        object.put(TableUser.level, 10);
        object.put(TableUser.createdTime, new Date());
        template.save(object);
        long id = object.getLongValue(TableUser.id);
        template.delete(object);
        ModelObject o = template.get(TableUser.class, id);
        if (o != null) {
            AssistUtils.error("应该被删除确还存在");
        }

    }

    public static void testDeletesTemplate(SessionTemplate template) {
        ModelObject object1 = new ModelObject(TableUser.class);
        String userName1 = RandomUtils.randomChineseCharacters(15, 20);
        object1.put(TableUser.userName, userName1);
        object1.put(TableUser.password, "123456");
        object1.put(TableUser.realName, "杨安康");
        object1.put(TableUser.address, "北京朝阳区");
        object1.put(TableUser.age, 25);
        object1.put(TableUser.level, 10);
        object1.put(TableUser.createdTime, new Date());
        template.save(object1);
        ModelObject object2 = new ModelObject(TableUser.class);
        String userName2 = RandomUtils.randomChineseCharacters(15, 20);
        object2.put(TableUser.userName, userName2);
        object2.put(TableUser.password, "123456");
        object2.put(TableUser.realName, "杨安康");
        object2.put(TableUser.address, "北京朝阳区");
        object2.put(TableUser.age, 25);
        object2.put(TableUser.level, 10);
        object2.put(TableUser.createdTime, new Date());
        template.save(object2);
        ModelObject object3 = new ModelObject(TableUser.class);
        String userName3 = RandomUtils.randomChineseCharacters(15, 20);
        object3.put(TableUser.userName, userName3);
        object3.put(TableUser.password, "123456");
        object3.put(TableUser.realName, "杨安康");
        object3.put(TableUser.address, "北京朝阳区");
        object3.put(TableUser.age, 25);
        object3.put(TableUser.level, 10);
        object3.put(TableUser.createdTime, new Date());
        template.save(object3);
        List<ModelObject> objects = new ArrayList<>();
        objects.add(object1);
        objects.add(object2);
        objects.add(object3);
        template.delete(objects);
        long i1 = object1.getLongValue(TableUser.id);
        long i2 = object1.getLongValue(TableUser.id);
        long i3 = object1.getLongValue(TableUser.id);
        if (template.get(TableUser.class, i1) != null) {
            AssistUtils.error("本来被删除的");
        }
        if (template.get(TableUser.class, i2) != null) {
            AssistUtils.error("本来被删除的");
        }
        if (template.get(TableUser.class, i3) != null) {
            AssistUtils.error("本来被删除的");
        }

    }

    public static void testUseDeleteTemplate(SessionTemplate template) {
        for (int i = 0; i < 5; i++) {
            ModelObject object1 = new ModelObject(TableUser.class);
            String userName1 = RandomUtils.randomChineseCharacters(15, 20);
            object1.put(TableUser.userName, userName1);
            object1.put(TableUser.password, "123456");
            object1.put(TableUser.realName, "杨安康");
            object1.put(TableUser.address, "北京朝阳区");
            object1.put(TableUser.age, 25);
            object1.put(TableUser.level, 10);
            object1.put(TableUser.createdTime, new Date());
            template.save(object1);
            long id = object1.getLongValue(TableUser.id);
            if (i == 0) {
                template.delete(Criteria.delete(TableUser.class).eq(TableUser.id, id));
                if (template.get(TableUser.class, id) != null) {
                    AssistUtils.error("本来被删除的");
                }
            } else if (i == 2) {
                template.delete(Criteria.delete(TableUser.class).in(TableUser.id, id));
                if (template.get(TableUser.class, id) != null) {
                    AssistUtils.error("本来被删除的");
                }
            } else {
                template.delete(Criteria.delete(TableUser.class).like(TableUser.userName, "%" + userName1 + "%"));
                if (template.get(TableUser.class, id) != null) {
                    AssistUtils.error("本来被删除的");
                }
            }
        }

    }

    public static void testDeleteKeyTemplate(SessionTemplate template) {
        ModelObject object1 = new ModelObject(TableUser.class);
        String userName1 = RandomUtils.randomChineseCharacters(15, 20);
        object1.put(TableUser.userName, userName1);
        object1.put(TableUser.password, "123456");
        object1.put(TableUser.realName, "杨安康");
        object1.put(TableUser.address, "北京朝阳区");
        object1.put(TableUser.age, 25);
        object1.put(TableUser.level, 10);
        object1.put(TableUser.createdTime, new Date());
        template.save(object1);
        template.delete(TableUser.class, object1.getLongValue(TableUser.id));
        if (template.get(TableUser.class, object1.getLongValue(TableUser.id)) != null) {
            AssistUtils.error("本来被删除的");
        }

    }

    public static void testGetKeyTemplate(SessionTemplate template) {
        ModelObject object1 = new ModelObject(TableUser.class);
        String userName1 = RandomUtils.randomChineseCharacters(15, 20);
        object1.put(TableUser.userName, userName1);
        object1.put(TableUser.password, "123456");
        object1.put(TableUser.realName, "杨安康");
        object1.put(TableUser.address, "北京朝阳区");
        object1.put(TableUser.age, 25);
        object1.put(TableUser.level, 10);
        object1.put(TableUser.createdTime, new Date());
        template.save(object1);
        if (template.get(TableUser.class, object1.getLongValue(TableUser.id)) == null) {
            AssistUtils.error("本来被删除的");
        }

    }

    public static void testGetByQueryTemplate(SessionTemplate template) {
        ModelObject object1 = new ModelObject(TableUser.class);
        String userName1 = RandomUtils.randomChineseCharacters(15, 20);
        String pwd = RandomUtils.uuid();
        object1.put(TableUser.userName, userName1);
        object1.put(TableUser.password, pwd);
        object1.put(TableUser.realName, "杨安康");
        object1.put(TableUser.address, "北京朝阳区");
        object1.put(TableUser.age, 25);
        object1.put(TableUser.level, 10);
        object1.put(TableUser.createdTime, new Date());
        template.save(object1);
        ModelObject o = template.get(Criteria.query(TableUser.class).eq(TableUser.password, pwd));
        if (o == null || !o.getString(TableUser.userName).equals(userName1)) {
            AssistUtils.error("查询出来的数据不正确");
        }

    }

    public static void testListByQueryTemplate_1(SessionTemplate template) {
        ModelObject object1 = new ModelObject(TableUser.class);
        String userName1 = RandomUtils.randomChineseCharacters(3, 6);
        String pwd = RandomUtils.uuid();
        object1.put(TableUser.userName, userName1);
        object1.put(TableUser.password, pwd);
        object1.put(TableUser.realName, "杨安康");
        object1.put(TableUser.address, "北京朝阳区");
        object1.put(TableUser.age, 25);
        object1.put(TableUser.level, 10);
        object1.put(TableUser.createdTime, new Date());
        template.save(object1);

        ModelObject object2 = new ModelObject(TableProduct.class);
        object2.put(TableProduct.name, RandomUtils.randomChineseCharacters(10, 20));
        object2.put(TableProduct.price, RandomUtils.randomDecimalNumber(10d, 1000d, 2));
        object2.put(TableProduct.amount, 1);
        template.save(object2);

        ModelObject object3 = new ModelObject(TableOrder.class);
        object3.put(TableOrder.productId, object2.getLongValue(TableProduct.id));
        object3.put(TableOrder.userId, object1.getLongValue(TableUser.id));
        object3.put(TableOrder.orderMoney, object2.getString(TableProduct.price));
        object3.put(TableOrder.address, RandomUtils.randomChineseCharacters(10, 20));
        object3.put(TableOrder.phone, RandomUtils.randomNumber(14, 14));
        template.save(object3);

        ModelObject object4 = new ModelObject(TablePay.class);
        object4.put(TablePay.userId, object1.getLongValue(TableUser.id));
        object4.put(TablePay.orderId, object3.getString(TableOrder.id));
        object4.put(TablePay.payMoney, object3.getString(TableOrder.orderMoney));
        object4.put(TablePay.status, RandomUtils.randomNumber(100, 110));
        template.save(object4);
        ModelObject object5 = new ModelObject(TablePay.class);
        object5.put(TablePay.userId, object1.getLongValue(TableUser.id));
        object5.put(TablePay.orderId, object3.getIntValue(TableOrder.id));
        object5.put(TablePay.payMoney, object3.getString(TableOrder.orderMoney));
        object5.put(TablePay.status, object4.get(TablePay.status));
        template.save(object5);

        List<Long> ids = new ArrayList<>();
        ids.add(object4.getLongValue(TablePay.id));
        ids.add(object5.getLongValue(TablePay.id));

//        List<ModelObject> objects = template.list(Criteria.query(TableUser.class)
//                .subjoin(TableOrder.class).eq(TableOrder.userId, TableUser.id)
//                .childJoin(TableProduct.class).eq(TableProduct.id, TableOrder.productId)
//                .query()
//                .subjoin(TablePay.class).eq(TablePay.userId, TableUser.id)
//                .query()
//                .eq(TableUser.id, object1.getLongValue(TableUser.id))
//                .limit(0, 10));


        List<ModelObject> objects = template.list(Criteria.query(TableUser.class)
                .addSubjoin(
                        Criteria.join(TableOrder.class).eq(TableOrder.userId, TableUser.id)
                                .addChildJoin(
                                        Criteria.join(TableProduct.class).eq(TableProduct.id, TableOrder.productId)
                                )
                )
                .addSubjoin(
                        Criteria.join(TablePay.class).eq(TablePay.userId, TableUser.id)
                )
                .eq(TableUser.id, object1.getLongValue(TableUser.id))
                .limit(0, 10));

        if (objects == null) {
            AssistUtils.error("没查到出问题了");
        }
        for (ModelObject object : objects) {
            if (!object.getString(TableUser.userName).equals(userName1)) {
                AssistUtils.error("数据不一致");
            }
            ModelArray os = object.getModelArray(TableOrder.class.getSimpleName());
            if (os == null) {
                AssistUtils.error("数据不一致");
            }
            for (Iterator<Object> it = os.iterator(); it.hasNext(); ) {
                ModelObject iterator = (ModelObject) it.next();
                if (!iterator.getString(TableOrder.address).equals(object3.getString(TableOrder.address))) {
                    AssistUtils.error("数据不一致");
                }
                ModelArray os2 = iterator.getModelArray(TableProduct.class.getSimpleName());
                if (os2 == null) {
                    AssistUtils.error("数据不一致");
                }
            }

            os = object.getModelArray(TablePay.class.getSimpleName());
            if (os == null) {
                AssistUtils.error("数据不一致");
            }
            for (Iterator<Object> it = os.iterator(); it.hasNext(); ) {
                ModelObject iterator = (ModelObject) it.next();
                if (!iterator.getString(TablePay.payMoney).equals(object4.getString(TablePay.payMoney))
                        || !ids.contains(iterator.getLongValue(TablePay.id))) {
                    AssistUtils.error("数据不一致");
                }
            }
        }

    }

    public static void testCountTemplate(SessionTemplate template) {
        ModelObject object1 = new ModelObject(TableUser.class);
        String userName1 = RandomUtils.randomChineseCharacters(3, 6);
        String pwd = RandomUtils.uuid();
        object1.put(TableUser.userName, userName1);
        object1.put(TableUser.password, pwd);
        object1.put(TableUser.realName, "杨安康");
        object1.put(TableUser.address, "北京朝阳区");
        object1.put(TableUser.age, 25);
        object1.put(TableUser.level, 10);
        object1.put(TableUser.createdTime, new Date());
        template.save(object1);

        long c = template.count(Criteria.query(TableUser.class).gt(TableUser.id, 0));
        if (c <= 0) {
            AssistUtils.error("查询Count出错");
        }

    }

    public static void testCalculateTemplate(SessionTemplate template) {
        ModelObject object1 = new ModelObject(TableUser.class);
        String userName1 = RandomUtils.randomChineseCharacters(3, 6);
        String pwd = RandomUtils.uuid();
        object1.put(TableUser.userName, userName1);
        object1.put(TableUser.password, pwd);
        object1.put(TableUser.realName, "杨安康");
        object1.put(TableUser.address, "北京朝阳区");
        object1.put(TableUser.age, 25);
        object1.put(TableUser.level, 10);
        object1.put(TableUser.createdTime, new Date());
        template.save(object1);

        ModelObject avg = template.calculate(
                Criteria.fun(TableUser.class)
                        .addFunction(BasicFunction.AVG, TableUser.age)
                        .addFunction(BasicFunction.SUM, TableUser.level)
                        .addFunction(BasicFunction.COUNT, TableUser.id)
                        .addFunction(BasicFunction.MAX, TableUser.age, "maxAge")
                        .addFunction(BasicFunction.MIN, TableUser.age, "minAge")
        );
        double r = avg.getDoubleValue(TableUser.age);
        if (r <= 0) {
            AssistUtils.error("计算平均值出错");
        }

    }

    public static void concurrentTestSaveNormalTemplate(final SessionTemplate template) {
        SessionTemplateServiceTesting.testSaveNormalTemplate(template);
        SessionTemplateServiceTesting.testSaveAndUpdateTemplate(template);
        SessionTemplateServiceTesting.testSavesTemplate(template);
        SessionTemplateServiceTesting.testUpdateTemplate(template);
        SessionTemplateServiceTesting.testUpdatesTemplate(template);
        SessionTemplateServiceTesting.testUseUpdateTemplate(template);
        SessionTemplateServiceTesting.testDeleteTemplate(template);
        SessionTemplateServiceTesting.testDeletesTemplate(template);
        SessionTemplateServiceTesting.testUseDeleteTemplate(template);
        SessionTemplateServiceTesting.testDeleteKeyTemplate(template);
        SessionTemplateServiceTesting.testGetKeyTemplate(template);
        SessionTemplateServiceTesting.testGetByQueryTemplate(template);
        SessionTemplateServiceTesting.testListByQueryTemplate_1(template);
        SessionTemplateServiceTesting.testCountTemplate(template);
        SessionTemplateServiceTesting.testCalculateTemplate(template);

    }

    public static void addJavaTypes(final SessionTemplate template) {
        ModelObject object = new ModelObject(TableJavaTypes.class);
        object.put(TableJavaTypes.text, RandomUtils.randomChineseCharacters(10));
        object.put(TableJavaTypes.mediumText, RandomUtils.randomChineseCharacters(20));
        object.put(TableJavaTypes.doubleType, 10.123123);
        object.put(TableJavaTypes.decimal, 20.678901);
        object.put(TableJavaTypes.stringType, RandomUtils.randomAlphanumericLetter(5));
        object.put(TableJavaTypes.charType, "m");
        object.put(TableJavaTypes.date, new Date());
        // object.put(TableJavaTypes.blob, RandomUtils.randomChineseCharacters(10));
        object.put(TableJavaTypes.clob, RandomUtils.randomChineseCharacters(10));
        object.put(TableJavaTypes.shortType, 120);
        object.put(TableJavaTypes.byteType, 30);
        object.put(TableJavaTypes.longType, 100000000000L);
        object.put(TableJavaTypes.floatType, 989898998234F);
        object.put(TableJavaTypes.booleanType, true);
        object.put(TableJavaTypes.timestamp, new Date());
        template.save(object);
    }
}
