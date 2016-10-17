package com.zhongyi.hid.service;

import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

import com.zhongyi.hid.util.SystemProperty;


public class SystemContextListener implements ServletContextListener {

	private static SystemProperty systemProperty;
	private static ServletContext servletContext;

	@Override
	public void contextInitialized(ServletContextEvent sce) {
		try {
			servletContext = sce.getServletContext();
			systemProperty = SystemProperty.create(SystemContextListener.class.getClassLoader()
					.getResourceAsStream("system.properties"));
		} catch (Exception ex) {
			throw new RuntimeException(ex);
		}
	}

	@Override
	public void contextDestroyed(ServletContextEvent sce) {

	}

	public static SystemProperty getSystemProperty() {
		return systemProperty;
	}

	public static ServletContext getServletContext() {
		return servletContext;
	}

}
