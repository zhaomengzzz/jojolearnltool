package fun.ltool.core.reflect.chain;


import fun.ltool.core.lang.fun.MethodFunction;
import fun.ltool.core.lang.reflect.FieldWrapper;
import fun.ltool.core.reflect.ClassTool;
import fun.ltool.core.reflect.handler.ReflectInstanceHandler;

import java.lang.reflect.Method;
import java.util.function.Supplier;

/**
 * 链式调用实例方法执行器
 *
 * @param <C> 类泛型
 * @author huangrongsong
 * @since 1.2
 */
public class MethodInstanceChain<C> {
    /**
     * 传入类实例的反射处理类
     */
    private final ReflectInstanceHandler<C> instanceHandler;

    public MethodInstanceChain(ReflectInstanceHandler<C> instanceHandler) {
        this.instanceHandler = instanceHandler;
    }

    /**
     * 执行方法
     *
     * @param methodName           方法名
     * @param methodResultFunction 方法执行结果回调
     * @param <R>                  结果返回值泛型
     * @param args                 方法参数
     *                             <p>
     *                             如果参数需要设置null 请调用<p> {@link FieldWrapper#valueNull(Class)} 方法传入参数类型 <p>例：{@code () -> FieldWrapper.valueNull(String.class)}
     *                             </p>
     * @return 链式调用返回自身
     */
    public final <R> MethodInstanceChain<C> method(String methodName, MethodFunction<R> methodResultFunction, Object... args) {
        ClassTool.MethodParams params = ClassTool.getParams(args);
        Method method = params.types.length == 0 ? instanceHandler.getMethod(methodName) : instanceHandler.getMethod(methodName, params.types);
        R invoke = instanceHandler.invoke(method, params.values);
        // 获取方法返回值
        methodResultFunction.results(invoke);
        return this;
    }

    /**
     * 执行方法
     *
     * @param methodName           方法名
     * @param methodResultFunction 方法执行结果回调
     * @param <R>                  结果返回值泛型
     * @param methodArgs           方法参数值函数引用
     *                             <p>
     *                             如果参数需要设置null 请调用<p> {@link FieldWrapper#valueNull(Class)} 方法传入参数类型 <p>例：{@code () -> FieldWrapper.valueNull(String.class)}
     *                             </p>
     * @return 链式调用返回自身
     */
    public final <R> MethodInstanceChain<C> method(String methodName, MethodFunction<R> methodResultFunction, Supplier<?>... methodArgs) {
        ClassTool.MethodParams params = ClassTool.getParams(methodArgs);
        Method method = params.types.length == 0 ? instanceHandler.getMethod(methodName) : instanceHandler.getMethod(methodName, params.types);
        R invoke = instanceHandler.invoke(method, params.values);
        // 获取方法返回值
        methodResultFunction.results(invoke);
        return this;
    }

}
