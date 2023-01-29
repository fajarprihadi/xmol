package com.fp.xmol.dao;

import java.util.ArrayList;
import java.util.List;

import org.hibernate.HibernateException;
import org.hibernate.Session;

import com.fp.xmol.domain.Tbanksoal;
import com.fp.utils.db.StoreHibernateUtil;

public class TbanksoalDAO {
	private Session session;

	@SuppressWarnings("unchecked")
	public List<Tbanksoal> listPaging(int first, int second, String filter, String orderby) throws Exception {		
    	List<Tbanksoal> oList = null;
    	if (filter == null || "".equals(filter))
			filter = "0 = 0";
    	session = StoreHibernateUtil.openSession();
    	oList = session.createSQLQuery("select * from Tbanksoal "
				+ "where " + filter + " order by " + orderby + " offset " + first +" rows fetch next " + second + " rows only")
				.addEntity(Tbanksoal.class).list();		

		session.close();
        return oList;
    }	
	
	public int pageCount(String filter) throws Exception {
		int count = 0;
		if (filter == null || "".equals(filter))
			filter = "0 = 0";
		session = StoreHibernateUtil.openSession();
		count = Integer.parseInt((String) session.createSQLQuery("select count(*) from Tbanksoal "
				+ "where " + filter).uniqueResult().toString());
		session.close();
        return count;
    }
	
	@SuppressWarnings("unchecked")
	public List<Tbanksoal> listByFilter(String filter, String orderby) throws Exception {		
    	List<Tbanksoal> oList = null;
    	if (filter == null || "".equals(filter))
			filter = "0 = 0";
    	session = StoreHibernateUtil.openSession();
		oList = session.createQuery("from Tbanksoal where " + filter + " order by " + orderby).list();
		session.close();
        return oList;
    }	
	
	public Tbanksoal findByPk(Integer pk) throws Exception {
		session = StoreHibernateUtil.openSession();
		Tbanksoal oForm = (Tbanksoal) session.createQuery("from Tbanksoal where tbanksoalpk = " + pk).uniqueResult();
		session.close();
		return oForm;
	}
	
	public Tbanksoal findById(String id) throws Exception {
		session = StoreHibernateUtil.openSession();
		Tbanksoal oForm = (Tbanksoal) session.createQuery("from Tbanksoal where linkid = '" + id + "'").uniqueResult();
		session.close();
		return oForm;
	}
	
	@SuppressWarnings("rawtypes")
	public List listStr(String fieldname) throws Exception {
		List oList = new ArrayList();
       	session = StoreHibernateUtil.openSession();
       	oList = session.createQuery("select " + fieldname + " from Tbanksoal order by " + fieldname).list();   
        session.close();
        return oList;
	}
		
	public void save(Session session, Tbanksoal oForm) throws HibernateException, Exception {
		session.saveOrUpdate(oForm);
	}
	
	public void delete(Session session, Tbanksoal oForm) throws HibernateException, Exception {
		session.delete(oForm);    
    }
	
}
