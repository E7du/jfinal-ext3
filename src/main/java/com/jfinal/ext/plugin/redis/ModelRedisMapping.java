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
package com.jfinal.ext.plugin.redis;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.jfinal.kit.StrKit;

public class ModelRedisMapping {
	
	private final Map<String, String> modelToRedisMap = new HashMap<String, String>();
	
	private static ModelRedisMapping me = new ModelRedisMapping();

	public static ModelRedisMapping me() {
		return me;
	}
	
	private List<String> tables = null;
	private List<String> caches = null;
	
	private ModelRedisMapping() {
		this.tables = new ArrayList<String>();
		this.caches = new ArrayList<String>();
	}

	public void put(String tableName, String cacheName) {
		//remove old cachename
		String oldCacheName = this.modelToRedisMap.get(tableName);
		if (StrKit.notBlank(oldCacheName)) {
			this.caches.remove(oldCacheName);
		}
		this.modelToRedisMap.put(tableName, cacheName);
		if (!this.tables.contains(tableName)) {
			this.tables.add(tableName);	
		}
		if (!this.caches.contains(cacheName)) {
			this.caches.add(cacheName);
		}
	}
	
	public String getCacheName(String tableName) {
		return this.modelToRedisMap.get(tableName);
	}
	
	public List<String> getCacheNames() {
		return this.caches;
	}
	
	public List<String> getTableNames() {
		return this.tables;
	}
}
