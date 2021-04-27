package org.mimosaframework.orm.merge;

import org.mimosaframework.core.json.ModelObject;
import org.mimosaframework.orm.ModelObjectConvertKey;
import org.mimosaframework.orm.criteria.DefaultJoin;
import org.mimosaframework.orm.criteria.OrderBy;
import org.mimosaframework.orm.criteria.Query;
import org.mimosaframework.orm.mapping.MappingField;
import org.mimosaframework.orm.mapping.MappingGlobalWrapper;
import org.mimosaframework.orm.mapping.MappingTable;
import org.mimosaframework.orm.platform.SelectFieldAliasReference;

import java.util.*;

/**
 * 实现接口{@link ObjectMerge}使得在查询后对象得以整合
 * 整合方式是通过查询条件{@link Query}得到的树形查询条件结构的出来的
 * 整合过程：
 * 1.数据库查询得到的只是表格类型的数据，每一行数据都包含N个对象，所以先分解出每一行的所有对象集
 * 2.如果想根据树形结构的查询条件对象{@link MergeTree}得出一个树形结构对象的数据，我的做法是将一个对象集合的集合进行去重
 * 比如：
 * |   列1 列2 列3        列1  列2   列3
 * List[A,  B,  C]
 * List[A,  D,  C]  ->  List[A,[B,D],[D,E]]
 * List[A,  D,  E]
 * 从这个结构可以看出从主查询开始去重，每一次去重把join查询的结构合并在一起，一直合并到最后一列对象(列3)
 * 3.最后则把合并好的对象最后整理换名称等等
 *
 * @author yangankang
 */
public class DefaultObjectMerge implements ObjectMerge {

    /**
     * 描述一次查询时的查询结构(树形结构)
     */
    private MergeTree mergeTree;

    /**
     * 映射表和映射类的名称转换方法
     */
    private ModelObjectConvertKey modelObjectConvertKey;

    /**
     * 一次查询时所有的select的字段集合
     */
    private List<SelectFieldAliasReference> mapperSelectFields;

    private MappingGlobalWrapper mappingGlobalWrapper;

    public void setMappingGlobalWrapper(MappingGlobalWrapper mappingGlobalWrapper) {
        this.mappingGlobalWrapper = mappingGlobalWrapper;
    }

    /**
     * 通过一系列的结果集转换整合得到一个带父子关系的对象集合
     *
     * @param objects 查询后得到的对象
     * @return 整合后的结果集
     */
    @Override
    public List<ModelObject> getMergeAfterObjects(List<ModelObject> objects, Class queryTableClass) {

        if (null == objects || objects.size() == 0) {
            return null;
        }

        List<ModelObject> ol = new ArrayList<ModelObject>();

        /**
         * 如果查询的select字段只有一个，也就说明没有join那么单独的简单处理就行
         * 处理时需要判断这个字段是否要保留数据库查询结果集的名称，也就是不转换名称一般是函数重命名时不需要转换
         */
        if (mapperSelectFields == null || mapperSelectFields.size() == 1) {

            for (ModelObject o : objects) {
                ModelObject on = new ModelObject();
                for (Map.Entry<Object, Object> entry : o.entrySet()) {
                    String key = String.valueOf(entry.getKey());
                    Object value = entry.getValue();
                    on.put(key, value);
                }
                on.setObjectClass(queryTableClass);
                on = this.modelObjectConvertKey.reconvert(queryTableClass, on);
                ol.add(on);
            }

            objects = null;
            return ol;
        } else {
            List<ModelObject> list = this.merge(objects, this.mergeTree);
            return list;
        }
    }

    private List<ModelObject> merge(List<ModelObject> objects, MergeTree mergeTree) {
        List<ModelObject> list = new ArrayList<>();
        for (ModelObject object : objects) {
            ModelObject item = this.mergeItem(object, mergeTree, list);
            if (item != null && list.indexOf(item) == -1) {
                list.add(item);
            }
        }
        return list;
    }

    private ModelObject mergeItem(ModelObject object,
                                  MergeTree mergeTree,
                                  List<ModelObject> list) {
        List<SelectFieldAliasReference> fields = mergeTree.getMapperSelectFields();

        ModelObject o = null;
        if (list != null) {
            for (ModelObject l : list) {
                boolean eq = this.checkEquals(object, mergeTree, l);
                if (eq) {
                    o = l;
                    break;
                }
            }
        }

        if (o == null) {
            for (SelectFieldAliasReference mapperSelectField : fields) {
                if (mapperSelectField.getJavaFieldName() != null) {
                    if (o == null) o = new ModelObject();
                    o.put(mapperSelectField.getJavaFieldName(), object.get(mapperSelectField.getFieldAliasName()));
                }
            }
            if (o != null) {
                o.setObjectClass(mergeTree.getSelfTable());
                //清楚值为null的元素,如果不清除在转换成为bean时出错
                o.clearNull();
            }
        }

        List<MergeTree> mergeTrees = mergeTree.getChildren();
        if (mergeTrees != null && mergeTrees.size() > 0) {
            for (MergeTree mt : mergeTrees) {
                String aliasName = mt.getExternalConnectionName();
                Object children = o.getAny(aliasName);

                if (mt.isMulti()) {
                    if (children == null) children = new ArrayList<>();
                    if (children instanceof List) {
                        ModelObject childItem = this.mergeItem(object, mt, (List<ModelObject>) children);
                        if (((List<ModelObject>) children).indexOf(childItem) == -1) {
                            ((List<ModelObject>) children).add(childItem);
                        }
                        o.put(aliasName, children);
                    }
                } else {
                    if (children == null) {
                        ModelObject childItem = this.mergeItem(object, mt, null);
                        children = childItem;
                        o.put(aliasName, children);
                    } else if (children instanceof ModelObject) {
                        this.mergeItem(object, mt,
                                Collections.singletonList((ModelObject) children));
                    }
                }
            }
        }
        return o;
    }

