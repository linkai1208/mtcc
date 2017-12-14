package com.sten.framework.util;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;

/**
 * 
 * @description List 帮助类
 * @author linkai
 * @date 2016-3-21
 * @time 下午06:16:27
 */
public class ListUtil<E> {

	/**
	 * 
	 * @param list
	 *            要排序的集合
	 * @param method
	 *            要排序的实体的属性所对应的get方法
	 * @param sort
	 *            asc 正序 desc 倒序
	 */
	public void Sort(List<E> list, final String method, final String sort) {
		// 用内部类实现排序
		Collections.sort(list, new Comparator<E>() {

			public int compare(E a, E b) {
				int ret = 0;
				try {
					// 获取m1的方法名
					Method m1 = a.getClass().getMethod(method, new Class[0]);
					// 获取m2的方法名
					Method m2 = b.getClass().getMethod(method, new Class[0]);
					if (sort != null && "desc".equals(sort)) {
						ret = m2.invoke(((E) b), new Object[] {})
								.toString()
								.compareTo(
										m1.invoke(((E) a), new Object[] {})
												.toString());
					} else {
						// 正序排序
						ret = m1.invoke(((E) a), new Object[] {})
								.toString()
								.compareTo(
										m2.invoke(((E) b), new Object[] {})
												.toString());
					}
				} catch (NoSuchMethodException ne) {
					System.out.println(ne);
				} catch (IllegalArgumentException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (IllegalAccessException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (InvocationTargetException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				return ret;
			}
		});
	}

	/**
	 * map 转换为 object 对象，map 的 key 和对象的属性必须大小写一致
	 * 
	 * @return 转换失败 返回 null
	 */
	public static Object mapToObject(Map<String, Object> map, Class<?> beanClass) {
		if (map == null)
			return null;
		Object obj = null;
		try {
			obj = beanClass.newInstance();
			org.apache.commons.beanutils.BeanUtils.populate(obj, map);
		} catch (InstantiationException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			e.printStackTrace();
		}
		return obj;
	}

	// public static Map<?, ?> objectToMap(Object obj) {
	// if(obj == null)
	// return null;
	//
	// return new org.apache.commons.beanutils.BeanMap(obj);
	// }
}