package com.epro.service;

import java.util.List;

import org.springframework.data.repository.Repository;

import com.epro.domain.PathMap;

public interface PathMapRepository extends Repository<PathMap, Long>{
	
	List<PathMap> findAll();
}
