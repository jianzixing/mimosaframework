package program;

import com.alibaba.druid.pool.DruidDataSource;
import org.mimosaframework.core.utils.parallel.OneTimeEndCallback;
import org.mimosaframework.core.utils.parallel.ParallelItem;
import org.mimosaframework.core.utils.parallel.ParallelTask;
import org.mimosaframework.core.utils.parallel.ParallelUtils;
import org.mimosaframework.orm.*;
import org.mimosaframework.orm.exception.ContextException;
import org.mimosaframework.orm.exception.MimosaException;
import program.service.DistributeSessionTemplateServiceTesting;
import program.service.SessionTemplateServiceTesting;
import program.service.TransactionSessionServiceTesting;
import utils.DruidUtils;

import javax.sql.DataSource;
import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;
import java.util.Set;

public class ParallelSessionTemplateTesting {

    public static void run() throws Exception {
//        run1();
        run2();
//        run3();
    }

    public static void run1() throws ContextException {
        XmlAppContext context = new XmlAppContext(SessionFactoryBuilder.class.getResourceAsStream("/template-mimosa.xml"));
        SessionFactory sessionFactory = context.getSessionFactoryBuilder().build();
        final SessionTemplate template = new MimosaSessionTemplate();
        ((MimosaSessionTemplate) template).setSessionFactory(sessionFactory);
        ParallelUtils.run(20, 100, new ParallelTask<Boolean>() {
            @Override
            public Boolean call() throws Exception {
                long c = System.currentTimeMillis();
                SessionTemplateServiceTesting.concurrentTestSaveNormalTemplate(template);
                System.out.println("");
                DruidUtils.printDruidCount();
                System.out.println("执行一次耗时 " + (System.currentTimeMillis() - c) + " ms");
                return true;
            }
        }, new ParallelItem<Boolean>() {
            @Override
            public void done(int time, List<Boolean> list) {

            }
        });

        Set<DataSource> dataSources = MimosaDataSource.getAllDataSources();
        for (DataSource ds : dataSources) {
            DruidDataSource dds = (DruidDataSource) ds;
            dds.close();
        }
    }

    public static void run2() throws ContextException {
        XmlAppContext context = new XmlAppContext(SessionFactoryBuilder.class.getResourceAsStream("/distributed-template-mimosa.xml"));
        SessionFactory sessionFactory = context.getSessionFactoryBuilder().build();
        final SessionTemplate template = new MimosaSessionTemplate();
        ((MimosaSessionTemplate) template).setSessionFactory(sessionFactory);
        ParallelUtils.run(20, 100, new ParallelTask<Boolean>() {
            @Override
            public Boolean call() throws Exception {
                try {
                    long c = System.currentTimeMillis();
                    DistributeSessionTemplateServiceTesting.concurrentTestSaveNormalTemplate(template);
                    System.out.println("");
                    DruidUtils.printDruidCount();
                    System.out.println("执行一次耗时 " + (System.currentTimeMillis() - c) + " ms");
                } catch (Exception e) {
                    e.printStackTrace();
                    System.exit(0);
                }
                return true;
            }
        }, new ParallelItem<Boolean>() {
            @Override
            public void done(int time, List<Boolean> list) {

            }
        }, new OneTimeEndCallback() {
            @Override
            public void end() {
                Set<DataSource> dataSources = MimosaDataSource.getAllDataSources();
                for (DataSource ds : dataSources) {
                    DruidDataSource dds = (DruidDataSource) ds;
                    if (dds.getActiveCount() > 0) {
                        System.out.println("$$ Active:" + dds.getActiveCount()
                                + "  ConnectionCount:" + dds.getConnectCount()
                                + "  CloseCount:" + dds.getCloseCount());
                        throw new IllegalArgumentException("卧槽 有连接未关闭");
                    }
                }
            }
        });

        Set<DataSource> dataSources = MimosaDataSource.getAllDataSources();
        for (DataSource ds : dataSources) {
            DruidDataSource dds = (DruidDataSource) ds;
            dds.close();
        }
    }

