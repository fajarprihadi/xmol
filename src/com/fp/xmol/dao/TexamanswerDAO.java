package com.fp.xmol.dao;

import java.util.ArrayList;
import java.util.List;

import org.hibernate.HibernateException;
import org.hibernate.Session;

import com.fp.xmol.domain.Texamanswer;
import com.fp.utils.db.StoreHibernateUtil;

public class TexamanswerDAO {
	private Session session;

	@SuppressWarnings("unchecked")
	public List<Texamanswer> listPaging(int first, int second, String filter, String orderby) throws Exception {		
    	List<Texamanswer> oList = null;
    	if (filter == null || "".equals(filter))
			filter = "0 = 0";
    	session = StoreHibernateUtil.openSession();
    	oList = session.createSQLQuery("select * from Texamanswer "
				+ "where " + filter + " order by " + orderby + " offset " + first +" rows fetch next " + second + " rows only")
				.addEntity(Texamanswer.class).list();		

		session.close();
        return oList;
    }	
	
	public int pageCount(String filter) throws Exception {
		int count = 0;
		if (filter == null || "".equals(filter))
			filter = "0 = 0";
		session = StoreHibernateUtil.openSession();
		count = Integer.parseInt((String) session.createSQLQuery("select count(*) from Texamanswer "
				+ "where " + filter).uniqueResult().toString());
		session.close();
        return count;
    }
	
	@SuppressWarnings("unchecked")
	public List<Texamanswer> listByFilter(String filter, String orderby) throws Exception {		
    	List<Texamanswer> oList = null;
    	if (filter == null || "".equals(filter))
			filter = "0 = 0";
    	session = StoreHibernateUtil.openSession();
		oList = session.createQuery("from Texamanswer where " + filter + " order by " + orderby).list();
		session.close();
        return oList;
    }	
	
	public Texamanswer findByPk(Integer pk) throws Exception {
		session = StoreHibernateUtil.openSession();
		Texamanswer oForm = (Texamanswer) session.createQuery("from Texamanswer where Texamanswerpk = " + pk).uniqueResult();
		session.close();
		return oForm;
	}
	
	public Texamanswer findByFilter(String filter) throws Exception {
		session = StoreHibernateUtil.openSession();
		Texamanswer oForm = (Texamanswer) session.createQuery("from Texamanswer where " + filter).uniqueResult();
		session.close();
		return oForm;
	}
	
	@SuppressWarnings("rawtypes")
	public List listStr(String fieldname) throws Exception {
		List oList = new ArrayList();
       	session = StoreHibernateUtil.openSession();
       	oList = session.createQuery("select " + fieldname + " from Texamanswer order by " + fieldname).list();   
        session.close();
        return oList;
	}
		
	public void save(Session session, Texamanswer oForm) throws HibernateException, Exception {
		session.saveOrUpdate(oForm);
	}
	
	public void delete(Session session, Texamanswer oForm) throws HibernateException, Exception {
		session.delete(oForm);    
    }
	
}
