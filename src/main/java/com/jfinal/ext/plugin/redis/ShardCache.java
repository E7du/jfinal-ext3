/**
 * Copyright (c) 2018, Jobsz (zcq@zhucongqi.cn).
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
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import com.jfinal.plugin.redis.IKeyNamingPolicy;
import com.jfinal.plugin.redis.serializer.ISerializer;

import redis.clients.jedis.ShardedJedis;
import redis.clients.jedis.ShardedJedisPool;

/**
 * ShardCache.
 * Redis 命令参考: http://redisdoc.com/
 */
public class ShardCache {
	
	final ShardedJedisPool jedisPool;
	final ISerializer serializer;
	final IKeyNamingPolicy keyNamingPolicy;
	
	ShardCache(ShardedJedisPool jedisPool, ISerializer serializer, IKeyNamingPolicy keyNamingPolicy) {
		this.jedisPool = jedisPool;
		this.serializer = serializer;
		this.keyNamingPolicy = keyNamingPolicy;
	}
	
	/**
	 * 存放 key value 对到 redis
	 * 如果 key 已经持有其他值， SET 就覆写旧值，无视类型。
	 * 对于某个原本带有生存时间（TTL）的键来说， 当 SET 命令成功在这个键上执行时， 这个键原有的 TTL 将被清除。
	 */
	public String set(Object key, Object value) {
		ShardedJedis jedis = getJedis();
		try {
			return jedis.set(keyToBytes(key), valueToBytes(value));
		}
		finally {close(jedis);}
	}
	
	/**
	 * 将 key 的值设为 value ，当且仅当 key 不存在。
	 * 若给定的 key 已经存在，则 SETNX 不做任何动作。
	 * SETNX 是『SET if Not eXists』(如果不存在，则 SET)的简写。
	 */
	public Long setnx(Object key, Object value) {
		ShardedJedis jedis = getJedis();
		try {
			return jedis.setnx(keyToBytes(key), valueToBytes(value));
		} 
		finally {close(jedis);}
	}
	
	/**
	 * 存放 key value 对到 redis，并将 key 的生存时间设为 seconds (以秒为单位)。
	 * 如果 key 已经存在， SETEX 命令将覆写旧值。
	 */
	public String setex(Object key, int seconds, Object value) {
		ShardedJedis jedis = getJedis();
		try {
			return jedis.setex(keyToBytes(key), seconds, valueToBytes(value));
		}
		finally {close(jedis);}
	}
	
	/**
	 * 如果 key 已经存在并且是一个字符串， APPEND 命令将 value 追加到 key 原来的值的末尾。
	 * 如果 key 不存在， APPEND 就简单地将给定 key 设为 value ，就像执行 SET key value 一样。
	 */
	public Long append(Object key, Object value) {
		ShardedJedis jedis = getJedis();
		try {
			return jedis.append(keyToBytes(key), valueToBytes(value));
		}
		finally {close(jedis);}
	}
	
	/**
	 * 截取 key 对应的一个字符串 
	 */
	public Object substr(Object key, int start, int end) {
		ShardedJedis jedis = getJedis();
		try {
			return valueToBytes(jedis.substr(keyToBytes(key), start, end));
		}
		finally {close(jedis);}
	}
	
	/**
	 * 返回 key 所关联的 value 值
	 * 如果 key 不存在那么返回特殊值 nil 。
	 */
	@SuppressWarnings("unchecked")
	public <T> T get(Object key) {
		ShardedJedis jedis = getJedis();
		try {
			return (T)valueFromBytes(jedis.get(keyToBytes(key)));
		}
		finally {close(jedis);}
	}
	
	/**
	 * 返回 key 所储存的字符串值的长度。
	 * 当 key 储存的不是字符串值时，返回一个错误。
	 */
	public Long strlen(Object key) {
		ShardedJedis jedis = getJedis();
		try {
			return jedis.strlen(keyToBytes(key));
		}
		finally {close(jedis);}
	}
	
	/**
	 * 删除给定的一个 key
	 * 不存在的 key 会被忽略。
	 */
	public Long del(Object key) {
		ShardedJedis jedis = getJedis();
		try {
			return jedis.del(keyToBytes(key));
		}
		finally {close(jedis);}
	}
	
	/**
	 * 将 key 中储存的数字值减一。
	 * 如果 key 不存在，那么 key 的值会先被初始化为 0 ，然后再执行 DECR 操作。
	 * 如果值包含错误的类型，或字符串类型的值不能表示为数字，那么返回一个错误。
	 * 本操作的值限制在 64 位(bit)有符号数字表示之内。
	 * 关于递增(increment) / 递减(decrement)操作的更多信息，请参见 INCR 命令。
	 */
	public Long decr(Object key) {
		ShardedJedis jedis = getJedis();
		try {
			return jedis.decr(keyToBytes(key));
		}
		finally {close(jedis);}
	}
	
