package com.sten.framework.util;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.WeakHashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class MessageUtil {

	private static WeakHashMap verifyCodes = new WeakHashMap();

	public static boolean sendMessage(String telphone, String message) {
		// webservice地址
		String serviceURL = PropertiesUtil.getProperty("MESSAGE_URL");// 发短信的接口地址
																		// "http://sdk2.entinfo.cn:8061/webservice.asmx";
		// webservice方法
		String soapAction = PropertiesUtil.getProperty("MESSAGE_FUN");// 发短信的方法"http://entinfo.cn/mdsmssend";
		// 某参数
		String mdsmssend = PropertiesUtil.getProperty("MESSAGE_ARG");// "http://entinfo.cn";
		// 序列号
		String sn = PropertiesUtil.getProperty("MESSAGE_NUM");
		// 密码
		String password = PropertiesUtil.getProperty("MESSAGE_PWD");

		String pwd = "";
		// 密码为md5(sn+password)
		try {
			pwd = getMD5(password);// 调用密码加密
			System.out.println("***********密码加密************");
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}

		String error = dosendmsg(message, telphone, sn, pwd, serviceURL,
				soapAction, mdsmssend);// 调用发的接口
		System.out.println("***********发送短信结果：" + error + "************");

		if (error.equals("")) {
			return false;
		}

		// List<UserMsgInfo> msgInfolist = task.getUserMsgList();
		// String errorMessage = "";
		// for (UserMsgInfo info : msgInfolist) {
		// String error = dosendmsg(context, info, sn, pwd, serviceURL,
		// soapAction, mdsmssend);//调用发的接口
		// if (!JqLib.isEmpty(error)) {
		// errorMessage += error + ";";
		// }
		// }
		// ReturnObject ro = new ReturnObject();
		// if (!JqLib.isEmpty(errorMessage)) {
		// ro.SetErrored(true);
		// ro.setErrorMessage(errorMessage);
		// task.setRo(ro);
		// }

		return true;
	}

	/*
	 * 方法名称：getMD5 功 能：字符串MD5加密 参 数：待转换字符串 返 回 值：加密之后字符串
	 */
	private static String getMD5(String sourceStr)
			throws UnsupportedEncodingException {
		String resultStr = "";
		try {
			byte[] temp = sourceStr.getBytes();
			MessageDigest md5 = MessageDigest.getInstance("MD5");
			md5.update(temp);
			// resultStr = new String(md5.digest());
			byte[] b = md5.digest();
			for (int i = 0; i < b.length; i++) {
				char[] digit = { '0', '1', '2', '3', '4', '5', '6', '7', '8',
						'9', 'A', 'B', 'C', 'D', 'E', 'F' };
				char[] ob = new char[2];
				ob[0] = digit[(b[i] >>> 4) & 0X0F];
				ob[1] = digit[b[i] & 0X0F];
				resultStr += new String(ob);
			}
			return resultStr;
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
			return null;
		}
	}

	/*
	 * 发送短信的方法
	 */
	private static String dosendmsg(String message, String telphone, String sn,
			String password, String serviceURL, String soapAction,
			String mdsmssend) {
		String errorMessage = "";
		// GUID userid = info.getUserID();
		// String content = info.getMsgTitle() + "," + info.getMsgContent();
		// FUser user = context.find(FUser.class, userid);

		// if (user != null) {
		// usertitle = user.getTitle();
		// phone = user.getPhone();
		// }
		// if (JqLib.isEmpty(phone)) {
		// errorMessage += "短信消息，发送失败！" + usertitle + "用户手机号为空！";
		// return errorMessage;
		// }

		if (telphone.isEmpty()) {
			errorMessage += "短信消息，发送失败！ 用户手机号为空！";
			System.out.println("***********发送短信结果：" + errorMessage
					+ "************");
			return errorMessage;
		}

		String result = "";
		String xml = "<?xml version=\"1.0\" encoding=\"utf-8\"?>";
		xml += "<soap:Envelope xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xmlns:xsd=\"http://www.w3.org/2001/XMLSchema\" xmlns:soap=\"http://schemas.xmlsoap.org/soap/envelope/\">";
		xml += "<soap:Body>";
		xml += "<SendSms  xmlns=\"http://tempuri.org/\">";
		xml += "<username>" + sn + "</username>";
		xml += "<password>" + password + "</password>";
		xml += "<mobile>" + telphone + "</mobile>";
		xml += "<content>" + message + "</content>";
		xml += "<ext>" + "" + "</ext>";
		xml += "<schtime>" + "" + "</schtime>";
		xml += "<rrid>" + "" + "</rrid>";
		xml += "</SendSms>";
		xml += "</soap:Body>";
		xml += "</soap:Envelope>";

		System.out.println("***********发送短信xml：" + xml + "************");
		URL url;
		InputStreamReader isr = null;
		BufferedReader in = null;
		OutputStream out = null;
		try {
			url = new URL(serviceURL);

			URLConnection connection = url.openConnection();
			HttpURLConnection httpconn = (HttpURLConnection) connection;
			ByteArrayOutputStream bout = new ByteArrayOutputStream();
			bout.write(xml.getBytes());
			byte[] b = bout.toByteArray();
			httpconn.setRequestProperty("Host", "192.168.4.30");
			httpconn.setRequestProperty("Content-Length",
					String.valueOf(b.length));
			httpconn.setRequestProperty("Content-Type",
					"text/xml; charset=gb2312");
			httpconn.setRequestProperty("SOAPAction", soapAction);
			httpconn.setRequestMethod("POST");
			httpconn.setDoInput(true);
			httpconn.setDoOutput(true);// 请求短信

			out = httpconn.getOutputStream();
			out.write(b);
			out.close();

			isr = new InputStreamReader(httpconn.getInputStream());
			in = new BufferedReader(isr);
			String inputLine;
			while (null != (inputLine = in.readLine())) {
				System.out.println("***********发送短信结果inputLine：" + inputLine
						+ "************");
				Pattern pattern = Pattern
						.compile("<SendSmsResult>(.*)</SendSmsResult>");
				Matcher matcher = pattern.matcher(inputLine);
				while (matcher.find()) {
					result = matcher.group(1);
					System.out.println("***********发送短信结果SendSmsResult："
							+ pattern + "************");
				}
			}

			System.out.println("***********发送短信结果result：" + result
					+ "************");
			if (result.length() > 3) {
				return result;
			} else {
				return getErrorMsg(result);
			}
		} catch (Exception e) {
			e.printStackTrace();
			return "";
		} finally {
			if (out != null) {
				try {
					out.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			if (isr != null) {
				try {
					isr.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			if (in != null) {
				try {
					in.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}

	private static String getErrorMsg(String result) {
		if ("-2".equals(result)) {
			return "帐号/密码不正确";
		} else if ("-4".equals(result)) {
			return "余额不足支持本次发送";
		} else if ("-5".equals(result)) {
			return "数据格式错误";
		}
		return "";
	}

	// private static final String targetURL =
	// "http://10.9.243.189:9080/mps/push/msg";

	// public static String sendNoticeToMobile(String type,String title,String
	// Content,String receiveUser) {
	// String resultStr = "";
	// StringBuffer msgBuffString = new StringBuffer();
	// String dspGroupKey = "";
	// int typeInt = Integer.parseInt(type);
	// //通告类型 注册验证码，查看诚信分验证码，考试安排通知验证码，计划提交审核
	// switch (typeInt) {
	// case 0:
	// dspGroupKey = "注册验证码";// 维修人员
	// break;
	// case 1:
	// dspGroupKey = "注册通过提醒";// 维修人员
	// break;
	// case 2:
	// dspGroupKey = "诚信分查询验证码";// 维修人员
	// break;
	// case 3:
	// dspGroupKey = "考试安排通知";// 维修人员
	// break;
	// case 4:
	// dspGroupKey = "登录验证码";// 考管中心和考点
	// break;
	// case 5:
	// dspGroupKey = "计划提交审核";// 给考管中心
	// break;
	// case 6:
	// dspGroupKey = "考官新的考试安排";// 到期提醒等
	// break;
	// default:
	// break;
	// }
	//
	// try {
	// msgBuffString.append("{")
	// .append("\"msgContent\":\"{\\\"msgTopic\\\":\\\"").append(dspGroupKey+"新消息").append("\\\",\\\"msgContent\\\":\\\"").append(title).append("\\\"}\",")
	// .append("\"validTime\":86400000,")
	// .append("\"appId\":\"com.AirchinaMEAP\",")
	// .append("\"msgKind\":\"0000280101\",")
	// .append("\"receiveUser\":[").append(receiveUser).append("],")
	// .append("\"msgDescription\":\"lapp=LAPP00003&dspNotify=y&dspMsgCent=y&dspGroupKey=").append(dspGroupKey).append("&termValidTime=120\"")
	// .append("}");
	//
	// URL targetUrl = new URL(targetURL);
	// HttpURLConnection httpConnection = (HttpURLConnection)
	// targetUrl.openConnection();
	// httpConnection.setDoOutput(true);
	// httpConnection.setRequestMethod("POST");
	// httpConnection.setRequestProperty("Content-Type",
	// "application/json;charset=utf-8");
	// String input = msgBuffString.toString();
	// OutputStream outputStream = httpConnection.getOutputStream();
	// outputStream.write(URLDecoder.decode(input, "UTF-8").getBytes());
	// outputStream.flush();
	// if (httpConnection.getResponseCode() != 200) {
	// resultStr =
	// "{\"ret_code\":"+httpConnection.getResponseCode()+",\"error_msg\":\"访问错误\"}";
	// }
	// BufferedReader responseBuffer = new BufferedReader(new InputStreamReader(
	// (httpConnection.getInputStream())));
	//
	// String output;
	// while ((output = responseBuffer.readLine()) != null) {
	// resultStr+= URLDecoder.decode(output, "UTF-8");
	// }
	// httpConnection.disconnect();
	// } catch (MalformedURLException e) {
	// resultStr = "{\"ret_code\":500,\"error_msg\":\""+e.getMessage()+"\"}";
	// } catch (IOException e) {
	// resultStr = "{\"ret_code\":500,\"error_msg\":\""+e.getMessage()+"\"}";
	// }
	// return resultStr;
	// }

	// /**
	// * 获得5位随机的验证码
	// * @return
	// */
	// public static String getRandomCode(){
	// return (int)((Math.random()*9+1)*100000) + "";
	// }
	//
	//
	// public static String getSendMessage(String code){
	// return String.format("验证码%1$s, 请勿转发他人。", code);
	// }
}
