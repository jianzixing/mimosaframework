package comprehensive;

import org.junit.Test;
import org.mimosaframework.orm.*;
import org.mimosaframework.orm.exception.ContextException;
import org.mimosaframework.orm.mapping.*;
import org.mimosaframework.orm.platform.DataSourceWrapper;

import java.sql.SQLException;
import java.util.*;

public class RunCompareTesting {
    private MimosaDataSource mimosaDataSource;

    @Test
    public void runAll() throws ContextException, SQLException {
        mimosaDataSource = new MimosaDataSource(RunDataSourceBuilder.currDataSource(), "mater");
//        this.test1();
//        this.test2();
//        this.test3();
//        this.test4();
        this.test5();
//        this.test7();
//        this.test8();
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

    // 测试新建表
    public void test1() throws SQLException {
        SpecificMappingTable mappingTable = new SpecificMappingTable();
        mappingTable.setMappingClass(RunCompareTesting.class);
        mappingTable.setMappingTableName("t_run_test");
        mappingTable.setEncoding("utf8");
        Map<String, MappingField> mappingFields = new LinkedHashMap<>();

        SpecificMappingField mappingField1 = new SpecificMappingField();
        mappingField1.setMappingColumnName("id");
        mappingField1.setMappingFieldType(int.class);
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

    // 测试增加字段
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

    // 测试修改字段除主键和自增外的属性
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

    // 测试替换主键
    public void test4() throws SQLException {
        SpecificMappingTable mappingTable = new SpecificMappingTable();
        mappingTable.setMappingClass(RunCompareTesting.class);
        mappingTable.setMappingTableName("t_run_test");
        mappingTable.setEncoding("utf8");
        Map<String, MappingField> mappingFields = new LinkedHashMap<>();

        SpecificMappingField mappingField1 = new SpecificMappingField();
        mappingField1.setMappingColumnName("id");
        mappingField1.setMappingFieldType(Long.class);
        // mappingField1.setMappingFieldPrimaryKey(true);
        // mappingField1.setMappingFieldAutoIncrement(true);
        mappingField1.setMappingFieldComment("测试id备注");
        mappingFields.put("id", mappingField1);

        SpecificMappingField mappingField2 = new SpecificMappingField();
        mappingField2.setMappingColumnName("name");
        mappingField2.setMappingFieldType(String.class);
        mappingField2.setMappingFieldNullable(false);
        mappingField2.setMappingFieldLength(20);
        mappingField2.setMappingFieldComment("测试name备注");
        mappingFields.put("name", mappingField2);

        SpecificMappingField mappingField3 = new SpecificMappingField();
        mappingField3.setMappingColumnName("age");
        mappingField3.setMappingFieldType(byte.class);
        mappingField3.setMappingFieldLength(30);
        mappingField3.setMappingFieldPrimaryKey(true);
        mappingField3.setMappingFieldAutoIncrement(true);
        mappingField3.setMappingFieldNullable(false);
        mappingField3.setMappingFieldComment("测试age2备注");
        mappingFields.put("age", mappingField3);

        mappingTable.setMappingFields(mappingFields);

        StartCompareMapping compareMapping = this.getMapping(MappingLevel.UPDATE, mappingTable);
        compareMapping.doMapping();
    }

    // 测试删除主键字段
    public void test5() throws SQLException {
        SpecificMappingTable mappingTable = new SpecificMappingTable();
        mappingTable.setMappingClass(RunCompareTesting.class);
        mappingTable.setMappingTableName("t_run_test");
        mappingTable.setEncoding("utf8");
        Map<String, MappingField> mappingFields = new LinkedHashMap<>();

        SpecificMappingField mappingField1 = new SpecificMappingField();
        mappingField1.setMappingColumnName("id");
        mappingField1.setMappingFieldType(Long.class);
        // mappingField1.setMappingFieldPrimaryKey(true);
        // mappingField1.setMappingFieldAutoIncrement(true);
        mappingField1.setMappingFieldComment("测试id备注");
        mappingFields.put("id", mappingField1);

        SpecificMappingField mappingField2 = new SpecificMappingField();
        mappingField2.setMappingColumnName("name");
        mappingField2.setMappingFieldType(String.class);
        mappingField2.setMappingFieldNullable(false);
        mappingField2.setMappingFieldLength(20);
        mappingField2.setMappingFieldComment("测试name备注");
        mappingFields.put("name", mappingField2);

        mappingTable.setMappingFields(mappingFields);

        StartCompareMapping compareMapping = this.getMapping(MappingLevel.UPDATE, mappingTable);
        compareMapping.doMapping();
    }


    // 测试增加多列主键并增加单列索引
    public void test6() throws SQLException {
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
        mappingField2.setMappingFieldNullable(false);
        mappingField1.setMappingFieldPrimaryKey(true);
        mappingField2.setMappingFieldLength(20);
        mappingField2.setMappingFieldComment("测试name备注");
        mappingFields.put("name", mappingField2);

        SpecificMappingField mappingField3 = new SpecificMappingField();
        mappingField3.setMappingColumnName("age");
        mappingField3.setMappingFieldType(byte.class);
        mappingField3.setMappingFieldLength(30);
        mappingField3.setMappingFieldPrimaryKey(true);
        mappingField3.setMappingFieldNullable(false);
        mappingField3.setMappingFieldIndex(true);
        mappingField3.setMappingFieldComment("测试age2备注");
        mappingFields.put("age", mappingField3);


        mappingTable.setMappingFields(mappingFields);

        StartCompareMapping compareMapping = this.getMapping(MappingLevel.UPDATE, mappingTable);
        compareMapping.doMapping();
    }

    // 定向测试
    // 字段A拥有索引B，如果删除字段A则索引B删除报错
    // 测试删除A时移除删除索引B的数据
    // PlatformExecutor的212行
    public void test7() throws SQLException {
        StartCompareMapping compareMapping = null;
        SpecificMappingTable mappingTable = null;

        mappingTable = new SpecificMappingTable();
        mappingTable.setMappingClass(RunCompareTesting.class);
        mappingTable.setMappingTableName("t_run_test");
        mappingTable.setEncoding("utf8");
        Map<String, MappingField> mappingFields = new LinkedHashMap<>();

        SpecificMappingField mappingField1 = new SpecificMappingField();
        mappingField1.setMappingColumnName("id");
        mappingField1.setMappingFieldType(Long.class);
        mappingField1.setMappingFieldPrimaryKey(false);
        mappingField1.setMappingFieldComment("测试id备注");
        mappingFields.put("id", mappingField1);

        SpecificMappingField mappingField2 = new SpecificMappingField();
        mappingField2.setMappingColumnName("name");
        mappingField2.setMappingFieldType(String.class);
        mappingField2.setMappingFieldNullable(false);
        mappingField1.setMappingFieldPrimaryKey(true);
        mappingField2.setMappingFieldLength(20);
        mappingField2.setMappingFieldComment("测试name备注");
        mappingFields.put("name", mappingField2);
        mappingTable.setMappingFields(mappingFields);

        compareMapping = this.getMapping(MappingLevel.UPDATE, mappingTable);
        compareMapping.doMapping();

        mappingField1.setMappingFieldPrimaryKey(true);
        mappingFields.remove("name");
        compareMapping = this.getMapping(MappingLevel.UPDATE, mappingTable);
        compareMapping.doMapping();
    }

    // 定向测试
    // 测试增加 NOT NULL 字段,且表中已经存在数据时是否能成功
    // mysql自动填充默认值,其他数据库需要手动添加
    // OraclePlatformDialect的127行
    public void test8() throws SQLException {
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

        mappingTable.setMappingFields(mappingFields);

        StartCompareMapping compareMapping = this.getMapping(MappingLevel.UPDATE, mappingTable);
        compareMapping.doMapping();

        SpecificMappingField mappingField2 = new SpecificMappingField();
        mappingField2.setMappingColumnName("name");
        mappingField2.setMappingFieldType(String.class);
        mappingField2.setMappingFieldNullable(false);
        mappingField2.setMappingFieldLength(20);
        mappingField2.setMappingFieldComment("测试name备注");
        mappingFields.put("name", mappingField2);
        compareMapping.doMapping();
    }

    // 定向测试
    // 如果复合主键删除其中一个主键后
    // 理论上需要重建表
    public void test9() throws SQLException {

    }
}