	/**
	 * 将 key 所储存的值减去减量 decrement 。
	 * 如果 key 不存在，那么 key 的值会先被初始化为 0 ，然后再执行 DECRBY 操作。
	 * 如果值包含错误的类型，或字符串类型的值不能表示为数字，那么返回一个错误。
	 * 本操作的值限制在 64 位(bit)有符号数字表示之内。
	 * 关于更多递增(increment) / 递减(decrement)操作的更多信息，请参见 INCR 命令。
	 */
	public Long decrBy(Object key, long longValue) {
		ShardedJedis jedis = getJedis();
		try {
			return jedis.decrBy(keyToBytes(key), longValue);
		}
		finally {close(jedis);}
	}
	
	/**
	 * 将 key 中储存的数字值增一。
	 * 如果 key 不存在，那么 key 的值会先被初始化为 0 ，然后再执行 INCR 操作。
	 * 如果值包含错误的类型，或字符串类型的值不能表示为数字，那么返回一个错误。
	 * 本操作的值限制在 64 位(bit)有符号数字表示之内。
	 */
	public Long incr(Object key) {
		ShardedJedis jedis = getJedis();
		try {
			return jedis.incr(keyToBytes(key));
		}
		finally {close(jedis);}
	}
	
	/**
	 * 将 key 所储存的值加上增量 increment 。
	 * 如果 key 不存在，那么 key 的值会先被初始化为 0 ，然后再执行 INCRBY 命令。
	 * 如果值包含错误的类型，或字符串类型的值不能表示为数字，那么返回一个错误。
	 * 本操作的值限制在 64 位(bit)有符号数字表示之内。
	 * 关于递增(increment) / 递减(decrement)操作的更多信息，参见 INCR 命令。 
	 */
	public Long incrBy(Object key, long longValue) {
		ShardedJedis jedis = getJedis();
		try {
			return jedis.incrBy(keyToBytes(key), longValue);
		}
		finally {close(jedis);}
	}
	
	/**
	 * 为 key 中所储存的值加上浮点数增量 increment 。
	 * 如果 key 不存在，那么 INCRBYFLOAT 会先将 key 的值设为 0 ，再执行加法操作。
	 * 如果命令执行成功，那么 key 的值会被更新为（执行加法之后的）新值，并且新值会以字符串的形式返回给调用者。
	 */
	public Double incrByFloat(Object key, double doubleValue) {
		ShardedJedis jedis = getJedis();
		try {
			return jedis.incrByFloat(keyToBytes(key), doubleValue);
		}
		finally {close(jedis);}
	}
	
	/**
	 * 检查给定 key 是否存在。
	 */
	public boolean exists(Object key) {
		ShardedJedis jedis = getJedis();
		try {
			return jedis.exists(keyToBytes(key));
		}
		finally {close(jedis);}
	}
	
	/**
	 * 将当前数据库的 key 移动到给定的数据库 db 当中。
	 * 如果当前数据库(源数据库)和给定数据库(目标数据库)有相同名字的给定 key ，或者 key 不存在于当前数据库，那么 MOVE 没有任何效果。
	 * 因此，也可以利用这一特性，将 MOVE 当作锁(locking)原语(primitive)。
	 */
	public Long move(Object key, int dbIndex) {
		ShardedJedis jedis = getJedis();
		try {
			return jedis.move(keyToBytes(key), dbIndex);
		}
		finally {close(jedis);}
	}
	
	/**
	 * 为给定 key 设置生存时间，当 key 过期时(生存时间为 0 )，它会被自动删除。
	 * 在 Redis 中，带有生存时间的 key 被称为『易失的』(volatile)。
	 */
	public Long expire(Object key, int seconds) {
		ShardedJedis jedis = getJedis();
		try {
			return jedis.expire(keyToBytes(key), seconds);
		}
		finally {close(jedis);}
	}
	
	/**
	 * 这个命令和 EXPIRE 命令类似，但它以毫秒为单位设置 key 的过期 unix 时间戳，而不是像 EXPIRE 那样，以秒为单位。
	 */
	public Long expireAt(Object key, long unixTime) {
		ShardedJedis jedis = getJedis();
		try {
			return jedis.expireAt(keyToBytes(key), unixTime);
		}
		finally {close(jedis);}
	}
	
	/**
	 * 这个命令和 EXPIRE 命令的作用类似，但是它以毫秒为单位设置 key 的生存时间，而不像 EXPIRE 命令那样，以秒为单位。
	 */
	public Long pexpire(Object key, long milliseconds) {
		ShardedJedis jedis = getJedis();
		try {
			return jedis.pexpire(keyToBytes(key), milliseconds);
		}
		finally {close(jedis);}
	}
	
	/**
	 * 这个命令和 EXPIREAT 命令类似，但它以毫秒为单位设置 key 的过期 unix 时间戳，而不是像 EXPIREAT 那样，以毫秒为单位。
	 */
	public Long pexpireAt(Object key, long millisecondsTimestamp) {
		ShardedJedis jedis = getJedis();
		try {
			return jedis.pexpireAt(keyToBytes(key), millisecondsTimestamp);
		}
		finally {close(jedis);}
	}
	
	/**
	 * 将给定 key 的值设为 value ，并返回 key 的旧值(old value)。
	 * 当 key 存在但不是字符串类型时，返回一个错误。
	 */
	@SuppressWarnings("unchecked")
	public <T> T getSet(Object key, Object value) {
		ShardedJedis jedis = getJedis();
		try {
			return (T)valueFromBytes(jedis.getSet(keyToBytes(key), valueToBytes(value)));
		}
		finally {close(jedis);}
	}
	
