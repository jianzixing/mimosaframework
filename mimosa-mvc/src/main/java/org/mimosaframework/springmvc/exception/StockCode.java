package org.mimosaframework.springmvc.exception;

/**
 * @author yangankang
 */
public class StockCode {
    /**
     * 参数是空的
     */
    public static final StockCode ARG_NULL = new StockCode("arg_null");
    /**
     * 参数不是空的
     */
    public static final StockCode ARG_NOT_NULL = new StockCode("arg_not_null");
    /**
     * 数据校验不通过
     */
    public static final StockCode ARG_VALID = new StockCode("arg_valid");
    /**
     * 数据已经存在
     */
    public static final StockCode EXIST_OBJ = new StockCode("exist_obj");
    /**
     * 执行失败
     */
    public static final StockCode FAILURE = new StockCode("failure");
    /**
     * 系统必须项目
     */
    public static final StockCode SYSTEM_MUST = new StockCode("system_must");
    /**
     * 数值不够
     */
    public static final StockCode NOT_ENOUGH = new StockCode("not_enough");
    /**
     * 不存在
     */
    public static final StockCode NOT_EXIST = new StockCode("not_exist");
    /**
     * 数值太大
     */
    public static final StockCode TOO_LARGE = new StockCode("too_large");
    /**
     * 数值太小
     */
    public static final StockCode TOO_SMALL = new StockCode("too_small");
    /**
     * 需要user信息
     */
    public static final StockCode MUST_USER = new StockCode("must_user");
    /**
     * 状态不争气
     */
    public static final StockCode STATUS_ERROR = new StockCode("status_error");
    /**
     * 系统依赖的数据
     */
    public static final StockCode SYSTEM_DEPEND_DATA = new StockCode("system_depend_data");
    /**
     * 数据中没有找到主键值
     */
    public static final StockCode NOT_PRIMARY_KEY = new StockCode("not_primary_key");
    /**
     * 创建文件夹失败
     */
    public static final StockCode CREATE_FOLDER_FAIL = new StockCode("create_folder_fail");
    /**
     * 创建文件失败
     */
    public static final StockCode CREATE_FILE_FAIL = new StockCode("create_file_fail");
    /**
     * 重命名失败
     */
    public static final StockCode RENAME_FILE_FAIL = new StockCode("rename_file_fail");
    /**
     * 删除文件失败
     */
    public static final StockCode DELETE_FILE_FAIL = new StockCode("delete_file_fail");
    public static final StockCode COPY_FILE_FAIL = new StockCode("copy_file_fail");
    public static final StockCode EXIST_DIR = new StockCode("exist_dir");
    public static final StockCode REG_FAIL = new StockCode("reg_fail");
    public static final StockCode NOT_ALLOW = new StockCode("not_allow");
    public static final StockCode API_CALL_FAIL = new StockCode("api_call_fail");
    public static final StockCode USING = new StockCode("using");
    public static final StockCode NOT_SUPPORT = new StockCode("not_support");
    public static final StockCode FORMAT = new StockCode("format");
    public static final StockCode ENABLE = new StockCode("enable");
    public static final StockCode DISABLE = new StockCode("disable");
    public static final StockCode NOT_START = new StockCode("not_start");
    public static final StockCode IS_FINISH = new StockCode("is_finish");

    private Object code;

    public StockCode(int code) {
        this.code = code;
    }

    public StockCode(String code) {
        this.code = code;
    }

    public Object getCode() {
        return this.code;
    }

    public static StockCode getStockCode(int code) {
        return new StockCode(code);
    }

    public static StockCode getStockCode(String code) {
        return new StockCode(code);
    }

    public boolean equals(Object code) {
        if (code instanceof StockCode) {
            return (this.code.equals(((StockCode) code).code));
        }
        return (this.code.equals(code));
    }

    public String toString() {
        return String.valueOf(this.code);
    }
}
