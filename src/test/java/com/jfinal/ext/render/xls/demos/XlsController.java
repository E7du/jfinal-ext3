package com.jfinal.ext.render.xls.demos;

import java.util.List;

import com.google.common.collect.Lists;
import com.jfinal.core.Controller;
import com.jfinal.ext.kit.excel.ExcelColumn;
import com.jfinal.ext.kit.excel.ExcelRule;
import com.jfinal.ext.render.excel.ExcelRender;
import com.jfinal.plugin.auth.AccessTokenBuilder;
import com.test.api.model.User;

public class XlsController extends Controller {
	
    String[] columns = new String[] { "id", "name", "addr" };
    String[] headers = new String[] { "编号", "姓名", "地址"};

    public void index(){
        renderText(AccessTokenBuilder.getAccessToken(getRequest()));
    }
    
    public void users() {
        List<User> data = Lists.newArrayList();
        for (int i = 0; i < 5; i++) {
        	User u = new User();
        	int id = 100+i;
        	u.setId(id);
        	u.setName("name"+id);
        	u.setAddr("addr"+id);
            data.add(u);
        }
        ExcelRule rule = new ExcelRule();
		
		rule.setHasHeader(1, true);
		rule.setHasHeader(2, true);

		ExcelColumn id = ExcelColumn.create("id");
		ExcelColumn name = ExcelColumn.create("name");
		ExcelColumn addr = ExcelColumn.create("addr");
		rule.alignColumn(id, name, addr);
        render(ExcelRender.me(data, rule).fileName("your_file_name.xls"));
    }
}
