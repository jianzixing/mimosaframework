package org.mimosaframework.core.monitor;

import org.mimosaframework.core.json.ModelObject;
import org.mimosaframework.core.utils.StringTools;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.net.Socket;

/**
 * 使用tcp方式发送监控日志，TCP方式能确保所有的消息的送达，但是也是会阻塞的
 * tcp的接收者只需要实现服务器接口就行，传送的数据格式是json格式，每一条消息会
 * 以\r\n为结束符,java端用流接收器readLine就行了
 *
 * @author yangankang
 */
public class TcpAsyncRemoteSender implements RemoteSender {

    /**
     * 远程服务器的配置信息
     */
    private RemoteSenderSetting setting;

    /**
     * 每一个发送者实例的socket的输出流
     */
    private PrintStream out;

    /**
     * 每一个发送者实例的socket的输入流
     */
    private BufferedReader buf;

    /**
     * 每一个发送者实例只有一个socket实例
     */
    private Socket socket = null;

    /**
     * tcp方式的构造函数，必须设置配置信息
     *
     * @param setting
     */
    public TcpAsyncRemoteSender(RemoteSenderSetting setting) {
        this.setting = setting;
        this.initSocket(setting);
    }

    /**
     * 设置配置信息，构造函数传入之后也可以修改当前的配置信息，但不管怎么
     * 修改配置信息都不能为空
     *
     * @param setting 配置参数
     */
    @Override
    public void setSetting(RemoteSenderSetting setting) {
        if (setting != null) {
            this.setting = setting;
            this.initSocket(setting);
        }
    }

    /**
     * 初始化socket实例并建立连接，如果不成功则会报错但不影响整个系统，
     * 初始化是一并初始化的还有socket的流，如果配置信息变化时也会调用
     * 这个方法重新生成socket
     *
     * @param setting socket配置信息
     */
    private void initSocket(RemoteSenderSetting setting) {
        String host = setting.getHost();
        int port = setting.getPort();
        if (!StringTools.isEmpty(host)) {
            if (socket != null) {
                try {
                    socket.close();
                    socket = null;
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            try {
                socket = new Socket(host, port);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        if (socket != null) {
            try {
                //获取Socket的输出流，用来发送数据到服务端
                out = new PrintStream(socket.getOutputStream());
                //获取Socket的输入流，用来接收从服务端发送过来的数据
                buf = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 使用tcp方式发送日志消息，日志消息的格式是json格式，发送前判断socket和
     * 消息内容是否正确才会发送，如果不正确不会有任何异常,只管发送不会接收处理任何回应消息
     *
     * @param monitoringCarrier 消息内容
     */
    @Override
    public void sendMonitoring(MonitoringCarrier monitoringCarrier) {

        String message = this.getSendMessage(monitoringCarrier);
        if (!StringTools.isEmpty(message) && socket != null
                && out != null) {
            out.println(message);
            try {
                buf.readLine();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 把消息对象转化成一个json格式的数据
     *
     * @param monitoringCarrier
     * @return
     */
    private String getSendMessage(MonitoringCarrier monitoringCarrier) {
        ModelObject object = new ModelObject();
        if (monitoringCarrier.getLevel() != null
                && monitoringCarrier.getMessage() != null) {
            object.put("method", TCP_MODE);
            object.put("level", monitoringCarrier.getLevel().name());
            object.put("message", String.valueOf(monitoringCarrier.getMessage()));
            if (monitoringCarrier.getClazz() != null) {
                object.put("clazz", monitoringCarrier.getClazz().getName());
            }
            if (monitoringCarrier.getThrowable() != null) {
                object.put("throwable", monitoringCarrier.getThrowable().getClass().getName());
                object.put("throwableMessage", monitoringCarrier.getThrowable().getMessage());
            }
            if (monitoringCarrier.getDate() != null) {
                object.put("date", monitoringCarrier.getDate().getTime());
            }
            return object.toJSONString();
        }
        return null;
    }
}
