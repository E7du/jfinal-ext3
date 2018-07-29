/*
 * Copyright 2018 Jobsz
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

import java.math.BigDecimal;

public final class TypeKit {
	
	public static boolean isInteger(Object obj) {
		return TypeKit.isNumeric(obj);
	}
	
	public static boolean isDouble(Object obj) {
		return TypeKit.isNumeric(obj);
	}
	
	public static boolean isFloat(Object obj) {
		return TypeKit.isNumeric(obj);
	}
	
	public static boolean isLong(Object obj) {
		return TypeKit.isNumeric(obj);
	}
	
	public static boolean isNumeric(Object obj) {
		if (null == obj) {
			return false;
		}
        try {
            new BigDecimal(obj.toString());
        } catch (Exception e) {
            return false;
        }
        return true;
//		Pattern pattern = Pattern.compile("-?[0-9]+(\\.[0-9]+)?");
//        Matcher isNum = pattern.matcher(obj.toString());
//        if (!isNum.matches()) {
//            return false;
//        }
//        return true;
	}
	
	public static boolean isString(Object obj) {
		return !TypeKit.isNumeric(obj);
	}
}
