package fun.ltool.core.bean;

import fun.ltool.core.bean.condition.BeanCondition;

import java.lang.reflect.Method;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;
import java.util.stream.Collectors;

/**
 * Bean复制到Bean具体实现类
 *
 * @param <T> 目标对象泛型
 * @param <S> 源对象泛型
 * @author huangrongsong
 * @since 1.1
 */
public class BeanToBeanReplicator<T, S> extends AbstractReplicator<T, S> {
    /**
     * JavaBean 操作对象条件实例
     */
    public List<Consumer<BeanCondition<T, S>>> conditions;

    public BeanToBeanReplicator(T target, S source) {
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
            Map<String, Method> fieldMap = this.targetGetterAndSetterMethod.get(fieldName);
            if (fieldMap != null) {
                // 获取set方法
                Method setter = fieldMap.get(PropTool.SETTER);
                // 判断get和set不为null
                if (getter != null && setter != null) {
                    // 取值
                    Object value = PropTool.getValue(source, getter);
                    if (value != null) {
                        // 赋值
                        PropTool.setValue(target, setter, value);
                    }
                }
            }
        });
        // 处理条件
        this.conditionProps.forEach((fieldName, value) -> PropTool.setValue(target, fieldName, value.getValue()));
        return target;
    }


}
