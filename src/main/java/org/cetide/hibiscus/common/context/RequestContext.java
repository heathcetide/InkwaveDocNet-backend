package org.cetide.hibiscus.common.context;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 * 请求上下文工具类，用于在线程内保存和获取与当前请求相关的数据。
 * 基于 ThreadLocal 实现，适用于拦截器、日志追踪、认证上下文等。
 *
 * @author heathcetide
 */
public class RequestContext {

    /**
     * 每个线程维护独立的上下文 Map
     */
    private static final ThreadLocal<Map<String, Object>> context = ThreadLocal.withInitial(HashMap::new);

    /**
     * 放入上下文中的键值对
     */
    public static void put(String key, Object value) {
        context.get().put(key, value);
    }

    /**
     * 获取值（不安全，需自行转换类型）
     */
    @SuppressWarnings("unchecked")
    public static <T> T get(String key) {
        return (T) context.get().get(key);
    }

    /**
     * 类型安全地获取值（带默认值）
     */
    public static <T> T get(String key, Class<T> type, T defaultValue) {
        Object value = context.get().get(key);
        if (type.isInstance(value)) {
            return type.cast(value);
        }
        return defaultValue;
    }

    /**
     * 移除某个键值
     */
    public static void remove(String key) {
        context.get().remove(key);
    }

    /**
     * 获取当前线程所有上下文内容（只读）
     */
    public static Map<String, Object> getAll() {
        return Collections.unmodifiableMap(context.get());
    }

    /**
     * 清除当前线程上下文（必须在请求结束时调用以避免内存泄露）
     */
    public static void clear() {
        context.remove();
    }
}
