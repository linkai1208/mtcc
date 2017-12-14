package com.sten.mtcc.controller;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import com.sten.framework.util.SessionUtil;
import com.sten.mtcc.common.URLConstants;
import com.sten.mtcc.model.RpUser;
import com.sten.mtcc.service.RpUserService;

/**
 * Created by ztw-a on 2017/6/12.
 */
@RestController
public class LoginController {
	private static final Logger logger = LoggerFactory
			.getLogger(LoginController.class);

	@Autowired
	private RpUserService rpUserService;

	@RequestMapping(value = "/login", method = RequestMethod.GET)
	public ModelAndView login(HttpServletRequest request) {
		ModelAndView mv = new ModelAndView();
		mv.setViewName("login");
		return mv;
	}

	@RequestMapping(value = "/logout", method = RequestMethod.GET)
	public ModelAndView logout(HttpServletRequest request) {
		ModelAndView mv = new ModelAndView();
		mv.setViewName("login");

		HttpSession session = request.getSession(true);
		session.removeAttribute("_SessionUtil_");
		return mv;
	}

	@RequestMapping(value = "/enter", method = RequestMethod.POST)
	public ModelAndView enter(String user, String password,
			HttpServletRequest request) {
		ModelAndView mv = new ModelAndView();
		try {
			HttpSession session = request.getSession(true);
			session.setAttribute("errormessage", "");
			RpUser rpUser = rpUserService.getById(user);
			if (rpUser != null && rpUser.getRu_password().equals(password)) {
				SessionUtil sessionUtil = SessionUtil.getInstance();
				mv.setViewName("frame");
				sessionUtil.setCommonURL(URLConstants.COMMON_URL);

				mv.addObject("pageLink", "/uploadTel/uploadTelList");
				sessionUtil.setURL(URLConstants.PERSON_URL);
				mv.addObject("memberName", rpUser.getRu_name());

				sessionUtil.setUserInfo(String.valueOf(rpUser.getRu_id()),
						rpUser.getRu_name(), rpUser.getRu_org_code());
				session.setAttribute("_SessionUtil_", sessionUtil);
			} else {
				mv.addObject("errmessage", "error");
				session.setAttribute("errormessage", "error");
				mv.setViewName("login");
			}
		} catch (Exception e) {
			mv.setViewName("login");
		}
		return mv;
	}

	@RequestMapping(value = "/checkLoginPassword", method = RequestMethod.POST)
	public Map<String, Object> checkLoginPassword(String user_id,
			String password) {
		Map<String, Object> map = new HashMap<String, Object>();
		Boolean flg = false;

		RpUser rpUser = rpUserService.getById(user_id);
		if (rpUser != null && rpUser.getRu_password().equals(password)) {
			flg = true;
		}
		if (flg) {
			map.put("result", "true");
			map.put("message", "操作成功");
		} else {
			map.put("result", "false");
			map.put("message", "操作失败");
		}
		return map;

	}

}
