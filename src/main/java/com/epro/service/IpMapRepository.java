package com.epro.service;

import java.util.List;

import org.springframework.data.repository.Repository;

import com.epro.domain.IpMap;
/**
 * ip映射表操作类
 * @author Grant.You
 *
 */
public interface IpMapRepository extends Repository<IpMap, Long>{
	
	List<IpMap> findAll();  
}
