/*
 * Copyright 2018 Jobsz (zcq@zhucongqi.cn)
 * 
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License.  You may obtain a copy
 * of the License at
 * 
 *   http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  See the
 * License for the specific language governing permissions and limitations under
 * the License.
*/
package com.jfinal.ext.plugin.activerecord;

import java.util.concurrent.ConcurrentHashMap;

import com.jfinal.kit.StrKit;
import com.jfinal.plugin.redis.Cache;
import com.jfinal.plugin.redis.Redis;

public final class GlobalSyncRedis {
	
	private static boolean sync = true;
	
	private static ConcurrentHashMap<String, Cache> caches = new ConcurrentHashMap<String, Cache>();
	
	/**
	 * default 30mins
	 */
	private static Integer syncExpire = 1800;
	
	public static void openSync() {
		GlobalSyncRedis.sync = true;
	}
	
	public static void closeSync() {
		GlobalSyncRedis.sync = false;
	}
	
	public static boolean syncState() {
		return GlobalSyncRedis.sync;
	}
	
	/**
	 * Set default expire time
	 * @param expire
	 */
	public static void setSyncExpire(Integer expire) {
		GlobalSyncRedis.syncExpire = expire;
	}
	
	/**
	 * Get default expire time
	 * @return
	 */
	public static Integer syncExpire() {
		return GlobalSyncRedis.syncExpire;
	}
	
	/**
	 * Set cache to the memory.
	 * @param name
	 * @param cache
	 */
	public static void setSyncCache(String name, Cache cache) {
		if (null == name || "".equals(name.trim()) || null == cache) {
			return;
		}
		GlobalSyncRedis.caches.put(name, cache);
	}
	
	/**
	 * remove cache from memory.
	 * @param name
	 */
	public static void removeSyncCache(String name) {
		if (StrKit.isBlank(name)) {
			return;
		}
		if (GlobalSyncRedis.caches.containsKey(name)) {
			GlobalSyncRedis.caches.remove(name);
		}
	}
	
	/**
	 * Get cache from memory.
	 * @param name
	 * @return
	 */
	public static Cache getSyncCache(String name) {
		if (StrKit.isBlank(name) || !GlobalSyncRedis.caches.containsKey(name)) {
			return Redis.use();
		}
		return GlobalSyncRedis.caches.get(name);
	}
}
