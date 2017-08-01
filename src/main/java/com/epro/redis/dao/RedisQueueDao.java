package com.epro.redis.dao;

import javax.annotation.Resource;

import org.springframework.data.redis.core.ListOperations;
import org.springframework.stereotype.Component;

/**
 * redis 队列 dao
 *
 * @author hlm
 */
@Component
public class RedisQueueDao {
	
	@Resource(name = "redisTemplate")
	private ListOperations<String, Object> listOps;
	
	/**
	 * 将任务目标添加到队列中
	 * 
	 * @param queueName	队列名称
	 * @param entity	队列元素对象
	 */
	public void push(String queueName, Object entity) {
		listOps.leftPush(queueName, entity);
	}
	
	/**
	 * 从任务队列中获取并移除任务目标
	 * 
	 * @param queueName	队列名称
	 */
	public Object pop(String queueName) {
		return listOps.rightPop(queueName);
	}
	
	/**
	 * 任务队列大小
	 * 
	 * @param queueName	队列名称
	 */
	public int size(String queueName) {
		return listOps.size(queueName).intValue();
	}
	
	/**
	 * 清空队列
	 * 
	 * @param queueName	队列名称
	 */
	public void clear(String queueName) {
		listOps.getOperations().delete(queueName);
	}

}
