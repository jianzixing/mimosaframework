package org.mimosaframework.core.utils.parallel;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.util.*;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class ParallelUtils {
    private static final Log logger = LogFactory.getLog(ParallelUtils.class);
    private static ExecutorService executor = Executors.newCachedThreadPool();

    public static void shutdown() {
        executor.shutdown();
    }

    public static <T> List<T> run(int concurrent, ParallelTask<T> task) {

        List<Future<T>> futures = new ArrayList<Future<T>>();
        for (int i = 0; i < concurrent; i++) {
            Future<T> future = executor.submit(task);
            futures.add(future);
        }
        List<T> results = new ArrayList<T>();
        for (Future<T> future : futures) {
            try {
                T result = future.get();
                results.add(result);
            } catch (ExecutionException e) {
                e.printStackTrace();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        return results;
    }

    public static <T> void run(int times, int concurrent, ParallelTask<T> task, ParallelItem<T> item) {
        run(times, concurrent, 1000, task, item, null);
    }

    public static <T> void run(int times, int concurrent, ParallelTask<T> task, ParallelItem<T> item, OneTimeEndCallback callback) {
        run(times, concurrent, 1000, task, item, callback);
    }

    public static <T> void run(int times, List<ParallelTask<T>> tasks, ParallelItem<T> item) {
        run(times, 1000, tasks, item, null);
    }

    public static <T> void run(final int times, long intervalTime, final List<ParallelTask<T>> tasks, final ParallelItem<T> item, final OneTimeEndCallback callback) {
        if (tasks != null) {
            for (int i = 0; i < times; i++) {
                List<T> list = ParallelUtils.run(tasks);
                if (item != null) {
                    item.done(i, list);
                }
                try {
                    logger.info("第" + i + "次执行完毕");
                    if (callback != null) callback.end();
                    Thread.sleep(intervalTime);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public static <T> void run(final int times, final int concurrent, long intervalTime, final ParallelTask<T> task, final ParallelItem<T> item, final OneTimeEndCallback callback) {
        if (task != null) {
            for (int i = 0; i < times; i++) {
                List<T> list = ParallelUtils.run(concurrent, task);
                if (item != null) {
                    item.done(i, list);
                }
                try {
                    logger.info("第" + i + "次执行完毕");
                    if (callback != null) callback.end();
                    Thread.sleep(intervalTime);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public static <T> List<T> run(List<ParallelTask<T>> tasks) {

        List<Future<T>> futures = new ArrayList<Future<T>>();
        for (ParallelTask<T> task : tasks) {
            Future<T> future = executor.submit(task);
            futures.add(future);
        }
        List<T> results = new ArrayList<T>();
        for (Future<T> future : futures) {
            try {
                T result = future.get();
                results.add(result);
            } catch (ExecutionException e) {
                e.printStackTrace();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        return results;
    }
}
