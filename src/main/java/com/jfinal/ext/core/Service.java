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
package com.jfinal.ext.core;

import java.util.HashMap;
import java.util.Map;

import com.jfinal.log.Log;

/**
 * @author 朱丛启 2015年5月6日 下午1:39:48
 *
 */
@SuppressWarnings("unchecked")
public abstract class Service {

	private static Map<Class<? extends Service>, Service> INSTANCE_MAP = new HashMap<Class<? extends Service>, Service>();
    protected ControllerExt controller;
    protected Log log = Log.getLog(this.getClass());
    
	public static <Ser extends Service> Ser getInstance(Class<Ser> clazz, ControllerExt controller) {
		Ser service = (Ser) INSTANCE_MAP.get(clazz);
		if (service == null) {
			try {
				service = clazz.newInstance();
				INSTANCE_MAP.put(clazz, service);
			} catch (InstantiationException e) {
			} catch (IllegalAccessException e) {
			}
		}
		service.controller = controller;
		return service;
	}
}
