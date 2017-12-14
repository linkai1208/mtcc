package com.sten.framework.util;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by linkai on 16-2-22. 2016-07-14 增加 IPAddrss 属性
 */
public class SessionUtil implements Serializable {

	private static ThreadLocal threadLocal = new ThreadLocal();
	private Map<String, Object> session;
	private String prefix = "UAV_";

	public static SessionUtil getInstance() {
		Object obj = threadLocal.get();

		SessionUtil instance = (SessionUtil) obj;

		// 测试数据
		// List<String> roleCode = new ArrayList<String>();
		// List<String> resourceCode = new ArrayList<String>();
		//
		// roleCode.add("OEAS_ROLE_ADMIN");
		// resourceCode.add("OEAS_RES_ADMIN");
		//
		// instance.setUserInfo("000001", "管理员", "220104198905176312",
		// "13811184457","010-12345678","10525379@qq.com",
		// "orgid123456", "caac", "民航局飞标司",
		// roleCode, resourceCode);

		return instance;
	}

	public static void setInstance(SessionUtil instance) {
		if (instance == null) {
			instance = new SessionUtil();
		}
		threadLocal.set(instance);
	}

	public SessionUtil() {
		if (this.session == null) {
			this.session = new HashMap<String, Object>();
		}
	}

	public void setUserInfo(String userId, String userName, String org_code) {
		setValue("UserID", userId);
		setValue("UserName", userName);
		setValue("OrgCode", org_code);
	}

	public String getValue(String name) {
		return session.get(prefix.concat(name)) != null ? session.get(
				prefix.concat(name)).toString() : "";
	}

	public void setValue(String name, String value) {
		session.put(prefix.concat(name), value);
	}

	public Object getObject(String name) {
		return session.get(prefix.concat(name)) != null ? session.get(prefix
				.concat(name)) : null;
	}

	public void setObject(String name, Object value) {
		session.put(prefix.concat(name), value);
	}

	public String getUserID() {

		return getValue("UserID");
	}

	public String getUsername() {

		return getValue("UserName");
	}

	public String getMobile() {

		return getValue("Mobile");
	}

	public String getEmail() {

		return getValue("Email");
	}

	public void setIpAddress(String ipaddress) {

		setValue("IpAddress", ipaddress);
	}

	public String getIpAddress() {

		return getValue("IpAddress");
	}

	public String getUserType() {

		return getValue("UserType");
	}

	public void setURL(String[] urls) {
		setObject("URL", urls);
	}

	public String[] getURL() {
		return (String[]) getObject("URL");
	}

	public void setCommonURL(String[] urls) {
		setObject("CommonURL", urls);
	}

	public String[] getCommonURL() {
		return (String[]) getObject("CommonURL");
	}
}
