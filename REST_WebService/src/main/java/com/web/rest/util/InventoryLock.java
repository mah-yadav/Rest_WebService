package com.web.rest.util;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.locks.ReentrantLock;

import org.springframework.stereotype.Component;

@Component("inventoryLock")
public class InventoryLock {
	private final Map<String, ReentrantLock> map = new HashMap<>();

	public void getLock(String id) {
		ReentrantLock reentrantLock = null;
		synchronized (this) {
			reentrantLock = map.get(id);
			if (reentrantLock == null) {
				reentrantLock = new ReentrantLock(true);
				map.put(id, reentrantLock);
			}
		}

		reentrantLock.lock();
	}

	public void releaseLock(String id) {
		ReentrantLock reentrantLock = map.get(id);
		if (reentrantLock != null) {
			reentrantLock.unlock();
		}
	}
}
