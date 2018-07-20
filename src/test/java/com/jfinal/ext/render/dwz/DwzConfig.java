package com.jfinal.ext.render.dwz;

import com.jfinal.config.Constants;
import com.jfinal.config.Handlers;
import com.jfinal.config.Interceptors;
import com.jfinal.config.JFinalConfig;
import com.jfinal.config.Plugins;
import com.jfinal.config.Routes;
import com.jfinal.core.JFinal;
import com.jfinal.plugin.activerecord.tx.TxByMethodRegex;
import com.jfinal.template.Engine;

public class DwzConfig extends JFinalConfig {

    public static void main(String[] args) {
        JFinal.start("WebRoot", 9090, "/", 5);
    }

    @Override
    public void configConstant(Constants me) {
        me.setEncoding("utf-8");
        me.setDevMode(true);
        loadPropertyFile("classes/config.txt");
    }

    @Override
    public void configHandler(Handlers me) {

    }

    @Override
    public void configInterceptor(Interceptors me) {
        me.add(new TxByMethodRegex(".*.save"));
    }

    @Override
    public void configPlugin(Plugins me) {
    }

    @Override
    public void configRoute(Routes me) {
        me.add("/dwz", DwzController.class, "WEB-INF/");
    }

	@Override
	public void configEngine(Engine me) {
		
	}

}
