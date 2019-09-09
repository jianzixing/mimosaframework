package org.mimosaframework.orm.utils;

import org.mimosaframework.core.utils.NumberUtils;

public class SimpleTruthValueSymbol implements TruthValueSymbol {
    @Override
    public boolean is(Object source, Object target, String symbol) {
        if (symbol != null) {
            if (symbol.equals("=")) {
                if (source.equals(target)) {
                    return true;
                }
                if (source == target) {
                    return true;
                }
            }

            if (symbol.equals(">") && this.gt(source, target, false)) {
                return true;
            }

            if (symbol.equals(">=") && this.gt(source, target, true)) {
                return true;
            }
            if (symbol.equals("<") && this.lt(source, target, false)) {
                return true;
            }

            if (symbol.equals("<=") && this.lt(source, target, true)) {
                return true;
            }

            if (symbol.equals("!=") && this.ne(source, target)) {
                return true;
            }

            if (symbol.equals("in") && this.in(source, target)) {
                return true;
            }

            if (symbol.equals("like") && this.like(source, target)) {
                return true;
            }
        }
        return false;
    }

    private boolean gt(Object source, Object target, boolean hasEqual) {
        if (hasEqual && source.equals(target)) {
            return true;
        }

        if (NumberUtils.isNumber(source) && NumberUtils.isNumber(target)) {
            if (hasEqual) {
                if ((Double) source >= (Double) target) return true;
            } else {
                if ((Double) source > (Double) target) return true;
            }
        }

        if (source instanceof String && target instanceof String) {
            if (((String) source).compareTo((String) target) > 0) {
                return true;
            } else {
                return false;
            }
        }
        return false;
    }

    private boolean lt(Object source, Object target, boolean hasEqual) {
        if (hasEqual && source.equals(target)) {
            return true;
        }

        if (NumberUtils.isNumber(source) && NumberUtils.isNumber(target)) {
            if (hasEqual) {
                if ((Double) source <= (Double) target) return true;
            } else {
                if ((Double) source < (Double) target) return true;
            }
        }

        if (source instanceof String && target instanceof String) {
            if (((String) source).compareTo((String) target) > 0) {
                return false;
            } else {
                return true;
            }
        }
        return false;
    }

    private boolean ne(Object source, Object target) {
        if (!source.equals(target) && source != target) {
            return true;
        }
        return false;
    }

    private boolean in(Object source, Object target) {
        if (target.getClass().isArray()) {
            Object[] objects = (Object[]) target;
            for (Object o : objects) {
                if (source == o || source.equals(o)) {
                    return true;
                }
            }
        }
        return false;
    }

    private boolean like(Object source, Object target) {
        if (String.valueOf(source).indexOf(String.valueOf(target).replaceAll("%", "")) >= 0) {
            return true;
        }
        return false;
    }
}
