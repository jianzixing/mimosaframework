package org.mimosaframework.orm.i18n;

import org.mimosaframework.core.utils.i18n.MessageWords;
import org.mimosaframework.core.utils.i18n.MessagesRegister;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.mimosaframework.orm.i18n.I18n.PROJECT;

public class LanguageMessageDefault implements MessagesRegister {
    private static List<MessageWords> messageWords = new ArrayList<>();

    static {
        // AbstractConfigBuilder
        Map<String, String> words = new HashMap<>();
        words.put("need_basic", "需要先初始化BasicDisposition拿到Convert实例");
        words.put("instance_convert_error", "实例化 ConvertClass 出错");
        words.put("convert_name_null", "字段名称转换器 %s 不存在");
        words.put("data_source_fail", "加载DataSourceClass类失败");


        // XmlConfigBuilder
        words.put("not_found_xml", "找不到配置文件");
        words.put("parse_xml_error", "解析Mimosa配置文件出错");
        words.put("must_app_name", "跟标签 %s 必须设置应用名称属性name作为全局唯一标识(相同应用部署多台机器应用名称也相同)");
        words.put("ds_must_name", "XML配置中的ds标签必须起一个name名称");
        words.put("wrapper_must_name", "wrapper配置必须有一个name属性");
        words.put("not_fount_master", "没有找到名称为 %s 主数据库");
        words.put("ds_type_fail", "获得数据库类型出错");
        words.put("strategy_class_must_be", "strategy的tableClass属性必须存在");
        words.put("strategy_must_ext_id", "strategy的策略实现继承自IDStrategy");
        words.put("strategy_impl_must", "strategy的策略实现必须存在");
        words.put("not_fount_wrapper_name", "没有找到wrapper名称为 %s 的包装");
        words.put("not_fount_data_source", "没有找到名称为 %s 的数据库配置");
        words.put("init_data_source_fail", "初始化master数据库链接失败");
        words.put("not_fount_slave", "没有找到名称为 %s 的(从)数据库配置");
        words.put("must_center_info", "配置中心必须填写server、port和clientName");
        words.put("must_center_info_port", "配置中心端口必须填写数字");
        words.put("init_intercept_session_error", "初始化interceptSession出错");
        words.put("mapping_level_not_found", "映射级别枚举MappingLevel不包含的级别 %s");

        // DefaultFilter
        words.put("key_not_allow_null", "查询字段不能为空");
        words.put("value_not_allow_null", "查询条件不能为空,特殊查询请用Keyword.NULL");
        words.put("value_must_have", "查询条件不能为空");
        words.put("value_must_gt0", "查询条件不能为空且条数大于0");
        words.put("value_between_must", "区间条件开闭条件不能为空");

        // DefaultJoin
        words.put("join_must_table", "JOIN的子表没有设置映射类");
        words.put("join_table_diff", "子JOIN映射类 %s 的主表和当前表 %s 不一致");
        words.put("just_filter", "仅支持DefaultFilter类型");
        words.put("rel_reversal", "当前字段对应关系相反");

        // DefaultQuery
        words.put("not_found_table", "Query的主表没有设置映射类");
        words.put("sub_table_diff", "子表 %s 的主表和当前表 %s 不一致");
        words.put("not_allow_java_bean", "当前是枚举映射成的表不能使用JavaBean方式读取");
        words.put("must_value", "请输入要查询的值");
        words.put("in_must_key_value", "in查询缺少指定字段");
        words.put("not_in_must_value", "not_in缺少查询列表");
        words.put("not_in_must_key_value", "not_in查询缺少指定字段");
        words.put("join_not_have_filter", "Join %s 查询时缺乏ON Filter条件");

        // AddCompareMapping
        words.put("add_new_table_error", "向数据库添加新表[%s]出错,请检查映射类[%s],如果出现不支持情况请手动建表");
        words.put("add_new_field_error", "向数据库表[%s]添加新字段[%s]出错,请检查映射类[%s],请手动修改数据库表字段信息");

        // CompareMappingFactory
        words.put("not_support_level", "不支持的数据库映射级别");

        // DefaultDisassembleMappingClass
        words.put("incr_field_one", "表 %s 自增长字段只允许有一个");
        words.put("must_have_pk", "表 %s 必须设置一个主键");
        words.put("timestamp_one", "时间戳类型列只允许有一个");
        words.put("auto_strategy_pk", "自增策略只能使用在主键列 %s 上");
        words.put("must_set_decimal", "BigDecimal类型必须设置精度值,默认255长度过大");
        words.put("miss_table_index_name", "表 %s 的索引必须添加一个索引名称");
        words.put("miss_table_index_column", "表 %s 的索引使用不存在的字段 %s");
        words.put("miss_table_index_columns", "表 %s 的索引缺少要索引的字段");

        // WarnCompareMapping
        words.put("defect_class", "对比数据库发现缺失映射表 %s 映射类 %s");
        words.put("defect_field", "对比数据库发现缺失字段 %s");
        words.put("defect_field_table", "对比数据库发现缺失字段 %s 在表 %s 中");
        words.put("update_field", "对比数据库发现字段 %s 已经被修改");
        words.put("update_field_table", "对比数据库发现缺失字段 %s 已经被修改," + "在表 %s 中");

        // DB2DatabasePorter
        words.put("not_fount_field", "没有找到字段 %s 映射字段");

        // DB2DifferentColumn
        words.put("not_support_type", "不支持的数据类型 %s");
        words.put("db2_max_decimal_len", "DB2的Decimal类型整数位最大只支持31位");

        // OracleCarryHandler
        words.put("oracle_auto_incr_empty", "获取Oracle自增序列值为空");
        words.put("type_not_batch", "传入执行类型不是批量执行");

        // OracleDatabasePorter
        words.put("reset_incr_field", "由于插入数据时带有主键信息，所以开始重置数据库自增初始值");
        words.put("reset_incr_field_error", "保存数据时由于包含主键值需要重置主键自增序列时出错");

        // AbstractDatabasePorter
        words.put("lack_mapping_table", "添加表字段必须有表MappingTable信息");
        words.put("not_found_field", "没有找到字段 %s 映射字段");
        words.put("empty_data", "添加数据库的表或者数据是空的");
        words.put("not_found_table_field", "没有在表 %s 中找到字段 %s");
        words.put("order_not_in_table", "排序字段 %s 不在当前表中");
        words.put("not_field_name", "在表 %s 中,没有找到字段 %s");
        words.put("not_support_select_type", "不支持的查询字段 %s");
        words.put("empty_trans_froms", "必须设置要查询的表");
        words.put("empty_trans_froms_fields", "查询表的字段不能为空,SELECT必须设置查询字段,查询所有字段请用FieldSelectType.FULL");

        // PlatformFactory
        words.put("not_support_platform", "不支持的数据库平台");

        // PlatformWrapperImpl
        words.put("not_fount_db_table", "映射类 %s 没有找到对应的数据库表,如果是NOTHING级别请手动创建表 %s");

        // SQLDefinedLoader
        words.put("not_fount_resource", "找不到资源 %s");

        // SpringMimosaSessionFactory
        words.put("center_config_fail", "配置中心配置不完整");
        words.put("ds_miss", "缺少数据源配置");
        words.put("ds_default_miss", "缺少默认数据源配置(名称为default的数据源束)");

        // AutoIncrementStrategy
        words.put("special_impl", "数据库自增ID策略,特殊处理无需调用");

        // StrategyFactory
        words.put("create_strategy_error", "创建ID策略出错");

        // MandatoryTransactionPropagation
        words.put("must_in_trans", "当前传播等级要求必须有外层事务");

        // NestedTransactionPropagation
        words.put("create_trans_fail", "创建事务失败");
        words.put("create_trans_point_fail", "创建事务保存点失败");
        words.put("submit_trans_fail", "提交事物失败");
        words.put("rollback_trans_fail", "回滚事物失败");
        words.put("rollback_trans_point_fail", "回滚到保存点失败");
        words.put("close_db_fail", "关闭数据库连接失败");

        // NeverTransactionPropagation
        words.put("found_trans", "以非事物运行但是发现已有事物开启");
        words.put("close_trans_fail", "关闭事物失败");


        // RequiresNewTransactionPropagation
        words.put("submit_db_close", "提交事物时数据库连接被关闭");
        words.put("rollback_trans_db_close", "回滚事物时数据库连接被关闭");
        words.put("db_close_fail", "关闭数据库连接失败");

        // TransactionManager
        words.put("check_db_size", "检测到数据库链接个数(%s)个");

        // SessionUtils
        words.put("not_set_mapping_table", "没有设置要操作的映射类");
        words.put("not_found_mapping_table", "没有找到对应的关系映射[%s]");
        words.put("not_found_db_table", "没有找到和 %s 对应的数据库映射表");

        // SQLUtils
        words.put("not_support_db", "不支持的数据库 %s");
        words.put("must_create_ds", "必须创建一个数据库连接");

        // AutoResult
        words.put("call_custom_sql_error", "调用自定义SQL语句出错");
        words.put("value_not_number", "值 %s 不是一个数字");

        // BeanAppContext
        words.put("get_ds_list_fail", "获得数据源列表出错");
        words.put("init_tool_error", "初始化辅助工具配置类出错");
        words.put("compare_db_error", "对比数据库映射出错");

        // DefaultSession
        words.put("save_empty", "保存的对象不能为空");
        words.put("miss_table_class", "请先使用setObjectClass设置对象映射类");
        words.put("not_found_mapping", "找不到映射类 %s 的映射表");
        words.put("id_strategy_error", "使用ID生成策略出错");
        words.put("add_data_error", "添加数据失败");
        words.put("batch_save_empty", "批量保存列表中存在空对象");
        words.put("batch_save_table_diff", "批量保存时所有对象表必须一致,[%s]和[%s]不一致");
        words.put("batch_save_data_error", "批量添加数据失败");
        words.put("update_empty", "更新对象不能为空");
        words.put("update_set_id", "修改一个对象必须设置主键的值");
        words.put("update_fail", "更新数据失败");
        words.put("update_filter_empty", "使用条件更新数据,过滤条件和要设置的值都不能为空");
        words.put("delete_id", "删除一个对象必须设置主键的值");
        words.put("delete_fail", "删除数据失败");
        words.put("delete_filter_empty", "使用条件删除数据,过滤条件不能为空");
        words.put("delete_only_pk", "当前方法只允许删除主键存在且唯一的对象,[%s]的主键数量为 %s");
        words.put("query_only_pk", "当前方法只允许查询主键唯一的值，查询结果数量 %s 不匹配");
        words.put("get_data_fail", "获取数据失败");
        words.put("get_data_count_fail", "获取数据条数失败");
        words.put("not_fount_class", "没有找到查询映射类");
        words.put("not_found_query", "没有找到查询条件");
        words.put("include_not_exist", "查询字段中包含不存在的字段");
        words.put("query_data_fail", "查询数据失败");
        words.put("not_found_file_sql", "没有发现配置文件SQL");
        words.put("not_support_action", "不支持的动作标签,当前仅支持select,update,delete,insert");

        // MimosaConnection
        words.put("must_ds", "必须传入DataSource实例");

        // MimosaDataSource
        words.put("close_ds_error", "关闭DataSource数据源出错");
        words.put("not_found_master", "没有找到 master 数据库 DataSource");
        words.put("not_found_slave", "没有找到从数据库配置,切换到主库链接!");
        words.put("not_found_slave_please", "没有找到从数据库配置,请先配置从数据库!");
        words.put("not_found_slave_config", "没有找到从数据库 %s ,请先配置名称为 %s 的从数据库!");
        words.put("run_close_db_error", "执行关闭连接池方法出错");

        // MimosaSessionFactory
        words.put("not_found_ds", "没有找到名称为 %s 的数据源,如果使用默认数据源传入null或者default字符串即可");

        // MimosaSessionFactoryBuilder
        words.put("init_context", "没有初始化上下文");

        // MimosaSessionTemplate
        words.put("fail_rollback", "执行事物失败并回滚");
        words.put("create_factory_error", "创建辅助工具工厂类失败");
        words.put("not_found_cache", "没有找到缓存实例实现");
        words.put("not_found_mq", "没有找到MQ实例实现");
        words.put("not_found_search", "没有找到搜索引擎实例实现");
        words.put("not_found_rpc", "没有找到RPC实例实现");
        words.put("not_found_monitor", "没有找到监控实例实现");
        words.put("not_found_center", "没有找到配置中心实例实现");
        words.put("must_set_factory", "必须先设置SessionFactory");

        // ModelMeasureChecker
        words.put("not_found_mapping_class", "映射类不存在或者ModelObject没有指定映射类");
        words.put("not_in_table", "当前类 %s 没有在映射类列表中");
        words.put("pk_must", "主键 %s 更新时必须存在");
        words.put("field_max_len", "字段 %s 太长无法校验通过");
        words.put("field_min_len", "字段 %s 太短无法校验通过");
        words.put("field_empty", "字段 %s 不能为空无法校验通过");
        words.put("field_format", "字段 %s %s 转化数字格式出错");
        words.put("field_regx", "字段 %s %s 匹配正则表达式 %s 失败");

        // NormalContextContainer
        words.put("conflict_name", "已经存在表名称为 %s 的映射类, %s 和 %s 冲突");
        words.put("empty_mapping_class", "您没有配置映射表类信息,当前不会创建任何表");
        words.put("not_scan_class", "没有扫描到表映射类");
        words.put("please_set_ds", "请设置一个默认数据源");
        words.put("must_ds_name", "数据源必须设置一个名称");

        // SingleZipperTable
        words.put("get_ds_fail", "获得数据量连接失败");
        words.put("mysql_version", "假如你使用MySQL数据库及驱动,请注意数据库和驱动版本大于5," +
                "并且如果你没有在URL上设置参数useCursorFetch=true则设置本参数属于无效行为");
        words.put("next_fail", "拉链表判断是否有下一条失败");
        words.put("zipper_not_allow_del", "拉链表时不允许删除数据");

        // SQLAutonomously
        words.put("not_empty", "要执行的数据源不能为空(多级可以用.表示分层,比如 app.default)");

        // SelectBuilder
        words.put("empty_from_builder", "没有设置要查询的字段");

        // RelationDatabaseExecutor
        words.put("result_set_empty", "没有获取到ResultSet的结果集");

        // CommonSQLBuilder
        words.put("miss_table_mapping", "没有找到类 %s 的映射类,请检查是否是表映射类");
        words.put("miss_field_mapping", "映射类 %s 中没有找到 %s 映射字段");

        // StampAction
        words.put("miss_index_columns", "缺少索引的列");

        // NothingCompareMapping
        words.put("compare_mapping_warn_create_table", "需要新建数据库表 %s");
        words.put("compare_mapping_warn_field_update", "需要更新表 %s 字段 %s 的 %s 属性");
        words.put("compare_mapping_warn_field_add", "需要向表 %s 添加字段 %s ");
        words.put("compare_mapping_warn_field_del", "需要删除表 %s 的字段 %s");
        words.put("compare_mapping_warn_index_update", "需要重置表 %s 的索引 %s");
        words.put("compare_mapping_warn_index_add", "需要向表 %s 添加索引 %s");

        // PlatformExecutor
        words.put("platform_executor_empty_type", "不支持的映射类型 %s");
        words.put("fun_miss_filter", "函数查询缺少having的条件");
        words.put("miss_executor_mapping_field", "缺少映射字段 %s 信息");

        // PlatformDialect
        words.put("platform_dialect_miss_fields", "表 %s 缺少字段信息");

        // DefaultSQLSelectBuilder
        words.put("select_from_select_must", "select的from子句必须是select语句");

        // PlatformDialect
        words.put("copy_table_data_error", "重建表(%s)数据无法使用被清除,错误描述 %s");

        messageWords.add(new MessageWords(PROJECT, words));
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
