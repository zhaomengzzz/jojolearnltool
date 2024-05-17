package fun.ltool.core.reflect;

import fun.ltool.core.exceptions.ReflectException;
import fun.ltool.core.lang.reflect.FieldWrapper;
import fun.ltool.core.map.ConcurrentMapCache;

import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.Arrays;
import java.util.function.Supplier;

/**
 * 处理类工具类
 *
 * @author huangrongsong
 * @since 1.2
 */
public class ClassTool {
    public static final ConcurrentMapCache<Class<?>, Class<?>> PRIMITIVE_TYPE_CLASS_MAP = new ConcurrentMapCache<>(8);

    // 初始化 包装类型和原始数据类型映射
    static {
        PRIMITIVE_TYPE_CLASS_MAP.put(Boolean.class, boolean.class);
        PRIMITIVE_TYPE_CLASS_MAP.put(Byte.class, byte.class);
        PRIMITIVE_TYPE_CLASS_MAP.put(Character.class, char.class);
        PRIMITIVE_TYPE_CLASS_MAP.put(Double.class, double.class);
        PRIMITIVE_TYPE_CLASS_MAP.put(Float.class, float.class);
        PRIMITIVE_TYPE_CLASS_MAP.put(Integer.class, int.class);
        PRIMITIVE_TYPE_CLASS_MAP.put(Long.class, long.class);
        PRIMITIVE_TYPE_CLASS_MAP.put(Short.class, short.class);
    }

    /**
     * 判断方法是否是静态方法
     *
     * @param method 方法
     * @return 返回结果
     */
    public static boolean isStatic(Method method) {
        if (method == null) {
            throw new ReflectException("method is null");
        }
        return Modifier.isStatic(method.getModifiers());
    }

    /**
     * 判断两个数组参数类型是否一致
     *
     * @param args1 数组参数1
     * @param args2 数组参数2
     * @return 返回判断结果
     */
    public static boolean isArgs(Class<?>[] args1, Class<?>[] args2) {
        if (args1 == null && args2 == null) {
            return true;
        }
        if (args1 == null || args2 == null) {
            return false;
        }
        if (Arrays.equals(args1, args2)) {
            return true;
        }
        if (args1.length != args2.length) {
            return false;
        }
        for (int i = 0; i < args1.length; i++) {
            Class<?> aClass1 = args1[i];
            Class<?> aClass2 = args2[i];
            // 处理包装类型和原始类型
            if (isPrimitiveType(aClass1) && isPrimitiveType(aClass2)) {
                if (getPrimitiveType(aClass1) != getPrimitiveType(aClass2)) {
                    return false;
                }
            } else if (!aClass1.isAssignableFrom(aClass2)) {
                return false;
            }
        }
        return true;
    }

    /**
     * 判断是否是基本数据类型
     *
     * @param clazz 类
     * @return 返回结果
     */
    public static boolean isPrimitiveType(Class<?> clazz) {
        if (clazz == null) {
            return false;
        }
        return (clazz.isPrimitive() || PRIMITIVE_TYPE_CLASS_MAP.get(clazz) != null);
    }

    /**
     * 获取原始数据类型
     *
     * @param typeClass 数据类型
     * @return 返回原始数据类型
     */
    public static Class<?> getPrimitiveType(Class<?> typeClass) {
        if (typeClass == null || typeClass.isPrimitive()) {
            return typeClass;
        }
        Class<?> aClass = PRIMITIVE_TYPE_CLASS_MAP.get(typeClass);
        if (aClass == null) {
            return typeClass;
        } else {
            return aClass;
        }
    }

    /**
     * 通过方法入参引用 获取方法入参参数值和类型
     *
     * @param methodArgs 方法入参引用
     * @return 返回
     * <p>{@link MethodParams#values}值
     * <p>{@link MethodParams#types}值类型
     */
    public static MethodParams getParams(Supplier<?>[] methodArgs) {
        Object[] values = new Object[methodArgs.length];
        Class<?>[] types = new Class[methodArgs.length];
        for (int i = 0; i < methodArgs.length; i++) {
            Supplier<?> methodArg = methodArgs[i];
            // 是否有方法入参
            if (methodArg != null) {
                Object v = methodArg.get();
                if (v == null) {
                    throw new ReflectException("方法参数null值设置有误请使用 FieldWrapper.valueNull() 方法设置null值", new RuntimeException());
                }
                // 判断是否是null null值包装类型解决参数为null无法映射方法入参类型问题
                if (v instanceof FieldWrapper) {
                    values[i] = null;
                    types[i] = ((FieldWrapper<?>) v).getFieldTypeClass();
                } else {
                    values[i] = v;
                    types[i] = v.getClass();
                }
            }
        }
        return new MethodParams(values, types);
    }

    /**
     * 通过方法入参引用 获取方法入参参数值和类型
     *
     * @param args 方法入参
     * @return 返回
     * <p>{@link MethodParams#values}值
     * <p>{@link MethodParams#types}值类型
     */
    public static MethodParams getParams(Object[] args) {
        Object[] values = new Object[args.length];
        Class<?>[] types = new Class[args.length];
        for (int i = 0; i < args.length; i++) {
            Object v = args[i];
            // 是否有方法入参
            if (v == null) {
                throw new ReflectException("方法参数null值设置有误请使用 FieldWrapper.valueNull() 方法设置null值", new RuntimeException());
            }
            // 判断是否是null null值包装类型解决参数为null无法映射方法入参类型问题
            if (v instanceof FieldWrapper) {
                values[i] = null;
                types[i] = ((FieldWrapper<?>) v).getFieldTypeClass();
            } else {
                values[i] = v;
                types[i] = v.getClass();
            }
        }
        return new MethodParams(values, types);
    }

    /**
     * 方法参数对象
     */
    public static class MethodParams {
        /**
         * 值
         */
        public final Object[] values;
        /**
         * 值类型
         */
        public final Class<?>[] types;

        public MethodParams(Object[] values, Class<?>[] types) {
            this.values = values;
            this.types = types;
        }
    }
}
