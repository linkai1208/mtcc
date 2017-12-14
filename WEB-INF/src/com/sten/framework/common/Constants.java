package com.sten.framework.common;

public class Constants {

	public static int PAGE_ROW_COUNT = 10;

	public static String CONTEXT_PATH = "";

	// 审核操作状态
	public enum ACTION {

		SUBMIT("提交", "0"), APROVE("通过", "1"), REJECT("退回", "2");

		private String name;
		private String code;

		ACTION(String name, String code) {
			this.name = name;
			this.code = code;
		}

		public String getName() {
			return name;
		}

		public String getCode() {
			return code;
		}

		public static String getName(String code) {
			String result = "";
			for (ACTION e : values()) {
				if (e.getCode().equals(code)) {
					result = e.getName();
					break;
				}
			}
			return result;
		}
	}
}
