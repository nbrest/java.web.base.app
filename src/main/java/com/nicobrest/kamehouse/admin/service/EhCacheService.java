package com.nicobrest.kamehouse.admin.service;

import net.sf.ehcache.Cache;
import net.sf.ehcache.Element;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.cache.ehcache.EhCacheCacheManager;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Service class to manage the ehcache in the system.
 * 
 * @author nbrest
 *
 */
@Service
public class EhCacheService {

  private static final Logger logger = LoggerFactory.getLogger(EhCacheService.class);

  @Autowired
  @Qualifier("cacheManager")
  private EhCacheCacheManager cacheManager;

  public void setCacheManager(EhCacheCacheManager cacheManager) {
    this.cacheManager = cacheManager;
  }

  public EhCacheCacheManager getCacheManager() {
    return cacheManager;
  }
  
  /**
   * Returns the cache information of the cache specified as a parameter.
   */
  public Map<String, Object> getCache(String cacheName) {

    logger.trace("Getting information for cache: {}", cacheName);
    Cache cache = cacheManager.getCacheManager().getCache(cacheName);
    Map<String, Object> cacheMap = new HashMap<>();
    if (cache != null) {
      populateCacheMap(cacheMap, cache);
    }
    return cacheMap;
  }

  /**
   * Returns the status of all the ehcaches.
   */
  public List<Map<String, Object>> getAllCaches() {

    String[] cacheNames = cacheManager.getCacheManager().getCacheNames();
    List<Map<String, Object>> cacheList = new ArrayList<>();

    for (int i = 0; i < cacheNames.length; i++) {
      Map<String, Object> cacheMap = getCache(cacheNames[i]);
      if (!cacheMap.isEmpty()) {
        cacheList.add(cacheMap);
      }
    }
    return cacheList;
  }

  /**
   * Clears the ehcache specified as a parameter.
   */
  public void clearCache(String cacheName) {

    logger.trace("Clearing cache: {}", cacheName);
    Cache cache = cacheManager.getCacheManager().getCache(cacheName);
    if (cache != null) {
      cache.removeAll();
    }
  }

  /**
   * Clears all the ehcaches.
   */
  public void clearAllCaches() {

    String[] cacheNames = cacheManager.getCacheManager().getCacheNames();
    for (int i = 0; i < cacheNames.length; i++) {
      clearCache(cacheNames[i]);
    }
  }

  /**
   * Populate the map that represents the cache with the cache information.
   */
  private void populateCacheMap(Map<String, Object> cacheMap, Cache cache) {

    cacheMap.put("name", cache.getName());
    cacheMap.put("status", cache.getStatus().toString());
    List<String> cacheValues = new ArrayList<>();
    List<?> cacheKeys = cache.getKeys();
    for (Object key : cacheKeys) {
      Element cacheElement = cache.get(key);
      if (cacheElement != null) {
        cacheValues.add(cacheElement.getObjectValue().toString());
      }
    }
    cacheMap.put("keys", cache.getKeys().toString());
    cacheMap.put("values", cacheValues);
  }
}
