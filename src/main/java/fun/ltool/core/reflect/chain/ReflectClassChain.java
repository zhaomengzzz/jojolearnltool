package fun.ltool.core.reflect.chain;

import fun.ltool.core.reflect.condition.ReflectClassCondition;
import fun.ltool.core.reflect.handler.ReflectClassHandler;

import java.util.function.Consumer;

/**
 * 链式调用类处理器
 *
 * @param <C> 处理类泛型
 * @author huangrongsong
 * @since 1.2
 */
public class ReflectClassChain<C> {

    /**
     * 执行条件
     */
    private final Consumer<ReflectClassCondition<C>> consumer;
    /**
     * 传入类实例的反射处理类
     */
    private final ReflectClassHandler<C> classHandler;

    public ReflectClassChain(Consumer<ReflectClassCondition<C>> consumer, ReflectClassHandler<C> classHandler) {
        this.consumer = consumer;
        this.classHandler = classHandler;
    }


    /**
     * 构建处理 执行器
     */
    public void build() {
        if (this.consumer != null) {
            consumer.accept(new ReflectClassCondition<>(classHandler));
        }
    }
}
