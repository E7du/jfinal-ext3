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

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.alibaba.excel.support.ExcelTypeEnum;
import com.jfinal.ext.kit.excel.ExcelColumn;
import com.jfinal.ext.kit.excel.ExcelRule;
import com.jfinal.ext.kit.excel.ExcelWriter;
import com.test.api.model.User;

class ExcelWriterDemo {

	@BeforeEach
	void setUp() throws Exception {
	}

	@Test
	void test() {
		
		ExcelRule rule = new ExcelRule();
		
		rule.setHasHeader(1, true);
		rule.setHasHeader(2, true);

		ExcelColumn id = ExcelColumn.create("id");
		ExcelColumn name = ExcelColumn.create("name");
		ExcelColumn addr = ExcelColumn.create("addr");
		rule.alignColumn(id, name, addr);
		
		List<User> users = new ArrayList<User>();
		for (int i = 0; i < 65365; i++) {
			User u = new User();
			u.setId(i+3);
			u.setAddr("addr"+i);
			u.setName("名字"+i);
			users.add(u);
		}
//		System.out.println(users);
		
		try {
			ExcelWriter writer = new ExcelWriter(new FileOutputStream("./src/test/resources/userswrite.xls"), ExcelTypeEnum.XLS);
			writer.setWriteRule(rule);
			writer.writeModel(users);
			writer.finish();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
	}

}
