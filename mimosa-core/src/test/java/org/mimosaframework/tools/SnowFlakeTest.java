package org.mimosaframework.tools;

import org.mimosaframework.core.utils.SnowFlake;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.*;

public class SnowFlakeTest extends Thread {
    private static List<Long> list = new ArrayList<>();
    private static SnowFlake snowFlake = new SnowFlake();
    private ExecutorService service = null;
    private int threadCount = 0;

    public SnowFlakeTest(int threadCount) {
        service = Executors.newFixedThreadPool(threadCount);
        this.threadCount = threadCount;
    }

    public static void main(String[] args) {
        new SnowFlakeTest(1000).start();
    }

    @Override
    public void run() {
        List<Callable<String>> callables = new LinkedList<Callable<String>>();
        for (int i = 0; i < threadCount; i++) {
            Callable callable = new Callable() {
                @Override
                public String call() throws Exception {
                    Long id = snowFlake.nextId();
                    if (list.contains(id)) {
                        throw new Exception("重复了");
                    }
                    list.add(id);
                    return "ok";
                }
            };
            callables.add(callable);
        }

        try {

            List<Future<String>> results = service.invokeAll(callables);
            for (Future<String> future : results) {
                String success = future.get();
                if (success != "ok") {
                    throw new Exception("报错啦！怪我咯！");
                } else {
                    System.out.println(list.toString());
                }
            }
            service.shutdown();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
