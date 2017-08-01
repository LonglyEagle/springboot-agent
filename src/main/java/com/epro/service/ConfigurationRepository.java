package com.epro.service;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.Repository;

import com.epro.domain.Configuration;

public interface ConfigurationRepository extends Repository<Configuration, Long> {
	
	List<Configuration> findAll();
	
	@Query("from Configuration c where c.ip = ?1")
	Configuration findByIP(String ip);
	
	Configuration save(Configuration config);
}
