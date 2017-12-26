package com.rsh.framework.weixin.cache.impl;

import com.rsh.framework.weixin.cache.IAccessTokenCache;
import org.springframework.dao.DataAccessException;
import org.springframework.data.redis.connection.RedisConnection;
import org.springframework.data.redis.core.RedisCallback;
import org.springframework.data.redis.core.StringRedisTemplate;

import java.io.UnsupportedEncodingException;

/**
 * redis缓存AccessToken实现
 * 使用redis集中管理AccessToken，分布式环境中使用
 * Created By Rsh
 *
 * @Description
 * @Date: 2017/12/26
 * @Time: 9:31
 */
public class RedisAccessTokenCache implements IAccessTokenCache {

    /**
     * key前缀
     */
    private static String KEY_PREFIX = "weixin:accessToken:";
    /**
     * StringRedisTemplate
     */
    private StringRedisTemplate redisTemplate;

    public RedisAccessTokenCache(StringRedisTemplate redisTemplate) {
        if (redisTemplate == null) {
            throw new IllegalArgumentException("redisTemplate 为空！");
        }
        this.redisTemplate = redisTemplate;
    }

    @Override
    public String getAccessToken(String key) {
        String newkey = KEY_PREFIX + key;
        return redisTemplate.execute(new RedisCallback<String>() {
            public String doInRedis(RedisConnection connection) throws DataAccessException {
                try {
                    byte[] tmp = connection.get(newkey.getBytes("UTF-8"));
                    if (tmp == null) {
                        return null;
                    } else {
                        return new String(tmp, "UTF-8");
                    }
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }
                return null;
            }
        });
    }

    @Override
    public boolean setAccessToken(String key, String value, int expireTime) {
        String newkey = KEY_PREFIX + key;
        return redisTemplate.execute(new RedisCallback<Boolean>() {
            public Boolean doInRedis(RedisConnection connection) throws DataAccessException {
                try {
                    return connection.setEx(newkey.getBytes("UTF-8"), expireTime, value.getBytes("UTF-8"));
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }
                return false;
            }
        });
    }

    @Override
    public void removeAccessToken(String key) {
        String newkey = KEY_PREFIX + key;
        redisTemplate.execute(new RedisCallback<Long>() {
            public Long doInRedis(RedisConnection connection) throws DataAccessException {
                try {
                    return connection.del(newkey.getBytes("UTF-8"));
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }
                return 0L;
            }
        });
    }
}