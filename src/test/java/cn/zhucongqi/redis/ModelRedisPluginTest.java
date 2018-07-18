package cn.zhucongqi.redis;

import java.util.List;

import org.junit.jupiter.api.Test;

import com.jfinal.ext.config.StandaloneAppConfig;
import com.jfinal.ext.plugin.redis.ModelRedisMapping;
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
		u.setId(11926);
		u.setName("zcq");
		u.setAddr("addr");
		u.syncToRedis(true);
		u.save();
		
	}

}
