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
package com.jfinal.ext.kit;

import com.jfinal.ext.plugin.activerecord.ModelExt;
import com.jfinal.plugin.activerecord.SqlPara;

/**
 * SqlpKit.java
 * @author Jobsz
 *
 */
public final class SqlpKit {
	
	private static final String where = "WHERE";
	private static final String and = "AND";
	private static final String space = " ";
	
	private static final String SELECT_ST = "SELECT * FROM `%s` ";
	
	private enum FLAG {
		ALL,
		ONE
	}
	
	private static SqlPara select(ModelExt<?> model, FLAG flag) {
		SqlPara sqlPara = new SqlPara();
		String[] columns = model._getAttrNames();

		StringBuilder sbr = new StringBuilder(String.format(SELECT_ST, model.tableName()));

		boolean first = true;
		for (Integer i = 0; i < columns.length; i++) {
			if (first) {
				first = false;

				sbr.append(SqlpKit.space);
				sbr.append(SqlpKit.where);
				sbr.append(SqlpKit.space);
			} else {
				sbr.append(SqlpKit.space);
				sbr.append(SqlpKit.and);
				sbr.append(SqlpKit.space);
			}

			sbr.append("`");
			sbr.append(columns[i]);
			sbr.append("`");
			sbr.append(SqlpKit.space);
			sbr.append("= ?");
			sqlPara.addPara(model.get(columns[i]));
		}

		if (flag == FLAG.ONE) {
			sbr.append(SqlpKit.space);
			sbr.append("LIMIT 1");
		}
		sqlPara.setSql(sbr.toString());
		return sqlPara;
	}
	
	/**
	 * make SqlPara use the model with attr datas.
	 * @param model
	 */
	public static SqlPara select(ModelExt<?> model) {
		return SqlpKit.select(model, FLAG.ALL);
	}
	
	/**
	 * make SqlPara use the model with attr datas.
	 * @param model
	 */
	public static SqlPara selectOne(ModelExt<?> model) {
		return SqlpKit.select(model, FLAG.ONE);
	}
}
