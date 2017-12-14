package com.sten.framework.interceptor;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

/**
 * Created by linkai on 16-1-16.
 */
public class BaseInterceptor implements HandlerInterceptor {

	private static final Logger logger = LoggerFactory
			.getLogger(BaseInterceptor.class);

	private static String[] exts = new String[] { ".js", ".css", ".gif",
			".png", ".jpg", ".jpeg", ".xls", ".xlsx", ".doc", ".docx", ".pdf",
			".rar", ".zip", ".exe" };

	public void afterCompletion(HttpServletRequest arg0,
			HttpServletResponse arg1, Object arg2, Exception arg3)
			throws Exception {
		// System.out.println("BaseInterceptor.afterCompletion 当前用户:"+
		// SessionUtil.getInstance().getUsername());

		// TODO Auto-generated method stub
		// fix 点击劫持：缺少X-Frame-Options头详情
		arg1.addHeader("X-Frame-Options", "SAMEORIGIN");
	}

	public void postHandle(HttpServletRequest arg0, HttpServletResponse arg1,
			Object arg2, ModelAndView arg3) throws Exception {
		if (arg3 != null) {
			String uri = arg0.getRequestURI();
			Map<String, Object> model = arg3.getModel();
			// header.jsp 页面使用
			model.put("RequestURI", uri);
		}
		// TODO Auto-generated method stub
	}

	public boolean preHandle(HttpServletRequest arg0, HttpServletResponse arg1,
			Object arg2) throws Exception {
		// TODO Auto-generated method stub
		String uri = arg0.getRequestURI();
		logger.info(uri);

		// 初始化 session
		if (com.sten.framework.util.StringUtil.lastIndexOfAny(uri, exts) == -1) {
			HttpSession session = ((HttpServletRequest) arg0).getSession(true);
			com.sten.framework.util.SessionUtil instance = (com.sten.framework.util.SessionUtil) session
					.getAttribute("_SessionUtil_");
			com.sten.framework.util.SessionUtil.setInstance(instance);
			session.setAttribute("_SessionUtil_",
					com.sten.framework.util.SessionUtil.getInstance());

			if (instance != null) {
				if ("".equals(instance.getIpAddress())) {
					String ip = getIpAddr(arg0);
					instance.setIpAddress(ip);
				}
			}
			if (instance != null && "".equals(instance.getUserID())
					&& uri.indexOf(arg0.getContextPath() + "/login") == -1
					&& uri.indexOf(arg0.getContextPath() + "/logout") == -1
					&& uri.indexOf(arg0.getContextPath() + "/portal") == -1
					&& uri.indexOf(arg0.getContextPath() + "/includes") == -1) {
				// 未登录, 并且不是登录页则跳出
				System.out.print("尝试请求未授权的资源:" + uri);
				// arg1.sendRedirect(arg0.getContextPath() + "/login");
			}
			if (instance == null) {
				arg1.sendRedirect(arg0.getContextPath() + "/login");
			}
			if (instance != null && !"".equals(instance.getUserID())) {
				boolean allow = false;
				String memberType = instance.getUserType();
				String contentPath = arg0.getContextPath();

				if (!"99".equals(memberType)) {
					// 不是管理员的场合
					for (String commonUri : instance.getCommonURL()) {
						if (uri.indexOf(contentPath + commonUri) == 0) {
							// 在授权范围内
							allow = true;
							break;
						}
					}

					if (!allow) {
						for (String userUri : instance.getURL()) {
							if (uri.indexOf(contentPath + userUri) == 0) {
								// 当前用户已经分配该权限
								allow = true;
								break;
							}
						}
					}
				} else {
					// 管理员的场合
					for (String userUri : instance.getURL()) {
						if (uri.indexOf(contentPath + userUri) == 0) {
							// 当前用户已经分配该权限
							allow = true;
							break;
						}
					}
				}

				// if(!allow){
				// //当前用户没有分配该权限
				// System.out.println("UserID = "+ instance.getUserID()
				// +" 尝试请求未授权的资源:" + uri );
				// logger.info("UserID = "+ instance.getUserID() +" 尝试请求未授权的资源:"
				// + uri );
				// //arg1.sendRedirect(arg0.getContextPath() + "/login");
				//
				// //跳转到登录页
				// PrintWriter out = arg1.getWriter();
				// out.println("<html>");
				// out.println("<script>");
				// out.println("window.open ('"+arg0.getContextPath()+"/login','_top')");
				// out.println("</script>");
				// out.println("</html>");
				// return false;
				// }
			}
		}

		return true;
	}

	/**
	 * 获取访问者 IP
	 * 
	 * 在一般情况下使用 Request.getRemoteAddr() 即可，但是经过 nginx 等反向代理软件后，这个方法会失效
	 * 
	 * 此方法先从 Header 中获取 X-Real-IP，如果不存在再从 X-Forwarded-For 获得第一个IP(用,分割)
	 * 如果还不存在则调用 Request.getRemoteAddr()
	 * 
	 * @param request
	 * @return
	 */
	private String getIpAddr(HttpServletRequest request) {
		String ip = request.getHeader("X-Real-IP");
		if (ip != null && !"".equals(ip) && !"unknown".equalsIgnoreCase(ip)) {
			return ip;
		}
		ip = request.getHeader("X-Forwarded-For");
		if (ip != null && !"".equals(ip) && !"unknown".equalsIgnoreCase(ip)) {
			// 多次反向代理后会有多个IP值，第一个为真实IP。
			int index = ip.indexOf(',');
			if (index != -1) {
				return ip.substring(0, index);
			} else {
				return ip;
			}
		} else {
			return request.getRemoteAddr();
		}
	}
}