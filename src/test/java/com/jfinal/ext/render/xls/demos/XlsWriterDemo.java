package com.jfinal.ext.render.xls.demos;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.Test;

import com.jfinal.ext.kit.excel.ExcelColumn;
import com.test.api.model.User;

class XlsWriterDemo {

	@Test
	void test() {
		
		List<User> users = new ArrayList<User>();
		for (int i = 0; i < 5; i++) {
			User u = new User();
			u.setId(i);
			u.setName("name"+i);
			u.setAddr("addr"+i);
			users.add(u);
		}
		
		 //XlsWriter.data(users).headerRow(1).header("Id","Name", "Addr").column("id","name", "addr").writeToFile("src/test/resources/users.xls");
		 
		 //ExcelColumn id = ExcelColumn.header("编号", "id");
		 //ExcelColumn name = ExcelColumn.header("姓名", "name");
		 //ExcelColumn addr = ExcelColumn.header("地址", "addr");
		// XlsWriter.data(users).columns(id, name, addr).writeToFile("src/test/resources/users.xls");
	}

}
