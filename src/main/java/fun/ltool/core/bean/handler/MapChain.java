package fun.ltool.core.bean.handler;

import fun.ltool.core.bean.BeanToMapReplicator;
import fun.ltool.core.bean.condition.MapCondition;

import java.util.Arrays;
import java.util.Map;
import java.util.function.Consumer;

/**
 * 链式处理 JavaBean转Map 类
 *
 * @param <T> 目标对象泛型
 * @param <S> 源对象泛型
 * @author huangrongsong
 * @since 1.1
 */
public class MapChain<T extends Map<String, Object>, S> {
    private final BeanToMapReplicator<T, S> beanToMapReplicator;


    public MapChain(T target, S source) {
        this.beanToMapReplicator = new BeanToMapReplicator<>(target, source);
    }

    public static <T extends Map<String, Object>, S> MapChain<T, S> create(T target, S source) {
        return new MapChain<>(target, source);
    }

    /**
     * 条件构造器
     *
     * @param consumers 条件集合
     * @return 链式调用返回自身
     */
    @SafeVarargs
    public final MapChain<T, S> condition(Consumer<MapCondition<T, S>>... consumers) {
        this.beanToMapReplicator.conditions = Arrays.asList(consumers);
        return this;
    }

    /**
     * 执行处理返回结果
     *
     * @return 目标对象 返回对象和target对应
     */
    public T build() {
        if (this.beanToMapReplicator.conditions != null) {
            for (Consumer<MapCondition<T, S>> condition : this.beanToMapReplicator.conditions) {
                condition.accept(new MapCondition<>(beanToMapReplicator));
            }
        }
        return this.beanToMapReplicator.copy();
    }
}
