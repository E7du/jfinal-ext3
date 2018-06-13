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
package com.jfinal.ext.plugin.activerecord.tx;

import java.sql.SQLException;

import com.jfinal.aop.Interceptor;
import com.jfinal.aop.Invocation;
import com.jfinal.ext.core.ControllerExt;
import com.jfinal.plugin.activerecord.Db;
import com.jfinal.plugin.activerecord.IAtom;

/**
 * 事务处理Interceptor
 * @author Jobsz
 */
public class DbTxInterceptor implements Interceptor {

	@Override
	public void intercept(final Invocation inv) {
		Db.tx(new IAtom() {
			@Override
			public boolean run() throws SQLException {
				try {
					inv.invoke();
				} catch (Exception e) {
					if (inv.getTarget() instanceof ControllerExt) {
						ControllerExt controller = inv.getTarget();
						controller.onExceptionError(e);
					}
					return false;
				}
				return true;
			}
		});
	}

}
