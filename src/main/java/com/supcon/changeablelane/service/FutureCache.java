package com.supcon.changeablelane.service;

import lombok.extern.slf4j.Slf4j;

import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Future;

/**
 * 定时任务缓存
 *
 * @author JWF
 * @date 2019/12/19
 */
@Slf4j
public class FutureCache {
  /** 定时器缓存 */
  public static final Map<Integer, Future<?>> scheduleMap = new ConcurrentHashMap<>();

  public static void cacheScheduleFuture(Integer acsId, Future<?> future) {
    scheduleMap.put(acsId, future);
  }


  public static void clearScheduleTask(Integer areaId) {
    Future<?> future = scheduleMap.get(areaId);
    if (Objects.nonNull(future)) {
      future.cancel(true);
      log.info("可变车道 / {} | 定时触发任务取消成功", areaId);
    }
  }

}
