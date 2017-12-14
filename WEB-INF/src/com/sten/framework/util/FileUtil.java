package com.sten.framework.util;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.DecimalFormat;

import org.owasp.esapi.ESAPI;
import org.owasp.esapi.errors.ValidationException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import sun.misc.BASE64Decoder;
import sun.misc.BASE64Encoder;

/**
 * Created by linkai on 16-4-12. 2016-07-22 增加 getWebRootPath
 */
public class FileUtil {

	private static final Logger logger = LoggerFactory
			.getLogger(FileUtil.class);

	/**
	 * 
	 * @Title: formatFileSize
	 * @Description: 一个将Byte单位文件大小转换为B\KB\MB\GB单位(String)
	 * @param fileSize
	 * @return
	 * 
	 */
	public static String formatFileSize(String fileSize) {
		long _fileSize = Long.parseLong(fileSize);
		/**
		 * size 如果小于1024,以B单位返回;小于1024 * 1024,以KB单位返回;小于1024 * 1024 *
		 * 1024,以MB单位返回;否则以GB单位返回
		 */
		DecimalFormat df = new DecimalFormat("###.##");
		float f;
		if (_fileSize < 1024) {
			f = (float) ((float) _fileSize / (float) 1);
			return (df.format(new Float(f).doubleValue()) + "B");
		} else if (_fileSize < 1024 * 1024) {
			f = (float) ((float) _fileSize / (float) 1024);
			return (df.format(new Float(f).doubleValue()) + "KB");
		} else if (_fileSize < 1024 * 1024 * 1024) {
			f = (float) ((float) _fileSize / (float) (1024 * 1024));
			return (df.format(new Float(f).doubleValue()) + "MB");
		} else {
			f = (float) ((float) _fileSize / (float) (1024 * 1024 * 1024));
			return (df.format(new Float(f).doubleValue()) + "GB");
		}
	}

	/**
	 * 
	 * @Title: formatFileSize
	 * @Description: 一个将Byte单位文件大小转换为B\KB\MB\GB单位(Long)
	 * @param fileSize
	 * 
	 */
	public static String formatFileSize(long fileSize) {
		/**
		 * size 如果小于1024,以B单位返回;小于1024 * 1024,以KB单位返回;小于1024 * 1024 *
		 * 1024,以MB单位返回;否则以GB单位返回
		 */
		DecimalFormat df = new DecimalFormat("###.##");
		float f;
		if (fileSize < 1024) {
			f = (float) ((float) fileSize / (float) 1);
			return (df.format(new Float(f).doubleValue()) + "B");
		} else if (fileSize < 1024 * 1024) {
			f = (float) ((float) fileSize / (float) 1024);
			return (df.format(new Float(f).doubleValue()) + "KB");
		} else if (fileSize < 1024 * 1024 * 1024) {
			f = (float) ((float) fileSize / (float) (1024 * 1024));
			return (df.format(new Float(f).doubleValue()) + "MB");
		} else {
			f = (float) ((float) fileSize / (float) (1024 * 1024 * 1024));
			return (df.format(new Float(f).doubleValue()) + "GB");
		}
	}

