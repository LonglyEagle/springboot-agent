package com.epro.redis.lock;

import java.util.concurrent.TimeUnit;

import javax.annotation.Resource;

import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Component;

import com.epro.constant.TimeEnum;

/**
 * 分布式立即失败锁
 * 
 * @author hlm
 */
@Component
public class AtOnceLock {
	
	public static final String Prefix = AtOnceLock.class.getName();
	public static final String Placeholder = "";
	
	@Resource(name = "redisTemplate")
	private ValueOperations<String, Object> valOps;
	
	/**
	 * 获得锁，成功返回true，失败返回false
	 */
	public boolean lock(String uniqueLockID) {
		String key = Prefix + uniqueLockID;
		if(valOps.setIfAbsent(key, Placeholder)) {
			valOps.getOperations().expire(key, TimeEnum.MILLIS_ONE_MIN, TimeUnit.MILLISECONDS);
			return true;
		} else {
			return false;
		}
	}
	
	/**
	 * 释放锁
	 */
	public void unlock(String uniqueLockID) {
		valOps.getOperations().delete(Prefix + uniqueLockID);
	}

}
