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

import java.io.InputStream;

import com.alibaba.excel.support.ExcelTypeEnum;
import com.jfinal.ext.plugin.activerecord.ModelExt;
import com.jfinal.kit.StrKit;

public class ExcelReader {

	private com.alibaba.excel.ExcelReader reader;
	private ExcelListener listener;
	
	public ExcelReader(String fileName, Class<? extends ModelExt<?>> readToModel, boolean trim) {
		this();
		this.validateFile(fileName);
		this.reader = new com.alibaba.excel.ExcelReader(this.getInputStream(fileName), ExcelKit.getExcelType(fileName), readToModel, this.listener, trim);
	}
	
	public ExcelReader(String fileName, Class<? extends ModelExt<?>> readToModel) {
		this();
		this.validateFile(fileName);
		this.reader = new com.alibaba.excel.ExcelReader(this.getInputStream(fileName), ExcelKit.getExcelType(fileName), readToModel, this.listener, true);
	}
	
	public ExcelReader(InputStream in, ExcelTypeEnum excelTypeEnum, Object customContent, boolean trim) {
		this();
		this.reader = new com.alibaba.excel.ExcelReader(in, excelTypeEnum, customContent, this.listener, trim);
	}
	
	public ExcelReader(InputStream in, ExcelTypeEnum excelTypeEnum, Object customContent) {
		this();
		this.reader = new com.alibaba.excel.ExcelReader(in, excelTypeEnum, customContent, this.listener, true);
	}

	/**
	 * Set read feed back
	 * @param feedback
	 */
	public void setReadFeedback(ExcelRowReadFeedback feedback) {
		this.listener.setReadFeedback(feedback);
	}
	
	/**
	 * Set reader rule
	 * @param readRule
	 */
	public void setReadRule(ExcelRule readRule) {
		this.listener.setReadRule(readRule);
	}
	
	@SuppressWarnings("unchecked")
	public <T> T getDatas() {
		return (T)this.listener.getDatas();
	}
	
	public void read() {
		this.reader.read();
	}

	public void finish() {
		this.reader.finish();
	}
	
	private ExcelReader() {
		this.listener = new ExcelListener();
	}
	
	private void validateFile(String fileName) {
		if (StrKit.isBlank(fileName) 
				|| (!fileName.endsWith(ExcelTypeEnum.XLSX.getValue()) && !fileName.endsWith(ExcelTypeEnum.XLS.getValue()))) {
			throw new IllegalArgumentException("Please use the .xls or .xlsx file.");
		}
	}
	
    private InputStream getInputStream(String fileName) {
        return Thread.currentThread().getContextClassLoader().getResourceAsStream(fileName);
    }
  
}