    public static void run3() throws Exception {
        XmlAppContext context = new XmlAppContext(SessionFactoryBuilder.class.getResourceAsStream("/distributed-template-mimosa.xml"));
        SessionFactory sessionFactory = context.getSessionFactoryBuilder().build();
        final SessionTemplate template = new MimosaSessionTemplate();
        ((MimosaSessionTemplate) template).setSessionFactory(sessionFactory);

        for (int i = 0; i < 1; i++) {
            TransactionSessionServiceTesting.transForNormal(template);
            TransactionSessionServiceTesting.transForNormalSave(template);
            TransactionSessionServiceTesting.transDeleteFail(template);
            TransactionSessionServiceTesting.transDeleteSucc(template);
            TransactionSessionServiceTesting.transUpdateFail(template);
            TransactionSessionServiceTesting.transUpdateSucc(template);
            TransactionSessionServiceTesting.testSupports(template);
            TransactionSessionServiceTesting.testMandatory(template);
            TransactionSessionServiceTesting.testRequiresNew(template);
            TransactionSessionServiceTesting.testNotSupported(template);
            TransactionSessionServiceTesting.testNever(template);
            TransactionSessionServiceTesting.testNested(template);
            TransactionSessionServiceTesting.test2(template);

            System.out.println("$:");
            Set<DataSource> dataSources = MimosaDataSource.getAllDataSources();
            for (DataSource ds : dataSources) {
                DruidDataSource dds = (DruidDataSource) ds;
                System.out.println("Active:" + dds.getActiveCount()
                        + "  ConnectionCount:" + dds.getConnectCount()
                        + "  CloseCount:" + dds.getCloseCount());
            }
        }

        ParallelUtils.run(Integer.MAX_VALUE, 100, new ParallelTask<Boolean>() {
            @Override
            public Boolean call() throws Exception {
                try {
                    long c = System.currentTimeMillis();
                    TransactionSessionServiceTesting.transForNormal(template);
                    TransactionSessionServiceTesting.transForNormalSave(template);
                    TransactionSessionServiceTesting.transDeleteFail(template);
                    TransactionSessionServiceTesting.transDeleteSucc(template);
                    TransactionSessionServiceTesting.transUpdateFail(template);
                    TransactionSessionServiceTesting.transUpdateSucc(template);
                    TransactionSessionServiceTesting.testSupports(template);
                    TransactionSessionServiceTesting.testMandatory(template);
                    TransactionSessionServiceTesting.testRequiresNew(template);
                    TransactionSessionServiceTesting.testNotSupported(template);
                    TransactionSessionServiceTesting.testNever(template);
                    TransactionSessionServiceTesting.testNested(template);
                    TransactionSessionServiceTesting.test2(template);

//                    System.out.println("");
//                    Set<DataSource> dataSources = MimosaDataSource.getAllDataSources();
//                    for (DataSource ds : dataSources) {
//                        DruidDataSource dds = (DruidDataSource) ds;
//                        System.out.println("Active:" + dds.getActiveCount()
//                                + "  ConnectionCount:" + dds.getConnectCount()
//                                + "  CloseCount:" + dds.getCloseCount());
//                    }
                    System.out.println("执行一次耗时 " + (System.currentTimeMillis() - c) + " ms");
                } catch (Exception e) {
                    e.printStackTrace();
                    System.exit(0);
                }
                return true;
            }
        }, new ParallelItem<Boolean>() {
            @Override
            public void done(int time, List<Boolean> list) {

            }
        }, new OneTimeEndCallback() {
            @Override
            public void end() {
                Set<DataSource> dataSources = MimosaDataSource.getAllDataSources();
                for (DataSource ds : dataSources) {
                    DruidDataSource dds = (DruidDataSource) ds;
                    if (dds.getActiveCount() > 0) {
                        System.out.println("$$ Active:" + dds.getActiveCount()
                                + "  ConnectionCount:" + dds.getConnectCount()
                                + "  CloseCount:" + dds.getCloseCount());
                        throw new IllegalArgumentException("卧槽 有连接未关闭");
                    }
                }
            }
        });

        Set<DataSource> dataSources = MimosaDataSource.getAllDataSources();
        for (DataSource ds : dataSources) {
            DruidDataSource dds = (DruidDataSource) ds;
            dds.close();
        }
    }
}
