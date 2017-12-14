package com.sten.framework.util;

import java.awt.Graphics2D;
import java.awt.Transparency;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

import javax.imageio.ImageIO;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by linkai on 2017/4/5.
 */
public class ImageUtil {

	private static final Logger logger = LoggerFactory
			.getLogger(ImageUtil.class);

	/**
	 * 图片去白色的背景，并裁切
	 * 
	 * @param sourceImageFile
	 *            原始图片完整路径
	 * @param targetImageFile
	 *            生成的目标图片完整路径, 生成的图片为 png 格式
	 * @param range
	 *            范围 1-255 越大 容错越高 去掉的背景越多
	 * @return true成功, false失败
	 * @throws Exception
	 *             异常
	 */
	public static boolean transferAlpha(String sourceImageFile,
			String targetImageFile, int range) {
		boolean result = false;
		InputStream in = null;
		BufferedImage sourceImage = null;
		BufferedImage targetImage = null;
		BufferedImage subImage = null;
		try {
			in = new FileInputStream(sourceImageFile);
			sourceImage = ImageIO.read(in);

			Graphics2D g2D = (Graphics2D) sourceImage.getGraphics();
			// ---------- 增加下面的代码使得背景透明 -----------------
			targetImage = g2D.getDeviceConfiguration().createCompatibleImage(
					sourceImage.getWidth(), sourceImage.getHeight(),
					Transparency.TRANSLUCENT);
			g2D.dispose();
			g2D = targetImage.createGraphics();
			g2D.dispose();
			// ---------- 背景透明代码结束 -----------------

			int alpha = 0;
			int minX = sourceImage.getWidth();
			int minY = sourceImage.getHeight();
			int maxX = 0;
			int maxY = 0;

			for (int j1 = sourceImage.getMinY(); j1 < sourceImage.getHeight(); j1++) {
				for (int j2 = sourceImage.getMinX(); j2 < sourceImage
						.getWidth(); j2++) {
					int rgb = sourceImage.getRGB(j2, j1);

					int R = (rgb & 0xff0000) >> 16;
					int G = (rgb & 0xff00) >> 8;
					int B = (rgb & 0xff);
					if (((255 - R) < range) && ((255 - G) < range)
							&& ((255 - B) < range)) {
						// 原图去除白色背景
						rgb = ((alpha + 1) << 24) | (rgb & 0x00ffffff);
					} else {
						minX = minX <= j2 ? minX : j2;
						minY = minY <= j1 ? minY : j1;
						maxX = maxX >= j2 ? maxX : j2;
						maxY = maxY >= j1 ? maxY : j1;
						// 非白色绘制到目标图片, 颜色转换为黑色
						rgb = ((alpha + 1) << 24) | (rgb & 0xee000000);
						targetImage.setRGB(j2, j1, rgb);
						// targetImage.setRGB(j2, j1, Color.black.getRGB());
					}
				}
			}

			// 裁剪
			int width = maxX - minX;
			int height = maxY - minY;
			int flag = 2; // 裁剪时预留的区域
			subImage = targetImage.getSubimage(minX - flag, minY - flag, width
					+ flag, height + flag);

			ImageIO.write(subImage, "png", new File(targetImageFile));
			result = true;
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("图片转换失败, 原始图片:" + sourceImageFile);
			logger.error(e.toString());
		} finally {
			if (in != null) {
				try {
					in.close();
				} catch (IOException e) {
					logger.error("图片转换失败, 关闭文件流异常:" + sourceImageFile);
					e.printStackTrace();
				}
			}
		}
		return result;
	}
}
