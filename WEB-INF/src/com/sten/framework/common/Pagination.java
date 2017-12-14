package com.sten.framework.common;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import org.apache.commons.codec.binary.Base64;
import org.springframework.util.StringUtils;

import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * Created by linkai on 16-1-22.
 * 
 * 2017-04-22 处理排序字段，防止sql注入 2017-05-23 增加 getValue() 函数，直接获取参数值
 */

public class Pagination {
	// / <summary>
	// / 当前页，从1开始计数
	// / </summary>
	public int page;

	// / <summary>
	// / 每页显示记录行数
	// / </summary>
	public int rows;

	// / <summary>
	// / 排序字段
	// / </summary>
	public String order = "";

	// / <summary>
	// / 升序/降序
	// / </summary>
	private String sort = "";

	public int getPage() {
		return page <= 0 ? 1 : page;
	}

	public void setPage(int page) {
		this.page = page;
	}

	public int getRows() {
		return rows <= 0 ? Constants.PAGE_ROW_COUNT : rows;
	}

	public void setRows(int rows) {
		this.rows = rows;
	}

	public String getOrder() {
		if ("asc".equals(order.trim().toLowerCase(Locale.getDefault()))) {
			return "asc";
		} else {
			return "desc";
		}
	}

	public void setOrder(String order) {
		this.order = order;
	}

	public String getSort() {
		String tmp = sort.trim();
		tmp = tmp.replace("'", "").replace("/*", "").replace("-", "")
				.replace(";", "").replace("/", "").replace("*", "")
				.replace("&", "").replace("<", "").replace(">", "");
		return tmp;
	}

	public void setSort(String sort) {
		this.sort = sort;
	}

	// / <summary>
	// / 前端缓存总数
	// / </summary>
	public int getClientPageCount() {

		int count = 0;
		QueryParams paramas = null;
		for (QueryParams p : getQuerys()) {
			if (p.getName().equals("PageCount")) {
				paramas = p;
				break;
			}
		}

		if (paramas != null) {
			count = Integer.parseInt(paramas.getValue().toString());
		}
		if (StringUtils.isEmpty(getQueryKey())) {
			// 如果查询条件为空，可能是第一次查询
			// fix easyui-grid 第一次加载默认为10的bug
			// $('#grid').datagrid('getPager').data('pagination').options.total;
			count = 0;
		}
		if (!getClientQueryKey().equals(getQueryKey())) {
			// 如果查询条件有变化，PageCount = 0，需要重新查询记录总数
			count = 0;
		}

		return count;
	}

	// / <summary>
	// / 前端缓存查询条件特征码
	// / </summary>
	private String getClientQueryKey() {
		String queryKey = "";
		QueryParams paramas = null;
		for (QueryParams p : getQuerys()) {
			if (p.getName().equals("QueryKey")) {
				paramas = p;
				break;
			}
		}
		if (paramas != null) {
			queryKey = paramas.getValue().toString();
		}
		return queryKey;
	}

	// / <summary>
	// / 查询条件 JSON 字符串
	// / </summary>
	public String query;

	public String getQuery() {
		return query;
	}

	public void setQuery(String query) {
		this.query = query;
	}

	private List<QueryParams> querys;

	// / <summary>
	// / 解析查询条件 JSON 字符串为对象
	// / </summary>
	public List<com.sten.framework.common.QueryParams> getQuerys() {
		if (!StringUtils.isEmpty(getQuery())) {
			querys = toQuery(getQuery());
		}
		return querys;

	}

	public void setQuerys(List<QueryParams> value) {
		querys = value;
	}

	public String getValue(String key) {
		String result = "";
		for (QueryParams param : getQuerys()) {
			if (key.equals(param.getName())) {
				if (param.getValue() != null) {
					result = param.getValue().toString();
					break;
				}
			}
		}
		return result;
	}

	private String queryKey = "";

	// / <summary>
	// / 查询条件特征码
	// / </summary>
	public String getQueryKey() {

		if (StringUtils.isEmpty(queryKey)) {
			for (QueryParams q : getQuerys()) {
				if ("QueryKey".equals(q.getName())
						|| "PageCount".equals(q.getName()))
					continue;

				if (!StringUtils.isEmpty((q.getValue()))) {
					queryKey += q.getName() + q.getValue();
				}
			}

			if (!StringUtils.isEmpty(queryKey)) {
				// 避免字符串太短，Base64失败
				queryKey = toBase64("QueryKey" + queryKey);
				String key = "";
				for (int i = 0; i < queryKey.length(); i = i + 2) {
					key += queryKey.charAt(i);
				}
				queryKey = key.toLowerCase();
			}
		}
		return queryKey;
	}

	public Pagination() {
		setQuerys(new ArrayList<QueryParams>());
	}

	private List<QueryParams> toQuery(String jsonString) {
		// jsonString =
		// [{"name":"Username","value":""},{"name":"LoginName","value":""},{"name":"Locked","value":""},{"name":"PageCount","value":100},{"name":"QueryKey"}]

		ObjectMapper mapper = new ObjectMapper();
		List<Map<String, String>> jsonList = null;
		try {
			jsonList = mapper.readValue(jsonString, List.class);
		} catch (IOException e) {
			e.printStackTrace();
		}

		List<QueryParams> queryParams = new ArrayList<com.sten.framework.common.QueryParams>();
		if (jsonList != null) {
			for (Map<String, String> json : jsonList) {
				// name + value = 2
				if (json.size() != 2)
					continue;
				QueryParams p = new QueryParams();
				p.setName(json.get("name"));
				p.setValue(json.get("value"));
				queryParams.add(p);
			}
		}

		return queryParams;
	}

	private String toBase64(String value) {
		String result = "";
		try {
			result = Base64.encodeBase64String(value.getBytes("UTF-8"));
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		return result;
	}
}
