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
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import com.google.common.base.Preconditions;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.jfinal.log.Log;
import com.jfinal.plugin.activerecord.Db;
import com.jfinal.plugin.activerecord.DbKit;
import com.jfinal.plugin.activerecord.Model;
import com.jfinal.plugin.activerecord.Record;
import com.jfinal.plugin.activerecord.Table;
import com.jfinal.plugin.activerecord.TableMapping;

public class ModelKit {

    protected final static Log logger = Log.getLog(ModelKit.class);

    public static Record toRecord(Model<?> model) {
        return model.toRecord();
    }

    @SuppressWarnings("rawtypes")
    public static Model set(Model model, Object... attrsAndValues) {
        int length = attrsAndValues.length;
        Preconditions.checkArgument(length % 2 == 0, "attrsAndValues length must be even number", length);
        for (int i = 0; i < length; i = i + 2) {
            Object attr = attrsAndValues[i];
            Preconditions.checkArgument(attr instanceof String, "the odd number of attrsAndValues  must be String");
            model.set((String) attr, attrsAndValues[i + 1]);
        }
        return model;
    }

    @SuppressWarnings({ "rawtypes", "unchecked" })
    public static Map<String, Object> toMap(Model model) {
        Map<String, Object> map = Maps.newHashMap();
        Set<Entry<String, Object>> attrs = model._getAttrsEntrySet();
        for (Entry<String, Object> entry : attrs) {
            map.put(entry.getKey(), entry.getValue());
        }
        return map;
    }

    public static Model<?> fromBean(Class<? extends Model<?>> clazz, Object bean) {
        Model<?> model = null;
        try {
            model = clazz.newInstance();
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            return model;
        }
        //TODO bean to model
        return model;
    }

    @SuppressWarnings("rawtypes")
    public static int[] batchSave(List<? extends Model> data) {
        return batchSave(data, data.size());
    }

    @SuppressWarnings("rawtypes")
    public static int[] batchSave(List<? extends Model> data, int batchSize) {
        Model model = data.get(0);
        Map<String, Object> attrs = Reflect.on(model).field("attrs").get();
        Class<? extends Model> modelClass = model.getClass();
        Table tableInfo = TableMapping.me().getTable(modelClass);
        StringBuilder sql = new StringBuilder();
        List<Object> paras = Lists.newArrayList();
        DbKit.getConfig().getDialect().forModelSave(tableInfo, attrs, sql, paras);
        Object[][] batchPara = new Object[data.size()][attrs.size()];
        for (int i = 0; i < data.size(); i++) {
            int j = 0;
            for (String key : attrs.keySet()) {
                batchPara[i][j++] = data.get(i).get(key);
            }
        }
        return Db.batch(sql.toString(), batchPara, batchSize);
    }
    @SuppressWarnings("rawtypes")
    public static void copyColumns(Model src, Model desc, String... columns){
        for(String column:columns)  {
            String[] res = column.split(",");
            if(res.length==1){
                desc.set(column,src.get(column));
            }else {
                desc.set(res[1],src.get(res[0]));
            }
        }
    }
    @SuppressWarnings("rawtypes")
    public static void clone(Model src,Model desc){
        @SuppressWarnings("unchecked")
        Set<Map.Entry<String, Object>> attrs =  src._getAttrsEntrySet();
        for(Map.Entry<String,Object> attr:attrs){
            String key = attr.getKey();
            Object value = attr.getValue();
            desc.set(key,value);
        }
    }
    public static int hashCode(Model<?> model) {
        final int prime = 31;
        int result = 1;
        Table tableinfo = TableMapping.me().getTable(model.getClass());
        Set<Entry<String, Object>> attrsEntrySet = model._getAttrsEntrySet();
        for (Entry<String, Object> entry : attrsEntrySet) {
            String key = entry.getKey();
            Object value = entry.getValue();
            Class<?> clazz = tableinfo.getColumnType(key);
            if (clazz == Integer.class) {
                result = prime * result + (Integer) value;
            } else if (clazz == Short.class) {
                result = prime * result + (Short) value;
            } else if (clazz == Long.class) {
                result = prime * result + (int) ((Long) value ^ ((Long) value >>> 32));
            } else if (clazz == Float.class) {
                result = prime * result + Float.floatToIntBits((Float) value);
            } else if (clazz == Double.class) {
                long temp = Double.doubleToLongBits((Double) value);
                result = prime * result + (int) (temp ^ (temp >>> 32));
            } else if (clazz == Boolean.class) {
                result = prime * result + ((Boolean) value ? 1231 : 1237);
            } else if (clazz == Model.class) {
                result = hashCode((Model<?>) value);
            } else {
                result = prime * result + ((value == null) ? 0 : value.hashCode());
            }
        }
        return result;
    }

    public static boolean equals(Model<?> model, Object obj) {
        if (model == obj)
            return true;
        if (obj == null)
            return false;
        if (model.getClass() != obj.getClass())
            return false;
        Model<?> other = (Model<?>) obj;
        Table tableinfo = TableMapping.me().getTable(model.getClass());
        Set<Entry<String, Object>> attrsEntrySet = model._getAttrsEntrySet();
        for (Entry<String, Object> entry : attrsEntrySet) {
            String key = entry.getKey();
            Object value = entry.getValue();
            Class<?> clazz = tableinfo.getColumnType(key);
            if (clazz == Float.class) {
            } else if (clazz == Double.class) {
            } else if (clazz == Model.class) {
            } else {
                if (value == null) {
                    if (other.get(key) != null)
                        return false;
                } else if (!value.equals(other.get(key)))
                    return false;
            }
        }
        return true;
    }
}
