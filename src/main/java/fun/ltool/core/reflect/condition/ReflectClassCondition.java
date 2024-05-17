package fun.ltool.core.reflect.condition;

import fun.ltool.core.reflect.chain.FieldClassChain;
import fun.ltool.core.reflect.chain.MethodClassChain;
import fun.ltool.core.reflect.chain.MethodInstanceChain;
import fun.ltool.core.reflect.handler.ReflectClassHandler;

import java.util.function.Consumer;

/**
 * 链式调用类执行器
 *
 * @param <C> 处理类泛型
 * @author huangrongsong
 * @since 1.2
 */
public class ReflectClassCondition<C> {

    /**
     * 传入类实例的反射处理类
     */
    private final ReflectClassHandler<C> classHandler;

    public ReflectClassCondition(ReflectClassHandler<C> classHandler) {
        this.classHandler = classHandler;
    }

    /**
     * 字段 执行器
     *
     * @param consumers 字段 执行条件集合
     * @return 链式调用返回自身
     */
    @SafeVarargs
    public final ReflectClassCondition<C> fields(Consumer<FieldClassChain<C>>... consumers) {
        for (Consumer<FieldClassChain<C>> consumer : consumers) {
            consumer.accept(new FieldClassChain<>(classHandler));
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
    public final ReflectClassCondition<C> methods(Consumer<MethodClassChain<C>>... consumers) {
        for (Consumer<MethodClassChain<C>> consumer : consumers) {
            consumer.accept(new MethodClassChain<>(classHandler));
        }
        return this;
    }
}
