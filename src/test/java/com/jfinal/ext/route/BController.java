package com.jfinal.ext.route;


@ControllerBind(controllerKey = "/bb")
// @ControllerBind(controllerKey = "/t")
public class BController extends BaseController {
    public void index() {
        setAttr("name", "zhoulei");
        setAttr("age", 10 * 2 + 4);
        render("add.html");
    }
}
