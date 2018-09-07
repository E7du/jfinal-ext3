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

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.List;

import com.alibaba.excel.read.context.AnalysisContext;
import com.alibaba.excel.read.event.AnalysisEventListener;
import com.alibaba.excel.write.exception.ExcelGenerateException;
import com.jfinal.ext.kit.AppDateFormatKit;
import com.jfinal.ext.kit.TypeKit;
import com.jfinal.ext.kit.poi.PoiException;
import com.jfinal.ext.plugin.activerecord.ModelExt;

@SuppressWarnings("rawtypes")
public class ExcelListener extends AnalysisEventListener {

	private ExcelRowReadFeedback feedback;
	private List<ModelExt<?>> datas;
	private ExcelRule readRule;

	public ExcelListener() {
		this.datas = new ArrayList<ModelExt<?>>();
	}
	
	public void setReadRule(ExcelRule readRule) {
		this.readRule = readRule;
	}

	public void setReadFeedback(ExcelRowReadFeedback feedback) {
		this.feedback = feedback;
	}
	
	public List<ModelExt<?>> getDatas() {
		return this.datas;
	}

	@Override
	public void doAfterAllAnalysed(AnalysisContext context) {

	}

	@SuppressWarnings("unchecked")
	@Override
	public void invoke(Object object, AnalysisContext context) {
		if (null == this.readRule) {
			throw (new PoiException("Please set read rule first."));
		}
		System.out.println("当前表格数" + context.getCurrentSheet().getSheetNo() + " 当前行：" + context.getCurrentRowNum());

		
		Integer currentSheetNo = context.getCurrentSheet().getSheetNo();
		Integer currentRow = context.getCurrentRowNum();
		if (this.readRule.getHasHedaer(currentSheetNo) && currentRow == 0) {
			return;
		}
		
		Class<?> clazz = (Class<?>) context.getCustom();
		ModelExt<?>  model = null;
		if (ModelExt.class.isAssignableFrom(clazz)) {
			try {
				model = (ModelExt<?>) clazz.newInstance();
			} catch (Exception e) {
				throw new ExcelGenerateException(e);
			}
			ByteArrayOutputStream byteOut = new ByteArrayOutputStream();
			ObjectOutputStream out = null;
			try {
				out = new ObjectOutputStream(byteOut);
				out.writeObject(object);
				ByteArrayInputStream byteIn = new ByteArrayInputStream(byteOut.toByteArray());
				ObjectInputStream in = new ObjectInputStream(byteIn);
				List<String> dest = (List<String>) in.readObject();
				
				// put data to model
				String format = AppDateFormatKit.getAppDateFormat();
				ExcelColumn col;
				for (int i = 0; i < dest.size(); i++) {
					col = this.readRule.getColumn(i);
					if (null == col) {
						continue;
					}
					String val = dest.get(i);
					if (null != val) {
						String attr = col.getAttr();
						Object value = TypeKit.convert(val, model.attrType(attr), format);
						if (value != null) {
							model.set(attr, value);
						}
					}
				}
			} catch (Exception e) {
				throw new ExcelGenerateException(e);
			} finally {
				if (null != out) {
					try {
						out.close();
					} catch (IOException e) {
						throw new ExcelGenerateException(e);
					}
				}
				if (null != byteOut) {
					try {
						byteOut.close();
					} catch (IOException e) {
						throw new ExcelGenerateException(e);
					}
				}
			}
		}
		
		this.datas.add(model);
		this.read(model);
	}

	private void read(ModelExt<?> model) {
		if (null != this.feedback) {
			this.feedback.readRow(model);
		}
	}
}
