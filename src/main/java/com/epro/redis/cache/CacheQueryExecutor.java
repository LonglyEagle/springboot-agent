package com.epro.redis.cache;

/**
 * 方法执行顺序：{@link #backCacheEntity(Object)}、{@link #execute()}、{@link #cache(Object)}
 * 
 * @author hlm
 */
public interface CacheQueryExecutor<T> {
	
	/**
	 * 是否返回缓存的值，不往下执行execute方法
	 * 
	 * @param entity 从缓存中得到的值
	 */
	public boolean backCacheEntity(T entity);
	
	/**
	 * 查询缓存后执行的方法
	 */
	public T execute();
	
	/**
	 * 是否缓存execute返回值
	 * 
	 * @param entity execute方法返回值
	 */
	public boolean putCacheEntity(T entity);
	
}
