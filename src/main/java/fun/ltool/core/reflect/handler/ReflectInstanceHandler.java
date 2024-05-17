package fun.ltool.core.reflect.handler;

import fun.ltool.core.exceptions.ReflectException;
import fun.ltool.core.reflect.chain.ReflectInstanceChain;
import fun.ltool.core.reflect.condition.ReflectInstanceCondition;

import java.lang.reflect.Field;
import java.util.function.Consumer;

/**
 * 传入类实例的反射处理类
 *
 * @param <C> 类泛型
 * @author huangrongsong
 * @since 1.2
 */
public class ReflectInstanceHandler<C> extends ReflectHandler<C> {
    public ReflectInstanceHandler(C classInstance) {
        super(classInstance);
    }

    /**
     * 通过字段名取值
     *
     * @param fieldName 字段名
     * @return 返回值
     */
    public Object getFieldValue(String fieldName) {
        Field field = getField(fieldName);
        if (field != null) {
            try {
                return field.get(this.classInstance);
            } catch (IllegalAccessException e) {
                throw new ReflectException("反射取值异常", e);
            }
        }
        return null;
    }


    /**
     * 通过字段名取值
     *
     * @param fieldName      字段名
     * @param valueTypeClass 值类型
     * @param <T>            值泛型
     * @return 返回值
     */
    public <T> T getFieldValue(String fieldName, Class<T> valueTypeClass) {
        Object value = getFieldValue(fieldName);
        if (value != null) {
            if (valueTypeClass.isInstance(value)) {
                return valueTypeClass.cast(value);
            }
        }
        return null;
    }

    /**
     * @param fieldName 字段名
     * @param value     值
     * @param <V>       值泛型
     */
    public <V> void setFieldValue(String fieldName, V value) {
        Field field = getField(fieldName);
        if (field == null) {
            throw new ReflectException("通过 " + fieldName + " 获取字段失败");
        }
        setAccessible(field);
        try {
            field.set(this.classInstance, value);
        } catch (IllegalAccessException e) {
            throw new ReflectException("反射赋值异常", e);
        }
    }

    /**
     * 获取类执行器
     *
     * @param consumer 执行条件
     * @return 类执行器链式调用实例
     */
    public ReflectInstanceChain<C> executor(Consumer<ReflectInstanceCondition<C>> consumer) {
        return new ReflectInstanceChain<>(consumer, this);
    }
}
