package es.urjc.dad.poshart.controller;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.CacheManager;
import org.springframework.cache.concurrent.ConcurrentMapCache;
import org.springframework.cache.concurrent.ConcurrentMapCacheManager;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/cache")
public class CacheController {
	@Autowired
	private CacheManager cacheManager;
	
	@RequestMapping(value="/post", method=RequestMethod.GET)
	public Map<Object, Object> getCacheContentPost(){
		ConcurrentMapCacheManager cacheMgr = (ConcurrentMapCacheManager) cacheManager;
		ConcurrentMapCache cache = (ConcurrentMapCache) cacheMgr.getCache("Posts");
		return cache.getNativeCache();
	}
	@RequestMapping(value="/user", method=RequestMethod.GET)
	public Map<Object, Object> getCacheContentUser(){
		ConcurrentMapCacheManager cacheMgr = (ConcurrentMapCacheManager) cacheManager;
		ConcurrentMapCache cache = (ConcurrentMapCache) cacheMgr.getCache("Users");
		return cache.getNativeCache();
	}
	@RequestMapping(value="/comment", method=RequestMethod.GET)
	public Map<Object, Object> getCacheContentComment(){
		ConcurrentMapCacheManager cacheMgr = (ConcurrentMapCacheManager) cacheManager;
		ConcurrentMapCache cache = (ConcurrentMapCache) cacheMgr.getCache("Comments");
		return cache.getNativeCache();
	}
}
