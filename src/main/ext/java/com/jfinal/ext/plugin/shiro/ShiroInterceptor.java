/**
 * Copyright (c) 2011-2013, dafei 李飞 (myaniu AT gmail DOT com)
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
package com.jfinal.ext.plugin.shiro;

import org.apache.shiro.authz.AuthorizationException;
import org.apache.shiro.authz.UnauthenticatedException;

import com.jfinal.aop.Interceptor;
import com.jfinal.aop.Invocation;

public class ShiroInterceptor implements Interceptor {

	@Override
    public void intercept(Invocation ai) {
		AuthzHandler ah = ShiroKit.getAuthzHandler(ai.getActionKey());
		// 存在访问控制处理器。
		if (ah != null) {
			try {
				// 执行权限检查。
				ah.assertAuthorized();
			} catch (UnauthenticatedException lae) {
				// RequiresGuest，RequiresAuthentication，RequiresUser，未满足时，抛出未经授权的异常。
				// 如果没有进行身份验证，返回HTTP401状态码
				ai.getController().renderError(401);
				return;
			} catch (AuthorizationException ae) {
				// RequiresRoles，RequiresPermissions授权异常
				// 如果没有权限访问对应的资源，返回HTTP状态码403。
				ai.getController().renderError(403);
				return;
			} catch (Exception e) {
				// 出现了异常，应该是没有登录。
				ai.getController().renderError(401);
				return;
			}
		}
		// 执行正常逻辑
		ai.invoke();
	}
}
