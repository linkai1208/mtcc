package com.sten.framework.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestController;

/**
 * Created by linkai on 16-1-26.
 */
@RestController
public class BaseController {

	private static final Logger logger = LoggerFactory
			.getLogger(BaseController.class);

	// @RequestMapping("*")
	// public ModelAndView error404(HttpServletRequest request) {
	// ModelAndView mv = new ModelAndView();
	// mv.setViewName("portal/404");
	// return mv;
	// }

	@ExceptionHandler(RuntimeException.class)
	public String runtimeExceptionHandler(RuntimeException runtimeException,
			ModelMap modelMap) {
		logger.error(runtimeException.getLocalizedMessage());

		modelMap.put("status", 500);
		return "exception";
	}
}
