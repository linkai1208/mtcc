package com.sten.mtcc.controller;

import java.io.File;
import java.io.IOException;
import java.math.BigDecimal;
import java.sql.Timestamp;
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
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;
import org.xml.sax.SAXException;

import com.sten.framework.common.Pagination;
import com.sten.framework.common.XLSXCovertCSVReader;
import com.sten.framework.model.UploadFile;
import com.sten.framework.service.UploadFileService;
import com.sten.framework.util.DateTimeUtil;
import com.sten.framework.util.PropertiesUtil;
import com.sten.framework.util.StringUtil;
import com.sten.mtcc.model.BzTelCostDetail;
import com.sten.mtcc.model.BzTelCostIndex;
import com.sten.mtcc.model.BzTelCostList;
import com.sten.mtcc.service.UploadTelService;

/**
 * Created by MZQ on 2017/2/27.
 */
@RestController
@RequestMapping(value = "user")
public class UserController {
	private static final Logger logger = LoggerFactory
			.getLogger(UploadTelController.class);

	@Autowired
	private UploadTelService uploadTelService;
	@Autowired
	private UploadFileService uploadFileService;

	@RequestMapping(value = "/uploadTelImport", method = RequestMethod.GET)
	public ModelAndView uploadTelImport() {
		// ExcelReader.readXlsx("D:/7月份话费部分详单.xlsx");
		ModelAndView mv = new ModelAndView();
		mv.setViewName("/mtcc/compare/importTelCost");
		String systemDate = DateTimeUtil.getCurDateFShort();
		mv.addObject("period_year", systemDate.substring(0, 4));
		mv.addObject("period_month", systemDate.substring(5, 7));
		return mv;
	}

	@RequestMapping(value = "saveOrUpdateListfile", method = RequestMethod.POST)
	public Map<String, Object> saveOrUpdateListfile(BzTelCostIndex model)
			throws IntrusionException, ValidationException {
		Map<String, Object> map = new HashMap<String, Object>();
		boolean flg = false;
		String period_year = model.getPeriod_year();
		String period_month = model.getPeriod_month();
		if (new Integer(period_month).intValue() < 10) {
			period_month = "0" + period_month;
			model.setPeriod_month(period_month);
		}
		BzTelCostIndex costIndex = uploadTelService.getCostIndexByYearMonth(
				period_year, period_month);
		if (costIndex == null) {
			costIndex = new BzTelCostIndex();
		}
		costIndex.setPeriod_year(period_year);
		costIndex.setPeriod_month(period_month);
		costIndex.setList_file_id(model.getList_file_id());

		// 删除附件id最后的逗号
		if (StringUtil.isNotEmpty(costIndex.getList_file_id())) {
			if (costIndex.getList_file_id().endsWith(",")) {
				costIndex
						.setList_file_id(costIndex.getList_file_id().substring(
								0, costIndex.getList_file_id().length() - 1));
			}
		}
		// 数据导入库表
		if (StringUtil.isNotEmpty(costIndex.getList_file_id())) {
			String path = costIndex.getList_file_id();
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
				list = XLSXCovertCSVReader.readerExcel(safePath, "清单", 10);
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
			for (int i = 0; i < list.size(); i++) {
				String[] record = list.get(i);
				for (int j = 0; j < record.length; j++) {
					String cell = record[j];
					if (StringUtil.isEmpty(cell)) {
						map.put("result", "false");
						map.put("message", "数据不全，有空白项，清单无法导入。");
						return map;
					}
					if (i > 0 && j == 1) {
						String period = period_year + period_month;
						if (!period.equals(cell)) {
							map.put("result", "false");
							map.put("message", "上传的清单的年月和页面上设定的年月不一致。");
							return map;
						}
					}
					System.out.print(cell + "  ");
				}
				System.out.println();
			}
			for (int i = 1; i < list.size(); i++) {
				String[] record = list.get(i);
				BzTelCostList costList = new BzTelCostList();
				// 电话号码
				costList.setTel_number(record[0]);
				String period = record[1];
				costList.setPeriod_year(period.substring(0, 4));
				costList.setPeriod_month(period.substring(4, 6));
				// 套餐及固定费
				// costList.setFixed_costs();
				// 套餐外语音通信费
				costList.setVoice_costs(new BigDecimal(record[2]));
				// 套餐外上网费
				costList.setNet_costs(new BigDecimal(record[3]));
				// 套餐外短信/彩信费
				costList.setMessage_costs(new BigDecimal(record[4]));
				// 增值业务费
				costList.setAdded_costs(new BigDecimal(record[5]));
				// 代收业务费
				costList.setCollection_costs(new BigDecimal(record[6]));
				// 其他费用
				costList.setOther_costs(new BigDecimal(record[7]));
				// 优惠及减免
				costList.setDeduction_costs(new BigDecimal(record[8]));
				// 合计
				costList.setTotal_costs(new BigDecimal(record[9]));

				costList.setIs_valid("1");
				uploadTelService.saveOrUpdateCostList(costList);
			}
		}

		costIndex.setList_file_date(DateTimeUtil.getCurTimestamp());
		costIndex.setIs_valid("1");
		flg = uploadTelService.saveOrUpdateCostIndex(costIndex);
		if (flg) {
			// 更新附件表
			List<UploadFile> list = new ArrayList<UploadFile>();
			if (StringUtil.isNotEmpty(costIndex.getList_file_id())) {
				String path = costIndex.getList_file_id();
				UploadFile uploadFile = uploadFileService.getFile(path);
				uploadFile.setForeignId(costIndex.getUuid());
				uploadFile.setState(1);
				list.add(uploadFile);
			}

			// 更新到数据库
			if (list.size() > 0) {
				flg = uploadFileService.updateFiles(list);
			}
			if (flg) {
				map.put("result", "true");
				map.put("message", "保存成功");
				map.put("uuid", model.getUuid());
			} else {
				map.put("result", "false");
				map.put("message", "保存失败");
			}
		} else {
			map.put("result", "false");
			map.put("message", "保存失败");
		}
		return map;
	}

