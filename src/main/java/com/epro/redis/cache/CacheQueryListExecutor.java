package com.epro.redis.cache;

import java.util.List;

/**
 * 方法执行顺序：{@link #backCacheEntity(Object)}、{@link #execute()}、{@link #cache(Object)}
 * 
 * @author hlm
 */
public interface CacheQueryListExecutor<T> {
	
	/**
	 * 是否返回缓存的值，不往下执行execute方法
	 * 
	 * @param entityList 从缓存中得到的值
	 */
	public boolean backCacheEntity(List<T> entityList);
	
	/**
	 * 查询缓存后执行的方法
	 */
	public List<T> execute();
	
	/**
	 * 是否缓存execute返回值
	 * 
	 * @param entityList execute方法返回值
	 */
	public boolean putCacheEntity(List<T> entityList);
	
}
