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

import com.jfinal.ext.config.StandaloneAppConfig;
import com.jfinal.ext.plugin.redis.KeyTypeShortNamingPolicy;
import com.jfinal.kit.JsonKit;
import com.jfinal.plugin.redis.RedisPlugin;
import com.test.api.model.User;

/**
 * @author Jobsz
 *
 */
public class RedisCache {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		
		 StandaloneAppConfig.start();
		
		RedisPlugin redis = new RedisPlugin("a", "localhost", 6379);
		
		redis.setKeyNamingPolicy(new KeyTypeShortNamingPolicy());
		
		redis.start();

		User user = new User();
		
		user.setAddr("地址");
		user.setName("姓名");
		user.setId(1015);
		
		//save
		//user.save();
		// update
		user.setAddr("新地址");
		user.update();
		
		user = new User();
		user.syncToRedis(true);
		
		List<User> users = user.find();
		for (User u : users) {
			System.out.println("user=="+JsonKit.toJson(u));
		}
		User u = new User();
		u.setId(1015);
		System.out.println(JsonKit.toJson(u));
		u = u.findByCache();
		System.out.println(JsonKit.toJson(u));
		
	}

}
