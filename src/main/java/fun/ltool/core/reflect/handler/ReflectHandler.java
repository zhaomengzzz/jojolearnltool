package fun.ltool.core.reflect.handler;

import fun.ltool.core.exceptions.ReflectException;
import fun.ltool.core.map.WeakConcurrentHashMapCache;
import fun.ltool.core.reflect.ClassTool;
import fun.ltool.core.reflect.MethodHandleUtil;
import lombok.Getter;

import java.lang.reflect.AccessibleObject;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Objects;

/**
 * 反射处理类
 *
 * @param <C> 类泛型
 * @author huangrongsong
 * @since 1.2
 */
public class ReflectHandler<C> {
    /**
     * 字段缓存
     */
    private static final WeakConcurrentHashMapCache<Class<?>, Field[]> FIELD_CACHE = new WeakConcurrentHashMapCache<>();
    /**
     * 方法缓存
     */
    private static final WeakConcurrentHashMapCache<Class<?>, Method[]> METHOD_CACHE = new WeakConcurrentHashMapCache<>();
    /**
     * 构造方法缓存
     */
    private static final WeakConcurrentHashMapCache<Class<?>, Constructor<?>[]> CONSTRUCTOR_CACHE = new WeakConcurrentHashMapCache<>();

    /**
     * 需要处理的类实例
     * -- GETTER --
     * 获取处理类实例
     *
     * @return 返回实例
     */
    @Getter
    protected C classInstance;

    /**
     * 需要处理的类类型
     */
    protected final Class<C> clazz;

    public ReflectHandler(Class<C> clazz) {
        this.clazz = clazz;
        if (!FIELD_CACHE.containsKey(clazz)) {
            FIELD_CACHE.put(clazz, clazz.getDeclaredFields());
        }
        if (!METHOD_CACHE.containsKey(clazz)) {
            METHOD_CACHE.put(clazz, clazz.getDeclaredMethods());
        }
        if (!CONSTRUCTOR_CACHE.containsKey(clazz)) {
            CONSTRUCTOR_CACHE.put(clazz, clazz.getDeclaredConstructors());
        }
    }

    @SuppressWarnings("unchecked")
    public ReflectHandler(C classInstance) {
        this.classInstance = classInstance;
        Class<?> clazz = classInstance.getClass();
        this.clazz = (Class<C>) clazz;
        if (!FIELD_CACHE.containsKey(clazz)) {
            FIELD_CACHE.put(clazz, clazz.getDeclaredFields());
        }
        if (!METHOD_CACHE.containsKey(clazz)) {
            METHOD_CACHE.put(clazz, clazz.getDeclaredMethods());
        }
        if (!CONSTRUCTOR_CACHE.containsKey(clazz)) {
            CONSTRUCTOR_CACHE.put(clazz, clazz.getDeclaredConstructors());
        }
    }

    /**
     * 获取构造方法
     *
     * @param parameterTypes 构造方法参数
     * @return 返回构造方法
     */
    public Constructor<C> getConstructor(Class<?>... parameterTypes) {
        if (this.clazz == null) {
            throw new ReflectException("类类型为null");
        }
        Constructor<C>[] constructors = getConstructors();
        Class<?>[] parameterTypes2;
        for (Constructor<C> constructor : constructors) {
            parameterTypes2 = constructor.getParameterTypes();
            if (ClassTool.isArgs(parameterTypes, parameterTypes2)) {
                setAccessible(constructor);
                return constructor;
            }
        }
        return null;
    }

    /**
     * 获取所有构造方法
     *
     * @return 返回构造方法数组
     */
    @SuppressWarnings("unchecked")
    public Constructor<C>[] getConstructors() {
        if (this.clazz == null) {
            throw new ReflectException("类类型为null");
        }
        return (Constructor<C>[]) CONSTRUCTOR_CACHE.get(this.clazz);
    }

    /**
     * 是否存在对应参数的构造方法
     *
     * @param parameterTypes 参数
     * @return 返回判断结果
     */
    public boolean hasConstructor(Class<?>... parameterTypes) {
        return getConstructor(parameterTypes) != null;
    }

