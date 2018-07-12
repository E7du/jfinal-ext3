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
package com.jfinal.ext.kit;

import com.jfinal.plugin.activerecord.Model;
import com.jfinal.plugin.activerecord.Table;
import com.jfinal.plugin.activerecord.TableMapping;

/**
 * ModelKit.java
 * @author Jobsz
 *
 */
@SuppressWarnings("rawtypes")
public final class ModelKit {

	public static Table getTable(Class<? extends Model> modelClass) {
		return TableMapping.me().getTable(modelClass);
	}
	
	public static String[] getPrimarykeys(Class<? extends Model> modelClass) {
		return ModelKit.getTable(modelClass).getPrimaryKey();
	}
	
	public static String getPrimaryKey(Class<? extends Model> modelClass) {
		String[] primaryKeys = ModelKit.getPrimarykeys(modelClass);
		if (primaryKeys.length >= 1) {
			return primaryKeys[0];
		}
		return "";
	}
	
}
