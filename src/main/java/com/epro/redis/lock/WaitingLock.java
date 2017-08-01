package com.epro.redis.lock;

import java.util.concurrent.TimeUnit;

import javax.annotation.Resource;

import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Component;

import com.epro.constant.TimeEnum;

/**
 * 分布式等待锁
 * 
 * @author hlm
 */
@Component
public class WaitingLock {
	
	public static final String Prefix = WaitingLock.class.getName();
	public static final String Placeholder = "";
	
	@Resource(name = "redisTemplate")
	private ValueOperations<String, Object> valOps;
	
	/**
	 * 获得锁，成功返回true，超时返回false
	 */
	public boolean lock(String uniqueLockID, long milliseconds) {
		String key = Prefix + uniqueLockID;
		long start = System.currentTimeMillis();
		while(true) {
			//尝试获得锁
			if(valOps.setIfAbsent(key, Placeholder)) {
				valOps.getOperations().expire(key, TimeEnum.MILLIS_ONE_MIN, TimeUnit.MILLISECONDS);
				return true;
			}
			//等待时间超时
			if(System.currentTimeMillis()-start > milliseconds) {
				return false;
			}
			//尝试频率
			try {
				Thread.sleep(50);
			} catch (InterruptedException e) {
				return false;
			}
		}
	}
	
	/**
	 * 释放锁
	 */
	public void unlock(String uniqueLockID) {
		valOps.getOperations().delete(Prefix + uniqueLockID);
	}

}
