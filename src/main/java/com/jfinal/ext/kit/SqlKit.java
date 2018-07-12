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

/**
 * @author Jobsz
 *
 */
final public class SqlKit {
	
	public static final String select = "SELECT";
	public static final String update = "UPDATE";
	public static final String set = "SET";
	public static final String insert = "INSERT INTO";
	public static final String values = "VALUES";
	public static final String from = "FROM";
	public static final String where = "WHERE";
	public static final String like = "LIKE";
	public static final String and = "AND";
	public static final String or = "OR";
	public static final String orderby = "ORDER BY";
	public static final String limit = "LIMIT";

	private static final String sapce = " ";
	private StringBuilder sql = null;

	private enum ORDER{
		DESC,
		ASC
	};
	
	private SqlKit orderBy(String condition, ORDER order){
		sql.append(SqlKit.orderby).append(SqlKit.sapce).append(condition).append(SqlKit.sapce).append(order.toString());
		return this;
	}
	
	public static class Column {
		
		private String colName = null;
		private String as = null;
		
		public Column(String colName, String as) {
			this.colName = colName;
			this.as = as;
		}
		
		public String column() {
			return new StringBuilder(this.colName).append(SqlKit.sapce).append(this.as).toString();
		}
	}

	//==========================
	
	public SqlKit(){
		sql = new StringBuilder();
	}
	
	public SqlKit select(String... selects){
		sql.append(SqlKit.select).append(SqlKit.sapce);
		int index = 0;
		for (String string : selects) {
			sql.append(string);
			if (index != selects.length - 1) {
				sql.append(",").append(SqlKit.sapce);
			} else {
				sql.append(SqlKit.sapce);
			}
			index++;
		}
		return this;
	}
	
	public Column column(String col, String as) {
		return new Column(col, as);
	}
	
	public SqlKit select(Column... cols) {
		sql.append(SqlKit.select).append(SqlKit.sapce);
		int index = 0;
		for (Column col : cols) {
			sql.append(col.column());
			if (index != cols.length - 1) {
				sql.append(",").append(SqlKit.sapce);
			} else {
				sql.append(SqlKit.sapce);
			}
			index++;
		}
		return this;
	}
	
	public SqlKit update(String tableName) {
		sql.append(SqlKit.update).append(SqlKit.sapce).append(tableName).append(SqlKit.sapce);
		return this;
	}
	
	/**
	 * set("name","新的值","addr","新的值")
	 * @param columns
	 * @return
	 */
	public SqlKit set(Object... columnsValues) {
		int len = columnsValues.length;
		if (len % 2 != 0) {
			throw new IllegalArgumentException("wrong number of arguments for set, columnsValues length can not be odd");
		}
		sql.append(SqlKit.set).append(SqlKit.sapce);
		
		StringBuilder column = null;
		for (int index = 0; index < len; index++) {
			column = new StringBuilder();
			if (index % 2 == 0) {
				column.append(columnsValues[index]).append(SqlKit.sapce).append("=");
			} else {
				column.append(columnsValues[index]);
				if (index != len - 1) {
					column.append(",");
				}
			}
			sql.append(column).append(SqlKit.sapce);
		}
		return this;
	}
	
	public SqlKit insert(String tableName) {
		sql.append(SqlKit.insert)
		.append(SqlKit.sapce)
		.append(tableName)
		.append(SqlKit.sapce);
		return this;
	}
	
	public SqlKit values(Object... columnsValues) {
		int len = columnsValues.length;
		if (len % 2 != 0) {
			throw new IllegalArgumentException("wrong number of arguments for values, columnsValues length can not be odd");
		}
		
		sql.append(SqlKit.values).append(SqlKit.sapce);
		sql.append("(").append(SqlKit.sapce);
		StringBuilder column = null;
		for (int index = 0; index < len; index++) {
			column = new StringBuilder();
			if (index % 2 == 0) {
				column.append(columnsValues[index]).append(SqlKit.sapce).append("=");
			} else {
				column.append(columnsValues[index]);
				if (index != len - 1) {
					column.append(",");
				}
			}
			sql.append(column).append(SqlKit.sapce);
		}
		sql.append(")");
		return this;
	}
	
	public SqlKit from(String... tableNames){
		sql.append(SqlKit.from).append(SqlKit.sapce);
		int index = 0;
		for (String string : tableNames) {
			sql.append(string);
			if (index != tableNames.length - 1) {
				sql.append(",").append(SqlKit.sapce);
			}else {
				sql.append(SqlKit.sapce);
			}
			index++;
		}
		return this;
	}
	
	public SqlKit where(String where){
		sql.append(SqlKit.where).append(SqlKit.sapce).append(where).append(SqlKit.sapce);
		return this;
	}
	
	//TODO 未完成
	public SqlKit like(String like) {
		throw new IllegalArgumentException("not finished");
//		sql.append(SqlKit.where).append(SqlKit.sapce).append(SqlKit.like).append(SqlKit.sapce);
//		return this;
	}
	
	public SqlKit and(String condition){
		sql.append(SqlKit.and).append(SqlKit.sapce).append(condition).append(SqlKit.sapce);
		return this;
	}
	
	public SqlKit or(String condition){
		sql.append(SqlKit.or).append(SqlKit.sapce).append(condition).append(SqlKit.sapce);
		return this;
	}
	
	public SqlKit ascOrderBy(String condition) {
		return this.orderBy(condition, ORDER.ASC);
	}
	
	public SqlKit descOrderBy(String condition) {
		return this.orderBy(condition, ORDER.DESC);
	}

	public SqlKit limit(String... params) {
		if (params.length > 2) {
			throw new IllegalArgumentException("more params");
		}
		
		sql.append(SqlKit.sapce).append(SqlKit.limit).append(SqlKit.sapce);
		
		int index = 0;
		for (String param : params) {
			sql.append(param);
			if (index != params.length - 1) {
				sql.append(",").append(SqlKit.sapce);
			}else {
				sql.append(SqlKit.sapce);
			}
			index++;
		}
		return this;
	}
	
	public SqlKit append(String append) {
		sql.append(append);
		return this;
	}
	
	public String sql(){
		String _sql = sql.toString();
		if (!_sql.endsWith(";")) {
			sql.append(";");
			return sql.toString();
		}
		return _sql;
	}
}
