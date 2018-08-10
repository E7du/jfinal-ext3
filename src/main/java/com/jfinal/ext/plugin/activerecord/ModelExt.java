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
import java.util.Map.Entry;
import java.util.Set;

import com.google.common.collect.Lists;
import com.jfinal.ext.kit.ArrayKit;
import com.jfinal.ext.kit.SqlpKit;
import com.jfinal.ext.plugin.redis.ModelRedisMapping;
import com.jfinal.kit.StrKit;
import com.jfinal.plugin.activerecord.Db;
import com.jfinal.plugin.activerecord.Model;
import com.jfinal.plugin.activerecord.Record;
import com.jfinal.plugin.activerecord.SqlPara;
import com.jfinal.plugin.activerecord.Table;
import com.jfinal.plugin.activerecord.TableMapping;
import com.jfinal.plugin.redis.Cache;
import com.jfinal.plugin.redis.Redis;

public abstract class ModelExt<M extends ModelExt<M>> extends Model<M> {

	private static final long serialVersionUID = -6061985137398460903L;

	private static final String RECORDS = "records:";
	//default sync to redis
	private boolean syncToRedis = GlobalSyncRedis.syncState();
	private Cache redis = null;
	private String cacheName = null;
	//call back
    private List<CallbackListener> callbackListeners = Lists.newArrayList();
    
    /**
     * add Call back listener
     * @param callbackListener
     */
    public void addCallbackListener(CallbackListener callbackListener) {
        this.callbackListeners.add(callbackListener);
    }

	/**
	 * redis key: records:tablename: id1 | id2 | id3...
	 * eg: the user has three primarykeys id1=1,id2=2,id3=3, so the redisKey is 'records:user:1|2|3'.
	 * @return redis key
	 */
	private String redisKey(ModelExt<?> m) {
		Table table = m.table();
		StringBuilder key = new StringBuilder();
		key.append(RECORDS);
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
		//save total data
		this.redis().hmset(this.redisKey(m), m.attrsCp());
	}
	
	private Cache redis() {
		if (null != this.redis) {
			return this.redis;
		}
		
		if (StrKit.isBlank(this.cacheName)) {
			this.cacheName = ModelRedisMapping.me().getCacheName(this.tableName());
		}
		
		if (StrKit.notBlank(this.cacheName)) {
			this.redis = Redis.use(this.cacheName);
		} else {
			this.redis = Redis.use();
		}
		if (null == this.redis) {
			throw new IllegalArgumentException(String.format("The Cache with the name '%s' was Not Found.", this.cacheName));	
		}
		return this.redis;
	}
	
	/**
	 * Use the columns that must contains primary keys fetch Data from db, and use the fetched primary keys fetch from redis.
	 * @param columns
	 */
	@SuppressWarnings("unchecked")
	private List<M> fetchDatasFromRedis(String[] columns) {
		// use columns fetch primary keys from db.
		List<M> fetchDatas = this.find(SqlpKit.select(this, columns));
		if (null == fetchDatas || fetchDatas.size() == 0) {
			return fetchDatas;
		}
		for (M m : fetchDatas) {
			// fetch data from redis
			if (null == m || m._isNull()) {
				continue;
			}
			Map<String, Object> attrs = this.redis().hgetAll(this.redisKey(m));
			m.put(attrs);
		}
		return fetchDatas;
	}
	
	@SuppressWarnings("unchecked")
	private M fetchOneFromRedis(String[] columns) {
		// use columns fetch primary keys from db.
		M m = this.findFirst(SqlpKit.selectOne(this, columns));
		if (null == m || m._isNull()) {
			return m;
		}
		// use primay key fetch from redis
		Map<String, Object> attrs = this.redis().hgetAll(this.redisKey(m));
		return m.put(attrs);
	}

