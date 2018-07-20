package com.jfinal.ext.test;

import com.jfinal.config.Constants;
import com.jfinal.config.Handlers;
import com.jfinal.config.Interceptors;
import com.jfinal.config.JFinalConfig;
import com.jfinal.config.Plugins;
import com.jfinal.config.Routes;
import com.jfinal.core.JFinal;
import com.jfinal.template.Engine;

public class Config extends JFinalConfig {

    public static void main(String[] args) {
        JFinal.start("WebRoot", 8080, "/", 5);
    }

    @Override
    public void configConstant(Constants me) {
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
    public void afterJFinalStart() {
        System.out.println(1);
    }

    
    @Override
    public void beforeJFinalStop() {
        System.out.println(2);
    }

    @Override
    public void configRoute(Routes me) {
        me.add("/post", PostDataController.class);
    }

	@Override
	public void configEngine(Engine me) {
		// TODO Auto-generated method stub
		
	}

}
