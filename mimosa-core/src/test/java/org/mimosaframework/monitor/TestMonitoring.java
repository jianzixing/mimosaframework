package org.mimosaframework.monitor;

import org.mimosaframework.core.monitor.*;
import org.mimosaframework.tools.ConcurrentCallback;
import org.mimosaframework.tools.ConcurrentCreateObject;
import org.mimosaframework.tools.ConcurrentRun;

/**
 * Created by yangankang on 2017/3/27.
 */
public class TestMonitoring {

    public static void main(String[] args) throws MonitoringException {
        TestMonitoring.concurrentHttp();
        TestMonitoring.concurrentTcp();
        TestMonitoring.concurrentUdp();
    }

    public static void concurrentTcp() {
        final RemoteSenderSetting remoteSenderSetting = new RemoteSenderSetting();
        ConcurrentRun concurrentRun = new ConcurrentRun(1000, new ConcurrentCreateObject() {
            @Override
            public ConcurrentCallback getObj() {
                return new ConcurrentCallback() {
                    @Override
                    public boolean start() {

                        try {
                            remoteSenderSetting.setHost("127.0.0.1");
                            remoteSenderSetting.setPort(8081);
                            MonitoringFactory.setSender(RemoteSenderFactory.getRemoteSender(remoteSenderSetting, RemoteSender.TCP_MODE));

                            AgencyMonitoring combineMonitoring = MonitoringFactory.getMonitoring(TestMonitoring.class);
                            combineMonitoring.info("test the monitoring.");
                        } catch (MonitoringException e) {
                            e.printStackTrace();
                            return false;
                        }
                        return true;
                    }
                };
            }
        });
        concurrentRun.start();
    }


    public static void concurrentHttp() {
        final RemoteSenderSetting remoteSenderSetting = new RemoteSenderSetting();
        ConcurrentRun concurrentRun = new ConcurrentRun(1000, new ConcurrentCreateObject() {
            @Override
            public ConcurrentCallback getObj() {
                return new ConcurrentCallback() {
                    @Override
                    public boolean start() {
                        try {
                            remoteSenderSetting.setHost("http://localhost:8080/log");
                            MonitoringFactory.setSender(RemoteSenderFactory.getRemoteSender(remoteSenderSetting, RemoteSender.HTTP_MODE));
                            AgencyMonitoring combineMonitoring = MonitoringFactory.getMonitoring(TestMonitoring.class);
                            combineMonitoring.info("test the monitoring.");
                        } catch (Exception e) {
                            e.printStackTrace();
                            return false;
                        }
                        return true;
                    }
                };
            }
        });
        concurrentRun.start();
    }

    public static void concurrentUdp() {
        final RemoteSenderSetting remoteSenderSetting = new RemoteSenderSetting();
        ConcurrentRun concurrentRun = new ConcurrentRun(1000, new ConcurrentCreateObject() {
            @Override
            public ConcurrentCallback getObj() {
                return new ConcurrentCallback() {
                    @Override
                    public boolean start() {

                        try {
                            remoteSenderSetting.setHost("127.0.0.1");
                            remoteSenderSetting.setPort(8082);
                            MonitoringFactory.setSender(RemoteSenderFactory.getRemoteSender(remoteSenderSetting, RemoteSender.UDP_MODE));

                            AgencyMonitoring combineMonitoring = MonitoringFactory.getMonitoring(TestMonitoring.class);
                            combineMonitoring.info("test the monitoring.");
                        } catch (MonitoringException e) {
                            e.printStackTrace();
                            return false;
                        }
                        return true;
                    }
                };
            }
        });
        concurrentRun.start();
    }
}
