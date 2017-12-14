package com.sten.mtcc.model;

import java.math.BigDecimal;
import java.sql.Timestamp;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.hibernate.annotations.GenericGenerator;

import com.fasterxml.jackson.annotation.JsonAutoDetect;

/**
 * Created by ztw-a on 2017/6/7.
 */
@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY, getterVisibility = JsonAutoDetect.Visibility.NONE)
@Entity
@Table(name = "bz_tel_cost_index")
public class BzTelCostIndex {
	private static final long serialVersionUID = 1L;

	public BzTelCostIndex() {
		super();
	}

	/**
	 * id(主键)
	 */
	@Id
	@Column(name = "UUID")
	@GenericGenerator(name = "systemUUID", strategy = "uuid")
	@GeneratedValue(generator = "systemUUID")
	private String uuid;

	@Column(name = "PERIOD_YEAR")
	private String period_year;

	@Column(name = "PERIOD_MONTH")
	private String period_month;

	@Column(name = "LIST_FILE_DATE")
	private Timestamp list_file_date;

	@Column(name = "DETAIL_FILE_DATE")
	private Timestamp detail_file_date;

	@Transient
	private String list_file_id;
	@Transient
	private String detail_file_id;

	@Transient
	private String list_file_date_dis;

	@Transient
	private String detail_file_date_dis;

	@Column(name = "PRE_COMPARE_STATUS")
	private String pre_compare_status;

	@Transient
	private String pre_compare_date_begin_dis;

	@Transient
	private String pre_compare_date_end_dis;

	@Column(name = "PRE_COMPARE_DATE_BEGIN")
	private Timestamp pre_compare_date_begin;

	@Column(name = "PRE_COMPARE_DATE_END")
	private Timestamp pre_compare_date_end;

	@Column(name = "COMPARE_STATUS")
	private String compare_status;

	@Column(name = "COMPARE_DATE_BEGIN")
	private Timestamp compare_date_begin;

	@Column(name = "COMPARE_DATE_END")
	private Timestamp compare_date_end;

	@Transient
	private String compare_date_begin_dis;

	@Transient
	private String compare_date_end_dis;

	@Column(name = "REMARK")
	private String remark;

	@Column(name = "ORDER_INDEX")
	private int order_index;

	@Column(name = "LIST_TOTAL_COSTS")
	private BigDecimal list_total_costs;

	@Column(name = "DETAIL_TOTAL_COSTS")
	private BigDecimal detail_total_costs;

	@Column(name = "TOTAL_COSTS_OUT")
	private BigDecimal total_costs_out;

	@Column(name = "COMPARE_RESULT")
	private String compare_result;

	@Column(name = "LIST_TEL_SHORTAGE")
	private int list_tel_shortage;

	@Column(name = "DETAIL_TEL_SHORTAGE")
	private int detail_tel_shortage;
	/**
	 * 有效状态：1-是 0-否
	 */
	@Column(name = "ISVALID")
	private String is_valid;

	/**
	 * 创建者ID
	 */
	@Column(name = "CREATEUSERID")
	private String createuserid;

	/**
	 * 创建者
	 */
	@Column(name = "CREATEUSER")
	private String createuser;

	/**
	 * 创建时间
	 */
	@Column(name = "CREATETIME")
	private Timestamp createtime;

	/**
	 * 最终更新者ID
	 */
	@Column(name = "UPDATEUSERID")
	private String updateuserid;

	/**
	 * 最终更新者
	 */
	@Column(name = "UPDATEUSER")
	private String updateuser;

	/**
	 * 最终更新时间
	 */
	@Column(name = "UPDATETIME")
	private Timestamp updatetime;

	public String getUuid() {
		return uuid;
	}

	public void setUuid(String uuid) {
		this.uuid = uuid;
	}

	public String getPeriod_year() {
		return period_year;
	}

	public void setPeriod_year(String period_year) {
		this.period_year = period_year;
	}

	public String getPeriod_month() {
		return period_month;
	}

