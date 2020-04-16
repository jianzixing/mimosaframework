package org.mimosaframework.orm.platform;

import org.mimosaframework.orm.mapping.MappingTable;

/**
 * 调用{@link PlatformDialect#define(DataDefinition)}后返回的参数
 * 参数代表上层对接下来要做的事情做出反应
 */
public enum DialectNextStep {
    /**
     * 不需要做任何下一步动作
     */
    NONE,
    /**
     * 需要重建表调用{@link PlatformDialect#rebuildTable(MappingTable, TableStructure)}方法
     */
    REBUILD
}
