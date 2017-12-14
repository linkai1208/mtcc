package com.sten.mtcc.controller;

import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import com.sten.framework.common.Pagination;
import com.sten.framework.service.UploadFileService;
import com.sten.mtcc.model.BiDeptInfo;
import com.sten.mtcc.service.DeptInfoService;

/**
 * Created by MZQ on 2017/2/27.
 */
@RestController
@RequestMapping(value = "deptInfo")
public class DeptInfoController {
	private static final Logger logger = LoggerFactory
			.getLogger(DeptInfoController.class);
	@Autowired
	private DeptInfoService deptInfoService;
	@Autowired
	private UploadFileService uploadFileService;

	@RequestMapping(value = "/deptInfoList", method = RequestMethod.GET)
	public ModelAndView uploadDeptList() {
		ModelAndView mv = new ModelAndView();
		mv.setViewName("/mtcc/basic/deptInfoList");

		return mv;
	}

	@RequestMapping(value = "/view/deptInfoList/load", method = RequestMethod.POST)
	public Map<String, Object> easyuiGridLoad(Pagination pagination) {
		Map<String, Object> map = new HashMap<String, Object>();
		map = deptInfoService.loadByPage(pagination);
		return map;
	}

	@RequestMapping(value = "view/addModel")
	public ModelAndView view_addModel() {
		ModelAndView mv = new ModelAndView();

		BiDeptInfo deptInfo = new BiDeptInfo();

		deptInfo.setIs_valid("1");

		mv.addObject("deptInfo", deptInfo);
		mv.setViewName("mtcc/basic/view_dialog_deptInfo");
		return mv;
	}

	@RequestMapping(value = "view/editModel/{id}")
	public ModelAndView view_editModel(@PathVariable String id) {
		ModelAndView mv = new ModelAndView();

		BiDeptInfo deptInfo = deptInfoService.getDeptInfoById(id);

		mv.addObject("deptInfo", deptInfo);
		mv.setViewName("mtcc/basic/view_dialog_deptInfo");
		return mv;
	}

	@RequestMapping(value = "deleteById")
	public Map<String, Object> deleteById(String id) {
		Map<String, Object> map = new HashMap<String, Object>();
		boolean flg = deptInfoService.deleteDeptInfoById(id);
		if (flg) {
			map.put("result", "true");
		} else {
			map.put("result", "false");
		}
		return map;
	}

	@RequestMapping(value = "saveOrUpdate", method = RequestMethod.POST)
	public Map<String, Object> saveModel(BiDeptInfo model) {
		Map<String, Object> map = new HashMap<String, Object>();
		boolean flg = false;
		String dept_name = model.getDept_name();
		flg = deptInfoService.isDeptNameExist(dept_name, model.getUuid());
		if (flg) {
			map.put("result", "false");
			map.put("message", "部门名称重复，请重新填写。");
		} else {
			flg = deptInfoService.saveOrUpdateModel(model);
			if (flg) {
				map.put("result", "true");
				map.put("message", "保存成功");
				map.put("uuid", model.getUuid());
			} else {
				map.put("result", "false");
				map.put("message", "保存失败，请重新填写");
			}
		}
		return map;
	}

}
