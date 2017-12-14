package com.sten.mtcc.model;

import java.math.BigDecimal;
import java.sql.Timestamp;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;

import com.fasterxml.jackson.annotation.JsonAutoDetect;

/**
 * Created by ztw-a on 2017/6/7.
 */
@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY, getterVisibility = JsonAutoDetect.Visibility.NONE)
@Entity
@Table(name = "bz_tel_cost_detail")
public class BzTelCostDetail {
	private static final long serialVersionUID = 1L;

	public BzTelCostDetail() {
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

	@Column(name = "TEL_NUMBER")
	private String tel_number;

	@Column(name = "PERIOD_YEAR")
	private String period_year;

	@Column(name = "PERIOD_MONTH")
	private String period_month;

	@Column(name = "HODING_TIME_ORIG")
	private String hoding_time_orig;

	@Column(name = "HODING_TIME")
	private Timestamp hoding_time;

	@Column(name = "CALL_DURATION_ORIG")
	private String call_duration_orig;

	@Column(name = "CALL_DURATION")
	private int call_duration;

	@Column(name = "CALL_TYPE")
	private String call_type;

	@Column(name = "CALL_TYPE_NAME")
	private String call_type_name;

	@Column(name = "CALLED_NUMBER")
	private String called_number;

	@Column(name = "LONG_DISTANCE_TYPE")
	private String long_distance_type;

	@Column(name = "LONG_DISTANCE_TYPE_NAME")
	private String long_distance_type_name;

	@Column(name = "BASIC_COSTS")
	private BigDecimal basic_costs;

	@Column(name = "LONG_COSTS")
	private BigDecimal long_costs;

	@Column(name = "COST_ATTACH_FLAG")
	private String cost_attach_flag;

	@Column(name = "REMARK")
	private String remark;

	@Column(name = "ORDER_INDEX")
	private int order_index;

	@Column(name = "ISVALID")
	private String is_valid;

	@Column(name = "BASIC_COSTS_COMP")
	private BigDecimal basic_costs_comp;

	@Column(name = "LONG_COSTS_COMP")
	private BigDecimal long_costs_comp;

	@Column(name = "BASIC_COSTS_COMP_OUT")
	private BigDecimal basic_costs_comp_out;

	@Column(name = "LONG_COSTS_COMP_OUT")
	private BigDecimal long_costs_comp_out;

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

	public String getTel_number() {
		return tel_number;
	}

	public void setTel_number(String tel_number) {
		this.tel_number = tel_number;
	}

	public String getHoding_time_orig() {
		return hoding_time_orig;
	}

	public void setHoding_time_orig(String hoding_time_orig) {
		this.hoding_time_orig = hoding_time_orig;
	}

	public Timestamp getHoding_time() {
		return hoding_time;
	}

	public void setHoding_time(Timestamp hoding_time) {
		this.hoding_time = hoding_time;
	}

	public String getCall_duration_orig() {
		return call_duration_orig;
	}

	public void setCall_duration_orig(String call_duration_orig) {
		this.call_duration_orig = call_duration_orig;
	}

	public int getCall_duration() {
		return call_duration;
	}

	public void setCall_duration(int call_duration) {
		this.call_duration = call_duration;
	}

	public String getCall_type() {
		return call_type;
	}

	public void setCall_type(String call_type) {
		this.call_type = call_type;
	}

	public String getCall_type_name() {
		return call_type_name;
	}

	public void setCall_type_name(String call_type_name) {
		this.call_type_name = call_type_name;
	}

	public String getCalled_number() {
		return called_number;
	}

	public void setCalled_number(String called_number) {
		this.called_number = called_number;
	}

	public String getLong_distance_type() {
		return long_distance_type;
	}

	public void setLong_distance_type(String long_distance_type) {
		this.long_distance_type = long_distance_type;
	}

	public String getLong_distance_type_name() {
		return long_distance_type_name;
	}

	public void setLong_distance_type_name(String long_distance_type_name) {
		this.long_distance_type_name = long_distance_type_name;
	}

	public BigDecimal getBasic_costs() {
		return basic_costs;
	}

	public void setBasic_costs(BigDecimal basic_costs) {
		this.basic_costs = basic_costs;
	}

	public BigDecimal getLong_costs() {
		return long_costs;
	}

	public void setLong_costs(BigDecimal long_costs) {
		this.long_costs = long_costs;
	}

	public String getCost_attach_flag() {
		return cost_attach_flag;
	}

	public void setCost_attach_flag(String cost_attach_flag) {
		this.cost_attach_flag = cost_attach_flag;
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

	public BigDecimal getBasic_costs_comp() {
		return basic_costs_comp;
	}

	public void setBasic_costs_comp(BigDecimal basic_costs_comp) {
		this.basic_costs_comp = basic_costs_comp;
	}

	public BigDecimal getLong_costs_comp() {
		return long_costs_comp;
	}

	public void setLong_costs_comp(BigDecimal long_costs_comp) {
		this.long_costs_comp = long_costs_comp;
	}

	public BigDecimal getBasic_costs_comp_out() {
		return basic_costs_comp_out;
	}

	public void setBasic_costs_comp_out(BigDecimal basic_costs_comp_out) {
		this.basic_costs_comp_out = basic_costs_comp_out;
	}

	public BigDecimal getLong_costs_comp_out() {
		return long_costs_comp_out;
	}

	public void setLong_costs_comp_out(BigDecimal long_costs_comp_out) {
		this.long_costs_comp_out = long_costs_comp_out;
	}

}
