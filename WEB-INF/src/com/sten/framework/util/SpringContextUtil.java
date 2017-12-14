package com.sten.framework.util;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

/**
 * Created by linkai on 16-1-28.
 */
public class SpringContextUtil implements ApplicationContextAware {

	private static ApplicationContext context;

	public void setApplicationContext(ApplicationContext contex)
			throws BeansException {
		this.context = contex;
	}

	public static ApplicationContext getContext() {
		return context;
	}
}