	/**
	 * 移除给定 key 的生存时间，将这个 key 从『易失的』(带生存时间 key )转换成『持久的』(一个不带生存时间、永不过期的 key )。
	 */
	public Long persist(Object key) {
		ShardedJedis jedis = getJedis();
		try {
			return jedis.persist(keyToBytes(key));
		}
		finally {close(jedis);}
	}
	
	/**
	 * 返回或保存给定列表、集合、有序集合 key 中经过排序的元素。
	 * 排序默认以数字作为对象，值被解释为双精度浮点数，然后进行比较。
	 */
	@SuppressWarnings("rawtypes")
	public List sort(Object key) {
		ShardedJedis jedis = getJedis();
		try {
			return jedis.sort(keyToBytes(key));
		}
		finally {close(jedis);}
	}
	
	/**
	 * 返回 key 所储存的值的类型。
	 */
	public String type(Object key) {
		ShardedJedis jedis = getJedis();
		try {
			return jedis.type(keyToBytes(key));
		}
		finally {close(jedis);}
	}
	
	/**
	 * 以秒为单位，返回给定 key 的剩余生存时间(TTL, time to live)。
	 */
	public Long ttl(Object key) {
		ShardedJedis jedis = getJedis();
		try {
			return jedis.ttl(keyToBytes(key));
		}
		finally {close(jedis);}
	}
	
	/**
	 * 对象被引用的数量
	 */
	public Long objectRefcount(Object key) {
		ShardedJedis jedis = getJedis();
		try {
			return jedis.objectRefcount(keyToBytes(key));
		}
		finally {close(jedis);}
	}
	
	/**
	 * 对象没有被访问的空闲时间
	 */
	public Long objectIdletime(Object key) {
		ShardedJedis jedis = getJedis();
		try {
			return jedis.objectIdletime(keyToBytes(key));
		}
		finally {close(jedis);}
	}
	
	/**
	 * 将哈希表 key 中的域 field 的值设为 value 。
	 * 如果 key 不存在，一个新的哈希表被创建并进行 HSET 操作。
	 * 如果域 field 已经存在于哈希表中，旧值将被覆盖。
	 */
	public Long hset(Object key, Object field, Object value) {
		ShardedJedis jedis = getJedis();
		try {
			return jedis.hset(keyToBytes(key), fieldToBytes(field), valueToBytes(value));
		}
		finally {close(jedis);}
	}
	
	/**
	 * 将哈希表 key 中的域 field 的值设置为 value ，当且仅当域 field 不存在。
	 * 若域 field 已经存在，该操作无效。
	 * 如果 key 不存在，一个新哈希表被创建并执行 HSETNX 命令。
	 */
	public Long hsetnx(Object key, Object field, Object value) {
		ShardedJedis jedis = getJedis();
		try {
			return jedis.hsetnx(keyToBytes(key), fieldToBytes(field), valueToBytes(value));
		}
		finally {close(jedis);}
	}
	
	/**
	 * 同时将多个 field-value (域-值)对设置到哈希表 key 中。
	 * 此命令会覆盖哈希表中已存在的域。
	 * 如果 key 不存在，一个空哈希表被创建并执行 HMSET 操作。
	 */
	public String hmset(Object key, Map<Object, Object> hash) {
		ShardedJedis jedis = getJedis();
		try {
			Map<byte[], byte[]> para = new HashMap<byte[], byte[]>();
			for (Entry<Object, Object> e : hash.entrySet())
				para.put(fieldToBytes(e.getKey()), valueToBytes(e.getValue()));
			return jedis.hmset(keyToBytes(key), para);
		}
		finally {close(jedis);}
	}
	
	/**
	 * 返回哈希表 key 中给定域 field 的值。
	 */
	@SuppressWarnings("unchecked")
	public <T> T hget(Object key, Object field) {
		ShardedJedis jedis = getJedis();
		try {
			return (T)valueFromBytes(jedis.hget(keyToBytes(key), fieldToBytes(field)));
		}
		finally {close(jedis);}
	}
	
	/**
	 * 返回哈希表 key 中，一个或多个给定域的值。
	 * 如果给定的域不存在于哈希表，那么返回一个 nil 值。
	 * 因为不存在的 key 被当作一个空哈希表来处理，所以对一个不存在的 key 进行 HMGET 操作将返回一个只带有 nil 值的表。
	 */
	@SuppressWarnings("rawtypes")
	public List hmget(Object key, Object... fields) {
		ShardedJedis jedis = getJedis();
		try {
			List<byte[]> data = jedis.hmget(keyToBytes(key), fieldsToBytesArray(fields));
			return valueListFromBytesList(data);
		}
		finally {close(jedis);}
	}
	
	/**
	 * 删除哈希表 key 中的一个或多个指定域，不存在的域将被忽略。
	 */
	public Long hdel(Object key, Object... fields) {
		ShardedJedis jedis = getJedis();
		try {
			return jedis.hdel(keyToBytes(key), fieldsToBytesArray(fields));
		}
		finally {close(jedis);}
	}
	
