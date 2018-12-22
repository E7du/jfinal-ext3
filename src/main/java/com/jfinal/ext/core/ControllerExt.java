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
package com.jfinal.ext.core;

import java.lang.reflect.Field;
import java.math.BigInteger;
import java.util.List;

import com.jfinal.core.NotAction;
import com.jfinal.ext.kit.UploadPathKit;
import com.jfinal.log.Log;
import com.jfinal.upload.UploadFile;

/**
 * @author Jobsz Jun 22, 20154:15:48 PM
 */
public abstract class ControllerExt extends com.jfinal.core.Controller {

	protected Log log = Log.getLog(this.getClass());
	
	public ControllerExt() {
		this.onInitService();
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })	
	private void onInitService() {
		Field[] fields = this.getClass().getDeclaredFields();
		for (int i = 0; i < fields.length; i++) {
			Field field = fields[i];
			Class clazz = field.getType();
			if (Service.class.isAssignableFrom(clazz) && clazz != Service.class) {
				try {
					if (!field.isAccessible()) {
						field.setAccessible(true);
					}
					
					field.set(this,Service.getInstance(clazz, this));
				} catch (IllegalAccessException e) {
					this.onExceptionError(e);
					throw new RuntimeException();
				}
			}
		}
	}

	/**
	 * Get upload file save to date path.
	 */
	@NotAction
	public List<UploadFile> getFilesSaveToDatePath(Integer maxPostSize, String encoding) {
		return super.getFiles(UploadPathKit.getDatePath(), maxPostSize, encoding);
	}

	@NotAction
	public UploadFile getFileSaveToDatePath(String parameterName, Integer maxPostSize, String encoding) {
		return super.getFile(parameterName, UploadPathKit.getDatePath(), maxPostSize, encoding);
	}

	@NotAction
	public List<UploadFile> getFilesSaveToDatePath(Integer maxPostSize) {
		return super.getFiles(UploadPathKit.getDatePath(), maxPostSize);
	}

	@NotAction
	public UploadFile getFileSaveToDatePath(String parameterName, Integer maxPostSize) {
		return super.getFile(parameterName, UploadPathKit.getDatePath(), maxPostSize);
	}

	@NotAction
	public List<UploadFile> getFilesSaveToDatePath() {
		return super.getFiles(UploadPathKit.getDatePath());
	}

	@NotAction
	public UploadFile getFileSaveToDatePath(String parameterName) {
		return super.getFile(parameterName, UploadPathKit.getDatePath());
	}
	
	// --------

	/**
	 * Returns the value of a request parameter and convert to BigInteger.
	 * @param name a String specifying the name of the parameter
	 * @return a BigInteger representing the single value of the parameter
	 */
	@NotAction
	public BigInteger getParaToBigInteger(String name){
		return this.toBigInteger(getPara(name), null);
	}
	
	/**
	 * Returns the value of a request parameter and convert to BigInteger with a default value if it is null.
	 * @param name a String specifying the name of the parameter
	 * @return a BigInteger representing the single value of the parameter
	 */
	@NotAction
	public BigInteger getParaToBigInteger(String name,BigInteger defaultValue){
		return this.toBigInteger(getPara(name), defaultValue);
	}
	
	private BigInteger toBigInteger(String value, BigInteger defaultValue) {
		if (value == null || "".equals(value.trim()))
			return defaultValue;
		return (new BigInteger(value));
	}
	
	/**
	 * Reflect Exception
	 * @param e
	 */
	@NotAction
	public abstract void onExceptionError(Exception e);
}
