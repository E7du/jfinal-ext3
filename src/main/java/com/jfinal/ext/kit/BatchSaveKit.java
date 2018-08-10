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

import java.util.List;
import java.util.Map;

import com.google.common.collect.Lists;
import com.jfinal.ext.plugin.activerecord.ModelExt;
import com.jfinal.plugin.activerecord.Db;
import com.jfinal.plugin.activerecord.DbKit;
import com.jfinal.plugin.activerecord.Table;

final public class BatchSaveKit {

	public static int[] batchSave(List<? extends ModelExt<?>> data) {
		return batchSave(data, data.size());
	}

	private static int[] batchSave(List<? extends ModelExt<?>> models, int batchSize) {
		ModelExt<?> m = models.get(0);
		Map<String, Object> attrs = m.attrs();
		Table table = m.table();

		StringBuilder sql = new StringBuilder();
		List<Object> paras = Lists.newArrayList();

		DbKit.getConfig().getDialect().forModelSave(table, attrs, sql, paras);

		Object[][] batchPara = new Object[models.size()][attrs.size()];

		for (int i = 0; i < models.size(); i++) {
			int j = 0;
			for (String key : attrs.keySet()) {
				batchPara[i][j++] = models.get(i).get(key);
			}
		}
		return Db.batch(sql.toString(), batchPara, batchSize);
	}
}
