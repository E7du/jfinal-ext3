package com.jfinal.ext.render.excel.test;

import java.io.File;

import org.junit.jupiter.api.Test;

import com.jfinal.ext.kit.excel.PoiReader;
import com.jfinal.ext.kit.excel.Rule;
import com.jfinal.kit.JsonKit;

class ExcelReader {
	
	Rule rule = null;

	@Test
	void test() {

		rule = new Rule();
		rule.setStart(3);
		rule.setEnd(7);
		rule.setClazz(Employee.class);
		
        String destFileName = "/Users/congqizhu/Desktop/employees-desc.xls";
        
       Object ret = PoiReader.processSheet(new File(destFileName), rule);
      String json = JsonKit.toJson(ret);
      System.out.println(json);
        
	}

}
