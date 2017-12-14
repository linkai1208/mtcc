package com.sten.mtcc.common;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URLDecoder;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.sten.framework.util.DateTimeUtil;
import com.sten.framework.util.FileUtil;
import com.sten.framework.util.StringUtil;

/**
 * Created by mazq on 2017/8/4.
 */
public class ExportExcel {
	private static final Logger logger = LoggerFactory
			.getLogger(ExportExcel.class);

	/**
	 * 出力无人机注册用户统计
	 */
	public static String registUserOutExcel(List list, String type)
			throws IOException {
		String url = "";

		// 方式二、打开已经存在的excel
		HSSFWorkbook workbook = null;
		InputStream inputStream = null;
		OutputStream outputStream = null;
		String fileName = "";
		String path = "";
		try {
			String template = FileUtil.getWebRootPath() + "includes/template/"
					+ type + ".xls";
			template = URLDecoder.decode(template, "UTF-8");
			File fileTemplate = new File(template);
			inputStream = new FileInputStream(fileTemplate);
			workbook = new HSSFWorkbook(inputStream);
			HSSFSheet sheet = workbook.getSheetAt(0);
			HSSFRow row;

			Map tempMap;
			String areaFlag;
			int totalGR = 0;
			int totalQY = 0;
			int totalSY = 0;
			int totalJG = 0;
			int totalCS = 0;
			// 表头部分处理
			int beginRow = 2;
			int colIndex = 0;
			for (int i = 0; i < list.size(); i++) {
				colIndex = 0;
				row = sheet.createRow(beginRow + i);
				tempMap = (Map) list.get(i);
				areaFlag = StringUtil.toString(tempMap.get("areaFlag"));
				if ("1".equals(areaFlag)) {
					row.createCell(colIndex++).setCellValue(
							StringUtil.toString(tempMap.get("area_region")));
				} else {
					row.createCell(colIndex++).setCellValue(
							"    "
									+ StringUtil.toString(tempMap
											.get("area_region")));
					totalGR += Integer.parseInt(StringUtil.toString(tempMap
							.get("gr")));
					totalQY += Integer.parseInt(StringUtil.toString(tempMap
							.get("qy")));
					totalSY += Integer.parseInt(StringUtil.toString(tempMap
							.get("sy")));
					totalJG += Integer.parseInt(StringUtil.toString(tempMap
							.get("jg")));
					totalCS += Integer.parseInt(StringUtil.toString(tempMap
							.get("cs")));
				}
				row.createCell(colIndex++).setCellValue(
						StringUtil.toString(tempMap.get("gr")));
				row.createCell(colIndex++).setCellValue(
						StringUtil.toString(tempMap.get("qy")));
				row.createCell(colIndex++).setCellValue(
						StringUtil.toString(tempMap.get("sy")));
				row.createCell(colIndex++).setCellValue(
						StringUtil.toString(tempMap.get("jg")));
				row.createCell(colIndex++).setCellValue(
						StringUtil.toString(tempMap.get("cs")));
			}

			colIndex = 0;
			row = sheet.createRow(beginRow + list.size());
			row.createCell(colIndex++).setCellValue("合计");
			row.createCell(colIndex++).setCellValue(totalGR);
			row.createCell(colIndex++).setCellValue(totalQY);
			row.createCell(colIndex++).setCellValue(totalSY);
			row.createCell(colIndex++).setCellValue(totalJG);
			row.createCell(colIndex++).setCellValue(totalCS);

			// 生成文件
			fileName = type + "_"
					+ DateTimeUtil.formatDateToString(new Date(), "yyyyMMdd_")
					+ UUID.randomUUID().toString().substring(0, 5) + ".xls";
			path = FileUtil.getWebRootPath() + "temps/";
			path = URLDecoder.decode(path, "UTF-8");

			File file = new File(path);
			if (!file.exists()) {
				file.mkdirs();
			}

			outputStream = new FileOutputStream(path + fileName);
			workbook.write(outputStream);
			url = "/temps/" + fileName;
			logger.info("导出excel " + path + fileName);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
			logger.error(e.toString());
		} catch (IOException e) {
			e.printStackTrace();
			logger.error(e.toString());
		} finally {
			if (inputStream != null) {
				try {
					inputStream.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			if (outputStream != null) {
				try {
					outputStream.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			if (workbook != null) {
				try {
					workbook.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		return url;
	}

	/**
	 * 出力无人机注册用户统计
	 */
	public static String factoryOutExcel(List list, String type)
			throws IOException {
		String url = "";

		// 方式二、打开已经存在的excel
		HSSFWorkbook workbook = null;
		InputStream inputStream = null;
		OutputStream outputStream = null;
		String fileName = "";
		String path = "";
		try {
			String template = FileUtil.getWebRootPath() + "includes/template/"
					+ type + ".xls";
			template = URLDecoder.decode(template, "UTF-8");
			File fileTemplate = new File(template);
			inputStream = new FileInputStream(fileTemplate);
			workbook = new HSSFWorkbook(inputStream);
			HSSFSheet sheet = workbook.getSheetAt(0);
			HSSFRow row;

			Map tempMap;
			String areaFlag;
			int totalCS = 0;
			int totalGD = 0;
			int totalDanX = 0;
			int totalDuoX = 0;
			int totalFT = 0;
			int totalQT = 0;

			// 表头部分处理
			int beginRow = 3;
			int colIndex = 0;
			for (int i = 0; i < list.size(); i++) {
				colIndex = 0;
				row = sheet.createRow(beginRow + i);
				tempMap = (Map) list.get(i);
				areaFlag = StringUtil.toString(tempMap.get("areaFlag"));
				if ("1".equals(areaFlag)) {
					row.createCell(colIndex++).setCellValue(
							StringUtil.toString(tempMap.get("area_region")));
				} else {
					row.createCell(colIndex++).setCellValue(
							"    "
									+ StringUtil.toString(tempMap
											.get("area_region")));
					totalCS += Integer.parseInt(StringUtil.toString(tempMap
							.get("cs")));
					totalGD += Integer.parseInt(StringUtil.toString(tempMap
							.get("gd")));
					totalDanX += Integer.parseInt(StringUtil.toString(tempMap
							.get("danx")));
					totalDuoX += Integer.parseInt(StringUtil.toString(tempMap
							.get("duox")));
					totalFT += Integer.parseInt(StringUtil.toString(tempMap
							.get("ft")));
					totalQT += Integer.parseInt(StringUtil.toString(tempMap
							.get("qt")));
				}
				row.createCell(colIndex++).setCellValue(
						StringUtil.toString(tempMap.get("cs")));
				row.createCell(colIndex++).setCellValue(
						StringUtil.toString(tempMap.get("gd")));
				row.createCell(colIndex++).setCellValue(
						StringUtil.toString(tempMap.get("danx")));
				row.createCell(colIndex++).setCellValue(
						StringUtil.toString(tempMap.get("duox")));
				row.createCell(colIndex++).setCellValue(
						StringUtil.toString(tempMap.get("ft")));
				row.createCell(colIndex++).setCellValue(
						StringUtil.toString(tempMap.get("qt")));
			}

			colIndex = 0;
			row = sheet.createRow(beginRow + list.size());
			row.createCell(colIndex++).setCellValue("合计");
			row.createCell(colIndex++).setCellValue(totalCS);
			row.createCell(colIndex++).setCellValue(totalGD);
			row.createCell(colIndex++).setCellValue(totalDanX);
			row.createCell(colIndex++).setCellValue(totalDuoX);
			row.createCell(colIndex++).setCellValue(totalFT);
			row.createCell(colIndex++).setCellValue(totalQT);

			// 生成文件
			fileName = type + "_"
					+ DateTimeUtil.formatDateToString(new Date(), "yyyyMMdd_")
					+ UUID.randomUUID().toString().substring(0, 5) + ".xls";
			path = FileUtil.getWebRootPath() + "temps/";
			path = URLDecoder.decode(path, "UTF-8");

			File file = new File(path);
			if (!file.exists()) {
				file.mkdirs();
			}

			outputStream = new FileOutputStream(path + fileName);
			workbook.write(outputStream);
			url = "/temps/" + fileName;
			logger.info("导出excel " + path + fileName);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
			logger.error(e.toString());
		} catch (IOException e) {
			e.printStackTrace();
			logger.error(e.toString());
		} finally {
			if (inputStream != null) {
				try {
					inputStream.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			if (outputStream != null) {
				try {
					outputStream.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			if (workbook != null) {
				try {
					workbook.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		return url;
	}

	/**
	 * 出力无人机注册情况统计
	 */
	public static String uavRegistOutExcel(List list, String type)
			throws IOException {
		String url = "";

		// 方式二、打开已经存在的excel
		HSSFWorkbook workbook = null;
		InputStream inputStream = null;
		OutputStream outputStream = null;
		String fileName = "";
		String path = "";
		try {
			String template = FileUtil.getWebRootPath() + "includes/template/"
					+ type + ".xls";
			template = URLDecoder.decode(template, "UTF-8");
			File fileTemplate = new File(template);
			inputStream = new FileInputStream(fileTemplate);
			workbook = new HSSFWorkbook(inputStream);
			HSSFSheet sheet = workbook.getSheetAt(0);
			HSSFRow row;

			Map tempMap;
			String areaFlag;
			int totalRnum = 0;
			int totalUnum = 0;

			// 表头部分处理
			int beginRow = 2;
			int colIndex = 0;
			for (int i = 0; i < list.size(); i++) {
				colIndex = 0;
				row = sheet.createRow(beginRow + i);
				tempMap = (Map) list.get(i);
				areaFlag = StringUtil.toString(tempMap.get("areaFlag"));
				if ("1".equals(areaFlag)) {
					row.createCell(colIndex++).setCellValue(
							StringUtil.toString(tempMap.get("area_region")));
				} else {
					row.createCell(colIndex++).setCellValue(
							"    "
									+ StringUtil.toString(tempMap
											.get("area_region")));
					totalRnum += Integer.parseInt(StringUtil.toString(tempMap
							.get("regist_num")));
					totalUnum += Integer.parseInt(StringUtil.toString(tempMap
							.get("unregist_num")));
				}
				row.createCell(colIndex++).setCellValue(
						StringUtil.toString(tempMap.get("regist_num")));
				row.createCell(colIndex++).setCellValue(
						StringUtil.toString(tempMap.get("unregist_num")));
			}

			colIndex = 0;
			row = sheet.createRow(beginRow + list.size());
			row.createCell(colIndex++).setCellValue("合计");
			row.createCell(colIndex++).setCellValue(totalRnum);
			row.createCell(colIndex++).setCellValue(totalUnum);

			// 生成文件
			fileName = type + "_"
					+ DateTimeUtil.formatDateToString(new Date(), "yyyyMMdd_")
					+ UUID.randomUUID().toString().substring(0, 5) + ".xls";
			path = FileUtil.getWebRootPath() + "temps/";
			path = URLDecoder.decode(path, "UTF-8");

			File file = new File(path);
			if (!file.exists()) {
				file.mkdirs();
			}

			outputStream = new FileOutputStream(path + fileName);
			workbook.write(outputStream);
			url = "/temps/" + fileName;
			logger.info("导出excel " + path + fileName);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
			logger.error(e.toString());
		} catch (IOException e) {
			e.printStackTrace();
			logger.error(e.toString());
		} finally {
			if (inputStream != null) {
				try {
					inputStream.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			if (outputStream != null) {
				try {
					outputStream.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			if (workbook != null) {
				try {
					workbook.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		return url;
	}

	/**
	 * 出力无人机注册按类型统计
	 */
	public static String uavRegistByTypeOutExcel(List list, String type)
			throws IOException {
		String url = "";

		// 方式二、打开已经存在的excel
		HSSFWorkbook workbook = null;
		InputStream inputStream = null;
		OutputStream outputStream = null;
		String fileName = "";
		String path = "";
		try {
			String template = FileUtil.getWebRootPath() + "includes/template/"
					+ type + ".xls";
			template = URLDecoder.decode(template, "UTF-8");
			File fileTemplate = new File(template);
			inputStream = new FileInputStream(fileTemplate);
			workbook = new HSSFWorkbook(inputStream);
			HSSFSheet sheet = workbook.getSheetAt(0);
			HSSFRow row;

			Map tempMap;
			String areaFlag;
			int totalPP = 0;
			int totalZZ = 0;

			// 表头部分处理
			int beginRow = 3;
			int colIndex = 0;
			for (int i = 0; i < list.size(); i++) {
				colIndex = 0;
				row = sheet.createRow(beginRow + i);
				tempMap = (Map) list.get(i);
				areaFlag = StringUtil.toString(tempMap.get("areaFlag"));
				if ("1".equals(areaFlag)) {
					row.createCell(colIndex++).setCellValue(
							StringUtil.toString(tempMap.get("area_region")));
				} else {
					row.createCell(colIndex++).setCellValue(
							"    "
									+ StringUtil.toString(tempMap
											.get("area_region")));
					totalPP += Integer.parseInt(StringUtil.toString(tempMap
							.get("pp")));
					totalZZ += Integer.parseInt(StringUtil.toString(tempMap
							.get("zz")));
				}
				row.createCell(colIndex++).setCellValue(
						StringUtil.toString(tempMap.get("pp")));
				row.createCell(colIndex++).setCellValue(
						StringUtil.toString(tempMap.get("zz")));
				row.createCell(colIndex++)
						.setCellValue(
								Integer.parseInt(StringUtil.toString(tempMap
										.get("pp")))
										+ Integer.parseInt(StringUtil
												.toString(tempMap.get("zz"))));
			}

			colIndex = 0;
			row = sheet.createRow(beginRow + list.size());
			row.createCell(colIndex++).setCellValue("合计");
			row.createCell(colIndex++).setCellValue(totalPP);
			row.createCell(colIndex++).setCellValue(totalZZ);
			row.createCell(colIndex++).setCellValue(totalPP + totalZZ);
			;

			// 生成文件
			fileName = type + "_"
					+ DateTimeUtil.formatDateToString(new Date(), "yyyyMMdd_")
					+ UUID.randomUUID().toString().substring(0, 5) + ".xls";
			path = FileUtil.getWebRootPath() + "temps/";
			path = URLDecoder.decode(path, "UTF-8");

			File file = new File(path);
			if (!file.exists()) {
				file.mkdirs();
			}

			outputStream = new FileOutputStream(path + fileName);
			workbook.write(outputStream);
			url = "/temps/" + fileName;
			logger.info("导出excel " + path + fileName);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
			logger.error(e.toString());
		} catch (IOException e) {
			e.printStackTrace();
			logger.error(e.toString());
		} finally {
			if (inputStream != null) {
				try {
					inputStream.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			if (outputStream != null) {
				try {
					outputStream.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			if (workbook != null) {
				try {
					workbook.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		return url;
	}

	/**
	 * 出力无人机注册按用途统计
	 */
	public static String uavRegistByPurposeOutExcel(List list, String type)
			throws IOException {
		String url = "";

		// 方式二、打开已经存在的excel
		HSSFWorkbook workbook = null;
		InputStream inputStream = null;
		OutputStream outputStream = null;
		String fileName = "";
		String path = "";
		try {
			String template = FileUtil.getWebRootPath() + "includes/template/"
					+ type + ".xls";
			template = URLDecoder.decode(template, "UTF-8");
			File fileTemplate = new File(template);
			inputStream = new FileInputStream(fileTemplate);
			workbook = new HSSFWorkbook(inputStream);
			HSSFSheet sheet = workbook.getSheetAt(0);
			HSSFRow row;

			Map tempMap;
			String areaFlag;
			int totalYL = 0;
			int totalSF = 0;
			int totalQT = 0;

			// 表头部分处理
			int beginRow = 3;
			int colIndex = 0;
			for (int i = 0; i < list.size(); i++) {
				colIndex = 0;
				row = sheet.createRow(beginRow + i);
				tempMap = (Map) list.get(i);
				areaFlag = StringUtil.toString(tempMap.get("areaFlag"));
				if ("1".equals(areaFlag)) {
					row.createCell(colIndex++).setCellValue(
							StringUtil.toString(tempMap.get("area_region")));
				} else {
					row.createCell(colIndex++).setCellValue(
							"    "
									+ StringUtil.toString(tempMap
											.get("area_region")));
					totalYL += Integer.parseInt(StringUtil.toString(tempMap
							.get("yl")));
					totalSF += Integer.parseInt(StringUtil.toString(tempMap
							.get("sf")));
					totalQT += Integer.parseInt(StringUtil.toString(tempMap
							.get("qt")));
				}
				row.createCell(colIndex++).setCellValue(
						StringUtil.toString(tempMap.get("yl")));
				row.createCell(colIndex++).setCellValue(
						StringUtil.toString(tempMap.get("sf")));
				row.createCell(colIndex++).setCellValue(
						StringUtil.toString(tempMap.get("qt")));
				row.createCell(colIndex++)
						.setCellValue(
								Integer.parseInt(StringUtil.toString(tempMap
										.get("yl")))
										+ Integer.parseInt(StringUtil
												.toString(tempMap.get("sf")))
										+ Integer.parseInt(StringUtil
												.toString(tempMap.get("qt"))));
			}

			colIndex = 0;
			row = sheet.createRow(beginRow + list.size());
			row.createCell(colIndex++).setCellValue("合计");
			row.createCell(colIndex++).setCellValue(totalYL);
			row.createCell(colIndex++).setCellValue(totalSF);
			row.createCell(colIndex++).setCellValue(totalQT);
			row.createCell(colIndex++)
					.setCellValue(totalYL + totalSF + totalQT);
			;

			// 生成文件
			fileName = type + "_"
					+ DateTimeUtil.formatDateToString(new Date(), "yyyyMMdd_")
					+ UUID.randomUUID().toString().substring(0, 5) + ".xls";
			path = FileUtil.getWebRootPath() + "temps/";
			path = URLDecoder.decode(path, "UTF-8");

			File file = new File(path);
			if (!file.exists()) {
				file.mkdirs();
			}

			outputStream = new FileOutputStream(path + fileName);
			workbook.write(outputStream);
			url = "/temps/" + fileName;
			logger.info("导出excel " + path + fileName);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
			logger.error(e.toString());
		} catch (IOException e) {
			e.printStackTrace();
			logger.error(e.toString());
		} finally {
			if (inputStream != null) {
				try {
					inputStream.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			if (outputStream != null) {
				try {
					outputStream.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			if (workbook != null) {
				try {
					workbook.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		return url;
	}

	/**
	 * 出力无人机注册按机型统计
	 */
	public static String uavRegistByUTypeOutExcel(List list, String type)
			throws IOException {
		String url = "";

		// 方式二、打开已经存在的excel
		HSSFWorkbook workbook = null;
		InputStream inputStream = null;
		OutputStream outputStream = null;
		String fileName = "";
		String path = "";
		try {
			String template = FileUtil.getWebRootPath() + "includes/template/"
					+ type + ".xls";
			template = URLDecoder.decode(template, "UTF-8");
			File fileTemplate = new File(template);
			inputStream = new FileInputStream(fileTemplate);
			workbook = new HSSFWorkbook(inputStream);
			HSSFSheet sheet = workbook.getSheetAt(0);
			HSSFRow row;

			Map tempMap;
			String areaFlag;
			int totalGD = 0;
			int totalDanX = 0;
			int totalDuoX = 0;
			int totalFT = 0;
			int totalQT = 0;

			// 表头部分处理
			int beginRow = 3;
			int colIndex = 0;
			for (int i = 0; i < list.size(); i++) {
				colIndex = 0;
				row = sheet.createRow(beginRow + i);
				tempMap = (Map) list.get(i);
				areaFlag = StringUtil.toString(tempMap.get("areaFlag"));
				if ("1".equals(areaFlag)) {
					row.createCell(colIndex++).setCellValue(
							StringUtil.toString(tempMap.get("area_region")));
				} else {
					row.createCell(colIndex++).setCellValue(
							"    "
									+ StringUtil.toString(tempMap
											.get("area_region")));
					totalGD += Integer.parseInt(StringUtil.toString(tempMap
							.get("gd")));
					totalDanX += Integer.parseInt(StringUtil.toString(tempMap
							.get("danx")));
					totalDuoX += Integer.parseInt(StringUtil.toString(tempMap
							.get("duox")));
					totalFT += Integer.parseInt(StringUtil.toString(tempMap
							.get("ft")));
					totalQT += Integer.parseInt(StringUtil.toString(tempMap
							.get("qt")));
				}
				row.createCell(colIndex++).setCellValue(
						StringUtil.toString(tempMap.get("gd")));
				row.createCell(colIndex++).setCellValue(
						StringUtil.toString(tempMap.get("danx")));
				row.createCell(colIndex++).setCellValue(
						StringUtil.toString(tempMap.get("duox")));
				row.createCell(colIndex++).setCellValue(
						StringUtil.toString(tempMap.get("ft")));
				row.createCell(colIndex++).setCellValue(
						StringUtil.toString(tempMap.get("qt")));
				row.createCell(colIndex++)
						.setCellValue(
								Integer.parseInt(StringUtil.toString(tempMap
										.get("gd")))
										+ Integer.parseInt(StringUtil
												.toString(tempMap.get("danx")))
										+ Integer.parseInt(StringUtil
												.toString(tempMap.get("duox")))
										+ Integer.parseInt(StringUtil
												.toString(tempMap.get("ft")))
										+ Integer.parseInt(StringUtil
												.toString(tempMap.get("qt"))));
			}

			colIndex = 0;
			row = sheet.createRow(beginRow + list.size());
			row.createCell(colIndex++).setCellValue("合计");
			row.createCell(colIndex++).setCellValue(totalGD);
			row.createCell(colIndex++).setCellValue(totalDanX);
			row.createCell(colIndex++).setCellValue(totalDuoX);
			row.createCell(colIndex++).setCellValue(totalFT);
			row.createCell(colIndex++).setCellValue(totalQT);
			row.createCell(colIndex++).setCellValue(
					totalGD + totalDanX + totalDuoX + totalFT + totalQT);
			;

			// 生成文件
			fileName = type + "_"
					+ DateTimeUtil.formatDateToString(new Date(), "yyyyMMdd_")
					+ UUID.randomUUID().toString().substring(0, 5) + ".xls";
			path = FileUtil.getWebRootPath() + "temps/";
			path = URLDecoder.decode(path, "UTF-8");

			File file = new File(path);
			if (!file.exists()) {
				file.mkdirs();
			}

			outputStream = new FileOutputStream(path + fileName);
			workbook.write(outputStream);
			url = "/temps/" + fileName;
			logger.info("导出excel " + path + fileName);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
			logger.error(e.toString());
		} catch (IOException e) {
			e.printStackTrace();
			logger.error(e.toString());
		} finally {
			if (inputStream != null) {
				try {
					inputStream.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			if (outputStream != null) {
				try {
					outputStream.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			if (workbook != null) {
				try {
					workbook.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		return url;
	}

	/**
	 * 出力无人机注册按最大起飞重量统计
	 */
	public static String uavRegistByFlyWeightOutExcel(List list, String type)
			throws IOException {
		String url = "";

		// 方式二、打开已经存在的excel
		HSSFWorkbook workbook = null;
		InputStream inputStream = null;
		OutputStream outputStream = null;
		String fileName = "";
		String path = "";
		try {
			String template = FileUtil.getWebRootPath() + "includes/template/"
					+ type + ".xls";
			template = URLDecoder.decode(template, "UTF-8");
			File fileTemplate = new File(template);
			inputStream = new FileInputStream(fileTemplate);
			workbook = new HSSFWorkbook(inputStream);
			HSSFSheet sheet = workbook.getSheetAt(0);
			HSSFRow row;

			Map tempMap;
			String areaFlag;
			int total1 = 0;
			int total2 = 0;
			int total3 = 0;
			int total4 = 0;
			int total5 = 0;
			int total6 = 0;

			// 表头部分处理
			int beginRow = 3;
			int colIndex = 0;
			for (int i = 0; i < list.size(); i++) {
				colIndex = 0;
				row = sheet.createRow(beginRow + i);
				tempMap = (Map) list.get(i);
				areaFlag = StringUtil.toString(tempMap.get("areaFlag"));
				if ("1".equals(areaFlag)) {
					row.createCell(colIndex++).setCellValue(
							StringUtil.toString(tempMap.get("area_region")));
				} else {
					row.createCell(colIndex++).setCellValue(
							"    "
									+ StringUtil.toString(tempMap
											.get("area_region")));
					total1 += Integer.parseInt(StringUtil.toString(tempMap
							.get("total1")));
					total2 += Integer.parseInt(StringUtil.toString(tempMap
							.get("total2")));
					total3 += Integer.parseInt(StringUtil.toString(tempMap
							.get("total3")));
					total4 += Integer.parseInt(StringUtil.toString(tempMap
							.get("total4")));
					total5 += Integer.parseInt(StringUtil.toString(tempMap
							.get("total5")));
					total6 += Integer.parseInt(StringUtil.toString(tempMap
							.get("total6")));
				}
				row.createCell(colIndex++).setCellValue(
						StringUtil.toString(tempMap.get("total1")));
				row.createCell(colIndex++).setCellValue(
						StringUtil.toString(tempMap.get("total2")));
				row.createCell(colIndex++).setCellValue(
						StringUtil.toString(tempMap.get("total3")));
				row.createCell(colIndex++).setCellValue(
						StringUtil.toString(tempMap.get("total4")));
				row.createCell(colIndex++).setCellValue(
						StringUtil.toString(tempMap.get("total5")));
				row.createCell(colIndex++).setCellValue(
						StringUtil.toString(tempMap.get("total6")));
				row.createCell(colIndex++).setCellValue(
						Integer.parseInt(StringUtil.toString(tempMap
								.get("total1")))
								+ Integer.parseInt(StringUtil.toString(tempMap
										.get("total2")))
								+ Integer.parseInt(StringUtil.toString(tempMap
										.get("total3")))
								+ Integer.parseInt(StringUtil.toString(tempMap
										.get("total4")))
								+ Integer.parseInt(StringUtil.toString(tempMap
										.get("total5")))
								+ Integer.parseInt(StringUtil.toString(tempMap
										.get("total6"))));
			}

			colIndex = 0;
			row = sheet.createRow(beginRow + list.size());
			row.createCell(colIndex++).setCellValue("合计");
			row.createCell(colIndex++).setCellValue(total1);
			row.createCell(colIndex++).setCellValue(total2);
			row.createCell(colIndex++).setCellValue(total3);
			row.createCell(colIndex++).setCellValue(total4);
			row.createCell(colIndex++).setCellValue(total5);
			row.createCell(colIndex++).setCellValue(total6);
			row.createCell(colIndex++).setCellValue(
					total1 + total2 + total3 + total4 + total5 + total6);
			;

			// 生成文件
			fileName = type + "_"
					+ DateTimeUtil.formatDateToString(new Date(), "yyyyMMdd_")
					+ UUID.randomUUID().toString().substring(0, 5) + ".xls";
			path = FileUtil.getWebRootPath() + "temps/";
			path = URLDecoder.decode(path, "UTF-8");

			File file = new File(path);
			if (!file.exists()) {
				file.mkdirs();
			}

			outputStream = new FileOutputStream(path + fileName);
			workbook.write(outputStream);
			url = "/temps/" + fileName;
			logger.info("导出excel " + path + fileName);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
			logger.error(e.toString());
		} catch (IOException e) {
			e.printStackTrace();
			logger.error(e.toString());
		} finally {
			if (inputStream != null) {
				try {
					inputStream.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			if (outputStream != null) {
				try {
					outputStream.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			if (workbook != null) {
				try {
					workbook.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		return url;
	}

	/**
	 * 出力无人机注册按厂商统计
	 */
	public static String uavRegistByFactoryOutExcel(List list, String type)
			throws IOException {
		String url = "";

		// 方式二、打开已经存在的excel
		HSSFWorkbook workbook = null;
		InputStream inputStream = null;
		OutputStream outputStream = null;
		String fileName = "";
		String path = "";
		try {
			String template = FileUtil.getWebRootPath() + "includes/template/"
					+ type + ".xls";
			template = URLDecoder.decode(template, "UTF-8");
			File fileTemplate = new File(template);
			inputStream = new FileInputStream(fileTemplate);
			workbook = new HSSFWorkbook(inputStream);
			HSSFSheet sheet = workbook.getSheetAt(0);
			HSSFRow row;

			Map tempMap;
			String flag;
			int total = 0;

			// 表头部分处理
			int beginRow = 2;
			int colIndex = 0;
			for (int i = 0; i < list.size(); i++) {
				colIndex = 0;
				row = sheet.createRow(beginRow + i);
				tempMap = (Map) list.get(i);
				flag = StringUtil.toString(tempMap.get("flag"));
				if ("1".equals(flag)) {
					row.createCell(colIndex++).setCellValue(
							StringUtil.toString(tempMap.get("factory_model")));
				} else {
					row.createCell(colIndex++).setCellValue(
							"    "
									+ StringUtil.toString(tempMap
											.get("factory_model")));
					total += Integer.parseInt(StringUtil.toString(tempMap
							.get("total")));
				}
				row.createCell(colIndex++).setCellValue(
						StringUtil.toString(tempMap.get("total")));
			}

			colIndex = 0;
			row = sheet.createRow(beginRow + list.size());
			row.createCell(colIndex++).setCellValue("合计");
			row.createCell(colIndex++).setCellValue(total);

			// 生成文件
			fileName = type + "_"
					+ DateTimeUtil.formatDateToString(new Date(), "yyyyMMdd_")
					+ UUID.randomUUID().toString().substring(0, 5) + ".xls";
			path = FileUtil.getWebRootPath() + "temps/";
			path = URLDecoder.decode(path, "UTF-8");

			File file = new File(path);
			if (!file.exists()) {
				file.mkdirs();
			}

			outputStream = new FileOutputStream(path + fileName);
			workbook.write(outputStream);
			url = "/temps/" + fileName;
			logger.info("导出excel " + path + fileName);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
			logger.error(e.toString());
		} catch (IOException e) {
			e.printStackTrace();
			logger.error(e.toString());
		} finally {
			if (inputStream != null) {
				try {
					inputStream.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			if (outputStream != null) {
				try {
					outputStream.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			if (workbook != null) {
				try {
					workbook.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		return url;
	}
}
