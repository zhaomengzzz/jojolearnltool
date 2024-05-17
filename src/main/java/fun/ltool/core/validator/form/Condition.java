package fun.ltool.core.validator.form;


import fun.ltool.core.exceptions.ValidatorException;
import fun.ltool.core.lang.fun.SFunction;
import fun.ltool.core.validator.form.bean.ValidateResult;
import fun.ltool.core.validator.form.config.PasswordConfig;
import fun.ltool.core.validator.form.utils.ValidTool;

import java.util.Arrays;
import java.util.Optional;
import java.util.function.Consumer;
import java.util.regex.Pattern;

import static fun.ltool.core.validator.form.utils.ValidTool.nullReplace;


/**
 * 表单校检条件类
 *
 * @param <T> 表单实体泛型
 * @author huangrongsong
 * @since 1.0
 */
public class Condition<T, R> {
    /**
     * 表单操作类
     */
    private final ValidatorForm.Form<T> form;

    /**
     * 表单项 操作类
     */
    private final ValidatorForm.Form.FormField<T, R> formField;

    /**
     * 表单校检结果
     */
    protected ValidateResult validateResult;
    /**
     * 字段校检结果
     */
    protected Boolean isValid = false;

    /**
     * 构造方法
     *
     * @param formField 表单项 操作类实例
     */
    public Condition(ValidatorForm.Form.FormField<T, R> formField) {
        this.form = formField.form;
        this.formField = formField;
    }

    /**
     * 构建表单校检规则
     *
     * @param formField 表单项 操作类
     * @param <T>       表单泛型
     * @param <R>       表单项的值
     * @return 表单校检规则实例
     */
    public static <T, R> Condition<T, R> create(ValidatorForm.Form.FormField<T, R> formField) {
        return new Condition<>(formField);
    }

    /**
     * 判断表单字段 为空则校检通过
     *
     * @param sFunctions 表单项
     * @return 链式调用返回本身
     */
    @SafeVarargs
    public final Condition<T, R> empty(SFunction<T, ?>... sFunctions) {
        return empty(null, sFunctions);
    }

    /**
     * 判断表单字段 为空则校检通过
     *
     * @param msg        校检失败时候的提示信息 会覆盖默认提示
     * @param sFunctions 表单项
     * @return 链式调用返回本身
     */
    @SafeVarargs
    public final Condition<T, R> empty(String msg, SFunction<T, ?>... sFunctions) {
        for (SFunction<T, ?> sFunction : sFunctions) {
            String propertyName = sFunction.methodToProperty();
            Object value = sFunction.apply(this.form.form);
            isValid = ValidTool.isEmpty(value);
            if (!isValid) {
                validateResult = getValidateResult(false, propertyName, msg);
                return this;
            }
        }
        return this;
    }

    /**
     * 判断表单字段 不为空则校检通过
     *
     * @param sFunctions 表单项
     * @return 链式调用返回本身
     */
    @SafeVarargs
    public final Condition<T, R> notEmpty(SFunction<T, ?>... sFunctions) {
        return notEmpty(null, sFunctions);
    }

    /**
     * 判断表单字段 不为空则校检通过
     *
     * @param msg        校检失败时候的提示信息 会覆盖默认提示
     * @param sFunctions 表单项
     * @return 链式调用返回本身
     */
    @SafeVarargs
    public final Condition<T, R> notEmpty(String msg, SFunction<T, ?>... sFunctions) {
        for (SFunction<T, ?> sFunction : sFunctions) {
            String propertyName = sFunction.methodToProperty();
            Object value = sFunction.apply(this.form.form);
            isValid = ValidTool.isNotEmpty(value);
            if (!isValid) {
                validateResult = getValidateResult(false, propertyName, msg);
                return this;
            }
        }
        return this;
    }

    /**
     * 判断表单字段 是否校检长度
     *
     * @param len        长度
     * @param sFunctions 表单项
     * @return 链式调用返回本身
     */
    @SafeVarargs
    public final Condition<T, R> len(int len, SFunction<T, ?>... sFunctions) {
        return len(null, len, sFunctions);
    }

