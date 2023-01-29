package com.fp.utils.thread;

import org.zkoss.zk.ui.WebApp;
import org.zkoss.zk.ui.util.WebAppCleanup;
import org.zkoss.zk.ui.util.WebAppInit;

public class ThreadPoolListeners implements WebAppInit, WebAppCleanup  {

	@Override
	public void init(WebApp wapp) throws Exception {		
		StoreThreadPool.getPool();
	}
	
	@Override
	public void cleanup(WebApp wapp) throws Exception {
		
	}	

}
