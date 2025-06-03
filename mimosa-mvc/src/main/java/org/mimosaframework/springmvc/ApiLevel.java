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
    STRICT(3);

    private final int value;

    ApiLevel(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }
}
