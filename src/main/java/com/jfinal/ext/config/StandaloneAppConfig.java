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

import java.util.List;

import com.jfinal.config.Constants;
import com.jfinal.config.Handlers;
import com.jfinal.config.Interceptors;
import com.jfinal.config.Plugins;
import com.jfinal.config.Routes;
import com.jfinal.plugin.IPlugin;
import com.jfinal.template.Engine;

/**
 * StandaloneAppConfig
 * @author Jobsz
 */
public class StandaloneAppConfig {
	
	private static StandaloneAppConfig instance = null;
	private JFinalAppConfig cfg = null;
	
	private StandaloneAppConfig(boolean geRuned) {
		this.cfg = new JFinalAppConfig(geRuned);
		this.cfg.start();
	}
	
	/**
	 * 启动数据库配置
	 */
	public static void start() {
		if (StandaloneAppConfig.instance == null) {
			StandaloneAppConfig.instance = new StandaloneAppConfig(false);
		}
	}
	
	/**
	 * 生成 Model+BaseModel
	 */
	public static void startGe() {
		if (StandaloneAppConfig.instance == null) {
			StandaloneAppConfig.instance = new StandaloneAppConfig(true);
		}
	}
	
	public static void stop() {
		if (StandaloneAppConfig.instance != null) {
			StandaloneAppConfig.instance.cfg.stop();
		}
	}
	
	/**
	 * Client App config
	 * @author Jobsz
	 */
	private final static class JFinalAppConfig extends JFinalConfigExt {

		private static final Plugins plugins = new Plugins();
		
		public JFinalAppConfig(boolean geRuned) {
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
		public void configMoreInterceptors(Interceptors me) {
			
		}

		@Override
		public void configMoreHandlers(Handlers me) {
			
		}

		@Override
		public void afterJFinalStarted() {
			
		}

		@Override
		public void configEngineMore(Engine e) {
			
		}
	}
	
}
