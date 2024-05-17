package fun.ltool.core.reflect.handler;

import fun.ltool.core.exceptions.ReflectException;
import fun.ltool.core.reflect.ClassTool;
import fun.ltool.core.reflect.MethodHandleUtil;
import fun.ltool.core.reflect.chain.ReflectClassChain;
import fun.ltool.core.reflect.condition.ReflectClassCondition;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.function.Consumer;

/**
 * 传入类类型的反射处理类
 *
 * @param <C> 类泛型
 * @author huangrongsong
 * @since 1.2
 */
public class ReflectClassHandler<C> extends ReflectHandler<C> {
    public ReflectClassHandler(Class<C> clazz) {
        super(clazz);
    }

    /**
     * 通过字段名取值
     *
     * @param instance  取值对象实例
     * @param fieldName 字段名
     * @return 返回值
     */
    public Object getFieldValue(C instance, String fieldName) {
        Field field = getField(fieldName);
        if (field != null) {
            try {
                return field.get(instance);
            } catch (IllegalAccessException e) {
                throw new ReflectException("反射取值异常", e);
            }
        }
        return null;
    }


    /**
     * 通过字段名取值
     *
     * @param instance       取值对象实例
     * @param fieldName      字段名
     * @param valueTypeClass 值类型
     * @param <T>            值泛型
     * @return 返回值
     */
    public <T> T getFieldValue(C instance, String fieldName, Class<T> valueTypeClass) {
        Object value = getFieldValue(instance, fieldName);
        if (value != null) {
            if (valueTypeClass.isInstance(value)) {
                return valueTypeClass.cast(value);
            }
        }
        return null;
    }

    /**
     * @param fieldName 字段名
     * @param value     值
     * @param <V>       值泛型
     */
    public <V> void setFieldValue(C instance, String fieldName, V value) {
        Field field = getField(fieldName);
        if (field == null) {
            throw new ReflectException("通过 " + fieldName + " 获取字段失败");
        }
        setAccessible(field);
        try {
            field.set(instance, value);
        } catch (IllegalAccessException e) {
            throw new ReflectException("反射赋值异常", e);
        }
    }

    /**
     * 执行方法
     *
     * @param instance 执行方法的对象实例
     * @param method   方法
     * @param args     方法参数
     * @param <R>      返回结果泛型
     * @return 返回方法执行结果
     */
    @SuppressWarnings("unchecked")
    public <R> R invoke(C instance, Method method, Object... args) {
        setAccessible(method);
        if (method == null) {
            throw new ReflectException("当前执行的方法为null");
        }
        // 判断是否是 默认方法
        if (method.isDefault()) {
            return MethodHandleUtil.invoke(ClassTool.isStatic(method) ? null : instance, method, args);
        } else {
            try {
                return (R) method.invoke(ClassTool.isStatic(method) ? null : instance, args);
            } catch (Throwable e) {
                throw new ReflectException("执行方法异常", e);
            }
        }
    }

    /**
     * 获取类执行器
     *
     * @param consumer 执行条件
     * @return 类执行器链式调用实例
     */
    public ReflectClassChain<C> executor(Consumer<ReflectClassCondition<C>> consumer) {
        return new ReflectClassChain<>(consumer, this);
    }

    /**
     * 实例化处理对象
     *
     * @return 实例的反射处理类
     */
    public ReflectInstanceHandler<C> newInstance(Object... args) {
        C instance = null;
        try {
            // 判断是否有入参
            if (args.length == 0) {
                instance = clazz.newInstance();
            } else {
                ClassTool.MethodParams methodParams = ClassTool.getParams(args);
                Constructor<C> constructor = getConstructor(methodParams.types);
                instance = constructor.newInstance(methodParams.values);
            }
        } catch (InstantiationException | IllegalAccessException | InvocationTargetException e) {
            throw new ReflectException(clazz + " 实例化对象失败", e);
        }
        return new ReflectInstanceHandler<>(instance);
    }

}
