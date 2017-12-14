package com.sten.mtcc.common;

import java.io.File;
import java.security.MessageDigest;

import javax.servlet.http.HttpServletRequest;

import org.owasp.esapi.ESAPI;
import org.owasp.esapi.errors.ValidationException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.sten.framework.util.PropertiesUtil;
import com.sten.framework.util.StringUtil;

/**
 * Created by ztw-a on 2017/3/9.
 */
public class CommonUtil {
	private static final Logger logger = LoggerFactory
			.getLogger(CommonUtil.class);

	public static String getNull2String(Object objIn) {
		if (objIn == null) {
			return "";
		}
		return objIn.toString();
	}

	/**
	 * 取得安全文件路径
	 * 
	 * @param path
	 *            文件路径
	 * @param flag
	 *            （UPLOAD_FOLDER_ROOT） 作为抛出异常信息的标识
	 * @return 安全文件路径
	 * */
	public static String getSafeFilePath(String path, String flag) {
		String rootPath = "";
		String safePath = "";
		String filename = "";
		String filePath = "";

		try {
			// UPLOAD_FOLDER_ROOT 作为抛出异常信息的标识
			// filepath 待验证的路径, 必须含有完整的根路径, 并且路径最后不能有 / 或 \
			// new File("/") 作为验证路径的根目录, linux 对应 / windows 对应 C:\\ 注意:路径区分大小写,
			// 必须一致, windows 路径方向必须右向 \\
			// 如果路径不安全则抛出异常
			rootPath = PropertiesUtil.getProperty("ESAPI_VALID_ROOT");
			path = path.replace("/", "\\");
			safePath = path;
			filePath = StringUtil.substringBeforeLast(path, File.separator);
			filename = StringUtil.substringAfterLast(path, File.separator);

			filePath = ESAPI.validator().getValidDirectoryPath(flag, filePath,
					new File(rootPath), false);
			filename = ESAPI.validator().getValidFileName(flag, filename,
					ESAPI.securityConfiguration().getAllowedFileExtensions(),
					false);
			safePath = filePath + File.separator + filename;
		} catch (ValidationException e) {
			e.printStackTrace();
			// logger.error("文件名验证失败:" + filename + "\r\n" + e.toString());
		}

		return safePath;
	}

	/**
	 * 取得安全路径
	 * 
	 * @param path
	 *            路径
	 * @param flag
	 *            （UPLOAD_FOLDER_ROOT） 作为抛出异常信息的标识
	 * @return 安全文件路径
	 * */
	public static String getSafePath(String path, String flag) {
		String rootPath = "";
		String safePath = path;

		try {
			// UPLOAD_FOLDER_ROOT 作为抛出异常信息的标识
			// filepath 待验证的路径, 必须含有完整的根路径, 并且路径最后不能有 / 或 \
			// new File("/") 作为验证路径的根目录, linux 对应 / windows 对应 C:\\ 注意:路径区分大小写,
			// 必须一致, windows 路径方向必须右向 \\
			// 如果路径不安全则抛出异常
			path = path.replace("/", "\\");
			path = StringUtil.trimEnd(path, File.separator);
			rootPath = PropertiesUtil.getProperty("ESAPI_VALID_ROOT");
			safePath = ESAPI.validator().getValidDirectoryPath(flag, path,
					new File(rootPath), false)
					+ File.separator;
		} catch (ValidationException e) {
			e.printStackTrace();
			// logger.error("文件路径验证失败:" + path + "\r\n" + e.toString());
		}

		return safePath;
	}

	/***
	 * MD5加码 生成32位md5码
	 */
	public static String string2MD5(String inStr) {
		MessageDigest md5 = null;
		try {
			md5 = MessageDigest.getInstance("MD5");
		} catch (Exception e) {
			System.out.println(e.toString());
			e.printStackTrace();
			return "";
		}
		char[] charArray = inStr.toCharArray();
		byte[] byteArray = new byte[charArray.length];

		for (int i = 0; i < charArray.length; i++)
			byteArray[i] = (byte) charArray[i];
		byte[] md5Bytes = md5.digest(byteArray);
		StringBuffer hexValue = new StringBuffer();
		for (int i = 0; i < md5Bytes.length; i++) {
			int val = ((int) md5Bytes[i]) & 0xff;
			if (val < 16)
				hexValue.append("0");
			hexValue.append(Integer.toHexString(val));
		}
		return hexValue.toString();

	}

	/***
	 * 加密MD5
	 */
	public static String KL(String inStr) {
		char[] a = inStr.toCharArray();
		for (int i = 0; i < a.length; i++) {
			a[i] = (char) (a[i] ^ 't');
		}
		String s = new String(a);
		return s;
	}

	/**
	 * 加密解密算法 执行一次加密，两次解密
	 */
	public static String convertMD5(String inStr) {

		char[] a = inStr.toCharArray();
		for (int i = 0; i < a.length; i++) {
			a[i] = (char) (a[i] ^ 't');
		}
		String s = new String(a);
		return s;

	}

	/**
	 * 获取登录用户IP地址
	 * 
	 * @param request
	 * @return
	 */
	public static String getIpAddr(HttpServletRequest request) {
		String ip = request.getHeader("x-forwarded-for");
		if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
			ip = request.getHeader("Proxy-Client-IP");
		}
		if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
			ip = request.getHeader("WL-Proxy-Client-IP");
		}
		if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
			ip = request.getRemoteAddr();
		}
		if (ip.equals("0:0:0:0:0:0:0:1")) {
			ip = "本地";
		}
		return ip;
	}

	/**
	 * 删除空目录
	 * 
	 * @param dir
	 *            将要删除的目录路径
	 */
	public static boolean doDeleteEmptyDir(String dir) {
		boolean success = (new File(dir)).delete();
		return success;
	}

	/**
	 * 递归删除目录下的所有文件及子目录下所有文件
	 * 
	 * @param path
	 *            将要删除的文件目录
	 * @return boolean Returns "true" if all deletions were successful. If a
	 *         deletion fails, the method stops attempting to delete and returns
	 *         "false".
	 */
	public static boolean deleteFile(String orgPath, String path) {

		File file = new File(path);
		boolean success = false;
		if (file.isDirectory()) {
			String[] children = file.list();
			// 递归删除目录中的子目录下
			if (!path.endsWith("\\")) {
				path = path + "\\";
			}
			for (int i = 0; i < children.length; i++) {
				success = deleteFile(orgPath, path + children[i]);
				if (!success) {
					return false;
				}
			}
			if (!orgPath.equals(path)) {
				file.delete();
			}

		} else if (file.isFile()) {
			success = file.delete();
			if (!success) {
				return false;
			}
		}
		// 目录此时为空，可以删除
		// return dir.delete();
		return true;
	}

}
