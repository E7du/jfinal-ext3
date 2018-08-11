package com.jfinal.ext.render.xls.demos;

import java.io.File;
import java.util.List;

import org.junit.jupiter.api.Test;

import com.jfinal.ext.kit.xls.XlsReadRule;
import com.jfinal.ext.kit.xls.XlsReader;
import com.jfinal.ext.kit.xls.XlsReadRule.Column;
import com.jfinal.kit.JsonKit;
import com.test.api.model.User;

class XlsReaderDemo {
	
	XlsReadRule xlsReadRule = null;

	@Test
	void test() {

		xlsReadRule = new XlsReadRule();
		xlsReadRule.setStart(2);
		xlsReadRule.setEnd(4);
		xlsReadRule.setClazz(User.class);
		
		Column id = Column.create("id");
		Column name = Column.create("name");
		Column addr = Column.create("addr");
		xlsReadRule.alignColumn(id, name, addr);
		
        String destFileName = "src/test/resources/users.xls";
        
       List<User> ret = XlsReader.readToModel(User.class, new File(destFileName), xlsReadRule);
       User u = ret.get(0);
      String json = JsonKit.toJson(ret.get(0));
      System.out.println(json+"id"+u.getId());
        
	}

}
