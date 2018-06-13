/**
 * 
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
 * @author BruceZCQ
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
		cache.set("zcq", "--BruceZCQ---");
		cache.set("zcq1", "--BruceZCQ---");
		
		Map<Object, Object> map = new HashMap<Object, Object>();
		map.put("name", "朱丛启");
		map.put("addr", "北京市");
		
		cache.hmset("map1", map);
		
		System.out.println("");;
		
		System.out.println(cache.hvals("map1"));
	}

}
