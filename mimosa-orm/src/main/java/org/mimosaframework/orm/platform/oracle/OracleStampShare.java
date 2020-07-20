package org.mimosaframework.orm.platform.oracle;

import org.mimosaframework.core.utils.StringTools;
import org.mimosaframework.orm.mapping.MappingGlobalWrapper;
import org.mimosaframework.orm.platform.ExecuteImmediate;
import org.mimosaframework.orm.platform.PlatformStampShare;
import org.mimosaframework.orm.platform.SQLDataPlaceholder;
import org.mimosaframework.orm.sql.stamp.*;

import java.util.Iterator;
import java.util.List;
import java.util.Map;

public class OracleStampShare extends PlatformStampShare {

    public void addAutoIncrement(MappingGlobalWrapper wrapper, Class table, String tableStr) {
        String tableName = this.commonality.getReference().getTableName(wrapper, table, tableStr);
        String seqName = tableName + "_SEQ";
        this.commonality.getSection().getDeclares().add("SEQUENCE_COUNT NUMBER");
        this.commonality.getSection().getBuilders().add(new ExecuteImmediate().setProcedure("SELECT COUNT(1) INTO SEQUENCE_COUNT FROM USER_SEQUENCES WHERE SEQUENCE_NAME = '" + seqName + "'"));
        this.commonality.getSection().getBuilders().add(new ExecuteImmediate("IF SEQUENCE_COUNT = 0 THEN ",
                "CREATE SEQUENCE " + seqName + " INCREMENT BY 1 START WITH 1 MINVALUE 1 MAXVALUE 9999999999999999", "END IF"));
    }

    /**
     * ORACLE的数据类型 -- ORACLE的数据类型
     * 常用的数据库字段类型如下：
     * 字段类型 中文说明 限制条件 其它说明
     * CHAR 固定长度字符串 最大长度2000 bytes
     * VARCHAR2 可变长度的字符串 最大长度4000 bytes 可做索引的最大长度749
     * NCHAR 根据字符集而定的固定长度字符串 最大长度2000 bytes
     * NVARCHAR2 根据字符集而定的可变长度字符串 最大长度4000 bytes
     * DATE 日期（日-月-年） DD-MM-YY（HH-MI-SS） 无千虫问题
     * LONG 超长字符串 最大长度2G（231-1） 足够存储大部头著作
     * RAW 固定长度的二进制数据 最大长度2000 bytes 可存放多媒体图象声音等
     * LONG RAW 可变长度的二进制数据 最大长度2G 同上
     * BLOB 二进制数据 最大长度4G
     * CLOB 字符数据 最大长度4G
     * NCLOB 根据字符集而定的字符数据 最大长度4G
     * BFILE 存放在数据库外的二进制数据 最大长度4G
     * ROWID 数据表中记录的唯一行号 10 bytes ********.****.****格式，*为0或1
     * NROWID 二进制数据表中记录的唯一行号 最大长度4000 bytes
     * NUMBER(P,S) 数字类型 P为总位数，S为小数位数
     * DECIMAL(P,S) 数字类型 P为总位数，S为小数位数
     * INTEGER 整数类型 小的整数
     * FLOAT 浮点数类型 NUMBER(38)，双精度
     * REAL 实数类型 NUMBER(63)，精度更高
     */

    public void addCommentSQL(MappingGlobalWrapper wrapper,
                              StampAction action,
                              Object param,
                              String commentStr,
                              int type) {
        Class table = null;
        String tableStr = null;
        if (action instanceof StampAlter) {
            table = ((StampAlter) action).tableClass;
            tableStr = ((StampAlter) action).tableName;
        }
        if (action instanceof StampCreate) {
            table = ((StampCreate) action).tableClass;
            tableStr = ((StampCreate) action).tableName;
        }

        StringBuilder comment = new StringBuilder();
        if (type == 1) {
            StampColumn column = (StampColumn) param;
            comment.append("COMMENT ON COLUMN ");
            if (table != null) {
                column.table = table;
            } else if (StringTools.isNotEmpty(tableStr)) {
                column.tableAliasName = tableStr;
            }
            comment.append(this.commonality.getReference().getColumnName(wrapper, action, column));
        }
        if (type == 2) {
            String tableName = this.commonality.getReference().getTableName(wrapper, table, tableStr);
            comment.append("COMMENT ON TABLE " + tableName);
        }
        comment.append(" IS ");
        comment.append("''" + commentStr + "''");
        this.commonality.getSection().getBuilders().add(new ExecuteImmediate(comment));
    }


