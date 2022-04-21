package org.mimosaframework.core.utils;

import java.text.SimpleDateFormat;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author yangankang
 */
public final class StringTools {
    private static Pattern linePattern = Pattern.compile("_(\\w)");

    public static boolean isEmpty(String s) {
        return s == null || s.equals("") ? true : false;
    }

    public static boolean isNotEmpty(String s) {
        return !isEmpty(s);
    }

    public static boolean isEqual(String s1, String s2) {
        if (isNotEmpty(s1) && isNotEmpty(s2) && s1.equals(s2)) {
            return true;
        }
        if (isEmpty(s1) && isEmpty(s2)) {
            return true;
        }
        return false;
    }

    public static boolean isNumber(String str) {
        NumberUtils.NumberType type = NumberUtils.getNumberType(str);
        return type == null ? false : true;
    }

    public static boolean isNormalName(String appName) {
        byte[] bs = appName.getBytes();
        if ((bs[0] >= 'a' && bs[0] <= 'z') || (bs[0] >= 'A' && bs[0] <= 'z') || bs[0] == '_') {
            for (byte b : bs) {
                if (!((b >= 'a' && b <= 'z') || (b >= 'A' && b <= 'z') || b == '_')) {
                    return false;
                }
            }
            return true;
        }
        return false;
    }

    public static boolean equals(String str1, String str2) {
        return str1 == null ? str2 == null : str1.equals(str2);
    }

    public static boolean equalsIgnoreCase(String str1, String str2) {
        return str1 == null ? str2 == null : str1.equalsIgnoreCase(str2);
    }

    /* 下划线转驼峰 */
    public static String lineToHump(String str) {
        str = str.toLowerCase();
        Matcher matcher = linePattern.matcher(str);
        StringBuffer sb = new StringBuffer();
        while (matcher.find()) {
            matcher.appendReplacement(sb, matcher.group(1).toUpperCase());
        }
        matcher.appendTail(sb);
        return sb.toString();
    }

    /* 驼峰转下划线 */
    public static String humpToLine(String str) {
        return humpToLine(str, false);
    }

    public static String humpToLine(String str, boolean ignoreBrace) {
        if (str == null || str.equals("")) {
            return str;
        }
        if (str.matches("[a-z_]+")) {
            return str;
        }
        StringBuilder column = new StringBuilder();
        char[] sources = str.toCharArray();
        boolean first = true, inBrace = false;
        for (int i = 0; i < sources.length; i++) {
            char c = sources[i];
            if (ignoreBrace != false) {
                if (c == '{') inBrace = true;
                if (c == '}') inBrace = false;
            }
            if (!first) {
                if (c >= 'A' && c <= 'Z' && inBrace == false) {
                    if ((i + 1 < sources.length && sources[i + 1] >= 'A' && sources[i + 1] <= 'Z')
                            && (i > 0 && sources[i - 1] >= 'a' && sources[i - 1] <= 'z')) {
                        column.append("_" + Character.toLowerCase(c));
                    } else if ((i + 1 < sources.length && sources[i + 1] >= 'A' && sources[i + 1] <= 'Z')
                            && (i > 0 && sources[i - 1] >= 'A' && sources[i - 1] <= 'Z')) {
                        column.append(Character.toLowerCase(c));
                    } else if ((i + 1 < sources.length && sources[i + 1] >= 'a' && sources[i + 1] <= 'z')
                            && (i > 0 && sources[i - 1] >= 'A' && sources[i - 1] <= 'Z')) {
                        column.append("_" + Character.toLowerCase(c));
                    } else if ((i + 1 == sources.length)
                            && (i > 0 && sources[i - 1] >= 'A' && sources[i - 1] <= 'Z')) {
                        column.append(Character.toLowerCase(c));
                    } else {
                        column.append("_" + Character.toLowerCase(c));
                    }
                } else {
                    column.append(c);
                }
            } else {
                column.append(Character.toLowerCase(c));
            }
            first = false;
        }
        return column.toString();
    }

    public static String formatTime(Date date) {
        return formatTime(date, null);
    }

    public static String formatTime(Date date, String format) {
        if (StringTools.isEmpty(format)) format = "yyyy-MM-dd HH:mm:ss";
        SimpleDateFormat dateFormat = new SimpleDateFormat(format);
        return dateFormat.format(date);
    }

    public static String formatTime(long time) {
        return formatTime(time, null);
    }

    public static String formatTime(long time, String format) {
        if (StringTools.isEmpty(format)) format = "yyyy-MM-dd HH:mm:ss";
        SimpleDateFormat dateFormat = new SimpleDateFormat(format);
        return dateFormat.format(new Date(time));
    }

