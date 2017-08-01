package com.epro.redis.cache;

import java.util.List;

/**
 * 缓存操作
 * 
 * @author hlm
 */
public final class CacheTemplate {

	/**
	 * 缓存查询（先查缓存、execute再查库、最后设置缓存）
	 */
	public static final <T extends SerializableEntity> T executeQuery(CacheDataSource dataSource, long millis, Class<T> clazz,
			Object[] cacheParams, CacheQueryExecutor<T> executor) {
		//查询缓存
		T entity = dataSource.get(cacheParams, clazz);
		//是否返回缓存值
		if(executor.backCacheEntity(entity)) {
			return entity;
		}
		//执行方法
		entity = executor.execute();
		//是否缓存
		if(executor.putCacheEntity(entity)) {
			dataSource.set(cacheParams, clazz, entity, millis);
		}
		return entity;
	}
	
	/**
	 * 设置缓存
	 */
	public static final <T extends SerializableEntity> void executePut(CacheDataSource dataSource, Class<T> clazz, T entity, long millis, Object... cacheParams) {
		dataSource.set(cacheParams, clazz, entity, millis);
	}
	
	/**
	 * 缓存清除
	 */
	public static final <T extends SerializableEntity> void executeDelete(CacheDataSource dataSource, Class<T> clazz, Object... cacheParams) {
		dataSource.delete(cacheParams, clazz);
	}
	
	/**
	 * 缓存查询集合（先查缓存、execute再查库、最后设置缓存）
	 */
	public static final <T extends SerializableEntity> List<T> executeQueryList(CacheDataSource dataSource, long millis, Class<T> clazz,
			Object[] cacheParams, CacheQueryListExecutor<T> executor) {
		//查询缓存
		List<T> entityList = dataSource.getList(cacheParams, clazz);
		//是否返回缓存值
		if(executor.backCacheEntity(entityList)) {
			return entityList;
		}
		//执行方法
		entityList = executor.execute();
		//是否缓存
		if(executor.putCacheEntity(entityList)) {
			dataSource.setList(cacheParams, clazz, entityList, millis);
		}
		return entityList;
	}
	
	/**
	 * 设置集合缓存
	 */
	public static final <T extends SerializableEntity> void executePutList(CacheDataSource dataSource, Class<T> clazz, List<T> entityList, long millis, Object... cacheParams) {
		dataSource.setList(cacheParams, clazz, entityList, millis);
	}
	
	/**
	 * 集合缓存清除
	 */
	public static final <T extends SerializableEntity> void executeDeleteList(CacheDataSource dataSource, Class<T> clazz, Object... cacheParams) {
		dataSource.deleteList(cacheParams, clazz);
	}

}
