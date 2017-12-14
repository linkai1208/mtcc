package com.sten.framework.service;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.criterion.CriteriaSpecification;
import org.hibernate.transform.Transformers;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.sten.framework.common.Constants;
import com.sten.framework.common.Page;
import com.sten.framework.common.Pagination;
import com.sten.framework.util.HibernateUtil;

/**
 * Created by linkai on 16-4-14. 2016-05-12 增加基于Page的分页查询
 * queryPageByHql\queryPageBySql 2016-06-28 param 增加list类型参数、 增加 queryOne、
 * queryByHql、queryBySql 没参数的版本、只有一个参数的版本 修正 queryOne 在结果不唯一情况下的异常 2016-07-20 删除
 * executeUpdate 方法, 修正 updateByHql、 updateBySql 失败的bug 2016-09-20 增加
 * deleteBySql 2016-10-20 条件参数增加 in 语句支持, Map<String, Object> param 中 Object 使用
 * String[] 类型保存 in 语句中的数据 2016-12-06 bySql相关函数返回值类型调整为泛型对象<T>, 获取单个对象方法名用get表示
 * 增、删、该方法增加 Session 重载函数, 支持方法外面统一处理事务 2016-12-13 修正 queryPageBySql count 计算
 * bug 2017-01-17 调整批量函数 add、update 参数 List<Object> 为 List 统一修正 count 计算方法
 * 2017-03-16 调整参数 Object parameterValue 支持 Stirng[] 类型 2017-05-29
 * 修正分页页码使用常量，而不是通过前台传递过来的bug
 */
@Service("baseService")
public class BaseService {

	private static final Logger logger = LoggerFactory
			.getLogger(BaseService.class);

	/**
	 * 根据主键获取一个对象
	 * 
	 * @param c
	 *            对象类
	 * @param id
	 *            主键
	 * @return
	 */
	public static Object get(Class c, String id) {
		Session session = null;
		Object object = null;
		try {
			session = HibernateUtil.getSession();
			object = session.get(c, id);
		} catch (Exception ex) {
			logger.error(ex.toString());
			ex.printStackTrace();
		} finally {
			HibernateUtil.closeSession(session);
		}
		return object;
	}

	/**
	 * 新增数据
	 * 
	 * @param object
	 *            要新增的对象
	 * @return
	 */
	public static void add(Object object, Session session) {
		session.save(object);
	}

	/**
	 * 新增数据
	 * 
	 * @param object
	 *            要新增的对象
	 * @return
	 */
	public static boolean add(Object object) {
		Session session = null;
		Transaction ts = null;
		boolean result = false;
		try {
			session = HibernateUtil.getSession();
			ts = session.beginTransaction();
			session.save(object);
			ts.commit();
			result = true;
		} catch (Exception ex) {
			logger.error(ex.toString());
			if (ts != null) {
				ts.rollback();
			}
			ex.printStackTrace();
		} finally {
			HibernateUtil.closeSession(session);
		}
		return result;
	}

	/**
	 * 批量新增数据
	 * 
	 * @param list
	 *            批量要新增的对象
	 * @return
	 */
	public static void add(List list, Session session) {
		for (Object object : list) {
			session.save(object);
		}
	}

	/**
	 * 批量新增数据
	 * 
	 * @param list
	 *            批量要新增的对象
	 * @return
	 */
	public static boolean add(List list) {
		Session session = null;
		Transaction ts = null;
		boolean result = false;
		try {
			session = HibernateUtil.getSession();
			ts = session.beginTransaction();
			for (Object object : list) {
				session.save(object);
			}

			ts.commit();
			result = true;
		} catch (Exception ex) {
			logger.error(ex.toString());
			if (ts != null) {
				ts.rollback();
			}
			ex.printStackTrace();
		} finally {
			HibernateUtil.closeSession(session);
		}
		return result;
	}

	/**
	 * 更新数据
	 * 
	 * @param object
	 *            要更新的对象
	 * @return
	 */
	public static void update(Object object, Session session) {
		session.update(object);
	}

	/**
	 * 更新数据
	 * 
	 * @param object
	 *            要更新的对象
	 * @return
	 */
	public static boolean update(Object object) {
		Session session = null;
		Transaction ts = null;
		boolean result = false;
		try {
			session = HibernateUtil.getSession();
			ts = session.beginTransaction();
			session.update(object);
			ts.commit();
			result = true;
		} catch (Exception ex) {
			logger.error(ex.toString());
			if (ts != null) {
				ts.rollback();
			}
			ex.printStackTrace();
		} finally {
			HibernateUtil.closeSession(session);
		}
		return result;
	}

	/**
	 * 批量更新数据
	 * 
	 * @param list
	 *            批量要更新的对象
	 * @return
	 */
	public static void update(List list, Session session) {
		for (Object object : list) {
			session.update(object);
		}
	}

	/**
	 * 批量更新数据
	 * 
	 * @param list
	 *            批量要更新的对象
	 * @return
	 */
	public static boolean update(List list) {
		Session session = null;
		Transaction ts = null;
		boolean result = false;
		try {
			session = HibernateUtil.getSession();
			ts = session.beginTransaction();
			for (Object object : list) {
				session.update(object);
			}
			ts.commit();
			result = true;
		} catch (Exception ex) {
			logger.error(ex.toString());
			if (ts != null) {
				ts.rollback();
			}
			ex.printStackTrace();
		} finally {
			HibernateUtil.closeSession(session);
		}
		return result;
	}

	/**
	 * 更新数据
	 * 
	 * @param hql
	 *            执行的hql语句
	 * @return
	 */
	public static int updateByHql(String hql, String parameterName,
			Object parameterValue) {
		Session session = null;
		Transaction ts = null;
		int count = 0;
		try {
			session = HibernateUtil.getSession();
			ts = session.beginTransaction();
			Query query = session.createQuery(hql);
			if ("[Ljava.lang.String;".equals(parameterValue.getClass()
					.getName())) {
				query.setParameterList(parameterName, (String[]) parameterValue);
			} else {
				query.setParameter(parameterName, parameterValue);
			}
			count = query.executeUpdate();
			ts.commit();
		} catch (Exception ex) {
			logger.error(ex.toString());
			logger.error("hql: {}", hql);
			logger.error("parameterValue: {}", parameterValue);
			if (ts != null) {
				ts.rollback();
			}
			ex.printStackTrace();
		} finally {
			HibernateUtil.closeSession(session);
		}
		return count;
	}

	/**
	 * 更新数据
	 * 
	 * @param hql
	 *            执行的hql语句
	 * @return
	 */
	public static int updateByHql(String hql, String parameterName,
			Object parameterValue, Session session) {
		int count = 0;
		Query query = session.createQuery(hql);
		// String[]
		if ("[Ljava.lang.String;".equals(parameterValue.getClass().getName())) {
			query.setParameterList(parameterName, (String[]) parameterValue);
		} else {
			query.setParameter(parameterName, parameterValue);
		}
		count = query.executeUpdate();

		return count;
	}

