/**
 * 本包属于辅助系统，用于监控整个系统发生的日志事件
 * 日志事件分5种等级同普通的日志等级是一样的
 * {@link org.mimosaframework.core.monitor.MonitoringLevel}
 * <p>
 * 日志发生后根据配置信息发送不同等级的消息到监控服务器
 * 本包实现3种监控协议，http、tcp、udp协议，监控消息只会发送一次且不会处理任何返回消息
 *
 * @author yangankang
 * @see org.mimosaframework.core.monitor.MonitoringFactory
 * @see org.mimosaframework.core.monitor.RemoteSenderFactory
 * @see org.mimosaframework.core.monitor.HttpAsyncRemoteSender
 * @see org.mimosaframework.core.monitor.UdpAsyncRemoteSender
 */
package org.mimosaframework.core.monitor;