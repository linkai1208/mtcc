package com.sten.framework.util;

/**
 * Created by linkai on 2017/5/12.
 */
public class Oracle11gDialect extends org.hibernate.dialect.Oracle10gDialect {

	public Oracle11gDialect() {
		super();
		// fix 执行原生 sql NVARCHAR2 字段类型映射错误 bug： No Dialect mapping for JDBC
		// type: -9
		registerHibernateType(-9, "string");
	}
}