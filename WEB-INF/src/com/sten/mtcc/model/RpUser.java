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
@Table(name = "rp_user")
public class RpUser {
	private static final long serialVersionUID = 1L;

	public RpUser() {
		super();
	}

	/**
	 * id(主键)
	 */
	@Id
	@Column(name = "RU_ID")
	@GenericGenerator(name = "systemUUID", strategy = "uuid")
	@GeneratedValue(generator = "systemUUID")
	private String ru_id;

	@Column(name = "RU_NAME")
	private String ru_name;

	@Column(name = "RU_PASSWORD")
	private String ru_password;

	@Column(name = "RU_ORG_CODE")
	private String ru_org_code;

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

	public String getRu_id() {
		return ru_id;
	}

	public void setRu_id(String ru_id) {
		this.ru_id = ru_id;
	}

	public String getRu_name() {
		return ru_name;
	}

	public void setRu_name(String ru_name) {
		this.ru_name = ru_name;
	}

	public String getRu_password() {
		return ru_password;
	}

	public void setRu_password(String ru_password) {
		this.ru_password = ru_password;
	}

	public String getRu_org_code() {
		return ru_org_code;
	}

	public void setRu_org_code(String ru_org_code) {
		this.ru_org_code = ru_org_code;
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
