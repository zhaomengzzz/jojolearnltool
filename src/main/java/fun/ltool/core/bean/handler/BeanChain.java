package fun.ltool.core.bean.handler;

import fun.ltool.core.bean.BeanToBeanReplicator;
import fun.ltool.core.bean.condition.BeanCondition;

import java.util.Arrays;
import java.util.function.Consumer;

/**
 * 链式处理 JavaBean 类
 *
 * @param <T> 目标对象泛型
 * @param <S> 源对象泛型
 * @author huangrongsong
 * @since 1.1
 */
public class BeanChain<T, S> {
    private final BeanToBeanReplicator<T, S> beanToBeanReplicator;


    public BeanChain(T target, S source) {
        this.beanToBeanReplicator = new BeanToBeanReplicator<>(target, source);
    }

    public static <T, S> BeanChain<T, S> create(T target, S source) {
        return new BeanChain<>(target, source);
    }

    /**
     * 条件构造器
     *
     * @param consumers 条件集合
     * @return 链式调用返回自身
     */
    @SafeVarargs
    public final BeanChain<T, S> condition(Consumer<BeanCondition<T, S>>... consumers) {
        this.beanToBeanReplicator.conditions = Arrays.asList(consumers);
        return this;
    }

    /**
     * 执行处理返回结果
     *
     * @return 目标对象 返回对象和target对应
     */
    public T build() {
        if (this.beanToBeanReplicator.conditions != null) {
            for (Consumer<BeanCondition<T, S>> condition : this.beanToBeanReplicator.conditions) {
                condition.accept(new BeanCondition<>(beanToBeanReplicator));
            }
        }
        return this.beanToBeanReplicator.copy();
    }
}
