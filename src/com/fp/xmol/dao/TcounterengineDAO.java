package com.fp.xmol.dao;

import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.Random;

import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.Transaction;

import com.fp.utils.db.StoreHibernateUtil;

public class TcounterengineDAO {
	
	private Session session;	
	private Transaction transaction;
	
	public String generateCounter(String prefix) throws Exception {
		Integer lastCounter = 0;
		String strCounter = "";
		String finalCounter = "";
		int len = 5;
		char[] fillUploadid = new char[len];
		
		try {
			String counterName = prefix + new SimpleDateFormat("YY").format(new Date());
			session = StoreHibernateUtil.openSession();
    		transaction = session.beginTransaction();
			Query q = session.createQuery("select lastcounter from Tcounterengine where countername = '" + counterName + "'");
			lastCounter = (Integer) q.uniqueResult();
			if (lastCounter != null) {
				lastCounter++;
				session.createSQLQuery("update Tcounterengine set lastcounter = lastcounter + 1 where countername = '" + counterName + "'").executeUpdate();				
			} else {
				lastCounter = 1;
				session.createSQLQuery("insert into Tcounterengine values ('" + counterName + "', " + lastCounter + ")").executeUpdate();
			}			
			transaction.commit();
			session.close();
			Arrays.fill(fillUploadid, '0');
			strCounter = new String(fillUploadid) + lastCounter;
			finalCounter = counterName + strCounter.substring(strCounter.length()-len, strCounter.length());			
		} catch (Exception e) {
			transaction.rollback();
			e.printStackTrace();
		}		
		return finalCounter;
	}
	
}
