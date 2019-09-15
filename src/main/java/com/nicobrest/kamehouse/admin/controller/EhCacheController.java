package com.nicobrest.kamehouse.admin.controller;

import com.nicobrest.kamehouse.admin.service.EhCacheService;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Controller to check the status and clear all the ehcaches.
 * 
 * @author nbrest
 *
 */
@Controller
@RequestMapping(value = "/api/v1/admin/ehcache")
public class EhCacheController {

  private final Logger logger = LoggerFactory.getLogger(getClass());

  @Autowired
  private EhCacheService ehCacheService;

  /**
   * Returns the status of all the ehcaches or the cache specified as a
   * parameter.
   */
  @GetMapping
  @ResponseBody
  public ResponseEntity<List<Map<String, Object>>> getCache(@RequestParam(value = "name",
      required = false) String cacheName) {
    logger.trace("In controller /api/v1/admin/ehcache (GET)");
    List<Map<String, Object>> cacheList;
    if (!StringUtils.isBlank(cacheName)) {
      cacheList = new ArrayList<>();
      Map<String, Object> cache = ehCacheService.getCache(cacheName);
      if (!cache.isEmpty()) {
        cacheList.add(cache);
      }
    } else {
      cacheList = ehCacheService.getAllCaches();
    }
    return new ResponseEntity<>(cacheList, HttpStatus.OK);
  }

  /**
   * Clears all the ehcaches or the cache specified as a parameter.
   */
  @DeleteMapping
  public ResponseEntity<Void> clearCache(@RequestParam(value = "name",
      required = false) String cacheName) {
    logger.trace("In controller /api/v1/admin/ehcache (DELETE)");
    if (!StringUtils.isBlank(cacheName)) {
      ehCacheService.clearCache(cacheName);
    } else {
      ehCacheService.clearAllCaches();
    }
    return new ResponseEntity<>(HttpStatus.OK);
  }
}
