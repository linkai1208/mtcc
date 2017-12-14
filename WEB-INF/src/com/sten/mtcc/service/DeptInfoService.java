package com.sten.mtcc.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;

import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.sten.framework.common.Constants;
import com.sten.framework.common.Pagination;
import com.sten.framework.common.QueryParams;
import com.sten.framework.service.BaseService;
import com.sten.framework.util.DateTimeUtil;
import com.sten.framework.util.HibernateUtil;
import com.sten.framework.util.SessionUtil;
import com.sten.framework.util.StringUtil;
import com.sten.mtcc.model.BiDeptInfo;

@Service("deptInfoService")
public class DeptInfoService {
	private static final Logger logger = LoggerFactory
			.getLogger(DeptInfoService.class);

	@Autowired
	private BaseService baseService;

	public BiDeptInfo getDeptInfoById(String id) {
		BiDeptInfo deptInfo = new BiDeptInfo();
		try {
			deptInfo = (BiDeptInfo) BaseService.get(BiDeptInfo.class, id);
		} catch (Exception e) {
			e.printStackTrace();
			logger.error(e.toString());
		}
		return deptInfo;
	}

	public Map<String, Object> loadByPage(Pagination pagination) {
		Map<String, Object> json = new Hashtable<String, Object>(3);
		List<BiDeptInfo> list_project = new ArrayList<BiDeptInfo>();
		int totalCount = 0;
		Session session = null;
		SessionUtil user = SessionUtil.getInstance();
		try {
			session = HibernateUtil.getSession();
			String hql_count = "SELECT count(*) FROM bi_dept_info T1 WHERE 1=1 [WHERE]";
			String hql_select = " SELECT t1.uuid,t1.dept_code, t1.dept_name,t1.dept_address,t1.remark, t1.isvalid, t1.order_index "
					+ " FROM bi_dept_info t1 WHERE 1=1 [WHERE] ";
			String where = "";

			// 拼接 sql where 条件
			for (QueryParams query : pagination.getQuerys()) {
				if (query.getName().equals("search_dept")
						&& !StringUtil.isEmpty(query.getValue().toString())) {
					where += " and t1.dept_name like :search_dept ";
				}
			}

			if (where.length() > 0) {
				hql_count = hql_count.replace("[WHERE]", where);
				hql_select = hql_select.replace("[WHERE]", where);
			} else {
				hql_count = hql_count.replace("[WHERE]", "");
				hql_select = hql_select.replace("[WHERE]", "");
			}

			// 拼接 sql 排序
			hql_select = hql_select + "order by t1.dept_name ";

			Query queryCount = session.createSQLQuery(hql_count);
			Query querySelect = session.createSQLQuery(hql_select);

			// 为 where 条件中的参数赋值
			for (QueryParams query : pagination.getQuerys()) {
				if (query.getName().equals("search_dept")
						&& !StringUtil.isEmpty(query.getValue().toString())) {
					queryCount.setString("search_dept",
							"%" + (String) query.getValue() + "%");
					querySelect.setString("search_dept",
							"%" + (String) query.getValue() + "%");
				}

			}

			// 计算总页数
			totalCount = pagination.getClientPageCount();
			// 避免每次都查询总页数
			if (totalCount == 0) {
				totalCount = ((Number) queryCount.list().get(0)).intValue();
			}
			if (totalCount > 0) {
				// 获取分页处理
				Integer rows_page = pagination.getRows() == 0 ? Constants.PAGE_ROW_COUNT
						: pagination.getRows();
				querySelect.setFirstResult((pagination.getPage() - 1)
						* rows_page);
				querySelect.setMaxResults(rows_page);
				List list = querySelect.list();
				if (list != null && list.size() > 0) {
					// 返回的是二维数组, 需要转换为对象
					for (int i = 0; i < list.size(); i++) {
						Object[] obj = (Object[]) list.get(i);
						BiDeptInfo deptInfo = new BiDeptInfo();
						deptInfo.setUuid(obj[0].toString());
						deptInfo.setDept_code(obj[1] == null ? "" : obj[1]
								.toString());
						deptInfo.setDept_name(obj[2] == null ? "" : obj[2]
								.toString());
						deptInfo.setDept_address(obj[3] == null ? "" : obj[3]
								.toString());
						deptInfo.setRemark(obj[4] == null ? "" : obj[4]
								.toString());
						deptInfo.setIs_valid(obj[5] == null ? "" : obj[5]
								.toString());
						deptInfo.setOrder_index(obj[6] == null ? 0
								: new Integer(obj[6].toString()).intValue());
						list_project.add(deptInfo);
					}
				}
			}

		} catch (Exception ex) {
			ex.printStackTrace();
		} finally {
			HibernateUtil.closeSession(session);
		}

		json.put("total", totalCount);
		json.put("rows", list_project);
		json.put("QueryKey", pagination.getQueryKey());

		return json;
	}

