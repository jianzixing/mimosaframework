package org.mimosaframework.orm.utils;

import java.util.Map;

public class LOBLoader {
    private static final ThreadLocal<Loader> THREAD_LOCAL = new ThreadLocal();

    public interface Loader {
        void lob(Map map, String columnName, Object lob);
    }

    public static void register(Loader loader) {
        THREAD_LOCAL.set(loader);
    }

    public static Loader currentLoader() {
        return THREAD_LOCAL.get();
    }

    public static void close() {
        THREAD_LOCAL.remove();
    }
}
