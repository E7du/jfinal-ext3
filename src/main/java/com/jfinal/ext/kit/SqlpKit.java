/**
 * 
 */
package com.jfinal.ext.kit;

import com.jfinal.plugin.activerecord.Model;
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
	
	/**
	 * make SqlPara use the model with attr datas.
	 * @param model
	 * @return
	 */
	public static SqlPara select(Model<?> model) {
		SqlPara sqlPara = new SqlPara();
		String[] columns = model._getAttrNames();

		String tableName = ModelKit.getTable(model.getClass()).getName();
		StringBuilder sbr = new StringBuilder(String.format(SELECT_ST, tableName));

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

		sqlPara.setSql(sbr.toString());
		return sqlPara;
	}
	
}
