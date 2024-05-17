package fun.ltool.core.bean;

import fun.ltool.core.bean.bean.SourcePropBean;
import fun.ltool.core.lang.replicator.Replicator;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

/**
 * 抽象的对象复制类
 *
 * @param <T> 目标对象泛型
 * @param <S> 源对象泛型
 * @author huangrongsong
 * @since 1.1
 */
public abstract class AbstractReplicator<T, S> implements Replicator<T> {

    /**
     * 目标对象
     */
    public final T target;
    /**
     * 源对象
     */
    public final S source;
    /**
     * 目标对象prop 和 get set方法
     */
    public Map<String, Map<String, Method>> targetGetterAndSetterMethod;
    /**
     * 源对象prop 和 get set方法
     */
    public Map<String, Map<String, Method>> sourceGetterAndSetterMethod;

    /**
     * 条件map key对应prop, value对应prop的值
     */
    public Map<String, SourcePropBean> conditionProps;

    public AbstractReplicator(final T target, final S source) {
        this.target = target;
        this.source = source;
        // 获取map   key -> prop, value -> (map getter setter）
        this.targetGetterAndSetterMethod = PropTool.getGetterAndSetterMethod(target.getClass());
        this.sourceGetterAndSetterMethod = PropTool.getGetterAndSetterMethod(source.getClass());
        // 初始化map
        if (conditionProps == null) {
            conditionProps = new HashMap<>();
        }
    }
}
