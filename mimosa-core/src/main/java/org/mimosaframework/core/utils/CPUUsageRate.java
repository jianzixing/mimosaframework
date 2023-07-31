package org.mimosaframework.core.utils;

import java.lang.management.*;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 计算当前线程CPU使用率
 */
public class CPUUsageRate {
    private static CPUUsageRate instance = new CPUUsageRate();
    private static RuntimeMXBean runtime = ManagementFactory.getRuntimeMXBean();
    private OperatingSystemMXBean osMxBean;
    private ThreadMXBean threadBean;
    private long preTime = System.nanoTime();
    private long preUsedTime = 0;

    private Map<Long, Long> prevThreadCpuTime = new HashMap<>();
    private long prevUpTime = 0;

    private CPUUsageRate() {
        osMxBean = ManagementFactory.getOperatingSystemMXBean();
        threadBean = ManagementFactory.getThreadMXBean();
    }

    public static CPUUsageRate getInstance() {
        return instance;
    }

    public double getProcessCpu() {
        long totalTime = 0;
        for (long id : threadBean.getAllThreadIds()) {
            totalTime += threadBean.getThreadCpuTime(id);
        }
        long curtime = System.nanoTime();
        long usedTime = totalTime - preUsedTime;
        long totalPassedTime = curtime - preTime;
        preTime = curtime;
        preUsedTime = totalTime;
        return (((double) usedTime) / totalPassedTime / osMxBean.getAvailableProcessors()) * 100;
    }

    //https://www.cnblogs.com/baiduboy/p/6144760.html
    // 计算cpu使用率
    // 计算方式是当前线程的使用时间(线程使用cpu时cpu的使用率肯定是100%的)
    // 然后通过两个时间段中的时间差相除即可
    // 比如 t1使用了cpu 10毫秒，然后这段时间长为20毫秒，这使用率就是 10/20
    public void getThreadCpu() {
        long upTime = runtime.getUptime();
        long[] threadIds = threadBean.getAllThreadIds();
        Map<Long, Long> threadCpuTime = new HashMap<>();
        for (int i = 0; i < threadIds.length; i++) {
            long threadId = threadIds[i];
            if (threadId != -1) {
                threadCpuTime.put(threadId, threadBean.getThreadCpuTime(threadId));
            } else {
                threadCpuTime.put(threadId, 0L);
            }
        }

        int nCPUs = osMxBean.getAvailableProcessors();
        List<Float> cpuUsageList = new ArrayList<Float>();
        if (prevUpTime > 0L && upTime > prevUpTime) {
            // elapsedTime is in ms
            long elapsedTime = upTime - prevUpTime;
            for (int i = 0; i < threadIds.length; i++) {
                long threadId = threadIds[i];
                // elapsedCpu is in ns
                long elapsedCpu = threadCpuTime.get(threadId) - prevThreadCpuTime.get(threadId);
                // cpuUsage could go higher than 100% because elapsedTime
                // and elapsedCpu are not fetched simultaneously. Limit to
                // 99% to avoid Chart showing a scale from 0% to 200%.
                float cpuUsage = Math.min(99F, elapsedCpu / (elapsedTime * 1000000F * nCPUs));
                cpuUsageList.add(cpuUsage);
                ThreadInfo info = threadBean.getThreadInfo(threadId);
                // System.out.println(info.getThreadName() + " " + threadId + " cpu usage " + new BigDecimal(cpuUsage).toPlainString());
                System.out.println(info.getThreadName() + " " + threadId + " cpu usage " + new BigDecimal(elapsedCpu / elapsedTime / 1000000F * 100).toPlainString() + "%");
            }
        }

        this.prevUpTime = upTime;
        this.prevThreadCpuTime = threadCpuTime;
    }

    public static void main(String[] args) throws Exception {
        for (int i = 0; i < 2; i++) {
            new Thread(new Runnable() {
                @Override
                public void run() {
                    while (true) {
                        long bac = 1000000;
                        bac = bac >> 1;
                        if (System.currentTimeMillis() % 38 == 0
                                || System.currentTimeMillis() % 28 == 0) {
                            try {
                                Thread.sleep(1);
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }
                        }
//                    try {
//                        J2V8Main.run();
//                    } catch (IOException e) {
//                        e.printStackTrace();
//                    }
                    }
                }
            }).start();
        }
        while (true) {
            Thread.sleep(1000);
            System.out.println();
            System.out.println(CPUUsageRate.getInstance().getProcessCpu());
            CPUUsageRate.getInstance().getThreadCpu();
        }

    }
}
