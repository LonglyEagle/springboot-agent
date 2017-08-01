package com.epro.redis.cache;

import java.util.List;

/**
 * 缓存数据源
 * 
 * @author hlm
 */
public interface CacheDataSource {

	public <T> void set(Object[] cacheParams, Class<T> clazz, T entity, long millis);
	
	public <T> void setList(Object[] cacheParams, Class<T> clazz, List<T> entityList, long millis);
	
	public <T> T get(Object[] cacheParams, Class<T> clazz);

	public <T> List<T> getList(Object[] cacheParams, Class<T> clazz);
	
	public <T> void delete(Object[] cacheParams, Class<T> clazz);

	public <T> void deleteList(Object[] cacheParams, Class<T> clazz);

}