    /**
     * 判断表单字段 是否校检长度
     *
     * @param msg        校检失败时候的提示信息 会覆盖默认提示
     * @param len        长度
     * @param sFunctions 表单项
     * @return 链式调用返回本身
     */
    @SafeVarargs
    public final Condition<T, R> len(String msg, int len, SFunction<T, ?>... sFunctions) {
        for (SFunction<T, ?> sFunction : sFunctions) {
            String propertyName = sFunction.methodToProperty();
            Object value = sFunction.apply(this.form.form);
            Optional.ofNullable(value).map(Object::toString).ifPresent(val -> isValid = val.length() == len);
            if (!isValid) {
                validateResult = getValidateResult(false, propertyName, msg);
                return this;
            }
        }
        return this;
    }

    /**
     * 判断表单字段 是否校检长度是否在区间内
     *
     * @param min        最小长度区间
     * @param max        最大长度区间
     * @param sFunctions 表单项
     * @return 链式调用返回本身
     */
    @SafeVarargs
    public final Condition<T, R> lenBetween(int min, int max, SFunction<T, ?>... sFunctions) {
        return lenBetween(null, min, max, sFunctions);
    }

    /**
     * 判断表单字段 是否校检长度是否在区间内
     * <p>最小长度区间默认 0</p>
     *
     * @param max        最大长度区间
     * @param sFunctions 表单项
     * @return 链式调用返回本身
     */
    @SafeVarargs
    public final Condition<T, R> lenBetween(int max, SFunction<T, ?>... sFunctions) {
        return lenBetween(null, 0, max, sFunctions);
    }

    /**
     * 判断表单字段 是否校检长度是否在区间内
     *
     * @param msg        校检失败时候的提示信息 会覆盖默认提示
     * @param min        最小长度区间
     * @param max        最大长度区间
     * @param sFunctions 表单项
     * @return 链式调用返回本身
     */
    @SafeVarargs
    public final Condition<T, R> lenBetween(String msg, int min, int max, SFunction<T, ?>... sFunctions) {
        for (SFunction<T, ?> sFunction : sFunctions) {
            String propertyName = sFunction.methodToProperty();
            Object value = sFunction.apply(this.form.form);
            Optional.ofNullable(value).map(Object::toString).ifPresent(val -> isValid = val.length() >= min && val.length() <= max);
            if (!isValid) {
                validateResult = getValidateResult(false, propertyName, msg);
                return this;
            }
        }
        return this;
    }

    /**
     * 判断表单字段 是否校检 是否是手机号码
     *
     * @param sFunctions 表单项
     * @return 链式调用返回本身
     */
    @SafeVarargs
    public final Condition<T, R> mobile(SFunction<T, ?>... sFunctions) {
        return mobile(null, sFunctions);
    }

    /**
     * 判断表单字段 是否校检 是否是手机号码
     *
     * @param msg        校检失败时候的提示信息 会覆盖默认提示
     * @param sFunctions 表单项
     * @return 链式调用返回本身
     */
    @SafeVarargs
    public final Condition<T, R> mobile(String msg, SFunction<T, ?>... sFunctions) {
        for (SFunction<T, ?> sFunction : sFunctions) {
            String propertyName = sFunction.methodToProperty();
            Object value = sFunction.apply(this.form.form);
            Optional.ofNullable(value).map(Object::toString).ifPresent(val -> isValid = ValidTool.isMobile(val));
            if (!isValid) {
                validateResult = getValidateResult(false, propertyName, msg);
                return this;
            }
        }
        return this;
    }

    /**
     * 判断表单字段 是否校检 是否是email
     *
     * @param sFunctions 表单项
     * @return 链式调用返回本身
     */
    @SafeVarargs
    public final Condition<T, R> email(SFunction<T, ?>... sFunctions) {
        return email(null, sFunctions);
    }

