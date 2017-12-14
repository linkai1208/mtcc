package com.sten.framework.util;

import java.io.FileInputStream;
import java.io.InputStream;
import java.net.URLDecoder;
import java.util.Properties;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.boot.registry.StandardServiceRegistry;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.cfg.Configuration;
import org.hibernate.internal.SessionFactoryImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by linkai 16-01-05. 2016-09-21 增加计数器, 判断 session 是否有未关闭情况 2017-04-09
 * 拆分记录关闭 session 情况的同步的方法
 */
public class HibernateUtil {

	private static final SessionFactory sessionFactory;
	private static int openSessionCount = 0;
	private static Object lock = new Object();
	private static boolean SYNC = false;

	private static final Logger logger = LoggerFactory
			.getLogger(HibernateUtil.class);

	static {
		try {
			String path = URLDecoder.decode(Thread.currentThread()
					.getContextClassLoader().getResource("config.properties")
					.getPath(), "UTF-8"); // config.properties保存的真实路径
			Properties properties = new Properties();
			InputStream fis = new FileInputStream(path); // config.properties
															// 文件对象，里面有数据库的连接信息，
			properties.load(fis);
			fis.close(); // 关流
			String name = properties.getProperty("user"); // 数据库IP(从config.properties读)
			String pass = properties.getProperty("pass"); // 端口(从config.properties读)
			String url = properties.getProperty("url");
			Properties extraProperties = new Properties();
			extraProperties.setProperty("hibernate.connection.url", url);
			extraProperties.setProperty("hibernate.connection.username", name);
			extraProperties.setProperty("hibernate.connection.password", pass);
			Configuration cfg = new Configuration();
			cfg.addProperties(extraProperties);
			cfg.configure("hibernate.cfg.xml"); // 路径可以改变
			// sessionFactory = cfg.buildSessionFactory();

			// Configuration cfg = new
			// Configuration().configure("hibernate.cfg.xml");
			StandardServiceRegistryBuilder sb = new StandardServiceRegistryBuilder();
			sb.applySettings(cfg.getProperties());
			StandardServiceRegistry standardServiceRegistry = sb.build();
			sessionFactory = cfg.buildSessionFactory(standardServiceRegistry);
		} catch (Throwable th) {
			System.err.println("Enitial SessionFactory creation failed" + th);
			throw new ExceptionInInitializerError(th);
		}
	}

	public static SessionFactory getSessionFactory() {
		return sessionFactory;
	}

	public static String getDialect() {
		// org.hibernate.dialect.MySQLDialect
		return ((SessionFactoryImpl) sessionFactory).getDialect().toString();
	}

	public static Session getSession() {
		if (!SYNC) {
			return sessionFactory.openSession();
		} else {
			return getSyncSession();
		}
	}

	private static Session getSyncSession() {
		synchronized (lock) {
			if (openSessionCount > 0) {
				Throwable ex = new Throwable();
				StackTraceElement[] stackElements = ex.getStackTrace();
				StringBuffer sb = new StringBuffer();
				if (stackElements != null) {
					for (int i = 0; i < stackElements.length; i++) {
						String className = stackElements[i].getClassName();
						// 过滤掉r1
						if (className.indexOf("com.icss.resourceone") > -1)
							continue;

						if (className.indexOf("com.icss") > -1) {
							sb.append(className + "\t"
									+
									// stackElements[i].getFileName() + "\t" +
									stackElements[i].getLineNumber() + "\t"
									+ stackElements[i].getMethodName() + "\r\n");
						}
					}
				}
				logger.info("openHibernateSessionCount=" + openSessionCount
						+ "\r\n" + sb.toString());
			}

			if (openSessionCount < Integer.MAX_VALUE) {
				openSessionCount++;
			}
			return sessionFactory.openSession();
		}
	}

	public static void closeSession(Session session) {
		if (!SYNC) {
			if (session != null) {
				session.close();
			}
		} else {
			closeSyncSessoin(session);
		}
	}

	private static void closeSyncSessoin(Session session) {
		synchronized (lock) {
			if (session != null) {
				session.close();
			}
			if (openSessionCount > 0) {
				openSessionCount--;
			}
		}
	}

	public static int getOpenSessionCount() {
		return openSessionCount;
	}
}