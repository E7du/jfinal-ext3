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
