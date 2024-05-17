package fun.ltool.core.reflect;

import fun.ltool.core.exceptions.ReflectException;
import fun.ltool.core.lang.reflect.LookupFactory;

import java.lang.invoke.MethodHandle;
import java.lang.invoke.MethodHandles;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;

/**
 * 方法句柄 工具类
 *
 * @author huangrongsong
 * @since 1.2
 */
public class MethodHandleUtil {
    @SuppressWarnings("unchecked")
    public static <R> R invoke(Object obj, Method method, Object... args) {
        Class<?> declaringClass = method.getDeclaringClass();
        MethodHandles.Lookup lookup = LookupFactory.lookup(declaringClass);
        try {
            MethodHandle methodHandle;
            // 判断是否是 默认方法或者私有方法
            if (!method.isDefault()) {
                throw new ReflectException("Can't invoke default method");
            }
            if (Modifier.isPrivate(method.getModifiers())) {
                methodHandle = lookup.unreflectSpecial(method, declaringClass);
            } else {
                methodHandle = lookup.unreflect(method);
            }
            // 判断是否是静态方法
            if (!Modifier.isStatic(method.getModifiers())) {
                methodHandle = methodHandle.bindTo(obj);
            }
            // 是否有方法入参
            if (args[0] == null) {
                return (R) methodHandle.invoke();
            } else {
                return (R) methodHandle.invokeWithArguments(args);
            }
        } catch (Throwable e) {
            throw new ReflectException("执行方法异常", e);
        }
    }
}
