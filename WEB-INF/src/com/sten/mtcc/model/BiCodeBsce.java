package com.sten.mtcc.model;

import java.sql.Timestamp;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonAutoDetect;

/**
 * Created by MaZQ on 2017/2/27.
 */
@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY, getterVisibility = JsonAutoDetect.Visibility.NONE)
@Entity
@Table(name = "BI_CODE_BSCE")
public class BiCodeBsce {
	private static final long serialVersionUID = 1L;

	public BiCodeBsce() {
		super();
	}

	/**
	 * id(主键)
	 */
	@Id
	@Column(name = "GBCT_CODE")
	private String gbct_code;

	/**
	 * id(主键)
	 */
	@Column(name = "GBCB_ID")
	private String gbcb_id;

	/**
	 * 基础代码名称
	 */
	@Column(name = "GBCB_NAME")
	private String gbcb_name;

	/**
	 * 基础代码名称别名1
	 */
	@Column(name = "GBCB_NAME_ALIAS1")
	private String gbcb_name_alias1;

	/**
	 * 基础代码名称别名2
	 */
	@Column(name = "GBCB_NAME_ALIAS2")
	private String gbcb_name_alias2;

	/**
	 * 基础代码名称别名3
	 */
	@Column(name = "GBCB_NAME_ALIAS3")
	private String gbcb_name_alias3;

	/**
	 * 有效状态：1-是 0-否
	 */
	@Column(name = "ISVALID")
	private String is_valid;

	/**
	 * 排序号
	 */
	@Column(name = "ORDER_INDEX")
	private int order_index;

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

	public String getGbct_code() {
		return gbct_code;
	}

	public void setGbct_code(String gbct_code) {
		this.gbct_code = gbct_code;
	}

	public String getGbcb_id() {
		return gbcb_id;
	}

	public void setGbcb_id(String gbcb_id) {
		this.gbcb_id = gbcb_id;
	}

	public String getGbcb_name() {
		return gbcb_name;
	}

	public void setGbcb_name(String gbcb_name) {
		this.gbcb_name = gbcb_name;
	}

	public String getGbcb_name_alias1() {
		return gbcb_name_alias1;
	}

	public void setGbcb_name_alias1(String gbcb_name_alias1) {
		this.gbcb_name_alias1 = gbcb_name_alias1;
	}

	public String getGbcb_name_alias2() {
		return gbcb_name_alias2;
	}

	public void setGbcb_name_alias2(String gbcb_name_alias2) {
		this.gbcb_name_alias2 = gbcb_name_alias2;
	}

	public String getGbcb_name_alias3() {
		return gbcb_name_alias3;
	}

	public void setGbcb_name_alias3(String gbcb_name_alias3) {
		this.gbcb_name_alias3 = gbcb_name_alias3;
	}

	public String getIs_valid() {
		return is_valid;
	}

	public void setIs_valid(String is_valid) {
		this.is_valid = is_valid;
	}

	public int getOrder_index() {
		return order_index;
	}

	public void setOrder_index(int order_index) {
		this.order_index = order_index;
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