    /**
     * 无order by
     * SELECT *
     * FROM (SELECT ROWNUM AS rowno, t.*
     * FROM emp t
     * WHERE hire_date BETWEEN TO_DATE ('20060501', 'yyyymmdd')
     * AND TO_DATE ('20060731', 'yyyymmdd')
     * AND ROWNUM <= 20) table_alias
     * WHERE table_alias.rowno >= 10;
     * <p>
     * <p>
     * 有order by
     * SELECT *
     * FROM (SELECT tt.*, ROWNUM AS rowno
     * FROM (  SELECT t.*
     * FROM emp t
     * WHERE hire_date BETWEEN TO_DATE ('20060501', 'yyyymmdd')
     * AND TO_DATE ('20060731', 'yyyymmdd')
     * ORDER BY create_time DESC, emp_no) tt
     * WHERE ROWNUM <= 20) table_alias
     * WHERE table_alias.rowno >= 10;
     *
     * @param limit
     */
    protected void limit(StampLimit limit) {
    }

    protected StringBuilder multiDeleteOrUpdate(MappingGlobalWrapper wrapper,
                                                Class delTable,
                                                String delTableName,
                                                StampFrom[] froms,
                                                StampWhere where,
                                                StampOrderBy[] orderBys,
                                                StampLimit limit,
                                                List<SQLDataPlaceholder> placeholders,
                                                StampAction action,
                                                Map<String, String> selectFieldMap) {
        StringBuilder select = new StringBuilder();
        select.append("SELECT ");
        if (selectFieldMap != null && selectFieldMap.size() > 0) {
            Iterator<Map.Entry<String, String>> iterator = selectFieldMap.entrySet().iterator();
            while (iterator.hasNext()) {
                Map.Entry<String, String> entry = iterator.next();
                select.append(entry.getKey());
                select.append(" AS " + entry.getValue());
                if (iterator.hasNext()) select.append(", ");
            }
        } else {
            String selectFields = "*";
            boolean isIn = false;
            for (StampFrom from : froms) {
                if (from.aliasName != null
                        && StringTools.isNotEmpty(delTableName)
                        && from.aliasName.equalsIgnoreCase(delTableName)) {
                    selectFields = from.aliasName.toUpperCase() + ".*";
                    isIn = true;
                }
            }
            if (!isIn && (delTable != null || StringTools.isNotEmpty(delTableName))) {
                selectFields = this.commonality.getReference().getTableName(wrapper, delTable, delTableName) + ".*";
            }
            select.append(selectFields);
        }
        select.append(" FROM");
        int i = 0;
        for (StampFrom from : froms) {
            select.append(" " + this.commonality.getReference().getTableName(wrapper, from.table, from.name));
            if (StringTools.isNotEmpty(from.aliasName)) {
                select.append(" " + from.aliasName.toUpperCase());
            }
            i++;
            if (i != froms.length) select.append(",");
        }
        if (where != null) {
            select.append(" WHERE ");
            this.buildWhere(wrapper, placeholders, action, where, select);
        }

        if (orderBys != null && orderBys.length > 0) {
            select.append(" ORDER BY ");
            int j = 0;
            for (StampOrderBy ob : orderBys) {
                select.append(this.commonality.getReference().getColumnName(wrapper, action, ob.column));
                if (ob.sortType == KeySortType.ASC)
                    select.append(" ASC");
                else
                    select.append(" DESC");
                j++;
                if (j != orderBys.length) select.append(",");
            }
        }

        if (limit != null) {
            long start = limit.start, len = start + limit.limit;
            StringBuilder sb = new StringBuilder();
            sb.append("SELECT * FROM (SELECT RN_TABLE_ALIAS.*, ROWNUM AS RN_ALIAS FROM (");
            sb.append(select);
            sb.append(") RN_TABLE_ALIAS WHERE ROWNUM <= " + len + ") RN_TABLE_ALIAS_2 WHERE RN_TABLE_ALIAS_2.RN_ALIAS >= " + start);
            return sb;
        } else {
            return select;
        }
    }
}
