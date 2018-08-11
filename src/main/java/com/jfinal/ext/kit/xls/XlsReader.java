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
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.time.DateFormatUtils;
import org.apache.poi.hssf.usermodel.HSSFDateUtil;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;

import com.google.common.collect.Lists;
import com.jfinal.ext.kit.Reflect;
import com.jfinal.ext.kit.poi.PoiException;
import com.jfinal.ext.plugin.activerecord.ModelExt;
import com.jfinal.kit.StrKit;
import com.jfinal.log.Log;

public class XlsReader {

	private static final Log LOG = Log.getLog(XlsReader.class);
	
	public static List<List<List<Object>>> readXls(File file, XlsReadRule xlsReadRule) {
        int start = xlsReadRule.getStart();
        int end = xlsReadRule.getEnd();
        List<List<List<Object>>> xlsDatas = Lists.newArrayList();
        Workbook wb = null;
        try {
            wb = WorkbookFactory.create(file);
        } catch (Exception e) {
        	LOG.error(e.getLocalizedMessage());
            throw new PoiException(e);
        }
        
        String dateFormat = xlsReadRule.getDateFormat();
        
        for (int i = 0; i < wb.getNumberOfSheets(); i++) {
            Sheet sheet = wb.getSheetAt(i);
            List<List<Object>> sheetList = Lists.newArrayList();
            int rows = sheet.getLastRowNum();
            if (start <= sheet.getFirstRowNum()) {
                start = sheet.getFirstRowNum();
            }
            if (end >= rows) {
                end = rows;
            } else if (end <= 0) {
                end = rows + end;
            }
            for (int rowIndex = start; rowIndex <= end; rowIndex++) {
                Row row = sheet.getRow(rowIndex);
                List<Object> columns = Lists.newArrayList();
                int cellNum = row.getLastCellNum();
                for (int cellIndex = row.getFirstCellNum(); cellIndex < cellNum; cellIndex++) {
                    Cell cell = row.getCell(cellIndex);
                    if (null == cell) {
						continue;
					}
                    CellType cellType = cell.getCellTypeEnum();
                    Object column = null;
                    if (CellType.NUMERIC.equals(cellType)) {
                    	if (HSSFDateUtil.isCellDateFormatted(cell)) {
                    		Date date = cell.getDateCellValue();
                    		column = DateFormatUtils.format(date, dateFormat);
						} else {
							column = cell.getNumericCellValue();
						}
					} else if (CellType.STRING.equals(cellType)) {
                        column = cell.getStringCellValue();
					} else if (CellType.BOOLEAN.equals(cellType)) {
                        column = cell.getBooleanCellValue();
					} else if (CellType.FORMULA.equals(cellType)) {
                        column = cell.getCellFormula();
					} else if (CellType.ERROR.equals(cellType)) {
						column = cell.getErrorCellValue();
					} else if (CellType.BLANK.equals(cellType)) {
						column = "";
					}
                    columns.add(column);
                }

                List<Boolean> rowFilterFlagList = Lists.newArrayList();
                List<RowFilter> rowFilterList = Lists.newArrayList();
                for (int k = 0; k < rowFilterList.size(); k++) {
                    RowFilter rowFilter = rowFilterList.get(k);
                    rowFilterFlagList.add(rowFilter.doFilter(rowIndex, columns));
                }
                if (!rowFilterFlagList.contains(false)) {
                    sheetList.add(columns);
                }
            }
            xlsDatas.add(sheetList);
        }
        return xlsDatas;
    }

    public static List<List<Object>> read(File file, XlsReadRule xlsReadRule) {
        return readXls(file, xlsReadRule).get(0);
    }

    public static List<ModelExt<?>> readToModel(File file, XlsReadRule xlsReadRule) {
        List<List<Object>> srcList = read(file, xlsReadRule);
        List<ModelExt<?>> xlsDatass = Lists.newArrayList();
        for (int i = 0; i < srcList.size(); i++) {
            List<Object> list = srcList.get(i);
            ModelExt<?> model = toModel(xlsReadRule.getClazz(), list, xlsReadRule);
            xlsDatass.add(model);
        }
        return xlsDatass;
    }
    
    public static <T extends ModelExt<T>> List<T> readToModel(Class<? extends ModelExt<?>> clazz, File file, XlsReadRule xlsReadRule) {
        List<List<Object>> srcList = read(file, xlsReadRule);
        List<T> xlsDatass = Lists.newArrayList();
        for (int i = 0; i < srcList.size(); i++) {
            List<Object> list = srcList.get(i);
            T model = toModel(xlsReadRule.getClazz(), list, xlsReadRule);
            xlsDatass.add(model);
        }
        return xlsDatass;
    }

	private static <T extends ModelExt<T>> T toModel(Class<? extends ModelExt<?>> clazz, List<Object> list, XlsReadRule xlsReadRule) {
		T model = Reflect.on(clazz).create().get();
        Object[] values = list.toArray(new Object[]{});
        String message = "";
        for (int i = 0; i < values.length; i++) {
            Object value = values[i];
            XlsReadRule.Column column = alignCell(xlsReadRule, i);
            if (null == column) {
				continue;
			}
            String name = column.getAttr();
            ColumnValidate columnValidate = column.getValidate();
            boolean valid = true;
            if (null != columnValidate) {
                valid = columnValidate.validate(value);
                if (!valid) {
                    message = message + "value(" + value + ") is invalid in column " + column.getIndex() + "</br>";
                }
            }
            if (valid) {
                Object convertedValue = value;
                ColumnConvert columnConvert = column.getConvert();
                if (null != columnConvert) {
                    convertedValue = columnConvert.convert(value, model);
                }
                model.set(name, convertedValue);
            }
        }
        if (StrKit.notBlank(message)) {
        	LOG.error(message);
            throw new PoiException(message);
        }
        return model;
    }

    private static XlsReadRule.Column alignCell(XlsReadRule xlsReadRule, int index) {
        Map<Integer, XlsReadRule.Column> columns = xlsReadRule.getColumns();
        XlsReadRule.Column column = columns.get(index);
        return column;
    }
}
