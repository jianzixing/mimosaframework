package org.mimosaframework.orm.platform;

import org.mimosaframework.core.utils.StringTools;
import org.mimosaframework.orm.mapping.MappingField;

import java.util.*;

public class TableStructure {
    private String tableSchema;
    /**
     * must be
     */
    private String tableName;
    private String type;
    private long count;
    private Object lastUsed;
    /**
     * if support must be
     */
    private String comment;
    private Object createTime;

    private List<TableColumnStructure> columnStructures;
    private List<TableIndexStructure> indexStructures;
    private List<TableConstraintStructure> constraintStructures;

    public String getTableSchema() {
        return tableSchema;
    }

    public void setTableSchema(String tableSchema) {
        this.tableSchema = tableSchema;
    }

    public String getTableName() {
        return tableName;
    }

    public void setTableName(String tableName) {
        this.tableName = tableName;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public long getCount() {
        return count;
    }

    public void setCount(long count) {
        this.count = count;
    }

    public Object getLastUsed() {
        return lastUsed;
    }

    public void setLastUsed(Object lastUsed) {
        this.lastUsed = lastUsed;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public Object getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Object createTime) {
        this.createTime = createTime;
    }

    public List<TableColumnStructure> getColumnStructures() {
        return columnStructures;
    }

    private boolean isSameString(String s1, String s2) {
        if (s1 == null && s2 == null) return true;
        if (s1 != null) {
            return s1.equalsIgnoreCase(s2);
        } else {
            return s2.equalsIgnoreCase(s1);
        }
    }

    public void setColumnStructures(List<TableColumnStructure> columnStructures) {
        // 去除重复的列
        if (columnStructures != null && columnStructures.size() > 0) {
            List<TableColumnStructure> newColumns = new ArrayList<>();
            for (TableColumnStructure structure : columnStructures) {
                boolean eq = false;
                for (TableColumnStructure n : newColumns) {
                    if (structure == null || structure.getColumnName() == null) {
                        System.out.println();
                    }
                    if (structure.getColumnName().equalsIgnoreCase(n.getColumnName())) {
                        eq = true;
                        break;
                    }
                }
                if (!eq) {
                    newColumns.add(structure);
                }
            }
            this.columnStructures = newColumns;
        }
    }

    public List<TableIndexStructure> getIndexStructures() {
        return indexStructures;
    }

    public void setIndexStructures(List<TableIndexStructure> indexStructures) {
        // 去除重复的索引
        if (indexStructures != null && indexStructures.size() > 0) {
            List<TableIndexStructure> newIndex = new ArrayList<>();
            for (TableIndexStructure structure : indexStructures) {
                boolean eq = false;
                for (TableIndexStructure n : newIndex) {
                    if (isSameString(structure.getTableSchema(), n.getTableSchema())
                            && isSameString(structure.getIndexName(), n.getIndexName())
                            && isSameString(structure.getTableName(), n.getTableName())
                            && isSameString(structure.getType(), n.getType())
                            && isSameString(structure.getColumnName(), n.getColumnName())
                            && isSameString(structure.getComment(), n.getComment())
                    ) {
                        eq = true;
                        break;
                    }
                }
                if (!eq) {
                    newIndex.add(structure);
                }
            }
            this.indexStructures = newIndex;
        }
    }

    public List<TableConstraintStructure> getConstraintStructures() {
        return constraintStructures;
    }

    public void setConstraintStructures(List<TableConstraintStructure> constraintStructures) {
        // 去除重复的约束
        if (constraintStructures != null && constraintStructures.size() > 0) {
            List<TableConstraintStructure> newConstraint = new ArrayList<>();
            for (TableConstraintStructure structure : constraintStructures) {
                boolean eq = false;
                for (TableConstraintStructure n : newConstraint) {
                    if (isSameString(structure.getTableSchema(), n.getTableSchema())
                            && isSameString(structure.getConstraintName(), n.getConstraintName())
                            && isSameString(structure.getTableName(), n.getTableName())
                            && isSameString(structure.getColumnName(), n.getColumnName())
                            && isSameString(structure.getForeignTableName(), n.getForeignTableName())
                            && isSameString(structure.getForeignColumnName(), n.getForeignColumnName())
                            && isSameString(structure.getType(), n.getType())
                    ) {
                        eq = true;
                        break;
                    }
                }
                if (!eq) {
                    newConstraint.add(structure);
                }
            }
            this.constraintStructures = newConstraint;
        }
    }

    public List<TableIndexStructure> getIndexStructures(String indexName) {
        List<TableIndexStructure> structures = null;
        if (indexStructures != null) {
            for (TableIndexStructure indexStructure : indexStructures) {
                if (indexStructure.getIndexName().equalsIgnoreCase(indexName)) {
                    if (structures == null) structures = new ArrayList<>();
                    structures.add(indexStructure);
                }
            }
        }
        return structures;
    }

    public boolean isPrimaryKeyColumn(String column) {
        if (constraintStructures != null) {
            for (TableConstraintStructure constraintStructure : constraintStructures) {
                if (column.equalsIgnoreCase(constraintStructure.getColumnName()) && "P".equals(constraintStructure.getType())) {
                    return true;
                }
            }
        }
        return false;
    }

    public List<TableConstraintStructure> getPrimaryKey() {
        if (constraintStructures != null) {
            List<TableConstraintStructure> structures = new ArrayList<>();
            for (TableConstraintStructure constraintStructure : constraintStructures) {
                if ("P".equals(constraintStructure.getType())
                        && !this.isColumnState(constraintStructure.getColumnName(), 2)) {
                    structures.add(constraintStructure);
                }
            }
            return structures;
        }
        return null;
    }

    public boolean isUniqueColumn(String mappingColumnName) {
        if (constraintStructures != null) {
            for (TableConstraintStructure constraintStructure : constraintStructures) {
                if (mappingColumnName.equalsIgnoreCase(constraintStructure.getColumnName())
                        && "U".equals(constraintStructure.getType())) {
                    return true;
                }
            }
        }
        return false;
    }

    public boolean isOnlyNormalIndex(String mappingColumnName) {
        if (indexStructures != null) {
            int c = 0;
            for (TableIndexStructure indexStructure : indexStructures) {
                if (mappingColumnName.equalsIgnoreCase(indexStructure.getColumnName())
                        && "D".equals(indexStructure.getType())) {
                    c++;
                }
            }
            if (c == 1) {
                return true;
            }
        }
        return false;
    }

    public boolean isOnlyUniqueIndex(String mappingColumnName) {
        if (indexStructures != null) {
            int c = 0;
            for (TableIndexStructure indexStructure : indexStructures) {
                if (mappingColumnName.equalsIgnoreCase(indexStructure.getColumnName())
                        && "U".equals(indexStructure.getType())) {
                    c++;
                }
            }
            if (c == 1) {
                return true;
            }
        }
        return false;
    }

    public boolean isOnlyPrimaryKey(String mappingColumnName) {
        if (constraintStructures != null) {
            int c = 0;
            for (TableConstraintStructure constraintStructure : constraintStructures) {
                if (mappingColumnName.equalsIgnoreCase(constraintStructure.getColumnName())
                        && "P".equals(constraintStructure.getType())) {
                    c++;
                }
            }
            if (c == 1) {
                return true;
            }
        }
        return false;
    }

    public boolean isOnlyNotNormalIndex(String mappingColumnName) {
        if (indexStructures != null) {
            int c1 = 0, c2 = 0;
            for (TableIndexStructure indexStructure : indexStructures) {
                if (mappingColumnName.equalsIgnoreCase(indexStructure.getColumnName())
                        && "P".equals(indexStructure.getType())) {
                    c1++;
                }
                if (mappingColumnName.equalsIgnoreCase(indexStructure.getColumnName())
                        && "U".equals(indexStructure.getType())) {
                    c2++;
                }
            }
            if (c1 == 1 || c2 == 1) {
                return true;
            }
        }
        return false;
    }

    public List<TableColumnStructure> getAutoIncrement() {
        if (columnStructures != null) {
            List<TableColumnStructure> columnStructures = null;
            for (TableColumnStructure columnStructure : this.columnStructures) {
                if (columnStructure.isAutoIncrement()) {
                    if (columnStructures == null) {
                        columnStructures = new ArrayList<>();
                    }
                    columnStructures.add(columnStructure);
                }
            }
            return columnStructures;
        }
        return null;
    }

    public boolean isColumnState(String columnName, int state) {
        if (columnStructures != null) {
            for (TableColumnStructure columnStructure : this.columnStructures) {
                if (columnName.equals(columnStructure.getColumnName()) &&
                        columnStructure.getState() == state) {
                    return true;
                }
            }
        }
        return false;
    }

    public Map<String, List<TableConstraintStructure>> getMapConstraintWithOutC() {
        if (constraintStructures != null) {
            Map<String, List<TableConstraintStructure>> map = null;
            for (TableConstraintStructure constraintStructure : constraintStructures) {
                if (!constraintStructure.getType().equals("C")) { // C表示其它约束比如不为空约束
                    if (map == null) map = new HashMap<>();
                    List<TableConstraintStructure> cname = map.get(constraintStructure.getConstraintName());
                    if (cname == null) cname = new ArrayList<>();
                    if (cname.indexOf(constraintStructure) == -1) cname.add(constraintStructure);
                    map.put(constraintStructure.getConstraintName(), cname);
                }
            }
            return map;
        }
        return null;
    }

    /**
     * 获取数据库的索引
     *
     * @return
     */
    public Map<String, List<TableIndexStructure>> getMapIndex() {
        Map<String, List<TableIndexStructure>> structures = null;
        if (indexStructures != null) {
            for (TableIndexStructure indexStructure : indexStructures) {
                if (structures == null) structures = new HashMap<>();
                List<TableIndexStructure> list = structures.get(indexStructure.getIndexName());
                if (list == null) list = new ArrayList<>();
                // oracle有一些字段为空的索引
                if (StringTools.isNotEmpty(indexStructure.getColumnName())) {
                    if (list.indexOf(indexStructure) == -1) list.add(indexStructure);
                    structures.put(indexStructure.getIndexName(), list);
                }
            }
        }
        return structures;
    }

    /**
     * 判断相同列是否在索引和约束中都存在
     *
     * @param indexName
     * @param indexes
     * @return
     */
    public boolean isIndexInConstraintByColumns(String indexName, List<TableIndexStructure> indexes) {
        // 排除约束同名的索引
        Map<String, List<TableConstraintStructure>> crts = this.getMapConstraintWithOutC();
        Iterator<Map.Entry<String, List<TableConstraintStructure>>> iterator = crts.entrySet().iterator();

        while (iterator.hasNext()) {
            Map.Entry<String, List<TableConstraintStructure>> entry = iterator.next();
            List<TableConstraintStructure> cs = entry.getValue();

            if (indexes != null && cs != null) {
                Set<String> consColumns = new HashSet<>();
                for (TableConstraintStructure c : cs) {
                    consColumns.add(c.getColumnName());
                }

                if (indexes.size() == consColumns.size()) {
                    boolean eq = true;
                    for (TableConstraintStructure a : cs) {
                        boolean is = false;
                        for (TableIndexStructure b : indexes) {
                            if (a.getColumnName().equals(b.getColumnName())) {
                                is = true;
                                break;
                            }
                        }
                        if (!is) {
                            eq = false;
                            break;
                        }
                    }
                    if (eq) {
                        return true;
                    }
                }
            }
        }

        return false;
    }

    public List<TableIndexStructure> getIndexStructures(List<MappingField> indexColumns) {
        Map<String, List<TableIndexStructure>> map = this.getMapIndex();
        if (map != null) {
            Map.Entry<String, List<TableIndexStructure>> entry = this.getIndexStructures(map, indexColumns);
            if (entry != null) {
                return entry.getValue();
            }
        }
        return null;
    }

    public Map.Entry<String, List<TableIndexStructure>> getIndexStructures(Map<String, List<TableIndexStructure>> map, List<MappingField> indexColumns) {
        Iterator<Map.Entry<String, List<TableIndexStructure>>> iterator = map.entrySet().iterator();
        while (iterator.hasNext()) {
            Map.Entry<String, List<TableIndexStructure>> entry = iterator.next();
            List<TableIndexStructure> structures = entry.getValue();
            if (structures != null && structures.size() > 0 && structures.size() == indexColumns.size()) {
                boolean eq = true;
                for (TableIndexStructure s : structures) {
                    boolean in = false;
                    for (MappingField field : indexColumns) {
                        if (StringTools.isNotEmpty(s.getColumnName())
                                && s.getColumnName().equals(field.getMappingColumnName())) {
                            in = true;
                            break;
                        }
                    }
                    if (!in) {
                        eq = false;
                        break;
                    }
                }
                if (eq) {
                    return entry;
                }
            }
        }
        return null;
    }
}
