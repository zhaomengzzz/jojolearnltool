package fun.ltool.core.reflect.chain;

import fun.ltool.core.reflect.condition.ReflectInstanceCondition;
import fun.ltool.core.reflect.handler.ReflectInstanceHandler;

import java.util.List;
import java.util.function.Consumer;

/**
 * 链式调用类实例处理器
 *
 * @param <C> 处理类泛型
 * @author huangrongsong
 * @since 1.2
 */
public class ReflectInstanceChain<C> {

    /**
     * 执行条件
     */
    private final Consumer<ReflectInstanceCondition<C>> consumer;
    /**
     * 传入类实例的反射处理类
     */
    private final ReflectInstanceHandler<C> instanceHandler;

    public ReflectInstanceChain(Consumer<ReflectInstanceCondition<C>> consumer, ReflectInstanceHandler<C> instanceHandler) {
        this.consumer = consumer;
        this.instanceHandler = instanceHandler;
    }


    /**
     * 构建处理 执行器
     */
    public void build() {
        if (this.consumer != null) {
            consumer.accept(new ReflectInstanceCondition<>(instanceHandler));
        }
    }
}
