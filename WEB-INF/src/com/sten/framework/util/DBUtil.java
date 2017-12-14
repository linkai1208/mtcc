package com.sten.framework.util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.sql.DataSource;

import org.apache.commons.collections4.MapUtils;
import org.apache.commons.dbutils.BasicRowProcessor;
import org.apache.commons.dbutils.BeanProcessor;
import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.handlers.ArrayHandler;
import org.apache.commons.dbutils.handlers.ArrayListHandler;
import org.apache.commons.dbutils.handlers.BeanHandler;
import org.apache.commons.dbutils.handlers.BeanListHandler;
import org.apache.commons.dbutils.handlers.ColumnListHandler;
import org.apache.commons.dbutils.handlers.KeyedHandler;
import org.apache.commons.dbutils.handlers.MapHandler;
import org.apache.commons.dbutils.handlers.MapListHandler;
import org.apache.commons.dbutils.handlers.ScalarHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by linkai on 16/6/20.
 * 
 * 2016-07-09 增加 openConn 方法, 可以通过 Context 中获取数据库连接
 * 
 */
public class DBUtil {

	public static String DRIVER_ORACLE = "oracle.jdbc.driver.OracleDriver";
	public static String DRIVER_MYSQL = "com.mysql.jdbc.Driver";

	private static final QueryRunner runner = new QueryRunner();
	private static int openSessionCount = 0;
	private static Object lock = new Object();

	private static final Logger logger = LoggerFactory
			.getLogger(com.sten.framework.util.DBUtil.class);
	private static Map<String, String> map = new HashMap<String, String>();

	/**
	 * @param driver
	 *            oracle.jdbc.driver.OracleDriver com.mysql.jdbc.Driver
	 * 
	 * @param url
	 *            jdbc:mysql://localhost:3306/weixin
	 *            jdbc:oracle:thin:@10.6.18.46:1521:ORCL
	 * @param user
	 *            username
	 * @param password
	 *            password
	 * @return
	 */
	public static Connection openConn(String driver, String url, String user,
			String password) {
		Connection conn = null;

		synchronized (lock) {
			StringBuffer sb = new StringBuffer();

			if (openSessionCount > 0) {
				Throwable ex = new Throwable();
				StackTraceElement[] stackElements = ex.getStackTrace();

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
				logger.info("openDBUtilsSessionCount=" + openSessionCount
						+ "\r\n" + sb.toString());
			}

			try {
				Class.forName(driver);
				conn = DriverManager.getConnection(url, user, password);
				if (openSessionCount > 0) {
					map.put(conn.toString(), sb.toString());
				}
			} catch (Exception e) {
				e.printStackTrace();
				logger.error("openConn Exception:" + e.toString());
			}

			if (openSessionCount < Integer.MAX_VALUE) {
				openSessionCount++;
			}

			return conn;
		}

		// try {
		// Class.forName(driver);
		// conn = DriverManager.getConnection(url, username, password);
		// } catch (Exception e) {
		// e.printStackTrace();
		// }
		// return conn;
	}

	/**
	 * 通过JNDI方式打开数据库链接
	 * 
	 * @param driver
	 *            oracle.jdbc.driver.OracleDriver com.mysql.jdbc.Driver
	 * 
	 * @param resourceName
	 *            Context 中配置的名字 tomcat java:comp/env/jdbc/OEAS weblogic
	 *            jdbc/OEAS
	 * @return
	 */
	public static Connection openConn(String driver, String resourceName) {
		Connection conn = null;
		try {
			Class.forName(driver);
			Context context = new InitialContext();
			DataSource ds = (DataSource) context.lookup(resourceName);
			conn = ds.getConnection();
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("openConn Exception:" + e.toString());
		}
		return conn;
	}

