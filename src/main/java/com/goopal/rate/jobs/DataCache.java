package com.goopal.rate.jobs;

import com.alibaba.fastjson.JSONArray;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.locks.ReentrantLock;

/**
 * 需要线程安全、设计一个带时间参数的灵活缓存类，当调用者想获取数据时，告知其缓存数据和缓存时长（优先时长）。
 * 适合一定量的并发
 * created by binhy on 2019-1-8
 */
public class DataCache {

  private static DataCache dataCache;// 静态对象，私有构方，锁
  private static ReentrantLock lock = new ReentrantLock();
  private DataCache(){}
  public static DataCache getDataCache(){
    if(null==dataCache){
      lock.lock();
      if(null==dataCache){
        dataCache = new DataCache();
      }
      lock.unlock();
    }
    return dataCache;
  }


  private static long alive_time;
  private static final long effective_time_seconds = 5000;//实际上一般6-10s才会刷新一次，去拿也是一样的数据,就担心刚好是变的前一刻拿的
  private static Feixiaohao data;

  public synchronized void setData(Feixiaohao data){
    this.data = data;
  }

  public Feixiaohao getData(){
    return this.data;
  }

  // 判断数据是否在有效时间内
  public boolean isEffective(){
    if(null == data){
      return false;
    }
    return (System.currentTimeMillis()-data.getCtime()) < effective_time_seconds;
  }


}
