package org.mimosaframework.orm.i18n;

import org.mimosaframework.core.utils.i18n.MessageWords;
import org.mimosaframework.core.utils.i18n.MessagesRegister;
import org.mimosaframework.orm.*;
import org.mimosaframework.orm.builder.AbstractConfigBuilder;
import org.mimosaframework.orm.builder.XmlConfigBuilder;
import org.mimosaframework.orm.criteria.DefaultFilter;
import org.mimosaframework.orm.criteria.DefaultJoin;
import org.mimosaframework.orm.criteria.DefaultQuery;
import org.mimosaframework.orm.mapping.*;
import org.mimosaframework.orm.platform.AbstractDatabasePorter;
import org.mimosaframework.orm.platform.PlatformFactory;
import org.mimosaframework.orm.platform.PlatformWrapperImpl;
import org.mimosaframework.orm.platform.db2.DB2DatabasePorter;
import org.mimosaframework.orm.platform.db2.DB2DifferentColumn;
import org.mimosaframework.orm.platform.mysql.MysqlDifferentColumn;
import org.mimosaframework.orm.platform.oracle.OracleCarryHandler;
import org.mimosaframework.orm.platform.oracle.OracleDatabasePorter;
import org.mimosaframework.orm.platform.oracle.OracleDifferentColumn;
import org.mimosaframework.orm.platform.postgresql.PostgreSQLDatabasePorter;
import org.mimosaframework.orm.platform.postgresql.PostgreSQLDifferentColumn;
import org.mimosaframework.orm.platform.sqlite.SqliteDatabasePorter;
import org.mimosaframework.orm.platform.sqlserver.SQLServerDifferentColumn;
import org.mimosaframework.orm.scripting.SQLDefinedLoader;
import org.mimosaframework.orm.spring.SpringMimosaSessionFactory;
import org.mimosaframework.orm.strategy.AutoIncrementStrategy;
import org.mimosaframework.orm.strategy.StrategyFactory;
import org.mimosaframework.orm.transaction.*;
import org.mimosaframework.orm.utils.SQLUtils;
import org.mimosaframework.orm.utils.SessionUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.mimosaframework.orm.i18n.LanguageMessageFactory.PROJECT;

public class LanguageMessageDefault implements MessagesRegister {
    private static List<MessageWords> messageWords = new ArrayList<>();

