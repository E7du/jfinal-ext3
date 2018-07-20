package com.jfinal.ext.render.excel;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.jfinal.core.Controller;
import com.jfinal.plugin.activerecord.Record;
import com.test.api.model.User;

public class JxlsController extends Controller {

    public void index() {
        List<Employee> staff = new ArrayList<Employee>();
        staff.add(new Employee("Derek", 35, 3000, 0.30));
        staff.add(new Employee("Elsa", 28, 1500, 0.15));
        staff.add(new Employee("Oleg", 32, 2300, 0.25));
        staff.add(new Employee("Neil", 34, 2500, 0.00));
        staff.add(new Employee("Maria", 34, 1700, 0.15));
        staff.add(new Employee("John", 35, 2800, 0.20));
        Map<String, Object> beans = new HashMap<String, Object>();
        beans.put("employee", staff);
        String templateFileName = "/home/kid/git/jfinal-ext/resource/employees.xls";
        String filename = "test.xls";
        render(JxlsRender.me(templateFileName).filename(filename).beans(beans));
    }

    public void model() {
        User user1 = new User();
        user1.setId(1);
        user1.setName("name1");
        User user2 = new User();
        user2.setId(2);
        user2.setName("name2");
        List<User> users  = Lists.newArrayList();
        users.add(user1);
        users.add(user2);
        Map<String, Object> beans = Maps.newHashMap();
        beans.put("user", users);
        String templateFileName = "src/test/resource/users.xls";
        String filename = "users.xls";
        render(JxlsRender.me(templateFileName).filename(filename).beans(beans));
    }

    public void para() {
        List<Employee> staff = new ArrayList<Employee>();
        staff.add(new Employee("Derek", 35, 3000, 0.30));
        staff.add(new Employee("Elsa", 28, 1500, 0.15));
        staff.add(new Employee("Oleg", 32, 2300, 0.25));
        staff.add(new Employee("Neil", 34, 2500, 0.00));
        staff.add(new Employee("Maria", 34, 1700, 0.15));
        staff.add(new Employee("John", 35, 2800, 0.20));
        setAttr("employee", staff);
        String templateFileName = "src/test/resource/employees.xls";
        render(JxlsRender.me(templateFileName));
    }

    public void record() {
        Record record = new Record();
        record.set("name", "Derek");
        record.set("id", 35);
        Record record2 = new Record();
        record2.set("name", "Oleg");
        record2.set("id", 32);
        List<Record> records  = new ArrayList<Record>();
        records.add(record);
        records.add(record2);
        Map<String, Object> beans = new HashMap<String, Object>();
        beans.put("users", records);
        String templateFileName = "src/test/resource/users.xls";
        String filename = "users.xls";
        render(JxlsRender.me(templateFileName).filename(filename).beans(beans));
    }

}
