package com.jfinal.ext.render.xls.demos;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.Test;

import com.jfinal.ext.kit.xls.XlsWriter;
import com.test.api.model.User;

class ExcelWriter {

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
		
		 XlsWriter.data(users).headerRow(1).header("Id","Name", "Addr").column("id","name", "addr")
         .writeToFile("src/test/resources/userss.xls");
	}

}
