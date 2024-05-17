package fun.ltool.core.reflect.chain;


import fun.ltool.core.lang.fun.MethodFunction;
import fun.ltool.core.lang.reflect.FieldWrapper;
import fun.ltool.core.reflect.ClassTool;
import fun.ltool.core.reflect.handler.ReflectClassHandler;

import java.lang.reflect.Method;
import java.util.function.Supplier;

/**
 * 链式调用类方法执行器
 *
 * @param <C> 类泛型
 * @author huangrongsong
 * @since 1.2
 */
public class MethodClassChain<C> {
    /**
     * 传入类实例的反射处理类
     */
    private final ReflectClassHandler<C> classHandler;

    public MethodClassChain(ReflectClassHandler<C> classHandler) {
        this.classHandler = classHandler;
    }

    /**
     * 执行静态方法
     *
     * @param methodName           方法名
     * @param methodResultFunction 方法执行结果回调
     * @param methodArgs           方法参数值函数引用
     *                             <p>
     *                             如果参数需要设置null 请调用<p> {@link FieldWrapper#valueNull(Class)} 方法传入参数类型 <p>例：{@code () -> FieldWrapper.valueNull(String.class)}
     *                             </p>
     * @return 链式调用返回自身
     */
    public final <R> MethodClassChain<C> staticMethod(String methodName, MethodFunction<R> methodResultFunction, Supplier<?>... methodArgs) {
        return method(null, methodName, methodResultFunction, methodArgs);
    }

    /**
     * 执行方法
     *
     * @param instance             需要执行方法的对象实例
     * @param methodName           方法名
     * @param methodResultFunction 方法执行结果回调
     * @param <R>                  结果返回值泛型
     * @param methodArgs           方法参数值函数引用
     *                             <p>
     *                             如果参数需要设置null 请调用<p> {@link FieldWrapper#valueNull(Class)} 方法传入参数类型 <p>例：{@code () -> FieldWrapper.valueNull(String.class)}
     *                             </p>
     * @return 链式调用返回自身
     */
    public final <R> MethodClassChain<C> method(C instance, String methodName, MethodFunction<R> methodResultFunction, Supplier<?>... methodArgs) {
        ClassTool.MethodParams params = ClassTool.getParams(methodArgs);
        Method method = classHandler.getMethod(methodName, params.types);
        R invoke = classHandler.invoke(instance, method, params.values);
        // 获取方法返回值
        methodResultFunction.results(invoke);
        return this;
    }

    /**
     * 执行方法
     *
     * @param instance             需要执行方法的对象实例
     * @param methodName           方法名
     * @param methodResultFunction 方法执行结果回调
     * @param <R>                  结果返回值泛型
     * @param args                 方法参数值
     *                             <p>
     *                             如果参数需要设置null 请调用<p> {@link FieldWrapper#valueNull(Class)} 方法传入参数类型 <p>例：{@code () -> FieldWrapper.valueNull(String.class)}
     *                             </p>
     * @return 链式调用返回自身
     */
    public final <R> MethodClassChain<C> method(C instance, String methodName, MethodFunction<R> methodResultFunction, Object... args) {
        ClassTool.MethodParams params = ClassTool.getParams(args);
        Method method = classHandler.getMethod(methodName, params.types);
        R invoke = classHandler.invoke(instance, method, params.values);
        // 获取方法返回值
        methodResultFunction.results(invoke);
        return this;
    }

}
