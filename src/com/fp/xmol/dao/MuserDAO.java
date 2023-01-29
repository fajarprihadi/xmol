package com.fp.xmol.dao;

import java.util.ArrayList;
import java.util.List;

import org.hibernate.HibernateException;
import org.hibernate.Session;

import com.fp.utils.db.StoreHibernateUtil;
import com.fp.xmol.domain.Muser;

public class MuserDAO {

	private Session session;

	public Muser login(Session session, String loginid) throws HibernateException, Exception {
		Muser oForm = null;
		if (loginid != null)
			loginid = loginid.trim().toUpperCase();
		oForm = (Muser) session.createQuery("from Muser where userid = '" + loginid + "'").uniqueResult();
		return oForm;
	}

	@SuppressWarnings("unchecked")
	public List<Muser> listPaging(int first, int second, String filter, String orderby) throws Exception {
		List<Muser> oList = null;
		if (filter == null || "".equals(filter))
			filter = "0 = 0";
		session = StoreHibernateUtil.openSession();
		oList = session.createSQLQuery("select * from Muser " + "where " + filter + " order by " + orderby + " offset "
				+ first + " rows fetch next " + second + " rows only").addEntity(Muser.class).list();

		session.close();
		return oList;
	}

	public int pageCount(String filter) throws Exception {
		int count = 0;
		if (filter == null || "".equals(filter))
			filter = "0 = 0";
		session = StoreHibernateUtil.openSession();
		count = Integer.parseInt((String) session.createSQLQuery(
				"select count(*) from Muser join Musergroup on musergroupfk = musergrouppk " + "where " + filter)
				.uniqueResult().toString());
		session.close();
		return count;
	}

	@SuppressWarnings("unchecked")
	public List<Muser> listByFilter(String filter, String orderby) throws Exception {
		List<Muser> oList = null;
		if (filter == null || "".equals(filter))
			filter = "0 = 0";
		session = StoreHibernateUtil.openSession();
		oList = session.createQuery("from Muser where " + filter + " order by " + orderby).list();
		session.close();
		return oList;
	}

	public Muser findByPk(Integer pk) throws Exception {
		session = StoreHibernateUtil.openSession();
		Muser oForm = (Muser) session.createQuery("from Muser where muserpk = " + pk).uniqueResult();
		session.close();
		return oForm;
	}

	public Muser findByUserid(String userid) throws Exception {
		session = StoreHibernateUtil.openSession();
		Muser oForm = (Muser) session.createQuery("from Muser where userid = '" + userid + "'").uniqueResult();
		session.close();
		return oForm;
	}

	@SuppressWarnings("rawtypes")
	public List listStr(String fieldname) throws Exception {
		List oList = new ArrayList();
		session = StoreHibernateUtil.openSession();
		oList = session.createQuery("select " + fieldname + " from Muser order by " + fieldname).list();
		session.close();
		return oList;
	}

	public void save(Session session, Muser oForm) throws HibernateException, Exception {
		session.saveOrUpdate(oForm);
	}

	public void delete(Session session, Muser oForm) throws HibernateException, Exception {
		session.delete(oForm);
	}

}