	/**
	 * 查看哈希表 key 中，给定域 field 是否存在。
	 */
	public boolean hexists(Object key, Object field) {
		ShardedJedis jedis = getJedis();
		try {
			return jedis.hexists(keyToBytes(key), fieldToBytes(field));
		}
		finally {close(jedis);}
	}
	
	/**
	 * 返回哈希表 key 中，所有的域和值。
	 * 在返回值里，紧跟每个域名(field name)之后是域的值(value)，所以返回值的长度是哈希表大小的两倍。
	 */
	@SuppressWarnings("rawtypes")
	public Map hgetAll(Object key) {
		ShardedJedis jedis = getJedis();
		try {
			Map<byte[], byte[]> data = jedis.hgetAll(keyToBytes(key));
			Map<Object, Object> result = new HashMap<Object, Object>();
			for (Entry<byte[], byte[]> e : data.entrySet())
				result.put(fieldFromBytes(e.getKey()), valueFromBytes(e.getValue()));
			return result;
		}
		finally {close(jedis);}
	}
	
	/**
	 * 返回哈希表 key 中所有域的值。
	 */
	@SuppressWarnings("rawtypes")
	public List hvals(Object key) {
		ShardedJedis jedis = getJedis();
		try {
			Collection<byte[]> data = jedis.hvals(keyToBytes(key));
			return valueListFromBytesList(data);
		}
		finally {close(jedis);}
	}
	
	/**
	 * 返回哈希表 key 中的所有域。
	 */
	public Set<String> hkeys(Object key) {
		ShardedJedis jedis = getJedis();
		try {
			Set<byte[]> keySet = jedis.hkeys(keyToBytes(key));
			return keySetFromBytesSet(keySet);	// 返回 key 的方法不能使用 valueSetFromBytesSet(...)
		}
		finally {close(jedis);}
	}
	
	/**
	 * 返回哈希表 key 中域的数量。 
	 */
	public Long hlen(Object key) {
		ShardedJedis jedis = getJedis();
		try {
			return jedis.hlen(keyToBytes(key));
		}
		finally {close(jedis);}
	}
	
	/**
	 * 为哈希表 key 中的域 field 的值加上增量 increment 。
	 * 增量也可以为负数，相当于对给定域进行减法操作。
	 * 如果 key 不存在，一个新的哈希表被创建并执行 HINCRBY 命令。
	 * 如果域 field 不存在，那么在执行命令前，域的值被初始化为 0 。
	 * 对一个储存字符串值的域 field 执行 HINCRBY 命令将造成一个错误。
	 * 本操作的值被限制在 64 位(bit)有符号数字表示之内。
	 */
	public Long hincrBy(Object key, Object field, long value) {
		ShardedJedis jedis = getJedis();
		try {
			return jedis.hincrBy(keyToBytes(key), fieldToBytes(field), value);
		}
		finally {close(jedis);}
	}
	
	/**
	 * 为哈希表 key 中的域 field 加上浮点数增量 increment 。
	 * 如果哈希表中没有域 field ，那么 HINCRBYFLOAT 会先将域 field 的值设为 0 ，然后再执行加法操作。
	 * 如果键 key 不存在，那么 HINCRBYFLOAT 会先创建一个哈希表，再创建域 field ，最后再执行加法操作。
	 * 当以下任意一个条件发生时，返回一个错误：
	 * 1:域 field 的值不是字符串类型(因为 redis 中的数字和浮点数都以字符串的形式保存，所以它们都属于字符串类型）
	 * 2:域 field 当前的值或给定的增量 increment 不能解释(parse)为双精度浮点数(double precision floating point number)
	 * HINCRBYFLOAT 命令的详细功能和 INCRBYFLOAT 命令类似，请查看 INCRBYFLOAT 命令获取更多相关信息。
	 */
	public Double hincrByFloat(Object key, Object field, double value) {
		ShardedJedis jedis = getJedis();
		try {
			return jedis.hincrByFloat(keyToBytes(key), fieldToBytes(field), value);
		}
		finally {close(jedis);}
	}
	
	/**
	 * 返回列表 key 中，下标为 index 的元素。
	 * 下标(index)参数 start 和 stop 都以 0 为底，也就是说，以 0 表示列表的第一个元素，以 1 表示列表的第二个元素，以此类推。
	 * 你也可以使用负数下标，以 -1 表示列表的最后一个元素， -2 表示列表的倒数第二个元素，以此类推。
	 * 如果 key 不是列表类型，返回一个错误。
	 */
	@SuppressWarnings("unchecked")
	
	/**
	 * 返回列表 key 中，下标为 index 的元素。
	 * 下标(index)参数 start 和 stop 都以 0 为底，也就是说，以 0 表示列表的第一个元素，
	 * 以 1 表示列表的第二个元素，以此类推。
	 * 你也可以使用负数下标，以 -1 表示列表的最后一个元素， -2 表示列表的倒数第二个元素，以此类推。
	 * 如果 key 不是列表类型，返回一个错误。
	 */
	public <T> T lindex(Object key, long index) {
		ShardedJedis jedis = getJedis();
		try {
			return (T)valueFromBytes(jedis.lindex(keyToBytes(key), index));
		}
		finally {close(jedis);}
	}
	
