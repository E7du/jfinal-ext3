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
package com.jfinal.ext.validate;

import java.util.Date;

import com.jfinal.core.Controller;
import com.jfinal.kit.StrKit;
import com.jfinal.resp.RespData;

/**
 * @author Jobsz
 *
 */
public abstract class ValidatorExt extends com.jfinal.validate.Validator {
	
	protected abstract void validateParams(Controller c);
	
	@Override
	protected void validate(Controller c) {
		this.setShortCircuit(true);
		this.validateParams(c);
	}
	
	@Override
	protected void handleError(Controller c) {
		String em = c.getAttrForStr(RespData.EM);
		if (StrKit.notBlank(em)) {
			c.removeAttr(RespData.EM);
			c.renderJson(RespData.failure(em));
			return;
		}
		c.renderError(403);
	}

	protected void validateRequired(String field) {
		super.validateRequired(field, RespData.EM, "参数" + field + "不能为空!");
	}

	protected void validateRequired(int index) {
		super.validateRequired(index, RespData.EM, "参数" + index + "不能为空!");
	}

	protected void validateRequiredString(String field) {
		super.validateRequiredString(field, RespData.EM, "参数" + field + "不能为空!");
	}

	protected void validateRequiredString(int index) {
		super.validateRequiredString(index, RespData.EM, "参数" + index + "不能为空!");
	}

	protected void validateInteger(String field, int min, int max) {
		super.validateInteger(field, min, max, RespData.EM, "参数"+ field + "范围在"+ min +"~"+ max +"!");
	}

	protected void validateInteger(int index, int min, int max) {
		super.validateInteger(index, min, max, RespData.EM, "参数"+ index + "范围在"+ min +"~"+ max +"!");
	}

	protected void validateInteger(String field) {
		super.validateInteger(field, RespData.EM, "参数" + field + "不能为空!");
	}

	protected void validateInteger(int index) {
		super.validateInteger(index, RespData.EM, "参数" + index + "不能为空!");
	}

	protected void validateLong(String field, long min, long max) {
		super.validateLong(field, min, max, RespData.EM, "参数" + field + "范围在"+ min +"~"+ max +"!");
	}

	protected void validateLong(int index, long min, long max) {
		super.validateLong(index, min, max, RespData.EM, "参数"+ index + "范围在"+ min +"~"+ max +"!");
	}

	protected void validateLong(String field) {
		super.validateLong(field, RespData.EM, "参数" + field + "不能为空!");
	}

	protected void validateLong(int index) {
		super.validateLong(index, RespData.EM, "参数" + index + "不能为空!");
	}

	protected void validateDouble(String field, double min, double max) {
		super.validateDouble(field, min, max, RespData.EM, "参数"+field + "范围在"+ min +"~"+ max +"!");
	}

	protected void validateDouble(String field) {
		super.validateDouble(field, RespData.EM, "参数" + field + "不能为空!");
	}

	protected void validateDate(String field) {
		super.validateDate(field, RespData.EM, "参数" + field + "不能为空!");
	}

	protected void validateDate(String field, Date min, Date max) {
		super.validateDate(field, min, max, RespData.EM, "参数" + field + "范围在" + min + "~" + max + "!");
	}

	protected void validateDate(String field, String min, String max) {
		super.validateDate(field, min, max, RespData.EM, "参数" + field + "范围在" + min + "~" + max + "!");
	}

	protected void validateEqualField(String field_1, String field_2) {
		super.validateEqualField(field_1, field_2, RespData.EM, field_1 + "!=" + field_2);
	}

	protected void validateEqualString(String s1, String s2) {
		super.validateEqualString(s1, s2, RespData.EM, s1 + "!=" + s2);
	}

	protected void validateEqualInteger(Integer i1, Integer i2) {
		super.validateEqualInteger(i1, i2, RespData.EM, i1 + "!=" + i2);
	}

	protected void validateEmail(String field) {
		super.validateEmail(field, RespData.EM, "参数" + field + "不是一个邮箱!");
	}

	protected void validateUrl(String field) {
		super.validateUrl(field, RespData.EM, "参数" + field + "不是一个合法的URL!");
	}

	protected void validateRegex(String field, String regExpression, boolean isCaseSensitive) {
		super.validateRegex(field, regExpression, isCaseSensitive, RespData.EM, "参数" + field + "不匹配!");
	}

	protected void validateRegex(String field, String regExpression) {
		super.validateRegex(field, regExpression, RespData.EM, "参数" + field + "不匹配!");
	}

	protected void validateString(String field, int minLen, int maxLen, String errorKey, String errorMessage) {
		super.validateString(field, minLen, maxLen, RespData.EM, "参数" + field + "长度在" + minLen + "~" + maxLen + "!");
	}

	protected void validateString(int index, int minLen, int maxLen, String errorKey, String errorMessage) {
		super.validateString(index, minLen, maxLen, RespData.EM, "参数" + index + "长度在" + minLen + "~" + maxLen + "!");
	}

	protected void validateBoolean(String field) {
		super.validateBoolean(field, RespData.EM, "参数" + field + "不能为空!");
	}

	protected void validateBoolean(int index, String errorKey, String errorMessage) {
		super.validateBoolean(index, RespData.EM, "参数" + index + "不能为空!");
	}

	protected void validateCaptcha(String field, String errorKey, String errorMessage) {
		super.validateCaptcha(field, RespData.EM, field + "验证码不对!");
	}
}
