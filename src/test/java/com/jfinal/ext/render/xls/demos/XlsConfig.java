package com.jfinal.ext.render.xls.demos;

import com.jfinal.config.Constants;
import com.jfinal.config.Handlers;
import com.jfinal.config.Interceptors;
import com.jfinal.config.JFinalConfig;
import com.jfinal.config.Plugins;
import com.jfinal.config.Routes;
import com.jfinal.core.JFinal;
import com.jfinal.template.Engine;

public class XlsConfig extends JFinalConfig {

    public static void main(String[] args) {
        JFinal.start("src/main/webapp", 8080, "/", 3);
    }

    @Override
    public void configConstant(Constants me) {
        me.setEncoding("utf-8");
        me.setDevMode(true);
    }

    @Override
    public void configHandler(Handlers me) {

    }

    @Override
    public void configInterceptor(Interceptors me) {
    }

    @Override
    public void configPlugin(Plugins me) {
    }

    @Override
    public void configRoute(Routes me) {
        me.add("/poi", XlsController.class);
    }

	@Override
	public void configEngine(Engine me) {
		// TODO Auto-generated method stub
		
	}

}