	@RequestMapping(value = "saveOrUpdateDetailfile", method = RequestMethod.POST)
	public Map<String, Object> saveOrUpdateDetailfile(BzTelCostIndex model)
			throws IntrusionException, ValidationException {
		Map<String, Object> map = new HashMap<String, Object>();
		boolean flg = false;
		String period_year = model.getPeriod_year();
		String period_month = model.getPeriod_month();
		if (new Integer(period_month).intValue() < 10) {
			period_month = "0" + period_month;
			model.setPeriod_month(period_month);
		}
		BzTelCostIndex costIndex = uploadTelService.getCostIndexByYearMonth(
				period_year, period_month);
		if (costIndex == null) {
			costIndex = new BzTelCostIndex();
		}
		costIndex.setPeriod_year(period_year);
		costIndex.setPeriod_month(period_month);
		costIndex.setDetail_file_id(model.getDetail_file_id());

		// 删除附件id最后的逗号
		if (StringUtil.isNotEmpty(costIndex.getDetail_file_id())) {
			if (costIndex.getDetail_file_id().endsWith(",")) {
				costIndex.setDetail_file_id(costIndex.getDetail_file_id()
						.substring(0,
								costIndex.getDetail_file_id().length() - 1));
			}
		}
		// 数据导入库表
		if (StringUtil.isNotEmpty(costIndex.getDetail_file_id())) {
			String path = costIndex.getDetail_file_id();
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
				list = XLSXCovertCSVReader.readerExcel(safePath, "详单", 9);
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
			for (int i = 0; i < list.size(); i++) {
				String[] record = list.get(i);
				for (int j = 0; j < record.length; j++) {
					String cell = record[j];
					if (StringUtil.isEmpty(cell)) {
						map.put("result", "false");
						map.put("message", "数据不全，有空白项，详单无法导入。");
						return map;
					}
					if (i > 0 && j == 0) {
						String month = cell.substring(1, 3);
						if (new Integer(period_month).intValue() != new Integer(
								month).intValue()) {
							map.put("result", "false");
							map.put("message", "上传的详单的月份和页面上设定的月份不一致。");
							return map;
						}
					}
					System.out.print(cell + "  ");
				}
				System.out.println();
			}
			for (int i = 1; i < list.size(); i++) {
				String[] record = list.get(i);
				BzTelCostDetail costDetail = new BzTelCostDetail();
				// 电话号码
				costDetail.setTel_number(record[7]);
				costDetail.setPeriod_year(period_year);
				costDetail.setPeriod_month(period_month);
				// 通话时间_原始
				costDetail.setHoding_time_orig(record[0].substring(1,
						record[0].length() - 1));
				// 转换成timestamp
				String strHoding_time = period_year + "-"
						+ costDetail.getHoding_time_orig().substring(0, 2)
						+ "-"
						+ costDetail.getHoding_time_orig().substring(3, 5)
						+ " " + costDetail.getHoding_time_orig().substring(6);
				Timestamp hoding_time = DateTimeUtil
						.getFormatString2Date(strHoding_time);
				costDetail.setHoding_time(hoding_time);
				// 通话时长_原始
				costDetail.setCall_duration_orig(record[1].substring(1,
						record[1].length() - 1));
				// 转换成秒
				int call_duration = 0;
				if (costDetail.getCall_duration_orig().length() == 6) {
					call_duration = new Integer(costDetail
							.getCall_duration_orig().substring(0, 2))
							.intValue()
							* 60
							+ new Integer(costDetail.getCall_duration_orig()
									.substring(3, 5)).intValue();
				} else if (costDetail.getCall_duration_orig().length() == 3) {
					call_duration = new Integer(costDetail
							.getCall_duration_orig().substring(0, 2))
							.intValue();
				}
				costDetail.setCall_duration(call_duration);
				// 通话类型_名称
				costDetail.setCall_type_name(record[2].substring(1,
						record[2].length() - 1));
				// 对方号码
				costDetail.setCalled_number(record[3]);
				// 长途类型名称
				costDetail.setLong_distance_type_name(record[4].substring(1,
						record[4].length() - 1));
				// 基本通话费
				costDetail.setBasic_costs(new BigDecimal(record[5]));
				// 长途费
				costDetail.setLong_costs(new BigDecimal(record[6]));
				// 费用所属标识
				costDetail.setCost_attach_flag(record[8].substring(1,
						record[8].length() - 1));

				costDetail.setIs_valid("1");
				uploadTelService.saveOrUpdateCostDetail(costDetail);
			}
		}

		costIndex.setDetail_file_date(DateTimeUtil.getCurTimestamp());
		costIndex.setIs_valid("1");
		flg = uploadTelService.saveOrUpdateCostIndex(costIndex);
		if (flg) {
			// 更新附件表
			List<UploadFile> list = new ArrayList<UploadFile>();
			if (StringUtil.isNotEmpty(costIndex.getDetail_file_id())) {
				String path = costIndex.getDetail_file_id();
				UploadFile uploadFile = uploadFileService.getFile(path);
				uploadFile.setForeignId(costIndex.getUuid());
				uploadFile.setState(1);
				list.add(uploadFile);
			}

			// 更新到数据库
			if (list.size() > 0) {
				flg = uploadFileService.updateFiles(list);
			}
			if (flg) {
				map.put("result", "true");
				map.put("message", "保存成功");
				map.put("uuid", model.getUuid());
			} else {
				map.put("result", "false");
				map.put("message", "保存失败");
			}
		} else {
			map.put("result", "false");
			map.put("message", "保存失败");
		}
		return map;
	}

