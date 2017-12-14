package com.sten.framework.util;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 
 * @description 正则表达式帮助类
 * @author linkai
 * @date 2016-10-25
 * @time 下午13:47:00
 */
public class PatternUtil {

	// 汉字
	public static String REGEX_CHINESE = "^[\\u4e00-\\u9fa5],{0,}$";
	// 字母
	public static String REGEX_LETTER = "^[A-Za-z]+$";
	// 小写字母
	public static String REGEX_LOWCHAR = "^[a-z]+$";
	// 大写字母
	public static String REGEX_UPCHAR = "^[A-Z]+$";
	// 非零的正整数
	public static String REGEX_INTNUMBER = "^\\\\+?[1-9][0-9]*$";
	// 验证数字输入
	public static String REGEX_NUMBER = "^[0-9]*$";
	// 两位小数
	public static String REGEX_DECIMAL = "^[0-9]+(.[0-9]{2})?$";
	// 简易版本的身份证号
	public static String REGEX_IDCARD = "(^\\\\d{18}$)|(^\\\\d{15}$)";
	// 手机号码
	public static String REGEX_MOBILE = "^1[0-9]{10}$";

	/**
	 * 替换没关闭的html标签, 并关闭标签 包括 br meta|link|img|input|
	 * 
	 * @param html
	 *            是否包含当前节点
	 * @return json 字符串
	 */
	public static String replaceCloseTag(String html) {
		// br
		String temp = Pattern.compile("(<\\s*br\\s*[^/>]*\\s*)>").matcher(html)
				.replaceAll("$1/>");

		// meta|link|img 排除 /> -->
		temp = Pattern
				.compile(
						"(<\\s*(meta|link|img|input)(?=>|\\s.*>)\\/?.*?[^(/>)|(-->)]\\s*)>")
				.matcher(temp).replaceAll("$1/>");
		return temp;
	}

	/**
	 * 根据操作系统环境替换路径中的重复字符为1个字符, 例如: abc\\\def\\gh - abc\def\gh abc///def//gh -
	 * abc/def/gh
	 * 
	 * @param path
	 *            被替的路径换字符串
	 * @return 返回替换后的字符
	 */
	public static String replaceSeparator(String path) {
		// return path.replaceAll("[|/\\\\]{2,}", File.separator);
		return path.replaceAll("[/\\\\]{2,}", File.separator);
	}

	/**
	 * 从 html 中解析 img 标签
	 * 
	 * @param html
	 *            是否包含当前节点
	 * @return json 字符串
	 */
	public static String[] getImages(String html) {
		// <img[^>]+src\s*=\s*['""]([^'""]+)['""][^>]*>
		Matcher matcher = Pattern.compile(
				"<img[^>]+src\\s*=\\s*['\"\"]([^'\"\"]+)['\"\"][^>]*>",
				Pattern.CASE_INSENSITIVE | Pattern.MULTILINE).matcher(html);
		List list = new ArrayList();
		while (matcher.find()) {
			String group = matcher.group(1);
			list.add(group);
		}
		return (String[]) list.toArray(new String[list.size()]);
	}

	/**
	 * 
	 * 基本功能：过滤所有以"<"开头以">"结尾的标签，替换 &nbsp; 为空格
	 * 
	 * @param html
	 * @return String
	 */
	public static String clearHtml(String html) {
		Pattern pattern = Pattern.compile("<([^>]*)>");
		Matcher matcher = pattern.matcher(html);
		StringBuffer sb = new StringBuffer();
		boolean result = matcher.find();
		while (result) {
			matcher.appendReplacement(sb, "");
			result = matcher.find();
		}
		matcher.appendTail(sb);
		return sb.toString().replaceAll("&nbsp;", " ").trim();
	}

	/**
	 * 验证正则表达式是否匹配，区分大小写
	 * 
	 * @param regex
	 *            正则表达式
	 * @param data
	 *            待验证的字符串
	 * @return 如果是符合的字符串，返回 <b>true</b>，否则为 <b>false</b>
	 */
	public static boolean findExists(String regex, String data) {
		Pattern pattern = Pattern.compile(regex);
		Matcher matcher = pattern.matcher(data);
		// find() 方法是部分匹配，是查找输入串中与模式匹配的子串，如果该匹配的串有组还可以使用group()函数。
		// matches() 是全部匹配，是将整个输入串与模式匹配，如果要验证一个输入的数据是否为数字类型或其他类型，一般要用matches()。
		return matcher.find();
	}

	/**
	 * 验证正则表达式是否匹配，忽略大小写
	 * 
	 * @param regex
	 *            正则表达式
	 * @param data
	 *            待验证的字符串
	 * @param flags
	 *            Match flags, a bit mask that may include CASE_INSENSITIVE,
	 *            MULTILINE, DOTALL...
	 * @return 如果是符合的字符串，返回 <b>true</b>，否则为 <b>false</b>
	 */
	public static boolean findExists(String regex, String data, int flags) {
		Pattern pattern = Pattern.compile(regex, flags);
		Matcher matcher = pattern.matcher(data);
		return matcher.find();
	}

	/**
	 * 提取分组数据
	 * 
	 * @param regex
	 *            正则表达式
	 * @param data
	 *            待验证的字符串
	 * @param groupIndex
	 *            返回匹配的分组数据，从 1 开始计数
	 * @return 如果是符合的字符串，返回 <b>true</b>，否则为 <b>false</b>
	 */
	public static List<String> findGroup(String regex, String data,
			int groupIndex) {
		List<String> list = new ArrayList<String>();
		Pattern pattern = Pattern.compile(regex);
		Matcher matcher = pattern.matcher(data);
		while (matcher.find()) {
			list.add(matcher.group(groupIndex));
		}
		return list;
	}
}
