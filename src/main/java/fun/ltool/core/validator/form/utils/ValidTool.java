package fun.ltool.core.validator.form.utils;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.regex.Pattern;

/**
 * 表单校检 表单项字段 辅助类
 * <p>部分正则转自 hutool</p>
 *
 * @author huangrongsong
 * @since 1.0
 */
public class ValidTool {
    /**
     * 数字
     */
    public final static Pattern NUMBERS = Pattern.compile("\".*\\\\d.*");
    /**
     * 移动电话
     * eg: 中国大陆： +86  180 4953 1399，2位区域码标示+11位数字
     * 中国大陆 +86 Mainland China
     */
    public final static Pattern MOBILE = Pattern.compile("(?:0|86|\\+86)?1[3-9]\\d{9}");
    /**
     * 邮件，符合RFC 5322规范，正则来自：<a href="http://emailregex.com/">...</a>
     * What is the maximum length of a valid email address? <a href="https://stackoverflow.com/questions/386294/what-is-the-maximum-length-of-a-valid-email-address/44317754">...</a>
     * 注意email 要宽松一点。比如 jetz.chong@hutool.cn、jetz-chong@ hutool.cn、jetz_chong@hutool.cn、dazhi.duan@hutool.cn 宽松一点把，都算是正常的邮箱
     */
    public final static Pattern EMAIL = Pattern.compile("(?:[a-z0-9!#$%&'*+/=?^_`{|}~-]+(?:\\.[a-z0-9!#$%&'*+/=?^_`{|}~-]+)*|\"(?:[\\x01-\\x08\\x0b\\x0c\\x0e-\\x1f\\x21\\x23-\\x5b\\x5d-\\x7f]|\\\\[\\x01-\\x09\\x0b\\x0c\\x0e-\\x7f])*\")@(?:(?:[a-z0-9](?:[a-z0-9-]*[a-z0-9])?\\.)+[a-z0-9](?:[a-z0-9-]*[a-z0-9])?|\\[(?:(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)\\.){3}(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?|[a-z0-9-]*[a-z0-9]:(?:[\\x01-\\x08\\x0b\\x0c\\x0e-\\x1f\\x21-\\x5a\\x53-\\x7f]|\\\\[\\x01-\\x09\\x0b\\x0c\\x0e-\\x7f])+)])");

    /**
     * 符号
     */
    public final static Pattern SYMBOL = Pattern.compile("\".*[^a-zA-Z0-9\\\\s].*\"");

    /**
     * 判断是否为空 为空则返回true
     *
     * @param value 字段
     * @param <T>   字段泛型
     * @return 校检结果
     */
    public static <T> boolean isEmpty(T value) {
        boolean isValid = value == null;
        if (value instanceof String) {
            if (value.toString().trim().isEmpty()) {
                isValid = true;
            }
        }
        if (value instanceof Integer) {
            if (value.toString().trim().isEmpty()) {
                isValid = true;
            }
        }
        if (value instanceof Boolean) {
            if (value.toString().trim().isEmpty()) {
                isValid = true;
            }
        }
        if (value instanceof Double) {
            if (value.toString().trim().isEmpty()) {
                isValid = true;
            }
        }
        if (value instanceof Float) {
            if (value.toString().trim().isEmpty()) {
                isValid = true;
            }
        }
        if (value instanceof Long) {
            if (value.toString().trim().isEmpty()) {
                isValid = true;
            }
        }
        if (value instanceof LocalDateTime || value instanceof LocalDate || value instanceof LocalTime) {
            if (value.toString().trim().isEmpty()) {
                isValid = true;
            }
        }
        return isValid;
    }

    /**
     * 判断是否为空 不为空则返回true
     *
     * @param value 字段
     * @param <T>   字段泛型
     * @return 校检结果
     */
    public static <T> boolean isNotEmpty(T value) {
        boolean isValid = value != null;
        if (value instanceof String) {
            if (value.toString().trim().isEmpty()) {
                isValid = false;
            }
        }
        if (value instanceof Integer) {
            if (value.toString().trim().isEmpty()) {
                isValid = false;
            }
        }
        if (value instanceof Boolean) {
            if (value.toString().trim().isEmpty()) {
                isValid = false;
            }
        }
        if (value instanceof Double) {
            if (value.toString().trim().isEmpty()) {
                isValid = false;
            }
        }
        if (value instanceof Float) {
            if (value.toString().trim().isEmpty()) {
                isValid = false;
            }
        }
        if (value instanceof Long) {
            if (value.toString().trim().isEmpty()) {
                isValid = false;
            }
        }
        if (value instanceof LocalDateTime || value instanceof LocalDate || value instanceof LocalTime) {
            if (value.toString().trim().isEmpty()) {
                isValid = false;
            }
        }
        return isValid;
    }

    /**
     * 校检正则表达式
     *
     * @param pattern 正则表达式
     * @param value   值
     * @return 正则为null或者""则不检查，返回true，内容为null返回false
     */
    public static boolean isMatch(Pattern pattern, CharSequence value) {
        if (value == null || pattern == null) {
            // 提供null的字符串为不匹配
            return false;
        }
        return pattern.matcher(value).matches();
    }

    /**
     * 验证是否为手机号码（中国）
     *
     * @param value 值
     * @return 是否为手机号码（中国）
     */
    public static boolean isMobile(CharSequence value) {
        return isMatch(MOBILE, value);
    }

    /**
     * 验证是否为可用邮箱地址
     *
     * @param value 值
     * @return true为可用邮箱地址
     */
    public static boolean isEmail(CharSequence value) {
        return isMatch(EMAIL, value);
    }

    /**
     * 验证是否包含大小写字母
     *
     * @param value 值
     * @return true为包含大小写字母
     */
    public static boolean isUpperOrLowerCase(CharSequence value) {
        if (value == null || value.toString().trim().isEmpty()) {
            return false;
        }
        boolean hasLowerCase = false;
        boolean hasUpperCase = false;

        // 遍历字符串的每个字符并判断是否包含大小写字母
        for (char c : value.toString().toCharArray()) {
            if (Character.isLowerCase(c)) {
                hasLowerCase = true;
            } else if (Character.isUpperCase(c)) {
                hasUpperCase = true;
            }
        }

        // 判断是否包含大小写字母
        return hasLowerCase && hasUpperCase;
    }

    /**
     * 是否包含数字
     *
     * @param value 值
     * @return boolean 是否存在数字
     */
    public static boolean isHasNumber(CharSequence value) {
        return isMatch(NUMBERS, value);
    }

    /**
     * 是否包含符号
     *
     * @param value 值
     * @return boolean 是否包含符号
     */
    public static boolean isSymbol(CharSequence value) {
        return isMatch(SYMBOL, value);
    }

    /**
     * 判断 source 是否为null 如果为null 则用 target 替换 source
     *
     * @param source 源数据
     * @param target 目标数据
     * @return 处理后数据
     */
    public static String nullReplace(String source, String target) {
        if (source == null) {
            return target;
        }
        return source;
    }
}
