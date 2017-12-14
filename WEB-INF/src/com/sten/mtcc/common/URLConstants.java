package com.sten.mtcc.common;

/**
 * Created by ztw-a on 2017/9/2.
 */
public class URLConstants {
	public static String[] COMMON_URL = { "/user/uavlist",
			"/user/open/uavlist", "/user/cancel", "/user/save/cancel",
			"/user/mobile/modify", "/user/save/mobile", "/user/modify",
			"/user/check/addUav", "/mtcc/add/self", "/mtcc/logout",
			"/open/uavInfo/", "/mtcc/image", "/user/password/validate" };

	/**
	 * 个人配置菜单
	 */
	public static String[] PERSON_URL = { "/user/open/password",
			"/user/password", "/user/userInfo", "/user/save/password",
			"/user/save/info", "/user/save/papersNum", "/mtcc/add",
			"/mtcc/trademark/modelInfo" };

	/**
	 * 单位配置菜单
	 */
	public static String[] UNIT_URL = { "/user/open/password",
			"/user/password", "/user/userInfo", "/user/userInfo/company",
			"/user/companyInfo/save", "/mtcc/add", "/mtcc/trademark/modelInfo" };

	/**
	 * 厂商配置菜单
	 */
	public static String[] FACTORY_URL = { "/user/open/password",
			"/user/password", "/user/userInfo", "/user/userInfo/factory",
			"/user/factoryInfo/save", "/user/factory/attachment",
			"/user/factory/download", "/logout", "/model/open/modelList",
			"/model/modelList", "/model/add", "/model/logout",
			"/factory/open/userList", "/factory/userList", "/factory/logout",
			"/factory/getArea", "/factory/add", "/factory/modify",
			"/factory/getFileList", "/factory/importUav/action",
			"/factory/mtcc/import/list", "/factory/upload/xlsx",
			"/factory/mtcc/import", "/factory/mtcc/import/delete",
			"public/qr/createQRForYH" };

	/**
	 * 管理员配置菜单
	 */
	public static String[] ADMIN_URL = { "/admin/open/userList",
			"/admin/userList", "/admin/factory/download",
			"/admin/password/validate", "/factory/open/userList",
			"/factory/userList", "/factory/logout", "/factory/add",
			"/factory/modify", "/factory/getFileList", "/admin/open/uavList",
			"/admin/uavList", "/admin/showUav", "/admin/open/adminList",
			"/admin/adminList", "/admin/logout", "/admin/add", "/admin/modify",
			"/admin/open/gAdminList", "/admin/gAdminList", "/gAdmin/logout",
			"/gAdmin/add", "/gAdmin/modify", "/admin/pw/modify",
			"/admin/save/password", "/admin/open/logList", "/admin/logList",
			"/log/logout", "/admin/cache/clear", "/admin/cache/open",
			"/admin/open/areaList", "/admin/areaList", "/admin/saveArea",
			"/area/logout", "/admin/open/stats/registUser",
			"/admin/search/stats/registUser", "/admin/out/stats/registUser",
			"/admin/open/stats/factory", "/admin/search/stats/factory",
			"/admin/out/stats/factory", "/admin/open/stats/uavRegist",
			"/admin/out/stats/uavRegist", "/admin/search/stats/uavRegist",
			"/admin/open/stats/uavRegistByType",
			"/admin/out/stats/uavRegistByType",
			"/admin/search/stats/uavRegistByType",
			"/admin/open/stats/uavRegistByPurpose",
			"/admin/out/stats/uavRegistByPurpose",
			"/admin/search/stats/uavRegistByPurpose",
			"/admin/open/stats/uavRegistByUType",
			"/admin/out/stats/uavRegistByUType",
			"/admin/search/stats/uavRegistByUType",
			"/admin/out/stats/uavRegistByFlyWeight",
			"/admin/open/stats/uavRegistByFlyWeight",
			"/admin/search/stats/uavRegistByFlyWeight",
			"/admin/out/stats/uavRegistByFactory",
			"/admin/open/stats/uavRegistByFactory",
			"/admin/search/stats/uavRegistByFactory",
			"/admin/open/stats/factoryModal/",
			"/admin/search/stats/factoryModal/", "/mtcc/trademark/modelInfo",
			"/user/modify", "/factory/open/regFactoryList",
			"/factory/reg/save", "/factory/reg/regFactoryList",
			"/factory/reg/logout" };
}