	/**
	 * 更新数据
	 * 
	 * @param hql
	 *            执行的hql语句
	 * @param param
	 *            传参
	 * @return
	 */
	public static int updateByHql(String hql, Map<String, Object> param,
			Session session) {
		int count = 0;
		Query query = session.createQuery(hql);

		Iterator iterator = param.keySet().iterator();
		while (iterator.hasNext()) {
			String key = iterator.next().toString();
			Object value = param.get(key);
			// String[]
			if ("[Ljava.lang.String;".equals(value.getClass().getName())) {
				query.setParameterList(key, (String[]) value);
			} else {
				query.setParameter(key, value);
			}
		}
		count = query.executeUpdate();

		return count;
	}

	/**
	 * 更新数据
	 * 
	 * @param hql
	 *            执行的hql语句
	 * @param param
	 *            传参
	 * @return
	 */
	public static int updateByHql(String hql, Map<String, Object> param) {
		int count = 0;

		Session session = null;
		Transaction ts = null;
		try {
			session = HibernateUtil.getSession();
			ts = session.beginTransaction();
			Query query = session.createQuery(hql);

			Iterator iterator = param.keySet().iterator();
			while (iterator.hasNext()) {
				String key = iterator.next().toString();
				Object value = param.get(key);
				// String[]
				if ("[Ljava.lang.String;".equals(value.getClass().getName())) {
					query.setParameterList(key, (String[]) value);
				} else {
					query.setParameter(key, value);
				}
			}
			count = query.executeUpdate();
			ts.commit();
		} catch (Exception ex) {
			logger.error(ex.toString());
			logger.error("hql: {}", hql);
			logger.error("param: {}", param);
			if (ts != null) {
				ts.rollback();
			}
			ex.printStackTrace();
		} finally {
			HibernateUtil.closeSession(session);
		}
		return count;
	}

	/**
	 * 执行sql语句更新数据
	 * 
	 * @param sql
	 * @param param
	 *            参数 String 为参数名 Object 为参数值, 参数值必须转换为对象属性匹配的数据类型
	 * @return 返回成功操作后的影响记录数量
	 */
	public static int updateBySql(String sql, Map<String, Object> param,
			Session session) {
		int count = 0;
		Query query = session.createSQLQuery(sql);

		Iterator iterator = param.keySet().iterator();
		while (iterator.hasNext()) {
			String key = iterator.next().toString();
			Object value = param.get(key);
			// String[]
			if ("[Ljava.lang.String;".equals(value.getClass().getName())) {
				query.setParameterList(key, (String[]) value);
			} else {
				query.setParameter(key, value);
			}
		}
		count = query.executeUpdate();

		return count;
	}

	/**
	 * 执行sql语句更新数据
	 * 
	 * @param sql
	 * @param param
	 *            参数 String 为参数名 Object 为参数值, 参数值必须转换为对象属性匹配的数据类型
	 * @return 返回成功操作后的影响记录数量
	 */
	public static int updateBySql(String sql, Map<String, Object> param) {
		int count = 0;

		Session session = null;
		Transaction ts = null;
		try {
			session = HibernateUtil.getSession();
			ts = session.beginTransaction();
			Query query = session.createSQLQuery(sql);

			Iterator iterator = param.keySet().iterator();
			while (iterator.hasNext()) {
				String key = iterator.next().toString();
				Object value = param.get(key);
				// String[]
				if ("[Ljava.lang.String;".equals(value.getClass().getName())) {
					query.setParameterList(key, (String[]) value);
				} else {
					query.setParameter(key, value);
				}
			}
			count = query.executeUpdate();
			ts.commit();
		} catch (Exception ex) {
			logger.error(ex.toString());
			logger.error("sql: {}", sql);
			logger.error("param: {}", param);
			if (ts != null) {
				ts.rollback();
			}
			ex.printStackTrace();
		} finally {
			HibernateUtil.closeSession(session);
		}
		return count;
	}

	/**
	 * 执行sql语句更新数据
	 * 
	 * @param sql
	 * @return 返回成功操作后的影响记录数量
	 */
	public static int updateBySql(String sql, Session session) {
		int count = 0;

		Query query = session.createSQLQuery(sql);
		count = query.executeUpdate();

		return count;
	}

	/**
	 * 执行sql语句更新数据
	 * 
	 * @param sql
	 * @return 返回成功操作后的影响记录数量
	 */
	public static int updateBySql(String sql) {
		int count = 0;

		Session session = null;
		Transaction ts = null;
		try {
			session = HibernateUtil.getSession();
			ts = session.beginTransaction();
			Query query = session.createSQLQuery(sql);
			count = query.executeUpdate();
			ts.commit();
		} catch (Exception ex) {
			logger.error(ex.toString());
			if (ts != null) {
				ts.rollback();
			}
			ex.printStackTrace();
		} finally {
			HibernateUtil.closeSession(session);
		}
		return count;
	}

	/**
	 * 删除对象
	 * 
	 * @param object
	 *            要删除的对象
	 * @return
	 */
	public static void delete(Object object, Session session) {
		session.delete(object);
	}

	/**
	 * 删除对象
	 * 
	 * @param object
	 *            要删除的对象
	 * @return
	 */
	public static boolean delete(Object object) {
		Session session = null;
		Transaction ts = null;
		boolean result = false;
		try {
			session = HibernateUtil.getSession();
			ts = session.beginTransaction();
			session.delete(object);
			ts.commit();
			result = true;
		} catch (Exception ex) {
			logger.error(ex.toString());
			if (ts != null) {
				ts.rollback();
			}
			ex.printStackTrace();
		} finally {
			HibernateUtil.closeSession(session);
		}
		return result;
	}

	/**
	 * 批量删除对象
	 * 
	 * @param list
	 *            批量要删除的对象
	 * @return
	 */
	public static void delete(List list, Session session) {
		for (Object object : list) {
			session.delete(object);
		}
	}

	/**
	 * 批量删除对象
	 * 
	 * @param list
	 *            批量要删除的对象
	 * @return
	 */
	public static boolean delete(List list) {
		Session session = null;
		Transaction ts = null;
		boolean result = false;
		try {
			session = HibernateUtil.getSession();
			ts = session.beginTransaction();
			for (Object object : list) {
				session.delete(object);
			}

			ts.commit();
			result = true;
		} catch (Exception ex) {
			logger.error(ex.toString());
			if (ts != null) {
				ts.rollback();
			}
			ex.printStackTrace();
		} finally {
			HibernateUtil.closeSession(session);
		}
		return result;
	}