    public static String replace(String s, int begin, int end, String replaceStr) {
        if (s.length() >= begin) {
            if (s.length() <= end) {
                return s.substring(0, begin) + replaceStr + s.substring(end);
            } else {
                return s.substring(0, begin) + replaceStr;
            }
        }
        return s;
    }

    public static int toInt(String s) {
        try {
            return Integer.parseInt(s);
        } catch (Exception e) {
            return 0;
        }
    }

    public static int toInt(String s, int replace) {
        try {
            return Integer.parseInt(s);
        } catch (Exception e) {
            return replace;
        }
    }

    /**
     * 过滤 html
     *
     * @param htmlStr
     * @return
     */
    public static String clearHtml(String htmlStr) {
        if (isEmpty(htmlStr)) {
            return "";
        }
        String regEx_script = "<script[^>]*?>[\\s\\S]*?<\\/script>"; // 定义script的正则表达式
        String regEx_style = "<style[^>]*?>[\\s\\S]*?<\\/style>"; // 定义style的正则表达式
        String regEx_html = "<[^>]+>"; // 定义HTML标签的正则表达式
        Pattern p_script = Pattern.compile(regEx_script, Pattern.CASE_INSENSITIVE);
        Matcher m_script = p_script.matcher(htmlStr);
        htmlStr = m_script.replaceAll(""); // 过滤script标签

        Pattern p_style = Pattern.compile(regEx_style, Pattern.CASE_INSENSITIVE);
        Matcher m_style = p_style.matcher(htmlStr);
        htmlStr = m_style.replaceAll(""); // 过滤style标签

        Pattern p_html = Pattern.compile(regEx_html, Pattern.CASE_INSENSITIVE);
        Matcher m_html = p_html.matcher(htmlStr);
        htmlStr = m_html.replaceAll(""); // 过滤html标签

        return convertCharEntities(htmlStr.trim());
    }

    /**
     * 客户端将常用的 html转义字符 转为普通字符进行显示
     *
     * @param htmlSrc
     * @return
     */
    public static String convertCharEntities(String htmlSrc) {
        if (isEmpty(htmlSrc)) {
            return "";
        }
        htmlSrc = htmlSrc.replaceAll("&ensp;", " ");
        htmlSrc = htmlSrc.replaceAll("&emsp;", " ");
        htmlSrc = htmlSrc.replaceAll("&nbsp;", " ");
        htmlSrc = htmlSrc.replaceAll("&lt;", "<");
        htmlSrc = htmlSrc.replaceAll("&gt;", ">");
        htmlSrc = htmlSrc.replaceAll("&amp;", "&");
        htmlSrc = htmlSrc.replaceAll("&quot;", "'");
        htmlSrc = htmlSrc.replaceAll("&copy;", "©");
        htmlSrc = htmlSrc.replaceAll("&reg;", "®");
        htmlSrc = htmlSrc.replaceAll("™", "™");
        htmlSrc = htmlSrc.replaceAll("&times;", "×");
        htmlSrc = htmlSrc.replaceAll("&divide;", "÷");
        return htmlSrc;
    }

    /**
     * 将map转换成 url参数
     *
     * @param map
     * @return
     */
    public static String map2UrlQueryString(Map map) {
        if (map != null) {
            Iterator<Map.Entry> iterator = map.entrySet().iterator();
            StringBuilder sb = new StringBuilder();
            while (iterator.hasNext()) {
                Map.Entry entry = iterator.next();
                sb.append(String.valueOf(entry.getKey()));
                sb.append("=");
                sb.append(String.valueOf(entry.getValue()));
                if (iterator.hasNext()) {
                    sb.append("&");
                }
            }
            return sb.toString();
        }
        return null;
    }

    public static String getHideMobile(String number) {
        return replace(number, 3, 7, "****");
    }

    /**
     * 将数字简化成要展示的文字
     * 比如 11249 转换成 1万+
     * 比如 12 转换成 10+
     *
     * @param count
     * @param dimStr
     * @param lowStr
     * @return
     */
    public static String getDimNumber(long count, String dimStr, String lowStr) {
        if (count > 10) {
            String s = String.valueOf(count);
            count = count / 10000;
            String r = s.substring(0, 1);
            if (s.length() > 1) {
                for (int i = 1; i < s.length(); i++) {
                    r += "0";
                }
            }
            if (count >= 10000) {
                return r + dimStr;
            } else {
                return r + lowStr;
            }
        }
        return count + "";
    }
}