	@RequestMapping(value = "/uploadTelList", method = RequestMethod.GET)
	public ModelAndView uploadTelList() {
		ModelAndView mv = new ModelAndView();
		mv.setViewName("/mtcc/compare/telCostList");
		String systemDate = DateTimeUtil.getCurDateFShort();
		mv.addObject("search_year", systemDate.substring(0, 4));
		mv.addObject("search_month", systemDate.substring(5, 7));
		return mv;
	}

	@RequestMapping(value = "/view/uploadTelList/load", method = RequestMethod.POST)
	public Map<String, Object> easyuiGridLoad(Pagination pagination) {
		Map<String, Object> map = new HashMap<String, Object>();
		map = uploadTelService.loadByPage(pagination);
		return map;
	}

	@RequestMapping(value = "view/importList", method = RequestMethod.GET)
	public ModelAndView importList(String doc, String uuid, String fileId) {
		ModelAndView mv = new ModelAndView();
		mv.addObject("docName", doc);
		mv.addObject("uuid", uuid);

		List<UploadFile> list = null;
		list = uploadFileService.loadFiles(uuid, doc, 1);
		if (list == null) {
			list = new ArrayList<UploadFile>();
		}
		String readOnly = "false";
		mv.addObject("list", list);
		mv.addObject("readOnly", readOnly);

		mv.setViewName("/mtcc/compare/importTelCost");
		return mv;
	}

}