	/**
	 * 根据主键删除数据
	 * 
	 * @param c
	 *            对象类
	 * @param id
	 *            要删除数据的主键
	 * @return
	 */
	public static void delete(Class c, String id, Session session) {
		Object object = session.get(c, id);
		session.delete(object);
	}

	/**
	 * 根据主键删除数据
	 * 
	 * @param c
	 *            对象类
	 * @param id
	 *            要删除数据的主键
	 * @return
	 */
	public static boolean delete(Class c, String id) {
		Session session = null;
		Transaction ts = null;
		boolean result = false;
		try {
			session = HibernateUtil.getSession();
			ts = session.beginTransaction();
			Object object = session.get(c, id);
			session.delete(object);
			ts.commit();
			result = true;
		} catch (Exception ex) {
			logger.error(ex.toString());
			if (ts != null) {
				ts.rollback();
			}
			ex.printStackTrace();
		} finally {
			HibernateUtil.closeSession(session);
		}
		return result;
	}

	/**
	 * 执行sql语句删除数据
	 * 
	 * @param sql
	 * @return 返回成功操作后的影响记录数量
	 */
	public static int deleteBySql(String sql, Session session) {
		return updateBySql(sql, session);
	}

	/**
	 * 执行sql语句删除数据
	 * 
	 * @param sql
	 * @return 返回成功操作后的影响记录数量
	 */
	public static int deleteBySql(String sql) {
		return updateBySql(sql);
	}

	/**
	 * 执行sql语句删除数据
	 * 
	 * @param sql
	 * @return 返回成功操作后的影响记录数量
	 */
	public static int deleteBySql(String sql, String parameterName,
			Object parameterValue, Session session) {
		Map<String, Object> param = new HashMap<String, Object>();
		param.put(parameterName, parameterValue);
		return updateBySql(sql, param, session);
	}

	/**
	 * 执行sql语句删除数据
	 * 
	 * @param sql
	 * @return 返回成功操作后的影响记录数量
	 */
	public static int deleteBySql(String sql, String parameterName,
			Object parameterValue) {
		Map<String, Object> param = new HashMap<String, Object>();
		param.put(parameterName, parameterValue);
		return updateBySql(sql, param);
	}

	/**
	 * 执行sql语句删除数据
	 * 
	 * @param sql
	 * @param param
	 *            参数 String 为参数名 Object 为参数值, 参数值必须转换为对象属性匹配的数据类型
	 * @return 返回成功操作后的影响记录数量
	 */
	public static int deleteBySql(String sql, Map<String, Object> param,
			Session session) {
		return updateBySql(sql, param, session);
	}

	/**
	 * 执行sql语句删除数据
	 * 
	 * @param sql
	 * @param param
	 *            参数 String 为参数名 Object 为参数值, 参数值必须转换为对象属性匹配的数据类型
	 * @return 返回成功操作后的影响记录数量
	 */
	public static int deleteBySql(String sql, Map<String, Object> param) {
		return updateBySql(sql, param);
	}

	/**
	 * 查询多条记录
	 * 
	 * @param <T>
	 * @param hql
	 *            hql语句
	 * @param param
	 *            参数 String 为参数名 Object 为参数值, 参数值必须转换为对象属性匹配的数据类型
	 * @return
	 */
	public static <T> List<T> queryByHql(String hql, Map<String, Object> param) {

		List<T> list = new ArrayList<T>();
		Session session = null;
		try {
			session = HibernateUtil.getSession();
			Query query = session.createQuery(hql);

			Iterator iterator = param.keySet().iterator();
			while (iterator.hasNext()) {
				String key = iterator.next().toString();
				Object value = param.get(key);
				// String[]
				if ("[Ljava.lang.String;".equals(value.getClass().getName())) {
					query.setParameterList(key, (String[]) value);
				} else {
					query.setParameter(key, value);
				}
			}
			list = query.list();
		} catch (Exception ex) {
			logger.error(ex.toString());
			logger.error("hql: {}", hql);
			logger.error("param: {}", param);
			ex.printStackTrace();
		} finally {
			HibernateUtil.closeSession(session);
		}
		return list;
	}

	/**
	 * 查询多条记录
	 * 
	 * @param <T>
	 * @param hql
	 *            hql语句
	 * @param parameterName
	 *            参数名
	 * @param parameterValue
	 *            参数值必须转换为对象属性匹配的数据类型
	 * @return
	 */
	public static <T> List<T> queryByHql(String hql, String parameterName,
			Object parameterValue) {

		List<T> list = new ArrayList<T>();
		Session session = null;
		try {
			session = HibernateUtil.getSession();
			Query query = session.createQuery(hql);
			if ("[Ljava.lang.String;".equals(parameterValue.getClass()
					.getName())) {
				query.setParameterList(parameterName, (String[]) parameterValue);
			} else {
				query.setParameter(parameterName, parameterValue);
			}
			list = query.list();
		} catch (Exception ex) {
			logger.error(ex.toString());
			logger.error("hql: {}", hql);
			logger.error("parameterValue: {}", parameterValue);
			ex.printStackTrace();
		} finally {
			HibernateUtil.closeSession(session);
		}
		return list;
	}

	/**
	 * 查询多条记录
	 * 
	 * @param <T>
	 * @param hql
	 *            hql语句
	 * @return
	 */
	public static <T> List<T> queryByHql(String hql) {

		List<T> list = new ArrayList<T>();
		Session session = null;
		try {
			session = HibernateUtil.getSession();
			Query query = session.createQuery(hql);
			list = query.list();
		} catch (Exception ex) {
			logger.error(ex.toString());
			logger.error("hql: {}", hql);
			ex.printStackTrace();
		} finally {
			HibernateUtil.closeSession(session);
		}
		return list;
	}

	/**
	 * 查询多条记录
	 * 
	 * @param sql
	 *            sql语句
	 * @param param
	 *            参数 String 为参数名 Object 为参数值, 参数值必须转换为对象属性匹配的数据类型
	 * @param entityType
	 *            返回对象类型
	 * @return
	 */
	public static <T> List<T> queryBySql(String sql, Map<String, Object> param,
			Class entityType) {

		List<T> list = new ArrayList<T>();
		Session session = null;
		try {
			session = HibernateUtil.getSession();
			Query query = session.createSQLQuery(sql).addEntity(entityType);

			Iterator iterator = param.keySet().iterator();
			while (iterator.hasNext()) {
				String key = iterator.next().toString();
				Object value = param.get(key);
				// String[]
				if ("[Ljava.lang.String;".equals(value.getClass().getName())) {
					query.setParameterList(key, (String[]) value);
				} else {
					query.setParameter(key, value);
				}
			}
			list = query.list();
		} catch (Exception ex) {
			logger.error(ex.toString());
			logger.error("sql: {}", sql);
			logger.error("param: {}", param);
			ex.printStackTrace();
		} finally {
			HibernateUtil.closeSession(session);
		}
		return list;
	}

