package com.sten.mtcc.controller;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Created by MZQ on 2017/2/27.
 */
@RestController
@RequestMapping(value = "download")
public class DownloadController {
	private static final Logger logger = LoggerFactory
			.getLogger(DownloadController.class);

	@RequestMapping("/downloadFile/{type}")
	public String download(@PathVariable String type,
			HttpServletRequest request, HttpServletResponse response) {
		String fileName = "";
		String realName = "";
		if ("1".equals(type)) {
			fileName = "话费清单模板.xlsx";
			try {
				realName = URLEncoder.encode(fileName, "UTF-8");
			} catch (UnsupportedEncodingException e) {
				e.printStackTrace();
			}
		} else if ("2".equals(type)) {
			fileName = "话费详单模板.xlsx";
			try {
				realName = URLEncoder.encode(fileName, "UTF-8");
			} catch (UnsupportedEncodingException e) {
				e.printStackTrace();
			}
		} else if ("3".equals(type)) {
			fileName = "TELINFO_TEMPLATE.xlsx";
			realName = "TELINFO_TEMPLATE.xlsx";
		}

		response.setCharacterEncoding("utf-8");
		response.setContentType("multipart/form-data");
		response.setHeader("Content-Disposition", "attachment;fileName="
				+ realName);

		InputStream inputStream = null;
		OutputStream os = null;
		try {
			String path = Thread.currentThread().getContextClassLoader()
					.getResource("").getPath();
			path = path.replace('/', '\\'); // 将/换成\
			path = path.replace("classes\\", ""); // 去掉class\
			path = path.replace("WEB-INF\\", ""); // 去掉class\
			path += "download";
			path = path.substring(1);
			path = URLDecoder.decode(path, "UTF-8");
			inputStream = new FileInputStream(new File(path + File.separator
					+ fileName));

			os = response.getOutputStream();
			byte[] b = new byte[2048];
			int length;
			while ((length = inputStream.read(b)) > 0) {
				os.write(b, 0, length);
			}

			// 这里主要关闭。
			os.close();

			inputStream.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				if (os != null)
					os.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
			try {
				if (inputStream != null)
					inputStream.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		// 返回值要注意，要不然就出现下面这句错误！
		// java+getOutputStream() has already been called for this response
		return null;
	}
}
