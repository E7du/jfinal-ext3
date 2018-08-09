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
package cn.zhucongqi.redis;

import java.util.List;

import org.junit.jupiter.api.Test;

import com.jfinal.ext.config.StandaloneAppConfig;
import com.jfinal.ext.plugin.redis.ModelRedisMapping;
import com.jfinal.kit.JsonKit;
import com.test.api.model.User;

class ModelRedisPluginTest {

	@Test
	void test() {

		StandaloneAppConfig.start();
		
		ModelRedisMapping me = ModelRedisMapping.me();
		List<String> cachenames = me.getCacheNames();
		for (String cache : cachenames) {
			System.out.println(cache);
		}
		
		
		List<String> tables = me.getTableNames();
		for (String table : tables) {
			System.out.println(table);
		}
		
		User u = new User();
//		u.setId(10926);
		u.setName("zcq");
		//u.setAddr("addr");
		u.syncToRedis(true);
//		u.save();
		System.out.println("id==="+u.getId());
		
		List<User> uu = u.fetch("addr");
		System.out.println("uu"+JsonKit.toJson(uu));
		
		List<User> us = u.fetchPrimaryKeysOnly();
		for (User user : us) {
			System.out.println(JsonKit.toJson(user));
		}
		Long cnt = u.dataCount();
		System.out.println("cnt"+cnt);
		
		us = u.fetch();
		System.out.println("find\n");
		for (User user : us) {
			System.out.println(JsonKit.toJson(user));
		}
		
		u = u.fetchOne();
		System.out.println(JsonKit.toJson(u));
	}

}
