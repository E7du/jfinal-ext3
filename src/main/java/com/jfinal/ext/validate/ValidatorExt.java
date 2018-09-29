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

import com.jfinal.core.Controller;

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
		c.renderError(403);
	}
	
//	/**
//	 * Validate long.
//	 */
//	protected void validateLong(String field, long min, long max, String errorKey, String errorMessage) {
//		try {
//			String value = controller.getPara(field);
//			long temp = Long.parseLong(value);
//			if (temp < min || temp > max)
//				addError(errorKey, errorMessage);
//		}
//		catch (Exception e) {
//			addError(errorKey, errorMessage);
//		}
//	}
//	
//	/**
//	 * Validate long.
//	 */
//	protected void validateLong(String field, String errorKey, String errorMessage) {
//		try {
//			String value = controller.getPara(field);
//			Long.parseLong(value);
//		}
//		catch (Exception e) {
//			addError(errorKey, errorMessage);
//		}
//	}
}
