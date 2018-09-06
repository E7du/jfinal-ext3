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

public final class GlobalSyncRedis {
	
	private static boolean sync = true;
	
	private static Integer syncExpire;
	
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
}
