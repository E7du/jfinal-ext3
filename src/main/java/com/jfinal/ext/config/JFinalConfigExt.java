/**
 * Copyright (c) 2018, Jobsz (zcq@zhucongqi.cn).
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.jfinal.ext.config;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import com.alibaba.druid.filter.stat.StatFilter;
import com.alibaba.druid.wall.WallFilter;
import com.jfinal.config.Constants;
import com.jfinal.config.Handlers;
import com.jfinal.config.Interceptors;
import com.jfinal.config.Plugins;
import com.jfinal.config.Routes;
import com.jfinal.core.Const;
import com.jfinal.ext.handler.ActionExtentionHandler;
import com.jfinal.ext.interceptor.NotFoundActionInterceptor;
import com.jfinal.ext.interceptor.OnExceptionInterceptorExt;
import com.jfinal.ext.interceptor.POST;
import com.jfinal.ext.kit.PageViewKit;
import com.jfinal.ext.plugin.druid.DruidEncryptPlugin;
import com.jfinal.ext.route.AutoBindRoutes;
import com.jfinal.kit.StrKit;
import com.jfinal.plugin.activerecord.ActiveRecordPlugin;
import com.jfinal.plugin.activerecord.generator.BaseModelGenerator;
import com.jfinal.plugin.activerecord.generator.Generator;
import com.jfinal.plugin.activerecord.generator.MappingKitGenerator;
import com.jfinal.plugin.activerecord.generator.ModelGenerator;
import com.jfinal.plugin.druid.DruidPlugin;
import com.jfinal.render.ViewType;
import com.jfinal.template.Engine;

/**
 * @author Jobsz
 */
public abstract class JFinalConfigExt extends com.jfinal.config.JFinalConfig {
	
	private final static String cfg = "conf/jf-app-cfg.conf";
	
	public static String APP_NAME = null;
	protected boolean geRuned = false;
	
	/**
	 * Config other More constant
	 */
	public abstract void configMoreConstants(Constants me);
	
	/**
	 * Config other more route
	 */
	public abstract void configMoreRoutes(Routes me);
	
	/**
	 * Config other more plugin
	 */
	public abstract void configMorePlugins(Plugins me);
	
	/**
	 * Config other more interceptor applied to all actions.
	 */
	public abstract void configMoreInterceptors(Interceptors me);
	
	/**
	 * Config other more handler
	 */
	public abstract void configMoreHandlers(Handlers me);
	
	/**
	 * Config engine more
	 */
	public abstract void configEngineMore(Engine e);

	/**
	 * After JFinalStarted
	 */
	public abstract void afterJFinalStarted();
	
	/**
	 * Config constant
	 * 
	 * Default <br/>
	 * ViewType: JSP <br/>
	 * Encoding: UTF-8 <br/>
	 * ErrorPages: <br/>
	 * 404 : /WEB-INF/errorpages/404.jsp <br/>
	 * 500 : /WEB-INF/errorpages/500.jsp <br/>
	 * 403 : /WEB-INF/errorpages/403.jsp <br/>
	 * UploadedFileSaveDirectory : cfg basedir + appName <br/>
	 */
	public void configConstant(Constants me) {
		me.setViewType(ViewType.JSP);
		me.setDevMode(this.getAppDevMode());
		me.setEncoding(Const.DEFAULT_ENCODING);
		me.setError404View(PageViewKit.get404PageView());
		me.setError500View(PageViewKit.get500PageView());
		me.setError403View(PageViewKit.get403PageView());
		JFinalConfigExt.APP_NAME = this.getAppName();
		// config others
		configMoreConstants(me);
	}
	
	/**
	 * Config route
	 * Config the AutoBindRoutes
	 * 自动bindRoute。controller命名为xxController。<br/>
	 * AutoBindRoutes自动取xxController对应的class的Controller之前的xx作为controllerKey(path)<br/>
	 * 如：MyUserController => myuser; UserController => user; UseradminController => useradmin<br/>
	 */
	public void configRoute(Routes me) {
		me.add(new AutoBindRoutes());
		// config others
		configMoreRoutes(me);
	}

