package session;

import com.alibaba.druid.pool.DruidDataSource;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.mimosaframework.core.json.ModelObject;
import org.mimosaframework.core.utils.RandomUtils;
import org.mimosaframework.orm.*;
import org.mimosaframework.orm.criteria.Criteria;
import org.mimosaframework.orm.exception.ContextException;
import tables.TableDisperseByOrder;
import tables.TableDisperseByUser;

import javax.sql.DataSource;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class DisperseStrategyTesting {
    private SessionTemplate template = null;

    @Before
    public void init() throws ContextException {
        XmlAppContext context = new XmlAppContext(SessionFactoryBuilder.class.getResourceAsStream("/disperse-strategy-mimosa.xml"));
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
    public void testSplitByUserId() {
        for (int j = 0; j < 20; j++) {
            long uid = RandomUtils.randomNumber(10, 90000000000l);
            int c = (int) RandomUtils.randomNumber(5, 10);
            List<String> strings = new ArrayList<>();
            for (int i = 0; i < c; i++) {
                ModelObject object = new ModelObject(TableDisperseByUser.class);
                object.put(TableDisperseByUser.userId, uid);
                object.put(TableDisperseByUser.detail, RandomUtils.randomIgnoreCaseLetter(5, 10));
                strings.add(object.getString(TableDisperseByUser.detail));
                template.save(object);
            }
            List<ModelObject> objectList = template.list(Criteria.query(TableDisperseByUser.class).eq(TableDisperseByUser.userId, uid));
            List<String> gets = new ArrayList<>();
            for (ModelObject object : objectList) {
                String detail = object.getString(TableDisperseByUser.detail);
                gets.add(detail);
            }

            if (objectList.size() < c) {
                throw new IllegalStateException("分表查询数量有问题");
            }

            for (String s : strings) {
                if (!gets.contains(s)) {
                    throw new IllegalArgumentException("本来应该有的 但是没有");
                }
            }
        }
    }

    @Test
    public void testSplitAndJoin() {
        for (int j = 0; j < 20; j++) {
            long uid = RandomUtils.randomNumber(10, 90000000000l);
            int c = (int) RandomUtils.randomNumber(5, 10);
            List<String> strings = new ArrayList<>();
            for (int i = 0; i < c; i++) {
                ModelObject object = new ModelObject(TableDisperseByUser.class);
                object.put(TableDisperseByUser.userId, uid);
                object.put(TableDisperseByUser.detail, RandomUtils.randomIgnoreCaseLetter(5, 10));
                strings.add(object.getString(TableDisperseByUser.detail));
                template.save(object);
            }

            int c2 = (int) RandomUtils.randomNumber(1, 3);
            List<String> strings2 = new ArrayList<>();
            for (int i = 0; i < c2; i++) {
                ModelObject object = new ModelObject(TableDisperseByOrder.class);
                object.put(TableDisperseByOrder.userId, uid);
                object.put(TableDisperseByOrder.detail, RandomUtils.randomIgnoreCaseLetter(5, 10));
                strings2.add(object.getString(TableDisperseByOrder.detail));
                template.save(object);
            }

            List<ModelObject> objectList = template.list(Criteria.query(TableDisperseByUser.class).eq(TableDisperseByUser.userId, uid)
                    .subjoin(TableDisperseByOrder.class).eq(TableDisperseByOrder.userId, TableDisperseByUser.userId)
                    .query());
            List<String> gets = new ArrayList<>();
            for (ModelObject object : objectList) {
                String detail = object.getString(TableDisperseByUser.detail);
                gets.add(detail);
            }

            if (objectList.size() < c) {
                throw new IllegalStateException("分表查询数量有问题");
            }

            for (String s : strings) {
                if (!gets.contains(s)) {
                    throw new IllegalArgumentException("本来应该有的 但是没有");
                }
            }
        }
    }
}
