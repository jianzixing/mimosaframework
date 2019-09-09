package org.mimosaframework.orm.auxiliary;

public enum MonitoringLevel {
    /**
     * 预料之内的异常捕捉,比如用户输入有误
     */
    LIGHT(100),

    /**
     * 预料之内的异常捕捉,但是已经开始影响业务了,但可以通过编码解决
     * 比如MQ发送失败
     */
    ERROR(200),

    /**
     * 无法预料的异常捕捉
     * 编码无法解决只能通过人工排查
     */
    HIGH_RISK(300),

    /**
     * 无法预料的异常捕捉
     * 会将整个系统拖死,比如数据库无法访问
     */
    DEADLY(400);

    private int code;

    MonitoringLevel(int code) {
        this.code = code;
    }

    public int getCode() {
        return code;
    }
}
