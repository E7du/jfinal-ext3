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

import org.junit.Test;

import com.jfinal.ext.config.StandaloneAppConfig;
import com.jfinal.ext.plugin.redis.KeyTypeShortNamingPolicy;
import com.jfinal.kit.JsonKit;
import com.jfinal.plugin.redis.RedisPlugin;
import com.test.api.model.Hello;
import com.test.api.model.User;

/**
 * @author Jobsz
 *
 */
public class RedisCache {

	private RedisPlugin redis = null;
	private int id = 0;
	private String name = null;
	
	private void initTest() {
		redis = new RedisPlugin("a", "localhost");

		redis.setKeyNamingPolicy(new KeyTypeShortNamingPolicy());
		
		redis.start();
		
		StandaloneAppConfig.start();
		id = 1029;
		name = "姓名"+id;
	}

	/**
	 * @param args
	 */
	@Test
	public void testCache() {
		this.initTest();
		

		User user = new User();
		
		user.setAddr("地址");
		user.setName(name);
		user.setId(id);
		
		//save
		user.save();
		// update
		user.setAddr("新地址");
		user.update();
		
		user = new User();
		user.syncToRedis(true);
		
		List<User> users = user.fetch();
		for (User u : users) {
			System.out.println("user=="+JsonKit.toJson(u));
		}
		User u = new User();
		u.setId(id);
		u.setName(name);
		System.out.println(JsonKit.toJson(u));
		u = u.fetchByRedis();
		System.out.println(JsonKit.toJson(u));
		
		Hello hl = new Hello();
		hl.setId(id);
		hl.setName(name);
		hl.setHello("hello");
		
		hl.syncToRedis(true);
		hl.save();
		
	}
	
	@Test
	public void testSetCacheName() {
		this.initTest();
		RedisPlugin redis1 = new RedisPlugin("b", "localhost");
		redis1.setKeyNamingPolicy(new KeyTypeShortNamingPolicy());
		redis1.start();
		
		User user = new User();
		user.shotCacheName("a");
		user.setAddr("地址");
		user.setName(name);
		user.setId(id);
		
		//save
		user.save();
		// update
		user.setAddr("新地址");
		user.shotCacheName("b");
		user.update();
	}
}
