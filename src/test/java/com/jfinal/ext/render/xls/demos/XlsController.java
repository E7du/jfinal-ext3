package com.jfinal.ext.render.xls.demos;

import java.util.List;

import com.google.common.collect.Lists;
import com.jfinal.core.Controller;
import com.jfinal.ext.render.xls.XlsRender;
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
        render(XlsRender.me(data).fileName("your_file_name.xls").headers(headers).sheetName("ok").columns(columns).cellWidth(5000).headerRow(2));
    }
}
