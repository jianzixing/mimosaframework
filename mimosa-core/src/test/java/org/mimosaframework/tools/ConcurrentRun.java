package org.mimosaframework.tools;

import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.*;

/**
 * @author yangankang
 */
public class ConcurrentRun extends Thread {
    private ExecutorService service = null;
    private int threadCount = 0;
    private ConcurrentCreateObject concurrentCreateObject;

    public ConcurrentRun(int threadCount, ConcurrentCreateObject concurrentCreateObject) {
        service = Executors.newFixedThreadPool(threadCount);
        this.threadCount = threadCount;
        this.concurrentCreateObject = concurrentCreateObject;
    }

    @Override
    public void run() {
        List<Callable<String>> callables = new LinkedList<Callable<String>>();
        for (int i = 0; i < threadCount; i++) {
            Callable callable = new Callable() {
                @Override
                public String call() throws Exception {
                    concurrentCreateObject.getObj().start();
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
