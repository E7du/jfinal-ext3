package com.jfinal.ext.render.excel.test;

import java.io.File;
import java.util.List;

import org.junit.jupiter.api.Test;

import com.jfinal.ext.kit.excel.Reader;
import com.jfinal.ext.kit.excel.ReadRule;
import com.jfinal.ext.kit.excel.ReadRule.Column;
import com.jfinal.kit.JsonKit;
import com.jfinal.plugin.activerecord.Model;

class ExcelReader {
	
	ReadRule readRule = null;

	@Test
	void test() {

		readRule = new ReadRule();
		readRule.setStart(3);
		readRule.setEnd(7);
		readRule.setClazz(Employee.class);
		//readRule.alignColumn("Name", "Age", "Payment", "Bonus");
		
		Column name = Column.create("Name");
		Column age = Column.create("Age");
		Column payment = Column.create("Payment");
		Column bonus = Column.create("Bonus");
		readRule.alignColumn(name, age, payment, bonus);
		
        String destFileName = "src/test/resources/employees-desc.xls";
        
       List<Model<?>> ret = Reader.readToModel(new File(destFileName), readRule);
      String json = JsonKit.toJson(ret);
      System.out.println(json);
        
	}

}