	/**
	 * 查询多条记录
	 * 
	 * @param sql
	 *            sql语句
	 * @param param
	 *            参数 String 为参数名 Object 为参数值, 参数值必须转换为对象属性匹配的数据类型
	 * @return
	 */
	public static <T> List<T> queryBySql(String sql, Map<String, Object> param) {

		List<T> list = new ArrayList<T>();
		Session session = null;
		try {
			session = HibernateUtil.getSession();
			Query query = session.createSQLQuery(sql);

			Iterator iterator = param.keySet().iterator();
			while (iterator.hasNext()) {
				String key = iterator.next().toString();
				Object value = param.get(key);
				// String[]
				if ("[Ljava.lang.String;".equals(value.getClass().getName())) {
					query.setParameterList(key, (String[]) value);
				} else {
					query.setParameter(key, value);
				}
			}
			list = query.list();
		} catch (Exception ex) {
			logger.error(ex.toString());
			logger.error("sql: {}", sql);
			logger.error("sql: {}", sql);
			logger.error("param: {}", param);
			ex.printStackTrace();
		} finally {
			HibernateUtil.closeSession(session);
		}
		return list;
	}

	/**
	 * 查询多条记录
	 * 
	 * @param sql
	 *            sql语句
	 * @param parameterName
	 *            参数名
	 * @param parameterValue
	 *            参数值必须转换为对象属性匹配的数据类型
	 * @param entityType
	 *            返回对象类型
	 * @return
	 */
	public static <T> List<T> queryBySql(String sql, String parameterName,
			Object parameterValue, Class entityType) {

		List<T> list = new ArrayList<T>();
		Session session = null;
		try {
			session = HibernateUtil.getSession();
			Query query = session.createSQLQuery(sql).addEntity(entityType);
			if ("[Ljava.lang.String;".equals(parameterValue.getClass()
					.getName())) {
				query.setParameterList(parameterName, (String[]) parameterValue);
			} else {
				query.setParameter(parameterName, parameterValue);
			}
			list = query.list();
		} catch (Exception ex) {
			logger.error(ex.toString());
			logger.error("sql: {}", sql);
			logger.error("parameterValue: {}", parameterValue);
			ex.printStackTrace();
		} finally {
			HibernateUtil.closeSession(session);
		}
		return list;
	}

	/**
	 * 查询多条记录
	 * 
	 * @param sql
	 *            sql语句
	 * @param parameterName
	 *            参数名
	 * @param parameterValue
	 *            参数值必须转换为对象属性匹配的数据类型
	 * @return
	 */
	public static <T> List<T> queryBySql(String sql, String parameterName,
			Object parameterValue) {

		List<T> list = new ArrayList<T>();
		Session session = null;
		try {
			session = HibernateUtil.getSession();
			Query query = session.createSQLQuery(sql);
			if ("[Ljava.lang.String;".equals(parameterValue.getClass()
					.getName())) {
				query.setParameterList(parameterName, (String[]) parameterValue);
			} else {
				query.setParameter(parameterName, parameterValue);
			}
			list = query.list();
		} catch (Exception ex) {
			logger.error(ex.toString());
			logger.error("sql: {}", sql);
			logger.error("parameterValue: {}", parameterValue);
			ex.printStackTrace();
		} finally {
			HibernateUtil.closeSession(session);
		}
		return list;
	}

	/**
	 * 查询多条记录
	 * 
	 * @param sql
	 *            sql语句
	 * @param entityType
	 *            返回对象类型
	 * @return
	 */
	public static <T> List<T> queryBySql(String sql, Class entityType) {

		List<T> list = new ArrayList<T>();
		Session session = null;
		try {
			session = HibernateUtil.getSession();
			Query query = session.createSQLQuery(sql).addEntity(entityType);
			list = query.list();
		} catch (Exception ex) {
			logger.error(ex.toString());
			logger.error("sql: {}", sql);
			ex.printStackTrace();
		} finally {
			HibernateUtil.closeSession(session);
		}
		return list;
	}

	/**
	 * 查询多条记录
	 * 
	 * @param sql
	 *            sql语句
	 * @return
	 */
	public static <T> List<T> queryBySql(String sql) {

		List<T> list = new ArrayList<T>();
		Session session = null;
		try {
			session = HibernateUtil.getSession();
			Query query = session.createSQLQuery(sql);
			list = query.list();
		} catch (Exception ex) {
			logger.error(ex.toString());
			logger.error("sql: {}", sql);
			ex.printStackTrace();
		} finally {
			HibernateUtil.closeSession(session);
		}
		return list;
	}

	/**
	 * 查询多条记录
	 * 
	 * @param sql
	 *            sql语句
	 * @return
	 */
	public static List<Map> queryMapBySql(String sql) {

		List<Map> list = new ArrayList<Map>();
		Session session = null;
		try {
			session = HibernateUtil.getSession();
			Query query = session.createSQLQuery(sql);
			query.setResultTransformer(CriteriaSpecification.ALIAS_TO_ENTITY_MAP);
			list = query.list();
		} catch (Exception ex) {
			logger.error(ex.toString());
			logger.error("sql: {}", sql);
			ex.printStackTrace();
		} finally {
			HibernateUtil.closeSession(session);
		}
		return list;
	}

	/**
	 * 查询多条记录
	 * 
	 * @param sql
	 *            sql语句
	 * @param parameterName
	 *            参数名
	 * @param parameterValue
	 *            参数值必须转换为对象属性匹配的数据类型
	 * @return
	 */
	public static List<Map> queryMapBySql(String sql, String parameterName,
			Object parameterValue) {

		List<Map> list = new ArrayList<Map>();
		Session session = null;
		try {
			session = HibernateUtil.getSession();
			Query query = session.createSQLQuery(sql);
			if ("[Ljava.lang.String;".equals(parameterValue.getClass()
					.getName())) {
				query.setParameterList(parameterName, (String[]) parameterValue);
			} else {
				query.setParameter(parameterName, parameterValue);
			}
			query.setResultTransformer(CriteriaSpecification.ALIAS_TO_ENTITY_MAP);
			list = query.list();
		} catch (Exception ex) {
			logger.error(ex.toString());
			logger.error("sql: {}", sql);
			logger.error("parameterValue: {}", parameterValue);
			ex.printStackTrace();
		} finally {
			HibernateUtil.closeSession(session);
		}
		return list;
	}

