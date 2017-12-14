package com.sten.framework.service;

import java.util.Date;

import javax.annotation.PostConstruct;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.sten.framework.common.Constants;

/**
 * Created by linkai on 16-5-11.
 */
@Component
public class TaskService {

	private static final Logger logger = LoggerFactory
			.getLogger(TaskService.class);

	// #每天12点触发
	// 0 0 12 * * ?
	// #每十秒执行一次
	// EXPORT_BASIC_FOLDER_TIME = 0/10 * * * * ?
	// #每隔一分钟执行一次
	// #EXPORT_BASIC_FOLDER_TIME = 0 0/1 * * * ?
	// #每5分钟执行一次
	// #EXPORT_BASIC_FOLDER_TIME = 0 0/5 * * * ?
	// #每30分钟执行一次
	// #EXPORT_BASIC_FOLDER_TIME = 0 0/30 * * * ?
	// #每天执行一次
	// #EXPORT_BASIC_FOLDER_TIME = 0 0 0 0/1 * ?

	@Scheduled(cron = "0/180 * * * * ? ")
	// Cron表达式 间隔180秒执行
	public void run() {
		logger.info("TaskService executing at " + new Date());
	}

	@PostConstruct
	public void initStatic() {
		// 系统初始化自动执行
		logger.info("TaskService initStatic at " + new Date());
		// 需要配置 web.xml
		// <listener>
		// <listener-class>org.springframework.web.context.request.RequestContextListener</listener-class>
		// </listener>
		// ServletContext context =
		// ContextLoader.getCurrentWebApplicationContext().getServletContext();
		// HttpServletRequest request = ((ServletRequestAttributes)
		// RequestContextHolder.getRequestAttributes()).getRequest();
		// Constants.CONTEXT_PATH = request.getContextPath();
		logger.info("Constants.CONTEXT_PATH = " + Constants.CONTEXT_PATH);

	}

	/*
	 * @Scheduled(cron="0 0/1 * * * ?") public void send(){
	 * userService.checkSend(); }
	 */
}
