package fun.ltool.core.validator.form;

import fun.ltool.core.lang.fun.SFunction;
import fun.ltool.core.validator.form.utils.ValidTool;

import java.util.function.Consumer;

/**
 * 表单校检 分组条件构造器
 *
 * @param <T> 表单实体泛型
 * @author huangrongsong
 * @since 1.1
 */
public class GroupGetterMultiple<T, R> {
    /**
     * 表单项 操作类
     */
    private final ValidatorForm.Form.FormField<T, R> formField;
    /**
     * 字段校检结果
     */
    protected Boolean isValid = true;

    public GroupGetterMultiple(ValidatorForm.Form.FormField<T, R> formField) {
        this.formField = formField;
    }

    /**
     * 分组项生效条件
     *
     * @param sFunctions 属性Getter方法引用
     */
    @SafeVarargs
    public final void property(SFunction<T, ?>... sFunctions) {
        for (SFunction<T, ?> sFunction : sFunctions) {
            if (sFunction != null) {
                Object value = sFunction.apply(this.formField.form.form);
                boolean isValid = ValidTool.isNotEmpty(value);
                if (!isValid) {
                    this.isValid = false;
                    return;
                }
            }
        }
    }

    /**
     * 表单验证规则
     *
     * @param consumers 多个验证规则
     */
    @SafeVarargs
    public final void condition(Consumer<Condition<T, R>>... consumers) {
        for (Consumer<Condition<T, R>> consumer : consumers) {
            Condition<T, R> rtCondition = new Condition<>(this.formField);
            consumer.accept(rtCondition);
            if (!rtCondition.isValid) {
                this.isValid = false;
                return;
            }
        }
    }
}
