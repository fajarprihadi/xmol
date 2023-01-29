package com.fp.xmol.dao;

import java.util.ArrayList;
import java.util.List;

import org.hibernate.HibernateException;
import org.hibernate.Session;

import com.fp.xmol.domain.Texamdoc;
import com.fp.utils.db.StoreHibernateUtil;

public class TexamdocDAO {
	private Session session;

	@SuppressWarnings("unchecked")
	public List<Texamdoc> listPaging(int first, int second, String filter, String orderby) throws Exception {		
    	List<Texamdoc> oList = null;
    	if (filter == null || "".equals(filter))
			filter = "0 = 0";
    	session = StoreHibernateUtil.openSession();
    	oList = session.createSQLQuery("select * from Texamdoc join Tbanksoal on tbanksoalfk = tbanksoalpk "
				+ "where " + filter + " order by " + orderby + " offset " + first +" rows fetch next " + second + " rows only")
				.addEntity(Texamdoc.class).list();		

		session.close();
        return oList;
    }	
	
	public int pageCount(String filter) throws Exception {
		int count = 0;
		if (filter == null || "".equals(filter))
			filter = "0 = 0";
		session = StoreHibernateUtil.openSession();
		count = Integer.parseInt((String) session.createSQLQuery("select count(*) from Texamdoc join Tbanksoal on tbanksoalfk = tbanksoalpk "
				+ "where " + filter).uniqueResult().toString());
		session.close();
        return count;
    }
	
	@SuppressWarnings("unchecked")
	public List<Texamdoc> listByFilter(String filter, String orderby) throws Exception {		
    	List<Texamdoc> oList = null;
    	if (filter == null || "".equals(filter))
			filter = "0 = 0";
    	session = StoreHibernateUtil.openSession();
		oList = session.createQuery("from Texamdoc where " + filter + " order by " + orderby).list();
		session.close();
        return oList;
    }	
	
	public Texamdoc findByPk(Integer pk) throws Exception {
		session = StoreHibernateUtil.openSession();
		Texamdoc oForm = (Texamdoc) session.createQuery("from Texamdoc where Texamdocpk = " + pk).uniqueResult();
		session.close();
		return oForm;
	}
	
	@SuppressWarnings("rawtypes")
	public List listStr(String fieldname) throws Exception {
		List oList = new ArrayList();
       	session = StoreHibernateUtil.openSession();
       	oList = session.createQuery("select " + fieldname + " from Texamdoc order by " + fieldname).list();   
        session.close();
        return oList;
	}
		
	public void save(Session session, Texamdoc oForm) throws HibernateException, Exception {
		session.saveOrUpdate(oForm);
	}
	
	public void delete(Session session, Texamdoc oForm) throws HibernateException, Exception {
		session.delete(oForm);    
    }
	
}
