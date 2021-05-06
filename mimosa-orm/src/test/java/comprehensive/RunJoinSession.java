package comprehensive;

import org.junit.Before;
import org.junit.Test;
import org.mimosaframework.core.json.ModelObject;
import org.mimosaframework.core.utils.RandomUtils;
import org.mimosaframework.orm.*;
import org.mimosaframework.orm.criteria.Criteria;
import org.mimosaframework.orm.exception.ContextException;
import tables.TableOrder;
import tables.TablePay;
import tables.TableUser;

import java.util.List;

public class RunJoinSession {
    private SessionTemplate template;

    @Before
    public void init() throws ContextException {
        if (template == null) {
            template = RunDataSourceBuilder.currTemplate();
        }
    }

    @Test
    public void testLeftJoin() {
        List<ModelObject> objects = template.list(Criteria.query(TableUser.class)
                .subjoin(Criteria.left(TablePay.class).on(TablePay.userId, TableUser.id).aliasName("pays"))
                .limit(0, 3));
        System.out.println(objects);
    }

    @Test
    public void testInnerJoin() {
        for (int i = 0; i < 20; i++) {
            ModelObject user = new ModelObject(TableUser.class);
            user.put(TableUser.userName, RandomUtils.randomIgnoreCaseAlphanumeric(10));
            template.save(user);

            ModelObject pay = new ModelObject(TablePay.class);
            pay.put(TablePay.userId, user.getIntValue(TableUser.id));
            template.save(pay);
            pay.remove(TablePay.id);
            template.save(pay);
        }

        List<ModelObject> objects = template.list(Criteria.query(TableUser.class)
                .subjoin(Criteria.inner(TablePay.class).on(TableUser.id, TablePay.userId).single().aliasName("pays"))
                .limit(0, 20));
        System.out.println(objects.size());
        System.out.println(objects);
    }

    @Test
    public void testChildJoin() {
        List<ModelObject> objects = template.list(Criteria.query(TableUser.class)
                .subjoin(
                        Criteria.inner(TablePay.class)
                                .subjoin(Criteria.left(TableOrder.class).on(TableOrder.userId, TablePay.userId).aliasName("orders"))
                                .on(TablePay.userId, TableUser.id).single().aliasName("pays")
                )
                .limit(0, 20));
        System.out.println(objects);
    }

    @Test
    public void testInnerJoinNoLimit() {
        List<ModelObject> objects = template.list(Criteria.query(TableUser.class)
                .subjoin(Criteria.inner(TablePay.class).on(TablePay.userId, TableUser.id).single().aliasName("pays"))
                .gte(TableUser.id, 1).lte(TableUser.id, 20));
        System.out.println(objects.size());
        System.out.println(objects);
    }

    @Test
    public void testIgnore() {
        List<ModelObject> objects = template.list(Criteria.query(TableUser.class)
                .subjoin(
                        Criteria.inner(TablePay.class)
                                .subjoin(Criteria.left(TableOrder.class).on(TablePay.userId, TableOrder.userId).aliasName("orders"))
                                .on(TablePay.userId, TableUser.id).ignore().single().aliasName("pays")
                )
                .limit(0, 3));
        System.out.println(objects);
    }

    @Test
    public void testCustomTableAlias() {
        List<ModelObject> objects = template.list(Criteria.query(TableUser.class)
                .subjoin(
                        Criteria.inner(TablePay.class)
                                .subjoin(
                                        Criteria.left(TableOrder.class)
                                                .on(TablePay.userId, TableOrder.userId)
                                                .aliasName("orders").as("order")
                                ).on(TablePay.userId, TableUser.id).aliasName("pays").as("pay")
                )
                .filter(Criteria.filter()
                        .eq("status", "10")
//                        .eq("id", "1")
                        .as("pays"))
                .limit(0, 3));
        System.out.println(objects);
    }
}
