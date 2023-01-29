package com.fp.xmol.dao;

import java.util.ArrayList;
import java.util.List;

import org.hibernate.HibernateException;
import org.hibernate.Session;

import com.fp.xmol.domain.Texam;
import com.fp.utils.db.StoreHibernateUtil;

public class TexamDAO {
	private Session session;

	@SuppressWarnings("unchecked")
	public List<Texam> listPaging(int first, int second, String filter, String orderby) throws Exception {		
    	List<Texam> oList = null;
    	if (filter == null || "".equals(filter))
			filter = "0 = 0";
    	session = StoreHibernateUtil.openSession();
    	oList = session.createSQLQuery("select * from Texam join Tbanksoal on tbanksoalfk = tbanksoalpk "
				+ "where " + filter + " order by " + orderby + " offset " + first +" rows fetch next " + second + " rows only")
				.addEntity(Texam.class).list();		

		session.close();
        return oList;
    }	
	
	public int pageCount(String filter) throws Exception {
		int count = 0;
		if (filter == null || "".equals(filter))
			filter = "0 = 0";
		session = StoreHibernateUtil.openSession();
		count = Integer.parseInt((String) session.createSQLQuery("select count(*) from Texam join Tbanksoal on tbanksoalfk = tbanksoalpk "
				+ "where " + filter).uniqueResult().toString());
		session.close();
        return count;
    }
	
	@SuppressWarnings("unchecked")
	public List<Texam> listByFilter(String filter, String orderby) throws Exception {		
    	List<Texam> oList = null;
    	if (filter == null || "".equals(filter))
			filter = "0 = 0";
    	session = StoreHibernateUtil.openSession();
		oList = session.createQuery("from Texam where " + filter + " order by " + orderby).list();
		session.close();
        return oList;
    }	
	
	public Texam findByPk(Integer pk) throws Exception {
		session = StoreHibernateUtil.openSession();
		Texam oForm = (Texam) session.createQuery("from Texam where Texampk = " + pk).uniqueResult();
		session.close();
		return oForm;
	}
	
	@SuppressWarnings("rawtypes")
	public List listStr(String fieldname) throws Exception {
		List oList = new ArrayList();
       	session = StoreHibernateUtil.openSession();
       	oList = session.createQuery("select " + fieldname + " from Texam order by " + fieldname).list();   
        session.close();
        return oList;
	}
		
	public void save(Session session, Texam oForm) throws HibernateException, Exception {
		session.saveOrUpdate(oForm);
	}
	
	public void delete(Session session, Texam oForm) throws HibernateException, Exception {
		session.delete(oForm);    
    }
	
}
