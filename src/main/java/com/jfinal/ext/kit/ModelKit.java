/**
 * 
 */
package com.jfinal.ext.kit;

import com.jfinal.plugin.activerecord.Model;
import com.jfinal.plugin.activerecord.Table;
import com.jfinal.plugin.activerecord.TableMapping;

/**
 * ModelKit.java
 * @author Jobsz
 *
 */
@SuppressWarnings("rawtypes")
public final class ModelKit {

	public static Table getTable(Class<? extends Model> modelClass) {
		return TableMapping.me().getTable(modelClass);
	}
	
	public static String[] getPrimarykeys(Class<? extends Model> modelClass) {
		return ModelKit.getTable(modelClass).getPrimaryKey();
	}
	
	public static String getPrimaryKey(Class<? extends Model> modelClass) {
		String[] primaryKeys = ModelKit.getPrimarykeys(modelClass);
		if (primaryKeys.length >= 1) {
			return primaryKeys[0];
		}
		return "";
	}
	
}
