package com.sten.framework.util;

import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.util.Properties;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 
 * @description 配置文件加载
 * @author linkai
 * @date 2016-07-02
 * @time 下午10:53:27
 * 
 *       调用方式 PropertiesUtil.getProperty("key"); fix 中文乱码问题
 */
public class PropertiesUtil {
	private static final Logger logger = LoggerFactory
			.getLogger(PropertiesUtil.class);
	private static Properties properties;

	public static void loadconfig() {
		logger.info("常用配置信息开始加载...");
		InputStream in = null;
		try {
			in = PropertiesUtil.class.getClassLoader().getResourceAsStream(
					"globals.properties");
			properties = new Properties();
			properties.load(in);
			logger.info("配置文件加载成功。");
		} catch (Exception e) {
			e.printStackTrace();
			logger.error(e.getMessage());
			logger.error("加载配置文件异常。", e);
		} finally {
			if (in != null) {
				try {
					in.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}

	public static String getProperty(String key) {
		if (null == properties) {
			loadconfig();
		}
		String value = properties.getProperty(key);
		try {
			value = new String(value.getBytes("ISO8859-1"), "UTF-8");
		} catch (UnsupportedEncodingException e) {
			value = "";
			logger.error("读取" + key + "属性错误:" + e.toString());
			e.printStackTrace();
		}
		return value;
	}
}
