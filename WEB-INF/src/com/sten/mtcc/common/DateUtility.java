package com.sten.mtcc.common;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class DateUtility {
	public DateUtility() {
	}

	/**
	 * 数字转换为日期时间型（yyyy-MM-dd HH:mm:ss）
	 * 
	 * @param intValue
	 * @return
	 */
	public static String getInt2YMDHMS(String intValue) {
		long timestamp = Long.parseLong(intValue) * 1000;
		String date = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
				.format(new Date(timestamp));
		return date;
	}

	/**
	 * 数字转换为日期型（yyyy-MM-dd）
	 * 
	 * @param intValue
	 * @return
	 */
	public static String getInt2YMD(String intValue) {
		long timestamp = Long.parseLong(intValue) * 1000;
		String date = new SimpleDateFormat("yyyy-MM-dd").format(new Date(
				timestamp));
		return date;
	}

	/**
	 * 日期型（yyyy-MM-dd HH:mm:ss）转换为数字
	 * 
	 * @param ymdhms
	 * @return
	 */
	public static int getStrYMDHMS2Int(String ymdhms) {
		java.sql.Timestamp date = java.sql.Timestamp.valueOf(ymdhms);
		SimpleDateFormat simpleDateFormat = new SimpleDateFormat(
				"yyyy-MM-dd HH:mm:ss");
		int value = 0;
		try {
			value = new Long((simpleDateFormat.parse(simpleDateFormat
					.format(date))).getTime() / 1000).intValue();
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return value;
	}

	/**
	 * 日期型（yyyy-MM-dd）转换为数字
	 * 
	 * @param ymd
	 * @return
	 */
	public static int getStrYMD2Int(String ymd) {
		Date date = java.sql.Date.valueOf(ymd);
		SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
		int value = 0;
		try {
			value = new Long((simpleDateFormat.parse(simpleDateFormat
					.format(date))).getTime() / 1000).intValue();
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return value;
	}

	/**
	 * 指定日期（yyyy-MM-dd）的前后几天
	 * 
	 * @param ymd
	 * @param days
	 *            天数
	 * @return
	 */
	public static String getDaysLaterYMD(String ymd, int days) {
		java.sql.Date date = java.sql.Date.valueOf(ymd);
		date.setDate(date.getDate() + days);
		return new SimpleDateFormat("yyyy-MM-dd").format(date);
	}

	/**
	 * 指定日期（yyyy-MM-dd）的前后几年
	 * 
	 * @param ymd
	 * @param years
	 *            年数
	 * @return
	 */
	public static String getYearsLaterYMD(String ymd, int years) {
		java.sql.Date date = java.sql.Date.valueOf(ymd);
		date.setYear(date.getYear() + years);
		return new SimpleDateFormat("yyyy-MM-dd").format(date);
	}

	/**
	 * 指定日期时间（yyyy-MM-dd HH:mm:ss）的前后几天
	 * 
	 * @param ymdhms
	 * @param days
	 * @return
	 */
	public static String getDaysLaterYMDHMS(String ymdhms, int days) {
		java.sql.Timestamp date = java.sql.Timestamp.valueOf(ymdhms);
		date.setDate(date.getDate() + days);
		return new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(date);
	}

	/**
	 * 指定日期时间（yyyy-MM-dd HH:mm:ss）的前后几小时
	 * 
	 * @param ymdhms
	 * @param hours
	 * @return
	 */
	public static String getHoursLaterYMDHMS(String ymdhms, int hours) {
		java.sql.Timestamp date = java.sql.Timestamp.valueOf(ymdhms);
		date.setHours(date.getHours() + hours);
		return new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(date);
	}

	/**
	 * 指定日期时间（yyyy-MM-dd HH:mm:ss）的前后几分钟
	 * 
	 * @param ymdhms
	 * @param minutes
	 * @return
	 */
	public static String getMinutesLaterYMDHMS(String ymdhms, int minutes) {
		java.sql.Timestamp date = java.sql.Timestamp.valueOf(ymdhms);
		date.setMinutes(date.getMinutes() + minutes);
		return new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(date);
	}

	/**
	 * 两个时间之间的天数
	 * 
	 * @param date1
	 * @param date2
	 * @return
	 */
	public static long getDays(String date1, String date2) {
		if (date1 == null || date1.equals(""))
			return 0;
		if (date2 == null || date2.equals(""))
			return 0;
		// 转换为标准时间
		SimpleDateFormat myFormatter = new SimpleDateFormat("yyyy-MM-dd");
		java.util.Date date = null;
		java.util.Date mydate = null;
		long day = 0;

		try {
			date = myFormatter.parse(date1);
			mydate = myFormatter.parse(date2);
			day = (date.getTime() - mydate.getTime()) / (24 * 60 * 60 * 1000);
		} catch (Exception e) {
		}
		return day;
	}

}
