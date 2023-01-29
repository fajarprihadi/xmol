package com.fp.xmol.dao;

import java.util.ArrayList;
import java.util.List;

import org.hibernate.HibernateException;
import org.hibernate.Session;

import com.fp.utils.db.StoreHibernateUtil;
import com.fp.xmol.domain.Mdepartment;

public class MdepartmentDAO {

	private Session session;

	@SuppressWarnings("unchecked")
	public List<Mdepartment> listPaging(int first, int second, String filter, String orderby) throws Exception {
		List<Mdepartment> oList = null;
		if (filter == null || "".equals(filter))
			filter = "0 = 0";
		session = StoreHibernateUtil.openSession();
		oList = session.createSQLQuery("select * from Mdepartment " + "where " + filter + " order by " + orderby + " offset "
				+ first + " rows fetch next " + second + " rows only").addEntity(Mdepartment.class).list();

		session.close();
		return oList;
	}

	public int pageCount(String filter) throws Exception {
		int count = 0;
		if (filter == null || "".equals(filter))
			filter = "0 = 0";
		session = StoreHibernateUtil.openSession();
		count = Integer.parseInt((String) session.createSQLQuery(
				"select count(*) from Mdepartment join Mdepartmentgroup on mdepartmentgroupfk = mdepartmentgrouppk " + "where " + filter)
				.uniqueResult().toString());
		session.close();
		return count;
	}

	@SuppressWarnings("unchecked")
	public List<Mdepartment> listByFilter(String filter, String orderby) throws Exception {
		List<Mdepartment> oList = null;
		if (filter == null || "".equals(filter))
			filter = "0 = 0";
		session = StoreHibernateUtil.openSession();
		oList = session.createQuery("from Mdepartment where " + filter + " order by " + orderby).list();
		session.close();
		return oList;
	}

	public Mdepartment findByPk(Integer pk) throws Exception {
		session = StoreHibernateUtil.openSession();
		Mdepartment oForm = (Mdepartment) session.createQuery("from Mdepartment where mdepartmentpk = " + pk).uniqueResult();
		session.close();
		return oForm;
	}

	public Mdepartment findByUserid(String departmentid) throws Exception {
		session = StoreHibernateUtil.openSession();
		Mdepartment oForm = (Mdepartment) session.createQuery("from Mdepartment where departmentid = '" + departmentid + "'").uniqueResult();
		session.close();
		return oForm;
	}

	@SuppressWarnings("rawtypes")
	public List listStr(String fieldname) throws Exception {
		List oList = new ArrayList();
		session = StoreHibernateUtil.openSession();
		oList = session.createQuery("select " + fieldname + " from Mdepartment order by " + fieldname).list();
		session.close();
		return oList;
	}

	public void save(Session session, Mdepartment oForm) throws HibernateException, Exception {
		session.saveOrUpdate(oForm);
	}

	public void delete(Session session, Mdepartment oForm) throws HibernateException, Exception {
		session.delete(oForm);
	}

}
