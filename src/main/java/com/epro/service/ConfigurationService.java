package com.epro.service;

import com.epro.domain.Configuration;

public interface ConfigurationService {
	
	Configuration findByIP(String ip);
	
	Configuration addConfig(Configuration config);
}
