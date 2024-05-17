package fun.ltool.core.bean;

import fun.ltool.core.bean.handler.BeanChain;
import fun.ltool.core.bean.handler.MapChain;
import fun.ltool.core.exceptions.BeanException;

import java.util.HashMap;
import java.util.Map;

/**
 * JavaBean 工具类
 *
 * @author huangrongsong
 * @since 1.1
 */
public class BeanUtil {
    /**
     * 构建加载 JavaBean
     *
     * @param target 目标对象
     * @param source 源对象
     * @param <T>    目标对象泛型
     * @param <S>    源对象泛型
     * @return BeanChain JavaBean链式处理实例
     */
    public static <T, S> BeanChain<T, S> buildBean(T target, S source) {
        return BeanChain.create(target, source);
    }

    /**
     * 构建加载 JavaBean
     *
     * @param targetClazz 目标对象类型 传类型会自动实例化对象
     * @param source      源对象
     * @param <T>         目标对象泛型
     * @param <S>         源对象泛型
     * @return BeanChain JavaBean链式处理实例
     */
    public static <T, S> BeanChain<T, S> buildBean(Class<T> targetClazz, S source) {
        try {
            return BeanChain.create(targetClazz.newInstance(), source);
        } catch (InstantiationException | IllegalAccessException e) {
            throw new BeanException("实例化目标对象失败", e);
        }
    }

    public static <S> MapChain<Map<String, Object>, S> buildMap(S source) {
        return MapChain.create(new HashMap<>(), source);
    }
}
