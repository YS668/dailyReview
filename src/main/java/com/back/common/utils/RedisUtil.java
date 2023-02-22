package com.back.common.utils;

import java.util.concurrent.TimeUnit;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;
import javax.annotation.PostConstruct;
import javax.annotation.Resource;

@Component
public class RedisUtil {

    @Autowired
    private  RedisTemplate redisTemplate;

    private static RedisTemplate template;

    @PostConstruct
    public void init(){
        template = redisTemplate;
    }

    //添加String数据
    public static void addString(String key,String value){
        template.boundValueOps(key).set(value);
    }

    //添加String数据(过期)
    public static void addExpireString(String key,String value,long timeout,TimeUnit unit){
        template.boundValueOps(key).set(value,timeout,unit);
    }

    //添加String数据(一小时过期)
    public static void addOneHoursExpireString(String key,String value){
        template.boundValueOps(key).set(value,1,TimeUnit.HOURS);
    }

    //获取String数据
    public static String getString(String key){
        return (String) template.boundValueOps(key).get();
    }

    //删除String数据
    public static void removeString(String key){
        template.delete(key);
    }

    //判断key是否存在
    public static boolean hasKey(String key){
        return template.hasKey(key);
    }

    //原子递增
    public static void incUp(String key){
        template.boundValueOps("StringKey").increment(1L);
    }

    //原子递减
    public static void incDown(String key){
        template.boundValueOps("StringKey").increment(-1L);
    }



}
