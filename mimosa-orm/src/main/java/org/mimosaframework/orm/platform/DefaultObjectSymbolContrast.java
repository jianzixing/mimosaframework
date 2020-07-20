package org.mimosaframework.orm.platform;

import org.mimosaframework.core.utils.StringTools;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Mysql 对比时如果是字符串
 * '213kjdolafsd' 如果和数字对比则会去掉所有非数字字符变成 213，比如 '213kjdolafsd'=213 返回时true
 * <p>
 * 如果两个比较值都是字符串则按照字符串对比来
 * 'kjsdflk9889dgfd' = 0等式成立如果第一个字符不是数字就按照0处理
 * <p>
 * 如果时间和字符串比较 则先把字符串前面的数字拿下来然后转换成时间类型
 */
public class DefaultObjectSymbolContrast implements ObjectSymbolContrast {
    @Override
    public boolean isTrue(Object first, Object second, String symbol) {
        if (first != null && second != null) {
            if (first instanceof Date || second instanceof Date) {
                if (first instanceof Date && second instanceof Date) {
                    long time1 = ((Date) first).getTime();
                    long time2 = ((Date) second).getTime();
                    if (symbol.equals("=")) {
                        return time1 == time2;
                    } else if (symbol.equals("!=")) {
                        return time1 != time2;
                    } else if (symbol.equals(">")) {
                        return time1 > time2;
                    } else if (symbol.equals("<")) {
                        return time1 < time2;
                    }
                }
                SimpleDateFormat format = new SimpleDateFormat("yyyyMMddHHmmss");
                if (isStringType(first)) {
                    String s1 = isFullNumber(String.valueOf(first));
                    String s2 = format.format(second);
                    if (symbol.equals("=")) {
                        return s1.equals(s2);
                    } else if (symbol.equals("!=")) {
                        return !s1.equals(s2);
                    } else if (symbol.equals(">")) {
                        return s1.compareTo(s2) > 0;
                    } else if (symbol.equals("<")) {
                        return s1.compareTo(s2) < 0;
                    }
                }
                if (isStringType(second)) {
                    String s1 = format.format(first);
                    String s2 = isFullNumber(String.valueOf(second));
                    if (symbol.equals("=")) {
                        return s1.equals(s2);
                    } else if (symbol.equals("!=")) {
                        return !s1.equals(s2);
                    } else if (symbol.equals(">")) {
                        return s1.compareTo(s2) > 0;
                    } else if (symbol.equals("<")) {
                        return s1.compareTo(s2) < 0;
                    }
                }
            }

            if (isStringType(first) && isStringType(second)) {
                String str1 = String.valueOf(first);
                String str2 = String.valueOf(second);
                if (symbol.equals("=")) {
                    return str1.equals(str2);
                } else if (symbol.equals("!=")) {
                    return !str1.equals(str2);
                } else if (symbol.equals(">")) {
                    return str1.compareTo(str2) > 0;
                } else if (symbol.equals("<")) {
                    return str1.compareTo(str2) < 0;
                }
            }
            if (isStringType(first) && isNumberType(second)) {
                String s = isStartNumber(String.valueOf(first));
                double d1 = Double.parseDouble(s);
                double d2 = numberToDouble(second);
                if (symbol.equals("=")) {
                    return d1 == d2;
                } else if (symbol.equals("!=")) {
                    return d1 != d2;
                } else if (symbol.equals(">")) {
                    return d1 > d2;
                } else if (symbol.equals("<")) {
                    return d1 < d2;
                }
            }
            if (isNumberType(first) && isStringType(second)) {
                String s = isStartNumber(String.valueOf(second));
                double d1 = numberToDouble(first);
                double d2 = Double.parseDouble(s);
                if (symbol.equals("=")) {
                    return d1 == d2;
                } else if (symbol.equals("!=")) {
                    return d1 != d2;
                } else if (symbol.equals(">")) {
                    return d1 > d2;
                } else if (symbol.equals("<")) {
                    return d1 < d2;
                }
            }
            if (isNumberType(first) && isNumberType(second)) {
                double d1 = numberToDouble(first);
                double d2 = numberToDouble(second);
                if (symbol.equals("=")) {
                    return d1 == d2;
                } else if (symbol.equals("!=")) {
                    return d1 != d2;
                } else if (symbol.equals(">")) {
                    return d1 > d2;
                } else if (symbol.equals("<")) {
                    return d1 < d2;
                }
            }
        }

        return false;
    }

    private String isStartNumber(String s) {
        byte[] bytes = s.getBytes();
        StringBuilder sb = new StringBuilder();
        for (byte b : bytes) {
            if (b >= '0' && b <= '9') {
                byte[] bbb = new byte[]{b};
                String sss = new String(bbb);
                sb.append(sss);
            } else {
                break;
            }
        }
        String r = sb.toString();
        if (StringTools.isEmpty(r)) {
            return "0";
        } else {
            return sb.toString();
        }
    }

    private String isFullNumber(String s) {
        byte[] bytes = s.getBytes();
        StringBuilder sb = new StringBuilder();
        for (byte b : bytes) {
            if (b >= '0' && b <= '9') {
                byte[] bbb = new byte[]{b};
                String sss = new String(bbb);
                sb.append(sss);
            }
        }
        String r = sb.toString();
        if (StringTools.isEmpty(r)) {
            return "0";
        } else {
            return sb.toString();
        }
    }

    private double numberToDouble(Object obj) {
        if (obj instanceof Boolean) {
            Boolean b = (Boolean) obj;
            if (b == true) return 1;
            else return 0;
        }
        if (obj instanceof Double) {
            return (Double) obj;
        }
        if (obj instanceof BigDecimal) {
            return ((BigDecimal) obj).doubleValue();
        }
        if (obj instanceof Integer) {
            return (Integer) obj;
        }
        if (obj instanceof Short) {
            return (Short) obj;
        }
        if (obj instanceof Byte) {
            return (Byte) obj;
        }
        if (obj instanceof Long) {
            return (Long) obj;
        }
        if (obj instanceof Float) {
            return (Float) obj;
        }
        return 0;
    }

    private boolean isStringType(Object obj) {
        if (obj instanceof String) return true;
        if (obj instanceof Character) return true;
        return false;
    }

    private boolean isNumberType(Object obj) {
        if (obj instanceof Boolean) return true;
        if (obj instanceof Double) return true;
        if (obj instanceof BigDecimal) return true;
        if (obj instanceof Integer) return true;
        if (obj instanceof Short) return true;
        if (obj instanceof Byte) return true;
        if (obj instanceof Long) return true;
        if (obj instanceof Float) return true;
        return false;
    }
}
