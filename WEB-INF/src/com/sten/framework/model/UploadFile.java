package com.sten.framework.model;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonAutoDetect;

//使用默认大小写属性转换json
@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY, getterVisibility = JsonAutoDetect.Visibility.NONE)
@Entity
@Table(name = "T_UPLOADFILE")
public class UploadFile {
	private static final long serialVersionUID = 1L;

	public UploadFile() {
		super();
	}

	// 主键, 上传的文件以此id命名
	@Id
	@Column(name = "FILEID")
	private String fileId;

	// 业务id, 上传的文件关联的业务主键
	@Column(name = "FOREIGNID")
	private String foreignId;

	// 文件名, 不包括扩展名
	@Column(name = "FILENAME")
	private String fileName;

	// 包含日期的本地相对路径、包括扩展名
	@Column(name = "FULLFILENAME")
	private String fullFileName;

	// 扩展名，包含点 .jpg .png
	@Column(name = "EXTENSION")
	private String extension;

	@Column(name = "FILELENGTH")
	private long fileLength;

	@Column(name = "FILESIZE")
	private String fileSize;

	// 业务分类, 自定义扩展使用
	// 例如一个人可以上传照片, 可以上传身份证, foreignId 相同都是同一个人
	// 用 category 区分上传的是什么数据, 例如照片可以保存为photo, 身份证可以保存idcard
	@Column(name = "CATEGORY")
	private String category;

	// 0临时文件 1正式文件 2删除文件
	@Column(name = "STATE")
	private int state;

	@Column(name = "ORDERINDEX")
	private int orderIndex;

	@Column(name = "DESCRIPTION")
	private String description;

	@Column(name = "CREATEUSERID")
	private String createUserId;

	@Column(name = "CREATEUSERNAME")
	private String createUserName;

	@Column(name = "CREATEDATE")
	private Date createDate;

	public String getFileId() {
		return fileId;
	}

	public void setFileId(String fileId) {
		this.fileId = fileId;
	}

	public String getForeignId() {
		return foreignId;
	}

	public void setForeignId(String foreignId) {
		this.foreignId = foreignId;
	}

	public String getFileName() {
		return fileName;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

	public String getFullFileName() {
		return fullFileName;
	}

	public void setFullFileName(String fullFileName) {
		this.fullFileName = fullFileName;
	}

	public String getExtension() {
		return extension;
	}

	public void setExtension(String extension) {
		this.extension = extension;
	}

	public long getFileLength() {
		return fileLength;
	}

	public void setFileLength(long fileLength) {
		this.fileLength = fileLength;
	}

	public String getFileSize() {
		return fileSize;
	}

	public void setFileSize(String fileSize) {
		this.fileSize = fileSize;
	}

	public String getCategory() {
		return category;
	}

	public void setCategory(String category) {
		this.category = category;
	}

	public int getState() {
		return state;
	}

	public void setState(int state) {
		this.state = state;
	}

	public int getOrderIndex() {
		return orderIndex;
	}

	public void setOrderIndex(int orderIndex) {
		this.orderIndex = orderIndex;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getCreateUserId() {
		return createUserId;
	}

	public void setCreateUserId(String createUserId) {
		this.createUserId = createUserId;
	}

	public String getCreateUserName() {
		return createUserName;
	}

	public void setCreateUserName(String createUserName) {
		this.createUserName = createUserName;
	}

	public Date getCreateDate() {
		return createDate;
	}

	public void setCreateDate(Date createDate) {
		this.createDate = createDate;
	}
}