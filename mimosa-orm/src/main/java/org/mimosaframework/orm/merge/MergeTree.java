package org.mimosaframework.orm.merge;

import org.mimosaframework.orm.criteria.DefaultJoin;
import org.mimosaframework.orm.criteria.Join;
import org.mimosaframework.orm.platform.SelectFieldAliasReference;

import java.util.ArrayList;
import java.util.List;

/**
 * 用来描述一次Query查询时主表和join表对应的查询结构
 * 当前的结构是树形结构的，以描述结果集的父子关系
 * 类似B+树结构
 *
 * @author yangankang
 * @see ObjectMerge
 */
public class MergeTree {

    private VariableBeanName variableBeanName;

    /**
     * 当前节点的父节点，如果是顶级节点则是空的
     */
    private MergeTree parent;

    /**
     * 当前节点的子节点，如果是叶子节点则是空的
     */
    private List<MergeTree> children;

    /**
     * 当前节点所描述的主表映射类
     */
    private Class<?> mainTable;

    /**
     * 当前节点锁描述的join表的映射类
     */
    private Class<?> selfTable;

    /**
     * 当前节点描述的join表的别名
     */
    private String tableAliasName;

    /**
     * 合并完成之后每一个子查询在父查询的数据中显示的名称
     */
    private String externalConnectionName;

    /**
     * 是否是数组方式添加子查询值
     */
    private boolean isMulti = true;

    /**
     * 当前的join表的包含的select的字段集合(不是所有的)
     */
    private List<SelectFieldAliasReference> mapperSelectFields;

    private Join join;

    public Join getJoin() {
        return join;
    }

    public void setJoin(Join join) {
        this.join = join;
    }

    public MergeTree() {
        variableBeanName = VariableBeanNameFactory.getVariableBeanName();
    }

    public String getTableAliasName() {
        return tableAliasName;
    }

    public void setTableAliasName(String tableAliasName) {
        this.tableAliasName = tableAliasName;
    }

    public Class<?> getMainTable() {
        return mainTable;
    }

    public void setMainTable(Class<?> mainTable) {
        this.mainTable = mainTable;
    }

    public Class<?> getSelfTable() {
        return selfTable;
    }

    public void setSelfTable(Class<?> selfTable) {
        this.selfTable = selfTable;
    }

    public MergeTree getParent() {
        return parent;
    }

    public void setParent(MergeTree parent) {
        this.parent = parent;
    }

    public List<MergeTree> getChildren() {
        return children;
    }

    public boolean isMulti() {
        return isMulti;
    }

    public void setMulti(boolean multi) {
        isMulti = multi;
    }

    public boolean isIgnore() {
        return join != null && ((DefaultJoin) join).isIgnore();
    }

    public void setChildren(List<MergeTree> children) {
        this.children = children;
    }

    /**
     * 单个的添加子节点
     *
     * @param m
     */
    public void addChildren(MergeTree m) {
        if (children == null) children = new ArrayList<MergeTree>();
        children.add(m);
    }

    public List<SelectFieldAliasReference> getMapperSelectFields() {
        return mapperSelectFields;
    }

    public void setMapperSelectFields(List<SelectFieldAliasReference> mapperSelectFields) {
        this.mapperSelectFields = mapperSelectFields;
    }

    public void addMapperSelectField(SelectFieldAliasReference mapperSelectField) {
        if (this.mapperSelectFields == null) this.mapperSelectFields = new ArrayList<SelectFieldAliasReference>();
        this.mapperSelectFields.add(mapperSelectField);
    }

    public String getExternalConnectionName() {
        if (externalConnectionName == null) {
            externalConnectionName = variableBeanName.getVarName(this.getSelfTable());
        }
        return externalConnectionName;
    }

    public void setExternalConnectionName(String externalConnectionName) {
        this.externalConnectionName = externalConnectionName;
    }
}
