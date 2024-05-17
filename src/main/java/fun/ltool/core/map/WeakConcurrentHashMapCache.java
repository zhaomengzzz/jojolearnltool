package fun.ltool.core.map;

import java.lang.ref.WeakReference;

/**
 * 弱引用线程安全的Map缓存
 * <p>弱引用 能够自动回收不再被引用的缓存对象。</p>
 *
 * @param <K> 键泛型
 * @param <V> 值泛型
 * @author huangrongosong
 * @since 1.2
 */
public class WeakConcurrentHashMapCache<K, V> {
    private final ConcurrentMapCache<K, WeakReference<V>> cache = new ConcurrentMapCache<>();

    /**
     * 添加缓存对象到缓存中
     *
     * @param key   键
     * @param value 值
     */
    public void put(K key, V value) {
        cache.put(key, new WeakReference<>(value));
    }

    /**
     * 从缓存中获取缓存对象
     *
     * @param key 键
     * @return 获取值
     */
    public V get(K key) {
        WeakReference<V> ref = cache.get(key);
        return ref != null ? ref.get() : null;
    }

    /**
     * 检查键是否存在
     *
     * @param key 键
     * @return 返回boolean类型的结果
     */
    public boolean containsKey(K key) {
        return cache.containsKey(key);
    }

    /**
     * 从缓存中移除缓存对象
     *
     * @param key 键
     */
    public void remove(K key) {
        cache.remove(key);
    }

    /**
     * 清空缓存
     */
    public void clear() {
        cache.clear();
    }
}
