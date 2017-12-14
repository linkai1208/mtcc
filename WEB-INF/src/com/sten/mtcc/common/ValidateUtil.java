package com.sten.mtcc.common;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.security.SecureRandom;
import java.util.HashMap;

import javax.imageio.ImageIO;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.sten.framework.util.PropertiesUtil;

/**
 * Created by ztw-a on 2017/6/9.
 */
public class ValidateUtil {
	private static final Logger logger = LoggerFactory
			.getLogger(ValidateUtil.class);

	public static SecureRandom random = new SecureRandom();

	public static int r(int min, int max) {
		int num = 0;
		num = random.nextInt(max - min) + min;
		return num;
	}

	public static HashMap<String, String> createValidateImg() {
		HashMap<String, String> returnValue = new HashMap<String, String>();
		String code = "";
		// 在内存中创建一副图片
		int w = 90;
		int h = 30;
		BufferedImage img = new BufferedImage(w, h, BufferedImage.TYPE_INT_RGB);
		// 在图片上画一个矩形当背景
		Graphics g = img.getGraphics();
		// g.setColor(new Color(r(50,250),r(50,250),r(50,250)));
		g.setColor(new Color(r(50, 250), r(50, 250), r(50, 250)));
		g.fillRect(0, 0, w, h);

		String str = "aqzxswedcfrvgtbhyujklp23456789";
		for (int i = 0; i < 4; i++) {
			g.setColor(new Color(r(50, 180), r(50, 180), r(50, 180)));
			g.setFont(new Font("黑体", Font.PLAIN, 30));
			char c = str.charAt(r(0, str.length()));
			code += c;
			// g.drawString(String.valueOf(c), 10+i*30, r(h-30,h));
			g.drawString(String.valueOf(c), 5 + i * 20, r(20, 25));
		}

		// 画随机线
		// for(int i=0;i<25;i++){
		// g.setColor(new Color(r(50,180),r(50,180),r(50,180)));
		// g.drawLine(r(0,w), r(0,h),r(0,w), r(0,h));
		// }
		// 把内存中创建的图像输出到文件中
		String filePath = PropertiesUtil.getProperty("V_CODE_IMG");
		String fileName = "vcode_" + System.currentTimeMillis() + ".png";
		String path = filePath + fileName;
		path = CommonUtil.getSafeFilePath(path, "V_CODE_IMG");
		String vCodeImg = CommonUtil.getSafePath(filePath, "V_CODE_IMG");
		File vCode = new File(vCodeImg);
		if (!vCode.exists()) {
			vCode.mkdirs();
		}
		File file = new File(path);
		try {
			ImageIO.write(img, "png", file);
		} catch (IOException e) {
			e.printStackTrace();
		}
		returnValue.put("vCode", code);
		returnValue.put("vCodeName", fileName);
		return returnValue;
	}
}
