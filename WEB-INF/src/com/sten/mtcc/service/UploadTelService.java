package com.sten.mtcc.service;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.sql.Timestamp;
import java.util.ArrayList;
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
import com.sten.mtcc.model.BzTelCostCompare;
import com.sten.mtcc.model.BzTelCostDetail;
import com.sten.mtcc.model.BzTelCostIndex;
import com.sten.mtcc.model.BzTelCostList;

@Service("uploadTelService")
public class UploadTelService {
	private static final Logger logger = LoggerFactory
			.getLogger(UploadTelService.class);

	@Autowired
	private BaseService baseService;
	@Autowired
	private TelInfoService telInfoService;

	public BzTelCostIndex getTelCostIndexById(String id) {
		BzTelCostIndex telCostIndex = new BzTelCostIndex();
		try {
			telCostIndex = (BzTelCostIndex) BaseService.get(
					BzTelCostIndex.class, id);
		} catch (Exception e) {
			e.printStackTrace();
			logger.error(e.toString());
		}
		return telCostIndex;
	}

	public BzTelCostCompare getTelCostCompareById(String id) {
		BzTelCostCompare telCostCompare = new BzTelCostCompare();
		try {
			telCostCompare = (BzTelCostCompare) BaseService.get(
					BzTelCostCompare.class, id);
		} catch (Exception e) {
			e.printStackTrace();
			logger.error(e.toString());
		}
		return telCostCompare;
	}

