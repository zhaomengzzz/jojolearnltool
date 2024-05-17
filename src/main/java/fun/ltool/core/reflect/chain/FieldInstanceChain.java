package fun.ltool.core.reflect.chain;


import fun.ltool.core.lang.fun.MethodFunction;
import fun.ltool.core.reflect.handler.ReflectInstanceHandler;

import java.util.function.Supplier;

/**
 * 链式调用实例字段执行器
 *
 * @param <C> 类泛型
 * @author huangrongsong
 * @since 1.2
 */
public class FieldInstanceChain<C> {
    /**
     * 传入类实例的反射处理类
     */
    private final ReflectInstanceHandler<C> instanceHandler;

    public FieldInstanceChain(ReflectInstanceHandler<C> instanceHandler) {
        this.instanceHandler = instanceHandler;
    }

    /**
     * 字段赋值
     *
     * @param fieldName      字段名
     * @param fieldSFunction 赋值函数引用
     * @param <V>            值泛型
     * @return 链式调用返回自身
     */
    public <V> FieldInstanceChain<C> set(String fieldName, Supplier<V> fieldSFunction) {
        V v = fieldSFunction.get();
        instanceHandler.setFieldValue(fieldName, v);
        return this;
    }

    /**
     * 字段赋值
     *
     * @param fieldName 字段名
     * @param value     值
     * @param <V>       值泛型
     * @return 链式调用返回自身
     */
    public <V> FieldInstanceChain<C> set(String fieldName, V value) {
        instanceHandler.setFieldValue(fieldName, value);
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
    public <R> FieldInstanceChain<C> get(String fieldName, MethodFunction<R> methodResultFunction) {
        methodResultFunction.results((R) instanceHandler.getFieldValue(fieldName));
        return this;
    }

}
