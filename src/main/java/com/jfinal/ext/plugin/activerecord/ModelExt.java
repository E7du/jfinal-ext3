package com.jfinal.ext.plugin.activerecord;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.jfinal.ext.kit.SqlpKit;
import com.jfinal.kit.StrKit;
import com.jfinal.plugin.activerecord.Model;
import com.jfinal.plugin.activerecord.Table;
import com.jfinal.plugin.redis.Cache;
import com.jfinal.plugin.redis.Redis;

public abstract class ModelExt<M extends ModelExt<M>> extends Model<M> {

	private static final long serialVersionUID = -6061985137398460903L;

	private boolean syncToRedis = false;
	private Cache cache = null;
	private String cacheName = null;

	/**
	 * redis key: tablename id1 | id2 | id3...
	 * eg: the user has three primarykeys id1=1,id2=2,id3=3, so the redisKey is 'user1|2|3'.
	 * @param m
	 * @return redis key
	 */
	private String redisKey(ModelExt<?> m) {
		Table table = m._getTable();
		StringBuilder key = new StringBuilder(table.getName());
		//fetch primary keys' values
		String[] primaryKeys = table.getPrimaryKey();
		List<Object> primaryKeyValues = new ArrayList<Object>();
		for (String primaryKey: primaryKeys) {
			Object val = m.get(primaryKey);
			if (null != val) {
				primaryKeyValues.add(val);
			}
		}
		//format key
		boolean first = true;
		for (Object primaryKeyValue : primaryKeyValues) {
			if (first) {
				first = false;
			} else {
				key.append("|");
			}
			key.append(primaryKeyValue);
		}
		return key.toString();
	}
	
	private void saveToRedis(ModelExt<?> m) {
		this.getCache().hmset(this.redisKey(m), m.getAttrsCp());
	}
	
	private Cache getCache() {
		if (null != this.cache) {
			return this.cache;
		}
		
		if (StrKit.notBlank(this.cacheName)) {
			this.cache = Redis.use(this.cacheName);
		} else {
			this.cache = Redis.use();
		}
		return this.cache;
	}
	
	/**
	 * Model attr copy version , the model just use default "DbKit.brokenConfig"
	 */
	public Map<Object, Object> getAttrsCp() {
		Map<Object, Object> attrs = new HashMap<Object, Object>();
		String[] attrNames = this._getAttrNames();
		for (String attr : attrNames) {
			attrs.put(attr, this.get(attr));
		}
		return attrs;
	}

	/**
	 * auto sync to the redis: true-sync,false-cancel
	 * @param syncToRedis
	 */
	public void syncToRedis(boolean syncToRedis) {
		this.syncToRedis = syncToRedis;
	}

	/**
	 * set cache's name
	 * @param cacheName, if current cacheName != the old cacheName, will reset old cache and update cache use the current cacheName.
	 */
	public void setCacheName(String cacheName) {
		//reset cache
		if (null != cacheName && !cacheName.equals(this.cacheName)) {
			this.cache = null;
		}
		this.cacheName = cacheName;
	}
	
	/**
	 * get current model's table
	 */
	public Table getTable() {
		return this._getTable();
	}

	/**
	 * get current model's tablename
	 */
	public String getTableName() {
		return this.getTable().getName();
	}

	/**
	 * get primary key.
	 * if there is not found the primary key will throw the IllegalArgumentException.
	 */
	public String getPrimaryKey() {
		String[] primaryKeys = this.getTable().getPrimaryKey();
		if (primaryKeys.length >= 1) {
			return primaryKeys[0];
		}
		throw new IllegalArgumentException("Not Found ,The Primary Key[s].");
	}
	
	/**
	 * get primary key value
	 * if there is not found the primary key will throw the IllegalArgumentException.
	 */
	public Object getPrimaryKeyValue() {
		return this.get(this.getPrimaryKey());
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
			this.getCache().del(this.redisKey(this));
		}
		return ret;
	}

	/**
	 * update from db, if `syncToRedis` is true , this model will update from redis in the same time.
	 */
	@Override
	public boolean update() {
		boolean ret = super.update();
		if (this.syncToRedis && ret) {
			this.getCache().hmset(this.redisKey(this), this.getAttrsCp());
		}
		return ret;
	}
	
	/**
	 * <b>Advanced Function</b>. 
	 * you can find Models: use any column value can do this.
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
	 * Use the redis key find the Model from cache.
	 */
	@SuppressWarnings("unchecked")
	public M findByCache() {
		Object pkValue = this.getPrimaryKeyValue();
		if (null == pkValue) {
			throw new IllegalArgumentException("The PrimaryKey's value is null. Please set value to it.");
		}
		Map<String, Object> attrs = this.getCache().hgetAll(this.redisKey(this));
		return this.put(attrs);
	}
}
