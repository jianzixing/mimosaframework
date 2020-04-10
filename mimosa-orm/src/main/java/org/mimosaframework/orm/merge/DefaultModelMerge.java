package org.mimosaframework.orm.merge;

import org.mimosaframework.core.json.ModelObject;
import org.mimosaframework.orm.ModelObjectConvertKey;
import org.mimosaframework.orm.platform.SelectFieldAliasReference;
import org.mimosaframework.orm.criteria.Query;

import java.util.*;

/**
 * 实现接口{@link ModelMerge}使得在查询后对象得以整合
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
public class DefaultModelMerge implements ModelMerge {

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

            /**
             * 根据select的所有字段得出整个结果集的结构
             * MapperSelectField 类包含不同的表的别名信息，用别名区分是因为假设一个映射类被join了多次类是一样的但是表别名不一样
             * 将表格结构的数据变成树形结构的数据
             */
            for (ModelObject object : objects) {
                Map<String, ModelObject> hasMap = new LinkedHashMap<String, ModelObject>();
                for (SelectFieldAliasReference mapperSelectField : mapperSelectFields) {
                    if (mapperSelectField.getJavaFieldName() != null) {
                        ModelObject o = hasMap.get(mapperSelectField.getTableAliasName());
                        if (o == null) o = new ModelObject();

                        o.setObjectClass(mapperSelectField.getTableClass());
                        o.put(mapperSelectField.getJavaFieldName(), object.get(mapperSelectField.getFieldAliasName()));

                        //清楚值为null的元素,如果不清除在转换成为bean时出错
                        o.clearNull();

                        hasMap.put(mapperSelectField.getTableAliasName(), o);
                    }
                }

                ol.add(this.getModelObjectByTree(hasMap, mergeTree));
            }

            //合并对象集合
            List<ModelObject> list = this.merge(ol);
            this.replaceModelObject(list);

            objects = null;
            return list;
        }
    }

    /**
     * 通过递归方法计算出每一行包含的N个对象的父子关系
     * 比如：
     * A,B,C -> A[B[C]]
     *
     * @param hasMap    一行数据中包含的N个对象的Map
     * @param mergeTree 表示父子关系的树形结构
     * @return
     */
    private ModelObject getModelObjectByTree(Map<String, ModelObject> hasMap, MergeTree mergeTree) {
        List<MergeTree> mergeTrees = mergeTree.getChildren();

        //使用LinkedHashMap 保持顺序
        Map<MergeTree, ModelObject> objectMap = new LinkedHashMap<MergeTree, ModelObject>();
        if (mergeTrees != null) {
            //假设当前的节点有子节点则递归直到没有子节点
            for (MergeTree m : mergeTrees) {
                ModelObject object = this.getModelObjectByTree(hasMap, m);
                objectMap.put(m, object);
            }
        }
        String tableAliasName = mergeTree.getTableAliasName();
        /**
         * 如果tableAliasName是null那么说明这个不是join来的
         * 直接取字段值的别名
         */
        if (tableAliasName == null) {
            tableAliasName = mergeTree.getMapperSelectFields().get(0).getTableAliasName();
        }
        ModelObject result = hasMap.get(tableAliasName);
        //如果有子数据则把子数据假如到当前对象中
        if (result != null && objectMap.size() > 0) {
            result.putAll(objectMap);
        }
        return result;
    }

    /**
     * 把一个包含父子关系的结果集进行去重合并
     *
     * @param objects 包含父子关系的行结果集
     * @return 包含父子关系的树形结果集
     */
    private List<ModelObject> merge(List<ModelObject> objects) {

        if (objects == null) return null;
        List<ModelObject> result = new ArrayList<ModelObject>();
        //检查result结果集中的对象是否和每一个对象相等
        for (ModelObject object : objects) {
            this.checkEquals(result, object);
        }
        for (ModelObject object : result) {
            Set<Object> keys = object.keySet();
            Map<Object, Object> children = new LinkedHashMap<Object, Object>();
            for (Object k : keys) {
                if (k instanceof MergeTree) {
                    Object o = object.getAny(k);
                    MergeTree mt = (MergeTree) k;
                    List<ModelObject> mos = null;
                    if (o instanceof List) {
                        mos = (List<ModelObject>) o;
                    } else {
                        mos = new ArrayList<ModelObject>();
                        mos.add((ModelObject) o);
                    }

                    List<ModelObject> ol = this.merge(mos);
                    if (ol != null) {
                        children.put(k, mt.isMulti() ? ol : (ol.size() > 0 ? ol.get(0) : null));
                    } else {
                        children.put(k, mt.isMulti() ? mos : (mos.size() > 0 ? mos.get(0) : null));
                    }
                }
            }
            if (children.size() > 0) {
                for (Map.Entry<Object, Object> entry : children.entrySet()) {
                    object.putAny(entry.getKey(), entry.getValue());
                }
            }
        }
        return result;
    }

    /**
     * 替换对象集合中的{@link MergeTree}类，并且根据配置信息更改键值名称
     *
     * @param objects
     */
    private void replaceModelObject(List<ModelObject> objects) {
        for (ModelObject object : objects) {
            Set<Object> sets = object.keySet();
            List<Object> cv = new ArrayList<Object>();
            List<Object> rmkey = new ArrayList<Object>();
            for (Object o : sets) {
                if (o instanceof MergeTree) {
                    Object v = object.getAny(o);
                    if (v != null) {
                        List<ModelObject> vl = null;
                        if (v instanceof List) {
                            vl = (List<ModelObject>) v;
                        } else {
                            ModelObject mo = (ModelObject) v;
                            vl = new ArrayList<ModelObject>();
                            vl.add(mo);
                        }

                        //如果有值就继续递归，如果没有值就删除这个空值以免得到空值对象
                        replaceModelObject(vl);
                        this.clearEmptyModelObject(vl);
                        if (vl.size() == 0) {
                            rmkey.add(o);
                        }
                    }
                    cv.add(o);
                }
            }
            for (Object o : rmkey) object.removeAny(o);
            for (Object o : cv) {
                MergeTree mergeTree = (MergeTree) o;
                Object os = object.getAny(o);
                object.removeAny(o);
                if (os != null) {
                    object.put(mergeTree.getExternalConnectionName(), os);
                }
            }
        }
    }

    private void clearEmptyModelObject(List<ModelObject> vs) {
        List<ModelObject> empty = new ArrayList<>();
        for (ModelObject v : vs) {
            if (v == null || v.size() == 0) {
                empty.add(v);
            }
        }
        vs.removeAll(empty);
    }

    /**
     * 检查result的结果集是否和 object相等
     * 如果相等则合并并将子对象合并
     * 如果不相等则假如result集合中去
     *
     * @param result 已经合并的结果集
     * @param object 还没有合并的结果集
     */
    private void checkEquals(List<ModelObject> result, ModelObject object) {

        if (result.size() == 0) {
            result.add(object);
        } else {
            boolean isAllNotEq = true;
            for (ModelObject o : result) {
                boolean isEq = true;
                Map<Object, List<ModelObject>> children = new LinkedHashMap<>();
                for (Map.Entry<Object, Object> entry : o.entrySet()) {
                    Object k = entry.getKey();
                    //判断是否是结果集对象
                    if (!(k instanceof MergeTree)) {
                        if (!String.valueOf(o.getAny(k)).equals(String.valueOf(object.getAny(k)))) {
                            isEq = false;
                        }
                    } else {
                        //假如到children的map缓存中去以便假设一致时处理
                        Object v = object.getAny(k);
                        if (v instanceof List) {
                            children.put(k, (List<ModelObject>) v);
                        } else {
                            children.put(k, Arrays.asList(new ModelObject[]{(ModelObject) v}));
                        }
                    }
                }
                if (isEq) {
                    isAllNotEq = false;
                }

                //如果一致则合并
                if (isEq) {
                    if (children.size() > 0) {
                        for (Map.Entry<Object, List<ModelObject>> entry : children.entrySet()) {
                            Object co = o.getAny(entry.getKey());
                            List<ModelObject> objects = null;
                            if (co == null) {
                                objects = entry.getValue();
                            } else {
                                if (co instanceof List) {
                                    objects = (List<ModelObject>) co;
                                } else {
                                    objects = new ArrayList<>();
                                    objects.add((ModelObject) co);
                                }
                                objects.addAll(entry.getValue());
                            }
                            o.putAny(entry.getKey(), objects);
                        }
                    }
                }
            }
            if (isAllNotEq) {
                result.add(object);
            }
        }
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
