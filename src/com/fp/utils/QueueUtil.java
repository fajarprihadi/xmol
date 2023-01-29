package com.fp.utils;

import org.zkoss.zk.ui.WebApp;
import org.zkoss.zk.ui.event.EventQueue;
import org.zkoss.zk.ui.event.EventQueues;

public class QueueUtil {
	
	//look up the desktop queue to communicate with another ui controller
	public static EventQueue<QueueMessage> lookupQueue(String queuename, WebApp webapp) {
		EventQueue<QueueMessage> queue = EventQueues.lookup(queuename, webapp, true);
		return queue;
	}
	
}
