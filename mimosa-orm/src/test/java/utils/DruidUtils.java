package utils;

import com.alibaba.druid.pool.DruidDataSource;
import org.mimosaframework.orm.MimosaDataSource;

import javax.sql.DataSource;
import java.util.Set;

public class DruidUtils {

    public static void printDruidCount() {
        System.out.println("$:");
        Set<DataSource> dataSources = MimosaDataSource.getAllDataSources();
        for (DataSource ds : dataSources) {
            DruidDataSource dds = (DruidDataSource) ds;
            System.out.println("Active:" + dds.getActiveCount()
                    + "  ConnectionCount:" + dds.getConnectCount()
                    + "  CloseCount:" + dds.getCloseCount());
        }
    }
}
