package com.sten.framework.interceptor;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.sten.framework.util.SessionUtil;
import com.sten.mtcc.common.CommonUtil;
import com.sten.mtcc.model.RpUser;
import com.sten.mtcc.service.RpUserService;

public class InitSessionFilter implements Filter {
	@Autowired
	private RpUserService rpUserService;
	private static final Logger logger = LoggerFactory
			.getLogger(InitSessionFilter.class);

	public void destroy() {
	}

	public void doFilter(ServletRequest request, ServletResponse response,
			FilterChain chain) throws IOException, ServletException {
		if (((HttpServletRequest) request)
				.getAttribute("javax.servlet.forward.request_uri") == null
				&& ((HttpServletRequest) request)
						.getAttribute("javax.servlet.include.request_uri") == null) {
			HttpSession session = ((HttpServletRequest) request)
					.getSession(true);
			com.sten.framework.util.SessionUtil instance = (com.sten.framework.util.SessionUtil) session
					.getAttribute("_SessionUtil_");
			com.sten.framework.util.SessionUtil.setInstance(instance);
			// System.out.println("doFilter 当前用户:"+
			// SessionUtil.getInstance().getUsername());

			try {
				String userId = CommonUtil.getNull2String(request
						.getAttribute("loginUserId"));
				// 与session里存在的数据进行比较，如果登录用户和RO的用户ID不一样，则新增加一个LoginUser
				if (!"".equals(userId)
						&& !userId
								.equals(SessionUtil.getInstance().getUserID())) {
					System.out.println("doFilter 初始化用户");
					RpUser rpUser = rpUserService.getById(userId);
					if (rpUser == null) {
						rpUser = new RpUser();
					}

					SessionUtil.getInstance().setUserInfo(
							String.valueOf(rpUser.getRu_id()),
							rpUser.getRu_name(), rpUser.getRu_org_code());

					session.setAttribute("_SessionUtil_",
							SessionUtil.getInstance());
				}
			} catch (Exception e) {
				logger.error("用户登陆信息获取失败！");
				throw new RuntimeException(e);
			}
			chain.doFilter(request, response);
		} else {
			chain.doFilter(request, response);
		}

	}

	public void init(FilterConfig arg0) throws ServletException {
	}
}