	/**
	 * 查询多条记录
	 * 
	 * @param sql
	 *            sql语句
	 * @param param
	 *            参数 String 为参数名 Object 为参数值, 参数值必须转换为对象属性匹配的数据类型
	 * @return
	 */
	public static List<Map> queryMapBySql(String sql, Map<String, Object> param) {

		List<Map> list = new ArrayList<Map>();
		Session session = null;
		try {
			session = HibernateUtil.getSession();
			Query query = session.createSQLQuery(sql);

			Iterator iterator = param.keySet().iterator();
			while (iterator.hasNext()) {
				String key = iterator.next().toString();
				Object value = param.get(key);
				// String[]
				if ("[Ljava.lang.String;".equals(value.getClass().getName())) {
					query.setParameterList(key, (String[]) value);
				} else {
					query.setParameter(key, value);
				}
			}
			query.setResultTransformer(CriteriaSpecification.ALIAS_TO_ENTITY_MAP);
			list = query.list();
		} catch (Exception ex) {
			logger.error(ex.toString());
			logger.error("sql: {}", sql);
			logger.error("param: {}", param);
			ex.printStackTrace();
		} finally {
			HibernateUtil.closeSession(session);
		}
		return list;
	}

	/**
	 * 分页查询
	 * 
	 * @param sqlCount
	 *            计算总页数hql
	 * @param sqlSelect
	 *            查询数据hql
	 * @param param
	 *            参数 String 为参数名 Object 为参数值, 参数值必须转换为对象属性匹配的数据类型
	 * @param pagination
	 *            分页对象
	 * @return 注意返回值的 rows 为List<Object[]>
	 */
	public static Map<String, Object> queryMapPageBySql(String sqlCount,
			String sqlSelect, Map<String, Object> param, Pagination pagination) {
		Map<String, Object> map = new Hashtable<String, Object>(3);
		List list = null;
		int totalCount = 0;
		Session session = null;
		try {
			session = HibernateUtil.getSession();

			Query queryCount = session.createSQLQuery(sqlCount);
			Query querySelect = session.createSQLQuery(sqlSelect);

			// 为 where 条件中的参数赋值
			Iterator iterator = param.keySet().iterator();
			while (iterator.hasNext()) {
				String key = iterator.next().toString();
				Object value = param.get(key);
				// String[]
				if ("[Ljava.lang.String;".equals(value.getClass().getName())) {
					queryCount.setParameterList(key, (String[]) value);
					querySelect.setParameterList(key, (String[]) value);
				} else {
					queryCount.setParameter(key, value);
					querySelect.setParameter(key, value);
				}
			}

			// 计算总页数
			totalCount = pagination.getClientPageCount();
			// 避免每次都查询总页数
			if (totalCount == 0) {
				// hibernate 4.3.11 oracle11g count 返回的是 BigDecimal mysql count
				// 返回的是 BigInteger
				if (HibernateUtil.getDialect().indexOf("MySQL") > -1) {
					totalCount = ((BigInteger) queryCount.uniqueResult())
							.intValue();
				} else {
					totalCount = ((BigDecimal) queryCount.uniqueResult())
							.intValue();
				}
			}

			// 获取分页处理
			querySelect.setFirstResult((pagination.getPage() - 1)
					* pagination.getRows());
			querySelect.setMaxResults(pagination.getRows());
			querySelect
					.setResultTransformer(CriteriaSpecification.ALIAS_TO_ENTITY_MAP);
			list = querySelect.list();

		} catch (Exception ex) {
			logger.error(ex.toString());
			logger.error("sqlSelect: {}", sqlSelect);
			logger.error("param: {}", param);
			ex.printStackTrace();
		} finally {
			HibernateUtil.closeSession(session);
		}

		map.put("total", totalCount);
		map.put("rows", list);
		map.put("QueryKey", pagination.getQueryKey());
		return map;
	}

	/**
	 * 查询单条记录
	 * 
	 * @param hql
	 * @param param
	 *            参数 String 为参数名 Object 为参数值, 参数值必须转换为对象属性匹配的数据类型
	 * @return
	 */
	public static <T> T getByHql(String hql, Map<String, Object> param) {
		List<T> list = queryByHql(hql, param);
		if (list.size() > 0) {
			return list.get(0);
		}
		return null;
	}

	/**
	 * 查询单条记录
	 * 
	 * @param hql
	 * @param parameterName
	 *            参数名
	 * @param parameterValue
	 *            参数值必须转换为对象属性匹配的数据类型
	 * @return
	 */
	public static <T> T getByHql(String hql, String parameterName,
			Object parameterValue) {
		List<T> list = queryByHql(hql, parameterName, parameterValue);
		if (list.size() > 0) {
			return list.get(0);
		}
		return null;
	}

	/**
	 * 查询单条记录
	 * 
	 * @param hql
	 * @return
	 */
	public static <T> T getByHql(String hql) {
		List<T> list = queryByHql(hql);
		if (list.size() > 0) {
			return list.get(0);
		}
		return null;
	}

	/**
	 * 查询单条记录
	 * 
	 * @param sql
	 * @param param
	 *            参数 String 为参数名 Object 为参数值, 参数值必须转换为对象属性匹配的数据类型
	 * @param entityType
	 *            返回对象类型
	 * @return
	 */
	public static <T> T getBySql(String sql, Map<String, Object> param,
			Class entityType) {
		List<T> list = queryBySql(sql, param, entityType);
		if (list.size() > 0) {
			return list.get(0);
		}
		return null;
	}

	/**
	 * 查询单条记录
	 * 
	 * @param sql
	 * @param param
	 *            参数 String 为参数名 Object 为参数值, 参数值必须转换为对象属性匹配的数据类型
	 * @return
	 */
	public static <T> T getBySql(String sql, Map<String, Object> param) {
		List<T> list = queryBySql(sql, param);
		if (list.size() > 0) {
			return list.get(0);
		}
		return null;
	}

	/**
	 * 查询单条记录
	 * 
	 * @param sql
	 * @param parameterName
	 *            参数名
	 * @param parameterValue
	 *            参数值必须转换为对象属性匹配的数据类型
	 * @param entityType
	 *            返回对象类型
	 * @return
	 */
	public static <T> T getBySql(String sql, String parameterName,
			Object parameterValue, Class entityType) {
		List<T> list = queryBySql(sql, parameterName, parameterValue,
				entityType);
		if (list.size() > 0) {
			return list.get(0);
		}
		return null;
	}

	/**
	 * 查询单条记录
	 * 
	 * @param sql
	 * @param parameterName
	 *            参数名
	 * @param parameterValue
	 *            参数值必须转换为对象属性匹配的数据类型
	 * @return
	 */
	public static <T> T getBySql(String sql, String parameterName,
			Object parameterValue) {
		List<T> list = queryBySql(sql, parameterName, parameterValue);
		if (list.size() > 0) {
			return list.get(0);
		}
		return null;
	}

