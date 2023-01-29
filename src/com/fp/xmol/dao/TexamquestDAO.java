package com.fp.xmol.dao;

import java.util.ArrayList;
import java.util.List;

import org.hibernate.HibernateException;
import org.hibernate.Session;

import com.fp.xmol.domain.Texamquest;
import com.fp.utils.db.StoreHibernateUtil;

public class TexamquestDAO {
	private Session session;

	@SuppressWarnings("unchecked")
	public List<Texamquest> listPaging(int first, int second, String filter, String orderby) throws Exception {		
    	List<Texamquest> oList = null;
    	if (filter == null || "".equals(filter))
			filter = "0 = 0";
    	session = StoreHibernateUtil.openSession();
    	oList = session.createSQLQuery("select * from Texamquest "
				+ "where " + filter + " order by " + orderby + " offset " + first +" rows fetch next " + second + " rows only")
				.addEntity(Texamquest.class).list();		

		session.close();
        return oList;
    }	
	
	public int pageCount(String filter) throws Exception {
		int count = 0;
		if (filter == null || "".equals(filter))
			filter = "0 = 0";
		session = StoreHibernateUtil.openSession();
		count = Integer.parseInt((String) session.createSQLQuery("select count(*) from Texamquest "
				+ "where " + filter).uniqueResult().toString());
		session.close();
        return count;
    }
	
	@SuppressWarnings("unchecked")
	public List<Texamquest> listByFilter(String filter, String orderby) throws Exception {		
    	List<Texamquest> oList = null;
    	if (filter == null || "".equals(filter))
			filter = "0 = 0";
    	session = StoreHibernateUtil.openSession();
		oList = session.createQuery("from Texamquest where " + filter + " order by " + orderby).list();
		session.close();
        return oList;
    }	
	
	public Texamquest findByPk(Integer pk) throws Exception {
		session = StoreHibernateUtil.openSession();
		Texamquest oForm = (Texamquest) session.createQuery("from Texamquest where Texamquestpk = " + pk).uniqueResult();
		session.close();
		return oForm;
	}
	
	@SuppressWarnings("rawtypes")
	public List listStr(String fieldname) throws Exception {
		List oList = new ArrayList();
       	session = StoreHibernateUtil.openSession();
       	oList = session.createQuery("select " + fieldname + " from Texamquest order by " + fieldname).list();   
        session.close();
        return oList;
	}
		
	public void save(Session session, Texamquest oForm) throws HibernateException, Exception {
		session.saveOrUpdate(oForm);
	}
	
	public void delete(Session session, Texamquest oForm) throws HibernateException, Exception {
		session.delete(oForm);    
    }
	
}
