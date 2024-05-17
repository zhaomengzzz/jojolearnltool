package fun.ltool.core.bean;

import fun.ltool.core.bean.condition.MapCondition;

import java.lang.reflect.Method;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;
import java.util.stream.Collectors;

/**
 * Bean复制到Map具体实现类
 *
 * @param <T> 目标对象泛型
 * @param <S> 源对象泛型
 * @author huangrongsong
 * @since 1.1
 */
public class BeanToMapReplicator<T extends Map<String, Object>, S> extends AbstractReplicator<T, S> {
    /**
     * JavaBean 操作对象条件实例
     */
    public List<Consumer<MapCondition<T, S>>> conditions;

    public BeanToMapReplicator(T target, S source) {
        super(target, source);
    }

    @Override
    public T copy() {
        // 过滤掉 condition 包含的 key
        Map<String, Map<String, Method>> map = this.sourceGetterAndSetterMethod.entrySet().stream()
                .filter(entry -> this.conditionProps.entrySet().stream()
                        .noneMatch(f -> f.getValue().getPropertyName().equals(entry.getKey())))
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
        // 遍历源对象方法
        map.forEach((fieldName, methodMap) -> {
            Method getter = methodMap.get(PropTool.GETTER);
            // 取值
            Object value = PropTool.getValue(source, getter);
            if (value != null) {
                // 赋值
                target.put(fieldName, value);
            }
        });
        // 处理条件
        this.conditionProps.forEach((k, v) -> target.put(k, v.getValue()));
        return target;
    }


}
