package comprehensive;


import org.junit.Before;
import org.junit.Test;
import org.mimosaframework.core.json.ModelObject;
import org.mimosaframework.core.utils.RandomUtils;
import org.mimosaframework.orm.*;
import org.mimosaframework.orm.criteria.Criteria;
import org.mimosaframework.orm.criteria.HavingField;
import org.mimosaframework.orm.exception.ContextException;
import tables.BeanPay;
import tables.BeanUser;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.Iterator;
import java.util.List;

public class RunBeanBaseSession {
    private BeanSessionTemplate template;

    @Before
    public void init() throws ContextException {
        if (template == null) {
            template = RunDataSourceBuilder.currBeanTemplate();
        }
    }

    @Test
    public void save() {
        BeanUser user = new BeanUser();
        user.setUserName(RandomUtils.randomIgnoreCaseAlphanumeric(30));
        user.setAge((int) RandomUtils.randomNumber(2));
        template.save(user);
        System.out.println(user);
    }

    @Test
    public void saveAndUpdate() {

    }

    @Test
    public void update() {
        BeanUser user = new BeanUser();
        user.setUserName(RandomUtils.randomIgnoreCaseAlphanumeric(30));
        user.setAge((int) RandomUtils.randomNumber(2));
        template.save(user);

        int id = user.getId();
        BeanUser update = new BeanUser();
        update.setId(id);
        update.setUserName(RandomUtils.randomIgnoreCaseAlphanumeric(30));
        update.setAge((int) RandomUtils.randomNumber(2));
        template.update(update);

        template.update(Criteria.update(BeanUser.class)
                .set("userName", RandomUtils.randomIgnoreCaseAlphanumeric(30))
                .eq("id", id));
    }


    @Test
    public void delete() {
        BeanUser user = new BeanUser();
        user.setUserName(RandomUtils.randomIgnoreCaseAlphanumeric(30));
        user.setAge((int) RandomUtils.randomNumber(2));
        template.save(user);

        int id = user.getId();
        BeanUser update = new BeanUser();
        update.setId(id);
        update.setUserName(RandomUtils.randomIgnoreCaseAlphanumeric(30));
        update.setAge((int) RandomUtils.randomNumber(2));
        template.delete(update);

        template.delete(Criteria.delete(BeanUser.class).eq("id", id));
    }

    @Test
    public void get() {
        BeanUser user = new BeanUser();
        user.setUserName(RandomUtils.randomIgnoreCaseAlphanumeric(30));
        user.setAge((int) RandomUtils.randomNumber(2));
        template.save(user);

        int id = user.getId();
        user = template.get(Criteria.query(BeanUser.class).eq("id", id));
        System.out.println(user.getUserName());
        user = template.get(BeanUser.class, id);
        System.out.println(user.getUserName());
    }

    @Test
    public void list() {
        List<BeanUser> objects = template.list(Criteria.query(BeanUser.class)
                .limit(0, 10));

        if (objects != null) {
            for (BeanUser user : objects) {
                System.out.println(user.getUserName());
            }
        }

        objects = template.list(Criteria.query(BeanUser.class)
                .fields("id", "userName")
                .excludes("level").limit(0, 10));
        if (objects != null) {
            for (BeanUser user : objects) {
                System.out.println(user.getUserName());
            }
        }
    }

    @Test
    public void count() {
        long count = template.count(Criteria.query(BeanUser.class)
                .limit(0, 10));
        System.out.println(count);
    }

    @Test
    public void paging() {
        Paging<BeanUser> paging = template.paging(Criteria.query(BeanUser.class)
                .limit(0, 10));
        System.out.println(paging);
        if (paging.getObjects() != null) {
            if (paging.getObjects() != null) {
                for (BeanUser user : paging.getObjects()) {
                    System.out.println(user.getUserName());
                }
            }
        }
    }

    @Test
    public void getZipperTable() {
        ZipperTable<BeanUser> zipperTable = template.getZipperTable(BeanUser.class);
        Iterator<BeanUser> iterator = zipperTable.iterator();
        while (iterator.hasNext()) {
            System.out.println(iterator.next().getUserName());
        }
    }

    @Test
    public void calculate() {
        AutoResult objects = template.calculate(Criteria.fun(BeanUser.class)
                .addFunction(BasicFunction.AVG, "id", "id")
                .lt("id", 1000));
        System.out.println(objects.getObjects());

        objects = template.calculate(Criteria.fun(BeanUser.class)
                .addFunction(BasicFunction.COUNT, "id", "count")
                .having(new HavingField(BasicFunction.COUNT).gt("age", 2))
                .lt("id", 1000));
        System.out.println(objects.getObjects());
    }

    @Test
    public void testLeftJoin() {
        for (int i = 0; i < 5; i++) {
            BeanUser user = new BeanUser();
            user.setUserName(RandomUtils.randomIgnoreCaseAlphanumeric(30));
            user.setAge((int) RandomUtils.randomNumber(2));
            template.save(user);

            BeanPay pay = new BeanPay();
            pay.setUserId(user.getId());
            pay.setMoney(new BigDecimal(RandomUtils.randomNumber(3)));
            template.save(pay);
            pay.setId(0);
            template.save(pay);
        }

        List<BeanUser> objects = template.list(Criteria.query(BeanUser.class)
                .subjoin(Criteria.left(BeanPay.class).on("id", "userId").aliasName("pays"))
                .limit(0, 2));
        if (objects != null && objects.size() > 0) {
            for (BeanUser beanUser : objects) {
                System.out.println(beanUser.getUserName());
                List<BeanPay> pays = beanUser.getPays();
                if (pays != null) {
                    for (BeanPay pay : pays) {
                        if (pay.getMoney() != null) {
                            System.out.println("--" + pay.getMoney().toPlainString());
                        }
                    }
                }
            }
        }
    }

    @Test
    public void testCFun() {
        BeanUser update = new BeanUser();
        update.setId(1);
        update.setUserName("ak1");
        update.setAge(19);

        template.saveOrUpdate(update);

        update.setId(1);
        update.setUserName("ak2");
        update.setAge(18);

        template.update(update, Criteria.nonFields(BeanUser.class, BeanUser::getId, BeanUser::getAge));
        BeanUser old = template.get(Criteria.query(BeanUser.class).eq(BeanUser::getId, 1));
        System.out.println(ModelObject.toJSONString(old));
        template.update(update, Criteria.nonFields(BeanUser.class));
        old = template.get(Criteria.query(BeanUser.class).eq(BeanUser::getId, 1));
        System.out.println(ModelObject.toJSONString(old));

        Object[] rs = Criteria.fields(BeanUser.class);
        System.out.println(ModelObject.toJSONString(rs));

    }


//    @Test
//    public void getAutonomously() throws Exception {
//        AutoResult result = template.getAutonomously(SQLAutonomously.select().all().from().table(BeanUser.class).autonomously());
//        System.out.println(result.getObjects());
//    }

    @Test
    public void getDataSourceNames() {
        List<DataSourceTableName> tableNames = template.getDataSourceNames(BeanUser.class);
        System.out.println(tableNames);
    }

    @Test
    public void close() throws IOException {

    }
}