	/**
	 * 查询单条记录
	 * 
	 * @param sql
	 * @param entityType
	 *            返回对象类型
	 * @return
	 */
	public static <T> T getBySql(String sql, Class entityType) {
		List<T> list = queryBySql(sql, entityType);
		if (list.size() > 0) {
			return list.get(0);
		}
		return null;
	}

	/**
	 * 查询单条记录
	 * 
	 * @param sql
	 * @return
	 */
	public static <T> T getBySql(String sql) {
		List<T> list = queryBySql(sql);
		if (list.size() > 0) {
			return list.get(0);
		}
		return null;
	}

	/**
	 * 查询单条记录，如果不存在，返回 null
	 * 
	 * @param sql
	 *            sql语句
	 * @return
	 */
	public static Map getMapBySql(String sql) {
		List<Map> list = queryMapBySql(sql);
		if (list.size() > 0) {
			return list.get(0);
		}
		return null;
	}

	/**
	 * 查询多条记录，如果不存在，返回 null
	 * 
	 * @param sql
	 *            sql语句
	 * @param parameterName
	 *            参数名
	 * @param parameterValue
	 *            参数值必须转换为对象属性匹配的数据类型
	 * @return
	 */
	public static Map getMapBySql(String sql, String parameterName,
			Object parameterValue) {
		List<Map> list = queryMapBySql(sql, parameterName, parameterValue);
		if (list.size() > 0) {
			return list.get(0);
		}
		return null;
	}

	/**
	 * 查询多条记录，如果不存在，返回 null
	 * 
	 * @param sql
	 *            sql语句
	 * @param param
	 *            参数 String 为参数名 Object 为参数值, 参数值必须转换为对象属性匹配的数据类型
	 * @return
	 */
	public static Map getMapBySql(String sql, Map<String, Object> param) {
		List<Map> list = queryMapBySql(sql, param);
		if (list.size() > 0) {
			return list.get(0);
		}
		return null;
	}

	/**
	 * 查询对象的数量
	 * 
	 * @param hql
	 * @param param
	 *            参数 String 为参数名 Object 为参数值, 参数值必须转换为对象属性匹配的数据类型
	 * @return 返回对象个数
	 */
	public static int getCountByHql(String hql, Map<String, Object> param) {
		int count = 0;
		Session session = null;
		try {
			session = HibernateUtil.getSession();
			Query query = session.createQuery(hql);

			Iterator iterator = param.keySet().iterator();
			while (iterator.hasNext()) {
				String key = iterator.next().toString();
				Object value = param.get(key);
				// String[]
				if ("[Ljava.lang.String;".equals(value.getClass().getName())) {
					query.setParameterList(key, (String[]) value);
				} else {
					query.setParameter(key, value);
				}
			}
			count = ((Number) query.iterate().next()).intValue();
		} catch (Exception ex) {
			logger.error(ex.toString());
			logger.error("sql: {}", hql);
			logger.error("param: {}", param);
			ex.printStackTrace();
		} finally {
			HibernateUtil.closeSession(session);
		}
		return count;
	}

	/**
	 * 查询对象的数量
	 * 
	 * @param sql
	 * @param param
	 *            参数 String 为参数名 Object 为参数值, 参数值必须转换为对象属性匹配的数据类型
	 * @return 返回对象个数
	 */
	public static int getCountBySql(String sql, Map<String, Object> param) {
		int count = 0;
		Session session = null;
		try {
			session = HibernateUtil.getSession();
			Query query = session.createSQLQuery(sql);

			Iterator iterator = param.keySet().iterator();
			while (iterator.hasNext()) {
				String key = iterator.next().toString();
				Object value = param.get(key);
				// String[]
				if ("[Ljava.lang.String;".equals(value.getClass().getName())) {
					query.setParameterList(key, (String[]) value);
				} else {
					query.setParameter(key, value);
				}
			}
			if (HibernateUtil.getDialect().indexOf("MySQL") > -1) {
				count = ((BigInteger) query.uniqueResult()).intValue();
			} else {
				count = ((BigDecimal) query.uniqueResult()).intValue();
			}

		} catch (Exception ex) {
			logger.error(ex.toString());
			logger.error("sql: {}", sql);
			logger.error("param: {}", param);
			ex.printStackTrace();
		} finally {
			HibernateUtil.closeSession(session);
		}
		return count;
	}

	/**
	 * 查询对象的数量
	 * 
	 * @param sql
	 * @return 返回对象个数
	 */
	public static int getCountBySql(String sql) {
		int count = 0;
		Session session = null;
		try {
			session = HibernateUtil.getSession();
			Query query = session.createSQLQuery(sql);
			if (HibernateUtil.getDialect().indexOf("MySQL") > -1) {
				count = ((BigInteger) query.uniqueResult()).intValue();
			} else {
				count = ((BigDecimal) query.uniqueResult()).intValue();
			}

		} catch (Exception ex) {
			logger.error(ex.toString());
			logger.error("sql: {}", sql);
			ex.printStackTrace();
		} finally {
			HibernateUtil.closeSession(session);
		}
		return count;
	}

	/**
	 * 分页查询
	 * 
	 * @param hqlCount
	 *            计算总页数hql
	 * @param hqlSelect
	 *            查询数据hql
	 * @param param
	 *            参数 String 为参数名 Object 为参数值, 参数值必须转换为对象属性匹配的数据类型
	 * @param pagination
	 *            分页对象
	 * @return
	 */
	public static Map<String, Object> queryPageByHql(String hqlCount,
			String hqlSelect, Map<String, Object> param, Pagination pagination) {
		Map<String, Object> map = new Hashtable<String, Object>(3);
		List list = null;
		int totalCount = 0;
		Session session = null;
		try {
			session = HibernateUtil.getSession();

			Query queryCount = session.createQuery(hqlCount);
			Query querySelect = session.createQuery(hqlSelect);

			// 为 where 条件中的参数赋值
			Iterator iterator = param.keySet().iterator();
			while (iterator.hasNext()) {
				String key = iterator.next().toString();
				Object value = param.get(key);
				// String[]
				if ("[Ljava.lang.String;".equals(value.getClass().getName())) {
					queryCount.setParameterList(key, (String[]) value);
					querySelect.setParameterList(key, (String[]) value);
				} else {
					queryCount.setParameter(key, value);
					querySelect.setParameter(key, value);
				}
			}

			// 计算总页数
			totalCount = pagination.getClientPageCount();
			// 避免每次都查询总页数
			// if (totalCount == 0)
			{
				totalCount = ((Number) queryCount.iterate().next()).intValue();
			}

			// 获取分页处理
			querySelect.setFirstResult((pagination.getPage() - 1)
					* pagination.getRows());
			querySelect.setMaxResults(pagination.getRows());
			list = querySelect.list();

		} catch (Exception ex) {
			logger.error(ex.toString());
			logger.error("sqlSelect: {}", hqlSelect);
			logger.error("param: {}", param);
			ex.printStackTrace();
		} finally {
			HibernateUtil.closeSession(session);
		}

		map.put("total", totalCount);
		map.put("rows", list);
		map.put("QueryKey", pagination.getQueryKey());
		return map;
	}

