package com.jfinal.ext.render.xls.demos;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.Test;

import com.jfinal.ext.kit.xls.XlsReadRule.Column;
import com.jfinal.ext.kit.xls.XlsWriter;
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
		 
		 Column id = Column.header("编号", "id");
		 Column name = Column.header("姓名", "name");
		 Column addr = Column.header("地址", "addr");
		 XlsWriter.data(users).columns(id, name, addr).writeToFile("src/test/resources/users.xls");
	}

}
