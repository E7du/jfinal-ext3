/*
 * Copyright 2018 Jobsz (zcq@zhucongqi.cn)
 * 
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License.  You may obtain a copy
 * of the License at
 * 
 *   http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  See the
 * License for the specific language governing permissions and limitations under
 * the License.
*/
package com.jfinal.ext.kit;

import java.util.HashMap;
import java.util.Map;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.jfinal.json.FastJson;
import com.jfinal.plugin.activerecord.Model;
import com.jfinal.plugin.activerecord.Record;

/**
 * @author Jobsz
 *
 */
final public class FastJsonKit {

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
		return FastJsonKit.parse(json, Object[].class);
	}
	
	/**
	 * json string to map
	 * @param json
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public static <K,V> Map<K,V> jsonToMap(String json){
		return FastJsonKit.parse(json, HashMap.class);
	}
	
	/**
	 * json String to Model<T extends Model<T>>
	 * @param json
	 * @param clazz
	 */
	public static <M extends Model<M>> Model<M> jsonToModel(String json, Class<M> clazz){
		return FastJsonKit.parse(json, clazz);
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

	public static String toJson(Object object) {
		return (FastJson.getJson().toJson(object));
	}

	public static <T> T parse(String jsonString, Class<T> type) {
		return JSON.parseObject(jsonString, type);
	}
}
