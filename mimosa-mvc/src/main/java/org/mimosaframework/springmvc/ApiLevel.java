package org.mimosaframework.springmvc;

public enum ApiLevel {
    /**
     * 开放，不需要登录
     */
    OPEN(0),
    /**
     * 需要登录
     */
    LOGIN(1),
    /**
     * 项目级权限
     */
    PROJECT(2),
    /**
     * 严格模式
     */
    STRICT(3),
    /**
     * 使用密钥访问
     */
    KEY(4),
    /**
     * 使用Token访问
     */
    TOKEN(5),
    /**
     * 多次验证
     */
    NFA(6);

    private final int value;

    ApiLevel(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }
}

