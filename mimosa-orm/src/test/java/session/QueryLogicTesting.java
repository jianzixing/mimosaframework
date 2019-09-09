package session;

import com.alibaba.druid.pool.DruidDataSource;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.mimosaframework.core.json.ModelObject;
import org.mimosaframework.core.utils.RandomUtils;
import org.mimosaframework.orm.*;
import org.mimosaframework.orm.criteria.Criteria;
import org.mimosaframework.orm.criteria.LogicLinked;
import org.mimosaframework.orm.exception.ContextException;
import tables.TableOrder;
import tables.TablePay;
import tables.TableQuery;
import tables.TableUser;

import javax.sql.DataSource;
import java.util.List;
import java.util.Set;

public class QueryLogicTesting {
    private SessionTemplate template = null;

    @Before
    public void init() throws ContextException {
        XmlAppContext context = new XmlAppContext(SessionFactoryBuilder.class.getResourceAsStream("/query-logic-mimosa.xml"));
        SessionFactory sessionFactory = context.getSessionFactoryBuilder().build();
        template = new MimosaSessionTemplate();
        ((MimosaSessionTemplate) template).setSessionFactory(sessionFactory);
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
    public void testNormalQuery() {
        List<ModelObject> r = template.list(Criteria.query(TableUser.class).eq(TableUser.id, 15));
        System.out.println(r);
    }

    @Test
    public void testQuery1() {
        List<ModelObject> r = template.list(
                Criteria.query(TableUser.class).gt(TableUser.id, 10)
                        .or(Criteria.filter().gt(TableUser.age, 10))
                        .and(Criteria.filter().isNotNull(TableUser.userName))
                        .limit(0, 10)
        );
        System.out.println(r);
    }

    @Test
    public void testQuery2() {
        List<ModelObject> r = template.list(
                Criteria.query(TableUser.class)
                        .addLinked(
                                LogicLinked.getInstance()
                                        .orwrap(LogicLinked.getInstance().eq(TableUser.id, 16).and(Criteria.filter().gt(TableUser.age, 10)))
                                        .orwrap(LogicLinked.getInstance().eq(TableUser.id, 17).and(Criteria.filter().gt(TableUser.age, 10)))
                                        .orwrap(LogicLinked.getInstance().eq(TableUser.id, 18).and(Criteria.filter().gt(TableUser.age, 10)))
                                        .orwrap(LogicLinked.getInstance().eq(TableUser.id, 19).and(Criteria.filter().gt(TableUser.age, 10))))
                        .limit(0, 10)
        );
        System.out.println(r);
    }

    @Test
    public void testQuery3() {
        List<ModelObject> r = template.list(
                Criteria.query(TableUser.class)
                        .addLinked(
                                LogicLinked.getInstance()
                                        .orwrap(LogicLinked.getInstance().eq(TableUser.id, 16).and(Criteria.filter().gt(TableUser.age, 10)))
                                        .orwrap(LogicLinked.getInstance().eq(TableUser.id, 17).and(Criteria.filter().gt(TableUser.age, 10)))
                                        .orwrap(LogicLinked.getInstance().wrap(
                                                LogicLinked.getInstance().eq(TableUser.id, 18).and(Criteria.filter().gt(TableUser.age, 10)))
                                                .lte(TableUser.level, 10))
                                        .orwrap(LogicLinked.getInstance().eq(TableUser.id, 19).and(Criteria.filter().gt(TableUser.age, 10))))
                        .limit(0, 10)
        );
        System.out.println(r);
    }


    @Test
    public void testQuery8() {
        List<ModelObject> r = template.list(
                Criteria.query(TablePay.class)
                        .addLinked(
                                LogicLinked.getInstance()
                                        .orwrap(LogicLinked.getInstance().eq(TablePay.id, 16).and(Criteria.filter().gt(TablePay.payMoney, 10)))
                                        .orwrap(LogicLinked.getInstance().eq(TablePay.id, 17).and(Criteria.filter().gt(TablePay.payMoney, 10)))
                                        .orwrap(LogicLinked.getInstance().eq(TablePay.id, 18).and(Criteria.filter().gt(TablePay.payMoney, 10)))
                                        .orwrap(LogicLinked.getInstance().eq(TablePay.id, 19).and(Criteria.filter().gt(TablePay.payMoney, 10))))
                        .limit(0, 10)
        );
        System.out.println(r);
    }

    @Test
    public void testQuery4() {
        List<ModelObject> r = template.list(
                Criteria.query(TableUser.class)
                        .subjoin(TableOrder.class)
                        .eq(TableOrder.userId, TableUser.id)
                        .query()
                        .eq(TableUser.id, 20)
        );
        System.out.println(r);
    }

    @Test
    public void testQuery5() {
        long time = System.currentTimeMillis();
        List<ModelObject> r = template.list(
                Criteria.query(TableUser.class)
                        .subjoin(TablePay.class)
                        .eq(TablePay.userId, TableUser.id)
                        .query()
                        .eq(TableUser.id, 20)
        );
        System.out.println("time " + (System.currentTimeMillis() - time) + "ms");
        System.out.println(r);
    }

    @Test
    public void testQuery6() {
        List<ModelObject> r = template.list(
                Criteria.query(TablePay.class)
                        .order(TablePay.id, false)
                        .limit(0, 10)
        );
        if (r != null) {
            for (ModelObject object : r) {
                System.out.println(object.getLongValue(TablePay.id));
            }
        }
    }

    @Test
    public void testQuery7() {
        for (int i = 0; i < 30; i++) {
            ModelObject object = new ModelObject(TableQuery.class);
            object.put(TableQuery.ca, RandomUtils.randomNumber(10, 15));
            object.put(TableQuery.cb, RandomUtils.randomNumber(100, 105));
            object.put(TableQuery.cc, RandomUtils.randomNumber(1000, 9000));
            template.save(object);
        }
        List<ModelObject> r = template.list(
                Criteria.query(TableQuery.class)
                        .order(TableQuery.cb, false)
                        .order(TableQuery.ca, false)
                        .order(TableQuery.cc, true)
                        .limit(0, 10)
        );
        if (r != null) {
            for (ModelObject object : r) {
                System.out.println(object.getLongValue(TableQuery.cb) + "  " + object.getBigDecimal(TableQuery.ca) + "  " + object.getBigDecimal(TableQuery.cc));
            }
        }

        template.delete(Criteria.delete(TableQuery.class).gt(TableQuery.id, 0));
    }
}
