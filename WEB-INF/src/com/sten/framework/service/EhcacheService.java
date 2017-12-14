package com.sten.framework.service;

import java.util.Date;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Component;

/**
 * Created by linkai on 2017/3/31.
 */
@Component
public class EhcacheService {

	private static final Logger logger = LoggerFactory
			.getLogger(EhcacheService.class);

	// 缓存数据, 第一次会执行输出日志, 第二次访问从缓存中返回数据不会输出日志
	// value 需要与在 ehcache.xml 中配置的名字对应
	// key 作为缓存对象的主键, 需要和参数一致, xxx 作为业务前缀, 避免key重复
	@Cacheable(value = "caches", key = "'xxx'+#userName")
	public String cacheable(String userName) {
		String result = "缓存时间:"
				+ com.sten.framework.util.DateTimeUtil.getDateFormat(
						new Date(), "yyyy-MM-dd HH:mm:ss") + " " + userName;
		logger.info("更新缓存对象, 参数 userName = " + result);
		return result;
	}

	// 更新缓存
	@CachePut(value = "caches", key = "'xxx'+#userName")
	public String cacheput(String userName) {
		String result = "缓存时间:"
				+ com.sten.framework.util.DateTimeUtil.getDateFormat(
						new Date(), "yyyy-MM-dd HH:mm:ss") + " " + userName;
		logger.info("更新缓存对象, 参数 userName = " + result);
		return result;
	}

	// 删除缓存数据
	@CacheEvict(value = "caches", key = "'xxx'+#userName")
	public String cacheevict(String userName) {
		String result = "缓存时间:"
				+ com.sten.framework.util.DateTimeUtil.getDateFormat(
						new Date(), "yyyy-MM-dd HH:mm:ss") + " " + userName;
		logger.info("更新缓存对象, 参数 userName = " + result);
		return result;
	}
}
