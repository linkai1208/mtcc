package com.sten.mtcc.service;

import java.util.ArrayList;
import java.util.List;

import org.hibernate.Query;
import org.hibernate.Session;
import org.springframework.stereotype.Service;

import com.sten.framework.util.HibernateUtil;
import com.sten.mtcc.model.BiCodeBsce;

/**
 * Created by MaZQ on 2017/3/3.
 */
@Service("biCodeBsceService")
public class BiCodeBsceService {

	/**
	 * 查询数据
	 * 
	 * @return
	 */
	public static List<BiCodeBsce> getBiCodeBsce(String gbct_code) {
		Session session = null;
		List<BiCodeBsce> codeList = new ArrayList<BiCodeBsce>();
		try {
			session = HibernateUtil.getSession();
			String hql = "select gbcb_id,gbcb_name from Bi_Code_Bsce where gbct_code='"
					+ gbct_code + "' and ISVALID='1' order by order_index asc";
			Query query = session.createSQLQuery(hql);
			List list = query.list();
			if (list != null && list.size() > 0) {
				for (int i = 0; i < list.size(); i++) {
					Object[] obj = (Object[]) list.get(i);
					BiCodeBsce codeBsce = new BiCodeBsce();
					codeBsce.setGbcb_id(obj[0] == null ? "" : obj[0].toString());
					codeBsce.setGbcb_name(obj[1] == null ? "" : obj[1]
							.toString());
					codeList.add(codeBsce);
				}
			}
		} catch (Exception ex) {
			ex.printStackTrace();
		} finally {
			HibernateUtil.closeSession(session);
		}
		return codeList;
	}

	public static String getBiCodeBsceNameByKey(String gbct_code, String gbcb_id) {
		Session session = null;
		String codeName = "";
		try {
			session = HibernateUtil.getSession();
			String hql = "select gbcb_name from Bi_Code_Bsce where gbct_code=:gbct_code and gbcb_id=:gbcb_id order by order_index asc";
			Query query = session.createSQLQuery(hql);
			query.setString("gbct_code", gbct_code);
			query.setString("gbcb_id", gbcb_id);

			List list = query.list();
			if (list != null && list.size() > 0) {
				String obj = (String) list.get(0);
				codeName = obj == null ? "" : obj.toString();
			}
		} catch (Exception ex) {
			ex.printStackTrace();
		} finally {
			HibernateUtil.closeSession(session);
		}
		return codeName;
	}

	public static String getBiCodeBsceKeyByName(String gbct_code,
			String gbcb_name) {
		Session session = null;
		String codeId = "";
		try {
			session = HibernateUtil.getSession();
			String hql = "select gbcb_id from Bi_Code_Bsce where gbct_code=:gbct_code and gbcb_name=:gbcb_name order by order_index asc";
			Query query = session.createSQLQuery(hql);
			query.setString("gbct_code", gbct_code);
			query.setString("gbcb_name", gbcb_name);

			List list = query.list();
			if (list != null && list.size() > 0) {
				String obj = (String) list.get(0);
				codeId = obj == null ? "" : obj.toString();
			}
		} catch (Exception ex) {
			ex.printStackTrace();
		} finally {
			HibernateUtil.closeSession(session);
		}
		return codeId;
	}
}
