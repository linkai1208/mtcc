package com.sten.framework.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Service;

/**
 * Created by linkai on 16/7/15.
 */
@Service
public class ReceivedCustomEventService implements
		ApplicationListener<com.sten.framework.event.CustomEvent> {

	private static final Logger logger = LoggerFactory
			.getLogger(ReceivedCustomEventService.class);

	public void onApplicationEvent(com.sten.framework.event.CustomEvent event) {
		logger.debug("接收到自定义消息:" + event.getValue());
	}
}