	public void setPeriod_month(String period_month) {
		this.period_month = period_month;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public int getOrder_index() {
		return order_index;
	}

	public void setOrder_index(int order_index) {
		this.order_index = order_index;
	}

	public String getIs_valid() {
		return is_valid;
	}

	public void setIs_valid(String is_valid) {
		this.is_valid = is_valid;
	}

	public String getCreateuserid() {
		return createuserid;
	}

	public void setCreateuserid(String createuserid) {
		this.createuserid = createuserid;
	}

	public String getCreateuser() {
		return createuser;
	}

	public void setCreateuser(String createuser) {
		this.createuser = createuser;
	}

	public Timestamp getCreatetime() {
		return createtime;
	}

	public void setCreatetime(Timestamp createtime) {
		this.createtime = createtime;
	}

	public String getUpdateuserid() {
		return updateuserid;
	}

	public void setUpdateuserid(String updateuserid) {
		this.updateuserid = updateuserid;
	}

	public String getUpdateuser() {
		return updateuser;
	}

	public void setUpdateuser(String updateuser) {
		this.updateuser = updateuser;
	}

	public Timestamp getUpdatetime() {
		return updatetime;
	}

	public void setUpdatetime(Timestamp updatetime) {
		this.updatetime = updatetime;
	}

	public Timestamp getList_file_date() {
		return list_file_date;
	}

	public void setList_file_date(Timestamp list_file_date) {
		this.list_file_date = list_file_date;
	}

	public Timestamp getDetail_file_date() {
		return detail_file_date;
	}

	public void setDetail_file_date(Timestamp detail_file_date) {
		this.detail_file_date = detail_file_date;
	}

	public String getList_file_date_dis() {
		return list_file_date_dis;
	}

	public void setList_file_date_dis(String list_file_date_dis) {
		this.list_file_date_dis = list_file_date_dis;
	}

	public String getDetail_file_date_dis() {
		return detail_file_date_dis;
	}

	public void setDetail_file_date_dis(String detail_file_date_dis) {
		this.detail_file_date_dis = detail_file_date_dis;
	}

	public String getCompare_status() {
		return compare_status;
	}

	public void setCompare_status(String compare_status) {
		this.compare_status = compare_status;
	}

	public Timestamp getCompare_date_begin() {
		return compare_date_begin;
	}

	public void setCompare_date_begin(Timestamp compare_date_begin) {
		this.compare_date_begin = compare_date_begin;
	}

	public Timestamp getCompare_date_end() {
		return compare_date_end;
	}

	public void setCompare_date_end(Timestamp compare_date_end) {
		this.compare_date_end = compare_date_end;
	}

	public String getList_file_id() {
		return list_file_id;
	}

	public void setList_file_id(String list_file_id) {
		this.list_file_id = list_file_id;
	}

	public String getDetail_file_id() {
		return detail_file_id;
	}

	public void setDetail_file_id(String detail_file_id) {
		this.detail_file_id = detail_file_id;
	}

	public BigDecimal getList_total_costs() {
		return list_total_costs;
	}

	public void setList_total_costs(BigDecimal list_total_costs) {
		this.list_total_costs = list_total_costs;
	}

	public BigDecimal getDetail_total_costs() {
		return detail_total_costs;
	}

	public void setDetail_total_costs(BigDecimal detail_total_costs) {
		this.detail_total_costs = detail_total_costs;
	}

	public BigDecimal getTotal_costs_out() {
		return total_costs_out;
	}

	public void setTotal_costs_out(BigDecimal total_costs_out) {
		this.total_costs_out = total_costs_out;
	}

	public String getCompare_result() {
		return compare_result;
	}

	public void setCompare_result(String compare_result) {
		this.compare_result = compare_result;
	}

	public String getPre_compare_status() {
		return pre_compare_status;
	}

	public void setPre_compare_status(String pre_compare_status) {
		this.pre_compare_status = pre_compare_status;
	}

	public Timestamp getPre_compare_date_begin() {
		return pre_compare_date_begin;
	}

	public void setPre_compare_date_begin(Timestamp pre_compare_date_begin) {
		this.pre_compare_date_begin = pre_compare_date_begin;
	}

	public Timestamp getPre_compare_date_end() {
		return pre_compare_date_end;
	}

	public void setPre_compare_date_end(Timestamp pre_compare_date_end) {
		this.pre_compare_date_end = pre_compare_date_end;
	}

	public int getList_tel_shortage() {
		return list_tel_shortage;
	}

	public void setList_tel_shortage(int list_tel_shortage) {
		this.list_tel_shortage = list_tel_shortage;
	}

	public int getDetail_tel_shortage() {
		return detail_tel_shortage;
	}

	public void setDetail_tel_shortage(int detail_tel_shortage) {
		this.detail_tel_shortage = detail_tel_shortage;
	}

	public String getPre_compare_date_begin_dis() {
		return pre_compare_date_begin_dis;
	}

	public void setPre_compare_date_begin_dis(String pre_compare_date_begin_dis) {
		this.pre_compare_date_begin_dis = pre_compare_date_begin_dis;
	}

	public String getPre_compare_date_end_dis() {
		return pre_compare_date_end_dis;
	}

	public void setPre_compare_date_end_dis(String pre_compare_date_end_dis) {
		this.pre_compare_date_end_dis = pre_compare_date_end_dis;
	}

	public String getCompare_date_begin_dis() {
		return compare_date_begin_dis;
	}

	public void setCompare_date_begin_dis(String compare_date_begin_dis) {
		this.compare_date_begin_dis = compare_date_begin_dis;
	}

	public String getCompare_date_end_dis() {
		return compare_date_end_dis;
	}

	public void setCompare_date_end_dis(String compare_date_end_dis) {
		this.compare_date_end_dis = compare_date_end_dis;
	}

}
