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
package cn.zhucongqi.shardredis;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.jfinal.ext.plugin.redis.ShardCache;
import com.jfinal.ext.plugin.redis.ShardRedis;
import com.jfinal.ext.plugin.redis.ShardRedisPlugin;

import redis.clients.jedis.JedisShardInfo;

/**
 * @author Jobsz
 *
 */
public class ShardRedisCache {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		
		List<JedisShardInfo> shards = new ArrayList<JedisShardInfo>();
		JedisShardInfo shard = new JedisShardInfo("192.168.1.250", 6379);
		shard.setPassword("redisadmin");
		shards.add( shard );
		ShardRedisPlugin redis = new ShardRedisPlugin(shards);
		redis.start();
		
		ShardCache cache = ShardRedis.userShard();
		cache.set("zcq", "--Jobsz---");
		cache.set("zcq1", "--Jobsz---");
		
		Map<Object, Object> map = new HashMap<Object, Object>();
		map.put("name", "朱丛启");
		map.put("addr", "北京市");
		
		cache.hmset("map1", map);
		
		System.out.println("");;
		
		System.out.println(cache.hvals("map1"));
	}

}
