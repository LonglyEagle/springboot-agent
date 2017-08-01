package com.epro.redis.dao;

import java.util.Arrays;
import java.util.List;
import java.util.concurrent.TimeUnit;

import javax.annotation.Resource;

import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Component;

import com.epro.redis.cache.CacheDataSource;

/**
 * redis key-value缓存 dao
 *
 * @author hlm
 */
@Component
public class RedisValuesDao implements CacheDataSource {

	@Resource(name = "redisTemplate")
	private ValueOperations<String, Object> valOps;
	
	/**
	 * 添加对象到缓存
	 * 
	 * @param cacheParams	条件参数
	 * @param clazz			缓存对象类型
	 * @param entity		缓存对象
	 * @param millis		缓存毫秒
	 */
	@Override
	public <T> void set(Object[] cacheParams, Class<T> clazz, T entity, long millis) {
		String cacheKey = clazz.getName() + Arrays.toString(cacheParams);
		valOps.set(cacheKey, entity , millis, TimeUnit.MILLISECONDS);
	}
	
	/**
	 * 添加对象到缓存（永久）
	 * @param cacheParams 	条件参数
	 * @param clazz 		缓存对象类型
	 * @param entity 		缓存对象
	 */
	public <T> void set(Object[] cacheParams, Class<T> clazz, T entity) {
		String cacheKey = clazz.getName() + Arrays.toString(cacheParams);
		valOps.set(cacheKey, entity);
	}
	
	/**
	 * 添加对象集合到缓存
	 * 
	 * @param cacheParams	条件参数
	 * @param clazz			缓存集合子元素类型
	 * @param entityList	缓存集合
	 * @param millis		缓存毫秒
	 */
	@Override
	public <T> void setList(Object[] cacheParams, Class<T> clazz, List<T> entityList, long millis) {
		String cacheKey = clazz.getName() + Arrays.toString(cacheParams);
		valOps.set(cacheKey, entityList , millis, TimeUnit.MILLISECONDS);
	}
	
	/**
	 * 查询缓存对象
	 * 
	 * @param cacheParams	条件参数
	 * @param clazz			缓存对象类型
	 * 
	 * @return <T> 缓存对象
	 */
	@Override
	@SuppressWarnings("unchecked")
	public <T> T get(Object[] cacheParams, Class<T> clazz) {
		String cacheKey = clazz.getName() + Arrays.toString(cacheParams);
		return (T)valOps.get(cacheKey);
	}
	
	/**
	 * 查询缓存对象集合
	 * 
	 * @param cacheParams	条件参数
	 * @param clazz			缓存集合子元素类型
	 * 
	 * @return List<T> 缓存集合
	 */
	@Override
	@SuppressWarnings("unchecked")
	public <T> List<T> getList(Object[] cacheParams, Class<T> clazz) {
		String cacheKey = clazz.getName() + Arrays.toString(cacheParams);
		return (List<T>)valOps.get(cacheKey);
	}
	
	/**
	 * 移除缓存对象
	 * 
	 * @param cacheParams	条件参数
	 * @param clazz			缓存对象类型
	 */
	@Override
	public <T> void delete(Object[] cacheParams, Class<T> clazz) {
		String cacheKey = clazz.getName() + Arrays.toString(cacheParams);
		valOps.getOperations().delete(cacheKey);
	}
	
	/**
	 * 移除缓存对象集合
	 * 
	 * @param cacheParams	条件参数
	 * @param clazz			缓存集合子元素类型
	 */
	@Override
	public <T> void deleteList(Object[] cacheParams, Class<T> clazz) {
		String cacheKey = clazz.getName() + Arrays.toString(cacheParams);
		valOps.getOperations().delete(cacheKey);
	}

}
