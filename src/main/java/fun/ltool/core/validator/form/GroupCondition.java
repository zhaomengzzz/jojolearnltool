package fun.ltool.core.validator.form;

import fun.ltool.core.validator.form.bean.ValidateResult;

import java.util.function.Consumer;

/**
 * 表单校检 分组条件构造器
 *
 * @param <T> 表单实体泛型
 * @author huangrongsong
 * @since 1.1
 */
public class GroupCondition<T, R> {
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

    public GroupCondition(ValidatorForm.Form.FormField<T, R> formField) {
        this.formField = formField;
    }

    /**
     * 分组项条件规则
     *
     * @param consumers 条件集合
     * @return 链式调用返回自身
     */
    @SafeVarargs
    public final GroupCondition<T, R> condition(Consumer<Condition<T, R>>... consumers) {
        for (Consumer<Condition<T, R>> consumer : consumers) {
            Condition<T, R> condition = Condition.create(this.formField);
            consumer.accept(condition);
            this.isValid = condition.isValid;
            this.validateResult = condition.validateResult;
            if (!this.isValid) {
                return this;
            }
        }
        return this;
    }
}