    /**
     * 判断表单字段 是否校检 是否是email
     *
     * @param msg        校检失败时候的提示信息 会覆盖默认提示
     * @param sFunctions 表单项
     * @return 链式调用返回本身
     */
    @SafeVarargs
    public final Condition<T, R> email(String msg, SFunction<T, ?>... sFunctions) {
        for (SFunction<T, ?> sFunction : sFunctions) {
            String propertyName = sFunction.methodToProperty();
            Object value = sFunction.apply(this.form.form);
            Optional.ofNullable(value).map(Object::toString).ifPresent(val -> isValid = ValidTool.isEmail(val));
            if (!isValid) {
                validateResult = getValidateResult(false, propertyName, msg);
                return this;
            }
        }
        return this;
    }

    /**
     * 判断表单字段 是否校检密码
     * <p>不传递 config 默认只校检是否为空</p>
     *
     * @param sFunctions 表单项
     * @return 链式调用返回本身
     */
    @SafeVarargs
    public final Condition<T, R> password(SFunction<T, ?>... sFunctions) {
        return password(null, null, sFunctions);
    }

    /**
     * 判断表单字段 是否校检密码
     *
     * @param config     密码验证配置
     * @param sFunctions 表单项
     * @return 链式调用返回本身
     */
    @SafeVarargs
    public final Condition<T, R> password(PasswordConfig config, SFunction<T, ?>... sFunctions) {
        return password(null, config, sFunctions);
    }

    /**
     * 判断表单字段 是否校检密码
     *
     * @param msg        校检失败时候的提示信息 会覆盖默认提示
     * @param config     密码验证配置
     * @param sFunctions 表单项
     * @return 链式调用返回本身
     */
    @SafeVarargs
    public final Condition<T, R> password(String msg, PasswordConfig config, SFunction<T, ?>... sFunctions) {
        for (SFunction<T, ?> sFunction : sFunctions) {
            String propertyName = sFunction.methodToProperty();
            Object value = sFunction.apply(this.form.form);
            Optional.ofNullable(value).map(Object::toString).ifPresent(val -> {
                if (config != null) {
                    Integer len = config.getLen();
                    Integer min = config.getMin();
                    Integer max = config.getMax();
                    // 长度校检
                    if (len != null) {
                        if (min != null || max != null) {
                            throw new ValidatorException("密码验证配置规则有误， len 和 max、min 不能同时配置，请检查");
                        }
                        // 判断长度是否相等
                        if (val.length() != len) {
                            validateResult = getValidateResult(false, propertyName, nullReplace(msg, "密码长度需要" + len + "位数，请检查"));
                        }
                    } else {
                        if (min != null && max != null) {
                            if (max < min) {
                                throw new ValidatorException("密码验证配置规则有误， max 小于 min 请检查");
                            }
                        }
                        if (min != null && min > val.length()) {
                            validateResult = getValidateResult(false, propertyName, nullReplace(msg, "密码长度必须在" + min + "和" + max + "之间，请检查"));
                        }
                        if (max != null && max < val.length()) {
                            validateResult = getValidateResult(false, propertyName, nullReplace(msg, "密码长度必须在" + min + "和" + max + "之间，请检查"));
                        }
                    }
                    Boolean containLetter = config.getContainLetter();
                    Boolean containCaps = config.getContainCaps();
                    Boolean containNumber = config.getContainNumber();
                    Boolean containSymbol = config.getContainSymbol();
                    // 字母大小写数字符号校检
                    if (containLetter != null && containLetter) {
                        // 判断是否包含字母
                        boolean isLetter = false;
                        for (char c : val.toCharArray()) {
                            if (isLetter) {
                                continue;
                            }
                            if (Character.isLetter(c)) {
                                isLetter = true;
                            }
                        }
                        if (!isLetter) {
                            validateResult = getValidateResult(false, propertyName, nullReplace(msg, "密码未包含字母"));
                        }
                    }
                    if (containCaps != null && containCaps) {
                        // 是否包含大小写
                        if (!ValidTool.isUpperOrLowerCase(val)) {
                            validateResult = getValidateResult(false, propertyName, nullReplace(msg, "密码未包含大小写"));
                        }
                    }
                    if (containNumber != null && containNumber) {
                        // 是否包含数字
                        if (!ValidTool.isHasNumber(val)) {
                            validateResult = getValidateResult(false, propertyName, nullReplace(msg, "密码未包含数字"));
                        }
                    }
                    if (containSymbol != null && containSymbol) {
                        // 是否包含符号
                        if (!ValidTool.isSymbol(val)) {
                            validateResult = getValidateResult(false, propertyName, nullReplace(msg, "密码未包含符号"));
                        }
                    }
                } else {
                    isValid = ValidTool.isNotEmpty(val);
                }
            });
            if (!isValid) {
                validateResult = getValidateResult(false, propertyName, msg);
                return this;
            }
        }
        return this;
    }

