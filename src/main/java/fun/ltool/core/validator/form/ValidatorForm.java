package fun.ltool.core.validator.form;


import fun.ltool.core.lang.fun.SFunction;
import fun.ltool.core.validator.form.bean.ValidateResult;
import fun.ltool.core.validator.form.utils.ValidTool;

import java.util.*;
import java.util.function.Consumer;

/**
 * 表单验证器
 * <p>
 * 校检表单类 Java 8 lambda 链式调用风格
 * </p>
 *
 * @param <T> 表单实体
 * @author huangrongsong
 * @since 1.0
 */
public class ValidatorForm<T> {
    private final T form;

    public ValidatorForm(T form) {
        this.form = form;
    }

    /**
     * 获取表单校检对象
     *
     * @return 返回校检表单实例
     */
    public Form<T> getForm() {
        return new Form<>(form);
    }

    /**
     * 需要校检的表单对象
     *
     * @param form 表单
     * @param <T>  表单对象泛型
     * @return 返回校检表单实例
     */
    public static <T> Form<T> buildForm(T form) {
        return Form.create(form);
    }

    /**
     * 表单操作类
     *
     * @param <T> 表单实体
     */
    public static class Form<T> {

        /**
         * 表单对象
         */
        protected final T form;

        /**
         * 表单 操作类 构造方法
         *
         * @param form 表单实例
         */
        public Form(T form) {
            this.form = form;
        }

        /**
         * 创建表单操作类
         *
         * @param form 表单实体对象
         * @param <T>  表单实体
         * @return 返回操作类
         */
        public static <T> Form<T> create(T form) {
            return new Form<>(form);
        }

        /**
         * 校检表单字段
         *
         * @param <R> 表单字段值泛型
         * @return 返回校检表单字段实例
         */
        public final <R> FormField<T, R> field() {
            return field((SFunction<T, R>) null);
        }

        /**
         * 校检表单字段
         *
         * @param fieldFunctions 需要校检的表单字段 默认校检是否为空
         * @param <R>            表单字段值泛型
         * @return 返回表单字段校检实例
         */
        @SafeVarargs
        public final <R> FormField<T, R> field(SFunction<T, R>... fieldFunctions) {
            Map<String, R> forms = new HashMap<>();
            if (fieldFunctions != null) {
                for (SFunction<T, R> fieldFunction : fieldFunctions) {
                    if (fieldFunction != null) {
                        String propertyName = fieldFunction.methodToProperty();
                        forms.put(propertyName, fieldFunction.apply(form));
                    }
                }
            }
            return FormField.create(forms, this);
        }

        /**
         * 表单项 操作类
         *
         * @param <T> 表单实体
         * @param <R> 表单项的值
         */
        public static class FormField<T, R> {
            /**
             * 表单操作类实例
             */
            protected final Form<T> form;
            /**
             * 表单字段Map k表单字段 v表单值
             */
            protected final Map<String, R> forms;
            /**
             * 表单验证规则集合
             */
            protected List<Consumer<Condition<T, R>>> conditions;

            /**
             * 表单验证分组条件规则集合
             */
            protected List<Consumer<GroupCondition<T, R>>> groupConditions;

            /**
             * 创建表单项 操作类
             *
             * @param forms 表单字段Map k表单字段 v表单值
             * @param form  表单 操作类
             * @param <R>   表单项泛型
             * @param <T>   表单泛型
             * @return 表单项 操作类实例
             */
            public static <R, T> FormField<T, R> create(Map<String, R> forms, Form<T> form) {
                return new FormField<>(forms, form);
            }

            /**
             * 表单项 操作类 构造方法
             *
             * @param forms 表单字段Map k表单字段 v表单值
             * @param form  表单 操作类
             */
            public FormField(Map<String, R> forms, Form<T> form) {
                this.forms = forms;
                this.form = form;
                this.conditions = new ArrayList<>();
                this.groupConditions = new ArrayList<>();
            }

            /**
             * 表单验证规则
             *
             * @param consumers 多个验证规则
             * @return 链式调用返回本身
             */
            @SafeVarargs
            public final FormField<T, R> condition(Consumer<Condition<T, R>>... consumers) {
                conditions = Arrays.asList(consumers);
                return this;
            }


            /**
             * 表单验证
             *
             * @return 返回验证结果
             */
            public ValidateResult validate() {
                return validate(null);
            }

            /**
             * 表单验证
             *
             * @param msg 表单验证未通过时统一提示信息优先级最高会覆盖前面自定义信息
             * @return 返回验证结果
             */
            public ValidateResult validate(String msg) {
                // 字段校检
                for (Map.Entry<String, R> entry : forms.entrySet()) {
                    String key = entry.getKey();
                    R value = entry.getValue();
                    boolean isValid = ValidTool.isNotEmpty(value);
                    if (!isValid) {
                        return ValidateResult.builder().isValid(false).message(ValidTool.nullReplace(msg, "字段: " + key + "校检失败")).build();
                    }
                }
                // 条件规则校检
                for (Consumer<Condition<T, R>> condition : conditions) {
                    Condition<T, R> rtCondition = Condition.create(this);
                    condition.accept(rtCondition);
                    if (!rtCondition.isValid) {
                        ValidateResult validateResult = rtCondition.validateResult;
                        validateResult.setMessage(ValidTool.nullReplace(msg, validateResult.getMessage()));
                        return validateResult;
                    }
                }
                // 分组规则校检
                for (Consumer<GroupCondition<T, R>> groupCondition : groupConditions) {
                    GroupCondition<T, R> rtCondition = new GroupCondition<>(this);
                    groupCondition.accept(rtCondition);
                    if (!rtCondition.isValid) {
                        ValidateResult validateResult = rtCondition.validateResult;
                        validateResult.setMessage(ValidTool.nullReplace(msg, validateResult.getMessage()));
                        return validateResult;
                    }
                }
                return ValidateResult.builder().isValid(true).message("校检成功").build();
            }
        }
    }
}
