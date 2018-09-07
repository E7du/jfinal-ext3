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

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.jfinal.ext.kit.excel.ExcelReadRule;
import com.jfinal.ext.kit.excel.ExcelReadRule.ReadColumn;
import com.jfinal.ext.kit.excel.ExcelReader;
import com.jfinal.ext.kit.excel.ExcelRowReadFeedback;
import com.jfinal.ext.plugin.activerecord.ModelExt;
import com.test.api.model.User;

class ExcelReaderDemo {

	@BeforeEach
	void setUp() throws Exception {
	}

	@Test
	void test() {
		
//		StandaloneAppConfig.start();
		
		ExcelReader reader = new ExcelReader("users.xls", User.class);
		reader.setReadFeedback(new ExcelRowReadFeedback() {
			
			@Override
			public void readRow(ModelExt<?> model) {
				System.out.println("read2 model="+model+"class"+model.getClass());
			}
		});
		
		ExcelReadRule readRule = new ExcelReadRule();
		
		readRule.setHasHeader(1, true);
		readRule.setHasHeader(2, true);

		ReadColumn id = ReadColumn.create("id");
		ReadColumn name = ReadColumn.create("name");
		ReadColumn addr = ReadColumn.create("addr");
		readRule.alignColumn(id, name, addr);
		reader.setReadRule(readRule);
		reader.read();
	}

}
