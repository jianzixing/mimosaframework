package comprehensive;

import com.alibaba.druid.pool.DruidDataSource;
import org.mimosaframework.orm.*;
import org.mimosaframework.orm.exception.ContextException;
import org.mimosaframework.orm.utils.DatabaseType;

import javax.sql.DataSource;

public class RunDataSourceBuilder {
    //    private static final DatabaseType type = DatabaseType.DB2;
//    private static final DatabaseType type = DatabaseType.MYSQL;
//    private static final DatabaseType type = DatabaseType.ORACLE;
//    private static final DatabaseType type = DatabaseType.POSTGRESQL;
    private static final DatabaseType type = DatabaseType.SQL_SERVER;
//    private static final DatabaseType type = DatabaseType.SQLITE;

    public static SessionTemplate currTemplate() throws ContextException {
        return getTemplate(type);
    }

    public static DataSource currDataSource() throws ContextException {
        return getDataSource(type);
    }

    public static SessionTemplate getTemplate(DatabaseType type) throws ContextException {
        String config = null;
        if (type == DatabaseType.MYSQL) config = "/template-mimosa.xml";
        if (type == DatabaseType.DB2) config = "/db2-template-mimosa.xml";
        if (type == DatabaseType.ORACLE) config = "/oracle-template-mimosa.xml";
        if (type == DatabaseType.SQL_SERVER) config = "/sqlserver-template-mimosa.xml";
        if (type == DatabaseType.POSTGRESQL) config = "/postgresql-template-mimosa.xml";
        if (type == DatabaseType.SQLITE) config = "/sqlite-template-mimosa.xml";

        XmlAppContext context = new XmlAppContext(SessionFactoryBuilder.class.getResourceAsStream(config));
        SessionFactory sessionFactory = context.getSessionFactoryBuilder().build();
        SessionTemplate template = new MimosaSessionTemplate();
        ((MimosaSessionTemplate) template).setSessionFactory(sessionFactory);
        return template;
    }

    public static DataSource getDataSource(DatabaseType type) {
        if (type == DatabaseType.MYSQL) {
            return getMysqlDataSource();
        }
        if (type == DatabaseType.DB2) {
            return getDB2DataSource();
        }
        if (type == DatabaseType.ORACLE) {
            return getOracleDataSource();
        }
        if (type == DatabaseType.SQL_SERVER) {
            return getSQLServerSource();
        }
        if (type == DatabaseType.POSTGRESQL) {
            return getPostgreSQLSource();
        }
        if (type == DatabaseType.SQLITE) {
            return getSqliteSource();
        }
        return null;
    }

    public static DataSource getDB2DataSource() {
        DruidDataSource dataSource = new DruidDataSource();
        dataSource.setDriverClassName("com.ibm.db2.jcc.DB2Driver");
        dataSource.setUrl("jdbc:db2://127.0.0.1:50000/database");
        dataSource.setUsername("db2inst1");
        dataSource.setPassword("12345");
        return dataSource;
    }

    public static DataSource getMysqlDataSource() {
        DruidDataSource dataSource = new DruidDataSource();
        dataSource.setDriverClassName("com.mysql.jdbc.Driver");
        dataSource.setUrl("jdbc:mysql://localhost:3306/mimosa?useUnicode=true&characterEncoding=utf-8&useSSL=false&serverTimezone=UTC&nullNamePatternMatchesAll=true");
        dataSource.setUsername("root");
        dataSource.setPassword("12345");
        return dataSource;
    }

    public static DataSource getOracleDataSource() {
        DruidDataSource dataSource = new DruidDataSource();
        dataSource.setDriverClassName("oracle.jdbc.OracleDriver");
        dataSource.setUrl("jdbc:oracle:thin:@localhost:1521:XE");
        dataSource.setUsername("system");
        dataSource.setPassword("oracle");
        return dataSource;
    }

    public static DataSource getPostgreSQLSource() {
        DruidDataSource dataSource = new DruidDataSource();
        dataSource.setDriverClassName("org.postgresql.Driver");
        dataSource.setUrl("jdbc:postgresql://localhost:5432/mimosa");
        dataSource.setUsername("postgres");
        dataSource.setPassword("12345");
        return dataSource;
    }

    public static DataSource getSQLServerSource() {
        DruidDataSource dataSource = new DruidDataSource();
        dataSource.setDriverClassName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
        dataSource.setUrl("jdbc:sqlserver://127.0.0.1:1433;DatabaseName=mimosa");
        dataSource.setUsername("SA");
        dataSource.setPassword("MSPWD_12345");
        return dataSource;
    }

    public static DataSource getSqliteSource() {
        DruidDataSource dataSource = new DruidDataSource();
        dataSource.setUrl("jdbc:sqlite:mimosa.db");
        dataSource.setUsername("");
        dataSource.setPassword("");
        return dataSource;
    }
}
