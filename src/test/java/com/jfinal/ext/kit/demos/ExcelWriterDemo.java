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

import cn.zhucongqi.excel.Writer;
import cn.zhucongqi.excel.metadata.Font;
import cn.zhucongqi.excel.metadata.Sheet;
import cn.zhucongqi.excel.metadata.TableStyle;
import cn.zhucongqi.excel.support.ExcelTypeEnum;

class ExcelWriterDemo {

	@BeforeEach
	void setUp() throws Exception {
	}

	 @SuppressWarnings("unused")
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
			
			String[] attr = u._getAttrNames();
			for (int j = 0; j < attr.length; j++) {
				System.out.println(attr[j]);
			}
		}
//		System.out.println(users);
		
		try {
			Writer writer = new Writer(new FileOutputStream("./src/test/resources/userswrite4.xls"), ExcelTypeEnum.XLS);
			
			Sheet sh = new Sheet(0, 1);
			
			List<List<String>> head = new ArrayList<List<String>>();
			List<String> id = new ArrayList<String>();
			id.add("编号");
			List<String> addr = new ArrayList<String>();
			addr.add("地址");
			List<String> name = new ArrayList<String>();
			name.add("姓名");
			head.add(id);
			head.add(name);
			head.add(addr);
			
			
//			writer.setRule(this.rule());
			writer.write(users, sh);
			writer.finish();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
	}
	
//	private Rule rule() {
//		Rule rule = new Rule();
//		Column id = Column.one("id", "编号");
//		Column name = Column.one("name", "姓名");
//		Column addr = Column.one("addr", "地址");
//		rule.alignColumn(name, id, addr);
//		return rule;
//	}

}
