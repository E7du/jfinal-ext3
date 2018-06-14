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
package com.jfinal.ext.plugin.druid;

import java.sql.SQLException;

import com.alibaba.druid.filter.config.ConfigTools;
import com.alibaba.druid.pool.DruidDataSource;
import com.jfinal.plugin.druid.DruidPlugin;

/**
 *  生成加密密码:
 *  java -cp druid-xx.jar com.alibaba.druid.filter.config.ConfigTools your_password
 * @author Jobsz
 */
public class DruidEncryptPlugin extends DruidPlugin {

	/**
	 * @param url
	 * @param username
	 * @param password
	 */
	public DruidEncryptPlugin(String url, String username, String password) {
		super(url, username, password);
	}
	
	/**
	 * @param url
	 * @param username
	 * @param password
	 * @param driverClass
	 */
	public DruidEncryptPlugin(String url, String username, String password, String driverClass) {
		super(url, username, password, driverClass);
	}

	/**
	 * @param url
	 * @param username
	 * @param password
	 * @param driverClass
	 * @param filters
	 */
	public DruidEncryptPlugin(String url, String username, String password, String driverClass, String filters) {
		super(url, username, password, driverClass, filters);
	}
	
	/**
	 * decryptedPassword with publich key
	 * @param publicKey
	 * @param password
	 * @return
	 */
	public static String decryptedPassword(String publicKey, String password) {
		try {
			return ConfigTools.decrypt(publicKey, password);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return password;
	}

	@Override
	public boolean start() {
		boolean ret = super.start();
		DruidDataSource ds = (DruidDataSource) this.getDataSource();
		//ds.setConnectionProperties("config.decrypt=true");
		try {
			ds.setFilters("config");
		} catch (SQLException e1) {
		}
		return ret;
	}
}
