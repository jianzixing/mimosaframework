package org.mimosaframework.orm.platform.db2;

import org.mimosaframework.core.utils.StringTools;
import org.mimosaframework.orm.mapping.MappingGlobalWrapper;
import org.mimosaframework.orm.platform.SQLBuilderCombine;
import org.mimosaframework.orm.sql.stamp.StampAction;
import org.mimosaframework.orm.sql.stamp.StampCombineBuilder;
import org.mimosaframework.orm.sql.stamp.StampStructure;

import java.util.Iterator;
import java.util.List;

/**
 * SELECT TABSCHEMA,         --模式名
 * TABNAME,           --表名
 * TYPE,              --类型(T: 表, V:视图, N:昵称)
 * CARD    AS COUNT,  --记录数(最新一次RUNSTATS统计)
 * LASTUSED,          --最近一次访问日期(增删改查)
 * CREATE_TIME,       --表的创建时间
 * REMARKS AS COMMENT --表的注释
 * FROM SYSCAT.TABLES
 * WHERE TYPE = 'T'
 * and TABNAME = 'T_USER'
 * AND TABSCHEMA = 'DB2INST1';
 * --查看表中列的注释
 * SELECT T1.TABSCHEMA, --模式名
 * T1.TABNAME,   --表名
 * T1.COLNAME,   --字段名
 * T1.TYPENAME,  --字段类型
 * T1.LENGTH,    --字段长度
 * T1.SCALE,     --精度
 * T1.DEFAULT,   --默认值
 * T1.NULLS,     --是否为空
 * (CASE WHEN T1.COLNAME = T2.COLNAME THEN 'Y' ELSE 'N' END) AS PK,
 * T1.REMARKS    --用户注释
 * FROM SYSCAT.COLUMNS T1
 * LEFT JOIN SYSCAT.KEYCOLUSE T2 ON T2.TABNAME=T1.TABNAME AND T2.TABSCHEMA=T1.TABSCHEMA
 * WHERE T1.TABSCHEMA = 'DB2INST1'
 * AND T1.TABNAME = 'T_TT';
 */
public class DB2StampStructure implements StampCombineBuilder {
    @Override
    public SQLBuilderCombine getSqlBuilder(MappingGlobalWrapper wrapper, StampAction action) {
        StampStructure structure = (StampStructure) action;
        String schema = structure.schema;

        if (structure.type == 0) {
            StringBuilder sb = new StringBuilder();
            sb.append(
                    "SELECT TABSCHEMA," +               // 模式名
                            "TABNAME," +                // 表名
                            "TYPE," +                   // 类型(T: 表, V:视图, N:昵称)
                            "CARD AS COUNT," +          // 记录数(最新一次RUNSTATS统计)
                            "LASTUSED," +               // 最近一次访问日期(增删改查)
                            "CREATE_TIME," +            // 表的创建时间
                            "REMARKS AS COMMENT " +     // 表的注释
                            "FROM SYSCAT.TABLES " +
                            "WHERE TYPE = 'T'"
            );
            if (StringTools.isNotEmpty(schema)) {
                sb.append(" AND TABSCHEMA = '" + schema + "'");
            }
            return new SQLBuilderCombine(sb.toString(), null);
        }
        if (structure.type == 1) {
            StringBuilder tableNames = this.getTableNames(structure);
            StringBuilder sb = new StringBuilder();
            sb.append(
                    "SELECT T1.TABSCHEMA," +                        // 模式名
                            "T1.TABNAME," +                         // 表名
                            "T1.COLNAME," +                         // 字段名
                            "T1.TYPENAME," +                        // 字段类型
                            "T1.LENGTH," +                          // 字段长度
                            "T1.SCALE," +                           // 精度
                            "T1.DEFAULT," +                         // 默认值
                            "T1.NULLS," +                           // 是否为空
                            "T1.IDENTITY AS AUTO_INCREMENT," +      // 是否自增
                            "(CASE WHEN T1.COLNAME = T2.COLNAME THEN 'Y' ELSE 'N' END) AS PK," + // 主键
                            "T1.REMARKS " +                         // 用户注释
                            "FROM SYSCAT.COLUMNS T1 " +
                            "LEFT JOIN SYSCAT.KEYCOLUSE T2 ON T2.TABNAME=T1.TABNAME AND T2.TABSCHEMA=T1.TABSCHEMA " +
                            "WHERE T1.TABNAME IN (" + tableNames + ")"
            );
            if (StringTools.isNotEmpty(schema)) {
                sb.append(" AND T1.TABSCHEMA = 'DB2INST1'");
            }
            return new SQLBuilderCombine(sb.toString(), null);
        }
        if (structure.type == 2) {
            StringBuilder tableNames = this.getTableNames(structure);
            StringBuilder sb = new StringBuilder();
            sb.append(
                    "SELECT T1.INDNAME," +                      // 索引名称
                            "T1.TABNAME," +                     //索引表
                            "T1.UNIQUERULE AS TYPE," +          //索引类型 D 普通所有  U 唯一索引  P 主键索引
                            "T2.COLNAME " +                      //列名称
                            "FROM SYSCAT.INDEXES T1 " +
                            "LEFT JOIN SYSCAT.INDEXCOLUSE T2 ON T1.INDNAME = T2.INDNAME " +
                            "WHERE T1.TABNAME IN (" + tableNames + ")"
            );
            if (StringTools.isNotEmpty(schema)) {
                sb.append(" AND T1.TABSCHEMA = 'DB2INST1'");
            }
            return new SQLBuilderCombine(sb.toString(), null);
        }
        return null;
    }

    private StringBuilder getTableNames(StampStructure structure) {
        StringBuilder tableNames = new StringBuilder();
        List<String> tables = structure.tables;
        Iterator<String> iterator = tables.iterator();
        while (iterator.hasNext()) {
            tableNames.append("'" + iterator.next() + "'");
            if (iterator.hasNext()) tableNames.append(",");
        }
        return tableNames;
    }
}
