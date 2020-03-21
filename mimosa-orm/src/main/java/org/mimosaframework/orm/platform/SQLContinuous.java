package org.mimosaframework.orm.platform;

public class SQLContinuous {
    private Object[] objects;

    public SQLContinuous(Object... objects) {
        this.objects = objects;
    }

    public String toString(String ruleStart, String ruleFinish) {
        StringBuilder sb = new StringBuilder();
        for (Object o : objects) {
            if (o == RuleType.RULE_START) {
                sb.append(ruleStart);
            } else if (o == RuleType.RULE_FINISH) {
                sb.append(ruleFinish);
            } else {
                sb.append(o);
            }
        }
        return sb.toString();
    }

    public enum RuleType {
        RULE_START,
        RULE_FINISH
    }
}
