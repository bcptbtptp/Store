package com.yaorange.store.utils;

import net.sf.ehcache.Cache;
import net.sf.ehcache.CacheManager;
import net.sf.ehcache.Element;

public class CacheUtils {
	private static CacheManager cacheManager = CacheManager
			.create(CacheUtils.class.getClassLoader().getResourceAsStream("ehcache.xml"));

	public static Element get(String key,String cacheName) {
		Cache cache = cacheManager.getCache(cacheName);
		Element element = cache.get(key);
		return element;
	}

	public static void put(Element element,String cacheName) {
		Cache cache = cacheManager.getCache(cacheName);
		cache.put(element);
	}

	public static void removeCache(String cacheName, String elementName) {
		Cache cache = cacheManager.getCache(cacheName);
//		Element element = cache.get(elementName);
		cache.remove(elementName);
	}

}
