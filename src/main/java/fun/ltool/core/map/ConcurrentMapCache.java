package fun.ltool.core.map;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 线程安全的Map缓存实现
 *
 * @param <K> key泛型
 * @param <V> value泛型
 * @author huangrongsong
 * @since 1.2
 */
public class ConcurrentMapCache<K, V> {
    private final Map<K, V> cache;

    /**
     * 构造方法
     */
    public ConcurrentMapCache() {
        // 初始化 ConcurrentHashMap 实例作为缓存
        cache = new ConcurrentHashMap<>();
    }

    /**
     * 构造方法
     *
     * @param initialCapacity 预估初始大小
     */
    public ConcurrentMapCache(int initialCapacity) {
        cache = new ConcurrentHashMap<>(initialCapacity);
    }

    /**
     * 线程安全的 put 方法
     *
     * @param key   键
     * @param value 值
     */
    public void put(K key, V value) {
        // 使用 ConcurrentHashMap 的 put 方法添加键值对，保证线程安全
        cache.put(key, value);
    }

    /**
     * 线程安全的 get 方法
     *
     * @param key 键
     * @return 返回值
     */
    public V get(K key) {
        // 使用 ConcurrentHashMap 的 get 方法获取值，保证线程安全
        return cache.get(key);
    }

    /**
     * 线程安全的检查键是否存在
     *
     * @param key 键
     * @return 返回boolean类型的结果
     */
    public boolean containsKey(K key) {
        // 使用 ConcurrentHashMap 的 containsKey 方法检查键是否存在，保证线程安全
        return cache.containsKey(key);
    }

    /**
     * 线程安全的 删除键值对方法
     *
     * @param key 键
     */
    public void remove(K key) {
        // 使用 ConcurrentHashMap 的 remove 方法移除键值对，保证线程安全
        cache.remove(key);
    }

    /**
     * 线程安全的 清空缓存
     */
    public void clear() {
        cache.clear();
    }

    /**
     * 线程安全的 缓存大小
     *
     * @return 返回大小
     */
    public int size() {
        // 使用 ConcurrentHashMap 的 size 方法获取缓存大小，保证线程安全
        return cache.size();
    }
}
