package fun.ltool.core.bean.condition;

import fun.ltool.core.bean.BeanToMapReplicator;
import fun.ltool.core.bean.bean.SourcePropBean;
import fun.ltool.core.exceptions.BeanException;
import fun.ltool.core.lang.fun.SFunction;

import java.util.Map;
import java.util.function.Consumer;

/**
 * JavaBean操作 Bean复制到Map条件构造器
 *
 * @param <T> 目标对象泛型
 * @param <S> 源对象泛型
 * @author huangrongsong
 * @since 1.1
 */
public class MapCondition<T extends Map<String, Object>, S> {

    private final BeanToMapReplicator<T, S> beanToMapReplicator;

    public MapCondition(BeanToMapReplicator<T, S> beanToMapReplicator) {
        this.beanToMapReplicator = beanToMapReplicator;
    }

    /**
     * 批量替换bean 属性映射关系
     * <p>bean目标对象和源对象属性名无法匹配时可使用此方法</p>
     *
     * @param replaceConditionConsumer 替换属性映射构造器
     * @return 链式调用返回自身
     */
    public MapCondition<T, S> replaceAll(Consumer<ReplaceMapCondition<T, S>> replaceConditionConsumer) {
        if (replaceConditionConsumer == null) {
            throw new BeanException("replaceAll方法未构建条件", new NullPointerException());
        }
        ReplaceMapCondition<T, S> condition = new ReplaceMapCondition<>(this.beanToMapReplicator);
        replaceConditionConsumer.accept(condition);
        Map<String, SourcePropBean> replaceCondition = condition.getReplaceCondition();
        this.beanToMapReplicator.conditionProps.putAll(replaceCondition);
        condition.clearCondition();
        return this;
    }

    /**
     * 替换bean 属性映射关系
     * <p>bean目标对象和源对象属性名无法匹配时可使用此方法</p>
     *
     * @param targetProp     目标属性
     * @param sourceFunction 源属性 getter方法引用
     * @return 链式调用返回自身
     */
    public MapCondition<T, S> replace(String targetProp, SFunction<S, ?> sourceFunction) {
        String sourcePropName = sourceFunction.methodToProperty();
        Object value = sourceFunction.apply(this.beanToMapReplicator.source);
        this.beanToMapReplicator.conditionProps.put(targetProp, new SourcePropBean(sourcePropName, value));
        return this;
    }
}
