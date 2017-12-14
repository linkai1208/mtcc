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
@Table(name = "bz_tel_cost_compare")
public class BzTelCostCompare {
	private static final long serialVersionUID = 1L;

	public BzTelCostCompare() {
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

	@Column(name = "TEL_UUID")
	private String tel_uuid;

	@Column(name = "TEL_NUMBER")
	private String tel_number;

	@Column(name = "DEPT_UUID")
	private String dept_uuid;

	@Column(name = "DEPT_CODE")
	private String dept_code;

	@Column(name = "DEPT_NAME")
	private String dept_name;

	@Column(name = "VALID_START_DATE")
	private String valid_start_date;

	@Column(name = "BASIC_COST")
	private BigDecimal basic_cost;

	@Column(name = "OUT_LOCAL_COST")
	private BigDecimal out_local_cost;

	@Column(name = "OUT_LOCAL_CYCLE")
	private Integer out_local_cycle;

	@Column(name = "OUT_LOCAL_MIN_CYCLE")
	private Integer out_local_min_cycle;

	@Column(name = "OUT_LONG_COST")
	private BigDecimal out_long_cost;

	@Column(name = "OUT_LONG_CYCLE")
	private Integer out_long_cycle;

	@Column(name = "OUT_LONG_MIN_CYCLE")
	private Integer out_long_min_cycle;

	@Column(name = "TELEC_OPERATOR_TYPE")
	private String telec_operator_type;

	@Column(name = "TOGETHER_FLAG")
	private String together_flag;

	@Column(name = "COST_ATTACH_FLAG")
	private String cost_attach_flag;

	@Column(name = "REMARK")
	private String remark;

	@Column(name = "ORDER_INDEX")
	private int order_index;

	@Column(name = "PERIOD_YEAR")
	private String period_year;

	@Column(name = "PERIOD_MONTH")
	private String period_month;

	@Column(name = "LIST_TOTAL_COSTS")
	private BigDecimal list_total_costs;

	@Column(name = "DETAIL_TOTAL_COSTS")
	private BigDecimal detail_total_costs;

	@Column(name = "TOTAL_COSTS_OUT")
	private BigDecimal total_costs_out;

	@Column(name = "COMPARE_RESULT")
	private String compare_result;

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

	@Transient
	private String attach_field;

	public String getCost_attach_flag() {
		return cost_attach_flag;
	}

	public void setCost_attach_flag(String cost_attach_flag) {
		this.cost_attach_flag = cost_attach_flag;
	}

	public String getTel_number() {
		return tel_number;
	}

	public void setTel_number(String tel_number) {
		this.tel_number = tel_number;
	}

	public String getDept_uuid() {
		return dept_uuid;
	}

	public void setDept_uuid(String dept_uuid) {
		this.dept_uuid = dept_uuid;
	}

	public String getDept_name() {
		return dept_name;
	}

	public void setDept_name(String dept_name) {
		this.dept_name = dept_name;
	}

	public String getValid_start_date() {
		return valid_start_date;
	}

	public void setValid_start_date(String valid_start_date) {
		this.valid_start_date = valid_start_date;
	}

	public BigDecimal getBasic_cost() {
		return basic_cost;
	}

	public void setBasic_cost(BigDecimal basic_cost) {
		this.basic_cost = basic_cost;
	}

	public BigDecimal getOut_local_cost() {
		return out_local_cost;
	}

	public void setOut_local_cost(BigDecimal out_local_cost) {
		this.out_local_cost = out_local_cost;
	}

	public Integer getOut_local_cycle() {
		return out_local_cycle;
	}

	public void setOut_local_cycle(Integer out_local_cycle) {
		this.out_local_cycle = out_local_cycle;
	}

	public Integer getOut_local_min_cycle() {
		return out_local_min_cycle;
	}

	public void setOut_local_min_cycle(Integer out_local_min_cycle) {
		this.out_local_min_cycle = out_local_min_cycle;
	}

	public BigDecimal getOut_long_cost() {
		return out_long_cost;
	}

	public void setOut_long_cost(BigDecimal out_long_cost) {
		this.out_long_cost = out_long_cost;
	}

	public Integer getOut_long_cycle() {
		return out_long_cycle;
	}

	public void setOut_long_cycle(Integer out_long_cycle) {
		this.out_long_cycle = out_long_cycle;
	}

	public Integer getOut_long_min_cycle() {
		return out_long_min_cycle;
	}

	public void setOut_long_min_cycle(Integer out_long_min_cycle) {
		this.out_long_min_cycle = out_long_min_cycle;
	}

	public String getTelec_operator_type() {
		return telec_operator_type;
	}

	public void setTelec_operator_type(String telec_operator_type) {
		this.telec_operator_type = telec_operator_type;
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

	public String getTogether_flag() {
		return together_flag;
	}

	public void setTogether_flag(String together_flag) {
		this.together_flag = together_flag;
	}

	public String getUuid() {
		return uuid;
	}

	public void setUuid(String uuid) {
		this.uuid = uuid;
	}

	public String getAttach_field() {
		return attach_field;
	}

	public void setAttach_field(String attach_field) {
		this.attach_field = attach_field;
	}

	public String getDept_code() {
		return dept_code;
	}

	public void setDept_code(String dept_code) {
		this.dept_code = dept_code;
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

	public String getCompare_result() {
		return compare_result;
	}

	public void setCompare_result(String compare_result) {
		this.compare_result = compare_result;
	}

	public String getTel_uuid() {
		return tel_uuid;
	}

	public void setTel_uuid(String tel_uuid) {
		this.tel_uuid = tel_uuid;
	}

	public BigDecimal getTotal_costs_out() {
		return total_costs_out;
	}

	public void setTotal_costs_out(BigDecimal total_costs_out) {
		this.total_costs_out = total_costs_out;
	}

}
