package session;

import com.alibaba.druid.pool.DruidDataSource;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.mimosaframework.core.json.ModelObject;
import org.mimosaframework.orm.*;
import org.mimosaframework.orm.exception.ContextException;
import tables.TablePay;

import javax.sql.DataSource;
import java.io.IOException;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class ZipperTableTesting {
    private SessionTemplate template = null;

    @Before
    public void init() throws ContextException {
        XmlAppContext context = new XmlAppContext(SessionFactoryBuilder.class.getResourceAsStream("/distributed-template-mimosa.xml"));
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
    public void testByJdbc() throws ClassNotFoundException, SQLException {
        String url = "jdbc:mysql://127.0.0.1:3306/mimosa_1?useUnicode=true&characterEncoding=utf-8&useSSL=false&serverTimezone=UTC&nullNamePatternMatchesAll=true";
        //1.加载驱动程序
        Class.forName("com.mysql.jdbc.Driver");
        //2.获得数据库链接
        Connection conn = DriverManager.getConnection(url, "root", "12345");

        // 3.创建statement
        PreparedStatement st = conn.prepareStatement("select * from t_pay_1", ResultSet.TYPE_FORWARD_ONLY, ResultSet.CONCUR_READ_ONLY);
        st.setFetchSize(Integer.MIN_VALUE);

        long begin = System.currentTimeMillis();
        ResultSet rs = st.executeQuery();
        //4.处理数据库的返回结果(使用ResultSet类)
        while (rs.next()) {
            System.out.println(rs.getLong("id"));
        }
        long end = System.currentTimeMillis();
        System.out.println("花费时间: " + (end - begin) + "豪秒");

        //关闭资源
        rs.close();
        st.close();
        conn.close();
    }

    @Test
    public void testByJdbc2() throws ClassNotFoundException, SQLException {
        String url = "jdbc:mysql://127.0.0.1:3306/mimosa?useUnicode=true&characterEncoding=utf-8&useSSL=false&serverTimezone=UTC&nullNamePatternMatchesAll=true";
        Class.forName("com.mysql.jdbc.Driver");
        Connection conn = DriverManager.getConnection(url, "root", "12345");
        PreparedStatement st = conn.prepareStatement("select * from t_pay");
        ResultSet rs = st.executeQuery();
        ResultSetMetaData data = rs.getMetaData();
        while (rs.next()) {
            for (int i = 1; i <= data.getColumnCount(); i++) {
//获得所有列的数目及实际列数
                int columnCount = data.getColumnCount();
//获得指定列的列名
                String columnName = data.getColumnName(i);
//获得指定列的列值
                String columnValue = rs.getString(i);
//获得指定列的数据类型
                int columnType = data.getColumnType(i);
//获得指定列的数据类型名
                String columnTypeName = data.getColumnTypeName(i);
//所在的Catalog名字
                String catalogName = data.getCatalogName(i);
//对应数据类型的类
                String columnClassName = data.getColumnClassName(i);
//在数据库中类型的最大字符个数
                int columnDisplaySize = data.getColumnDisplaySize(i);
//默认的列的标题
                String columnLabel = data.getColumnLabel(i);
//获得列的模式
                String schemaName = data.getSchemaName(i);
//某列类型的精确度(类型的长度)
                int precision = data.getPrecision(i);
//小数点后的位数
                int scale = data.getScale(i);
//获取某列对应的表名
                String tableName = data.getTableName(i);
// 是否自动递增
                boolean isAutoInctement = data.isAutoIncrement(i);
//在数据库中是否为货币型
                boolean isCurrency = data.isCurrency(i);
//是否为空
                int isNullable = data.isNullable(i);
//是否为只读
                boolean isReadOnly = data.isReadOnly(i);
//能否出现在where中
                boolean isSearchable = data.isSearchable(i);
                System.out.println(columnCount);
                System.out.println("获得列" + i + "的字段名称:" + columnName);
                System.out.println("获得列" + i + "的字段值:" + columnValue);
                System.out.println("获得列" + i + "的类型,返回SqlType中的编号:" + columnType);
                System.out.println("获得列" + i + "的数据类型名:" + columnTypeName);
                System.out.println("获得列" + i + "所在的Catalog名字:" + catalogName);
                System.out.println("获得列" + i + "对应数据类型的类:" + columnClassName);
                System.out.println("获得列" + i + "在数据库中类型的最大字符个数:" + columnDisplaySize);
                System.out.println("获得列" + i + "的默认的列的标题:" + columnLabel);
                System.out.println("获得列" + i + "的模式:" + schemaName);
                System.out.println("获得列" + i + "类型的精确度(类型的长度):" + precision);
                System.out.println("获得列" + i + "小数点后的位数:" + scale);
                System.out.println("获得列" + i + "对应的表名:" + tableName);
                System.out.println("获得列" + i + "是否自动递增:" + isAutoInctement);
                System.out.println("获得列" + i + "在数据库中是否为货币型:" + isCurrency);
                System.out.println("获得列" + i + "是否为空:" + isNullable);
                System.out.println("获得列" + i + "是否为只读:" + isReadOnly);
                System.out.println("获得列" + i + "能否出现在where中:" + isSearchable);
            }
        }
        //关闭资源
        rs.close();
        st.close();
        conn.close();
    }

    @Test
    public void testContrastDatabase() throws SQLException {
        DruidDataSource druidDataSource = new DruidDataSource();
        druidDataSource.setDriverClassName("com.mysql.jdbc.Driver");
        druidDataSource.setUrl("jdbc:mysql://localhost:3306/dbt4?useUnicode=true&amp;characterEncoding=utf-8&amp;useSSL=false&amp;serverTimezone=UTC&amp;nullNamePatternMatchesAll=true");
        druidDataSource.setUsername("root");
        druidDataSource.setPassword("12345");
        druidDataSource.setInitialSize(5);
    }


    @Test
    public void testZipperTable() throws IOException {
        ZipperTable<ModelObject> zipperTable = template.getZipperTable(TablePay.class);
        int i = 1;
        List<Integer> ids = new ArrayList<>();
        long time = System.currentTimeMillis();
        for (ModelObject object : zipperTable) {
            // System.out.println(i + "  :  " + object.toJSONString());
            System.out.println(i);
            if (ids.contains(object.getIntValue(TablePay.id))) {
                throw new IllegalArgumentException("完蛋！有重复");
            }
            ids.add(object.getIntValue(TablePay.id));
            i++;
        }
        zipperTable.close();
        System.out.println("执行结束 耗时 : " + (((double) (System.currentTimeMillis() - time)) / 1000) + "s");
    }
}
