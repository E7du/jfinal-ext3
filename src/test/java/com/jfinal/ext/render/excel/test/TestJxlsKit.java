package com.jfinal.ext.render.excel.test;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import org.apache.poi.openxml4j.exceptions.InvalidFormatException;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import net.sf.jxls.exception.ParsePropertyException;
import net.sf.jxls.transformer.XLSTransformer;

public class TestJxlsKit {
    public static void main(String[] args) throws ParsePropertyException, IOException, InvalidFormatException {
        List<Employee> staff = Lists.newArrayList();
        staff.add(new Employee("Derek", 35, 3000, 0.30));
        staff.add(new Employee("Elsa", 28, 1500, 0.15));
        staff.add(new Employee("Oleg", 32, 2300, 0.25));
        staff.add(new Employee("Neil", 34, 2500, 0.00));
        staff.add(new Employee("Maria", 34, 1700, 0.15));
        staff.add(new Employee("John", 35, 2800, 0.20));
        Map<String, Object> beans = Maps.newHashMap();
        beans.put("employee", staff);
        XLSTransformer transformer = new XLSTransformer();
        String templateFileName = "src/test/resources/employees.xls";
        String destFileName = "/Users/congqizhu/Desktop/employees-desc.xls";
        transformer.transformXLS(templateFileName, beans, destFileName);
    }
}
