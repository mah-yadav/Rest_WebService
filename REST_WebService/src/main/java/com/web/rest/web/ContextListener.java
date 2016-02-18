package com.web.rest.web;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

public class ContextListener implements ServletContextListener {

	@Override
	public void contextDestroyed(ServletContextEvent arg0) {
		try {
			ApplicationConfig applicationConfig = ApplicationConfig.getInstance();
			applicationConfig.loadConfig();
		} catch (Exception e) {
			System.out.println("Some issue has occurred while loading application config, so destroying context");
			throw new RuntimeException(e.getMessage(), e.getCause());
		}

	}

	@Override
	public void contextInitialized(ServletContextEvent arg0) {
		// TODO Auto-generated method stub
	}

}
