package fun.ltool.core.reflect;

import fun.ltool.core.reflect.handler.ReflectClassHandler;
import fun.ltool.core.reflect.handler.ReflectInstanceHandler;

/**
 * 反射工具类
 *
 * @author huangrongsong
 * @since 1.2
 */
public class ReflectUtil {

    /**
     * 构建加载类
     *
     * @param clazz 类
     * @param <C>   类泛型
     * @return 反射工具处理实例
     */
    public static <C> ReflectClassHandler<C> buildClass(Class<C> clazz) {
        return new ReflectClassHandler<>(clazz);
    }

    /**
     * 构建加载类
     *
     * @param Instance 类实例
     * @param <C>      类泛型
     * @return 反射工具处理实例
     */
    public static <C> ReflectInstanceHandler<C> buildClass(C Instance) {
        return new ReflectInstanceHandler<>(Instance);
    }
}
