package com.mmall.common;

import com.mmall.util.PropertiesUtil;
import lombok.extern.slf4j.Slf4j;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

/**
 * redis的连接池
 */
@Slf4j
public class RedisPool {
    private static JedisPool pool; //jedis(对应就是一个redis的客户端)的连接池，用于和redis的服务器进行连接
    private static Integer maxTotal = Integer.parseInt(PropertiesUtil.getProperty("redis.max.total", "20"));//最大连接数
    private static Integer maxIdle = Integer.parseInt(PropertiesUtil.getProperty("redis.max.idle", "10"));//在jedispool中最大的空闲状态的jedis实例个数
    private static Integer minIdle = Integer.parseInt(PropertiesUtil.getProperty("redis.min.idle", "2"));//在jedispool中最小的空闲状态的jedis实例个数
    private static Boolean testOnBorrow = Boolean.parseBoolean(PropertiesUtil.getProperty("redis.test.borrow", "true"));//在borrow一个实例时，是否要先测试jedis实例是否可用，如果为true，那么代表拿到的jedis实例一定可用
    private static Boolean testOnReturn = Boolean.parseBoolean(PropertiesUtil.getProperty("redis.test.return", "true"));//在return一个实例时，是否要先测试jedis实例是否可用，如果为true，那么代表放回jedispool的jedis实例一定可用
    private static String redisIp = PropertiesUtil.getProperty("redis1.ip");
    private static Integer redisPort = Integer.parseInt(PropertiesUtil.getProperty("redis1.port"));

    private static void initPool() {
        JedisPoolConfig config = new JedisPoolConfig();
        config.setMaxTotal(maxTotal);
        config.setMaxIdle(maxIdle);
        config.setMinIdle(minIdle);

        config.setTestOnBorrow(testOnBorrow);
        config.setTestOnReturn(testOnReturn);

        //连接耗尽的时候，是否阻塞。如果为false，会抛出异常，如果为true，就会阻塞直到超时
        config.setBlockWhenExhausted(true);

        pool = new JedisPool(config, redisIp, redisPort, 1000 * 2);
    }

    static {
        //保证只会初始化一次
        initPool();
    }

    public static Jedis getJedis() {
        return pool.getResource();
    }

    public static void returnResource(Jedis jedis) {
        pool.returnResource(jedis);
    }

    public static void returnBrokenResource(Jedis jedis) {
        pool.returnBrokenResource(jedis);
    }

}
