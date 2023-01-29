package com.fp.xmol.dao;

import java.util.ArrayList;
import java.util.List;

import org.hibernate.HibernateException;
import org.hibernate.Session;

import com.fp.xmol.domain.Tquestanswer;
import com.fp.utils.db.StoreHibernateUtil;

public class TquestanswerDAO {
	private Session session;

	@SuppressWarnings("unchecked")
	public List<Tquestanswer> listPaging(int first, int second, String filter, String orderby) throws Exception {		
    	List<Tquestanswer> oList = null;
    	if (filter == null || "".equals(filter))
			filter = "0 = 0";
    	session = StoreHibernateUtil.openSession();
    	oList = session.createSQLQuery("select * from Tquestanswer "
				+ "where " + filter + " order by " + orderby + " offset " + first +" rows fetch next " + second + " rows only")
				.addEntity(Tquestanswer.class).list();		

		session.close();
        return oList;
    }	
	
	public int pageCount(String filter) throws Exception {
		int count = 0;
		if (filter == null || "".equals(filter))
			filter = "0 = 0";
		session = StoreHibernateUtil.openSession();
		count = Integer.parseInt((String) session.createSQLQuery("select count(*) from Tquestanswer "
				+ "where " + filter).uniqueResult().toString());
		session.close();
        return count;
    }
	
	@SuppressWarnings("unchecked")
	public List<Tquestanswer> listByFilter(String filter, String orderby) throws Exception {		
    	List<Tquestanswer> oList = null;
    	if (filter == null || "".equals(filter))
			filter = "0 = 0";
    	session = StoreHibernateUtil.openSession();
		oList = session.createQuery("from Tquestanswer where " + filter + " order by " + orderby).list();
		session.close();
        return oList;
    }	
	
	public Tquestanswer findByPk(Integer pk) throws Exception {
		session = StoreHibernateUtil.openSession();
		Tquestanswer oForm = (Tquestanswer) session.createQuery("from Tquestanswer where Tquestanswerpk = " + pk).uniqueResult();
		session.close();
		return oForm;
	}
	
	@SuppressWarnings("rawtypes")
	public List listStr(String fieldname) throws Exception {
		List oList = new ArrayList();
       	session = StoreHibernateUtil.openSession();
       	oList = session.createQuery("select " + fieldname + " from Tquestanswer order by " + fieldname).list();   
        session.close();
        return oList;
	}
		
	public void save(Session session, Tquestanswer oForm) throws HibernateException, Exception {
		session.saveOrUpdate(oForm);
	}
	
	public void delete(Session session, Tquestanswer oForm) throws HibernateException, Exception {
		session.delete(oForm);    
    }
	
	public void deleteByFk(Session session, Integer fk) throws HibernateException, Exception {
		session.createSQLQuery("delete from Tquestanswer where tquestfk = " + fk).executeUpdate();    
    }
	
}
