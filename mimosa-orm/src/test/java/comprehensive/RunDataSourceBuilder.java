package comprehensive;

import com.alibaba.druid.pool.DruidDataSource;
import org.mimosaframework.orm.*;
import org.mimosaframework.orm.exception.ContextException;
import org.mimosaframework.orm.utils.DatabaseType;

import javax.sql.DataSource;

public class RunDataSourceBuilder {
    private static final DatabaseType type = DatabaseType.DB2;
//    private static final DatabaseType type = DatabaseType.MYSQL;
//    private static final DatabaseType type = DatabaseType.ORACLE;
//    private static final DatabaseType type = DatabaseType.SQLITE;
//    private static final DatabaseType type = DatabaseType.SQL_SERVER;
//    private static final DatabaseType type = DatabaseType.POSTGRESQL;

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

        }
        if (type == DatabaseType.SQL_SERVER) {

        }
        if (type == DatabaseType.POSTGRESQL) {

        }
        if (type == DatabaseType.SQLITE) {

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
}
