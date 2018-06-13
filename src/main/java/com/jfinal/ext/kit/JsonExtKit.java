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
package com.jfinal.ext.kit;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.jfinal.log.Log;
import com.jfinal.plugin.activerecord.Model;
import com.jfinal.plugin.activerecord.Record;

/**
 * @author BruceZCQ
 *
 */
final public class JsonExtKit {

	private static Log log = Log.getLog(JsonExtKit.class);
	
	/**
	 * json string to JSONObject
	 * @param json
	 * @return
	 */
	public static JSONObject jsonToObject(String json){
		return JSON.parseObject(json);
	}
	
	/**
	 * json to array
	 * @param <T>
	 * @param json
	 * @return
	 */
	public static JSONArray jsonToJSONArray(String json) {
		return JSON.parseArray(json);
	}
	
	/**
	 * json to array
	 * @param json
	 * @return
	 */
	public static Object[] jsonToObjArray(String json) {
		return JsonExtKit.jsonToJSONArray(json).toArray();
	}
	
	/**
	 * json string to map
	 * @param json
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public static <K,V> Map<K,V> jsonToMap(String json){
		JSONObject obj = jsonToObject(json);
		Map<K, V> map = new HashMap<K, V>();
		Iterator<String> keyIterator = obj.keySet().iterator();
		while (keyIterator.hasNext()) {
			Object key = keyIterator.next();
			Object value = obj.get(key);
			if (value instanceof JSONObject) {
				value = jsonToMap(((JSONObject) value).toJSONString());
			}
			map.put((K)key, (V)value);
		}	
		return map;
	}
	
	/**
	 * json String to Model<T extends Model<T>>
	 * @param json
	 * @param clazz
	 * @return
	 */
	public static <T extends Model<T>> Model<T> jsonToModel(String json, Class<T> clazz){
		Model<T> model = null;
		try {
			model = clazz.newInstance();
		} catch (InstantiationException e) {
			JsonExtKit.log.error(e.getLocalizedMessage());
		} catch (IllegalAccessException e) {
			JsonExtKit.log.error(e.getLocalizedMessage());
		}
		Map<String, Object> attrs = jsonToMap(json);
		return model._setAttrs(attrs);
	}

	/**
	 * json to Record
	 * @param json
	 * @return
	 */
	public static Record jsonToRecord(String json){
		Map<String,Object> map = jsonToMap(json);
		return new Record().setColumns(map);
	}
}