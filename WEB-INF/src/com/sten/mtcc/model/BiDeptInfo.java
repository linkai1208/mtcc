package com.sten.mtcc.model;

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
@Table(name = "bi_dept_info")
public class BiDeptInfo {
	private static final long serialVersionUID = 1L;

	public BiDeptInfo() {
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

	@Column(name = "DEPT_CODE")
	private String dept_code;

	@Column(name = "DEPT_NAME")
	private String dept_name;

	@Column(name = "DEPT_ADDRESS")
	private String dept_address;

	@Column(name = "COST_ATTACH_FLAG")
	private String cost_attach_flag;

	@Column(name = "REMARK")
	private String remark;

	@Column(name = "ORDER_INDEX")
	private int order_index;

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

	public String getDept_code() {
		return dept_code;
	}

	public void setDept_code(String dept_code) {
		this.dept_code = dept_code;
	}

	public String getDept_name() {
		return dept_name;
	}

	public void setDept_name(String dept_name) {
		this.dept_name = dept_name;
	}

	public String getDept_address() {
		return dept_address;
	}

	public void setDept_address(String dept_address) {
		this.dept_address = dept_address;
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

}
