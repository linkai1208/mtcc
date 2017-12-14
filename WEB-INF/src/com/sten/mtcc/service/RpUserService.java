package com.sten.mtcc.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.sten.framework.service.BaseService;
import com.sten.mtcc.model.RpUser;

@Service("roUserService")
public class RpUserService {
	private static final Logger logger = LoggerFactory
			.getLogger(RpUserService.class);

	@Autowired
	private BaseService baseService;

	public RpUser getById(String id) {
		RpUser rpUser = new RpUser();
		try {
			rpUser = (RpUser) BaseService.get(RpUser.class, id);
		} catch (Exception e) {
			e.printStackTrace();
			logger.error(e.toString());
		}
		return rpUser;
	}

}
