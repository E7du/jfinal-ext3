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

import com.jfinal.kit.StrKit;
import com.jfinal.plugin.IPlugin;

public class ModelRedisPlugin implements IPlugin {
	
	private String cacheName = null;
	private String[] tables = null;
	
	/**
	 * cacheName,
	 * tables: tablename1,tablename2,tablename3...
	 */
	public ModelRedisPlugin(String cacheName, String tables) {
		if (StrKit.isBlank(cacheName)) {
			throw new IllegalArgumentException("cacheName can not be blank.");
		}
		if (StrKit.isBlank(tables)) {
			throw new IllegalArgumentException("tables can not be blank.");
		}
		
		this.cacheName = cacheName.trim();
		this.tables = tables.split(",");
	}

	@Override
	public boolean start() {
		ModelRedisMapping me = ModelRedisMapping.me();
		for (int i = 0; i < this.tables.length; i++) {
			me.put(this.tables[i].trim(), this.cacheName);
		}
		return true;
	}

	@Override
	public boolean stop() {
		return true;
	}
}
