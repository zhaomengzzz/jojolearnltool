package fun.ltool.core.reflect.chain;


import fun.ltool.core.lang.fun.MethodFunction;
import fun.ltool.core.reflect.handler.ReflectClassHandler;

import java.util.function.Supplier;

/**
 * 链式调用类字段执行器
 *
 * @param <C> 类泛型
 * @author huangrongsong
 * @since 1.2
 */
public class FieldClassChain<C> {
    /**
     * 传入类实例的反射处理类
     */
    private final ReflectClassHandler<C> classHandler;

    public FieldClassChain(ReflectClassHandler<C> classHandler) {
        this.classHandler = classHandler;
    }

    /**
     * 字段赋值
     *
     * @param instance       设置值的类实例
     * @param fieldName      字段名
     * @param fieldSFunction 赋值函数引用
     * @param <V>            值泛型
     * @return 链式调用返回自身
     */
    public <V> FieldClassChain<C> set(C instance, String fieldName, Supplier<V> fieldSFunction) {
        V v = fieldSFunction.get();
        classHandler.setFieldValue(instance, fieldName, v);
        return this;
    }

    /**
     * 字段赋值
     *
     * @param instance  设置值的类实例
     * @param fieldName 字段名
     * @param value     值
     * @param <V>       值泛型
     * @return 链式调用返回自身
     */
    public <V> FieldClassChain<C> set(C instance, String fieldName, V value) {
        classHandler.setFieldValue(instance, fieldName, value);
        return this;
    }

    /**
     * 获取字段的值
     *
     * @param fieldName            字段名称
     * @param methodResultFunction 返回值回调
     * @param <R>                  返回值泛型
     * @return 链式调用返回自身
     */
    @SuppressWarnings("unchecked")
    public <R> FieldClassChain<C> get(C instance, String fieldName, MethodFunction<R> methodResultFunction) {
        methodResultFunction.results((R) classHandler.getFieldValue(instance, fieldName));
        return this;
    }
}
