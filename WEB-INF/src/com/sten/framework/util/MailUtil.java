package com.sten.framework.util;

import java.io.File;
import java.io.UnsupportedEncodingException;
import java.util.Properties;

import javax.activation.DataHandler;
import javax.activation.DataSource;
import javax.activation.FileDataSource;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.NoSuchProviderException;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import javax.mail.internet.MimeUtility;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.sten.mtcc.common.CommonUtil;

/**
 * 
 * @description 邮件服务
 * @author linkai
 * @date 2016-3-9
 * @time 下午05:09:38
 */
@Service
public class MailUtil {

	private static final Logger logger = LoggerFactory
			.getLogger(MailUtil.class);

	/**
	 * 
	 * @param host
	 *            邮件服务器地址
	 * @param port
	 *            邮件服务器端口
	 * @param user
	 *            某些邮件服务器的账号仅包括@前面部分的，某些邮件服务器的账号是完整的邮件地址
	 * @param password
	 *            发件人邮箱密码
	 * @param from
	 *            发件人完整邮件地址
	 * @param to
	 *            收件人邮件地址
	 * @param cc
	 *            抄送人邮件地址，可以为 null
	 * @param bcc
	 *            密送人邮件地址，可以为 null
	 * @param suject
	 *            标题
	 * @param content
	 *            正文，可以是 html
	 * @param attchPath
	 *            附件路径
	 * @param attchName
	 *            附件名称
	 */
	public static void sendMail(final String host, final String port,
			final String user, final String password, final String from,
			final String to, final String cc, final String bcc,
			final String suject, final String content, final String attchPath,
			String attchName) {

		try {
			Properties prop = new Properties();
			prop.setProperty("mail.debug", "true");
			prop.setProperty("mail.host", host);
			prop.setProperty("mail.smtp.port", port);
			prop.setProperty("mail.transport.protocol", "smtp");
			if ("465".equals(port)) {
				prop.setProperty("mail.smtp.socketFactory.port", port);
				prop.setProperty("mail.smtp.socketFactory.class",
						"javax.net.ssl.SSLSocketFactory");
			}
			prop.setProperty("mail.smtp.auth", "true");

			// 1、创建session
			Session session = Session.getInstance(prop);
			Transport ts = null;

			// 2、通过session得到transport对象
			ts = session.getTransport();

			// 3、连上邮件服务器
			ts.connect(host, user, password);

			// 4、创建邮件
			MimeMessage message = new MimeMessage(session);

			// 邮件消息头
			// 发件人
			try {
				message.setFrom(new InternetAddress("\""
						+ MimeUtility.encodeText("无人机实名登记系统") + "\"<" + from
						+ ">"));
			} catch (UnsupportedEncodingException e) {
				message.setFrom(new InternetAddress(from));
			}
			// message.setFrom(new InternetAddress(from)); // 邮件的发件人
			if (to.indexOf(",") > -1) {
				InternetAddress[] tos = InternetAddress.parse(to);
				message.setRecipients(Message.RecipientType.TO, tos); // 邮件的收件人
			} else {
				message.setRecipient(Message.RecipientType.TO,
						new InternetAddress(to)); // 邮件的收件人
			}
			if (cc != null && cc.length() > 0) {
				if (cc.indexOf(",") > -1) {
					InternetAddress[] ccs = InternetAddress.parse(cc);
					message.setRecipients(Message.RecipientType.CC, ccs); // 邮件的抄送人
				} else {
					message.setRecipient(Message.RecipientType.CC,
							new InternetAddress(cc)); // 邮件的抄送人
				}
			}
			if (bcc != null && bcc.length() > 0) {
				if (bcc.indexOf(",") > -1) {
					InternetAddress[] bccs = InternetAddress.parse(bcc);
					message.setRecipients(Message.RecipientType.CC, bccs); // 邮件的密送人
				} else {
					message.setRecipient(Message.RecipientType.BCC,
							new InternetAddress(bcc)); // 邮件的密送人
				}
			}
			message.setSubject(suject); // 邮件的标题
			MimeBodyPart text = new MimeBodyPart();
			text.setContent(content, "text/html;charset=UTF-8");

			// 描述数据关系
			MimeMultipart mm = new MimeMultipart();
			mm.setSubType("related");
			mm.addBodyPart(text);

			// 添加邮件附件
			// if(files != null){
			// for (String filename : files) {
			// MimeBodyPart attachPart = new MimeBodyPart();
			// attachPart.attachFile(filename);
			// mm.addBodyPart(attachPart);
			// }
			// }
			// 把文件，添加到附件1中
			// 数据源
			MimeBodyPart attch1 = new MimeBodyPart();
			String namePath = CommonUtil.getSafeFilePath(attchPath + attchName,
					"QR_PDF_PATH");
			DataSource ds1 = new FileDataSource(new File(namePath));
			// 数据处理器
			DataHandler dh1 = new DataHandler(ds1);
			// 设置第一个附件的数据
			attch1.setDataHandler(dh1);
			// 设置第一个附件的文件名
			try {
				attch1.setFileName(MimeUtility.encodeText("二维码") + ".pdf");
			} catch (UnsupportedEncodingException e) {
				attch1.setFileName(attchName);
			}

			// 把附件加入到 MINE消息体中
			mm.addBodyPart(attch1);

			message.setContent(mm);
			message.saveChanges();

			// 5、发送邮件
			ts.sendMessage(message, message.getAllRecipients());
			ts.close();

		} catch (NoSuchProviderException e) {
			e.printStackTrace();
			logger.error("[MAIL SERVER ERROR]：", e);
		} catch (AddressException e) {
			e.printStackTrace();
			logger.error("[MAIL SERVER ERROR]：", e);
		} catch (MessagingException e) {
			e.printStackTrace();
			logger.error("[MAIL SERVER ERROR]：", e);
		}

	}

