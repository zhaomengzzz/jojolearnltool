package fun.ltool.core.lang.reflect;

import lombok.Getter;

/**
 * null值包装 用于处理反射 获取参数类型问题
 *
 * @param <T> 参数泛型
 * @author huangrongsong
 * @since 1.2
 */
public class FieldWrapper<T> {
    /**
     * 字段类型
     */
    @Getter
    private Class<T> fieldTypeClass;

    public FieldWrapper(Class<T> fieldTypeClass) {
        this.fieldTypeClass = fieldTypeClass;
    }

    public static <T> FieldWrapper<T> valueNull(Class<T> fieldTypeClass) {
        return new FieldWrapper<>(fieldTypeClass);
    }

}
