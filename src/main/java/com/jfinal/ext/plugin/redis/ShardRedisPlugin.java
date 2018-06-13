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
package com.jfinal.ext.plugin.redis;

import java.util.ArrayList;
import java.util.List;

import com.jfinal.plugin.IPlugin;
import com.jfinal.plugin.redis.IKeyNamingPolicy;
import com.jfinal.plugin.redis.serializer.FstSerializer;
import com.jfinal.plugin.redis.serializer.ISerializer;

import redis.clients.jedis.JedisPoolConfig;
import redis.clients.jedis.JedisShardInfo;
import redis.clients.jedis.ShardedJedisPool;

/**
 * ShardRedisPlugin.
 */
public class ShardRedisPlugin implements IPlugin {
	
	private List<JedisShardInfo> shards = new ArrayList<JedisShardInfo>();
	
	private ISerializer serializer = null;
	private IKeyNamingPolicy keyNamingPolicy = null;
	private JedisPoolConfig jedisPoolConfig = new JedisPoolConfig();
	
	public ShardRedisPlugin(List<JedisShardInfo> shards) {
		this.shards = shards;
	}
	
	public boolean addShard(JedisShardInfo shard) {
		if (shard == null) {
			throw new IllegalArgumentException("want add a null shard");
		}
		for (JedisShardInfo jedisShardInfo : this.shards) {
			if (jedisShardInfo.getHost().equals(shard.getHost())
					&& jedisShardInfo.getPort() == shard.getPort()) {
				return true;
			}
		}
		return this.shards.add(shard);
	}
	
	public boolean start() {
		ShardedJedisPool jedisPool = new ShardedJedisPool(jedisPoolConfig, this.shards);;
		
		if (serializer == null)
			serializer = FstSerializer.me;
		if (keyNamingPolicy == null)
			keyNamingPolicy = IKeyNamingPolicy.defaultKeyNamingPolicy;
		
		ShardCache cache = new ShardCache(jedisPool, serializer, keyNamingPolicy);
		ShardRedis.setShardCache(cache);
		return true;
	}
	
	public boolean stop() {
		ShardRedis.shardCache.jedisPool.destroy();
		return true;
	}
	
	/**
	 * 当RedisPlugin 提供的设置属性仍然无法满足需求时，通过此方法获取到
	 * JedisPoolConfig 对象，可对 redis 进行更加细致的配置
	 * <pre>
	 * 例如：
	 * redisPlugin.getJedisPoolConfig().setMaxTotal(100);
	 * </pre>
	 */
	public JedisPoolConfig getJedisPoolConfig() {
		return jedisPoolConfig;
	}
	
	// ---------
	
	public void setSerializer(ISerializer serializer) {
		this.serializer = serializer;
	}
	
	public void setKeyNamingPolicy(IKeyNamingPolicy keyNamingPolicy) {
		this.keyNamingPolicy = keyNamingPolicy;
	}
	
	// ---------
	
	public void setTestWhileIdle(boolean testWhileIdle) {
		jedisPoolConfig.setTestWhileIdle(testWhileIdle);
	}
	
	public void setMinEvictableIdleTimeMillis(int minEvictableIdleTimeMillis) {
		jedisPoolConfig.setMinEvictableIdleTimeMillis(minEvictableIdleTimeMillis);
	}
	
	public void setTimeBetweenEvictionRunsMillis(int timeBetweenEvictionRunsMillis) {
		jedisPoolConfig.setTimeBetweenEvictionRunsMillis(timeBetweenEvictionRunsMillis);
	}
	
	public void setNumTestsPerEvictionRun(int numTestsPerEvictionRun) {
		jedisPoolConfig.setNumTestsPerEvictionRun(numTestsPerEvictionRun);
	}
}


