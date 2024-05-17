package fun.ltool.core.bean.condition;

import fun.ltool.core.bean.BeanToBeanReplicator;
import fun.ltool.core.bean.bean.SourcePropBean;
import fun.ltool.core.lang.fun.SFunction;

/**
 * Bean复制到Bean替换自定义属性条件构造器
 *
 * @param <T> 目标对象泛型
 * @param <S> 源对象泛型
 * @author huangrongsong
 * @since 1.1
 */
public class ReplaceBeanCondition<T, S> extends ReplaceCondition {

    private final BeanToBeanReplicator<T, S> beanToBeanReplicator;

    public ReplaceBeanCondition(BeanToBeanReplicator<T, S> beanToBeanReplicator) {
        this.beanToBeanReplicator = beanToBeanReplicator;
    }

    /**
     * 目标对象赋值属性
     *
     * @param targetSFunctions 目标对象属性getter方法引用
     *                         <p>注：targetProp 和 sourceProp 属性一一对应顺序 和数量</p>
     * @return 链式调用返回自身
     */
    @SafeVarargs
    public final ReplaceBeanCondition<T, S> targetProp(SFunction<T, ?>... targetSFunctions) {
        for (SFunction<T, ?> targetSFunction : targetSFunctions) {
            String propertyName = targetSFunction.methodToProperty();
            CONDITION_REPLACE_ALL_PROPERTY_NAMES.add(propertyName);
        }
        return this;
    }

    /**
     * 源对象取值属性
     *
     * @param sourceSFunctions 源对象属性getter方法引用
     *                         <p>注：targetProp 和 sourceProp 属性一一对应顺序 和数量</p>
     * @return 链式调用返回自身
     */
    @SafeVarargs
    public final ReplaceBeanCondition<T, S> sourceProp(SFunction<S, ?>... sourceSFunctions) {
        for (SFunction<S, ?> sourceSFunction : sourceSFunctions) {
            String property = sourceSFunction.methodToProperty();
            Object value = sourceSFunction.apply(this.beanToBeanReplicator.source);
            CONDITION_REPLACE_ALL_VALUES.add(new SourcePropBean(property, value));
        }
        return this;
    }


}