	/**
	 * Config plugin
	 */
	public void configPlugin(Plugins me) {
			String[] dses = this.getDataSource();
			for (String ds : dses) {
				if (!this.getDbActiveState(ds)) {
					continue;
				}
				DruidEncryptPlugin drp = this.getDruidPlugin(ds);
				me.add(drp);
				ActiveRecordPlugin arp = this.getActiveRecordPlugin(ds, drp);
				me.add(arp);
		}
		// config others
		configMorePlugins(me);
	}
	
	/**
	 * Config interceptor applied to all actions.
	 */
	public void configInterceptor(Interceptors me) {
		// when action not found fire 404 error
		me.add(new NotFoundActionInterceptor());
		// add excetion interceptor
		me.add(new OnExceptionInterceptorExt());
		if (this.getHttpPostMethod()) {
			me.add(new POST());
		}
		// config others
		configMoreInterceptors(me);
	}
	
	/**
	 * Config handler
	 */
	public void configHandler(Handlers me) {
		// add extension handler
		me.add(new ActionExtentionHandler());
		// config others
		configMoreHandlers(me);
	}
	
	/**
	 * Config engine
	 */
	public void configEngine(Engine e) {
		e.setDevMode(this.getAppDevMode());
	}
	
	public void afterJFinalStart() {
		super.afterJFinalStart();
		this.afterJFinalStarted();
	}

	private void loadPropertyFile() {
		if (this.prop == null) {
			this.loadPropertyFile(cfg);
		}
	}
	
	private boolean getHttpPostMethod() {
		this.loadPropertyFile();
		return this.getPropertyToBoolean("app.post", false);
	}

	/**
	 * 获取app的dev mode
	 * @return
	 */
	private boolean getAppDevMode(){
		this.loadPropertyFile();
		return this.getPropertyToBoolean("app.dev", true);
	}

	/**
	 * 获取 AppName
	 * @return
	 */
	protected String getAppName() {
		this.loadPropertyFile();
		String appName = this.getProperty("app.name", "");
		if (StrKit.isBlank(appName)) {
			throw new IllegalArgumentException("Please Set Your App Name in Your cfg file");
		}
		return appName;
	}
	
	private static final String ACTIVE_TEMPLATE = "db.%s.active";
	private static final String URL_TEMPLATE = "jdbc:%s://%s";
	private static final String USER_TEMPLATE = "db.%s.user";
	private static final String PASSWORD_PKEY_TEMPLATE = "db.%s.password.pkey";
	private static final String PASSWORD_TEMPLATE = "db.%s.password";
	private static final String INITSIZE_TEMPLATE = "db.%s.initsize";
	private static final String MAXSIZE_TEMPLATE = "db.%s.maxactive";
	
	/**
	 * 获取是否打开数据库状态
	 * @return
	 */
	private boolean getDbActiveState(String ds){
		this.loadPropertyFile();
		return this.getPropertyToBoolean(String.format(ACTIVE_TEMPLATE, ds), false);
	}
	
	/**
	 * 获取数据源
	 * @return
	 */
	private String[] getDataSource() {
		this.loadPropertyFile();
		String ds = this.getProperty("db.ds", "");
		if (StrKit.isBlank(ds)) {
			return (new String[0]);
		}
		if (ds.contains("，")) {
			new IllegalArgumentException("Cannot use ，in ds");
		}
		return ds.split(",");
	}
	