	/**
	 * 获取记数器的值
	 */
	public Long getCounter(Object key) {
		ShardedJedis jedis = getJedis();
		try {
			return Long.parseLong((String)jedis.get(keyNamingPolicy.getKeyName(key)));
		}
		finally {close(jedis);}
	}
	
	/**
	 * 返回列表 key 的长度。
	 * 如果 key 不存在，则 key 被解释为一个空列表，返回 0 .
	 * 如果 key 不是列表类型，返回一个错误。
	 */
	public Long llen(Object key) {
		ShardedJedis jedis = getJedis();
		try {
			return jedis.llen(keyToBytes(key));
		}
		finally {close(jedis);}
	}
	
	/**
	 * 移除并返回列表 key 的头元素。
	 */
	@SuppressWarnings("unchecked")
	public <T> T lpop(Object key) {
		ShardedJedis jedis = getJedis();
		try {
			return (T)valueFromBytes(jedis.lpop(keyToBytes(key)));
		}
		finally {close(jedis);}
	}
	
	/**
	 * 将一个或多个值 value 插入到列表 key 的表头
	 * 如果有多个 value 值，那么各个 value 值按从左到右的顺序依次插入到表头： 比如说，
	 * 对空列表 mylist 执行命令 LPUSH mylist a b c ，列表的值将是 c b a ，
	 * 这等同于原子性地执行 LPUSH mylist a 、 LPUSH mylist b 和 LPUSH mylist c 三个命令。
	 * 如果 key 不存在，一个空列表会被创建并执行 LPUSH 操作。
	 * 当 key 存在但不是列表类型时，返回一个错误。
	 */
	public Long lpush(Object key, Object... values) {
		ShardedJedis jedis = getJedis();
		try {
			return jedis.lpush(keyToBytes(key), valuesToBytesArray(values));
		}
		finally {close(jedis);}
	}
	
	/**
	 * 将值 value 插入到列表 key 的表头，当且仅当 key 存在并且是一个列表。
	 * 和 LPUSH 命令相反，当 key 不存在时， LPUSHX 命令什么也不做。
	 */
	public Long lpushx(Object key, Object... values) {
		ShardedJedis jedis = getJedis();
		try {
			return jedis.lpushx(keyToBytes(key), valuesToBytesArray(values));
		}
		finally {close(jedis);}
	}
	
	/**
	 * 将列表 key 下标为 index 的元素的值设置为 value 。
	 * 当 index 参数超出范围，或对一个空列表( key 不存在)进行 LSET 时，返回一个错误。
	 * 关于列表下标的更多信息，请参考 LINDEX 命令。
	 */
	public String lset(Object key, long index, Object value) {
		ShardedJedis jedis = getJedis();
		try {
			return jedis.lset(keyToBytes(key), index, valueToBytes(value));
		}
		finally {close(jedis);}
	}
	
	/**
	 * 根据参数 count 的值，移除列表中与参数 value 相等的元素。
	 * count 的值可以是以下几种：
	 * count > 0 : 从表头开始向表尾搜索，移除与 value 相等的元素，数量为 count 。
	 * count < 0 : 从表尾开始向表头搜索，移除与 value 相等的元素，数量为 count 的绝对值。
	 * count = 0 : 移除表中所有与 value 相等的值。
	 */
	public Long lrem(Object key, long count, Object value) {
		ShardedJedis jedis = getJedis();
		try {
			return jedis.lrem(keyToBytes(key), count, valueToBytes(value));
		}
		finally {close(jedis);}
	}
	
	/**
	 * 返回列表 key 中指定区间内的元素，区间以偏移量 start 和 stop 指定。
	 * 下标(index)参数 start 和 stop 都以 0 为底，也就是说，以 0 表示列表的第一个元素，以 1 表示列表的第二个元素，以此类推。
	 * 你也可以使用负数下标，以 -1 表示列表的最后一个元素， -2 表示列表的倒数第二个元素，以此类推。
	 * <pre>
	 * 例子：
	 * 获取 list 中所有数据：cache.lrange(listKey, 0, -1);
	 * 获取 list 中下标 1 到 3 的数据： cache.lrange(listKey, 1, 3);
	 * </pre>
	 */
	@SuppressWarnings("rawtypes")
	public List lrange(Object key, long start, long end) {
		ShardedJedis jedis = getJedis();
		try {
			List<byte[]> data = jedis.lrange(keyToBytes(key), start, end);
			return valueListFromBytesList(data);
		}
		finally {close(jedis);}
	}
	
	/**
	 * 对一个列表进行修剪(trim)，就是说，让列表只保留指定区间内的元素，不在指定区间之内的元素都将被删除。
	 * 举个例子，执行命令 LTRIM list 0 2 ，表示只保留列表 list 的前三个元素，其余元素全部删除。
	 * 下标(index)参数 start 和 stop 都以 0 为底，也就是说，以 0 表示列表的第一个元素，以 1 表示列表的第二个元素，以此类推。
	 * 你也可以使用负数下标，以 -1 表示列表的最后一个元素， -2 表示列表的倒数第二个元素，以此类推。
	 * 当 key 不是列表类型时，返回一个错误。
	 */
	public String ltrim(Object key, long start, long end) {
		ShardedJedis jedis = getJedis();
		try {
			return jedis.ltrim(keyToBytes(key), start, end);
		}
		finally {close(jedis);}
	}
	
