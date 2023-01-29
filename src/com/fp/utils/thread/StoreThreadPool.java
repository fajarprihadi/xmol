package com.fp.utils.thread;

import org.zkoss.lang.Library;

import com.fp.xmol.handler.ThreadPool;

public class StoreThreadPool {
	
	private static final ThreadPool pool;
	
	static {
		try {
			pool = new ThreadPool(Integer.parseInt(Library.getProperty("maxThreadPool")));
		} catch (Throwable ex) {
            System.err.println("Initial ThreadPool creation failed." + ex);
            throw new ExceptionInInitializerError(ex);
        }
	}
		
	public static ThreadPool getPool() {
		return pool;
	} 
	
	public static void addTask(Runnable r) {
        pool.addTask(r);
    }
}
