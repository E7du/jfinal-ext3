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

import com.jfinal.plugin.activerecord.Model;
import com.test.api.model.User;

import cn.zhucongqi.excel.Reader;
import cn.zhucongqi.excel.metadata.ColumnType;
import cn.zhucongqi.excel.metadata.Rule;
import cn.zhucongqi.excel.metadata.Column;
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
			public Rule rule() {
				Rule rule = new Rule();
				Column id = Column.one("id", ColumnType.INTEGER);
				Column name = Column.one("name", ColumnType.STRING);
				Column addr = Column.one("addr", ColumnType.STRING);
				rule.alignColumn(name, id, addr);
				return rule;
			}

		};
		
		Reader reader = new Reader("userswrite.xls", User.class, listener);
		
		reader.read();
		
		List<User> users = listener.getDatas();
		System.out.println(users);
	}

}
