package com.fp.xmol.dao;

import java.util.ArrayList;
import java.util.List;

import org.hibernate.HibernateException;
import org.hibernate.Session;

import com.fp.xmol.domain.Tquest;
import com.fp.utils.db.StoreHibernateUtil;

public class TquestDAO {
	private Session session;

	@SuppressWarnings("unchecked")
	public List<Tquest> listPaging(int first, int second, String filter, String orderby) throws Exception {		
    	List<Tquest> oList = null;
    	if (filter == null || "".equals(filter))
			filter = "0 = 0";
    	session = StoreHibernateUtil.openSession();
    	oList = session.createSQLQuery("select * from Tquest "
				+ "where " + filter + " order by " + orderby + " offset " + first +" rows fetch next " + second + " rows only")
				.addEntity(Tquest.class).list();		

		session.close();
        return oList;
    }	
	
	public int pageCount(String filter) throws Exception {
		int count = 0;
		if (filter == null || "".equals(filter))
			filter = "0 = 0";
		session = StoreHibernateUtil.openSession();
		count = Integer.parseInt((String) session.createSQLQuery("select count(*) from Tquest "
				+ "where " + filter).uniqueResult().toString());
		session.close();
        return count;
    }
	
	@SuppressWarnings("unchecked")
	public List<Tquest> listByFilter(String filter, String orderby) throws Exception {		
    	List<Tquest> oList = null;
    	if (filter == null || "".equals(filter))
			filter = "0 = 0";
    	session = StoreHibernateUtil.openSession();
		oList = session.createQuery("from Tquest where " + filter + " order by " + orderby).list();
		session.close();
        return oList;
    }	
	
	public Tquest findByPk(Integer pk) throws Exception {
		session = StoreHibernateUtil.openSession();
		Tquest oForm = (Tquest) session.createQuery("from Tquest where Tquestpk = " + pk).uniqueResult();
		session.close();
		return oForm;
	}
	
	@SuppressWarnings("rawtypes")
	public List listStr(String fieldname) throws Exception {
		List oList = new ArrayList();
       	session = StoreHibernateUtil.openSession();
       	oList = session.createQuery("select " + fieldname + " from Tquest order by " + fieldname).list();   
        session.close();
        return oList;
	}
		
	public void save(Session session, Tquest oForm) throws HibernateException, Exception {
		session.saveOrUpdate(oForm);
	}
	
	public void delete(Session session, Tquest oForm) throws HibernateException, Exception {
		session.delete(oForm);    
    }
	
}
