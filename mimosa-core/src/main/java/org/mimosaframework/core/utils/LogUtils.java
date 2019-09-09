package org.mimosaframework.core.utils;

import org.apache.commons.logging.Log;

/**
 * Created by qinmingtao on 2016/5/30.
 * Desc 用于统一添加日志的工具类
 */
public class LogUtils {
    private static final String splitStr = " ^=^ "; //分隔符

    /**
     * debug日志
     *
     * @param logger     logger实例
     * @param methodDesc 方法描述
     */
    public static void logDebug(Log logger, String methodDesc, String key, String message, Throwable throwable) {
        if (!logger.isDebugEnabled()) {
            return;
        }
        // 增加换行显示
        StringBuilder buf = new StringBuilder(splitStr + "[DEBUG]");
        buf.append("[功能名|方法名]-->").append(methodDesc).append(splitStr);
        buf.append("[关键字]-->").append(key).append(splitStr);
        buf.append("[日志信息]-->").append(message).append(splitStr);
        if (throwable != null) {
            buf.append("[异常信息]-->").append(throwable.getMessage()).append(splitStr);
        }
        buf.append(System.getProperty("line.separator"));
        logger.debug(buf.toString(), throwable);
    }

    /**
     * error日志
     */
    public static void logError(Log logger, String methodDesc, String key, String message, Throwable throwable) {
        if (!logger.isErrorEnabled()) {
            return;
        }
        StringBuilder buf = new StringBuilder(splitStr + "[ERROR]");
        buf.append("[功能名|方法名]-->").append(methodDesc).append(splitStr);
        buf.append("[关键字]-->").append(key).append(splitStr);
        buf.append("[日志信息]-->").append(message).append(splitStr);
        if (throwable != null) {
            buf.append("[异常信息]-->").append(throwable.getMessage()).append(splitStr);
        }
        buf.append(System.getProperty("line.separator"));
        logger.error(buf.toString(), throwable);
    }

    public static void logInfo(Log logger, String methodDesc, String key, String message) {
        if (!logger.isInfoEnabled()) {
            return;
        }
        StringBuilder buf = new StringBuilder(splitStr + "[INFO]");
        buf.append("[功能名|方法名]-->").append(methodDesc).append(splitStr);
        buf.append("[关键字]-->").append(key).append(splitStr);
        buf.append("[日志信息]-->").append(message).append(splitStr);

        buf.append(System.getProperty("line.separator"));
        logger.info(buf.toString());
    }

}
