package com.mmall.task;

import com.mmall.common.Const;
import com.mmall.common.RedisShardedPool;
import com.mmall.service.IOrderService;
import com.mmall.util.PropertiesUtil;
import com.mmall.util.RedisShardedPoolUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import redis.clients.jedis.ShardedJedisPool;

import javax.annotation.PreDestroy;

/**
 * @author colaz
 * @date 2019/6/13
 **/
@Component
@Slf4j
public class CloseOrderTask {

    @Autowired
    private IOrderService iOrderService;

    @PreDestroy
    public void delLock() {
        //使用tomcat shutdown安全关闭时调用
        RedisShardedPoolUtil.del(Const.REDIS_LOCK.CLOSE_ORDER_TASK_LOCK);
    }

//    @Scheduled(cron = "0 */1 * * * ?")  //每一分钟，每个1分钟的整数倍
    public void closeOrderTaskV1() {
        log.info("关闭订单定时任务启动");
        int hour = PropertiesUtil.getIntProperty("close.order.task.time.hour", 2);
        iOrderService.closeOrder(hour);
    }

//    @Scheduled(cron = "0 */1 * * * ?")
    public void closeOrderTaskV2() {
        log.info("关闭订单定时任务启动");
        Long lockTimeOut = Long.parseLong(PropertiesUtil.getProperty("lock.timeout", "5000"));
        Long setnxRes = RedisShardedPoolUtil.setnx(Const.REDIS_LOCK.CLOSE_ORDER_TASK_LOCK, String.valueOf(System.currentTimeMillis() + lockTimeOut));
        //setnx set成功返回1，失败返回0
        if (setnxRes != null && setnxRes.intValue() == 1) {
            //获得锁
            log.info("线程{}， 获得分布式锁{}", Thread.currentThread().getName(), Const.REDIS_LOCK.CLOSE_ORDER_TASK_LOCK);
            closeOrder();
        } else {
            log.info("线程{}， 未获得分布式锁{}", Thread.currentThread().getName(), Const.REDIS_LOCK.CLOSE_ORDER_TASK_LOCK);
        }

    }

    @Scheduled(cron = "0 */1 * * * ?")
    public void closeOrderTaskV3() {
        log.info("关闭订单定时任务启动");
        Long lockTimeOut = Long.parseLong(PropertiesUtil.getProperty("lock.timeout", "5000"));
        Long setnxRes = RedisShardedPoolUtil.setnx(Const.REDIS_LOCK.CLOSE_ORDER_TASK_LOCK, String.valueOf(System.currentTimeMillis() + lockTimeOut));
        //setnx set成功返回1，失败返回0
        if (setnxRes != null && setnxRes.intValue() == 1) {
            //获得锁
            log.info("线程{}， 获得分布式锁{}", Thread.currentThread().getName(), Const.REDIS_LOCK.CLOSE_ORDER_TASK_LOCK);
            closeOrder();
        } else {
            // 未获得锁，根据时间戳判断是否可以重置并获取锁
            String lockValue = RedisShardedPoolUtil.get(Const.REDIS_LOCK.CLOSE_ORDER_TASK_LOCK);
            if (lockValue != null && System.currentTimeMillis() > Long.parseLong(lockValue)) {
                //锁消失 或 锁超时,可以重新获得锁
                String getSetLockValue = RedisShardedPoolUtil.getSet(Const.REDIS_LOCK.CLOSE_ORDER_TASK_LOCK, String.valueOf(System.currentTimeMillis()+lockTimeOut));
                //再次用当前时间戳getset，返回旧值与lockValue判断，是否可以获得锁
                //当key没有旧值，返回null --> 获取锁
                if (getSetLockValue == null || (getSetLockValue != null && StringUtils.equals(getSetLockValue, lockValue))) {
                    //真正获得锁
                    closeOrder();
                } else {
                    log.info("线程{}， 未获得分布式锁{}", Thread.currentThread().getName(), Const.REDIS_LOCK.CLOSE_ORDER_TASK_LOCK);
                }
            } else {
                log.info("线程{}， 未获得分布式锁{}", Thread.currentThread().getName(), Const.REDIS_LOCK.CLOSE_ORDER_TASK_LOCK);
            }
        }
    }


    private void closeOrder() {
        RedisShardedPoolUtil.expire(Const.REDIS_LOCK.CLOSE_ORDER_TASK_LOCK, 5); //设置锁过期时间，防止死锁
        int hour = PropertiesUtil.getIntProperty("close.order.task.time.hour", 2);
        iOrderService.closeOrder(hour);
        RedisShardedPoolUtil.del(Const.REDIS_LOCK.CLOSE_ORDER_TASK_LOCK);   //任务完成释放锁
    }





}