    private boolean checkEquals(ModelObject object, MergeTree mergeTree, ModelObject item) {
        List<SelectFieldAliasReference> fields = mergeTree.getMapperSelectFields();
        boolean doEq = false;
        for (SelectFieldAliasReference f : fields) {
            if (f.isPrimaryKey()) {
                doEq = true;
                Object v1 = object.getAny(f.getFieldAliasName());
                Object v2 = item.getAny(f.getJavaFieldName());
                if (v1 == null || v2 == null || !v1.equals(v2)) {
                    return false;
                }
            }
        }
        return doEq && true;
    }

    /**
     * 重新排序
     *
     * @param mergeTree
     * @param list
     */
    private void sortList(MergeTree mergeTree, List list) {
        DefaultJoin join = (DefaultJoin) mergeTree.getJoin();
        Set<OrderBy> orders = join.getSorts();
        if (orders != null && orders.size() > 0) {
            for (OrderBy orderBy : orders) {
                this.sortListByKey(list, "" + orderBy.getField(), orderBy.isAsc());
            }
        } else {
            Class table = join.getTable();
            MappingTable mappingTable = this.mappingGlobalWrapper.getMappingTable(table);
            if (mappingTable != null) {
                List<MappingField> primaryKeyFields = mappingTable.getMappingPrimaryKeyFields();
                if (primaryKeyFields != null) {
                    for (final MappingField mappingField : primaryKeyFields) {
                        this.sortListByKey(list, mappingField.getMappingFieldName(), true);
                        break;
                    }
                }
            }
        }
    }

    private void sortListByKey(List list, final String key, final boolean isAsc) {
        Collections.sort(list, new Comparator() {
            @Override
            public int compare(Object o1, Object o2) {
                if (o1 instanceof ModelObject && o2 instanceof ModelObject) {
                    Object a1 = ((ModelObject) o1).getAny(key);
                    Object a2 = ((ModelObject) o2).getAny(key);
                    if (a1 instanceof Integer && a2 instanceof Integer) {
                        int number = isAsc ? (int) a1 - (int) a2 : (int) a2 - (int) a1;
                        return number > 0 ? 1 : number == 0 ? 0 : -1;
                    }
                    if (a1 instanceof Byte && a2 instanceof Byte) {
                        int number = isAsc ? (byte) a1 - (byte) a2 : (byte) a2 - (byte) a1;
                        return number > 0 ? 1 : number == 0 ? 0 : -1;
                    }
                    if (a1 instanceof Long && a2 instanceof Long) {
                        long number = isAsc ? (long) a1 - (long) a2 : (long) a2 - (long) a1;
                        return number > 0 ? 1 : number == 0 ? 0 : -1;
                    }
                    if (a1 instanceof Double && a2 instanceof Double) {
                        double number = isAsc ? (double) a1 - (double) a2 : (double) a2 - (double) a1;
                        return number > 0 ? 1 : number == 0 ? 0 : -1;
                    }
                    if (a1 instanceof Float && a2 instanceof Float) {
                        float number = isAsc ? (float) a1 - (float) a2 : (float) a2 - (float) a1;
                        return number > 0 ? 1 : number == 0 ? 0 : -1;
                    }
                    if (a1 instanceof Short && a2 instanceof Short) {
                        int number = isAsc ? (short) a1 - (short) a2 : (short) a2 - (short) a1;
                        return number > 0 ? 1 : number == 0 ? 0 : -1;
                    }
                    if (a1 instanceof Boolean && a2 instanceof Boolean) {
                        return a1 == a2 ? 0 : isAsc ? 1 : -1;
                    }
                    if (a1 instanceof String && a2 instanceof String) {
                        return ((String) a1).compareTo((String) a2);
                    }
                    if (a1 instanceof Character && a2 instanceof Character) {
                        return ((Character) a1).compareTo((Character) a2);
                    }
                }
                return 0;
            }
        });
    }


    /**
     * 设置映射类和映射表的字段转换类
     *
     * @param modelObjectConvertKey 转换类
     */
    @Override
    public void setMappingNamedConvert(ModelObjectConvertKey modelObjectConvertKey) {
        this.modelObjectConvertKey = modelObjectConvertKey;
    }

    /**
     * 设置select的所有字段集合
     *
     * @param mapperSelectFields select的字段集合
     */
    @Override
    public void setMapperSelectFields(List<SelectFieldAliasReference> mapperSelectFields) {
        this.mapperSelectFields = mapperSelectFields;
    }

    /**
     * 设置一次查询的树形描述类
     *
     * @param top 描述查询的树形结构对象
     */
    @Override
    public void setMergeTree(MergeTree top) {
        this.mergeTree = top;
    }
}