	public boolean saveOrUpdateModel(BiDeptInfo model) {
		boolean flg = true;
		SessionUtil user = SessionUtil.getInstance();
		try {
			if (StringUtil.isNotEmpty(model.getUuid())) {
				BiDeptInfo deptInfo = getDeptInfoById(model.getUuid());
				deptInfo.setDept_code(model.getDept_code());
				deptInfo.setDept_name(model.getDept_name());
				deptInfo.setDept_address(model.getDept_address());
				deptInfo.setOrder_index(model.getOrder_index());

				deptInfo.setRemark(model.getRemark());
				deptInfo.setUpdatetime(DateTimeUtil.getCurTimestamp());
				deptInfo.setUpdateuser(user.getUsername());
				deptInfo.setUpdateuserid(user.getUserID());
				BaseService.update(deptInfo);
			} else {
				model.setIs_valid("1");
				model.setCreatetime(DateTimeUtil.getCurTimestamp());
				model.setCreateuser(user.getUsername());
				model.setCreateuserid(user.getUserID());
				model.setUpdatetime(DateTimeUtil.getCurTimestamp());
				model.setUpdateuser(user.getUsername());
				model.setUpdateuserid(user.getUserID());

				BaseService.add(model);
			}
		} catch (Exception ex) {
			flg = false;
			ex.printStackTrace();
			logger.error(ex.toString());
		}
		return flg;
	}

	public boolean deleteDeptInfoById(String id) {
		Session session = HibernateUtil.getSession();
		Transaction ts = session.beginTransaction();
		boolean result = false;
		try {
			Query query = session
					.createQuery("delete from BiDeptInfo where uuid =:uuid ");
			query.setString("uuid", id);

			query.executeUpdate();

			result = true;
			ts.commit();
		} catch (Exception ex) {
			logger.error(ex.toString());
			if (ts != null) {
				ts.rollback();
			}
			ex.printStackTrace();
		} finally {
			HibernateUtil.closeSession(session);
		}
		return result;
	}

	public static List<BiDeptInfo> getBiDeptInfoForCheck() {
		List<BiDeptInfo> list = new ArrayList<BiDeptInfo>();
		Session session = null;
		try {
			session = HibernateUtil.getSession();
			// 注意表名为对象名
			String hql = "from BiDeptInfo where isvalid ='1' order by order_index, dept_name ";
			Query query = session.createQuery(hql);
			// query.setString("OS_TYPE_CODE", "%"+os_type_code+"%");
			list = query.list();
		} catch (Exception ex) {
			ex.printStackTrace();
		} finally {
			HibernateUtil.closeSession(session);
		}
		return list;
	}

	public boolean isDeptNameExist(String dept_name, String uuid) {
		String hql = "";
		HashMap<String, Object> map = new HashMap<String, Object>();
		if ("".equals(uuid)) {
			hql = "from BiDeptInfo where dept_name = :dept_name";
		} else {
			hql = "from BiDeptInfo where dept_name = :dept_name and uuid <> :uuid";
			map.put("uuid", uuid);
		}

		map.put("dept_name", dept_name);
		if (BaseService.queryOne(hql, map) != null) {
			return true;
		}
		return false;
	}

	public static BiDeptInfo getBiDeptInfoByName(String dept_name) {
		BiDeptInfo info = null;
		Session session = null;
		try {
			session = HibernateUtil.getSession();
			// 注意表名为对象名
			String hql = "from BiDeptInfo where dept_name =:dept_name ";
			Query query = session.createQuery(hql);
			query.setString("dept_name", dept_name);
			ArrayList list = (ArrayList) query.list();
			if (list != null && list.size() > 0) {
				info = (BiDeptInfo) list.get(0);
			}
		} catch (Exception ex) {
			ex.printStackTrace();
		} finally {
			HibernateUtil.closeSession(session);
		}
		return info;
	}

}
