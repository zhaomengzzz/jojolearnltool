package fun.ltool.core.lang.replicator;

/**
 * 复制函数式接口
 *
 * @param <T> 目标对象泛型
 * @author huangrongsong
 * @since 1.1
 */
@FunctionalInterface
public interface Replicator<T> {
    /**
     * 执行复制
     *
     * @return 目标对象
     */
    T copy();
}
