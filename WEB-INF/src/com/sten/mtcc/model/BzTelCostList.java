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
@Table(name = "bz_tel_cost_list")
public class BzTelCostList {
	private static final long serialVersionUID = 1L;

	public BzTelCostList() {
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

	@Column(name = "TEL_NUMBER")
	private String tel_number;

	@Column(name = "FIXED_COSTS")
	private BigDecimal fixed_costs;

	@Column(name = "VOICE_COSTS")
	private BigDecimal voice_costs;

	@Column(name = "NET_COSTS")
	private BigDecimal net_costs;

	@Column(name = "MESSAGE_COSTS")
	private BigDecimal message_costs;

	@Column(name = "ADDED_COSTS")
	private BigDecimal added_costs;

	@Column(name = "COLLECTION_COSTS")
	private BigDecimal collection_costs;

	@Column(name = "OTHER_COSTS")
	private BigDecimal other_costs;

	@Column(name = "DEDUCTION_COSTS")
	private BigDecimal deduction_costs;

	@Column(name = "TOTAL_COSTS")
	private BigDecimal total_costs;

	@Column(name = "REMARK")
	private String remark;

	@Column(name = "ORDER_INDEX")
	private int order_index;

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

	public String getTel_number() {
		return tel_number;
	}

	public void setTel_number(String tel_number) {
		this.tel_number = tel_number;
	}

	public BigDecimal getFixed_costs() {
		return fixed_costs;
	}

	public void setFixed_costs(BigDecimal fixed_costs) {
		this.fixed_costs = fixed_costs;
	}

	public BigDecimal getVoice_costs() {
		return voice_costs;
	}

	public void setVoice_costs(BigDecimal voice_costs) {
		this.voice_costs = voice_costs;
	}

	public BigDecimal getNet_costs() {
		return net_costs;
	}

	public void setNet_costs(BigDecimal net_costs) {
		this.net_costs = net_costs;
	}

	public BigDecimal getMessage_costs() {
		return message_costs;
	}

	public void setMessage_costs(BigDecimal message_costs) {
		this.message_costs = message_costs;
	}

	public BigDecimal getAdded_costs() {
		return added_costs;
	}

	public void setAdded_costs(BigDecimal added_costs) {
		this.added_costs = added_costs;
	}

	public BigDecimal getCollection_costs() {
		return collection_costs;
	}

	public void setCollection_costs(BigDecimal collection_costs) {
		this.collection_costs = collection_costs;
	}

	public BigDecimal getOther_costs() {
		return other_costs;
	}

	public void setOther_costs(BigDecimal other_costs) {
		this.other_costs = other_costs;
	}

	public BigDecimal getDeduction_costs() {
		return deduction_costs;
	}

	public void setDeduction_costs(BigDecimal deduction_costs) {
		this.deduction_costs = deduction_costs;
	}

	public BigDecimal getTotal_costs() {
		return total_costs;
	}

	public void setTotal_costs(BigDecimal total_costs) {
		this.total_costs = total_costs;
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

}
