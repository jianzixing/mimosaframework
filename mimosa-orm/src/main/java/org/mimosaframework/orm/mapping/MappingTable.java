package org.mimosaframework.orm.mapping;

import java.util.List;
import java.util.Set;

public interface MappingTable {
    void applyFromClassMappingTable(MappingTable table);

    Class getMappingClass();

    String getDatabaseTableName();

    void addDatabaseColumnField(MappingField field);

    void addMappingField(MappingField field);

    void setMappingDatabase(MappingDatabase mappingDatabase);

    Set<MappingField> getMappingFields();

    Set<MappingIndex> getMappingIndexes();

    Set<MappingField> getMappingColumns();

    /**
     * 如果注解@Table有自定义名称,则就是这个值
     * 这个值是已经变换后的名称，如果正确则和数据库名称一致
     * 注意：别名不转换
     * <p>
     * 如果在匹配数据库是否存在时，有配置集群则是配置的集群
     * 分表名称
     */
    String getMappingTableName();

    void setMappingTableName(String mappingTableName);

    MappingTable clone();

    /**
     * 获得Class映射表中的主键值
     *
     * @return
     */
    List<MappingField> getMappingPrimaryKeyFields();

    String getEngineName();

    String getEncoding();

    /**
     * 不区分字段是Java字段还是数据库字段
     *
     * @param fieldName
     * @return
     */
    MappingField getMappingFieldByName(String fieldName);

    MappingField getMappingFieldByJavaName(String fieldName);

    /**
     * 通过数据库字段获取完整的信息
     *
     * @param fieldName
     * @return
     */
    MappingField getMappingFieldByDBName(String fieldName);

    String getMappingClassName();

    /**
     * 获取映射类转成表的名称
     *
     * @return
     */
    String getSourceMappingTableName();
}
