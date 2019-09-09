package session;

import com.alibaba.druid.pool.DruidDataSource;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.mimosaframework.core.utils.AssistUtils;
import org.mimosaframework.core.utils.StringTools;
import org.mimosaframework.orm.*;
import org.mimosaframework.orm.auxiliary.CacheClient;
import org.mimosaframework.orm.auxiliary.CacheLockCallback;
import org.mimosaframework.orm.exception.ContextException;

import javax.sql.DataSource;
import java.util.Set;

public class CacheTesting {
    private SessionTemplate template = null;

    @Before
    public void init() throws ContextException {
        XmlAppContext context = new XmlAppContext(SessionFactoryBuilder.class.getResourceAsStream("/cache-mimosa.xml"));
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
    public void testCacheSave() throws Exception {
        CacheClient cacheClient = template.getCacheClient("redis1");

        String s = "good luck !";
        cacheClient.set("yangankang", s);

        String v = cacheClient.get("yangankang");
        if (!s.equals(v)) {
            AssistUtils.error("保存的和拿出来的不一样");
        }

        cacheClient.setExpireMilliseconds("ankang1", "2324", 1000);
        String v2 = cacheClient.get("ankang1");
        if (!v2.equals("2324")) {
            AssistUtils.error("保存的和拿出来的不一样");
        }
        Thread.sleep(1100);
        v2 = cacheClient.get("ankang1");
        if (StringTools.isNotEmpty(v2)) {
            AssistUtils.error("应该过期的");
        }
    }

    @Test
    public void testCacheLock() throws Exception {
        CacheClient cacheClient = template.getCacheClient("redis1");
        final boolean[] booleans = {false};
        cacheClient.lock("hello", new CacheLockCallback() {
            @Override
            public void execute() {
                booleans[0] = true;
            }
        }, 1000);
        Thread.sleep(100);
        cacheClient.lock("hello", new CacheLockCallback() {
            @Override
            public void execute() {
                booleans[0] = false;
            }
        }, 1000);
        if (!booleans[0]) {
            AssistUtils.error("第二个不应该执行");
        }
    }

    @Test
    public void testIncr() {
        CacheClient cacheClient = template.getCacheClient("redis1");
        cacheClient.set("key1", "6");
        long id1 = cacheClient.incr("key1");
        System.out.println(id1);
        long id2 = cacheClient.incrByLong("key2", 5);
        System.out.println(id2);
    }
}
