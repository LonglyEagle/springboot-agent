package com.epro.service;

import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.epro.domain.Configuration;

@Component("configurationService")
@Transactional
public class ConfigurationServiceImpl implements ConfigurationService {

	private final ConfigurationRepository configurationRepository;
	
	
	public ConfigurationServiceImpl(ConfigurationRepository configurationRepository) {
		this.configurationRepository = configurationRepository;
	}

	@Override
	public Configuration findByIP(String ip) {
		return configurationRepository.findByIP(ip);
	}

	@Override
	public Configuration addConfig(Configuration config) {
		return configurationRepository.save(config);
	}

}