	/**
	 * 分页查询
	 * 
	 * @param sqlCount
	 *            计算总页数hql
	 * @param sqlSelect
	 *            查询数据hql
	 * @param param
	 *            参数 String 为参数名 Object 为参数值, 参数值必须转换为对象属性匹配的数据类型
	 * @param pagination
	 *            分页对象
	 * @return 注意返回值的 rows 为List<Object[]>
	 */
	public static Map<String, Object> queryPageBySql(String sqlCount,
			String sqlSelect, Map<String, Object> param, Pagination pagination) {
		Map<String, Object> map = new Hashtable<String, Object>(3);
		List list = null;
		int totalCount = 0;
		Session session = null;
		try {
			session = HibernateUtil.getSession();

			Query queryCount = session.createSQLQuery(sqlCount);
			Query querySelect = session.createSQLQuery(sqlSelect);

			// 为 where 条件中的参数赋值
			Iterator iterator = param.keySet().iterator();
			while (iterator.hasNext()) {
				String key = iterator.next().toString();
				Object value = param.get(key);
				// String[]
				if ("[Ljava.lang.String;".equals(value.getClass().getName())) {
					queryCount.setParameterList(key, (String[]) value);
					querySelect.setParameterList(key, (String[]) value);
				} else {
					queryCount.setParameter(key, value);
					querySelect.setParameter(key, value);
				}
			}

			// 计算总页数
			totalCount = pagination.getClientPageCount();
			// 避免每次都查询总页数
			if (totalCount == 0) {
				// hibernate 4.3.11 oracle11g count 返回的是 BigDecimal mysql count
				// 返回的是 BigInteger
				if (HibernateUtil.getDialect().indexOf("MySQL") > -1) {
					totalCount = ((BigInteger) queryCount.uniqueResult())
							.intValue();
				} else {
					totalCount = ((BigDecimal) queryCount.uniqueResult())
							.intValue();
				}
			}

			// 获取分页处理
			querySelect.setFirstResult((pagination.getPage() - 1)
					* pagination.getRows());
			querySelect.setMaxResults(pagination.getRows());

			querySelect
					.setResultTransformer(CriteriaSpecification.ALIAS_TO_ENTITY_MAP);
			list = querySelect.list();

		} catch (Exception ex) {
			logger.error(ex.toString());
			logger.error("sqlSelect: {}", sqlSelect);
			logger.error("param: {}", param);
			ex.printStackTrace();
		} finally {
			HibernateUtil.closeSession(session);
		}

		map.put("total", totalCount);
		map.put("rows", list);
		map.put("QueryKey", pagination.getQueryKey());
		return map;
	}

	/**
	 * 分页查询
	 * 
	 * @param sqlCount
	 *            计算总页数hql
	 * @param sqlSelect
	 *            查询数据hql
	 * @param param
	 *            参数 String 为参数名 Object 为参数值, 参数值必须转换为对象属性匹配的数据类型
	 * @param pagination
	 *            分页对象
	 * @return 注意返回值的 rows 为List<Object[]>
	 */
	public static <T> Map<String, Object> queryPageBySql(String sqlCount,
			String sqlSelect, Map<String, Object> param, Pagination pagination,
			Class entityType) {
		Map<String, Object> map = new Hashtable<String, Object>(3);
		List<T> list = null;
		int totalCount = 0;
		Session session = null;
		try {
			session = HibernateUtil.getSession();

			Query queryCount = session.createSQLQuery(sqlCount);
			Query querySelect = session.createSQLQuery(sqlSelect)
					.setResultTransformer(Transformers.aliasToBean(entityType));

			// 为 where 条件中的参数赋值
			Iterator iterator = param.keySet().iterator();
			while (iterator.hasNext()) {
				String key = iterator.next().toString();
				Object value = param.get(key);
				// String[]
				if ("[Ljava.lang.String;".equals(value.getClass().getName())) {
					queryCount.setParameterList(key, (String[]) value);
					querySelect.setParameterList(key, (String[]) value);
				} else {
					queryCount.setParameter(key, value);
					querySelect.setParameter(key, value);
				}
			}

			// 计算总页数
			totalCount = pagination.getClientPageCount();
			// 避免每次都查询总页数
			if (totalCount == 0) {
				// hibernate 4.3.11 oracle11g count 返回的是 BigDecimal mysql count
				// 返回的是 BigInteger
				if (HibernateUtil.getDialect().indexOf("MySQL") > -1) {
					totalCount = ((BigInteger) queryCount.uniqueResult())
							.intValue();
				} else {
					totalCount = ((BigDecimal) queryCount.uniqueResult())
							.intValue();
				}
			}

			// 获取分页处理
			querySelect.setFirstResult((pagination.getPage() - 1)
					* pagination.getRows());
			querySelect.setMaxResults(pagination.getRows());
			list = querySelect.list();

		} catch (Exception ex) {
			logger.error(ex.toString());
			logger.error("sqlSelect: {}", sqlSelect);
			logger.error("param: {}", param);
			ex.printStackTrace();
		} finally {
			HibernateUtil.closeSession(session);
		}

		map.put("total", totalCount);
		map.put("rows", list);
		map.put("QueryKey", pagination.getQueryKey());
		return map;
	}

	/**
	 * 分页查询
	 * 
	 * @param hqlCount
	 *            计算总页数hql
	 * @param hqlSelect
	 *            查询数据hql
	 * @param param
	 *            参数 String 为参数名 Object 为参数值, 参数值必须转换为对象属性匹配的数据类型
	 * @param page
	 *            分页对象
	 * @return List
	 */
	public static List queryPageByHql(String hqlCount, String hqlSelect,
			Map<String, Object> param, Page page) {
		List list = new ArrayList();
		int totalCount = 0;
		Session session = null;
		try {
			session = HibernateUtil.getSession();

			Query queryCount = session.createQuery(hqlCount);
			Query querySelect = session.createQuery(hqlSelect);

			// 为 where 条件中的参数赋值
			Iterator iterator = param.keySet().iterator();
			while (iterator.hasNext()) {
				String key = iterator.next().toString();
				Object value = param.get(key);
				// String[]
				if ("[Ljava.lang.String;".equals(value.getClass().getName())) {
					queryCount.setParameterList(key, (String[]) value);
					querySelect.setParameterList(key, (String[]) value);
				} else {
					queryCount.setParameter(key, value);
					querySelect.setParameter(key, value);
				}
			}

			// 计算总页数
			totalCount = ((Number) queryCount.iterate().next()).intValue();
			totalCount = (int) Math.ceil(totalCount * 1.00
					/ Constants.PAGE_ROW_COUNT);
			page.setCount(totalCount);
			if (page.getCurrIndex() <= 0) {
				page.setCurrIndex(1);
			}
			if (page.getCurrIndex() > page.getCount()) {
				page.setCurrIndex(page.getCurrIndex() - 1);
			}

			// 获取分页处理
			querySelect.setFirstResult((page.getCurrIndex() - 1)
					* Constants.PAGE_ROW_COUNT);
			querySelect.setMaxResults(Constants.PAGE_ROW_COUNT);
			list = querySelect.list();

		} catch (Exception ex) {
			logger.error(ex.toString());
			logger.error("sqlSelect: {}", hqlSelect);
			logger.error("param: {}", param);
			ex.printStackTrace();
		} finally {
			HibernateUtil.closeSession(session);
		}

		return list;
	}

