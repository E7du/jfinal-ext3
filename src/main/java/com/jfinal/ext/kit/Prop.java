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

import java.lang.reflect.Field;
import java.util.Properties;

import com.jfinal.log.Log;

public class Prop extends com.jfinal.kit.Prop {

	private Log LOG = Log.getLog(Prop.class);
	
	@SuppressWarnings({"rawtypes" })	
	public Prop(Properties prop) {
		super("com/jfinal/ext/kit/lazy.jf");
		
		Field[] fields = com.jfinal.kit.Prop.class.getDeclaredFields();
		for (int i = 0; i < fields.length; i++) {
			Field field = fields[i];
			Class clazz = field.getType();
			if (Properties.class.isAssignableFrom(clazz)) {
				try {
					if (!field.isAccessible()) {
						field.setAccessible(true);
					}
					field.set(this, prop);
				} catch (IllegalArgumentException | IllegalAccessException e) {
					e.printStackTrace();
					LOG.error(e.getLocalizedMessage());
				}
			}
		}
	}
}
