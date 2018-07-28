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
package com.jfinal.ext.plugin.activerecord;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.jfinal.ext.kit.SqlpKit;
import com.jfinal.ext.plugin.redis.ModelRedisMapping;
import com.jfinal.kit.StrKit;
import com.jfinal.plugin.activerecord.Model;
import com.jfinal.plugin.activerecord.Table;
import com.jfinal.plugin.redis.Cache;
import com.jfinal.plugin.redis.Redis;

public abstract class ModelExt<M extends ModelExt<M>> extends Model<M> {

	private static final long serialVersionUID = -6061985137398460903L;

	//default sync to redis
	private boolean syncToRedis = true;
	private Cache cache = null;
	private String cacheName = null;

	/**
	 * redis key: records:tablename: id1 | id2 | id3...
	 * eg: the user has three primarykeys id1=1,id2=2,id3=3, so the redisKey is 'records:user:1|2|3'.
	 * @return redis key
	 */
	private String redisKey(ModelExt<?> m) {
		Table table = m.table();
		StringBuilder key = new StringBuilder();
		key.append("records:");
		key.append(table.getName());
		key.append(":");
		//fetch primary keys' values
		String[] primaryKeys = table.getPrimaryKey();
		//format key
		boolean first = true;
		for (int idx = 0; idx < primaryKeys.length; idx++) {
			Object primaryKeyVal = m.get(primaryKeys[idx]);
			if (null != primaryKeyVal) {
				if (first) {
					first = false;
				} else {
					key.append("|");
				}
				key.append(primaryKeyVal);
			}
		}
		return key.toString();
	}
	
	private void saveToRedis(ModelExt<?> m) {
		this.cache().hmset(this.redisKey(m), m.attrsCp());
	}
	
	private Cache cache() {
		if (null != this.cache) {
			return this.cache;
		}
		
		if (StrKit.isBlank(this.cacheName)) {
			this.cacheName = ModelRedisMapping.me().getCacheName(this.tableName());
		}
		
		if (StrKit.notBlank(this.cacheName)) {
			this.cache = Redis.use(this.cacheName);
		} else {
			this.cache = Redis.use();
		}
		if (null == this.cache) {
			throw new IllegalArgumentException(String.format("The Cache with the name '%s' was Not Found.", this.cacheName));	
		}
		return this.cache;
	}
	
	/**
	 * Model attr copy version , the model just use default "DbKit.brokenConfig"
	 */
	public Map<Object, Object> attrsCp() {
		Map<Object, Object> attrs = new HashMap<Object, Object>();
		String[] attrNames = this._getAttrNames();
		for (String attr : attrNames) {
			attrs.put(attr, this.get(attr));
		}
		return attrs;
	}

	/**
	 * auto sync to the redis: true-sync,false-cancel, default true
	 * @param syncToRedis
	 */
	public void syncToRedis(boolean syncToRedis) {
		this.syncToRedis = syncToRedis;
	}

	/**
	 * shot cache's name.
	 * if current cacheName != the old cacheName, will reset old cache, update cache use the current cacheName and open syncToRedis.
	 */
	public void shotCacheName(String cacheName) {
		//reset cache
		if (StrKit.notBlank(cacheName) && !cacheName.equals(this.cacheName)) {
			this.cache = null;
		}
		this.cacheName = cacheName;
		//auto open sync to redis
		this.syncToRedis = true;
		//update model redis mapping
		ModelRedisMapping.me().put(this.tableName(), this.cacheName);
	}
	
	/**
	 * get current model's table
	 */
	public Table table() {
		return this._getTable();
	}

	/**
	 * get current model's tablename
	 */
	public String tableName() {
		return this.table().getName();
	}

	/**
	 * get primary key.
	 * if there is not found the primary key will throw the IllegalArgumentException.
	 */
	public String primaryKey() {
		String[] primaryKeys = this.table().getPrimaryKey();
		if (primaryKeys.length >= 1) {
			return primaryKeys[0];
		}
		throw new IllegalArgumentException("Not Found ,The Primary Key[s].");
	}
	
	/**
	 * get primary key value
	 * if there is not found the primary key will throw the IllegalArgumentException.
	 */
	public Object primaryKeyValue() {
		return this.get(this.primaryKey());
	}

	/**
	 * save to db, if `syncToRedis` is true , this model will save to redis in the same time.
	 */
	@Override
	public boolean save() {
		boolean ret = super.save();
		if (this.syncToRedis && ret) {
			this.saveToRedis(this);
		}
		return ret;
	}

	/**
	 * delete from db, if `syncToRedis` is true , this model will delete from redis in the same time.
	 */
	@Override
	public boolean delete() {
		boolean ret = super.delete();
		if (this.syncToRedis && ret) {
			this.cache().del(this.redisKey(this));
		}
		return ret;
	}

	/**
	 * update db, if `syncToRedis` is true , this model will update redis in the same time.
	 */
	@Override
	public boolean update() {
		boolean ret = super.update();
		if (this.syncToRedis && ret) {
			this.cache().hmset(this.redisKey(this), this.attrsCp());
		}
		return ret;
	}
	
	/**
	 * <b>Advanced Function</b>. 
	 * you can find Models: use any column value can do this. the data from DB.
	 */
	public List<M> find() {
		List<M> ret = this.find(SqlpKit.select(this));
		if (this.syncToRedis && null != ret) {
			for (M model : ret) {
				this.saveToRedis(model);
			}
		}
		return ret;
	}
	
	/**
	 * <b>Advanced Function</b>. 
	 * you can find FirstOne Model: use any column value can do this.the data from DB.
	 */
	public M findOne() {
		M m = this.findFirst(SqlpKit.selectOne(this));
		if (this.syncToRedis && null != m) {
			this.saveToRedis(m);
		}
		return m;
	}
	
	/**
	 * Use the redis key find the Model from cache.
	 */
	@SuppressWarnings("unchecked")
	public M findByCache() {
		String[] primaryKeys = this.table().getPrimaryKey();
		if (null == primaryKeys || primaryKeys.length == 0) {
			throw new IllegalArgumentException("The PrimaryKey[ALL]'s value is null. Please set value to it.");
		}
		
		for (String pk : primaryKeys) {
			Object val = this.get(pk);
			if (null == val) {
				throw new IllegalArgumentException(String.format("The PrimaryKey[%s]'s value is null. Please set value to it.", pk));
			}
		}
		
		Map<String, Object> attrs = this.cache().hgetAll(this.redisKey(this));
		return this.put(attrs);
	}
}
