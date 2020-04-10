package org.mimosaframework.orm.merge;

import org.mimosaframework.core.json.ModelObject;
import org.mimosaframework.orm.ModelObjectConvertKey;
import org.mimosaframework.orm.platform.SelectFieldAliasReference;

import java.util.List;

/**
 * 在每次查询得到结果之后返回的数据结构是一个Map，但是数据结构中的字段
 * 可能和映射类的字段不一致这个时候需要字段的名称转换，并且数据库的join查询结果是
 * 重复的，这个类会解析表格格式的结构变成树类型的结构数据，比如：{f1:'',f2:'',joinObject:[{f3:'',f4:''}]}
 * <p>
 * 在得到树类型的结构数据之前需要先通过映射类和Query类的查询条件整合出join的结构类型
 * {@link MergeTree} 这个类型可以让程序在整合数据时知道每一个对象的父子关系
 * <p>
 * 在整合数据过程中还必须要有 {@link SelectFieldAliasReference} 集合对象
 * 查询条件得到的select语句后面的所有字段，比如 select f0,f1,f2
 *
 * @author yangankang
 */
public interface ModelMerge {

    /**
     * 通过数据库查询后的结果获得一个整合后的结果
     *
     * @param objects         查询后得到的对象
     * @param queryTableClass 主表的映射类
     * @return 整合后的对象
     */
    List<ModelObject> getMergeAfterObjects(List<ModelObject> objects, Class queryTableClass);

    /**
     * 设置字段的转换方式
     * 从数据库查询后的结果可能和映射类字段不一致，比如从驼峰命名到底横线的命名方式
     *
     * @param modelObjectConvertKey 转换类
     */
    void setMappingNamedConvert(ModelObjectConvertKey modelObjectConvertKey);

    /**
     * 设置所有的select后面的字段集合
     *
     * @param mapperSelectFields select的字段集合
     */
    void setMapperSelectFields(List<SelectFieldAliasReference> mapperSelectFields);

    /**
     * 设置一个查询语句的树形结构对象
     * 一般是 主表->[join表->[join表，join表,..],join表,...]
     *
     * @param top 描述查询的树形结构对象
     */
    void setMergeTree(MergeTree top);
}
