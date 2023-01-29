package com.fp.xmol.dao;

import java.util.ArrayList;
import java.util.List;

import org.hibernate.HibernateException;
import org.hibernate.Session;

import com.fp.xmol.domain.Texammemo;
import com.fp.utils.db.StoreHibernateUtil;

public class TexammemoDAO {
	private Session session;

	@SuppressWarnings("unchecked")
	public List<Texammemo> listPaging(int first, int second, String filter, String orderby) throws Exception {		
    	List<Texammemo> oList = null;
    	if (filter == null || "".equals(filter))
			filter = "0 = 0";
    	session = StoreHibernateUtil.openSession();
    	oList = session.createSQLQuery("select * from Texammemo join Tbanksoal on tbanksoalfk = tbanksoalpk "
				+ "where " + filter + " order by " + orderby + " offset " + first +" rows fetch next " + second + " rows only")
				.addEntity(Texammemo.class).list();		

		session.close();
        return oList;
    }	
	
	public int pageCount(String filter) throws Exception {
		int count = 0;
		if (filter == null || "".equals(filter))
			filter = "0 = 0";
		session = StoreHibernateUtil.openSession();
		count = Integer.parseInt((String) session.createSQLQuery("select count(*) from Texammemo join Tbanksoal on tbanksoalfk = tbanksoalpk "
				+ "where " + filter).uniqueResult().toString());
		session.close();
        return count;
    }
	
	@SuppressWarnings("unchecked")
	public List<Texammemo> listByFilter(String filter, String orderby) throws Exception {		
    	List<Texammemo> oList = null;
    	if (filter == null || "".equals(filter))
			filter = "0 = 0";
    	session = StoreHibernateUtil.openSession();
		oList = session.createQuery("from Texammemo where " + filter + " order by " + orderby).list();
		session.close();
        return oList;
    }	
	
	public Texammemo findByPk(Integer pk) throws Exception {
		session = StoreHibernateUtil.openSession();
		Texammemo oForm = (Texammemo) session.createQuery("from Texammemo where Texammemopk = " + pk).uniqueResult();
		session.close();
		return oForm;
	}
	
	@SuppressWarnings("rawtypes")
	public List listStr(String fieldname) throws Exception {
		List oList = new ArrayList();
       	session = StoreHibernateUtil.openSession();
       	oList = session.createQuery("select " + fieldname + " from Texammemo order by " + fieldname).list();   
        session.close();
        return oList;
	}
		
	public void save(Session session, Texammemo oForm) throws HibernateException, Exception {
		session.saveOrUpdate(oForm);
	}
	
	public void delete(Session session, Texammemo oForm) throws HibernateException, Exception {
		session.delete(oForm);    
    }
	
}