	// 关闭数据库连接
	public static void closeConn(Connection conn) {

		synchronized (lock) {
			try {
				if (conn != null) {
					conn.close();
					map.remove(conn.toString());

					if (map.size() > 0) {
						StringBuffer sb = new StringBuffer();
						for (int i = 0; i < map.size(); i++) {
							sb.append("----------\r\n"
									+ map.values().toArray()[i]);
						}
						logger.info("可能未关闭的DBUtils数据库连接=" + map.size() + "\r\n"
								+ sb.toString() + "可能未关闭的DBUtils数据库连接 end");
					}
				}
				if (openSessionCount > 0) {
					openSessionCount--;
				}
			} catch (Exception e) {
				e.printStackTrace();
				logger.error("closeConn Exception:" + e.toString());
			}
		}

		// try {
		// if (conn != null) {
		// conn.close();
		// }
		// } catch (Exception e) {
		// e.printStackTrace();
		// }
	}

	public static int getOpenSessionCount() {
		return openSessionCount;
	}

	// 查询（返回Array结果）
	public static Object[] queryArray(Connection conn, String sql) {
		Object[] result = null;
		try {
			result = runner.query(conn, sql, new ArrayHandler());
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return result;
	}

	// 查询（返回Array结果）
	public static Object[] queryArray(Connection conn, String sql, Object params) {
		Object[] result = null;
		try {
			result = runner.query(conn, sql, new ArrayHandler(), params);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return result;
	}

	// 查询（返回ArrayList结果）
	public static List<Object[]> queryArrayList(Connection conn, String sql) {
		List<Object[]> result = null;
		try {
			result = runner.query(conn, sql, new ArrayListHandler());
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return result;
	}

	// 查询（返回ArrayList结果）
	public static List<Object[]> queryArrayList(Connection conn, String sql,
			Object params) {
		List<Object[]> result = null;
		try {
			result = runner.query(conn, sql, new ArrayListHandler(), params);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return result;
	}

	// 查询（返回ArrayList结果）
	public static List<Object[]> queryArrayList(Connection conn, String sql,
			Object[] params) {
		List<Object[]> result = null;
		try {
			result = runner.query(conn, sql, new ArrayListHandler(), params);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return result;
	}

	// 查询（返回Map结果）
	public static Map<String, Object> queryMap(Connection conn, String sql) {
		Map<String, Object> result = null;
		try {
			result = runner.query(conn, sql, new MapHandler());
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return result;
	}

	// 查询（返回Map结果）
	public static Map<String, Object> queryMap(Connection conn, String sql,
			Object params) {
		Map<String, Object> result = null;
		try {
			result = runner.query(conn, sql, new MapHandler(), params);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return result;
	}

	// 查询（返回MapList结果）
	public static List<Map<String, Object>> queryMapList(Connection conn,
			String sql) {
		List<Map<String, Object>> result = null;
		try {
			result = runner.query(conn, sql, new MapListHandler());
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return result;
	}

	// 查询（返回MapList结果）
	public static List<Map<String, Object>> queryMapList(Connection conn,
			String sql, Object params) {
		List<Map<String, Object>> result = null;
		try {
			result = runner.query(conn, sql, new MapListHandler(), params);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return result;
	}

	// 查询（返回Bean结果）
	public static <T> T queryBean(Class<T> cls, Map<String, String> map,
			Connection conn, String sql) {
		T result = null;
		try {
			if (MapUtils.isNotEmpty(map)) {
				result = runner.query(conn, sql, new BeanHandler<T>(cls,
						new BasicRowProcessor(new BeanProcessor(map))));
			} else {
				result = runner.query(conn, sql, new BeanHandler<T>(cls));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return result;
	}

	// 查询（返回Bean结果）
	public static <T> T queryBean(Class<T> cls, Map<String, String> map,
			Connection conn, String sql, Object params) {
		T result = null;
		try {
			if (MapUtils.isNotEmpty(map)) {
				result = runner.query(conn, sql, new BeanHandler<T>(cls,
						new BasicRowProcessor(new BeanProcessor(map))), params);
			} else {
				result = runner.query(conn, sql, new BeanHandler<T>(cls),
						params);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return result;
	}

	// 查询（返回BeanList结果）
	public static <T> List<T> queryBeanList(Class<T> cls,
			Map<String, String> map, Connection conn, String sql) {
		List<T> result = null;
		try {
			if (MapUtils.isNotEmpty(map)) {
				result = runner.query(conn, sql, new BeanListHandler<T>(cls,
						new BasicRowProcessor(new BeanProcessor(map))));
			} else {
				result = runner.query(conn, sql, new BeanListHandler<T>(cls));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return result;
	}

	// 查询（返回BeanList结果）
	public static <T> List<T> queryBeanList(Class<T> cls,
			Map<String, String> map, Connection conn, String sql, Object params) {
		List<T> result = null;
		try {
			if (MapUtils.isNotEmpty(map)) {
				result = runner.query(conn, sql, new BeanListHandler<T>(cls,
						new BasicRowProcessor(new BeanProcessor(map))), params);
			} else {
				result = runner.query(conn, sql, new BeanListHandler<T>(cls),
						params);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return result;
	}

	// 查询指定列名的值（单条数据）
	public static <T> T queryColumn(String column, Connection conn, String sql) {
		T result = null;
		try {
			result = runner.query(conn, sql, new ScalarHandler<T>(column));
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return result;
	}

	// 查询指定列名的值（单条数据）
	public static <T> T queryColumn(String column, Connection conn, String sql,
			Object params) {
		T result = null;
		try {
			result = runner.query(conn, sql, new ScalarHandler<T>(column),
					params);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return result;
	}

	// 查询指定列名的值（多条数据）
	public static <T> List<T> queryColumnList(String column, Connection conn,
			String sql) {
		List<T> result = null;
		try {
			result = runner.query(conn, sql, new ColumnListHandler<T>(column));
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return result;
	}

	// 查询指定列名的值（多条数据）
	public static <T> List<T> queryColumnList(String column, Connection conn,
			String sql, Object params) {
		List<T> result = null;
		try {
			result = runner.query(conn, sql, new ColumnListHandler<T>(column),
					params);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return result;
	}

	// 查询指定列名对应的记录映射
	public static <T> Map<T, Map<String, Object>> queryKeyMap(String column,
			Connection conn, String sql) {
		Map<T, Map<String, Object>> result = null;
		try {
			result = runner.query(conn, sql, new KeyedHandler<T>(column));
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return result;
	}

	// 查询指定列名对应的记录映射
	public static <T> Map<T, Map<String, Object>> queryKeyMap(String column,
			Connection conn, String sql, Object params) {
		Map<T, Map<String, Object>> result = null;
		try {
			result = runner.query(conn, sql, new KeyedHandler<T>(column),
					params);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return result;
	}

	// 更新单条（包括UPDATE、INSERT、DELETE，返回受影响的行数）
	public static int update(Connection conn, String sql) {
		int result = 0;
		try {
			result = runner.update(conn, sql);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return result;
	}

	// 更新单条（包括UPDATE、INSERT、DELETE，返回受影响的行数）
	public static int update(Connection conn, String sql, String params) {
		int result = 0;
		try {
			result = runner.update(conn, sql, params);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return result;
	}

	// 更新单条（包括UPDATE、INSERT、DELETE，返回受影响的行数）
	public static int update(Connection conn, String sql, Object[] params) {
		int result = 0;
		try {
			result = runner.update(conn, sql, params);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return result;
	}

	// 更新批量（包括UPDATE、INSERT、DELETE，返回受影响的行数）
	// 第一维表示更新记录条数, 第二维是参数数据, 数量与参数 ? 匹配
	public static int[] update(Connection conn, String sql, Object[][] params) {
		int[] result = new int[] {};
		try {
			result = runner.batch(conn, sql, params);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return result;
	}
}
