package com.mmall.common;

import com.mmall.util.PropertiesUtil;
import redis.clients.jedis.JedisPoolConfig;
import redis.clients.jedis.ShardedJedis;

/**
 * @author colaz
 * @date 2019/5/27
 **/
public class RedisShardedPool {
    private static ShardedJedis pool;      //jedis连接池
    private static Integer maxTotal     = PropertiesUtil.getIntProperty("redis.max.total", 20);//最大的连接数
    private static Integer maxIdle      = PropertiesUtil.getIntProperty("redis.max.idle", 10);   //最大空闲连接数
    private static Integer minIdle      = PropertiesUtil.getIntProperty("redis.min.idle", 1);   //最小空闲连接数
    private static Boolean testOnBorrow = PropertiesUtil.getBoolProperty("redis.test.borrow", true); //是否验证，赋值为true，则borrow的jedis实例是可用的
    private static Boolean testOnReturn = PropertiesUtil.getBoolProperty("redis.test.return", true); //是否验证，赋值为true，则return到jedis pool的连接是可用的
    private static String  ip           = PropertiesUtil.getProperty("redis.ip");
    private static Integer port         = PropertiesUtil.getIntProperty("redis.port");

    private static void initPool() {
        JedisPoolConfig config = new JedisPoolConfig();

        config.setMaxTotal(maxTotal);
        config.setMaxIdle(maxIdle);
        config.setMinIdle(minIdle);
        config.setTestOnBorrow(testOnBorrow);
        config.setTestOnReturn(testOnReturn);
        config.setBlockWhenExhausted(true);     //连接耗尽时是否阻塞，false会抛出异常，true会阻塞直到timeout


    }

    static {
        initPool();
    }



}