	/**
	 * 分页查询
	 * 
	 * @param sqlCount
	 *            计算总页数sql
	 * @param sqlSelect
	 *            查询数据sql
	 * @param param
	 *            参数 String 为参数名 Object 为参数值, 参数值必须转换为对象属性匹配的数据类型
	 * @param page
	 *            分页对象
	 * @return List<Object[]>
	 */
	public static List queryPageBySql(String sqlCount, String sqlSelect,
			Map<String, Object> param, Page page) {
		List list = new ArrayList();
		int totalCount = 0;
		Session session = null;
		try {
			session = HibernateUtil.getSession();

			Query queryCount = session.createSQLQuery(sqlCount);
			Query querySelect = session.createSQLQuery(sqlSelect);

			// 为 where 条件中的参数赋值
			Iterator iterator = param.keySet().iterator();
			while (iterator.hasNext()) {
				String key = iterator.next().toString();
				Object value = param.get(key);
				// String[]
				if ("[Ljava.lang.String;".equals(value.getClass().getName())) {
					queryCount.setParameterList(key, (String[]) value);
					querySelect.setParameterList(key, (String[]) value);
				} else {
					queryCount.setParameter(key, value);
					querySelect.setParameter(key, value);
				}
			}

			// 计算总页数
			// hibernate 4.3.11 oracle11g count 返回的是 BigDecimal mysql count 返回的是
			// BigInteger
			if (HibernateUtil.getDialect().indexOf("MySQL") > -1) {
				totalCount = ((BigInteger) queryCount.uniqueResult())
						.intValue();
			} else {
				totalCount = ((BigDecimal) queryCount.uniqueResult())
						.intValue();
			}
			totalCount = (int) Math.ceil(totalCount * 1.00
					/ Constants.PAGE_ROW_COUNT);
			page.setCount(totalCount);
			if (page.getCurrIndex() <= 0) {
				page.setCurrIndex(1);
			}
			if (page.getCurrIndex() > page.getCount()) {
				page.setCurrIndex(page.getCurrIndex() - 1);
			}

			// 获取分页处理
			querySelect.setFirstResult((page.getCurrIndex() - 1)
					* Constants.PAGE_ROW_COUNT);
			querySelect.setMaxResults(Constants.PAGE_ROW_COUNT);
			list = querySelect.list();

		} catch (Exception ex) {
			logger.error(ex.toString());
			logger.error("sqlSelect: {}", sqlSelect);
			logger.error("param: {}", param);
			ex.printStackTrace();
		} finally {
			HibernateUtil.closeSession(session);
		}

		return list;
	}

	/**
	 * 分页查询
	 * 
	 * @param sqlCount
	 *            计算总页数sql
	 * @param sqlSelect
	 *            查询数据sql
	 * @param param
	 *            参数 String 为参数名 Object 为参数值, 参数值必须转换为对象属性匹配的数据类型
	 * @param page
	 *            分页对象
	 * @return List<Map>
	 */
	public static List<Map> queryMapPageBySql(String sqlCount,
			String sqlSelect, Map<String, Object> param, Page page) {
		List<Map> list = new ArrayList<Map>();
		int totalCount = 0;
		Session session = null;
		try {
			session = HibernateUtil.getSession();

			Query queryCount = session.createSQLQuery(sqlCount);
			Query querySelect = session.createSQLQuery(sqlSelect);

			// 为 where 条件中的参数赋值
			Iterator iterator = param.keySet().iterator();
			while (iterator.hasNext()) {
				String key = iterator.next().toString();
				Object value = param.get(key);
				// String[]
				if ("[Ljava.lang.String;".equals(value.getClass().getName())) {
					queryCount.setParameterList(key, (String[]) value);
					querySelect.setParameterList(key, (String[]) value);
				} else {
					queryCount.setParameter(key, value);
					querySelect.setParameter(key, value);
				}
			}

			// 计算总页数
			// hibernate 4.3.11 oracle11g count 返回的是 BigDecimal mysql count 返回的是
			// BigInteger
			if (HibernateUtil.getDialect().indexOf("MySQL") > -1) {
				totalCount = ((BigInteger) queryCount.uniqueResult())
						.intValue();
			} else {
				totalCount = ((BigDecimal) queryCount.uniqueResult())
						.intValue();
			}

			page.setRows(totalCount);
			totalCount = (int) Math.ceil(totalCount * 1.00
					/ Constants.PAGE_ROW_COUNT);
			page.setCount(totalCount);
			if (page.getCurrIndex() <= 0) {
				page.setCurrIndex(1);
			}
			if (page.getCurrIndex() > page.getCount()) {
				page.setCurrIndex(page.getCurrIndex() - 1);
			}

			// 获取分页处理
			querySelect.setFirstResult((page.getCurrIndex() - 1)
					* Constants.PAGE_ROW_COUNT);
			querySelect.setMaxResults(Constants.PAGE_ROW_COUNT);

			querySelect
					.setResultTransformer(CriteriaSpecification.ALIAS_TO_ENTITY_MAP);
			list = querySelect.list();

		} catch (Exception ex) {
			logger.error(ex.toString());
			logger.error("sqlSelect: {}", sqlSelect);
			logger.error("param: {}", param);
			ex.printStackTrace();
		} finally {
			HibernateUtil.closeSession(session);
		}

		return list;
	}

	/**
	 * 查询单条记录
	 * 
	 * @param hql
	 * @param param
	 *            参数 String 为参数名 Object 为参数值, 参数值必须转换为对象属性匹配的数据类型
	 * @return
	 */
	public static Object queryOne(String hql, HashMap<String, Object> param) {
		List list = queryByHql(hql, param);
		if (list.size() > 0) {
			return list.get(0);
		}
		return null;
	}
}