	/**
	 * current instance is Null or not.
	 */
	public boolean _isNull() {
		return this._getAttrs().isEmpty();
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
	 * Model Attrs
	 */
	public Map<String, Object> attrs() {
		return this._getAttrs();
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
			this.redis = null;
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
	 * All primary keys
	 */
	public String[] primaryKeys() {
		return this.table().getPrimaryKey();
	}

	/**
	 * get numeric primary key.
	 * if there is not found the primary key will throw the IllegalArgumentException.
	 */
	public String primaryKey() {
		String[] primaryKeys = this.primaryKeys();
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
		for (CallbackListener callbackListener : this.callbackListeners) {
			callbackListener.beforeSave(this);
		}
		boolean ret = super.save();
		if (this.syncToRedis && ret) {
			this.saveToRedis(this);
		}
		for (CallbackListener callbackListener : this.callbackListeners) {
			callbackListener.afterSave(this);
		}
		return ret;
	}

	/**
	 * delete from db, if `syncToRedis` is true , this model will delete from redis in the same time.
	 */
	@Override
	public boolean delete() {
        for (CallbackListener callbackListener : this.callbackListeners) {
            callbackListener.beforeDelete(this);
        }
		boolean ret = super.delete();
		if (this.syncToRedis && ret) {
			this.redis().del(this.redisKey(this));
		}
        for (CallbackListener callbackListener : this.callbackListeners) {
            callbackListener.afterDelete(this);
        }
		return ret;
	}

	/**
	 * update db, if `syncToRedis` is true , this model will update redis in the same time.
	 */
	@Override
	public boolean update() {
        for (CallbackListener callbackListener : this.callbackListeners) {
            callbackListener.beforeUpdate(this);
        }
		boolean ret = super.update();
		if (this.syncToRedis && ret) {
			this.redis().hmset(this.redisKey(this), this.attrsCp());
		}
        for (CallbackListener callbackListener : this.callbackListeners) {
            callbackListener.afterUpdate(this);
        }
		return ret;
	}

	/**
	 * <b>Advanced Function</b>. 
	 * you can fetch Models: will fetch all columns, use any column value can do this.
	 * if `syncToRedis` is true, the data from redis , else from DB.
	 */
	public List<M> fetch() {
		//from db
		if (!this.syncToRedis) {
			return this.find(SqlpKit.select(this));
		}
		//from redis
		// fetch primary keys
		return this.fetchDatasFromRedis(this.primaryKeys());
	}

	/**
	 * <b>Advanced Function</b>. 
	 * List All data just contains columns, use any column value can do this. the data from DB.
	 * @param columns: will fetch columns
	 */
	public List<M> fetch(String... columns) {
		//from db
		if (!this.syncToRedis) {
			return this.find(SqlpKit.select(this, columns));
		}
		//from redis
		//union the fetch columns , make the column contains primary keys.
		String[] fetchColoumns = ArrayKit.union(this.primaryKeys(), columns);
		return this.fetchDatasFromRedis(fetchColoumns);
	}
	
	/**
	 * <b>Advanced Function</b>. 
	 * you can fetch FirstOne Model: use any column value can do this.the data from DB.
	 */
	public M fetchOne() {
		//from db
		if (!this.syncToRedis) {
			return this.findFirst(SqlpKit.selectOne(this));
		}
		//from redis
		return this.fetchOneFromRedis(this.primaryKeys());
	}
	
	/**
	 * <b>Advanced Function</b>. 
	 * you can fetch FirstOne Model:just contains columns, use any column value can do this.the data from DB.
	 */
	public M fetchOne(String... columns) {
		//from db
		if (!this.syncToRedis) {
			return this.findFirst(SqlpKit.selectOne(this, columns));
		}
		//from redis
		String[] fetchColoumns = ArrayKit.union(this.primaryKeys(), columns);
		return this.fetchOneFromRedis(fetchColoumns);
	}
	
	/**
	 * List All data just contains the primary keys.
	 */
	public List<M> fetchPrimaryKeysOnly() {
		return this.find(SqlpKit.select(this, this.primaryKeys()));
	}
	
	/**
	 * Use the redis key:based on primary key fetch the Model from redis.
	 */
	@SuppressWarnings("unchecked")
	public M fetchByRedis() {
		String[] primaryKeys = this.primaryKeys();
		if (null == primaryKeys || primaryKeys.length == 0) {
			throw new IllegalArgumentException("The PrimaryKey[ALL]'s value is null. Please set value to it.");
		}
		
		for (String pk : primaryKeys) {
			Object val = this.get(pk);
			if (null == val) {
				throw new IllegalArgumentException(String.format("The PrimaryKey[%s]'s value is null. Please set value to it.", pk));
			}
		}
		
		Map<String, Object> attrs = this.redis().hgetAll(this.redisKey(this));
		return this.put(attrs);
	}

	/**
	 * Data Count
	 */
	public Long dataCount() {
		SqlPara sql = SqlpKit.select(this, "count(*) AS cnt");
		Record record = Db.findFirst(sql);
		if (null != record) {
			return record.get("cnt");
		}
		return 0L;
	}

	@SuppressWarnings("unchecked")
	private M cp(boolean cpAttrs) {
		M m = null;
		try {
			m = (M) this._getUsefulClass().newInstance();
		} catch (InstantiationException | IllegalAccessException e) {
			e.printStackTrace();
		}
		if (cpAttrs) {
			m.put(this._getAttrs());
		}
		return m;
	}
	

	/**
	 * Clone new instance[wrapper clone]
	 */
	public M cp() {
		return this.cp(true);
	}
	
	/**
	 * Clone new instance[wrapper clone], just link attrs values
	 * @param attrs
	 */
	public M cp(String... attrs) {
		M m = this.cp(false);
		for (String attr : attrs) {
			m.set(attr, this.get(attr));
		}
		return m;
	}
	
	/**
	 * check current instance is equal obj or not.[wrapper equal]
	 * @param obj
	 * @return true:equal, false:not equal.
	 */
	public boolean eq(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (this._getUsefulClass() != obj.getClass())
            return false;
        
        Model<?> other = (Model<?>) obj;
        Table tableinfo = this._getTable();
        Set<Entry<String, Object>> attrsEntrySet = this._getAttrsEntrySet();
        for (Entry<String, Object> entry : attrsEntrySet) {
            String key = entry.getKey();
            Object value = entry.getValue();
            Class<?> clazz = tableinfo.getColumnType(key);
            if (clazz == Float.class) {
            } else if (clazz == Double.class) {
            } else if (clazz == Model.class) {
            } else {
                if (value == null) {
                    if (other.get(key) != null)
                        return false;
                } else if (!value.equals(other.get(key)))
                    return false;
            }
        }
        return true;		
	}
	
	/**
	 * wrapper hash code
	 */
	public int hcode() {
		final int prime = 31;
		int result = 1;
		Table tableinfo = TableMapping.me().getTable(this._getUsefulClass());
		Set<Entry<String, Object>> attrsEntrySet = this._getAttrsEntrySet();
		for (Entry<String, Object> entry : attrsEntrySet) {
			String key = entry.getKey();
			Object value = entry.getValue();
			Class<?> clazz = tableinfo.getColumnType(key);
			if (clazz == Integer.class) {
				result = prime * result + (Integer) value;
			} else if (clazz == Short.class) {
				result = prime * result + (Short) value;
			} else if (clazz == Long.class) {
				result = prime * result + (int) ((Long) value ^ ((Long) value >>> 32));
			} else if (clazz == Float.class) {
				result = prime * result + Float.floatToIntBits((Float) value);
			} else if (clazz == Double.class) {
				long temp = Double.doubleToLongBits((Double) value);
				result = prime * result + (int) (temp ^ (temp >>> 32));
			} else if (clazz == Boolean.class) {
				result = prime * result + ((Boolean) value ? 1231 : 1237);
			} else if (clazz == Model.class) {
				result = this.hcode();
			} else {
				result = prime * result + ((value == null) ? 0 : value.hashCode());
			}
		}
		return result;
	}
	
	/**
	 * Convert to Record
	 */
	public Record convertToRecord() {
		Record r = new Record();
		r.setColumns(this._getAttrs());
		return r;
	}
}
