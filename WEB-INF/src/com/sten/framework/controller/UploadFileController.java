package com.sten.framework.controller;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URLEncoder;
import java.util.Date;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.UUID;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONArray;

import org.owasp.esapi.ESAPI;
import org.owasp.esapi.errors.ValidationException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.multipart.commons.CommonsMultipartResolver;
import org.springframework.web.servlet.ModelAndView;

import com.sten.framework.model.UploadFile;
import com.sten.framework.service.UploadFileService;
import com.sten.framework.util.DateTimeUtil;
import com.sten.framework.util.FileUtil;
import com.sten.framework.util.PropertiesUtil;
import com.sten.framework.util.StringUtil;

/**
 * Created by linkai on 16-1-15.
 */
@RestController
public class UploadFileController {

	private static final Logger logger = LoggerFactory
			.getLogger(UploadFileController.class);

	@Autowired
	private UploadFileService uploadFileService;

	/**
	 * 显示附件文件页面, 根据业务情况参考该方法在业务系统内实现
	 * 
	 */
	@RequestMapping(value = "/upload/view", method = RequestMethod.GET)
	public ModelAndView uploadView(String jsp, String foreignid) {
		ModelAndView mv = new ModelAndView();
		// 初始化业务对象主键作为上传对象的外键
		mv.addObject("foreignid", foreignid);
		// 初始化扩展数据(可选)
		mv.addObject("category", "sfz");

		// 查询已经上传的并且有效的数据
		List<UploadFile> list = uploadFileService.loadFiles(foreignid, 1);
		mv.addObject("list", list);

		mv.setViewName("demo/" + jsp);
		return mv;
	}

	/**
	 * 显示附件文件页面, 根据业务情况参考该方法在业务系统内实现
	 * 
	 */
	@RequestMapping(value = "/upload/window", method = RequestMethod.GET)
	public ModelAndView uploadView(String foreignid) {
		ModelAndView mv = new ModelAndView();
		// 初始化业务对象主键作为上传对象的外键
		mv.addObject("foreignid", foreignid);
		// 初始化扩展数据(可选)
		mv.addObject("category", "sfz");

		// 查询已经上传的并且有效的数据
		List<UploadFile> list = uploadFileService.loadFiles(foreignid, 1);
		mv.addObject("list", list);

		mv.setViewName("demo/uploadView1");
		return mv;
	}

	/**
	 * DEMO, 集成时需该方法需要在业务Controller中实现 更新文件状态, 删除和新增的文件, 最终已提交的ids为主进行处理
	 * 
	 */
	@RequestMapping(value = "/upload/submit", method = RequestMethod.POST)
	public Map<String, Object> uploadSubmit(String foreignid, String ids,
			HttpServletRequest request) {

		boolean result = true;
		String errormsg = "";

		// -----处理附件-----
		// 附件数据每次上传的时候已经提交数据库
		// 前台每次执行删除时, 只是把div删除, 更新了前台隐藏域的ids, 上传的文件和数据都没有处理
		// 查询当前业务相关的所有附件数据
		List<UploadFile> list = uploadFileService.loadFiles(foreignid);
		// 前台传过来最终确定的附件id
		List<String> array = java.util.Arrays.asList(ids.split(","));
		// 更新状态 0临时文件 1正式文件 2删除文件, 具体的文件删除由后台定时任务自动执行
		for (int i = 0; i < list.size(); i++) {
			if (array.contains(list.get(i).getFileId())) {
				list.get(i).setState(1);
			} else {
				list.get(i).setState(2);
			}
		}
		// 更新到数据库
		uploadFileService.updateFiles(list);

		// -----处理具体业务-----

		Map<String, Object> json = new Hashtable<String, Object>(1);
		json.put("result", result);
		json.put("errormsg", errormsg);
		return json;
	}

	// -----以下方法一般情况下不需要修改-----

