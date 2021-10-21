package org.mimosaframework.core.utils;

import org.mimosaframework.core.exception.ModuleException;

public class AssistUtils {
    public static void isNull(Object o, String s) {
        if (o == null) {
            throw new IllegalArgumentException(s);
        }
    }

    public static void isNull(Object o, String code, String s) {
        if (o == null) {
            throw new ModuleException(code, s);
        }
    }

    public static void isNull(Object o, int code, String s) {
        if (o == null) {
            throw new ModuleException(code, s);
        }
    }

    public static void error(String msg) {
        throw new ModuleException(-999, msg);
    }

    public static void error(String code, String msg) {
        throw new ModuleException(code, msg);
    }

    public static void error(int code, String msg) {
        throw new ModuleException(code, msg);
    }

    public static void error(String msg, Throwable e) {
        throw new ModuleException(-999, msg, e);
    }

    public static void error(String code, String msg, Throwable e) {
        throw new ModuleException(code, msg, e);
    }

    public static void error(int code, String msg, Throwable e) {
        throw new ModuleException(code, msg, e);
    }

    public static void notNull(Object object, String msg) {
        if (object != null) {
            throw new IllegalArgumentException(msg);
        }
    }

    public static void notNull(Object object, String code, String msg) {
        if (object != null) {
            throw new ModuleException(code, msg);
        }
    }

    public static void notNull(Object object, int code, String msg) {
        if (object != null) {
            throw new ModuleException(code, msg);
        }
    }

    public static void empty(Object object, String msg) {
        if (object == null || (object instanceof String && ((String) object).trim().equals(""))) {
            error(msg);
        }
    }

    public static void zero(Long id, String msg) {
        if (id == null || id == 0) {
            error(msg);
        }
    }

    public static void zero(Integer id, String msg) {
        if (id == null || id == 0) {
            error(msg);
        }
    }

    public static void empty(Object object, String code, String msg) {
        if (object == null || (object instanceof String && ((String) object).trim().equals(""))) {
            error(code, msg);
        }
    }

    public static void zero(Long id, String code, String msg) {
        if (id == null || id == 0) {
            error(code, msg);
        }
    }

    public static void zero(Integer id, String code, String msg) {
        if (id == null || id == 0) {
            error(msg);
        }
    }

    public static void empty(Object object, int code, String msg) {
        if (object == null || (object instanceof String && ((String) object).trim().equals(""))) {
            error(code, msg);
        }
    }

    public static void zero(Long id, int code, String msg) {
        if (id == null || id == 0) {
            error(code, msg);
        }
    }

    public static void zero(Integer id, int code, String msg) {
        if (id == null || id == 0) {
            error(msg);
        }
    }
}
