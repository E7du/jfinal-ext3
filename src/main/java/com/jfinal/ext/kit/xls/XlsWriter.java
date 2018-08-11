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
package com.jfinal.ext.kit.xls;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.List;
import java.util.Map;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;

import com.google.common.base.Preconditions;
import com.google.common.collect.Lists;
import com.jfinal.ext.kit.TypeKit;
import com.jfinal.ext.plugin.activerecord.ModelExt;
import com.jfinal.log.Log;
import com.jfinal.plugin.activerecord.Model;
import com.jfinal.plugin.activerecord.Record;

public class XlsWriter {

	private Log LOG = Log.getLog(XlsWriter.class);
    private static final int HEADER_ROW = 1;
    private static final int MAX_ROWS = 65535;
    private String[] sheetNames = new String[]{"Sheet"};
    private int cellWidth = 8000;
    private int headerRow;
    private String[][] headers;
    private String[][] columns;
    private List<?>[] data;

    public XlsWriter(List<?>... data) {
        this.data = data;
    }

    public static XlsWriter data(List<?>... data) {
        return new XlsWriter(data);
    }

    public static List<List<?>> dice(List<?> num, int chunkSize) {
        int size = num.size();
        int chunk_num = size / chunkSize + (size % chunkSize == 0 ? 0 : 1);
        List<List<?>> result = Lists.newArrayList();
        for (int i = 0; i < chunk_num; i++) {
            result.add(Lists.newArrayList(num.subList(i * chunkSize, i == chunk_num - 1 ? size : (i + 1) * chunkSize)));
        }
        return result;
    }
    
    public boolean writeToFile(String fileName) {
		try {
			File file = new File(fileName);
			if(!file.exists()) {
				file.createNewFile();
			}
			OutputStream outputStream = new FileOutputStream(file);
			this.write().write(outputStream);
		} catch (IOException e) {
			e.printStackTrace();
			LOG.error(e.getLocalizedMessage());
		}
		return true;
    }

    public Workbook write() {
        Preconditions.checkNotNull(data, "data can not be null");
        Preconditions.checkNotNull(headers, "headers can not be null");
        Preconditions.checkNotNull(columns, "columns can not be null");
        Preconditions.checkArgument(data.length == sheetNames.length && sheetNames.length == headers.length
                && headers.length == columns.length, "data,sheetNames,headers and columns'length should be the same." +
                "(data:" + data.length + ",sheetNames:" + sheetNames.length + ",headers:" + headers.length + ",columns:" + columns.length + ")");
        Preconditions.checkArgument(cellWidth >= 0, "cellWidth can not be less than 0");
        
        Workbook wb = new HSSFWorkbook();
        if (data.length > 1) {
            for (int i = 0; i < data.length; i++) {
                List<?> item = data[i];
                Preconditions.checkArgument(item.size() < MAX_ROWS, "Data [" + i + "] is invalid:invalid data size (" + item.size() + ") outside allowable range (0..65535)");
            }
        } else if (data.length == 1 && data[0].size() > MAX_ROWS) {
            data = dice(data[0], MAX_ROWS).toArray(new List<?>[]{});
            String sheetName = sheetNames[0];
            sheetNames = new String[data.length];
            for (int i = 0; i < data.length; i++) {
                sheetNames[i] = sheetName + (i == 0 ? "" : (i + 1));
            }
            String[] header = headers[0];
            headers = new String[data.length][];
            for (int i = 0; i < data.length; i++) {
                headers[i] = header;
            }
            String[] column = columns[0];
            columns = new String[data.length][];
            for (int i = 0; i < data.length; i++) {
                columns[i] = column;
            }
        }
        
        if (data.length == 0) {
            return wb;
        }
        
        for (int i = 0; i < data.length; i++) {
            Sheet sheet = wb.createSheet(sheetNames[i]);
            Row row;
            Cell cell;
            if (headers[i].length > 0) {
                row = sheet.createRow(0);
                if (headerRow <= 0) {
                    headerRow = HEADER_ROW;
                }
                headerRow = Math.min(headerRow, MAX_ROWS);
                for (int h = 0, lenH = headers[i].length; h < lenH; h++) {
                    if (cellWidth > 0) {
                        sheet.setColumnWidth(h, cellWidth);
                    }
                    cell = row.createCell(h);
                    cell.setCellValue(headers[i][h]);
                }
            }

            for (int j = 0, len = data[i].size(); j < len; j++) {
                row = sheet.createRow(j + headerRow);
                Object obj = data[i].get(j);
                if (obj == null) {
                    continue;
                }
                if (obj instanceof Map) {
                    processAsMap(columns[i], row, obj);
                } else if (obj instanceof Model) {
                    processAsModel(columns[i], row, obj);
                } else if (obj instanceof Record) {
                    processAsRecord(columns[i], row, obj);
                } else {
                    throw new RuntimeException("Not support type[" + obj.getClass() + "]");
                }
            }
        }
        return wb;
    }

    @SuppressWarnings("unchecked")
    private static void processAsMap(String[] columns, Row row, Object obj) {
        Cell cell = null;
        Map<String, Object> map = (Map<String, Object>) obj;
        for (int j = 0, len = columns.length; j < len; j++) {
        	cell = row.createCell(j);
            Object val = map.get(columns[j]);
            if (null == val) {
                cell.setCellValue("");
                continue;
			}
            if (TypeKit.isNumeric(val)) {
				cell.setCellType(CellType.NUMERIC);
				cell.setCellValue(Double.valueOf(val.toString()));
				continue;
			}
            cell.setCellValue(val + "");
        }
    }

    private static void processAsModel(String[] columns, Row row, Object obj) {
        ModelExt<?> model = (ModelExt<?>) obj;
        XlsWriter.processAsMap(columns, row, model.attrs());
    }

    private static void processAsRecord(String[] columns, Row row, Object obj) {
        Record record = (Record) obj;
        XlsWriter.processAsMap(columns, row, record.getColumns());
    }

    public XlsWriter sheetName(String sheetName) {
        this.sheetNames = new String[]{sheetName};
        return this;
    }

    public XlsWriter sheetNames(String... sheetName) {
        this.sheetNames = sheetName;
        return this;
    }

    public XlsWriter cellWidth(int cellWidth) {
        this.cellWidth = cellWidth;
        return this;
    }

    public XlsWriter headerRow(int headerRow) {
        this.headerRow = headerRow;
        return this;
    }

    public XlsWriter header(String... header) {
        this.headers = new String[][]{header};
        return this;
    }

    public XlsWriter headers(String[]... headers) {
        this.headers = headers;
        return this;
    }

    public XlsWriter column(String... column) {
        this.columns = new String[][]{column};
        return this;
    }

    public XlsWriter columns(String[]... columns) {
        this.columns = columns;
        return this;
    }

}
