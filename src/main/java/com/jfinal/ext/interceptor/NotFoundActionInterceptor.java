/**
 * Copyright (c) 2018, Jobsz (zcq@zhucongqi.cn).
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.jfinal.ext.interceptor;

import java.lang.reflect.Method;

import com.jfinal.aop.Interceptor;
import com.jfinal.aop.Invocation;
import com.jfinal.core.Controller;

/**
 * 找不到Action
 * @author Jobsz
 */
public class NotFoundActionInterceptor implements Interceptor {

	/**
	 * 参数处理
	 * 1. 参数为空时直接调用
	 * 2. 参数不为空时先判断是否使用？间隔，使用了？间隔先split一下参数，拿到？之前的参数，查找是否有这个action。没有就404，有就直接invoke
	 */
	@Override
	public void intercept(Invocation ai) {
		// 获取controller
		Controller controller = ai.getController();
		// 获取controller 的参数
		String param = controller.getPara();
		if (param == null) {
			ai.invoke();
		} else {
			String[] params = null;
			if (param.contains("?")) {
				params = param.split("?");
			}
			if (params != null) {
				param = params[0];
			}
			
			boolean contained = false;
			Method[] methods = controller.getClass().getMethods();
			for (Method method : methods) {
				if (param.equals(method.getName())) {
					contained = true;
					break;
				}
			}
			if (!contained){
				controller.renderError(404);
			} 
			ai.invoke();
		}
	}
}
