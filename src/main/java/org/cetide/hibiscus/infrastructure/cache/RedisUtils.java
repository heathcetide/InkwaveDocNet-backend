package org.cetide.hibiscus.infrastructure.cache;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import java.util.Set;
import java.util.concurrent.TimeUnit;

@Component
public class RedisUtils {
    private final RedisTemplate<String, Object> redisTemplate;

    private final ObjectMapper objectMapper;


    public RedisUtils(RedisTemplate<String, Object> redisTemplate, ObjectMapper objectMapper) {
        this.redisTemplate = redisTemplate;
        this.objectMapper = objectMapper;
    }

    public void set(String key, String value, long timeout, TimeUnit unit) {
        redisTemplate.opsForValue().set(key, value, timeout, unit);
    }

    public <T> void set(String key, T value, long timeout, TimeUnit unit) {
        try  {
            String json = objectMapper.writeValueAsString(value);
            redisTemplate.opsForValue().set(key, json, timeout, unit);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public boolean exists(String key) {
        return Boolean.TRUE.equals(redisTemplate.hasKey(key));
    }

    /**
     * 设置缓存
     */
    public void set(String key, Object value, long time) {
        redisTemplate.opsForValue().set(key, value, time, TimeUnit.SECONDS);
    }

    /**
     * 设置缓存
     */
    public void set(String key, Object value) {
        redisTemplate.opsForValue().set(key, value);
    }

    /**
     * 获取缓存
     */
    public Object get(String key) {
        return redisTemplate.opsForValue().get(key);
    }

    /**
     * 获取值
     *
     * @param key 键
     * @param clazz 返回的类型
     * @return 值
     */
    public <T> T get(String key, Class<T> clazz) {
        Object value = redisTemplate.opsForValue().get(key);
        if (value == null) {
            return null;
        }

        if (clazz.isInstance(value)) {
            return clazz.cast(value);
        }

        return objectMapper.convertValue(value, clazz);
    }

    /**
     * 删除缓存
     */
    public Boolean delete(String key) {
        return redisTemplate.delete(key);
    }

    /**
     * lazy-free 惰性释放
     */
    public Boolean delLazyFree(String key){
        return redisTemplate.unlink(key);
    }

    /**
     * 判断key是否存在
     */
    public Boolean hasKey(String key) {
        return redisTemplate.hasKey(key);
    }

    /**
     * 设置过期时间
     */
    public Boolean expire(String key, long time) {
        return redisTemplate.expire(key, time, TimeUnit.SECONDS);
    }

    /**
     * 获取过期时间
     */
    public Long getExpire(String key) {
        return redisTemplate.getExpire(key, TimeUnit.SECONDS);
    }

    /**
     * 自增操作
     * @param key 键
     * @param delta 增量（通常为1，负数表示减少）
     * @return 自增后的值
     */
    public Long increment(String key, long delta) {
        return redisTemplate.opsForValue().increment(key, delta);
    }

    /**
     * 设置值，如果 key 不存在则设置成功（分布式锁的典型用法）
     */
    public Boolean setIfAbsent(String key, String value, long time) {
        Boolean result = redisTemplate.opsForValue().setIfAbsent(key, value, time, TimeUnit.SECONDS);
        return result != null && result;
    }

    /**
     * 设置值，如果 key 不存在则设置成功（分布式锁的典型用法）
     */
    public Boolean setIfAbsent(String key, String value, long time, TimeUnit minutes) {
        Boolean result = redisTemplate.opsForValue().setIfAbsent(key, value, time, minutes);
        return result != null && result;
    }

    /**
     * 获取匹配的所有键
     * @param pattern 匹配模式（如 "prefix:*"）
     * @return 匹配的键集合
     */
    public Set<String> keys(String pattern) {
        return redisTemplate.keys(pattern);
    }

    /**
     * 添加或更新用户分数
     * @param ranking 排行榜名
     * @param user 用户名
     * @param score 分数
     */
    public void addScore(String ranking, String user, double score) {
        redisTemplate.opsForZSet().add(ranking, user, score);
    }

    /**
     * 增加用户分数
     * @param ranking 排行榜名
     * @param user 用户名
     * @param increment 分数
     */
    public void incrementScore(String ranking, String user, double increment) {
        redisTemplate.opsForZSet().incrementScore(ranking, user, increment);
    }

    /**
     * 获取前N名
     * @param ranking 排行榜名
     * @param n 第n
     */
    public Set<Object> getTopN(String ranking, int n) {
        return redisTemplate.opsForZSet().reverseRange(ranking, 0, n - 1);
    }

    /**
     * 获取用户排名
     * @param ranking 排行榜名
     * @param user 用户名
     */
    public Long getUserRank(String ranking, String user) {
        return redisTemplate.opsForZSet().reverseRank(ranking, user);
    }

    /**
     * 获取用户分数
     * @param ranking 排行榜名
     * @param user 用户名
     */
    public Double getRankingScore(String ranking, String user) {
        return redisTemplate.opsForZSet().score(ranking, user);
    }
} 