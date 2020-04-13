package comprehensive;

import com.alibaba.druid.pool.DruidDataSource;
import org.junit.Test;
import org.mimosaframework.orm.*;
import org.mimosaframework.orm.exception.ContextException;
import org.mimosaframework.orm.mapping.*;
import org.mimosaframework.orm.platform.DataSourceWrapper;

import javax.sql.DataSource;
import java.sql.SQLException;
import java.util.*;

public class RunCompareTesting {
    private MimosaDataSource mimosaDataSource;

    @Test
    public void runAll() throws ContextException, SQLException {
        DataSource[] dataSources = new DataSource[]{
                this.getMysqlDataSource()
        };

        for (DataSource dataSource : dataSources) {
            mimosaDataSource = new MimosaDataSource(dataSource, "mater");
            // this.test1();
            // this.test2();
            this.test3();
        }
    }

    private DataSource getMysqlDataSource() {
        DruidDataSource dataSource = new DruidDataSource();
        dataSource.setDriverClassName("com.mysql.jdbc.Driver");
        dataSource.setUrl("jdbc:mysql://localhost:3306/mimosa?useUnicode=true&characterEncoding=utf-8&useSSL=false&serverTimezone=UTC&nullNamePatternMatchesAll=true");
        dataSource.setUsername("root");
        dataSource.setPassword("12345");
        return dataSource;
    }

    public StartCompareMapping getMapping(MappingLevel level, MappingTable mappingTable) {
        MappingGlobalWrapper mappingGlobalWrapper = new MappingGlobalWrapper();
        Set<MappingTable> mappingTables = new LinkedHashSet<>();
        mappingTables.add(mappingTable);
        mappingGlobalWrapper.setMappingTables(mappingTables);

        NormalContextContainer contextContainer = new NormalContextContainer();
        contextContainer.setIgnoreEmptySlave(true);
        contextContainer.setShowSQL(true);
        DataSourceWrapper dataSourceWrapper = new DataSourceWrapper(contextContainer);
        dataSourceWrapper.setDataSource(this.mimosaDataSource);

        StartCompareMapping compareMapping = CompareMappingFactory.getCompareMapping(
                level,
                mappingGlobalWrapper,
                dataSourceWrapper
        );
        return compareMapping;
    }

    public void test1() throws SQLException {
        SpecificMappingTable mappingTable = new SpecificMappingTable();
        mappingTable.setMappingClass(RunCompareTesting.class);
        mappingTable.setMappingTableName("t_run_test");
        mappingTable.setEncoding("utf8");
        Map<String, MappingField> mappingFields = new LinkedHashMap<>();

        SpecificMappingField mappingField1 = new SpecificMappingField();
        mappingField1.setMappingColumnName("id");
        mappingField1.setMappingFieldType(Long.class);
        mappingField1.setMappingFieldPrimaryKey(true);
        mappingField1.setMappingFieldAutoIncrement(true);
        mappingField1.setMappingFieldComment("测试id备注");
        mappingFields.put("id", mappingField1);

        SpecificMappingField mappingField2 = new SpecificMappingField();
        mappingField2.setMappingColumnName("name");
        mappingField2.setMappingFieldType(String.class);
        mappingField2.setMappingFieldLength(30);
        mappingField2.setMappingFieldNullable(false);
        mappingField2.setMappingFieldComment("测试name备注");
        mappingFields.put("name", mappingField2);

        mappingTable.setMappingFields(mappingFields);

        StartCompareMapping compareMapping = this.getMapping(MappingLevel.NOTHING, mappingTable);
        compareMapping.doMapping();

        StartCompareMapping compareMapping2 = this.getMapping(MappingLevel.CREATE, mappingTable);
        compareMapping2.doMapping();
    }


    public void test2() throws SQLException {
        SpecificMappingTable mappingTable = new SpecificMappingTable();
        mappingTable.setMappingClass(RunCompareTesting.class);
        mappingTable.setMappingTableName("t_run_test");
        mappingTable.setEncoding("utf8");
        Map<String, MappingField> mappingFields = new LinkedHashMap<>();

        SpecificMappingField mappingField1 = new SpecificMappingField();
        mappingField1.setMappingColumnName("id");
        mappingField1.setMappingFieldType(Long.class);
        mappingField1.setMappingFieldPrimaryKey(true);
        mappingField1.setMappingFieldAutoIncrement(true);
        mappingField1.setMappingFieldComment("测试id备注");
        mappingFields.put("id", mappingField1);

        SpecificMappingField mappingField2 = new SpecificMappingField();
        mappingField2.setMappingColumnName("name");
        mappingField2.setMappingFieldType(String.class);
        mappingField2.setMappingFieldLength(30);
        mappingField2.setMappingFieldNullable(false);
        mappingField2.setMappingFieldComment("测试name备注");
        mappingFields.put("name", mappingField2);

        SpecificMappingField mappingField3 = new SpecificMappingField();
        mappingField3.setMappingColumnName("age");
        mappingField3.setMappingFieldType(int.class);
        mappingField3.setMappingFieldLength(30);
        mappingField3.setMappingFieldDefaultValue("10");
        mappingField3.setMappingFieldComment("测试age备注");
        mappingFields.put("age", mappingField3);

        mappingTable.setMappingFields(mappingFields);

        StartCompareMapping compareMapping = this.getMapping(MappingLevel.CREATE, mappingTable);
        compareMapping.doMapping();
    }


    public void test3() throws SQLException {
        SpecificMappingTable mappingTable = new SpecificMappingTable();
        mappingTable.setMappingClass(RunCompareTesting.class);
        mappingTable.setMappingTableName("t_run_test");
        mappingTable.setEncoding("utf8");
        Map<String, MappingField> mappingFields = new LinkedHashMap<>();

        SpecificMappingField mappingField1 = new SpecificMappingField();
        mappingField1.setMappingColumnName("id");
        mappingField1.setMappingFieldType(Long.class);
        mappingField1.setMappingFieldPrimaryKey(true);
        mappingField1.setMappingFieldAutoIncrement(true);
        mappingField1.setMappingFieldComment("测试id备注");
        mappingFields.put("id", mappingField1);

        SpecificMappingField mappingField2 = new SpecificMappingField();
        mappingField2.setMappingColumnName("name");
        mappingField2.setMappingFieldType(String.class);
        mappingField2.setMappingFieldLength(30);
        mappingField2.setMappingFieldNullable(false);
        mappingField2.setMappingFieldComment("测试name备注");
        mappingFields.put("name", mappingField2);

        SpecificMappingField mappingField3 = new SpecificMappingField();
        mappingField3.setMappingColumnName("age");
        mappingField3.setMappingFieldType(byte.class);
        mappingField3.setMappingFieldLength(30);
        mappingField3.setMappingFieldDefaultValue("13");
        mappingField3.setMappingFieldNullable(false);
        mappingField3.setMappingFieldComment("测试age2备注");
        mappingFields.put("age", mappingField3);

        mappingTable.setMappingFields(mappingFields);

        StartCompareMapping compareMapping = this.getMapping(MappingLevel.UPDATE, mappingTable);
        compareMapping.doMapping();
    }
}
