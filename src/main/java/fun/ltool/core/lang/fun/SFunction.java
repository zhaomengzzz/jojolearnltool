package fun.ltool.core.lang.fun;


import java.io.Serializable;
import java.lang.invoke.SerializedLambda;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Locale;
import java.util.function.Function;

/**
 * 支持序列化的函数式接口
 *
 * @param <T> 入参泛型
 * @param <R> 出参泛型
 * @author huangrongsong
 * @since 1.0
 */
@FunctionalInterface
public interface SFunction<T, R> extends Function<T, R>, Serializable {

    /**
     * 获取lambda 表达式序列化对象
     *
     * @return 返回序列化对象
     */
    default SerializedLambda getSerializedLambda() {
        try {
            Method method = this.getClass().getDeclaredMethod("writeReplace");
            method.setAccessible(true);
            return (SerializedLambda) method.invoke(this);
        } catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 获取lambda 表达式 调用的方法名
     *
     * @return 返回方法名
     */
    default String getImplMethodName() {
        return getSerializedLambda().getImplMethodName();
    }

    /**
     * 获取属性名
     *
     * @return 返回属性名
     */
    default String methodToProperty() {
        String name = getImplMethodName();
        if (name.startsWith("is")) {
            name = name.substring(2);
        } else {
            if (!name.startsWith("get") && !name.startsWith("set")) {
                throw new RuntimeException("Error parsing property name '" + name + "'.  Didn't start with 'is', 'get' or 'set'.");
            }
            name = name.substring(3);
        }

        if (name.length() == 1 || name.length() > 1 && Character.isUpperCase(name.charAt(0))) {
            name = name.substring(0, 1).toLowerCase(Locale.ENGLISH) + name.substring(1);
        }
        return name;
    }
}
