package com.sten.mtcc.controller;

import java.io.File;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.xml.parsers.ParserConfigurationException;

import org.apache.poi.openxml4j.exceptions.OpenXML4JException;
import org.owasp.esapi.ESAPI;
import org.owasp.esapi.errors.IntrusionException;
import org.owasp.esapi.errors.ValidationException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;
import org.xml.sax.SAXException;

import com.sten.framework.common.Pagination;
import com.sten.framework.common.XLSXCovertCSVReader;
import com.sten.framework.model.UploadFile;
import com.sten.framework.service.UploadFileService;
import com.sten.framework.util.PropertiesUtil;
import com.sten.framework.util.StringUtil;
import com.sten.mtcc.model.BiCodeBsce;
import com.sten.mtcc.model.BiDeptInfo;
import com.sten.mtcc.model.BiTelInfo;
import com.sten.mtcc.service.BiCodeBsceService;
import com.sten.mtcc.service.DeptInfoService;
import com.sten.mtcc.service.TelInfoService;

/**
 * Created by MZQ on 2017/2/27.
 */
@RestController
@RequestMapping(value = "telInfo")
public class TelInfoController {
	private static final Logger logger = LoggerFactory
			.getLogger(TelInfoController.class);
	@Autowired
	private TelInfoService telInfoService;
	@Autowired
	private UploadFileService uploadFileService;

	@RequestMapping(value = "/telInfoList", method = RequestMethod.GET)
	public ModelAndView uploadTelList() {
		ModelAndView mv = new ModelAndView();
		mv.setViewName("/mtcc/basic/telInfoList");

		return mv;
	}

	@RequestMapping(value = "/view/telInfoList/load", method = RequestMethod.POST)
	public Map<String, Object> easyuiGridLoad(Pagination pagination) {
		Map<String, Object> map = new HashMap<String, Object>();
		map = telInfoService.loadByPage(pagination);
		return map;
	}

	@RequestMapping(value = "/view/importTelInfo")
	public ModelAndView imortTelInfo() {
		ModelAndView mv = new ModelAndView();

		List<UploadFile> list = null;
		list = new ArrayList<UploadFile>();

		String readOnly = "false";
		mv.addObject("list", list);
		mv.addObject("readOnly", readOnly);
		mv.setViewName("/mtcc/basic/importTelInfo");

		return mv;
	}

	@RequestMapping(value = "view/addModel")
	public ModelAndView view_addModel() {
		ModelAndView mv = new ModelAndView();
		mv.addObject("deptInfoList", DeptInfoService.getBiDeptInfoForCheck());
		List<BiCodeBsce> cycleList = BiCodeBsceService.getBiCodeBsce("001");
		mv.addObject("cycleList", cycleList);
		List<BiCodeBsce> minCycleList = BiCodeBsceService.getBiCodeBsce("001");
		mv.addObject("minCycleList", minCycleList);
		mv.addObject("telecOperatorList",
				BiCodeBsceService.getBiCodeBsce("002"));

		BiTelInfo telInfo = new BiTelInfo();

		telInfo.setIs_valid("1");
		telInfo.setTogether_flag(BiCodeBsceService.getBiCodeBsceNameByKey(
				"007", "1"));

		mv.addObject("telInfo", telInfo);
		mv.setViewName("mtcc/basic/view_dialog_telInfo");
		return mv;
	}

	@RequestMapping(value = "view/editModel/{id}")
	public ModelAndView view_editModel(@PathVariable String id) {
		ModelAndView mv = new ModelAndView();
		mv.addObject("deptInfoList", DeptInfoService.getBiDeptInfoForCheck());
		List<BiCodeBsce> cycleList = BiCodeBsceService.getBiCodeBsce("001");
		mv.addObject("cycleList", cycleList);
		List<BiCodeBsce> minCycleList = BiCodeBsceService.getBiCodeBsce("001");
		mv.addObject("minCycleList", minCycleList);
		mv.addObject("telecOperatorList",
				BiCodeBsceService.getBiCodeBsce("002"));

		BiTelInfo telInfo = telInfoService.getTelInfoById(id);

		mv.addObject("telInfo", telInfo);
		mv.setViewName("mtcc/basic/view_dialog_telInfo");
		return mv;
	}

	@RequestMapping(value = "deleteById")
	public Map<String, Object> deleteById(String id) {
		Map<String, Object> map = new HashMap<String, Object>();
		boolean flg = telInfoService.deleteTelInfoById(id);
		if (flg) {
			map.put("result", "true");
		} else {
			map.put("result", "false");
		}
		return map;
	}

