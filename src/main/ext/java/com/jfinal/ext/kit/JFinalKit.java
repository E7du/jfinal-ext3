/**
 * Copyright (c) 2011-2013, kidzhou 周磊 (zhouleib1412@gmail.com)
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
package com.jfinal.ext.kit;

import java.util.List;

import com.google.common.collect.Lists;
import com.jfinal.config.Constants;
import com.jfinal.config.Handlers;
import com.jfinal.config.Interceptors;
import com.jfinal.config.Plugins;
import com.jfinal.config.Routes;
import com.jfinal.log.Log;
import com.jfinal.plugin.IPlugin;

public class JFinalKit {

    protected static final Log LOG = Log.getLog(JFinalKit.class);

    static {
        init();
    }

    private static List<IPlugin> pluginList;
    private static Constants constants;
    private static Routes routes;
    private static Plugins plugins;
    private static Interceptors interceptors;
    private static Handlers handlers;

    public static void init() {
        Reflect reflect = Reflect.on("com.jfinal.core.Config");
        constants = reflect.get("constants");
        routes = reflect.get("routes");
        plugins = reflect.get("plugins");
        interceptors = reflect.get("interceptors");
        handlers = reflect.get("handlers");
        pluginList = plugins.getPluginList();
    }

    public static Constants getConstants() {
        return JFinalKit.constants;
    }

    public static Routes getRoutes() {
        return JFinalKit.routes;
    }

    public static Plugins getPlugins() {
        return JFinalKit.plugins;
    }

    public static Interceptors getInterceptors() {
        return JFinalKit.interceptors;
    }

    public static Handlers getHandlers() {
        return JFinalKit.handlers;
    }

    public static void stopPlugin(String pluginName) {
        for (IPlugin iPlugin : pluginList) {
            if (iPlugin.getClass().getSimpleName().equals(pluginName)) {
                iPlugin.stop();
            }
        }
    }

    public static List<IPlugin> findPlugin(Class<? extends IPlugin> plugin) {
        return findPlugin(plugin.getSimpleName());
    }

    public static List<IPlugin> findPlugin(String pluginName) {
        List<IPlugin> plugins = Lists.newArrayList();
        for (IPlugin iPlugin : pluginList) {
            if (iPlugin.getClass().getSimpleName().equals(pluginName)) {
                plugins.add(iPlugin);
            }
        }
        return plugins;
    }

    public static void startPlugin(String pluginName) {
        for (IPlugin iPlugin : pluginList) {
            if (iPlugin.getClass().getSimpleName().equals(pluginName)) {
                iPlugin.start();
            }
        }
    }

    public static void restartPlugin(String pluginName) {
        for (IPlugin iPlugin : pluginList) {
            if (iPlugin.getClass().getSimpleName().equals(pluginName)) {
                iPlugin.stop();
                iPlugin.start();
            }
        }
    }

}
