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

public interface CallbackListener {

	/**
	 * Call before Save
	 * @param m
	 */
	void beforeSave(ModelExt<?> m);

	/**
	 * Call after Save
	 * @param m
	 */
	void afterSave(ModelExt<?> m);
	
	/**
	 * Call before Update
	 * @param m
	 */
	void beforeUpdate(ModelExt<?> m);

	/**
	 * Call after Update
	 * @param m
	 */
	void afterUpdate(ModelExt<?> m);

	/**
	 * Call before Delete
	 * @param m
	 */
	void beforeDelete(ModelExt<?> m);

	/**
	 * Call after Delete
	 * @param m
	 */
	void afterDelete(ModelExt<?> m);
}
