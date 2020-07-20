package org.mimosaframework.orm.sql.stamp;

/**
 * SQL 公式比如 amount = amount - 1
 * 只支持加法和减法
 */
public class StampFormula {
    public Formula[] formulas;

    public static class Formula {
        public Express express;
        // StampColumn or Number
        public StampColumn column;
        public Number value;
    }

    public enum Express {
        ADD, MINUS
    }
}
