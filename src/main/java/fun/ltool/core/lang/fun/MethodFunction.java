package fun.ltool.core.lang.fun;

/**
 * 方法执行获取返回值函数式接口
 *
 * @param <R> 方法返回值泛型
 * @author huangrongsong
 * @since 1.2
 */
@FunctionalInterface
public interface MethodFunction<R> {
    /**
     * 获取方法返回值
     *
     * @param result 方法返回值
     */
    void results(R result);
}