    /**
     * 是否存在字段
     *
     * @param name 字段名
     * @return 返回boolean
     */
    public boolean hasField(String name) {
        return getField(name) != null;
    }

    /**
     * 获取字段
     *
     * @param name 字段名
     * @return 返回字段
     */
    public Field getField(String name) {
        Field[] fields = FIELD_CACHE.get(this.clazz);
        return Arrays.stream(fields).filter(field -> {
            setAccessible(field);
            return Objects.equals(field.getName(), name);
        }).findFirst().orElse(null);
    }

    /**
     * 获取所有字段
     *
     * @return 返回字段
     */
    public Field[] getFields() {
        return FIELD_CACHE.get(this.clazz);
    }

    /**
     * 通过静态变量字段名取值
     *
     * @param fieldName 字段名
     * @return 返回值
     */
    public Object getStaticFieldValue(String fieldName) {
        Field field = getField(fieldName);
        if (field != null) {
            try {
                return field.get(null);
            } catch (IllegalAccessException e) {
                throw new ReflectException("反射取值异常", e);
            }
        }
        return null;
    }

    /**
     * 通过静态变量字段名取值
     *
     * @param fieldName      字段名
     * @param valueTypeClass 值类型
     * @param <T>            值泛型
     * @return 返回值
     */
    public <T> T getStaticFieldValue(String fieldName, Class<T> valueTypeClass) {
        Object value = getStaticFieldValue(fieldName);
        if (value != null) {
            if (valueTypeClass.isInstance(value)) {
                return valueTypeClass.cast(value);
            }
        }
        return null;
    }

    /**
     * 设置私有方法可访问
     *
     * @param accessibleObject 可访问对象
     * @param <T>              访问对象泛型
     * @return 返回可访问对象
     */
    public <T extends AccessibleObject> T setAccessible(T accessibleObject) {
        if (accessibleObject != null && !accessibleObject.isAccessible()) {
            accessibleObject.setAccessible(true);
        }
        return accessibleObject;
    }

    /**
     * 获取所有方法
     *
     * @return 返回方法数组
     */
    public Method[] getMethods() {
        return METHOD_CACHE.get(this.clazz);
    }

    /**
     * 通过方法名 和参数类型 获取方法
     *
     * @param name           方法名
     * @param parameterTypes 参数类型
     * @return 返回方法
     */
    public Method getMethod(String name, Class<?>... parameterTypes) {
        Method[] methods = getMethods();
        for (Method method : methods) {
            if (parameterTypes.length == 0) {
                if (Objects.equals(name, method.getName())) {
                    return method;
                }
            } else {
                if (Objects.equals(name, method.getName())
                        && ClassTool.isArgs(method.getParameterTypes(), parameterTypes)
                        // 排除协变桥接方法
                        && !method.isBridge()) {
                    return method;
                }
            }
        }
        return null;
    }

    /**
     * 是否存在方法
     *
     * @param name           方法名称
     * @param parameterTypes 方法入参
     * @return 返回判断结果
     */
    public boolean hasMethod(String name, Class<?>... parameterTypes) {
        return getMethod(name, parameterTypes) != null;
    }

    /**
     * 执行方法
     *
     * @param method 方法
     * @param args   方法参数
     * @param <R>    返回结果泛型
     * @return 返回方法执行结果
     */
    @SuppressWarnings("unchecked")
    public <R> R invoke(Method method, Object... args) {
        setAccessible(method);
        if (method == null) {
            throw new ReflectException("当前执行的方法为null");
        }
        // 判断是否是 默认方法
        if (method.isDefault()) {
            return MethodHandleUtil.invoke(ClassTool.isStatic(method) ? null : this.classInstance, method, args);
        } else {
            try {
                return (R) method.invoke(ClassTool.isStatic(method) ? null : this.classInstance, args);
            } catch (Throwable e) {
                throw new ReflectException("执行方法异常", e);
            }
        }
    }
}
