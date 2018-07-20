package com.jfinal.ext.render.dwz;

import com.jfinal.aop.Before;
import com.jfinal.core.Controller;
import com.jfinal.ext.kit.JFinalKit;
import com.jfinal.i18n.I18nInterceptor;
import com.test.api.model.User;

public class DwzController extends Controller {
    public void add() {
        User user = new User();
        user.setName("11");
        user.setId(11);
        user.save();
    }

    @Before(I18nInterceptor.class)
    public void delete() {
       // int id = getParaToInt(0);
//        if (id % 2 == 0) {
//            render(DwzRender.success());
//        } else {
//            render(DwzRender.error("该记录已经删除"));
//        }
        render("list.html");
    }

    public void restart() {
        JFinalKit.restartPlugin("active");
        renderNull();
    }
}
