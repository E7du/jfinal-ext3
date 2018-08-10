package com.jfinal.ext.render.excel.test;

import java.io.File;
import java.util.List;

import org.junit.jupiter.api.Test;

import com.jfinal.ext.kit.excel.PoiReader;
import com.jfinal.ext.kit.excel.Rule;
import com.jfinal.kit.JsonKit;
import com.jfinal.plugin.activerecord.Model;

class ExcelReader {
	
	Rule rule = null;

	@Test
	void test() {

		rule = new Rule();
		rule.setStart(3);
		rule.setEnd(8);
		rule.setClazz(Employee.class);
		rule.addCell(1, "Name");
		rule.addCell(2, "Age");
		rule.addCell(3, "Payment");
		rule.addCell(4, "Bonus");
		
        String destFileName = "src/test/resources/employees-desc.xls";
        
       List<Model<?>> ret = PoiReader.processSheet(new File(destFileName), rule);
      String json = JsonKit.toJson(ret.get(0));
      System.out.println(json);
        
	}

}