	/**
	 * DruidPlugin
	 * @param prop ： property
	 * @return
	 */
	private DruidEncryptPlugin getDruidPlugin(String ds) {
		this.loadPropertyFile();
		String url = this.getProperty(String.format("db.%s.url", ds));
		url = String.format(URL_TEMPLATE, ds, url);
		String endsWith = "?characterEncoding=UTF8&zeroDateTimeBehavior=CONVERT_TO_NULL";
		if (!url.endsWith(endsWith)) {
			url += endsWith;
		}
		
		String password = this.getProperty(String.format(PASSWORD_TEMPLATE, ds));
		password = DruidEncryptPlugin.decryptedPassword(this.getProperty(String.format(PASSWORD_PKEY_TEMPLATE, ds)), password);
		String user = this.getProperty(String.format(USER_TEMPLATE, ds));
		
		DruidEncryptPlugin dp = new DruidEncryptPlugin(url, user, password);
		dp.setInitialSize(this.getPropertyToInt(String.format(INITSIZE_TEMPLATE, ds)));
		dp.setMaxActive(this.getPropertyToInt(String.format(MAXSIZE_TEMPLATE, ds)));
		dp.addFilter(new StatFilter());
		WallFilter wall = new WallFilter();
		wall.setDbType(ds);
		dp.addFilter(wall);
		
		if (this.geRuned) {
			dp.start();
			BaseModelGenerator baseGe = new BaseModelGenerator(this.getBaseModelPackage(), this.getBaseModelOutDir());
			baseGe.setGenerateChainSetter(true);
			ModelGenerator modelGe = new ModelGenerator(this.getModelPackage(), this.getBaseModelPackage(), this.getModelOutDir());
			modelGe.setGenerateDaoInModel(this.getGeDaoInModel());
			Generator ge = new Generator(dp.getDataSource(), baseGe, modelGe);
			MappingKitGenerator mappingKitGe = new MappingKitGenerator(this.getModelPackage(), this.getModelOutDir());
			if (!JFinalConfigExt.DEFAULT_MAPPINGKIT_CLASS_NAME.equals(this.getMappingKitClassName())) {
				mappingKitGe.setMappingKitClassName(ds.toUpperCase()+this.getMappingKitClassName());
			}
			ge.setMappingKitGenerator(mappingKitGe);
			ge.setGenerateDataDictionary(this.getGeDictionary());
			ge.generate();
		}
		
		return dp;
	}

	/**
	 * 获取ActiveRecordPlugin 
	 * @param dp DruidPlugin
	 * @return
	 */
	private ActiveRecordPlugin getActiveRecordPlugin(String ds, DruidPlugin dp){
		this.loadPropertyFile();
		ActiveRecordPlugin arp = new ActiveRecordPlugin(ds, dp);
		arp.setShowSql(this.getPropertyToBoolean("db.showsql"));

		// auto mapping
		if (!this.geRuned) {
			try {
				Class<?> clazz = Class.forName(this.getModelPackage()+"."+ds.toUpperCase()+this.getMappingKitClassName());
				Method mapping = clazz.getMethod("mapping", ActiveRecordPlugin.class);
				mapping.invoke(clazz, arp);
			} catch (ClassNotFoundException | NoSuchMethodException | SecurityException | IllegalAccessException
					| IllegalArgumentException | InvocationTargetException e) {
				throw (new RuntimeException(String.valueOf(e) + ",may be your table is not contain `PrimaryKey`."));
			}
		}
		return arp;
	}
	
	private Boolean geDaoInModel = null;
	
	private boolean getGeDictionary() {
		this.loadPropertyFile();
		return this.getPropertyToBoolean("ge.dict", false);
	}
	
	private String getBaseModelOutDir() {
		this.loadPropertyFile();
		return this.getProperty("ge.base.model.outdir");
	}
	
	private String getBaseModelPackage() {
		this.loadPropertyFile();
		return this.getProperty("ge.base.model.package");
	}
	
	private boolean getGeDaoInModel() {
		this.loadPropertyFile();
		if (this.geDaoInModel == null) {
			this.geDaoInModel = this.getPropertyToBoolean("ge.model.dao", Boolean.TRUE);
		}
		return this.geDaoInModel.booleanValue();
	}
	
	private String getModelOutDir() {
		this.loadPropertyFile();
		return this.getProperty("ge.model.outdir");
	}
	
	private String getModelPackage() {
		this.loadPropertyFile();
		return this.getProperty("ge.model.package");
	}
	
	private static final String DEFAULT_MAPPINGKIT_CLASS_NAME = "_MappingKit";
	private String mappingKitClassName = null;
	private String getMappingKitClassName() {
		this.loadPropertyFile();
		if (this.mappingKitClassName == null) {
			this.mappingKitClassName = this.getProperty("ge.mappingkit.classname", JFinalConfigExt.DEFAULT_MAPPINGKIT_CLASS_NAME);
		}
		return this.mappingKitClassName;
	}

	//=========== Override
	
	@Override
	public String getProperty(String key) {
		String p = super.getProperty(key);
		if (StrKit.isBlank(p)) {
			new IllegalArgumentException("`"+key+"` Cannot be empty, set `"+key+"` in "+cfg+" file");
		}
		return p;
	}
	
}