	/**
	 * 移除并返回列表 key 的尾元素。
	 */
	@SuppressWarnings("unchecked")
	public <T> T rpop(Object key) {
		ShardedJedis jedis = getJedis();
		try {
			return (T)valueFromBytes(jedis.rpop(keyToBytes(key)));
		}
		finally {close(jedis);}
	}
	
	/**
	 * 将一个或多个值 value 插入到列表 key 的表尾(最右边)。
	 * 如果有多个 value 值，那么各个 value 值按从左到右的顺序依次插入到表尾：比如
	 * 对一个空列表 mylist 执行 RPUSH mylist a b c ，得出的结果列表为 a b c ，
	 * 等同于执行命令 RPUSH mylist a 、 RPUSH mylist b 、 RPUSH mylist c 。
	 * 如果 key 不存在，一个空列表会被创建并执行 RPUSH 操作。
	 * 当 key 存在但不是列表类型时，返回一个错误。
	 */
	public Long rpush(Object key, Object... values) {
		ShardedJedis jedis = getJedis();
		try {
			return jedis.rpush(keyToBytes(key), valuesToBytesArray(values));
		}
		finally {close(jedis);}
	}
	
	/**
	 * BLPOP 是列表的阻塞式(blocking)弹出原语。
	 * 它是 LPOP 命令的阻塞版本，当给定列表内没有任何元素可供弹出的时候，连接将被 BLPOP 命令阻塞，直到等待超时或发现可弹出元素为止。
	 * 当给定多个 key 参数时，按参数 key 的先后顺序依次检查各个列表，弹出第一个非空列表的头元素。
	 */
	@SuppressWarnings("rawtypes")
	public List blpop(Object key) {
		ShardedJedis jedis = getJedis();
		try {
			List<byte[]> data = jedis.blpop(keyToBytes(key));
			return valueListFromBytesList(data);
		}
		finally {close(jedis);}
	}
	
	/**
	 * BRPOP 是列表的阻塞式(blocking)弹出原语。
	 * 它是 RPOP 命令的阻塞版本，当给定列表内没有任何元素可供弹出的时候，连接将被 BRPOP 命令阻塞，直到等待超时或发现可弹出元素为止。
	 * 当给定多个 key 参数时，按参数 key 的先后顺序依次检查各个列表，弹出第一个非空列表的尾部元素。
	 * 关于阻塞操作的更多信息，请查看 BLPOP 命令， BRPOP 除了弹出元素的位置和 BLPOP 不同之外，其他表现一致。
	 */
	@SuppressWarnings("rawtypes")
	public List brpop(Object key) {
		ShardedJedis jedis = getJedis();
		try {
			List<byte[]> data = jedis.brpop(keyToBytes(key));
			return valueListFromBytesList(data);
		}
		finally {close(jedis);}
	}
	
	/**
	 * 将一个或多个 member 元素加入到集合 key 当中，已经存在于集合的 member 元素将被忽略。
	 * 假如 key 不存在，则创建一个只包含 member 元素作成员的集合。
	 * 当 key 不是集合类型时，返回一个错误。
	 */
	public Long sadd(Object key, Object... members) {
		ShardedJedis jedis = getJedis();
		try {
			return jedis.sadd(keyToBytes(key), valuesToBytesArray(members));
		}
		finally {close(jedis);}
	}
	
	/**
	 * 返回集合 key 的基数(集合中元素的数量)。
	 */
	public Long scard(Object key) {
		ShardedJedis jedis = getJedis();
		try {
			return jedis.scard(keyToBytes(key));
		}
		finally {close(jedis);}
	}
	
	/**
	 * 移除并返回集合中的一个随机元素。
	 * 如果只想获取一个随机元素，但不想该元素从集合中被移除的话，可以使用 SRANDMEMBER 命令。
	 */
	@SuppressWarnings("unchecked")
	public <T> T spop(Object key) {
		ShardedJedis jedis = getJedis();
		try {
			return (T)valueFromBytes(jedis.spop(keyToBytes(key)));
		}
		finally {close(jedis);}
	}
	
	/**
	 * 返回集合 key 中的所有成员。
	 * 不存在的 key 被视为空集合。
	 */
	@SuppressWarnings("rawtypes")
	public Set smembers(Object key) {
		ShardedJedis jedis = getJedis();
		try {
			Set<byte[]> data = jedis.smembers(keyToBytes(key));
			Set<Object> result = new HashSet<Object>();
			valueSetFromBytesSet(data, result);
			return result;
		}
		finally {close(jedis);}
	}
	
	/**
	 * 判断 member 元素是否集合 key 的成员。
	 */
	public boolean sismember(Object key, Object member) {
		ShardedJedis jedis = getJedis();
		try {
			return jedis.sismember(keyToBytes(key), valueToBytes(member));
		}
		finally {close(jedis);}
	}
	
