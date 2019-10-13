package session;

import com.alibaba.druid.pool.DruidDataSource;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.mimosaframework.core.json.ModelObject;
import org.mimosaframework.core.utils.AssistUtils;
import org.mimosaframework.core.utils.RandomUtils;
import org.mimosaframework.orm.*;
import org.mimosaframework.orm.auxiliary.CacheClient;
import org.mimosaframework.orm.criteria.Criteria;
import org.mimosaframework.orm.exception.ContextException;
import tables.TableContacts;

import javax.sql.DataSource;
import java.io.IOException;
import java.util.*;

public class RedisStrategyTesting {
    private SessionTemplate template = null;

    @Before
    public void init() throws ContextException {
        XmlAppContext context = new XmlAppContext(SessionFactoryBuilder.class.getResourceAsStream("/redis-incr-mimosa.xml"));
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
    public void save() {
        List<Long> ids = new ArrayList<>();
        Map<Long, ModelObject> map = new LinkedHashMap<>();
        for (int i = 0; i < 20; i++) {
            ModelObject object = ModelObject.builder(TableContacts.class)
                    .chainPut(TableContacts.name, "yak_" + RandomUtils.randomIgnoreCaseLetter(6))
                    .chainPut(TableContacts.phone, "159" + RandomUtils.randomNumber(9));
            long t = System.currentTimeMillis();
            template.save(object);
            System.out.println("添加一次耗时: " + (System.currentTimeMillis() - t) + "ms");
            ids.add(object.getLongValue(TableContacts.id));
            map.put(object.getLongValue(TableContacts.id), object);
            System.out.println(object.getLongValue(TableContacts.id));
        }
        List<ModelObject> objects = template.query(TableContacts.class).in(TableContacts.id, ids).queries();

        if (objects == null || objects.size() == 0) {
            AssistUtils.error("没有查询到");
        }

        for (ModelObject o : objects) {
            ModelObject s = map.get(o.getLongValue(TableContacts.id));
            if (!o.getString(TableContacts.name).equals(s.getString(TableContacts.name))
                    || !o.getString(TableContacts.phone).equals(s.getString(TableContacts.phone))) {
                AssistUtils.error("查询的数据有错误");
            }
        }
        // template.delete(TableContacts.class).gt(TableContacts.id, 0).delete();
    }

    @Test
    public void testIdRepeat() throws IOException, InterruptedException {
        for (int i = 0; i < 1; i++) {
            AutoResult d = template.calculate(Criteria.fun(TableContacts.class).addFunction(BasicFunction.MAX, TableContacts.id, "max"));
            long max = d.getSingle().getLongValue("max");
            ModelObject object = ModelObject.builder(TableContacts.class)
                    .chainPut(TableContacts.id, max)
                    .chainPut(TableContacts.name, "yak_" + RandomUtils.randomIgnoreCaseLetter(6))
                    .chainPut(TableContacts.phone, "159" + RandomUtils.randomNumber(9));

            template.save(object);

            CacheClient client = template.getCacheClient(TableContacts.class.getName());
            client.del("id_incr_tablecontacts");
            client.close();

            Thread.sleep(RandomUtils.randomNumber(50, 3000));

            List<ModelObject> objects = template.query(TableContacts.class)
                    .eq(TableContacts.id, object.getLongValue(TableContacts.id)).queries();
            if (objects.size() > 1) {
                AssistUtils.error("出错啦 同一个ID命中多个数据库" + object.getLongValue(TableContacts.id));
            }
        }
    }
}
