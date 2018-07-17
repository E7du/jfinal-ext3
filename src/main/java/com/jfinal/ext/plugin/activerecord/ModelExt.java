package com.jfinal.ext.plugin.activerecord;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.jfinal.ext.kit.SqlpKit;
import com.jfinal.plugin.activerecord.Model;
import com.jfinal.plugin.activerecord.Table;
import com.jfinal.plugin.redis.Redis;

public abstract class ModelExt<M extends ModelExt<M>> extends Model<M> {

	private static final long serialVersionUID = -6061985137398460903L;

	private boolean syncToRedis = false;

	// model redis key: tablename id1 | id2 | id3...
	private String modelRedisKey(ModelExt<?> m) {
		Table table = m._getTable();
		StringBuilder key = new StringBuilder(table.getName());
		//fetch primary keys' values
		String[] primaryKeys = table.getPrimaryKey();
		List<Object> primaryKeyValues = new ArrayList<Object>();
		for (String primaryKey: primaryKeys) {
			primaryKeyValues.add(m.get(primaryKey));
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
		Redis.use().hmset(this.modelRedisKey(m), m.getAttrsCp());
	}

	//get current model's table
	public Table getTable() {
		return this._getTable();
	}

	//get current model's tablename
	public String getTableName() {
		return this.getTable().getName();
	}

	//get primary key
	public String getPrimaryKey() {
		String[] primaryKeys = this.getTable().getPrimaryKey();
		if (primaryKeys.length >= 1) {
			return primaryKeys[0];
		}
		throw new IllegalArgumentException("Not Found!the primaryKey.");
	}
	
	// get primary key value
	public Object getPrimaryKeyValue() {
		return this.get(this.getPrimaryKey());
	}

	//Model attr copy version , the model just use default "DbKit.brokenConfig"
	public Map<Object, Object> getAttrsCp() {
		Map<Object, Object> attrs = new HashMap<Object, Object>();
		String[] attrNames = this._getAttrNames();
		for (String attr : attrNames) {
			attrs.put(attr, this.get(attr));
		}
		return attrs;
	}

	// auto sync to the redis
	public void setSyncToRedis(boolean syncToRedis) {
		this.syncToRedis = syncToRedis;
	}

	@Override
	public boolean save() {
		boolean ret = super.save();
		if (this.syncToRedis && ret) {
			this.saveToRedis(this);
		}
		return ret;
	}

	@Override
	public boolean delete() {
		boolean ret = super.delete();
		if (this.syncToRedis && ret) {
			Redis.use().del(this.modelRedisKey(this));
		}
		return ret;
	}

	@Override
	public boolean update() {
		boolean ret = super.update();
		if (this.syncToRedis && ret) {
			Redis.use().hmset(this.modelRedisKey(this), this.getAttrsCp());
		}
		return ret;
	}
	
	//find Models: use any column value can do this.
	public List<M> find() {
		List<M> ret = this.find(SqlpKit.select(this));
		if (this.syncToRedis && null != ret) {
			for (M model : ret) {
				this.saveToRedis(model);
			}
		}
		return ret;
	}
	
	@SuppressWarnings("unchecked")
	public M findByCache() {
		Object pkValue = this.getPrimaryKeyValue();
		if (null == pkValue) {
			throw new IllegalArgumentException("The PrimaryKey's value is null. Please set value to it.");
		}
		Map<String, Object> attrs = Redis.use().hgetAll(this.modelRedisKey(this));
		return this.put(attrs);
	}
}
