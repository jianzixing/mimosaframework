package compare;

import com.alibaba.druid.pool.DruidDataSource;
import org.junit.Test;
import org.mimosaframework.orm.platform.*;
import org.mimosaframework.orm.utils.SessionUtils;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class TestCompareDB {

    @Test
    public void test1() throws SQLException {
        DruidDataSource dataSource = new DruidDataSource();
        dataSource.setUrl("jdbc:mysql://localhost:3306/dm_builder?serverTimezone=GMT%2B8&useSSL=true&useUnicode=true&characterEncoding=utf8");
        dataSource.setDriverClassName("com.mysql.jdbc.Driver");
        dataSource.setUsername("root");
        dataSource.setPassword("12345");
        SessionContext context = SessionUtils.buildSessionContext(dataSource);
        PlatformExecutor executor = PlatformExecutorFactory.getExecutor(null, context);
        PlatformDialect dialect = executor.getDialect();
        List<String> tables = new ArrayList<>();
        tables.add("t_project_history_page");
        List<TableStructure> structures = dialect.getTableStructures(tables);
        List<TableColumnStructure> columnStructures = dialect.getColumnsStructures("dm_builder", tables);
        if (structures != null) {
            for (TableStructure structure : structures) {
                System.out.println(structure.getTableName() + "  ");
            }
        }
        for (TableColumnStructure columnStructure : columnStructures) {
            System.out.println(columnStructure.getColumnName());
        }
    }
}