	/**
	 * 返回集合中的一个随机元素。
	 */
	@SuppressWarnings("unchecked")
	public <T> T srandmember(Object key) {
		ShardedJedis jedis = getJedis();
		try {
			return (T)valueFromBytes(jedis.srandmember(keyToBytes(key)));
		}
		finally {close(jedis);}
	}
	
	/**
	 * 返回集合中的 count 个随机元素。
	 * 从 Redis 2.6 版本开始， SRANDMEMBER 命令接受可选的 count 参数：
	 * 如果 count 为正数，且小于集合基数，那么命令返回一个包含 count 个元素的数组，数组中的元素各不相同。
	 * 如果 count 大于等于集合基数，那么返回整个集合。
	 * 如果 count 为负数，那么命令返回一个数组，数组中的元素可能会重复出现多次，而数组的长度为 count 的绝对值。
	 * 该操作和 SPOP 相似，但 SPOP 将随机元素从集合中移除并返回，而 SRANDMEMBER 则仅仅返回随机元素，而不对集合进行任何改动。
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public List srandmember(Object key, int count) {
		ShardedJedis jedis = getJedis();
		try {
			List<byte[]> data = jedis.srandmember(keyToBytes(key), count);
			return valueListFromBytesList(data);
		}
		finally {close(jedis);}
	}
	
	/**
	 * 移除集合 key 中的一个或多个 member 元素，不存在的 member 元素会被忽略。
	 */
	public Long srem(Object key, Object... members) {
		ShardedJedis jedis = getJedis();
		try {
			return jedis.srem(keyToBytes(key), valuesToBytesArray(members));
		}
		finally {close(jedis);}
	}
	
	/**
	 * 将一个或多个 member 元素及其 score 值加入到有序集 key 当中。
	 * 如果某个 member 已经是有序集的成员，那么更新这个 member 的 score 值，
	 * 并通过重新插入这个 member 元素，来保证该 member 在正确的位置上。
	 */
	public Long zadd(Object key, double score, Object member) {
		ShardedJedis jedis = getJedis();
		try {
			return jedis.zadd(keyToBytes(key), score, valueToBytes(member));
		}
		finally {close(jedis);}
	}
	
	public Long zadd(Object key, Map<Object, Double> scoreMembers) {
		ShardedJedis jedis = getJedis();
		try {
			Map<byte[], Double> para = new HashMap<byte[], Double>();
			for (Entry<Object, Double> e : scoreMembers.entrySet())
				para.put(valueToBytes(e.getKey()), e.getValue());	// valueToBytes is important
			return jedis.zadd(keyToBytes(key), para);
		}
		finally {close(jedis);}
	}
	
	/**
	 * 返回有序集 key 的基数。
	 */
	public Long zcard(Object key) {
		ShardedJedis jedis = getJedis();
		try {
			return jedis.zcard(keyToBytes(key));
		}
		finally {close(jedis);}
	}
	
	/**
	 * 返回有序集 key 中， score 值在 min 和 max 之间(默认包括 score 值等于 min 或 max )的成员的数量。
	 * 关于参数 min 和 max 的详细使用方法，请参考 ZRANGEBYSCORE 命令。
	 */
	public Long zcount(Object key, double min, double max) {
		ShardedJedis jedis = getJedis();
		try {
			return jedis.zcount(keyToBytes(key), min, max);
		}
		finally {close(jedis);}
	}
	
	/**
	 * 为有序集 key 的成员 member 的 score 值加上增量 increment 。
	 */
	public Double zincrby(Object key, double score, Object member) {
		ShardedJedis jedis = getJedis();
		try {
			return jedis.zincrby(keyToBytes(key), score, valueToBytes(member));
		}
		finally {close(jedis);}
	}
	
	/**
	 * 返回有序集 key 中，指定区间内的成员。
	 * 其中成员的位置按 score 值递增(从小到大)来排序。
	 * 具有相同 score 值的成员按字典序(lexicographical order )来排列。
	 * 如果你需要成员按 score 值递减(从大到小)来排列，请使用 ZREVRANGE 命令。
	 */
	@SuppressWarnings("rawtypes")
	public Set zrange(Object key, long start, long end) {
		ShardedJedis jedis = getJedis();
		try {
			Set<byte[]> data = jedis.zrange(keyToBytes(key), start, end);
			Set<Object> result = new LinkedHashSet<Object>();	// 有序集合必须 LinkedHashSet
			valueSetFromBytesSet(data, result);
			return result;
		}
		finally {close(jedis);}
	}
	
	/**
	 * 返回有序集 key 中，指定区间内的成员。
	 * 其中成员的位置按 score 值递减(从大到小)来排列。
	 * 具有相同 score 值的成员按字典序的逆序(reverse lexicographical order)排列。
	 * 除了成员按 score 值递减的次序排列这一点外， ZREVRANGE 命令的其他方面和 ZRANGE 命令一样。
	 */
	@SuppressWarnings("rawtypes")
	public Set zrevrange(Object key, long start, long end) {
		ShardedJedis jedis = getJedis();
		try {
			Set<byte[]> data = jedis.zrevrange(keyToBytes(key), start, end);
			Set<Object> result = new LinkedHashSet<Object>();	// 有序集合必须 LinkedHashSet
			valueSetFromBytesSet(data, result);
			return result;
		}
		finally {close(jedis);}
	}
	
