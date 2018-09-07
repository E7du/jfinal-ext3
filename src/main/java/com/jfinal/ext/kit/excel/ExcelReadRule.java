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

import java.util.HashMap;
import java.util.Map;

public class ExcelReadRule {
	
    private Map<Integer, ReadColumn> columns = new HashMap<Integer, ReadColumn>();

	private Map<Integer, Boolean> hasHeader = new HashMap<Integer, Boolean>();
	
    public Map<Integer, ReadColumn> getColumns() {
        return columns;
    }

    public void setColumns(Map<Integer, ReadColumn> columns) {
        this.columns = columns;
    }
    
    public ReadColumn getColumn(Integer idx) {
    	if (this.columns.containsKey(idx)) {
			return this.columns.get(idx);
		}
    	return null;
    }
    
    public void alignColumn(String... columns) {
    	if (null == columns) {
			return;
		}
    	
    	for (int idx = 0; idx < columns.length; idx++) {
			ReadColumn col = ReadColumn.create(columns[idx]);
			col.setIndex(idx);
	    	this.columns.put(idx, col);
		}
    }

    public void alignColumn(ReadColumn... columns) {
    	if (null == columns) {
			return;
		}
    	for (int idx = 0; idx < columns.length; idx++) {
			ReadColumn col = columns[idx];
			col.index = idx;
	    	this.columns.put(idx, col);
		}
    }
    
	public void setHasHeader(Integer sheetNo, Boolean hasHeder) {
		this.hasHeader.put(sheetNo, hasHeder);
	}
    
	public Boolean getHasHedaer(Integer sheetNo) {
		if (this.hasHeader.containsKey(sheetNo)) {
			return this.hasHeader.get(sheetNo);
		}
		return false;
	}
	
    public static class ReadColumn {

    	private int index;

    	private String attr;
    	
    	private String header;
    	
    	private ReadColumn() {
    		
    	}
    	
    	public static ReadColumn header(String header, String attr) {
    		ReadColumn ReadColumn = new ReadColumn();
            ReadColumn.setAttr(attr);
            ReadColumn.setHeader(header);
            return ReadColumn;
    	}

    	public static ReadColumn create(String attr) {
            ReadColumn ReadColumn = new ReadColumn();
            ReadColumn.setAttr(attr);
            return ReadColumn;
        }
    	
        public int getIndex() {
            return index;
        }

        public void setIndex(int value) {
            this.index = value;
        }

        public String getAttr() {
            return attr;
        }

        public void setAttr(String attr) {
            this.attr = attr;
        }

        public String getHeader() {
			return header;
		}

		public void setHeader(String header) {
			this.header = header;
		}

    }
}
