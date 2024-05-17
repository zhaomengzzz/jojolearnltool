package fun.ltool.core.reflect.condition;

import fun.ltool.core.reflect.chain.FieldInstanceChain;
import fun.ltool.core.reflect.chain.MethodInstanceChain;
import fun.ltool.core.reflect.handler.ReflectInstanceHandler;

import java.util.function.Consumer;

/**
 * 链式调用类实例执行器
 *
 * @param <C> 处理类泛型
 * @author huangrongsong
 * @since 1.2
 */
public class ReflectInstanceCondition<C> {

    /**
     * 传入类实例的反射处理类
     */
    private final ReflectInstanceHandler<C> instanceHandler;

    public ReflectInstanceCondition(ReflectInstanceHandler<C> instanceHandler) {
        this.instanceHandler = instanceHandler;
    }

    /**
     * 字段 执行器
     *
     * @param consumers 字段 执行条件集合
     * @return 链式调用返回自身
     */
    @SafeVarargs
    public final ReflectInstanceCondition<C> fields(Consumer<FieldInstanceChain<C>>... consumers) {
        for (Consumer<FieldInstanceChain<C>> consumer : consumers) {
            consumer.accept(new FieldInstanceChain<C>(instanceHandler));
        }
        return this;
    }

    /**
     * 方法 执行器
     *
     * @param consumers 方法 执行条件集合
     * @return 链式调用返回自身
     */
    @SafeVarargs
    public final ReflectInstanceCondition<C> methods(Consumer<MethodInstanceChain<C>>... consumers) {
        for (Consumer<MethodInstanceChain<C>> consumer : consumers) {
            consumer.accept(new MethodInstanceChain<>(instanceHandler));
        }
        return this;
    }
}
