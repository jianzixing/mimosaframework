package org.mimosaframework.springmvc;

public enum ApiLevel {
    /**
     * 开放，不需要鉴权
     */
    OPEN(0),
    /**
     * 需要用户登录鉴权
     */
    LOGIN(1),
    /**
     * 需要后台登录鉴权
     */
    ADMIN(2),
    /**
     * 项目级权限鉴权
     */
    PROJECT(3),
    /**
     * 官网级权限鉴权
     */
    OFFICIAL(4),
    /**
     * 严格模式，无含义自行使用
     */
    STRICT(5),
    /**
     * 使用密钥访问鉴权
     */
    KEY(6),
    /**
     * 使用Token访问鉴权
     */
    TOKEN(7),
    /**
     * 内部API调用鉴权
     */
    CALL(8),
    /**
     * 多步验证
     */
    NFA(9),
    ;

    private final int value;

    ApiLevel(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }
}
