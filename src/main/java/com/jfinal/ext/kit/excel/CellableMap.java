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

import java.util.LinkedHashMap;
import java.util.Set;

public class CellableMap extends LinkedHashMap<String, Object> implements Cellable {
	
	private static final long serialVersionUID = 5256520140236102824L;

	public String[] getHeaderCellValue() {
		return keySet().toArray(new String[] {});
	}

	public String[] getCellValues() {

		String[] cellValues = new String[size()];
		Set<String> keys = keySet();
		int index = 0;
		for (String key : keys) {
			cellValues[index++] = get(key).toString();
		}
		return cellValues;
	}

}
