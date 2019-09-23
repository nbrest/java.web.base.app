package com.nicobrest.kamehouse.admin.controller;

import com.nicobrest.kamehouse.admin.service.EhCacheService;
import com.nicobrest.kamehouse.main.controller.AbstractController;

import org.apache.commons.lang.StringUtils;
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
public class EhCacheController extends AbstractController {

  @Autowired
  private EhCacheService ehCacheService;

  /**
   * Returns the status of all the ehcaches or the cache specified as a
   * parameter.
   */
  @GetMapping
  @ResponseBody
  public ResponseEntity<List<Map<String, Object>>> get(@RequestParam(value = "name",
      required = false) String cacheName) {
    logger.trace("In controller /api/v1/admin/ehcache (GET)");
    List<Map<String, Object>> cacheList;
    if (!StringUtils.isBlank(cacheName)) {
      cacheList = new ArrayList<>();
      Map<String, Object> cache = ehCacheService.read(cacheName);
      if (!cache.isEmpty()) {
        cacheList.add(cache);
      }
    } else {
      cacheList = ehCacheService.readAll();
    }
    return generateGetResponseEntity(cacheList);
  }

  /**
   * Clears all the ehcaches or the cache specified as a parameter.
   */
  @DeleteMapping
  public ResponseEntity<Void> clear(@RequestParam(value = "name",
      required = false) String cacheName) {
    logger.trace("In controller /api/v1/admin/ehcache (DELETE)");
    if (!StringUtils.isBlank(cacheName)) {
      ehCacheService.clear(cacheName);
    } else {
      ehCacheService.clearAll();
    }
    return new ResponseEntity<>(HttpStatus.OK);
  }
}
