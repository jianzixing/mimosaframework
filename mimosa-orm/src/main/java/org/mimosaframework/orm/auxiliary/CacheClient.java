package org.mimosaframework.orm.auxiliary;

import java.util.List;
import java.util.Map;

public interface CacheClient extends AuxiliaryClient {
    int NOT_EXIST_SET = 0;
    int EXIST_SET = 1;
    int SECONDS = 0;
    int MILLI_SECONDS = 1;

    void set(String key, String value);

    void del(String key);

    String get(String key);

    List<String> get(String... ks);

    void lock(String key, CacheLockCallback lock);

    void lock(String key, CacheLockCallback lock, long milliSeconds);

    long incr(String key);

    void setExpireSeconds(String key, String value, int time);

    void setExpireMilliseconds(String key, String value, long time);

    void setNX(String key, String value);

    void setEX(String key, long seconds, String value);

    void set(String key, String value, int nxxx, int expx, long time);

    void set(Map<String, String> map);

    void expire(String key, long secondsOrMilliSeconds);

    boolean exists(String key);

    /**
     * 使用一定步长增长
     *
     * @param key
     * @param value
     * @return
     */
    long incrByLong(String key, long value);
}
