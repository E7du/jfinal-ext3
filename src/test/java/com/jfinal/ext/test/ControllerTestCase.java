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
package com.jfinal.ext.test;

import java.io.File;
import java.io.IOException;
import java.io.StringWriter;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.List;

import javax.servlet.ServletContext;

import org.junit.AfterClass;
import org.junit.Before;

import com.google.common.base.Charsets;
import com.google.common.base.Joiner;
import com.google.common.base.Preconditions;
import com.google.common.base.Throwables;
import com.google.common.io.Files;
import com.jfinal.config.JFinalConfig;
import com.jfinal.core.JFinal;
import com.jfinal.ext.kit.Reflect;
import com.jfinal.handler.Handler;
import com.jfinal.log.Log;

public abstract class ControllerTestCase<T extends JFinalConfig> {
    protected static final Log LOG = Log.getLog(ControllerTestCase.class);
    protected static ServletContext servletContext = new MockServletContext();;
    protected static MockHttpRequest request;
    protected static MockHttpResponse response;
    protected static Handler handler;
    private static boolean configStarted = false;
    private static JFinalConfig configInstance;
    private String actionUrl;
    private String bodyData;
    private File bodyFile;
    private File responseFile;
    private Class<? extends JFinalConfig> config;

    private static void initConfig(Class<JFinal> clazz, JFinal me, ServletContext servletContext, JFinalConfig config) {
        Reflect.on(me).call("init", config, servletContext);
    }

    public static void start(Class<? extends JFinalConfig> configClass) throws Exception {
        if (configStarted) {
            return;
        }
        Class<JFinal> clazz = JFinal.class;
        JFinal me = JFinal.me();
        configInstance = configClass.newInstance();
        initConfig(clazz, me, servletContext, configInstance);
        handler = Reflect.on(me).get("handler");
        configStarted = true;
        configInstance.afterJFinalStart();
    }

    @SuppressWarnings("unchecked")
    public ControllerTestCase() {
        Type genericSuperclass = getClass().getGenericSuperclass();
        Preconditions.checkArgument(genericSuperclass instanceof ParameterizedType,
                "Your ControllerTestCase must have genericType");
        config = (Class<? extends JFinalConfig>) ((ParameterizedType) genericSuperclass).getActualTypeArguments()[0];
    }

    public Object findAttrAfterInvoke(String key) {
        return request.getAttribute(key);
    }

    private String getTarget(String url, MockHttpRequest request) {
        String target = url;
        if (url.contains("?")) {
            target = url.substring(0, url.indexOf("?"));
            String queryString = url.substring(url.indexOf("?") + 1);
            String[] keyVals = queryString.split("&");
            for (String keyVal : keyVals) {
                int i = keyVal.indexOf('=');
                String key = keyVal.substring(0, i);
                String val = keyVal.substring(i + 1);
                request.setParameter(key, val);
            }
        }
        return target;

    }

    @Before
    public void init() throws Exception {
        start(config);
    }

    @AfterClass
    public static void stop() throws Exception {
        configInstance.beforeJFinalStop();
    }

    public String invoke() {
        if (bodyFile != null) {
            List<String> req = null;
            try {
                req = Files.readLines(bodyFile, Charsets.UTF_8);
            } catch (IOException e) {
                Throwables.propagate(e);
            }
            bodyData = Joiner.on("").join(req);
        }
        StringWriter resp = new StringWriter();
        request = new MockHttpRequest(bodyData);
        response = new MockHttpResponse(resp);
        Reflect.on(handler).call("handle", getTarget(actionUrl, request), request, response, new boolean[] { true });
        String response = resp.toString();
        if (responseFile != null) {
            try {
                Files.write(response, responseFile, Charsets.UTF_8);
            } catch (IOException e) {
                Throwables.propagate(e);
            }
        }
        return response;
    }

    public ControllerTestCase<T> post(File bodyFile) {
        this.bodyFile = bodyFile;
        return this;
    }

    public ControllerTestCase<T> post(String bodyData) {
        this.bodyData = bodyData;
        return this;
    }

    public ControllerTestCase<T> use(String actionUrl) {
        this.actionUrl = actionUrl;
        return this;
    }

    public ControllerTestCase<T> writeTo(File responseFile) {
        this.responseFile = responseFile;
        return this;
    }

}