    static {
        // AbstractConfigBuilder
        Map<String, String> mapAbstractConfigBuilder = new HashMap<>();
        mapAbstractConfigBuilder.put("need_basic", "需要先初始化BasicDisposition拿到Convert实例");
        mapAbstractConfigBuilder.put("not_found_mapping_class", "没有找到映射类");
        mapAbstractConfigBuilder.put("instance_convert_error", "实例化 ConvertClass 出错");
        mapAbstractConfigBuilder.put("convert_name_null", "字段名称转换器 %d 不存在");
        mapAbstractConfigBuilder.put("data_source_fail", "加载DataSourceClass类失败");
        messageWords.add(new MessageWords(PROJECT, AbstractConfigBuilder.class, mapAbstractConfigBuilder));

        // XmlConfigBuilder
        Map<String, String> mapXmlConfigBuilder = new HashMap<>();
        mapXmlConfigBuilder.put("not_found_xml", "找不到配置文件");
        mapXmlConfigBuilder.put("parse_xml_error", "解析Mimosa配置文件出错");
        mapXmlConfigBuilder.put("must_app_name", "跟标签 %d 必须设置应用名称属性name作为全局唯一标识(相同应用部署多台机器应用名称也相同)");
        mapXmlConfigBuilder.put("ds_must_name", "XML配置中的ds标签必须起一个name名称");
        mapXmlConfigBuilder.put("wrapper_must_name", "wrapper配置必须有一个name属性");
        mapXmlConfigBuilder.put("not_fount_master", "没有找到名称为 %d 主数据库");
        mapXmlConfigBuilder.put("ds_type_fail", "获得数据库类型出错");
        mapXmlConfigBuilder.put("strategy_class_must_be", "strategy的tableClass属性必须存在");
        mapXmlConfigBuilder.put("strategy_must_ext_id", "strategy的策略实现继承自IDStrategy");
        mapXmlConfigBuilder.put("strategy_impl_must", "strategy的策略实现必须存在");
        mapXmlConfigBuilder.put("not_fount_wrapper_name", "没有找到wrapper名称为 %d 的包装");
        mapXmlConfigBuilder.put("not_fount_data_source", "没有找到名称为 %d 的数据库配置");
        mapXmlConfigBuilder.put("init_data_source_fail", "初始化master数据库链接失败");
        mapXmlConfigBuilder.put("not_fount_slave", "没有找到名称为 %d 的(从)数据库配置");
        mapXmlConfigBuilder.put("must_center_info", "配置中心必须填写server、port和clientName");
        mapXmlConfigBuilder.put("must_center_info_port", "配置中心端口必须填写数字");
        mapXmlConfigBuilder.put("init_intercept_session_error", "初始化interceptSession出错");
        mapXmlConfigBuilder.put("mapping_level_not_found", "映射级别枚举MappingLevel不包含的级别 %d");
        messageWords.add(new MessageWords(PROJECT, XmlConfigBuilder.class, mapXmlConfigBuilder));

        // DefaultFilter
        Map<String, String> mapDefaultFilter = new HashMap<>();
        mapDefaultFilter.put("key_not_allow_null", "查询字段不能为空");
        mapDefaultFilter.put("value_not_allow_null", "查询条件不能为空,特殊查询请用Keyword.NULL");
        mapDefaultFilter.put("value_must_have", "查询条件不能为空");
        mapDefaultFilter.put("value_must_gt0", "查询条件不能为空且条数大于0");
        mapDefaultFilter.put("value_between_must", "区间条件开闭条件不能为空");
        messageWords.add(new MessageWords(PROJECT, DefaultFilter.class, mapDefaultFilter));

        // DefaultJoin
        Map<String, String> mapDefaultJoin = new HashMap<>();
        mapDefaultJoin.put("join_must_table", "JOIN的子表没有设置映射类");
        mapDefaultJoin.put("join_table_diff", "子JOIN映射类 %d 的主表和当前表 %d 不一致");
        mapDefaultJoin.put("just_filter", "仅支持DefaultFilter类型");
        mapDefaultJoin.put("rel_reversal", "当前字段对应关系相反");
        messageWords.add(new MessageWords(PROJECT, DefaultJoin.class, mapDefaultJoin));

        // DefaultQuery
        Map<String, String> mapDefaultQuery = new HashMap<>();
        mapDefaultQuery.put("not_found_table", "Query的主表没有设置映射类");
        mapDefaultQuery.put("sub_table_diff", "子表 %d 的主表和当前表 %d 不一致");
        mapDefaultQuery.put("not_allow_java_bean", "当前是枚举映射成的表不能使用JavaBean方式读取");
        mapDefaultQuery.put("must_value", "请输入要查询的值");
        mapDefaultQuery.put("in_must_key_value", "in查询缺少指定字段");
        mapDefaultQuery.put("not_in_must_value", "not_in缺少查询列表");
        mapDefaultQuery.put("not_in_must_key_value", "not_in查询缺少指定字段");
        mapDefaultQuery.put("join_not_have_filter", "Join %d 查询时缺乏ON Filter条件");
        messageWords.add(new MessageWords(PROJECT, DefaultQuery.class, mapDefaultQuery));

        // AddCompareMapping
        Map<String, String> mapAddCompareMapping = new HashMap<>();
        mapAddCompareMapping.put("add_new_table_error", "向数据库添加新表[%d]出错,请检查映射类[%d],如果出现不支持情况请手动建表");
        mapAddCompareMapping.put("add_new_field_error", "向数据库表[%d]添加新字段[%d]出错,请检查映射类[%d],请手动修改数据库表字段信息");
        messageWords.add(new MessageWords(PROJECT, AddCompareMapping.class, mapAddCompareMapping));

        // CompareMappingFactory
        Map<String, String> mapCompareMappingFactory = new HashMap<>();
        mapCompareMappingFactory.put("not_support_level", "不支持的数据库映射级别");
        messageWords.add(new MessageWords(PROJECT, CompareMappingFactory.class, mapCompareMappingFactory));

        // DefaultDisassembleMappingClass
        Map<String, String> mapDefaultDisassembleMappingClass = new HashMap<>();
        mapDefaultDisassembleMappingClass.put("incr_field_one", "表 %d 自增长字段只允许有一个");
        mapDefaultDisassembleMappingClass.put("timestamp_one", "时间戳类型列只允许有一个");
        mapDefaultDisassembleMappingClass.put("auto_strategy_pk", "自增策略只能使用在主键列 %d 上");
        mapDefaultDisassembleMappingClass.put("must_set_decimal", "BigDecimal类型必须设置精度值,默认255长度过大");
        messageWords.add(new MessageWords(PROJECT, DefaultDisassembleMappingClass.class, mapDefaultDisassembleMappingClass));

        // MappingTableWrapper
        Map<String, String> mapMappingTableWrapper = new HashMap<>();
        mapMappingTableWrapper.put("mapping_defect_name", "映射信息MappingTable中缺少对应的映射表名称");
        mapMappingTableWrapper.put("not_fount_different_column", "没有找到数据库和映射字段对比类实现");
        messageWords.add(new MessageWords(PROJECT, MappingTableWrapper.class, mapMappingTableWrapper));

        // WarnCompareMapping
        Map<String, String> mapWarnCompareMapping = new HashMap<>();
        mapWarnCompareMapping.put("defect_class", "对比数据库发现缺失映射表 %d 映射类 %d");
        mapWarnCompareMapping.put("defect_field", "对比数据库发现缺失字段 %d");
        mapWarnCompareMapping.put("defect_field_table", "对比数据库发现缺失字段 %d 在表 %d 中");
        mapWarnCompareMapping.put("update_field", "对比数据库发现字段 %d 已经被修改");
        mapWarnCompareMapping.put("update_field_table", "对比数据库发现缺失字段 %d 已经被修改," + "在表 %d 中");
        messageWords.add(new MessageWords(PROJECT, WarnCompareMapping.class, mapWarnCompareMapping));

        // DB2DatabasePorter
        Map<String, String> mapDB2DatabasePorter = new HashMap<>();
        mapDB2DatabasePorter.put("not_fount_field", "没有找到字段 %d 映射字段");
        messageWords.add(new MessageWords(PROJECT, DB2DatabasePorter.class, mapDB2DatabasePorter));

        // DB2DifferentColumn
        Map<String, String> mapDB2DifferentColumn = new HashMap<>();
        mapDB2DifferentColumn.put("not_support_type", "不支持的数据类型 %d");
        mapDB2DifferentColumn.put("db2_max_decimal_len", "DB2的Decimal类型整数位最大只支持31位");
        messageWords.add(new MessageWords(PROJECT, DB2DifferentColumn.class, mapDB2DifferentColumn));

        // MysqlDifferentColumn
        Map<String, String> mapMysqlDifferentColumn = new HashMap<>();
        mapMysqlDifferentColumn.put("not_support_type", "不支持的数据类型 %d");
        messageWords.add(new MessageWords(PROJECT, MysqlDifferentColumn.class, mapMysqlDifferentColumn));

        // OracleCarryHandler
        Map<String, String> mapOracleCarryHandler = new HashMap<>();
        mapOracleCarryHandler.put("oracle_auto_incr_empty", "获取Oracle自增序列值为空");
        mapOracleCarryHandler.put("type_not_batch", "传入执行类型不是批量执行");
        messageWords.add(new MessageWords(PROJECT, OracleCarryHandler.class, mapOracleCarryHandler));

        // OracleDatabasePorter
        Map<String, String> mapOracleDatabasePorter = new HashMap<>();
        mapOracleDatabasePorter.put("reset_incr_field", "由于插入数据时带有主键信息，所以开始重置数据库自增初始值");
        mapOracleDatabasePorter.put("reset_incr_field_error", "保存数据时由于包含主键值需要重置主键自增序列时出错");
        messageWords.add(new MessageWords(PROJECT, OracleDatabasePorter.class, mapOracleDatabasePorter));

        // OracleDifferentColumn
        Map<String, String> mapOracleDifferentColumn = new HashMap<>();
        mapOracleDifferentColumn.put("not_support_type", "不支持的数据类型 %d");
        messageWords.add(new MessageWords(PROJECT, OracleDifferentColumn.class, mapOracleDifferentColumn));

        // PostgreSQLDatabasePorter
        Map<String, String> mapPostgreSQLDatabasePorter = new HashMap<>();
        mapPostgreSQLDatabasePorter.put("reset_incr_field", "由于插入数据时带有主键信息，所以开始重置数据库自增初始值");
        mapPostgreSQLDatabasePorter.put("reset_incr_field_error", "保存数据时由于包含主键值需要重置主键自增序列时出错");
        mapPostgreSQLDatabasePorter.put("not_fount_field", "没有找到字段 %d 映射字段");
        messageWords.add(new MessageWords(PROJECT, PostgreSQLDatabasePorter.class, mapPostgreSQLDatabasePorter));

        // PostgreSQLDifferentColumn
        Map<String, String> mapPostgreSQLDifferentColumn = new HashMap<>();
        mapPostgreSQLDifferentColumn.put("not_support_type", "不支持的数据类型 %d");
        messageWords.add(new MessageWords(PROJECT, PostgreSQLDifferentColumn.class, mapPostgreSQLDifferentColumn));

        // SqliteDatabasePorter
        Map<String, String> mapSqliteDatabasePorter = new HashMap<>();
        mapSqliteDatabasePorter.put("not_fount_field", "没有找到字段 %d 映射字段");
        mapSqliteDatabasePorter.put("not_support_type", "不支持的数据类型 %d");
        messageWords.add(new MessageWords(PROJECT, SqliteDatabasePorter.class, mapSqliteDatabasePorter));

        // SQLServerDifferentColumn
        Map<String, String> mapSQLServerDifferentColumn = new HashMap<>();
        mapSQLServerDifferentColumn.put("not_support_type", "不支持的数据类型 %d");
        messageWords.add(new MessageWords(PROJECT, SQLServerDifferentColumn.class, mapSQLServerDifferentColumn));

        // AbstractDatabasePorter
        Map<String, String> mapAbstractDatabasePorter = new HashMap<>();
        mapAbstractDatabasePorter.put("lack_mapping_table", "添加表字段必须有表MappingTable信息");
        mapAbstractDatabasePorter.put("not_found_field", "没有找到字段 %d 映射字段");
        mapAbstractDatabasePorter.put("empty_data", "添加数据库的表或者数据是空的");
        mapAbstractDatabasePorter.put("not_found_table_field", "没有在表 %d 中找到字段 %d");
        mapAbstractDatabasePorter.put("order_not_in_table", "排序字段 %d 不在当前表中");
        messageWords.add(new MessageWords(PROJECT, AbstractDatabasePorter.class, mapAbstractDatabasePorter));

        // PlatformFactory
        Map<String, String> mapPlatformFactory = new HashMap<>();
        mapPlatformFactory.put("not_support_platform", "不支持的数据库平台");
        messageWords.add(new MessageWords(PROJECT, PlatformFactory.class, mapPlatformFactory));

        // PlatformWrapperImpl
        Map<String, String> mapPlatformWrapperImpl = new HashMap<>();
        mapPlatformWrapperImpl.put("not_fount_db_table", "映射类 %d 没有找到对应的数据库表,如果是NOTHING级别请手动创建表 %d");
        messageWords.add(new MessageWords(PROJECT, PlatformWrapperImpl.class, mapPlatformWrapperImpl));

        // SQLDefinedLoader
        Map<String, String> mapSQLDefinedLoader = new HashMap<>();
        mapSQLDefinedLoader.put("not_fount_resource", "找不到资源 %d");
        messageWords.add(new MessageWords(PROJECT, SQLDefinedLoader.class, mapSQLDefinedLoader));

        // SpringMimosaSessionFactory
        Map<String, String> mapSpringMimosaSessionFactory = new HashMap<>();
        mapSpringMimosaSessionFactory.put("center_config_fail", "配置中心配置不完整");
        mapSpringMimosaSessionFactory.put("ds_miss", "缺少数据源配置");
        mapSpringMimosaSessionFactory.put("ds_default_miss", "缺少默认数据源配置(名称为default的数据源束)");
        messageWords.add(new MessageWords(PROJECT, SpringMimosaSessionFactory.class, mapSpringMimosaSessionFactory));

        // AutoIncrementStrategy
        Map<String, String> mapAutoIncrementStrategy = new HashMap<>();
        mapAutoIncrementStrategy.put("special_impl", "数据库自增ID策略,特殊处理无需调用");
        messageWords.add(new MessageWords(PROJECT, AutoIncrementStrategy.class, mapAutoIncrementStrategy));

        // StrategyFactory
        Map<String, String> mapStrategyFactory = new HashMap<>();
        mapStrategyFactory.put("create_strategy_error", "创建ID策略出错");
        messageWords.add(new MessageWords(PROJECT, StrategyFactory.class, mapStrategyFactory));

        // MandatoryTransactionPropagation
        Map<String, String> mapMandatoryTransactionPropagation = new HashMap<>();
        mapMandatoryTransactionPropagation.put("must_in_trans", "当前传播等级要求必须有外层事务");
        messageWords.add(new MessageWords(PROJECT, MandatoryTransactionPropagation.class, mapMandatoryTransactionPropagation));

        // NestedTransactionPropagation
        Map<String, String> mapNestedTransactionPropagation = new HashMap<>();
        mapNestedTransactionPropagation.put("create_trans_fail", "创建事务失败");
        mapNestedTransactionPropagation.put("create_trans_point_fail", "创建事务保存点失败");
        mapNestedTransactionPropagation.put("submit_trans_fail", "提交新的事物失败");
        mapNestedTransactionPropagation.put("rollback_trans_fail", "回滚新的事物失败");
        mapNestedTransactionPropagation.put("rollback_trans_point_fail", "回滚到保存点失败");
        mapNestedTransactionPropagation.put("close_db_fail", "关闭数据库连接失败");
        messageWords.add(new MessageWords(PROJECT, NestedTransactionPropagation.class, mapNestedTransactionPropagation));

        // NeverTransactionPropagation
        Map<String, String> mapNeverTransactionPropagation = new HashMap<>();
        mapNeverTransactionPropagation.put("found_trans", "以非事物运行但是发现已有事物开启");
        mapNeverTransactionPropagation.put("create_trans_fail", "创建事物失败");
        mapNeverTransactionPropagation.put("close_trans_fail", "关闭事物失败");
        messageWords.add(new MessageWords(PROJECT, NeverTransactionPropagation.class, mapNeverTransactionPropagation));

        // NotSupportedTransactionPropagation
        Map<String, String> mapNotSupportedTransactionPropagation = new HashMap<>();
        mapNotSupportedTransactionPropagation.put("create_trans_fail", "创建事物失败");
        mapNotSupportedTransactionPropagation.put("close_db_fail", "关闭数据库连接失败");
        messageWords.add(new MessageWords(PROJECT, NotSupportedTransactionPropagation.class, mapNotSupportedTransactionPropagation));

        // RequiredTransactionPropagation
        Map<String, String> mapRequiredTransactionPropagation = new HashMap<>();
        mapRequiredTransactionPropagation.put("create_trans_fail", "创建事物失败");
        mapRequiredTransactionPropagation.put("submit_trans_fail", "提交事物失败");
        mapRequiredTransactionPropagation.put("rollback_trans_fail", "回滚事物失败");
        mapRequiredTransactionPropagation.put("close_db_fail", "关闭数据库连接失败");
        messageWords.add(new MessageWords(PROJECT, RequiredTransactionPropagation.class, mapRequiredTransactionPropagation));

        // RequiresNewTransactionPropagation
        Map<String, String> mapRequiresNewTransactionPropagation = new HashMap<>();
        mapRequiresNewTransactionPropagation.put("create_trans_fail", "创建事物失败");
        mapRequiresNewTransactionPropagation.put("submit_db_close", "提交事物时数据库连接被关闭");
        mapRequiresNewTransactionPropagation.put("submit_trans_fail", "提交事物失败");
        mapRequiresNewTransactionPropagation.put("rollback_trans_db_close", "回滚事物时数据库连接被关闭");
        mapRequiresNewTransactionPropagation.put("rollback_trans_fail", "回滚事物失败");
        mapRequiresNewTransactionPropagation.put("db_close_fail", "关闭数据库连接失败");
        messageWords.add(new MessageWords(PROJECT, RequiresNewTransactionPropagation.class, mapRequiresNewTransactionPropagation));

        // SupportsTransactionPropagation
        Map<String, String> mapSupportsTransactionPropagation = new HashMap<>();
        mapSupportsTransactionPropagation.put("create_trans_fail", "创建事物失败");
        mapSupportsTransactionPropagation.put("db_close_fail", "关闭数据库连接失败");
        messageWords.add(new MessageWords(PROJECT, SupportsTransactionPropagation.class, mapSupportsTransactionPropagation));

        // TransactionManager
        Map<String, String> mapTransactionManager = new HashMap<>();
        mapTransactionManager.put("check_db_size", "检测到数据库链接个数(%d)个");
        mapTransactionManager.put("create_trans_fail", "创建事物失败,原因没找到配置的DataSource连接");
        messageWords.add(new MessageWords(PROJECT, TransactionManager.class, mapTransactionManager));

        // SessionUtils
        Map<String, String> mapSessionUtils = new HashMap<>();
        mapSessionUtils.put("not_set_mapping_table", "没有设置要操作的映射类");
        mapSessionUtils.put("not_found_mapping_table", "没有找到对应的关系映射[%d]");
        mapSessionUtils.put("not_found_db_table", "没有找到和 %d 对应的数据库映射表");
        messageWords.add(new MessageWords(PROJECT, SessionUtils.class, mapSessionUtils));

        // SQLUtils
        Map<String, String> mapSQLUtils = new HashMap<>();
        mapSQLUtils.put("not_support_db", "不支持的数据库 %d");
        mapSQLUtils.put("must_create_ds", "必须创建一个数据库连接");
        messageWords.add(new MessageWords(PROJECT, SQLUtils.class, mapSQLUtils));

        // AutoResult
        Map<String, String> mapAutoResult = new HashMap<>();
        mapAutoResult.put("call_custom_sql_error", "调用自定义SQL语句出错");
        mapAutoResult.put("value_not_number", "值 %d 不是一个数字");
        messageWords.add(new MessageWords(PROJECT, AutoResult.class, mapAutoResult));

        // BeanAppContext
        Map<String, String> mapBeanAppContext = new HashMap<>();
        mapBeanAppContext.put("get_ds_list_fail", "获得数据源列表出错");
        mapBeanAppContext.put("init_tool_error", "初始化辅助工具配置类出错");
        mapBeanAppContext.put("compare_db_error", "对比数据库映射出错");
        messageWords.add(new MessageWords(PROJECT, BeanAppContext.class, mapBeanAppContext));

        // DefaultSession
        Map<String, String> mapDefaultSession = new HashMap<>();
        mapDefaultSession.put("save_empty", "保存的对象不能为空");
        mapDefaultSession.put("miss_table_class", "请先使用setObjectClass设置对象映射类");
        mapDefaultSession.put("not_found_mapping", "找不到映射类 %d 的映射表");
        mapDefaultSession.put("id_strategy_error", "使用ID生成策略出错");
        mapDefaultSession.put("add_data_error", "添加数据失败");
        mapDefaultSession.put("batch_save_empty", "批量保存列表中存在空对象");
        mapDefaultSession.put("batch_save_table_diff", "批量保存时所有对象表必须一致,[%d]和[%d]不一致");
        mapDefaultSession.put("batch_save_data_error", "批量添加数据失败");
        mapDefaultSession.put("update_empty", "更新对象不能为空");
        mapDefaultSession.put("update_set_id", "修改一个对象必须设置主键的值");
        mapDefaultSession.put("update_fail", "更新数据失败");
        mapDefaultSession.put("update_filter_empty", "使用条件更新数据,过滤条件和要设置的值都不能为空");
        mapDefaultSession.put("delete_id", "删除一个对象必须设置主键的值");
        mapDefaultSession.put("delete_fail", "删除数据失败");
        mapDefaultSession.put("delete_filter_empty", "使用条件删除数据,过滤条件不能为空");
        mapDefaultSession.put("delete_only_pk", "当前方法只允许删除主键存在且唯一的对象,[%d]的主键数量为 %d");
        mapDefaultSession.put("query_only_pk", "当前方法只允许查询主键唯一的值，查询结果数量 %d 不匹配");
        mapDefaultSession.put("get_data_fail", "获取数据失败");
        mapDefaultSession.put("get_data_count_fail", "获取数据条数失败");
        mapDefaultSession.put("not_fount_class", "没有找到查询映射类");
        mapDefaultSession.put("not_found_query", "没有找到查询条件");
        mapDefaultSession.put("include_not_exist", "查询字段中包含不存在的字段");
        mapDefaultSession.put("query_data_fail", "查询数据失败");
        mapDefaultSession.put("not_found_file_sql", "没有发现配置文件SQL");
        mapDefaultSession.put("not_support_action", "不支持的动作标签,当前仅支持select,update,delete,insert");
        mapDefaultSession.put("close_db_fail", "关闭数据库连接出错");
        messageWords.add(new MessageWords(PROJECT, DefaultSession.class, mapDefaultSession));

        // MimosaConnection
        Map<String, String> mapMimosaConnection = new HashMap<>();
        mapMimosaConnection.put("must_ds", "必须传入DataSource实例");
        messageWords.add(new MessageWords(PROJECT, MimosaConnection.class, mapMimosaConnection));

        // MimosaDataSource
        Map<String, String> mapMimosaDataSource = new HashMap<>();
        mapMimosaDataSource.put("close_ds_error", "关闭DataSource数据源出错");
        mapMimosaDataSource.put("not_found_master", "没有找到 master 数据库 DataSource");
        mapMimosaDataSource.put("not_found_slave", "没有找到从数据库配置,切换到主库链接!");
        mapMimosaDataSource.put("not_found_slave_please", "没有找到从数据库配置,请先配置从数据库!");
        mapMimosaDataSource.put("not_found_slave_config", "没有找到从数据库 %d ,请先配置名称为 %d 的从数据库!");
        mapMimosaDataSource.put("run_close_db_error", "执行关闭连接池方法出错");
        messageWords.add(new MessageWords(PROJECT, MimosaDataSource.class, mapMimosaDataSource));

        // MimosaSessionFactory
        Map<String, String> mapMimosaSessionFactory = new HashMap<>();
        mapMimosaSessionFactory.put("not_found_ds", "没有找到名称为 %d 的数据源,如果使用默认数据源传入null或者default字符串即可");
        messageWords.add(new MessageWords(PROJECT, MimosaSessionFactory.class, mapMimosaSessionFactory));

        // MimosaSessionFactoryBuilder
        Map<String, String> mapMimosaSessionFactoryBuilder = new HashMap<>();
        mapMimosaSessionFactoryBuilder.put("init_context", "没有初始化上下文");
        messageWords.add(new MessageWords(PROJECT, MimosaSessionFactoryBuilder.class, mapMimosaSessionFactoryBuilder));

        // MimosaSessionTemplate
        Map<String, String> mapMimosaSessionTemplate = new HashMap<>();
        mapMimosaSessionTemplate.put("fail_rollback", "执行事物失败并回滚");
        mapMimosaSessionTemplate.put("create_factory_error", "创建辅助工具工厂类失败");
        mapMimosaSessionTemplate.put("not_found_cache", "没有找到缓存实例实现");
        mapMimosaSessionTemplate.put("not_found_mq", "没有找到MQ实例实现");
        mapMimosaSessionTemplate.put("not_found_search", "没有找到搜索引擎实例实现");
        mapMimosaSessionTemplate.put("not_found_rpc", "没有找到RPC实例实现");
        mapMimosaSessionTemplate.put("not_found_monitor", "没有找到监控实例实现");
        mapMimosaSessionTemplate.put("not_found_center", "没有找到配置中心实例实现");
        mapMimosaSessionTemplate.put("must_set_factory", "必须先设置SessionFactory");
        messageWords.add(new MessageWords(PROJECT, MimosaSessionTemplate.class, mapMimosaSessionTemplate));

        // ModelMeasureChecker
        Map<String, String> mapModelMeasureChecker = new HashMap<>();
        mapModelMeasureChecker.put("not_found_mapping_class", "映射类不存在或者ModelObject没有指定映射类");
        mapModelMeasureChecker.put("not_in_table", "当前类 %d 没有在映射类列表中");
        mapModelMeasureChecker.put("pk_must", "主键 %d 更新时必须存在");
        mapModelMeasureChecker.put("field_max_len", "字段 %d 太长无法校验通过");
        mapModelMeasureChecker.put("field_min_len", "字段 %d 太短无法校验通过");
        mapModelMeasureChecker.put("field_empty", "字段 %d 不能为空无法校验通过");
        mapModelMeasureChecker.put("field_format", "字段 %d %d 转化数字格式出错");
        mapModelMeasureChecker.put("field_regx", "字段 %d %d 匹配正则表达式 %d 失败");
        messageWords.add(new MessageWords(PROJECT, ModelMeasureChecker.class, mapModelMeasureChecker));

        // NormalContextContainer
        Map<String, String> mapNormalContextContainer = new HashMap<>();
        mapNormalContextContainer.put("conflict_name", "已经存在表名称为 %d 的映射类, %d 和 %d 冲突");
        mapNormalContextContainer.put("empty_mapping_class", "您没有配置映射表类信息,当前不会创建任何表");
        mapNormalContextContainer.put("not_scan_class", "没有扫描到表映射类");
        mapNormalContextContainer.put("please_set_ds", "请设置一个默认数据源");
        mapNormalContextContainer.put("must_ds_name", "数据源必须设置一个名称");
        messageWords.add(new MessageWords(PROJECT, NormalContextContainer.class, mapNormalContextContainer));

        // SingleZipperTable
        Map<String, String> mapSingleZipperTable = new HashMap<>();
        mapSingleZipperTable.put("get_ds_fail", "获得数据量连接失败");
        mapSingleZipperTable.put("mysql_version", "假如你使用MySQL数据库及驱动,请注意数据库和驱动版本大于5," +
                "并且如果你没有在URL上设置参数useCursorFetch=true则设置本参数属于无效行为");
        mapSingleZipperTable.put("next_fail", "拉链表判断是否有下一条失败");
        mapSingleZipperTable.put("zipper_not_allow_del", "拉链表时不允许删除数据");
        messageWords.add(new MessageWords(PROJECT, SingleZipperTable.class, mapSingleZipperTable));

        // SQLAutonomously
        Map<String, String> mapSQLAutonomously = new HashMap<>();
        mapSQLAutonomously.put("not_empty", "要执行的数据源不能为空(多级可以用.表示分层,比如 app.default)");
        messageWords.add(new MessageWords(PROJECT, SQLAutonomously.class, mapSQLAutonomously));
    }

    @Override
    public String[] getLanTypes() {
        return new String[]{"default", "zh"};
    }

    @Override
    public List<MessageWords> getMessages() {
        //
        // Map<String, String> map = new HashMap<>();
        // map.put("", "");
        // messageWords.add(new MessageWords(PROJECT, .class, map));

        return messageWords;
    }
}
