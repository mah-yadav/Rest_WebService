package com.web.rest.web;

import java.io.FileReader;
import java.util.Properties;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.web.rest.util.ServicesConstants;

public class ApplicationConfig {
	
	private static final Logger LOG = LoggerFactory.getLogger(ApplicationConfig.class);
	private Properties configProperties = new Properties();

	private static final ApplicationConfig INSTANCE = new ApplicationConfig();

	private ApplicationConfig() {
		//
	}

	public static ApplicationConfig getInstance() {
		return INSTANCE;
	}

	public synchronized void loadConfig() throws Exception {

		LOG.info("Started loading application config data from : " + ServicesConstants.PROPERTIES);
		Properties prop = new Properties();

		try (FileReader reader = new FileReader(ServicesConstants.PROP_LOCATION + ServicesConstants.PROPERTIES)) {

			if (reader != null) {
				prop.load(reader);
			}
		}

		configProperties = prop;
		LOG.info("Loading of application config data completed");
	}

	public String getValue(String key) {
		return configProperties.getProperty(key);
	}
}