	public Map<String, Object> loadByPage(Pagination pagination) {
		Map<String, Object> json = new Hashtable<String, Object>(3);
		List<BzTelCostIndex> list_project = new ArrayList<BzTelCostIndex>();
		int totalCount = 0;
		Session session = null;
		SessionUtil user = SessionUtil.getInstance();
		try {
			session = HibernateUtil.getSession();
			String hql_count = "SELECT count(*) FROM bz_tel_cost_index T1 WHERE 1=1 [WHERE]";
			String hql_select = " SELECT t1.period_year,t1.period_month,"
					+ " DATE_FORMAT(t1.list_file_date, '%Y-%m-%d %T') list_file_date_dis, DATE_FORMAT(t1.detail_file_date, '%Y-%m-%d %T') detail_file_date_dis,  "
					+ " t1.compare_status, t1.uuid, "
					+ " DATE_FORMAT(t1.compare_date_begin, '%Y-%m-%d %T') compare_date_begin_dis, "
					+ " DATE_FORMAT(t1.compare_date_end, '%Y-%m-%d %T') compare_date_end_dis, "
					+ " DATE_FORMAT(t1.pre_compare_date_begin, '%Y-%m-%d %T') pre_compare_date_begin_dis, "
					+ " DATE_FORMAT(t1.pre_compare_date_end, '%Y-%m-%d %T') pre_compare_date_end_dis, "
					+ " t1.pre_compare_status, t1.compare_result, t1.list_total_costs, t1.detail_total_costs, t1.total_costs_out, "
					+ " t1.list_tel_shortage, t1.detail_tel_shortage "
					+ " FROM bz_tel_cost_index t1 WHERE 1=1 [WHERE] ";
			String where = "";

			// 拼接 sql where 条件
			for (QueryParams query : pagination.getQuerys()) {
				if (query.getName().equals("search_year")
						&& !StringUtil.isEmpty(query.getValue().toString())) {
					where += " and t1.period_year = :period_year ";
				}
				if (query.getName().equals("search_month")
						&& !StringUtil.isEmpty(query.getValue().toString())) {
					where += " and t1.period_month = :period_month ";
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
			hql_select = hql_select
					+ "order by t1.period_year, t1.period_month ";

			Query queryCount = session.createSQLQuery(hql_count);
			Query querySelect = session.createSQLQuery(hql_select);

			// 为 where 条件中的参数赋值
			for (QueryParams query : pagination.getQuerys()) {
				if (query.getName().equals("search_year")
						&& !StringUtil.isEmpty(query.getValue().toString())) {
					queryCount.setString("period_year",
							(String) query.getValue());
					querySelect.setString("period_year",
							(String) query.getValue());
				}
				if (query.getName().equals("search_month")
						&& !StringUtil.isEmpty(query.getValue().toString())) {
					String month = (String) query.getValue();
					if (new Integer(month).intValue() < 10) {
						month = "0"
								+ new Integer(new Integer(month).intValue())
										.toString();
					}
					queryCount.setString("period_month", month);
					querySelect.setString("period_month", month);
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
						BzTelCostIndex costIndex = new BzTelCostIndex();
						costIndex.setPeriod_year(obj[0].toString());
						costIndex.setPeriod_month(obj[1] == null ? "" : obj[1]
								.toString());
						costIndex.setList_file_date_dis(obj[2] == null ? ""
								: obj[2].toString());
						costIndex.setDetail_file_date_dis(obj[3] == null ? ""
								: obj[3].toString());
						costIndex.setCompare_status(obj[4] == null ? ""
								: obj[4].toString());
						costIndex.setUuid(obj[5] == null ? "" : obj[5]
								.toString());
						costIndex.setCompare_date_begin_dis(obj[6] == null ? ""
								: obj[6].toString());
						costIndex.setCompare_date_end_dis(obj[7] == null ? ""
								: obj[7].toString());
						costIndex
								.setPre_compare_date_begin_dis(obj[8] == null ? ""
										: obj[8].toString());
						costIndex
								.setPre_compare_date_end_dis(obj[9] == null ? ""
										: obj[9].toString());
						costIndex.setPre_compare_status(obj[10] == null ? ""
								: obj[10].toString());
						costIndex.setCompare_result(obj[11] == null ? ""
								: obj[11].toString());
						costIndex
								.setList_total_costs(obj[12] == null ? new BigDecimal(
										0) : new BigDecimal(obj[12].toString()));
						costIndex
								.setDetail_total_costs(obj[13] == null ? new BigDecimal(
										0) : new BigDecimal(obj[13].toString()));
						costIndex
								.setTotal_costs_out(obj[14] == null ? new BigDecimal(
										0) : new BigDecimal(obj[14].toString()));
						costIndex.setList_tel_shortage(obj[15] == null ? 0
								: new Integer(obj[15].toString()).intValue());
						costIndex.setDetail_tel_shortage(obj[16] == null ? 0
								: new Integer(obj[16].toString()).intValue());

						list_project.add(costIndex);
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

	public BzTelCostIndex getCostIndexByYearMonth(String period_year,
			String period_month) {
		Session session = null;
		BzTelCostIndex model = null;
		try {
			session = HibernateUtil.getSession();
			String hql = "from BzTelCostIndex where period_year=:period_year and period_month=:period_month ";
			Query query = session.createQuery(hql);
			query.setString("period_year", period_year);
			query.setString("period_month", period_month);

			List<BzTelCostIndex> list = query.list();
			if (list.size() > 0) {
				model = list.get(0);
			}

		} catch (Exception ex) {
			ex.printStackTrace();
		} finally {
			HibernateUtil.closeSession(session);
		}
		return model;
	}

	public boolean saveOrUpdateCostIndex(BzTelCostIndex model) {
		boolean flg = true;
		SessionUtil user = SessionUtil.getInstance();
		try {
			if (StringUtil.isNotEmpty(model.getUuid())) {
				model.setUpdatetime(DateTimeUtil.getCurTimestamp());
				model.setUpdateuser(user.getUsername());
				model.setUpdateuserid(user.getUserID());
				BaseService.update(model);
			} else {
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

	public boolean saveOrUpdateCostList(BzTelCostList model) {
		boolean flg = true;
		SessionUtil user = SessionUtil.getInstance();
		try {
			if (StringUtil.isNotEmpty(model.getUuid())) {
				model.setUpdatetime(DateTimeUtil.getCurTimestamp());
				model.setUpdateuser(user.getUsername());
				model.setUpdateuserid(user.getUserID());
				BaseService.update(model);
			} else {
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

	public boolean saveOrUpdateCostDetail(BzTelCostDetail model) {
		boolean flg = true;
		SessionUtil user = SessionUtil.getInstance();
		try {
			if (StringUtil.isNotEmpty(model.getUuid())) {
				model.setUpdatetime(DateTimeUtil.getCurTimestamp());
				model.setUpdateuser(user.getUsername());
				model.setUpdateuserid(user.getUserID());
				BaseService.update(model);
			} else {
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

	public boolean saveOrUpdateCostCompare(BzTelCostCompare model) {
		boolean flg = true;
		SessionUtil user = SessionUtil.getInstance();
		try {
			if (StringUtil.isNotEmpty(model.getUuid())) {
				model.setUpdatetime(DateTimeUtil.getCurTimestamp());
				model.setUpdateuser(user.getUsername());
				model.setUpdateuserid(user.getUserID());
				BaseService.update(model);
			} else {
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

	public boolean deleteCostListByYearMonth(String period_year,
			String period_month) {
		Session session = HibernateUtil.getSession();
		Transaction ts = session.beginTransaction();
		boolean result = false;
		try {
			Query query = session
					.createQuery("delete from BzTelCostList where period_year =:period_year and period_month = :period_month ");
			query.setString("period_year", period_year);
			query.setString("period_month", period_month);

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

	/**
	 * 预处理
	 * 
	 * @param period_year
	 * @param period_month
	 * @return
	 */
	public boolean initial(String indexUuid, String period_year,
			String period_month) {
		Session session = null;
		try {
			session = HibernateUtil.getSession();
			Timestamp pre_compare_date_begin = DateTimeUtil.getCurTimestamp();
			// 详单预处理
			String hql = "select uuid, tel_number, call_duration,long_distance_type, long_distance_type_name from bz_tel_cost_detail where period_year=:period_year and period_month= :period_month ";
			Query query = session.createSQLQuery(hql);
			query.setString("period_year", period_year);
			query.setString("period_month", period_month);

			List list = query.list();
			if (list != null && list.size() > 0) {
				for (int i = 0; i < list.size(); i++) {
					Object[] obj = (Object[]) list.get(i);
					String uuid = obj[0] == null ? "" : obj[0].toString();
					String tel_number = obj[1] == null ? "" : obj[1].toString();
					// if (!tel_number.equals("1053953604")) continue;
					BigDecimal call_duration = obj[2] == null ? new BigDecimal(
							0) : new BigDecimal(obj[2].toString());
					String long_distance_type_name = obj[4] == null ? ""
							: obj[4].toString();
					String long_distance_type = "1";
					if (long_distance_type_name.equals("统一Centrex区内主叫通话费")) {
						long_distance_type = "1";
					} else if (long_distance_type_name
							.equals("统一Centrex普通省际长途费")) {
						long_distance_type = "2";
					}
					BiTelInfo telInfo = telInfoService
							.getTelInfoByTelNumber(tel_number);
					if (telInfo != null) {
						String together_flag = telInfo.getTogether_flag();
						BigDecimal localMoney = new BigDecimal(0);
						BigDecimal longMoney = new BigDecimal(0);
						BigDecimal localMoneyXs = new BigDecimal(0);
						BigDecimal longMoneyXs = new BigDecimal(0);
						if (together_flag.equals("0")) {
							if (long_distance_type.equals("1")) {
								BigDecimal out_local_cost = telInfo
										.getOut_local_cost();
								BigDecimal out_local_cycle = new BigDecimal(
										telInfo.getOut_local_cycle());

								BigDecimal out_local_min_cycle = new BigDecimal(
										telInfo.getOut_local_min_cycle());
								BigDecimal out_local_min_cost = out_local_cost
										.divide(out_local_cycle
												.divide(out_local_min_cycle));
								BigDecimal money1 = call_duration.divide(
										out_local_cycle, 0,
										BigDecimal.ROUND_DOWN).multiply(
										out_local_cost);
								BigDecimal midValue = call_duration.divide(
										out_local_cycle, 0,
										BigDecimal.ROUND_DOWN).multiply(
										out_local_cycle);
								BigDecimal money2 = call_duration
										.subtract(midValue)
										.divide(out_local_min_cycle, 0,
												BigDecimal.ROUND_UP)
										.multiply(out_local_min_cost);
								localMoney = money1.add(money2);
								// 销售
								BigDecimal out_local_min_cycle_xs = new BigDecimal(
										BiCodeBsceService
												.getBiCodeBsceNameByKey("008",
														"1"));
								BigDecimal out_local_min_cost_xs = out_local_cost
										.divide(out_local_cycle
												.divide(out_local_min_cycle_xs));
								BigDecimal money1_xs = call_duration.divide(
										out_local_cycle, 0,
										BigDecimal.ROUND_DOWN).multiply(
										out_local_cost);
								BigDecimal midValue_xs = call_duration.divide(
										out_local_cycle, 0,
										BigDecimal.ROUND_DOWN).multiply(
										out_local_cycle);
								BigDecimal money2_xs = call_duration
										.subtract(midValue_xs)
										.divide(out_local_min_cycle_xs, 0,
												BigDecimal.ROUND_UP)
										.multiply(out_local_min_cost_xs);
								localMoneyXs = money1_xs.add(money2_xs);

							} else if (long_distance_type.equals("2")) {
								BigDecimal out_long_cost = telInfo
										.getOut_long_cost();
								BigDecimal out_long_cycle = new BigDecimal(
										telInfo.getOut_long_cycle());

								BigDecimal out_long_min_cycle = new BigDecimal(
										telInfo.getOut_long_min_cycle());
								BigDecimal out_long_min_cost = out_long_cost
										.divide(out_long_cycle
												.divide(out_long_min_cycle));
								BigDecimal money1 = call_duration.divide(
										out_long_cycle, 0,
										BigDecimal.ROUND_DOWN).multiply(
										out_long_cost);
								BigDecimal midValue = call_duration.divide(
										out_long_cycle, 0,
										BigDecimal.ROUND_DOWN).multiply(
										out_long_cycle);
								BigDecimal money2 = call_duration
										.subtract(midValue)
										.divide(out_long_min_cycle, 0,
												BigDecimal.ROUND_UP)
										.multiply(out_long_min_cost);
								longMoney = money1.add(money2);
								// 销售
								BigDecimal out_long_min_cycle_xs = new BigDecimal(
										BiCodeBsceService
												.getBiCodeBsceNameByKey("008",
														"1"));
								BigDecimal out_long_min_cost_xs = out_long_cost
										.divide(out_long_cycle
												.divide(out_long_min_cycle_xs));
								BigDecimal money1_xs = call_duration.divide(
										out_long_cycle, 0,
										BigDecimal.ROUND_DOWN).multiply(
										out_long_cost);
								BigDecimal midValue_xs = call_duration.divide(
										out_long_cycle, 0,
										BigDecimal.ROUND_DOWN).multiply(
										out_long_cycle);
								BigDecimal money2_xs = call_duration
										.subtract(midValue_xs)
										.divide(out_long_min_cycle_xs, 0,
												BigDecimal.ROUND_UP)
										.multiply(out_long_min_cost_xs);
								longMoneyXs = money1_xs.add(money2_xs);
							}

						} else {
							if (long_distance_type.equals("1")) {
								BigDecimal out_local_cost = telInfo
										.getOut_local_cost();
								BigDecimal out_local_cycle = new BigDecimal(
										telInfo.getOut_local_cycle());
								BigDecimal out_local_min_cycle = new BigDecimal(
										telInfo.getOut_local_min_cycle());
								BigDecimal out_local_min_cost = out_local_cost
										.divide(out_local_cycle
												.divide(out_local_min_cycle));
								BigDecimal money1 = call_duration.divide(
										out_local_cycle, 0,
										BigDecimal.ROUND_DOWN).multiply(
										out_local_cost);
								BigDecimal midValue = call_duration.divide(
										out_local_cycle, 0,
										BigDecimal.ROUND_DOWN).multiply(
										out_local_cycle);
								BigDecimal money2 = call_duration
										.subtract(midValue)
										.divide(out_local_min_cycle, 0,
												BigDecimal.ROUND_UP)
										.multiply(out_local_min_cost);
								localMoney = money1.add(money2);
								// 销售
								BigDecimal out_local_min_cycle_xs = new BigDecimal(
										BiCodeBsceService
												.getBiCodeBsceNameByKey("008",
														"1"));
								BigDecimal out_local_min_cost_xs = out_local_cost
										.divide(out_local_cycle
												.divide(out_local_min_cycle_xs));
								BigDecimal money1_xs = call_duration.divide(
										out_local_cycle, 0,
										BigDecimal.ROUND_DOWN).multiply(
										out_local_cost);
								BigDecimal midValue_xs = call_duration.divide(
										out_local_cycle, 0,
										BigDecimal.ROUND_DOWN).multiply(
										out_local_cycle);
								BigDecimal money2_xs = call_duration
										.subtract(midValue_xs)
										.divide(out_local_min_cycle_xs, 0,
												BigDecimal.ROUND_UP)
										.multiply(out_local_min_cost_xs);
								localMoneyXs = money1_xs.add(money2_xs);
							} else if (long_distance_type.equals("2")) {
								BigDecimal out_local_cost = telInfo
										.getOut_local_cost();
								BigDecimal out_local_cycle = new BigDecimal(
										telInfo.getOut_local_cycle());
								BigDecimal out_local_min_cycle = new BigDecimal(
										telInfo.getOut_local_min_cycle());
								BigDecimal out_local_min_cost = out_local_cost
										.divide(out_local_cycle
												.divide(out_local_min_cycle));

								BigDecimal money1 = call_duration.divide(
										out_local_cycle, 0,
										BigDecimal.ROUND_DOWN).multiply(
										out_local_cost);
								BigDecimal midValue = call_duration.divide(
										out_local_cycle, 0,
										BigDecimal.ROUND_DOWN).multiply(
										out_local_cycle);
								BigDecimal money2 = call_duration
										.subtract(midValue)
										.divide(out_local_min_cycle, 0,
												BigDecimal.ROUND_UP)
										.multiply(out_local_min_cost);
								longMoney = money1.add(money2);
								// 销售
								BigDecimal out_local_min_cycle_xs = new BigDecimal(
										BiCodeBsceService
												.getBiCodeBsceNameByKey("008",
														"1"));
								BigDecimal out_local_min_cost_xs = out_local_cost
										.divide(out_local_cycle
												.divide(out_local_min_cycle_xs));
								BigDecimal money1_xs = call_duration.divide(
										out_local_cycle, 0,
										BigDecimal.ROUND_DOWN).multiply(
										out_local_cost);
								BigDecimal midValue_xs = call_duration.divide(
										out_local_cycle, 0,
										BigDecimal.ROUND_DOWN).multiply(
										out_local_cycle);
								BigDecimal money2_xs = call_duration
										.subtract(midValue_xs)
										.divide(out_local_min_cycle_xs, 0,
												BigDecimal.ROUND_UP)
										.multiply(out_local_min_cost_xs);
								localMoneyXs = money1_xs.add(money2_xs);
							}
						}
						BzTelCostDetail model = getTelCostDetailById(uuid);
						model.setBasic_costs_comp(localMoney);
						model.setLong_costs_comp(longMoney);
						model.setBasic_costs_comp_out(localMoneyXs);
						model.setLong_costs_comp_out(longMoneyXs);
						saveOrUpdateCostDetail(model);
					} else {
						System.out.println("无效号码：" + tel_number);
					}
				}
			}
			Timestamp pre_compare_date_end = DateTimeUtil.getCurTimestamp();

			BzTelCostIndex telCostIndex = getTelCostIndexById(indexUuid);
			telCostIndex.setPre_compare_date_begin(pre_compare_date_begin);
			telCostIndex.setPre_compare_date_end(pre_compare_date_end);
			telCostIndex.setPre_compare_status("1");

			saveOrUpdateCostIndex(telCostIndex);

		} catch (Exception ex) {
			ex.printStackTrace();
		} finally {
			HibernateUtil.closeSession(session);
		}
		return true;
	}

	public boolean compare(String indexUuid, String period_year,
			String period_month) {
		Session session = null;

		try {
			session = HibernateUtil.getSession();
			Timestamp compare_date_begin = DateTimeUtil.getCurTimestamp();
			updateDetailCostOut(indexUuid, period_year, period_month);
			updateListCompare(indexUuid, period_year, period_month);

			// 汇总比对结果到索引表
			String hqlAll = "select sum(list_total_costs), sum(detail_total_costs), sum(total_costs_out) from bz_tel_cost_compare where period_year=:period_year and period_month= :period_month ";
			Query queryAll = session.createSQLQuery(hqlAll);
			queryAll.setString("period_year", period_year);
			queryAll.setString("period_month", period_month);
			List listAll = queryAll.list();
			Object[] obj = (Object[]) listAll.get(0);
			BigDecimal list_total_costs = obj[0] == null ? new BigDecimal(0)
					: new BigDecimal(obj[0].toString());
			BigDecimal detail_total_costs = obj[1] == null ? new BigDecimal(0)
					: new BigDecimal(obj[1].toString());
			BigDecimal total_costs_out = obj[2] == null ? new BigDecimal(0)
					: new BigDecimal(obj[2].toString());
			String compare_result = "";
			if (list_total_costs.compareTo(detail_total_costs) == 0) {
				compare_result = "0";
			} else if (list_total_costs.compareTo(detail_total_costs) > 0) {
				compare_result = "1";
			} else if (list_total_costs.compareTo(detail_total_costs) < 0) {
				compare_result = "2";
			}
			// 计算清单计费号码少的个数
			String hqlAllList = "select count(uuid) from bz_tel_cost_compare where period_year=:period_year and period_month= :period_month and compare_result = '4'";
			Query queryAllList = session.createSQLQuery(hqlAllList);
			queryAllList.setString("period_year", period_year);
			queryAllList.setString("period_month", period_month);
			List listAllList = queryAllList.list();
			BigInteger bi1 = (BigInteger) listAllList.get(0);
			int list_tel_shortage = bi1.intValue();
			// 计算详单计费号码少的个数
			String hqlAllDetail = "select count(uuid) from bz_tel_cost_compare where period_year=:period_year and period_month= :period_month and compare_result = '3'";
			Query queryAllDetail = session.createSQLQuery(hqlAllDetail);
			queryAllDetail.setString("period_year", period_year);
			queryAllDetail.setString("period_month", period_month);
			List listAllDetail = queryAllDetail.list();
			BigInteger bi2 = (BigInteger) listAllDetail.get(0);
			int detail_tel_shortage = bi2.intValue();

			Timestamp compare_date_end = DateTimeUtil.getCurTimestamp();

			BzTelCostIndex telCostIndex = getTelCostIndexById(indexUuid);
			telCostIndex.setCompare_date_begin(compare_date_begin);
			telCostIndex.setCompare_date_end(compare_date_end);
			telCostIndex.setList_total_costs(list_total_costs);
			telCostIndex.setDetail_total_costs(detail_total_costs);
			telCostIndex.setCompare_result(compare_result);
			telCostIndex.setTotal_costs_out(total_costs_out);
			telCostIndex.setCompare_status("1");
			telCostIndex.setList_tel_shortage(list_tel_shortage);
			telCostIndex.setDetail_tel_shortage(detail_tel_shortage);
			saveOrUpdateCostIndex(telCostIndex);

		} catch (Exception ex) {
			ex.printStackTrace();
		} finally {
			HibernateUtil.closeSession(session);
		}
		return true;
	}

	private boolean updateDetailCostOut(String indexUuid, String period_year,
			String period_month) {
		Session session = null;

		try {
			session = HibernateUtil.getSession();

			// 更新详单的成本合计和销售合计（按计费号码）
			String hqlGroup = "select tel_number, sum(basic_costs_comp) basic_costs_comp, sum(long_costs_comp) long_costs_comp, sum(basic_costs_comp_out) basic_costs_comp_out, sum(long_costs_comp_out) long_costs_comp_out from bz_tel_cost_detail where period_year=:period_year and period_month= :period_month group by tel_number ";
			Query queryGroup = session.createSQLQuery(hqlGroup);
			queryGroup.setString("period_year", period_year);
			queryGroup.setString("period_month", period_month);
			List listGroup = queryGroup.list();
			if (listGroup != null && listGroup.size() > 0) {
				for (int i = 0; i < listGroup.size(); i++) {
					Object[] obj = (Object[]) listGroup.get(i);
					String tel_number = obj[0] == null ? "" : obj[0].toString();
					// if (!tel_number.equals("1053953604")) continue;
					BiTelInfo telInfo = telInfoService
							.getTelInfoByTelNumber(tel_number);
					// 成本
					BigDecimal basic_costs_comp = obj[1] == null ? new BigDecimal(
							0) : new BigDecimal(obj[1].toString());
					BigDecimal long_costs_comp = obj[2] == null ? new BigDecimal(
							0) : new BigDecimal(obj[2].toString());
					BigDecimal detail_total_costs = basic_costs_comp
							.add(long_costs_comp);
					if (detail_total_costs.compareTo(telInfo.getBasic_cost()) < 0) {
						detail_total_costs = telInfo.getBasic_cost();
					}
					// 销售
					BigDecimal basic_costs_comp_out = obj[3] == null ? new BigDecimal(
							0) : new BigDecimal(obj[3].toString());
					BigDecimal long_costs_comp_out = obj[4] == null ? new BigDecimal(
							0) : new BigDecimal(obj[4].toString());
					BigDecimal total_costs_out = basic_costs_comp_out
							.add(long_costs_comp_out);
					if (total_costs_out.compareTo(telInfo.getBasic_cost()) < 0) {
						total_costs_out = telInfo.getBasic_cost();
					}

					BzTelCostCompare compareInfo = new BzTelCostCompare();
					compareInfo.setTel_number(tel_number);
					compareInfo.setPeriod_year(period_year);
					compareInfo.setPeriod_month(period_month);
					compareInfo.setDetail_total_costs(detail_total_costs);
					compareInfo.setTotal_costs_out(total_costs_out);
					compareInfo.setCompare_result("4"); // 默认是"无清单号码“，待清单更新时再修改此标志

					compareInfo.setTel_uuid(telInfo.getUuid());
					compareInfo.setValid_start_date(telInfo
							.getValid_start_date());
					compareInfo.setBasic_cost(telInfo.getBasic_cost());
					compareInfo.setOut_local_cost(telInfo.getOut_local_cost());
					compareInfo
							.setOut_local_cycle(telInfo.getOut_local_cycle());
					compareInfo.setOut_local_min_cycle(telInfo
							.getOut_local_min_cycle());
					compareInfo.setOut_long_cost(telInfo.getOut_long_cost());
					compareInfo.setOut_long_cycle(telInfo.getOut_long_cycle());
					compareInfo.setOut_long_min_cycle(telInfo
							.getOut_long_min_cycle());
					compareInfo.setTogether_flag(telInfo.getTogether_flag());
					compareInfo.setDept_uuid(telInfo.getDept_uuid());
					compareInfo.setDept_name(telInfo.getDept_name());
					compareInfo.setCost_attach_flag(telInfo
							.getCost_attach_flag());
					saveOrUpdateCostCompare(compareInfo);
				}
			}

		} catch (Exception ex) {
			ex.printStackTrace();
		} finally {
			HibernateUtil.closeSession(session);
		}
		return true;
	}

	public boolean updateListCompare(String indexUuid, String period_year,
			String period_month) {
		Session session = null;

		try {
			session = HibernateUtil.getSession();
			// 更新清单和比对结果
			String hqlList = "select tel_number, total_costs from bz_tel_cost_list where period_year=:period_year and period_month= :period_month ";
			Query queryList = session.createSQLQuery(hqlList);
			queryList.setString("period_year", period_year);
			queryList.setString("period_month", period_month);
			List listList = queryList.list();
			if (listList != null && listList.size() > 0) {
				for (int i = 0; i < listList.size(); i++) {
					Object[] obj = (Object[]) listList.get(i);
					String tel_number = obj[0] == null ? "" : obj[0].toString();
					// if (!tel_number.equals("1053953604")) continue;
					BigDecimal total_costs = obj[1] == null ? new BigDecimal(0)
							: new BigDecimal(obj[1].toString());
					String hqlComp = "select uuid, tel_number from bz_tel_cost_compare where period_year=:period_year and period_month= :period_month and tel_number=:tel_number";
					Query queryComp = session.createSQLQuery(hqlComp);
					queryComp.setString("period_year", period_year);
					queryComp.setString("period_month", period_month);
					queryComp.setString("tel_number", tel_number);
					List listComp = queryComp.list();
					if (listComp != null && listComp.size() > 0) {
						Object[] objStr = (Object[]) listComp.get(0);
						String uuid = objStr[0] == null ? "" : objStr[0]
								.toString();
						BzTelCostCompare telCostCompare = getTelCostCompareById(uuid);
						telCostCompare.setList_total_costs(total_costs);

						if (total_costs.compareTo(telCostCompare
								.getDetail_total_costs()) == 0) {
							telCostCompare.setCompare_result("0");
						} else if (total_costs.compareTo(telCostCompare
								.getDetail_total_costs()) > 0) {
							telCostCompare.setCompare_result("1");
						} else if (total_costs.compareTo(telCostCompare
								.getDetail_total_costs()) < 0) {
							telCostCompare.setCompare_result("2");
						}
						saveOrUpdateCostCompare(telCostCompare);
					} else {
						BzTelCostCompare compareInfo = new BzTelCostCompare();
						compareInfo.setTel_number(tel_number);
						compareInfo.setPeriod_year(period_year);
						compareInfo.setPeriod_month(period_month);
						compareInfo.setList_total_costs(total_costs);
						compareInfo.setCompare_result("3");
						BiTelInfo telInfo = telInfoService
								.getTelInfoByTelNumber(tel_number);
						compareInfo.setTel_uuid(telInfo.getUuid());
						compareInfo.setValid_start_date(telInfo
								.getValid_start_date());
						compareInfo.setBasic_cost(telInfo.getBasic_cost());
						compareInfo.setOut_local_cost(telInfo
								.getOut_local_cost());
						compareInfo.setOut_local_cycle(telInfo
								.getOut_local_cycle());
						compareInfo.setOut_local_min_cycle(telInfo
								.getOut_local_min_cycle());
						compareInfo
								.setOut_long_cost(telInfo.getOut_long_cost());
						compareInfo.setOut_long_cycle(telInfo
								.getOut_long_cycle());
						compareInfo.setOut_long_min_cycle(telInfo
								.getOut_long_min_cycle());
						compareInfo
								.setTogether_flag(telInfo.getTogether_flag());
						compareInfo.setDept_uuid(telInfo.getDept_uuid());
						compareInfo.setDept_name(telInfo.getDept_name());
						compareInfo.setCost_attach_flag(telInfo
								.getCost_attach_flag());
						saveOrUpdateCostCompare(compareInfo);
					}

				}
			}
		} catch (Exception ex) {
			ex.printStackTrace();
		} finally {
			HibernateUtil.closeSession(session);
		}
		return true;
	}

	public BzTelCostDetail getTelCostDetailById(String id) {
		BzTelCostDetail telCostDetail = new BzTelCostDetail();
		try {
			telCostDetail = (BzTelCostDetail) BaseService.get(
					BzTelCostDetail.class, id);
		} catch (Exception e) {
			e.printStackTrace();
			logger.error(e.toString());
		}
		return telCostDetail;
	}
}
