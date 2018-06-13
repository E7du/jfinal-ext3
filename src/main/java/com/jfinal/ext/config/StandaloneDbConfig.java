/**
 * Copyright (c) 2015-2016, BruceZCQ (zcq@zhucongqi.cn).
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

import java.util.List;

import com.jfinal.config.Constants;
import com.jfinal.config.Handlers;
import com.jfinal.config.Interceptors;
import com.jfinal.config.Plugins;
import com.jfinal.config.Routes;
import com.jfinal.plugin.IPlugin;
import com.jfinal.plugin.activerecord.ActiveRecordPlugin;
import com.jfinal.template.Engine;

/**
 * @author BruceZCQ
 */
public class StandaloneDbConfig {
	
	private static StandaloneDbConfig instance = null;
	private JFinalSubConfig cfg = null;
	
	private StandaloneDbConfig(boolean geRuned) {
		this.cfg = new JFinalSubConfig(geRuned);
		this.cfg.start();
	}
	
	/**
	 * 启动数据库配置
	 */
	public static void start() {
		if (StandaloneDbConfig.instance == null) {
			StandaloneDbConfig.instance = new StandaloneDbConfig(false);
		}
	}
	
	/**
	 * 生成 Model+BaseModel
	 */
	public static void startGe() {
		if (StandaloneDbConfig.instance == null) {
			StandaloneDbConfig.instance = new StandaloneDbConfig(true);
		}
	}
	
	public static void stop() {
		if (StandaloneDbConfig.instance != null) {
			StandaloneDbConfig.instance.cfg.stop();
		}
	}
	
	private static class JFinalSubConfig extends JFinalConfigExt {

		private static final Plugins plugins = new Plugins();
		
		public JFinalSubConfig(boolean geRuned) {
			this.geRuned = geRuned;
			this.configPlugin(plugins);
		}
		
		public void start() {
			this.startPlugins();
		}
		
		public void stop() {
			this.stopPlugins();
		}
		
		private void startPlugins() {
			List<IPlugin> pluginList = plugins.getPluginList();
			if (pluginList == null)
				return;
			
			for (IPlugin plugin : pluginList) {
				try {
					if (plugin.start() == false) {
						String message = "Plugin start error: " + plugin.getClass().getName();
						throw new RuntimeException(message);
					}
				}
				catch (Exception e) {
					String message = "Plugin start error: " + plugin.getClass().getName() + ". \n" + e.getMessage();
					throw new RuntimeException(message, e);
				}
			}
		}
		
		private void stopPlugins() {
			List<IPlugin> pluginList = plugins.getPluginList();
			if (pluginList == null)
				return ;
			
			for (IPlugin plugin : pluginList) {
				try {
					if (plugin.stop() == false) {
						String message = "Plugin stop error: " + plugin.getClass().getName();
						throw new RuntimeException(message);
					}
				}
				catch (Exception e) {
					String message = "Plugin stop error: " + plugin.getClass().getName() + ". \n" + e.getMessage();
					throw new RuntimeException(message, e);
				}
			}
		}
		
		@Override
		public void configMoreConstants(Constants me) {
			
		}

		@Override
		public void configMoreRoutes(Routes me) {
			
		}

		@Override
		public void configMorePlugins(Plugins me) {
			
		}

		@Override
		public void configTablesMapping(String configName, ActiveRecordPlugin arp) {
		}

		@Override
		public void configMoreInterceptors(Interceptors me) {
			
		}

		@Override
		public void configMoreHandlers(Handlers me) {
			
		}

		@Override
		public void afterJFinalStarted() {
			
		}

		@Override
		public void configEngine(Engine e) {
			
		}
	}
	
}
