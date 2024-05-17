package fun.ltool.core.bean.condition;

import fun.ltool.core.bean.BeanToMapReplicator;
import fun.ltool.core.bean.bean.SourcePropBean;
import fun.ltool.core.lang.fun.SFunction;

import java.util.Arrays;
import java.util.Map;

/**
 * Bean复制到Map替换自定义属性条件构造器
 *
 * @param <T> 目标对象泛型
 * @param <S> 源对象泛型
 * @author huangrongsong
 * @since 1.1
 */
public class ReplaceMapCondition<T extends Map<String, Object>, S> extends ReplaceCondition {
    private final BeanToMapReplicator<T, S> beanToMapReplicator;

    public ReplaceMapCondition(BeanToMapReplicator<T, S> beanToMapReplicator) {
        this.beanToMapReplicator = beanToMapReplicator;
    }

    /**
     * 目标对象赋值属性
     *
     * @param targetProps 目标对象属性名
     *                    <p>注：targetProp 和 sourceProp 属性一一对应顺序 和数量</p>
     * @return 链式调用返回自身
     */
    public final ReplaceMapCondition<T, S> targetProp(String... targetProps) {
        CONDITION_REPLACE_ALL_PROPERTY_NAMES.addAll(Arrays.asList(targetProps));
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
    public final ReplaceMapCondition<T, S> sourceProp(SFunction<S, ?>... sourceSFunctions) {
        for (SFunction<S, ?> sourceSFunction : sourceSFunctions) {
            String property = sourceSFunction.methodToProperty();
            Object value = sourceSFunction.apply(this.beanToMapReplicator.source);
            CONDITION_REPLACE_ALL_VALUES.add(new SourcePropBean(property, value));
        }
        return this;
    }

}
