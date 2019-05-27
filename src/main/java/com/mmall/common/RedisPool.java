package com.mmall.common;

import com.mmall.util.PropertiesUtil;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

/**
 * @author colaz
 * @date 2019/5/25
 **/
public class RedisPool {

    private static JedisPool pool;      //jedis连接池
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

        pool = new JedisPool(config, ip, port, 1000*2); //2秒
    }

    static {
        initPool();
    }

    public static Jedis getJedis() {
        return pool.getResource();
    }

    //Jedis 3.0 returnResource() and returnBrokenResource()  method will not be exposed.
    //使用redis.close()将redis实例返回给连接池

    public static void main(String[] args) {
        Jedis jedis = getJedis();
        jedis.set("cola", "colavalue");

        pool.destroy();
        System.out.println("done");

    }
}
