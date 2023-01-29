package com.fp.utils;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class SysUtils {	 
	public static final int PAGESIZE = 20;
	public static final String USERS_PASSWORD_DEFAULT = "123456";
	
	public static final String FILES_ROOT_PATH = "/files";
	public static final String DOC_PATH = "/doc";
	public static final String REPORT_PATH = "/report";
		
	public static String encryptionCommand(String text)
			throws NoSuchAlgorithmException, Exception {
		MessageDigest md5 = MessageDigest.getInstance("MD5");
		byte byteData[] = md5.digest(text.getBytes());

		StringBuffer sb = new StringBuffer();
		for (int i = 0; i < byteData.length; i++) {
			sb.append(Integer.toString((byteData[i] & 0xff) + 0x100, 16)
					.substring(1));
		}		
		return sb.toString();
	}
	
	public static void copyFile(String sourceFileName,String destinationFileName) { 
	      BufferedReader br = null;
	      BufferedWriter bw = null; 

	      try {
	      	br = new BufferedReader(new FileReader( sourceFileName ));
	      	bw = new BufferedWriter(new FileWriter( destinationFileName ));
	        int c;
	        while ((c = br.read()) != -1)  {
	           bw.write(c);
	        }
	        br.close();
	        bw.close();
	      } catch (Exception e) {
	    	  e.printStackTrace();
	      }
	}

}