	/**
	 * 上传文件, 文件状态标记为临时
	 * 
	 */
	@RequestMapping(value = "/upload/file", method = RequestMethod.POST)
	public Map<String, Object> uploadFile(UploadFile uploadFile,
			HttpServletRequest request) throws ValidationException {

		boolean result = true;
		String errormsg = "";

		String datePath = File.separator
				+ DateTimeUtil.getCurDateFShort().replace("-", File.separator)
				+ File.separator;
		String filepath = StringUtil.trimEnd(
				PropertiesUtil.getProperty("UPLOAD_FOLDER_ROOT"),
				File.separator);
		String rootPath = PropertiesUtil.getProperty("ESAPI_VALID_ROOT");
		// UPLOAD_FOLDER_ROOT 作为抛出异常信息的标识
		// filepath 待验证的路径, 必须含有完整的根路径, 并且路径最后不能有 / 或 \
		// new File("/") 作为验证路径的根目录, linux 对应 / windows 对应 C:\\ 注意:路径区分大小写,
		// 必须一致, windows 路径方向必须右向 \\
		// 如果路径不安全则抛出异常
		String safePath = ESAPI.validator().getValidDirectoryPath(
				"UPLOAD_FOLDER_ROOT", filepath, new File(rootPath), false)
				+ datePath;

		File file = new File(safePath);
		if (!file.exists()) {
			file.mkdirs();
		}

		MultipartHttpServletRequest multipartRequest = (MultipartHttpServletRequest) request;
		MultipartFile multipartFile = multipartRequest.getFile("qqfile");

		// 获取上传文件的属性
		String fileid = request.getParameter("qquuid");
		String foreignid = request.getParameter("foreignid");
		String extension = multipartFile.getOriginalFilename().substring(
				multipartFile.getOriginalFilename().lastIndexOf('.'));
		// IE 上传的文件有原始路径， chorme 上传的文件没有路径
		int beginIndex = multipartFile.getOriginalFilename().lastIndexOf('\\');
		if (beginIndex == -1) {
			beginIndex = 0;
		} else {
			beginIndex++;
		}
		String oldFileName = multipartFile.getOriginalFilename().substring(
				beginIndex);
		oldFileName = oldFileName.substring(0, oldFileName.lastIndexOf('.'));
		String newFileName = safePath + fileid + extension;
		String fullFileName = datePath + fileid + extension;
		long fileLength = multipartFile.getSize();
		String fileSize = FileUtil.formatFileSize(fileLength);

		// 保存文件
		InputStream is = null;
		try {
			is = multipartFile.getInputStream();
			FileUtil.saveFileFromInputStream(is, newFileName);
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (is != null) {
				try {
					is.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}

		// 获取最大排序号
		int orderIndex = uploadFileService.getMaxOrderIndex(
				uploadFile.getForeignId(), uploadFile.getCategory());

		// 保存数据
		uploadFile.setFileId(fileid);
		uploadFile.setFileName(oldFileName);
		uploadFile.setFullFileName(fullFileName);
		uploadFile.setExtension(extension);
		uploadFile.setFileLength(fileLength);
		uploadFile.setFileSize(fileSize);
		// 标记为临时文件
		uploadFile.setState(0);
		uploadFile.setOrderIndex(orderIndex);
		// TODO:修改为提交用户名和密码
		// uploadFile.setCreateUserId();
		// uploadFile.setCreateUseName();
		uploadFile.setCreateDate(new Date());
		uploadFileService.addFile(uploadFile);

		Map<String, Object> json = new Hashtable<String, Object>(1);
		json.put("success", result);
		json.put("errormsg", errormsg);
		json.put("qquuid", fileid);
		json.put("qqfilename", oldFileName + extension);
		json.put("qqfilepath", newFileName);
		json.put("qqtotalfilesize", fileSize);
		json.put("extension", extension.toLowerCase(Locale.ENGLISH));
		json.put("orderindex", orderIndex);

		return json;
	}

	@RequestMapping(value = "/uploadify/file", method = RequestMethod.POST)
	public void uploadifyFile(UploadFile uploadFile,
			HttpServletRequest request, HttpServletResponse response)
			throws ValidationException {

		boolean result = true;
		String errormsg = "";

		String datePath = File.separator
				+ DateTimeUtil.getCurDateFShort().replace("-", File.separator)
				+ File.separator;
		String filepath = StringUtil.trimEnd(
				PropertiesUtil.getProperty("UPLOAD_FOLDER_ROOT"),
				File.separator);
		String rootPath = PropertiesUtil.getProperty("ESAPI_VALID_ROOT");
		// UPLOAD_FOLDER_ROOT 作为抛出异常信息的标识
		// filepath 待验证的路径, 必须含有完整的根路径, 并且路径最后不能有 / 或 \
		// new File("/") 作为验证路径的根目录, linux 对应 / windows 对应 C:\\ 注意:路径区分大小写,
		// 必须一致, windows 路径方向必须右向 \\
		// 如果路径不安全则抛出异常
		String safePath = ESAPI.validator().getValidDirectoryPath(
				"UPLOAD_FOLDER_ROOT", filepath, new File(rootPath), false)
				+ datePath;

		File dirPath = new File(safePath);
		if (!dirPath.exists()) {
			dirPath.mkdirs();
		}

		// 获取上传文件的属性
		String fileid = UUID.randomUUID().toString();
		String foreignId = request.getParameter("foreignid");
		String category = request.getParameter("category");
		String extension = "";
		String oldFileName = "";
		String newFileName = "";
		String fullFileName = "";
		String fileSize = "";
		long fileLength = 0;

		// 解析器解析request的上下文
		CommonsMultipartResolver multipartResolver = new CommonsMultipartResolver(
				request.getSession().getServletContext());
		// 先判断request中是否包涵multipart类型的数据，
		if (multipartResolver.isMultipart(request)) {
			// 再将request中的数据转化成multipart类型的数据
			MultipartHttpServletRequest multiRequest = (MultipartHttpServletRequest) request;
			@SuppressWarnings("rawtypes")
			Iterator iter = multiRequest.getFileNames();

			while (iter.hasNext()) {
				MultipartFile file = multiRequest.getFile((String) iter.next());
				if (file != null) {
					String fileName = file.getOriginalFilename();
					fileLength = file.getSize();
					fileSize = FileUtil.formatFileSize(fileLength);
					extension = fileName.substring(fileName.lastIndexOf('.'));
					oldFileName = fileName.substring(0,
							fileName.lastIndexOf('.'));
					System.out.println(fileName);
					newFileName = safePath + fileid + extension;
					fullFileName = datePath + fileid + extension;

					File localFile = new File(newFileName);
					// 写文件到本地
					try {
						file.transferTo(localFile);
					} catch (IllegalStateException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			}
		}

		// 获取最大排序号
		uploadFile.setCategory(category);
		uploadFile.setForeignId(foreignId);
		int orderIndex = uploadFileService.getMaxOrderIndex(
				uploadFile.getForeignId(), uploadFile.getCategory());

		// 保存数据
		uploadFile.setFileId(fileid);
		uploadFile.setFileName(oldFileName);
		uploadFile.setFullFileName(fullFileName);
		uploadFile.setExtension(extension);
		uploadFile.setFileLength(fileLength);
		uploadFile.setFileSize(fileSize);
		// 标记为临时文件
		uploadFile.setState(0);
		uploadFile.setOrderIndex(orderIndex);
		// TODO:修改为提交用户名和密码
		// uploadFile.setCreateUserId();
		// uploadFile.setCreateUseName();
		uploadFile.setCreateDate(new Date());
		uploadFileService.addFile(uploadFile);

		Map<String, Object> json = new Hashtable<String, Object>(1);
		json.put("success", result);
		json.put("errormsg", errormsg);
		json.put("qquuid", fileid);

		json.put("qqfilename", oldFileName + extension);
		json.put("qqfilepath", newFileName);
		json.put("qqtotalfilesize", fileSize);
		json.put("extension", extension.toLowerCase(Locale.ENGLISH));
		json.put("orderindex", orderIndex);

		JSONArray jsonArr = JSONArray.fromObject(json);
		try {
			response.setContentType("application/json; charset=UTF-8");
			response.getWriter().print(jsonArr.toString());
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	/**
	 * 删除文件, 文件状态标记为删除, 文件和数据并不删除
	 * 
	 */
	@RequestMapping(value = "/upload/delete", method = RequestMethod.POST)
	public Map<String, Object> uploadDelete(String fileid,
			HttpServletRequest request) {

		boolean result = true;
		String errormsg = "";

		// 标记删除
		uploadFileService.markDeleteFile(fileid);

		Map<String, Object> json = new Hashtable<String, Object>(1);
		json.put("result", result);
		json.put("errormsg", errormsg);
		return json;
	}

	/**
	 * 下载文件
	 * 
	 */
	@RequestMapping(value = "/upload/download/{fileId}", method = RequestMethod.GET)
	public void download(@PathVariable("fileId") String fileId,
			HttpServletRequest request, HttpServletResponse response)
			throws IOException, ValidationException {
		if (fileId.indexOf(",") >= 0) {
			// ZipCompressor zip = new ZipCompressor(".zip");
			// zip.compressExe("destination"+"/"+"folderName");
		} else {
			UploadFile file = uploadFileService.getFile(fileId);

			String filepath = StringUtil.trimEnd(
					PropertiesUtil.getProperty("UPLOAD_FOLDER_ROOT"),
					File.separator);
			String rootPath = PropertiesUtil.getProperty("ESAPI_VALID_ROOT");
			String safePath = ESAPI.validator().getValidDirectoryPath(
					"UPLOAD_FOLDER_ROOT", filepath, new File(rootPath), false);
			safePath += File.separator + file.getFullFileName();

			// fix chrome ERR_RESPONSE_HEADERS_MULTIPLE_CONTENT_DISPOSITION,
			// 文件名中不支持逗号
			String filename = file.getFileName().replaceAll(",", "")
					+ file.getExtension();
			if (request.getHeader("User-Agent").toUpperCase(Locale.ENGLISH)
					.indexOf("MSIE") > 0) {
				filename = URLEncoder.encode(filename, "UTF-8");
			} else {
				filename = URLEncoder.encode(filename, "UTF-8");
				// filename = new String(filename.getBytes("UTF-8"),
				// "ISO8859-1");
			}

			BufferedInputStream in = null;
			BufferedOutputStream out = null;
			try {
				File f = new File(safePath);
				response.setContentType("text/html;charset=UTF-8");
				String extensionName = file.getExtension().toLowerCase(
						Locale.ENGLISH);
				if (".jpeg".equals(extensionName)
						|| ".jpg".equals(extensionName)
						|| ".png".equals(extensionName)) {
					response.setContentType("image/jpeg;");
				} else if (".gif".equals(extensionName)) {
					response.setContentType("image/gif;");
				} else if (".pdf".equals(extensionName)) {
					response.setContentType("application/pdf;");

				} else if (".docx".equals(extensionName)
						|| ".doc".equals(extensionName)) {
					response.setContentType("application/msword;");

				} else if (".txt".equals(extensionName)) {
					response.setContentType("text/plain;");

				} else {
					response.setContentType("application/x-msdownload;");
				}
				response.setCharacterEncoding("UTF-8");
				response.setHeader("Content-Disposition",
						"attachment; filename=" + filename);
				response.setHeader("Content-Length", String.valueOf(f.length()));

				in = new BufferedInputStream(new FileInputStream(f));
				out = new BufferedOutputStream(response.getOutputStream());
				byte[] data = new byte[1024];
				int len = 0;
				while (-1 != (len = in.read(data, 0, data.length))) {
					out.write(data, 0, len);
				}
			} catch (Exception e) {
				e.printStackTrace();
			} finally {
				if (in != null) {
					in.close();
				}
				if (out != null) {
					out.close();
				}
			}
		}
	}

	// public void uploadifyFile(UploadFile uploadFile, HttpServletRequest
	// request, HttpServletResponse response ) throws ValidationException {
	//
	// boolean result = true;
	// String errormsg = "";
	//
	// String datePath = File.separator +
	// DateTimeUtil.getCurDateFShort().replace("-", File.separator) +
	// File.separator;
	// String filepath =
	// StringUtil.trimEnd(PropertiesUtil.getProperty("UPLOAD_FOLDER_ROOT"),
	// File.separator);
	// String rootPath = PropertiesUtil.getProperty("ESAPI_VALID_ROOT");
	// //UPLOAD_FOLDER_ROOT 作为抛出异常信息的标识
	// //filepath 待验证的路径, 必须含有完整的根路径, 并且路径最后不能有 / 或 \
	// //new File("/") 作为验证路径的根目录, linux 对应 / windows 对应 C:\\ 注意:路径区分大小写, 必须一致,
	// windows 路径方向必须右向 \\
	// //如果路径不安全则抛出异常
	// String safePath =
	// ESAPI.validator().getValidDirectoryPath("UPLOAD_FOLDER_ROOT", filepath,
	// new File(rootPath), false) + datePath;
	//
	// File dirPath = new File(safePath);
	// if (!dirPath .exists())
	// {
	// dirPath .mkdirs();
	// }
	//
	// // 获取上传文件的属性
	// //String fileid = UUID.randomUUID().toString();
	// String foreignId = request.getParameter("foreignid");
	// String category = request.getParameter("category");
	// String extension = "";
	// String oldFileName = "";
	// String newFileName = "";
	// String fullFileName = "";
	// String fileSize = "";
	// long fileLength = 0;
	//
	// DiskFileItemFactory fac = new DiskFileItemFactory();
	// ServletFileUpload upload = new ServletFileUpload(fac);
	// upload.setHeaderEncoding("utf-8");
	// List fileList = null;
	// try {
	// fileList = upload.parseRequest(request);
	// } catch (FileUploadException ex) {
	// return;
	// }
	//
	// Iterator<FileItem> it = fileList.iterator();
	// List list = new ArrayList();
	// while (it.hasNext()) {
	// FileItem item = it.next();
	// if (!item.isFormField()) {
	// String fileid = UUID.randomUUID().toString();
	// String fileName = item.getName();
	// fileLength = item.getSize();
	// String type = item.getContentType();
	// System.out.println(fileLength + " " + type);
	// if (fileName == null || fileName.trim().equals("")) {
	// continue;
	// }
	//
	// //扩展名格式：
	// extension = fileName.substring(fileName.lastIndexOf("."));
	// oldFileName = fileName.substring(0, fileName.lastIndexOf('.'));
	// newFileName = safePath + fileid + extension;
	// fullFileName = datePath + fileid + extension;
	//
	// File file = null;
	// do {
	// // 生成文件名：
	// file = new File(newFileName);
	// } while (file.exists());
	// File saveFile = new File(newFileName);
	// try {
	// item.write(saveFile);
	// } catch (Exception e) {
	// e.printStackTrace();
	// }
	//
	// //获取最大排序号
	// uploadFile.setCategory(category);
	// uploadFile.setForeignId(foreignId);
	// int orderIndex =
	// uploadFileService.getMaxOrderIndex(uploadFile.getForeignId(),
	// uploadFile.getCategory());
	//
	// //保存数据
	// uploadFile.setFileId(fileid);
	// uploadFile.setFileName(oldFileName);
	// uploadFile.setFullFileName(fullFileName);
	// uploadFile.setExtension(extension);
	// uploadFile.setFileLength(fileLength);
	// uploadFile.setFileSize(fileSize);
	// //标记为临时文件
	// uploadFile.setState(0);
	// uploadFile.setOrderIndex(orderIndex);
	// //TODO:修改为提交用户名和密码
	// //uploadFile.setCreateUserId();
	// //uploadFile.setCreateUseName();
	// uploadFile.setCreateDate(new Date());
	// uploadFileService.addFile(uploadFile);
	//
	// Map<String, Object> json = new Hashtable<String, Object>(1);
	// json.put("success", result);
	// json.put("errormsg", errormsg);
	// json.put("qquuid", fileid);
	//
	// json.put("qqfilename", oldFileName + extension);
	// json.put("qqfilepath", newFileName);
	// json.put("qqtotalfilesize", fileSize);
	// json.put("extension", extension.toLowerCase(Locale.ENGLISH));
	// json.put("orderindex", orderIndex);
	//
	// list.add(json);
	// }
	// }
	//
	//
	// JSONArray jsonArr = JSONArray.fromObject(list);
	// try {
	// response.setContentType("application/json; charset=UTF-8");
	// response.getWriter().print(jsonArr.toString());
	// } catch (IOException e) {
	// // TODO Auto-generated catch block
	// e.printStackTrace();
	// }
	// }
}