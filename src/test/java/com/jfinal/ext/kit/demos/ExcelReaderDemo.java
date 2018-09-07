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
package com.jfinal.ext.kit.demos;

import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.jfinal.ext.kit.AppDateFormatKit;
import com.jfinal.plugin.activerecord.Model;
import com.test.api.model.User;

import cn.zhucongqi.excel.ExcelReader;
import cn.zhucongqi.excel.metadata.ExcelColumn;
import cn.zhucongqi.excel.metadata.ExcelReadRule;
import cn.zhucongqi.excel.metadata.ColumnType;
import cn.zhucongqi.excel.read.event.JFModelReadListener;

class ExcelReaderDemo {

	@BeforeEach
	void setUp() throws Exception {
	}

	@Test
	void test() {
		
//		StandaloneAppConfig.start();
		
		JFModelReadListener listener = new JFModelReadListener() {
			
			@Override
			public void readRow(Model<?> model) {
				System.out.println("read2 model="+model+"class"+model.getClass());
//				ModelExt<?> t = (ModelExt<?>)model;
//				t.save();
			}

			@Override
			public ExcelReadRule rule() {
				ExcelReadRule rule = new ExcelReadRule();
				
				rule.setHasHeader(1, true);
				rule.setHasHeader(2, true);

				ExcelColumn id = ExcelColumn.one("id", ColumnType.INTEGER);
				ExcelColumn name = ExcelColumn.one("name", ColumnType.STRING);
				ExcelColumn addr = ExcelColumn.one("addr", ColumnType.STRING);
				rule.alignColumn(name, id, addr);
				return rule;
			}

			@Override
			public String dateFormat() {
				return AppDateFormatKit.getAppDateFormat();
			}
		};
		
		ExcelReader reader = new ExcelReader("userswrite.xls", User.class, listener);
		
		reader.read();
		
		List<User> users = listener.getDatas();
		System.out.println(users);
	}

}