	@RequestMapping(value = "saveOrUpdate", method = RequestMethod.POST)
	public Map<String, Object> saveModel(BiTelInfo model) {
		Map<String, Object> map = new HashMap<String, Object>();
		boolean flg = false;
		String tel_number = model.getTel_number();
		flg = telInfoService.isTelNumberExist(tel_number, model.getUuid());
		if (flg) {
			map.put("result", "false");
			map.put("message", "计费号码重复，请重新填写。");
		} else {
			if (model.getTogether_flag().equals("1")) {
				model.setOut_long_cost(null);
				model.setOut_long_cycle(null);
				model.setOut_long_min_cycle(null);
			}
			flg = telInfoService.saveOrUpdateModel(model);
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

	@RequestMapping(value = "importData", method = RequestMethod.POST)
	public Map<String, Object> importData(BiTelInfo model)
			throws IntrusionException, ValidationException {
		Map<String, Object> map = new HashMap<String, Object>();
		boolean flg = false;

		String attach_field = model.getAttach_field();

		// 删除附件id最后的逗号
		if (StringUtil.isNotEmpty(attach_field)) {
			if (attach_field.endsWith(",")) {
				attach_field = attach_field.substring(0,
						attach_field.length() - 1);
			}
		}
		// 数据导入库表
		if (StringUtil.isNotEmpty(attach_field)) {
			String path = attach_field;
			UploadFile uploadFile = uploadFileService.getFile(path);
			String filepath = StringUtil.trimEnd(
					PropertiesUtil.getProperty("UPLOAD_FOLDER_ROOT"),
					File.separator);
			String rootPath = PropertiesUtil.getProperty("ESAPI_VALID_ROOT");
			String safePath = ESAPI.validator().getValidDirectoryPath(
					"UPLOAD_FOLDER_ROOT", filepath, new File(rootPath), false);
			safePath += File.separator + uploadFile.getFullFileName();

			List<String[]> list = new ArrayList();
			try {
				list = XLSXCovertCSVReader.readerExcel(safePath, "计费号码", 12);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (OpenXML4JException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (ParserConfigurationException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (SAXException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			String msg = "";
			for (int i = 1; i < list.size(); i++) {
				String[] record = list.get(i);
				BiTelInfo telInfo = new BiTelInfo();
				// 电话号码
				String tel_number = record[0] == null ? "" : record[0]
						.toString();
				if (!tel_number.equals("")
						&& tel_number.substring(0, 1).equals("\"")
						&& tel_number.substring(tel_number.length() - 1,
								tel_number.length()).equals("\"")) {
					tel_number = tel_number.substring(1,
							tel_number.length() - 1);
				}
				telInfo.setTel_number(tel_number);
				String dept_name = record[1] == null ? "" : record[1]
						.toString();
				if (!dept_name.equals("")
						&& dept_name.substring(0, 1).equals("\"")
						&& dept_name.substring(dept_name.length() - 1,
								dept_name.length()).equals("\"")) {
					dept_name = dept_name.substring(1, dept_name.length() - 1);
				}
				telInfo.setDept_name(dept_name);
				BiDeptInfo deptInfo = DeptInfoService
						.getBiDeptInfoByName(telInfo.getDept_name());
				if (deptInfo != null) {
					telInfo.setDept_uuid(deptInfo.getUuid());
				} else {
					// 部门不匹配
				}
				String valid_start_date = record[2] == null ? "" : record[2]
						.toString();
				if (!valid_start_date.equals("")
						&& valid_start_date.substring(0, 1).equals("\"")
						&& valid_start_date.substring(
								valid_start_date.length() - 1,
								valid_start_date.length()).equals("\"")) {
					valid_start_date = valid_start_date.substring(1,
							valid_start_date.length() - 1);
				}
				telInfo.setValid_start_date(valid_start_date);
				String basic_cost = record[3] == null ? "0" : record[3]
						.toString();
				telInfo.setBasic_cost(new BigDecimal(basic_cost));
				String out_local_cost = record[4] == null ? "0" : record[4]
						.toString();
				telInfo.setOut_local_cost(new BigDecimal(out_local_cost));
				String out_local_cycle = record[5] == null ? "0" : record[5]
						.toString();
				telInfo.setOut_local_cycle(new Integer(out_local_cycle));
				String out_local_min_cycle = record[6] == null ? "0"
						: record[6].toString();
				telInfo.setOut_local_min_cycle(new Integer(out_local_min_cycle));
				String together_flag = record[7] == null ? "" : record[7]
						.toString();
				if (!together_flag.equals("")
						&& together_flag.substring(0, 1).equals("\"")
						&& together_flag.substring(together_flag.length() - 1,
								together_flag.length()).equals("\"")) {
					together_flag = together_flag.substring(1,
							together_flag.length() - 1);
				}
				telInfo.setTogether_flag(together_flag);
				if (StringUtil.getValue(together_flag).equals("0")) {
					String out_long_cost = record[8] == null ? "0" : record[8]
							.toString();
					telInfo.setOut_long_cost(new BigDecimal(out_long_cost));
					String out_long_cycle = record[9] == null ? "0" : record[9]
							.toString();
					telInfo.setOut_long_cycle(new Integer(out_long_cycle));
					String out_long_min_cycle = record[10] == null ? "0"
							: record[10].toString();
					telInfo.setOut_long_min_cycle(new Integer(
							out_long_min_cycle));
				}
				String telec_operator_type_name = record[11] == null ? ""
						: record[11].toString();
				if (!telec_operator_type_name.equals("")
						&& telec_operator_type_name.substring(0, 1)
								.equals("\"")
						&& telec_operator_type_name.substring(
								telec_operator_type_name.length() - 1,
								telec_operator_type_name.length()).equals("\"")) {
					telec_operator_type_name = telec_operator_type_name
							.substring(1, telec_operator_type_name.length() - 1);
				}
				telInfo.setTelec_operator_type(BiCodeBsceService
						.getBiCodeBsceKeyByName("002", telec_operator_type_name));
				String cost_attach_flag_name = record[12] == null ? ""
						: record[12].toString();
				if (!cost_attach_flag_name.equals("")
						&& cost_attach_flag_name.substring(0, 1).equals("\"")
						&& cost_attach_flag_name.substring(
								cost_attach_flag_name.length() - 1,
								cost_attach_flag_name.length()).equals("\"")) {
					cost_attach_flag_name = cost_attach_flag_name.substring(1,
							cost_attach_flag_name.length() - 1);
				}
				telInfo.setCost_attach_flag(BiCodeBsceService
						.getBiCodeBsceKeyByName("003", cost_attach_flag_name));

				telInfoService.saveOrUpdateModel(telInfo);
			}
		}

		map.put("result", "true");
		map.put("message", "保存成功");

		return map;
	}
}
