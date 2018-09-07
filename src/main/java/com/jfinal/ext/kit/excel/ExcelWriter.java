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
package com.jfinal.ext.kit.excel;

import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;

import cn.zhucongqi.excel.metadata.Sheet;
import cn.zhucongqi.excel.metadata.Table;
import cn.zhucongqi.excel.support.ExcelTypeEnum;
import com.jfinal.ext.plugin.activerecord.ModelExt;

public class ExcelWriter {

	private cn.zhucongqi.excel.ExcelWriter writer;
	
	public ExcelWriter(OutputStream outputStream, ExcelTypeEnum typeEnum) {
		this.writer = new cn.zhucongqi.excel.ExcelWriter(outputStream, typeEnum);
	}

	public void writeModel(List<? extends ModelExt<?>> models) {
		this.writer.write0(this.modelToString(models), new Sheet(1, 0));
	}

	public void writeModel(List<? extends ModelExt<?>> models, Table table) {
		this.writer.write0(this.modelToString(models), new Sheet(1, 0), table);
	}

	public void finish() {
		writer.finish();
	}
	
	private List<List<String>> modelToString(List<? extends ModelExt<?>> models) {
		List<List<String>> datas = new ArrayList<List<String>>();
		Object val;
		for (ModelExt<?> m : models) {
			List<String> rowData = new ArrayList<String>();
			String[] attrs = m._getAttrNames();
			for (int i = 0; i < attrs.length; i++) {
				val = m.get(attrs[i]);
				if (null == val) {
					rowData.add("");
				} else {
					rowData.add(val.toString());
				}
			}
			datas.add(rowData);
		}
		
		return datas;
	}
}