	/**
	 * 
	 * @param host
	 *            邮件服务器地址
	 * @param port
	 *            邮件服务器端口
	 * @param user
	 *            某些邮件服务器的账号仅包括@前面部分的，某些邮件服务器的账号是完整的邮件地址
	 * @param password
	 *            发件人邮箱密码
	 * @param from
	 *            发件人完整邮件地址
	 * @param to
	 *            收件人邮件地址
	 * @param cc
	 *            抄送人邮件地址，可以为 null
	 * @param bcc
	 *            密送人邮件地址，可以为 null
	 * @param suject
	 *            标题
	 * @param content
	 *            正文，可以是 html
	 */
	public static void sendMailToErro(final String host, final String port,
			final String user, final String password, final String from,
			final String to, final String cc, final String bcc,
			final String suject, final String content) {

		try {
			Properties prop = new Properties();
			prop.setProperty("mail.debug", "true");
			prop.setProperty("mail.host", host);
			prop.setProperty("mail.smtp.port", port);
			prop.setProperty("mail.transport.protocol", "smtp");
			if ("465".equals(port)) {
				prop.setProperty("mail.smtp.socketFactory.port", port);
				prop.setProperty("mail.smtp.socketFactory.class",
						"javax.net.ssl.SSLSocketFactory");
			}
			prop.setProperty("mail.smtp.auth", "true");

			// 1、创建session
			Session session = Session.getInstance(prop);
			Transport ts = null;

			// 2、通过session得到transport对象
			ts = session.getTransport();

			// 3、连上邮件服务器
			ts.connect(host, user, password);

			// 4、创建邮件
			MimeMessage message = new MimeMessage(session);

			// 邮件消息头
			// 发件人
			try {
				message.setFrom(new InternetAddress("\""
						+ MimeUtility.encodeText("无人机实名登记系统") + "\"<" + from
						+ ">"));
			} catch (UnsupportedEncodingException e) {
				message.setFrom(new InternetAddress(from));
			}
			// message.setFrom(new InternetAddress(from)); // 邮件的发件人
			if (to.indexOf(",") > -1) {
				InternetAddress[] tos = InternetAddress.parse(to);
				message.setRecipients(Message.RecipientType.TO, tos); // 邮件的收件人
			} else {
				message.setRecipient(Message.RecipientType.TO,
						new InternetAddress(to)); // 邮件的收件人
			}
			if (cc != null && cc.length() > 0) {
				if (cc.indexOf(",") > -1) {
					InternetAddress[] ccs = InternetAddress.parse(cc);
					message.setRecipients(Message.RecipientType.CC, ccs); // 邮件的抄送人
				} else {
					message.setRecipient(Message.RecipientType.CC,
							new InternetAddress(cc)); // 邮件的抄送人
				}
			}
			if (bcc != null && bcc.length() > 0) {
				if (bcc.indexOf(",") > -1) {
					InternetAddress[] bccs = InternetAddress.parse(bcc);
					message.setRecipients(Message.RecipientType.CC, bccs); // 邮件的密送人
				} else {
					message.setRecipient(Message.RecipientType.BCC,
							new InternetAddress(bcc)); // 邮件的密送人
				}
			}
			message.setSubject(suject); // 邮件的标题
			MimeBodyPart text = new MimeBodyPart();
			text.setContent(content, "text/html;charset=UTF-8");

			// 描述数据关系
			MimeMultipart mm = new MimeMultipart();
			mm.setSubType("related");
			mm.addBodyPart(text);

			message.setContent(mm);
			message.saveChanges();

			// 5、发送邮件
			ts.sendMessage(message, message.getAllRecipients());
			ts.close();

		} catch (NoSuchProviderException e) {
			e.printStackTrace();
			logger.error("[MAIL SERVER ERROR]：", e);
		} catch (AddressException e) {
			e.printStackTrace();
			logger.error("[MAIL SERVER ERROR]：", e);
		} catch (MessagingException e) {
			e.printStackTrace();
			logger.error("[MAIL SERVER ERROR]：", e);
		}

	}
}
