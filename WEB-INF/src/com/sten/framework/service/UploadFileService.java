package com.sten.framework.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections.map.HashedMap;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.springframework.stereotype.Service;

import com.sten.framework.model.UploadFile;
import com.sten.framework.util.HibernateUtil;

/**
 * Created by linkai on 16-1-19.
 */
@Service
public class UploadFileService {

	// 文件状态
	public enum STATE {

		// 0临时文件 1正式文件 2删除文件
		TEMP("临时文件", 0), PUBLISHED("正式文件", 1), DELETED("删除文件", 2);

		private String name;
		private int code;

		STATE(String name, int code) {
			this.name = name;
			this.code = code;
		}

		public String getName() {
			return name;
		}

		public int getCode() {
			return code;
		}

		public static String getName(int code) {
			String result = "";
			for (STATE e : values()) {
				if (e.getCode() == code) {
					result = e.getName();
					break;
				}
			}
			return result;
		}
	}

	/**
	 * 查询某个业务分类文件的最大排序号码
	 * 
	 * @param foreignId
	 *            业务对象的主键
	 * @param category
	 *            分类参数
	 * @return
	 */
	public int getMaxOrderIndex(String foreignId, String category) {

		HashMap<String, Object> param = new HashMap<String, Object>();
		param.put("foreignId", foreignId);

		String hql = "select count(*) from UploadFile where foreignId=:foreignId";
		if (category != null && category.length() > 0) {
			hql += " and category=:category";
			param.put("category", category);
		}

		int count = BaseService.getCountByHql(hql, param);
		count++;
		return count;
	}

	public void addFile(UploadFile uploadFile) {
		BaseService.add(uploadFile);
	}

	/**
	 * 标记删除某个文件记录
	 * 
	 * @param fileid
	 *            主键
	 * @return
	 */
	public void markDeleteFile(String fileid) {
		UploadFile uploadFile = (UploadFile) BaseService.get(UploadFile.class,
				fileid);
		uploadFile.setState(2);
		BaseService.update(uploadFile);
	}

	/**
	 * 删除某个文件记录及物理文件
	 * 
	 * @param fileid
	 *            主键
	 * @return
	 */
	public void deleteFile(String fileid) {
		Object uploadFile = BaseService.get(UploadFile.class, fileid);
		BaseService.delete(uploadFile);
		// TODO:删除文件
	}

	/**
	 * 根据 foreignId 删除某个文件记录及物理文件
	 * 
	 * @param foreignId
	 *            外键
	 * @return
	 */
	public void deleteFileByForeignId(String foreignId) {
		String hql = "from UploadFile where foreignId = :foreignId";
		List list = BaseService.queryByHql(hql, "foreignId", foreignId);
		BaseService.delete(list);
		// TODO:删除文件
	}

	/**
	 * 根据某个业务主键 foreignId 查询某个状态的文件对象
	 * 
	 * @param foreignId
	 *            业务主键
	 * @param state
	 *            0临时文件 1正式文件 2删除文件
	 * @return
	 */
	public List<UploadFile> loadFiles(String foreignId, int state) {
		String hql = "from UploadFile where foreignId = :foreignId and state=:state";
		HashMap<String, Object> param = new HashMap<String, Object>();
		param.put("foreignId", foreignId);
		param.put("state", state);
		List<UploadFile> list = BaseService.queryByHql(hql, param);
		return list;
	}

	/**
	 * 根据某个业务主键 foreignId 查询某个状态的文件对象
	 * 
	 * @param foreignId
	 *            业务主键
	 * @param category
	 *            业务分类
	 * @return
	 */
	public List<UploadFile> loadFiles(String foreignId, String category) {
		String hql = "from UploadFile where foreignId = :foreignId and category = :category";
		HashMap<String, Object> param = new HashMap<String, Object>();
		param.put("foreignId", foreignId);
		param.put("category", category);
		List<UploadFile> list = BaseService.queryByHql(hql, param);
		return list;
	}

	/**
	 * 根据某个业务主键 foreignId 查询某个状态的文件对象
	 * 
	 * @param foreignId
	 *            业务主键
	 * @param category
	 *            业务分类
	 * @param state
	 *            0临时文件 1正式文件 2删除文件
	 * @return
	 */
	public List<UploadFile> loadFiles(String foreignId, String category,
			int state) {
		String hql = "from UploadFile where foreignId = :foreignId and category = :category and state=:state";
		HashMap<String, Object> param = new HashMap<String, Object>();
		param.put("foreignId", foreignId);
		param.put("category", category);
		param.put("state", state);
		List<UploadFile> list = BaseService.queryByHql(hql, param);
		return list;
	}

	/**
	 * 根据某个业务主键 foreignId 查询所有文件对象
	 * 
	 * @param foreignId
	 *            业务主键
	 * @return
	 */
	public List<UploadFile> loadFiles(String foreignId) {
		String hql = "from UploadFile where foreignId = :foreignId";
		HashMap<String, Object> param = new HashMap<String, Object>();
		param.put("foreignId", foreignId);
		List<UploadFile> list = BaseService.queryByHql(hql, param);
		return list;
	}

	/**
	 * 根据文件id 查询所有文件对象
	 * 
	 * @param fileids
	 *            文件id
	 * @return
	 */
	public List<UploadFile> loadFiles(String[] fileids) {
		String hql = "from UploadFile where fileId in(:fileids)";
		HashMap<String, Object> param = new HashMap<String, Object>();
		param.put("fileids", fileids);
		List<UploadFile> list = BaseService.queryByHql(hql, param);
		return list;
	}

	/**
	 * 根据某个业务主键 foreignId 查询所有文件对象
	 * 
	 * @param fileid
	 *            业务主键
	 * @return
	 */
	public UploadFile getFile(String fileid) {
		UploadFile file = (UploadFile) BaseService
				.get(UploadFile.class, fileid);
		return file;
	}

	/**
	 * 批量更新文件对象状态
	 * 
	 * @param files
	 *            文件对象
	 * @return
	 */
	public boolean updateFiles(List<UploadFile> files) {
		Session session = null;
		Transaction ts = null;
		boolean result = false;
		try {
			session = HibernateUtil.getSession();
			ts = session.beginTransaction();
			for (UploadFile file : files) {
				session.update(file);
			}
			ts.commit();
			result = true;
		} catch (Exception ex) {
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
	 * 批量更新文件对象状态
	 * 
	 * @return
	 */
	public int updateFileState(String foreignId, String category, STATE state) {
		Session session = null;
		Transaction ts = null;
		int result = 0;

		String hql = "update UploadFile set state = :state where foreignId = :foreignId and category = :category";
		Map<String, Object> param = new HashedMap();
		param.put("foreignId", foreignId);
		param.put("category", category);
		param.put("state", state.code);
		result = BaseService.updateByHql(hql, param);
		return result;
	}

	/**
	 * 更新文件对象状态
	 * 
	 * @return
	 */
	public int updateFileState(String foreignId, STATE state) {
		Session session = null;
		Transaction ts = null;
		int result = 0;

		String hql = "update UploadFile set state = :state where foreignId = :foreignId";
		Map<String, Object> param = new HashedMap();
		param.put("foreignId", foreignId);
		param.put("state", state.code);
		result = BaseService.updateByHql(hql, param);
		return result;
	}
}