    /**
     * 判断表单字段 是否校检自定义正则表达式
     *
     * @param regex      正则表达式
     * @param sFunctions 表单项
     * @return 链式调用返回本身
     */
    @SafeVarargs
    public final Condition<T, R> matches(Pattern regex, SFunction<T, ?>... sFunctions) {
        return matches(null, regex, sFunctions);
    }

    /**
     * 判断表单字段 是否校检自定义正则表达式
     *
     * @param msg        校检失败时候的提示信息 会覆盖默认提示
     * @param regex      正则表达式
     * @param sFunctions 表单项
     * @return 链式调用返回本身
     */
    @SafeVarargs
    public final Condition<T, R> matches(String msg, Pattern regex, SFunction<T, ?>... sFunctions) {
        for (SFunction<T, ?> sFunction : sFunctions) {
            String propertyName = sFunction.methodToProperty();
            Object value = sFunction.apply(this.form.form);
            Optional.ofNullable(value).map(Object::toString).ifPresent(val -> isValid = ValidTool.isMatch(regex, val));
            if (!isValid) {
                validateResult = getValidateResult(false, propertyName, msg);
                return this;
            }
        }
        return this;
    }

    /**
     * 分组验证
     *
     * @param groupSFunction 分组验证字段 字段不为空才验证分组里面的规则，为空则忽略分组里面规则
     * @param consumers      分组校检规则
     * @return 链式调用返回自身
     * @since 1.1
     */
    @SafeVarargs
    public final Condition<T, R> group(SFunction<T, ?> groupSFunction, Consumer<GroupCondition<T, R>>... consumers) {
        Object value = groupSFunction.apply(this.form.form);
        boolean isValid = ValidTool.isNotEmpty(value);
        // 是否为空仅影响分组条件 不影响校检是否成功
        this.isValid = true;
        this.validateResult = ValidateResult.builder().isValid(true).message("校检成功").build();
        // 不为空则验证分组规则
        if (isValid) {
            this.formField.groupConditions = Arrays.asList(consumers);
        }
        return this;
    }

    /**
     * 分组验证
     *
     * @param groupConsumer 分组验证多个字段 全部字段不为空才验证分组里面的规则，为空则忽略分组里面规则
     * @param consumers     分组校检规则
     * @return 链式调用返回自身
     * @since 1.1
     */
    @SafeVarargs
    public final Condition<T, R> group(Consumer<GroupGetterMultiple<T, R>> groupConsumer, Consumer<GroupCondition<T, R>>... consumers) {
        GroupGetterMultiple<T, R> groupGetterMultiple = new GroupGetterMultiple<>(this.formField);
        groupConsumer.accept(groupGetterMultiple);
        // 是否为空仅影响分组条件 不影响校检是否成功
        this.isValid = true;
        this.validateResult = ValidateResult.builder().isValid(true).message("校检成功").build();
        // 不为空则验证分组规则
        if (groupGetterMultiple.isValid) {
            this.formField.groupConditions = Arrays.asList(consumers);
        }
        return this;
    }

    private ValidateResult getValidateResult(boolean isValid, String propertyName) {
        return getValidateResult(isValid, propertyName, null);
    }

    private ValidateResult getValidateResult(boolean isValid, String propertyName, String msg) {
        if (isValid) {
            return ValidateResult.builder().isValid(true).message("校检成功").build();
        } else {
            return ValidateResult.builder().isValid(false).message(nullReplace(msg, "字段: " + propertyName + "校检失败")).build();
        }
    }
}