	/**
	 * 返回有序集 key 中，所有 score 值介于 min 和 max 之间(包括等于 min 或 max )的成员。
	 * 有序集成员按 score 值递增(从小到大)次序排列。
	 */
	@SuppressWarnings("rawtypes")
	public Set zrangeByScore(Object key, double min, double max) {
		ShardedJedis jedis = getJedis();
		try {
			Set<byte[]> data = jedis.zrangeByScore(keyToBytes(key), min, max);
			Set<Object> result = new LinkedHashSet<Object>();	// 有序集合必须 LinkedHashSet
			valueSetFromBytesSet(data, result);
			return result;
		}
		finally {close(jedis);}
	}
	
	/**
	 * 返回有序集 key 中成员 member 的排名。其中有序集成员按 score 值递增(从小到大)顺序排列。
	 * 排名以 0 为底，也就是说， score 值最小的成员排名为 0 。
	 * 使用 ZREVRANK 命令可以获得成员按 score 值递减(从大到小)排列的排名。
	 */
	public Long zrank(Object key, Object member) {
		ShardedJedis jedis = getJedis();
		try {
			return jedis.zrank(keyToBytes(key), valueToBytes(member));
		}
		finally {close(jedis);}
	}
	
	/**
	 * 返回有序集 key 中成员 member 的排名。其中有序集成员按 score 值递减(从大到小)排序。
	 * 排名以 0 为底，也就是说， score 值最大的成员排名为 0 。
	 * 使用 ZRANK 命令可以获得成员按 score 值递增(从小到大)排列的排名。
	 */
	public Long zrevrank(Object key, Object member) {
		ShardedJedis jedis = getJedis();
		try {
			return jedis.zrevrank(keyToBytes(key), valueToBytes(member));
		}
		finally {close(jedis);}
	}
	
	/**
	 * 移除有序集 key 中的一个或多个成员，不存在的成员将被忽略。
	 * 当 key 存在但不是有序集类型时，返回一个错误。
	 */
	public Long zrem(Object key, Object... members) {
		ShardedJedis jedis = getJedis();
		try {
			return jedis.zrem(keyToBytes(key), valuesToBytesArray(members));
		}
		finally {close(jedis);}
	}
	
	/**
	 * 返回有序集 key 中，成员 member 的 score 值。
	 * 如果 member 元素不是有序集 key 的成员，或 key 不存在，返回 nil 。
	 */
	public Double zscore(Object key, Object member) {
		ShardedJedis jedis = getJedis();
		try {
			return jedis.zscore(keyToBytes(key), valueToBytes(member));
		}
		finally {close(jedis);}
	}
	
	// ---------
	
	private byte[] keyToBytes(Object key) {
		String keyStr = keyNamingPolicy.getKeyName(key);
		return serializer.keyToBytes(keyStr);
	}
	
	private String keyFromBytes(byte[] bytes) {
		return serializer.keyFromBytes(bytes);
	}
	
	private Set<String> keySetFromBytesSet(Set<byte[]> data) {
		Set<String> result = new HashSet<String>();
		for (byte[] keyBytes : data)
			result.add(keyFromBytes(keyBytes));
		return result;
	}
	
	private byte[] valueToBytes(Object object) {
		return serializer.valueToBytes(object);
	}
	
	private Object valueFromBytes(byte[] bytes) {
		return serializer.valueFromBytes(bytes);
	}
	
	private byte[][] valuesToBytesArray(Object... objectArray) {
		byte[][] data = new byte[objectArray.length][];
		for (int i=0; i<data.length; i++)
			data[i] = valueToBytes(objectArray[i]);
		return data;
	}
	
	private void valueSetFromBytesSet(Set<byte[]> data, Set<Object> result) {
		for (byte[] d : data)
			result.add(valueFromBytes(d));
	}
	
	@SuppressWarnings("rawtypes")
	private List valueListFromBytesList(Collection<byte[]> data) {
		List<Object> result = new ArrayList<Object>();
		for (byte[] d : data)
			result.add(valueFromBytes(d));
		return result;
	}
	
	private byte[] fieldToBytes(Object field) {
		return serializer.fieldToBytes(field);
	}
	
	private Object fieldFromBytes(byte[] bytes) {
		return serializer.fieldFromBytes(bytes);
	}
	
	private byte[][] fieldsToBytesArray(Object... fields){
		byte[][] data = new byte[fields.length][];
		for (int i = 0; i < data.length; i++) {
			data[i] = fieldToBytes(fields[i]);
		}
		return data;
	}
	
	// ---------
	public ISerializer getSerializer() {
		return serializer;
	}
	
	public IKeyNamingPolicy getKeyNamingPolicy() {
		return keyNamingPolicy;
	}
	
	// ---------
	
	public ShardedJedis getJedis() {
		return (ShardedJedis) jedisPool.getResource();
	}
	
	public void close(ShardedJedis jedis) {
		if (jedis != null)
			jedis.close();
	}
}