	/**
	 * 
	 * @Title: getFileSize
	 * @Description: 获取文件大小
	 * @param file
	 * @return
	 * @throws Exception
	 * 
	 */
	public static long getFileSizes(File file) throws FileNotFoundException {
		long size = 0;
		if (file.exists()) {
			FileInputStream in = null;
			try {
				in = new FileInputStream(file);
				size = in.available();
			} catch (FileNotFoundException e) {
				e.printStackTrace();
				logger.error(e.toString());
			} catch (IOException e) {
				e.printStackTrace();
				logger.error(e.toString());
			} finally {
				if (in != null) {
					try {
						in.close();
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
			}
		} else {
			throw new FileNotFoundException(file.getName() + "文件不存在");
		}
		return size;
	}

	/**
	 * 
	 * @Title: getFloderSize
	 * @Description: 递归取得文件夹大小
	 * @param floder
	 * @return
	 * @throws Exception
	 * 
	 */
	public static long getFloderSize(File floder) throws Exception {
		long size = 0;
		if (floder == null) {
			return size;
		}
		if (!floder.exists()) {
			throw new FileNotFoundException(floder.getName() + "文件不存在");
		} else if (floder.isDirectory()) {
			File fList[] = floder.listFiles();
			for (int i = 0; i < fList.length; i++) {
				if (fList[i].isDirectory()) {
					size = size + getFloderSize(fList[i]);
				} else {
					size = size + fList[i].length();
				}
			}
		} else if (floder.isFile()) {
			// floder 是文件不是文件夹直接返回文件大小
			size = size + floder.length();
		}
		return size;
	}

	/**
	 * 
	 * @Title: getFloderListNum
	 * @Description: 递归求取目录文件个数
	 * @param floder
	 * @return
	 * @throws Exception
	 * 
	 */
	public static long getFloderListNum(File floder) throws Exception {
		long size = 0;
		if (!floder.exists()) {
			throw new FileNotFoundException(floder.getName() + "文件不存在");
		} else if (floder.isDirectory()) {
			File flist[] = floder.listFiles();
			size = flist.length;
			for (int i = 0; i < flist.length; i++) {
				if (flist[i].isDirectory()) {
					size = size + getFloderListNum(flist[i]);
					size--;// 不计算文件目录
				}
			}
		} else if (floder.isFile()) {
			size = 1L;
		}
		return size;
	}

	public static void main(String[] args) {
		long startTime = System.currentTimeMillis();
		File testFile = new File("C:/WINDOWS");
		try {
			System.out.println(FileUtil.getFloderListNum(testFile));
			System.out.println(FileUtil.getFloderSize(testFile));
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		long endTime = System.currentTimeMillis();
		System.out.println("总共花费时间为：" + (endTime - startTime) + "毫秒...");
	}

	// 文件复制
	public static void saveFileFromInputStream(InputStream stream,
			String filename) throws IOException {

		String safePath = "";
		try {
			String rootPath = PropertiesUtil.getProperty("ESAPI_VALID_ROOT");

			// 路径和文件名要分开验证
			safePath = StringUtil.substringBeforeLast(filename, File.separator);
			filename = StringUtil.substringAfterLast(filename, File.separator);

			// UPLOAD_FOLDER_ROOT 作为抛出异常信息的标识
			// safePath 待验证的路径, 必须含有完整的根路径, 并且路径最后不能有 / 或 \
			// new File("/") 作为验证路径的根目录, linux 对应 / windows 对应 C:\\ 注意:路径区分大小写,
			// 必须一致, windows 路径方向必须右向 \\
			// 如果路径不安全则抛出异常
			safePath = ESAPI.validator().getValidDirectoryPath(
					"UPLOAD_FOLDER_ROOT", safePath, new File(rootPath), false);

			filename = ESAPI.validator().getValidFileName("UPLOAD_FOLDER_ROOT",
					filename,
					ESAPI.securityConfiguration().getAllowedFileExtensions(),
					false);

			safePath = safePath + File.separator + filename;
		} catch (ValidationException e) {
			e.printStackTrace();
			logger.error("文件名验证失败:" + filename + "\r\n" + e.toString());
		}

		FileOutputStream out = null;
		try {
			out = new FileOutputStream(safePath);
			byte[] buffer = new byte[1024 * 1024];
			int bytesum = 0;
			int byteread = 0;
			while ((byteread = stream.read(buffer)) != -1) {
				bytesum += byteread;
				out.write(buffer, 0, byteread);
				out.flush();
			}
		} catch (Exception e) {
			e.printStackTrace();
			logger.error(e.toString());
		} finally {
			if (out != null) {
				out.close();
			}
		}
		if (stream != null) {
			stream.close();
		}
	}

	// 文件复制
	public static boolean copy(String fileFrom, String fileTo) {
		boolean result = false;
		FileInputStream in = null;
		FileOutputStream out = null;
		try {
			in = new FileInputStream(fileFrom);
			out = new FileOutputStream(fileTo);
			byte[] bt = new byte[1024];
			int count;
			while ((count = in.read(bt)) > 0) {
				out.write(bt, 0, count);
			}

			result = true;
		} catch (FileNotFoundException e) {
			e.printStackTrace();
			logger.error(e.toString());
		} catch (IOException e) {
			e.printStackTrace();
			logger.error(e.toString());
		} finally {
			if (in != null) {
				try {
					in.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			if (out != null) {
				try {
					out.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		return result;
	}

	/**
	 * 计算文件的哈希值
	 * 
	 * @param fileName
	 *            要计算哈希值的全路径文件名
	 * @param hashType
	 *            MD5，SHA1，SHA-256，SHA-384，SHA-512
	 * @return
	 */
	public static String getHashFile(String fileName, String hashType) {
		String result = "";
		InputStream in = null;
		try {
			in = new FileInputStream(fileName);
			byte buffer[] = new byte[1024];
			MessageDigest md5 = MessageDigest.getInstance(hashType);
			for (int numRead = 0; (numRead = in.read(buffer)) > 0;) {
				md5.update(buffer, 0, numRead);
			}
			result = StringUtil.toHexString(md5.digest());
		} catch (IOException e) {
			e.printStackTrace();
			logger.error(e.toString());
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
			logger.error(e.toString());
		} finally {
			if (in != null) {
				try {
					in.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		return result;
	}

	public static void saveFile(String value, String filename)
			throws IOException {
		FileWriter fileWriter = null;
		OutputStreamWriter osw = null;
		FileOutputStream fos = null;
		try {
			// File file = new File(filename);
			// fileWriter = new FileWriter(file);
			// fileWriter.write(value);
			filename = getSafeFilePath(filename, "PDF_ROOT");
			fos = new FileOutputStream(filename);
			osw = new OutputStreamWriter(fos, "UTF-8");
			osw.write(value);
			osw.flush();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (fileWriter != null) {
				try {
					fileWriter.close();
				} catch (IOException e) {
					// log(e);
				}
			}
			if (osw != null) {
				try {
					osw.close();
				} catch (IOException e) {
					// log(e);
				}
			}
			if (fos != null) {
				try {
					fos.close();
				} catch (IOException e) {
					// log(e);
				}
			}
		}
	}

	public static String readFile(String filename) {
		StringBuilder sb = new StringBuilder();
		InputStreamReader inputStreamReader = null;
		InputStream inputStream = null;
		try {
			String encoding = "UTF-8";
			File file = new File(filename);
			if (file.isFile() && file.exists()) { // 判断文件是否存在
				inputStream = new FileInputStream(file);
				inputStreamReader = new InputStreamReader(inputStream, encoding);// 考虑到编码格式
				BufferedReader bufferedReader = new BufferedReader(
						inputStreamReader);
				String lineTxt = null;
				while ((lineTxt = bufferedReader.readLine()) != null) {
					sb.append(lineTxt.trim() + "\r\n");
				}
			} else {
				logger.warn("找不到指定的文件" + filename);
			}
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
			logger.error(e.toString());
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
			if (inputStreamReader != null) {
				try {
					inputStreamReader.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		return sb.toString();
	}

	/**
	 * 获取 FileUtil 所在本地路径, 失败返回空字符串
	 * 
	 * Users/linkai/IdeaProjects/oeas/target/oeas/
	 */
	public static String getWebRootPath() {
		String path = "";
		try {
			Class obj = Class.forName(FileUtil.class.getName());
			Object ins = obj.newInstance();
			path = ins.getClass().getResource("/").getPath();
			// Users/linkai/IdeaProjects/oeas/target/oeas/WEB-INF/classes/
			path = path.replace("WEB-INF/classes/", "");
			logger.info("FileUtil.WebRootPath = " + path);
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
			logger.error(e.toString());
		} catch (InstantiationException e) {
			e.printStackTrace();
			logger.error(e.toString());
		} catch (IllegalAccessException e) {
			e.printStackTrace();
			logger.error(e.toString());
		}
		return path;
	}

	/**
	 * @Description: 将base64编码字符串转换为图片
	 * @Author:
	 * @CreateTime:
	 * @param base64
	 *            base64编码字符串
	 * @param imgFilename
	 *            图片路径-具体到文件
	 * @return
	 */
	public static boolean generateImage(String base64, String imgFilename) {
		if (base64 == null) {
			return false;
		}
		boolean result = false;
		BASE64Decoder decoder = new BASE64Decoder();
		OutputStream out = null;
		try {
			// 解密
			byte[] b = decoder.decodeBuffer(base64);
			// 处理数据
			for (int i = 0; i < b.length; ++i) {
				if (b[i] < 0) {
					b[i] += 256;
				}
			}
			out = new FileOutputStream(imgFilename);
			out.write(b);
			out.flush();
			result = true;
		} catch (Exception e) {
			e.printStackTrace();
			logger.error(e.toString());
		} finally {
			if (out != null) {
				try {
					out.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		return result;
	}

	/**
	 * @Description: 根据图片地址转换为base64编码字符串
	 * @Author:
	 * @CreateTime:
	 * @return
	 */
	public static String getImageBase64(String imgFilename) {
		String result = "";
		InputStream in = null;
		byte[] data = null;
		try {
			in = new FileInputStream(imgFilename);
			data = new byte[in.available()];
			in.read(data);
			// 加密
			BASE64Encoder encoder = new BASE64Encoder();
			result = encoder.encode(data);
		} catch (IOException e) {
			e.printStackTrace();
			logger.error(e.toString());
		} finally {
			if (in != null) {
				try {
					in.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		return result;
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
			logger.error("文件名验证失败:" + filename + "\r\n" + e.toString());
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
			logger.error("文件路径验证失败:" + path + "\r\n" + e.toString());
		}

		return safePath;
	}
}
