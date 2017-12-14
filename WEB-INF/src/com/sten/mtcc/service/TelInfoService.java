package com.sten.mtcc.service;

import java.math.BigDecimal;
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
import com.sten.mtcc.model.BiTelInfo;

@Service("telInfoService")
public class TelInfoService {
	private static final Logger logger = LoggerFactory
			.getLogger(TelInfoService.class);

	@Autowired
	private BaseService baseService;

	public BiTelInfo getTelInfoById(String id) {
		BiTelInfo telInfo = new BiTelInfo();
		try {
			telInfo = (BiTelInfo) BaseService.get(BiTelInfo.class, id);
		} catch (Exception e) {
			e.printStackTrace();
			logger.error(e.toString());
		}
		return telInfo;
	}

	public BiTelInfo getTelInfoByTelNumber(String tel_number) {
		BiTelInfo telInfo = null;
		Session session = null;
		try {
			session = HibernateUtil.getSession();
			String hql = "from BiTelInfo where tel_number=:tel_number";
			Query query = session.createQuery(hql);
			query.setString("tel_number", tel_number);

			List<BiTelInfo> list = query.list();
			if (list.size() > 0) {
				telInfo = list.get(0);
			}
		} catch (Exception ex) {
			ex.printStackTrace();
		} finally {
			HibernateUtil.closeSession(session);
		}
		return telInfo;
	}

	public Map<String, Object> loadByPage(Pagination pagination) {
		Map<String, Object> json = new Hashtable<String, Object>(3);
		List<BiTelInfo> list_project = new ArrayList<BiTelInfo>();
		int totalCount = 0;
		Session session = null;
		SessionUtil user = SessionUtil.getInstance();
		try {
			session = HibernateUtil.getSession();
			String hql_count = "SELECT count(*) FROM bi_tel_info T1 WHERE 1=1 [WHERE]";
			String hql_select = " SELECT t1.tel_number,t1.dept_uuid, t1.dept_name,t1.valid_start_date,t1.basic_cost, "
					+ " t1.out_local_cost, t1.out_local_cycle, t1.out_local_min_cycle, "
					+ " t1.out_long_cost, t1.out_long_cycle, t1.out_long_min_cycle, "
					+ " (select gbcb_name from bi_code_bsce where gbct_code= '002' and gbcb_id = t1.telec_operator_type) telec_operator_type, t1.isvalid, t1.uuid, t1.together_flag "
					+ " FROM bi_tel_info t1 WHERE 1=1 [WHERE] ";
			String where = "";

			// 拼接 sql where 条件
			for (QueryParams query : pagination.getQuerys()) {
				if (query.getName().equals("search_dept")
						&& !StringUtil.isEmpty(query.getValue().toString())) {
					where += " and t1.dept_name like :search_dept ";
				}
				if (query.getName().equals("search_tel")
						&& !StringUtil.isEmpty(query.getValue().toString())) {
					where += " and t1.tel_number like :search_tel ";
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
			hql_select = hql_select + "order by t1.tel_number ";

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
				if (query.getName().equals("search_tel")
						&& !StringUtil.isEmpty(query.getValue().toString())) {
					queryCount.setString("search_tel",
							"%" + (String) query.getValue() + "%");
					querySelect.setString("search_tel",
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
						BiTelInfo telInfo = new BiTelInfo();
						telInfo.setTel_number(obj[0].toString());
						telInfo.setDept_uuid(obj[1] == null ? "" : obj[1]
								.toString());
						telInfo.setDept_name(obj[2] == null ? "" : obj[2]
								.toString());
						telInfo.setValid_start_date(obj[3] == null ? ""
								: obj[3].toString());
						telInfo.setBasic_cost(obj[4] == null ? new BigDecimal(0)
								: new BigDecimal(obj[4].toString()));
						telInfo.setOut_local_cost(obj[5] == null ? new BigDecimal(
								0) : new BigDecimal(obj[5].toString()));
						telInfo.setOut_local_cycle(obj[6] == null ? 0
								: new Integer(obj[6].toString()));
						telInfo.setOut_local_min_cycle(obj[7] == null ? 0
								: new Integer(obj[7].toString()));
						telInfo.setOut_long_cost(obj[8] == null ? new BigDecimal(
								0) : new BigDecimal(obj[8].toString()));
						telInfo.setOut_long_cycle(obj[9] == null ? 0
								: new Integer(obj[9].toString()));
						telInfo.setOut_long_min_cycle(obj[10] == null ? 0
								: new Integer(obj[10].toString()));
						telInfo.setTelec_operator_type(obj[11] == null ? ""
								: obj[11].toString());
						telInfo.setIs_valid(obj[12] == null ? "" : obj[12]
								.toString());
						telInfo.setUuid(obj[13] == null ? "" : obj[13]
								.toString());
						telInfo.setTogether_flag(obj[14] == null ? "" : obj[14]
								.toString());

						list_project.add(telInfo);
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

	public boolean saveOrUpdateModel(BiTelInfo model) {
		boolean flg = true;
		SessionUtil user = SessionUtil.getInstance();
		try {
			if (StringUtil.isNotEmpty(model.getUuid())) {
				BiTelInfo telInfo = getTelInfoById(model.getUuid());
				telInfo.setTel_number(model.getTel_number());
				telInfo.setDept_uuid(model.getDept_uuid());
				telInfo.setDept_name(model.getDept_name());
				telInfo.setValid_start_date(model.getValid_start_date());
				telInfo.setBasic_cost(model.getBasic_cost());

				telInfo.setOut_local_cost(model.getOut_local_cost());
				telInfo.setOut_local_cycle(model.getOut_local_cycle());
				telInfo.setOut_local_min_cycle(model.getOut_local_min_cycle());

				telInfo.setOut_long_cost(model.getOut_long_cost());
				telInfo.setOut_long_cycle(model.getOut_long_cycle());
				telInfo.setOut_long_min_cycle(model.getOut_long_min_cycle());

				telInfo.setTogether_flag(model.getTogether_flag());
				telInfo.setTelec_operator_type(model.getTelec_operator_type());

				telInfo.setRemark(model.getRemark());
				telInfo.setUpdatetime(DateTimeUtil.getCurTimestamp());
				telInfo.setUpdateuser(user.getUsername());
				telInfo.setUpdateuserid(user.getUserID());
				BaseService.update(telInfo);
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

	public boolean deleteTelInfoById(String id) {
		Session session = HibernateUtil.getSession();
		Transaction ts = session.beginTransaction();
		boolean result = false;
		try {
			Query query = session
					.createQuery("delete from BiTelInfo where uuid =:uuid ");
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

	public boolean isTelNumberExist(String tel_number, String uuid) {
		String hql = "";
		HashMap<String, Object> map = new HashMap<String, Object>();
		if ("".equals(uuid)) {
			hql = "from BiTelInfo where tel_number = :tel_number";
		} else {
			hql = "from BiTelInfo where tel_number = :tel_number and uuid <> :uuid";
			map.put("uuid", uuid);
		}

		map.put("tel_number", tel_number);
		if (BaseService.queryOne(hql, map) != null) {
			return true;
		}
		return false;
	}
}
