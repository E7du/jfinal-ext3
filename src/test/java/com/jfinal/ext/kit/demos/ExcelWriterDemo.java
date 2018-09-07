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

import org.apache.poi.ss.usermodel.IndexedColors;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.test.api.model.User;

import cn.zhucongqi.excel.ExcelWriter;
import cn.zhucongqi.excel.metadata.Font;
import cn.zhucongqi.excel.metadata.Sheet;
import cn.zhucongqi.excel.metadata.TableStyle;
import cn.zhucongqi.excel.support.ExcelTypeEnum;

class ExcelWriterDemo {

	@BeforeEach
	void setUp() throws Exception {
	}

	 private TableStyle getTableStyle1() {
	        TableStyle tableStyle = new TableStyle();
	        Font headFont = new Font();
	        headFont.setBold(true);
	        headFont.setFontHeightInPoints((short)22);
	        headFont.setFontName("楷体");
	        tableStyle.setTableHeadFont(headFont);
	        tableStyle.setTableHeadBackGroundColor(IndexedColors.LIGHT_BLUE);

	        Font contentFont = new Font();
	        contentFont.setBold(true);
	        contentFont.setFontHeightInPoints((short)22);
	        contentFont.setFontName("黑体");
	        tableStyle.setTableContentFont(contentFont);
	        tableStyle.setTableContentBackGroundColor(IndexedColors.LIGHT_GREEN);
	        return tableStyle;
	    }
	
	@Test
	void test() {
		
		List<User> users = new ArrayList<User>();
		for (int i = 0; i < 65; i++) {
			User u = new User();
			u.setId(i+3);
			u.setAddr("addr"+i);
			u.setName("名字"+i);
			users.add(u);
		}
//		System.out.println(users);
		
		try {
			ExcelWriter writer = new ExcelWriter(new FileOutputStream("./src/test/resources/userswrite.xls"), ExcelTypeEnum.XLS);
			Sheet sh = new Sheet(1, 0);
			sh.setTableStyle(this.getTableStyle1());
			writer.write(users, sh);
			writer.finish();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
	}

}
