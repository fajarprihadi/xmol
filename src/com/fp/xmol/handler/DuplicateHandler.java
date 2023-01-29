package com.fp.xmol.handler;

import com.fp.xmol.dao.TexamDAO;
import com.fp.xmol.domain.Texam;

public class DuplicateHandler {

	public boolean examDuplicateChecker(Texam obj) throws Exception {
		boolean isDuplicate = false;
		
		try {
			int count = new TexamDAO().pageCount("tbanksoalfk = " + obj.getTbanksoal().getTbanksoalpk() + " and email = '" + obj.getEmail().toLowerCase().trim() + "'");
			if (count == 0) {
				count = new TexamDAO().pageCount("tbanksoalfk = " + obj.getTbanksoal().getTbanksoalpk() + " and nohp = '" + obj.getNohp() + "'");
			}
			if (count != 0)
				isDuplicate = true; 
		} catch (Exception e) {
			e.printStackTrace();			
		}
		
		return isDuplicate;
	}
}
