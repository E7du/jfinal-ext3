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
package com.jfinal.ext.plugin.config;

import java.io.File;
import java.io.FileFilter;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.regex.Pattern;

import com.google.common.collect.Maps;
import com.jfinal.ext.kit.ResourceKit;
import com.jfinal.kit.PathKit;
import com.jfinal.log.Log;

public class ConfigKit {

    protected final static Log LOG = Log.getLog(ConfigKit.class);

    private static List<String> includeResources;

    private static List<String> excludeResources;

    private static Map<String, String> map = Maps.newHashMap();

    private static Map<String, String> testMap = Maps.newHashMap();

    private static Map<String, Long> lastmodifies = Maps.newHashMap();

    private static boolean reload = true;

    /**
     * @param includeResources
     * @param excludeResources
     * @param reload
     */
    static void init(List<String> includeResources, List<String> excludeResources, boolean reload) {
        ConfigKit.includeResources = includeResources;
        ConfigKit.excludeResources = excludeResources;
        ConfigKit.reload = reload;
        for (final String resource : includeResources) {
            LOG.debug("include :" + resource);
            File[] propertiesFiles = null;
            propertiesFiles = new File(PathKit.getRootClassPath()).listFiles(new FileFilter() {

                @Override
                public boolean accept(File pathname) {
                    return Pattern.compile(resource).matcher(pathname.getName()).matches();
                }
            });
            for (File file : propertiesFiles) {
                String fileName = file.getName();
                LOG.debug("fileName:" + fileName);
                if (fileName.endsWith("-test." + ConfigPlugin.suffix)) {
                    continue;
                }
                boolean excluded = false;
                for (String exclude : excludeResources) {
                    if (Pattern.compile(exclude).matcher(file.getName()).matches()) {
                        excluded = true;
                    }
                }
                if (excluded) {
                    continue;
                }
                lastmodifies.put(fileName, new File(fileName).lastModified());
                map.putAll(ResourceKit.readProperties(fileName));
                try {
                    testMap.putAll(ResourceKit.readProperties(testFileName(fileName)));
                } catch (IllegalArgumentException e) {
                    LOG.info(e.getMessage());
                }
            }
        }
        LOG.debug("map" + map);
        LOG.debug("testMap" + testMap);
        LOG.info("config init success!");
    }

    private static String testFileName(String fileName) {
        return fileName.substring(0, fileName.indexOf("." + ConfigPlugin.suffix)) + "-test." + ConfigPlugin.suffix;
    }

    public static String getStr(String key, String defaultVal) {
        if (testMap == null || map == null) {
            throw new RuntimeException(" please start ConfigPlugin first~");
        }
        if (reload) {
            checkFileModify();
        }
        String val = testMap.get(key);
        if (val == null || "".equals(val.trim())) {
            val = map.get(key);
        }
        return val == null ? defaultVal : val + "";

    }

    private static void checkFileModify() {
        Set<String> filenames = lastmodifies.keySet();
        for (String filename : filenames) {
            File file = new File(filename);
            if (lastmodifies.get(filename) != file.lastModified()) {
                LOG.info(filename + " changed, reload.");
                init(includeResources, excludeResources, reload);
            }
        }
    }

    public static String getStr(String key) {
        return getStr(key, "");
    }

    public static long getLong(String key) {
        return getLong(key, 0);
    }

    public static long getLong(String key, long defaultVal) {
        String val = getStr(key).trim();
        if ("".equals(val)) {
            return defaultVal;
        }
        return Long.parseLong(val);
    }

    public static int getInt(String key, int defaultVal) {
        String val = getStr(key).trim();
        if ("".equals(val)) {
            return defaultVal;
        }
        return Integer.parseInt(val);
    }

    public static int getInt(String key) {
        return getInt(key, 0);
    }

    public static void set(String key, String val) {
        // TODO
        throw new RuntimeException("I do not know how to do it now..");
    }

}
