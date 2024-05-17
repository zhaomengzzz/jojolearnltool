package fun.ltool.core.bean.condition;

import fun.ltool.core.bean.BeanToBeanReplicator;
import fun.ltool.core.bean.bean.SourcePropBean;
import fun.ltool.core.exceptions.BeanException;
import fun.ltool.core.lang.fun.SFunction;

import java.util.Map;
import java.util.function.Consumer;

/**
 * JavaBean操作 Bean复制到Bean条件构造器
 *
 * @param <T> 目标对象泛型
 * @param <S> 源对象泛型
 * @author huangrongsong
 * @since 1.1
 */
public class BeanCondition<T, S> {

    private final BeanToBeanReplicator<T, S> beanToBeanReplicator;

    public BeanCondition(BeanToBeanReplicator<T, S> beanToBeanReplicator) {
        this.beanToBeanReplicator = beanToBeanReplicator;
    }

    /**
     * 批量替换bean 属性映射关系
     * <p>bean目标对象和源对象属性名无法匹配时可使用此方法</p>
     *
     * @param replaceConditionConsumer 替换属性映射构造器
     * @return 链式调用返回自身
     */
    public BeanCondition<T, S> replaceAll(Consumer<ReplaceBeanCondition<T, S>> replaceConditionConsumer) {
        if (replaceConditionConsumer == null) {
            throw new BeanException("replaceAll方法未构建条件", new NullPointerException());
        }
        ReplaceBeanCondition<T, S> condition = new ReplaceBeanCondition<>(this.beanToBeanReplicator);
        replaceConditionConsumer.accept(condition);
        Map<String, SourcePropBean> replaceCondition = condition.getReplaceCondition();
        this.beanToBeanReplicator.conditionProps.putAll(replaceCondition);
        condition.clearCondition();
        return this;
    }

    /**
     * 替换bean 属性映射关系
     * <p>bean目标对象和源对象属性名无法匹配时可使用此方法</p>
     *
     * @param targetFunction 目标属性 getter方法引用
     * @param sourceFunction 源属性 getter方法引用
     * @return 链式调用返回自身
     */
    public BeanCondition<T, S> replace(SFunction<T, ?> targetFunction, SFunction<S, ?> sourceFunction) {
        targetFunction.apply(this.beanToBeanReplicator.target);
        String targetPropName = targetFunction.methodToProperty();
        String sourcePropName = sourceFunction.methodToProperty();
        Object value = sourceFunction.apply(this.beanToBeanReplicator.source);
        this.beanToBeanReplicator.conditionProps.put(targetPropName, new SourcePropBean(sourcePropName, value));
        return this;
    }
}
